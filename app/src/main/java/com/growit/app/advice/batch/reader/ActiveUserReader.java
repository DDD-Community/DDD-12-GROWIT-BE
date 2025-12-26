package com.growit.app.advice.batch.reader;

import com.growit.app.advice.domain.chatadvice.ChatAdvice;
import com.growit.app.advice.domain.chatadvice.repository.ChatAdviceRepository;
import com.growit.app.user.infrastructure.persistence.user.source.DBUserRepository;
import com.growit.app.user.infrastructure.persistence.user.source.entity.UserEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

@Slf4j
@RequiredArgsConstructor
public class ActiveUserReader implements ItemReader<UserEntity> {

  private final ChatAdviceRepository chatAdviceRepository;
  private final DBUserRepository dbUserRepository;
  private final java.time.Clock clock;

  private Iterator<UserEntity> userIterator;

  @Override
  public UserEntity read() {
    if (userIterator == null) {
      userIterator = fetchActiveUsers().iterator();
    }

    if (userIterator.hasNext()) {
      return userIterator.next();
    } else {
      return null; // End of data
    }
  }

  private List<UserEntity> fetchActiveUsers() {
    LocalDate yesterday = LocalDate.now(clock).minusDays(1);
    LocalDateTime start = LocalDateTime.of(yesterday, LocalTime.MIN);
    LocalDateTime end = LocalDateTime.of(yesterday, LocalTime.MAX);

    List<ChatAdvice> activeChatAdvices =
        chatAdviceRepository.findByLastConversatedAtBetween(start, end);

    if (activeChatAdvices.isEmpty()) {
      return Collections.emptyList();
    }

    List<String> userIds =
        activeChatAdvices.stream().map(ChatAdvice::getUserId).collect(Collectors.toList());

    return dbUserRepository.findAllByUidIn(userIds);
  }
}
