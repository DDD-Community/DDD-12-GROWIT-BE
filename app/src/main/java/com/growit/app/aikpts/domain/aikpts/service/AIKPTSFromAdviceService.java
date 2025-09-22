package com.growit.app.aikpts.domain.aikpts.service;

import com.growit.app.ai.domain.event.AIAdviceResponseEvent;
import com.growit.app.aikpts.domain.aikpts.AIKPTS;
import com.growit.app.aikpts.domain.aikpts.AIKPTSRepository;
import com.growit.app.aikpts.domain.aikpts.vo.KPT;
import com.growit.app.common.util.IDGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIKPTSFromAdviceService {
  
  private final AIKPTSRepository aikptsRepository;

  public String upsertAIKPTSFromAdvice(AIAdviceResponseEvent adviceEvent) {
    log.info("=== AIKPTS 파싱 시작 ===");
    log.info("AdviceEvent - Success: {}, AdviceId: {}", adviceEvent.isSuccess(), adviceEvent.getAdviceId());
    
    if (!adviceEvent.isSuccess() || adviceEvent.getOutput() == null) {
      log.error("Invalid advice event: {}", adviceEvent.getError());
      throw new IllegalArgumentException("Invalid advice event: " + adviceEvent.getError());
    }

    AIAdviceResponseEvent.AdviceOutput output = adviceEvent.getOutput();
    
    log.info("=== Nest 서버 원본 응답 ===");
    log.info("원본 Keeps: [{}]", output.getKeeps());
    log.info("원본 Problems: [{}]", output.getProblems());
    log.info("원본 Trys: [{}]", output.getTrys());
    
    // String을 List로 변환 (줄바꿈이나 구분자로 분리)
    List<String> keeps = parseAdviceText(output.getKeeps());
    List<String> problems = parseAdviceText(output.getProblems());
    List<String> trys = parseAdviceText(output.getTrys());

    log.info("=== 파싱된 KPT 데이터 ===");
    log.info("파싱된 Keeps ({}개): {}", keeps.size(), keeps);
    log.info("파싱된 Problems ({}개): {}", problems.size(), problems);
    log.info("파싱된 Trys ({}개): {}", trys.size(), trys);

    KPT kpt = KPT.builder()
        .keeps(keeps)
        .problems(problems)
        .trys(trys)
        .build();

    // AIAdviceId로 기존 활성 AIKPTS 조회
    List<AIKPTS> existingAIKPTS = aikptsRepository.findActiveByAiAdviceId(adviceEvent.getAdviceId());
    
    AIKPTS aikpts;
    if (!existingAIKPTS.isEmpty()) {
      // 기존 데이터가 있으면 업데이트
      AIKPTS existing = existingAIKPTS.get(0);
      aikpts = existing.updateFromAdvice(kpt);
      log.info("Updating existing AIKPTS: id={}, adviceId={}", aikpts.getId(), adviceEvent.getAdviceId());
    } else {
      // 기존 데이터가 없으면 새로 생성
      aikpts = AIKPTS.fromAdvice(IDGenerator.generateId(), adviceEvent.getAdviceId(), kpt);
      log.info("Creating new AIKPTS: id={}, adviceId={}", aikpts.getId(), adviceEvent.getAdviceId());
    }

    // 도메인 검증
    if (!aikpts.isValid()) {
      log.error("Invalid AIKPTS created: {}", aikpts);
      throw new IllegalArgumentException("Invalid AIKPTS data");
    }

    aikptsRepository.save(aikpts);
    return aikpts.getId();
  }

  private List<String> parseAdviceText(String text) {
    log.debug("parseAdviceText 입력: [{}]", text);
    
    if (text == null || text.trim().isEmpty()) {
      log.debug("텍스트가 null이거나 비어있음");
      return List.of();
    }
    
    // 다양한 구분자로 분리 시도 (줄바꿈, 세미콜론, 쉼표, 대시 등)
    String[] splitText;
    if (text.contains("\n")) {
      splitText = text.split("\\n");
      log.debug("줄바꿈으로 분리");
    } else if (text.contains(";")) {
      splitText = text.split(";");
      log.debug("세미콜론으로 분리");
    } else if (text.contains(",")) {
      splitText = text.split(",");
      log.debug("쉼표로 분리");
    } else if (text.contains(" - ") || text.contains("- ")) {
      // 공백과 함께 있는 대시만 구분자로 처리
      splitText = text.split("\\s*-\\s*");
      log.debug("대시로 분리 (공백 포함)");
    } else {
      // 구분자가 없으면 전체를 하나의 항목으로 처리
      splitText = new String[]{text};
      log.debug("구분자 없음, 전체를 하나의 항목으로 처리");
    }
    
    log.debug("분리된 배열: {}", Arrays.toString(splitText));
    
    List<String> result = Arrays.stream(splitText)
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .filter(s -> !s.equals("-")) // 대시만 있는 경우 제거
        .filter(s -> !s.equals("•")) // 불릿 포인트만 있는 경우 제거
        .filter(s -> !s.equals("*")) // 별표만 있는 경우 제거
        .map(s -> s.replaceAll("^[•*\\-\\s]+", "")) // 앞의 불릿, 별표, 대시, 공백 제거
        .filter(s -> !s.isEmpty())
        .toList();
        
    log.debug("최종 파싱 결과: {}", result);
    return result;
  }
}
