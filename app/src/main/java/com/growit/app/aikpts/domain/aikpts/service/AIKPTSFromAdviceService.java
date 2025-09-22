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
    log.info("AIKPTS 파싱 시작 - Success: {}, AdviceId: {}", adviceEvent.isSuccess(), adviceEvent.getAdviceId());
    
    if (!adviceEvent.isSuccess() || adviceEvent.getOutput() == null) {
      log.error("Invalid advice event: {}", adviceEvent.getError());
      throw new IllegalArgumentException("Invalid advice event: " + adviceEvent.getError());
    }

    AIAdviceResponseEvent.AdviceOutput output = adviceEvent.getOutput();
    
    List<String> keeps = parseAdviceText(output.getKeeps());
    List<String> problems = parseAdviceText(output.getProblems());
    List<String> trys = parseAdviceText(output.getTrys());

    KPT kpt = KPT.builder()
        .keeps(keeps)
        .problems(problems)
        .trys(trys)
        .build();

    List<AIKPTS> existingAIKPTS = aikptsRepository.findActiveByAiAdviceId(adviceEvent.getAdviceId());
    
    AIKPTS aikpts;
    if (!existingAIKPTS.isEmpty()) {
      AIKPTS existing = existingAIKPTS.get(0);
      aikpts = existing.updateFromAdvice(kpt);
      log.info("Updating existing AIKPTS: id={}, adviceId={}", aikpts.getId(), adviceEvent.getAdviceId());
    } else {
      aikpts = AIKPTS.fromAdvice(IDGenerator.generateId(), adviceEvent.getAdviceId(), kpt);
      log.info("Creating new AIKPTS: id={}, adviceId={}", aikpts.getId(), adviceEvent.getAdviceId());
    }

    if (!aikpts.isValid()) {
      log.error("Invalid AIKPTS created: {}", aikpts);
      throw new IllegalArgumentException("Invalid AIKPTS data");
    }

    aikptsRepository.save(aikpts);
    return aikpts.getId();
  }

  private List<String> parseAdviceText(String text) {
    if (text == null || text.trim().isEmpty()) {
      return List.of();
    }
    
    String[] splitText;
    if (text.contains("\n")) {
      splitText = text.split("\\n");
    } else if (text.contains(";")) {
      splitText = text.split(";");
    } else if (text.contains(",")) {
      splitText = text.split(",");
    } else if (text.contains(" - ") || text.contains("- ")) {
      splitText = text.split("\\s*-\\s*");
    } else {
      splitText = new String[]{text};
    }
    
    return Arrays.stream(splitText)
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .filter(s -> !s.equals("-"))
        .filter(s -> !s.equals("•"))
        .filter(s -> !s.equals("*"))
        .map(s -> s.replaceAll("^[•*\\-\\s]+", ""))
        .filter(s -> !s.isEmpty())
        .toList();
  }
}
