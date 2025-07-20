package com.growit.app.resource.usecase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.growit.app.resource.domain.saying.command.SyncSayingsCommand;
import com.growit.app.resource.domain.saying.repository.SayingRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SyncSayingsUseCaseTest {

  @Mock private SayingRepository sayingRepository;

  @InjectMocks private SyncSayingsUseCase syncSayingsUseCase;

  @Test
  void should_sync_sayings_successfully() {
    // given
    List<SyncSayingsCommand.SayingData> sayingData =
        Arrays.asList(
            new SyncSayingsCommand.SayingData("saying1", "테스트 격언 1", "그로냥"),
            new SyncSayingsCommand.SayingData("saying2", "테스트 격언 2", "그로냥"));
    SyncSayingsCommand command = new SyncSayingsCommand(sayingData);

    // when
    syncSayingsUseCase.execute(command);

    // then
    verify(sayingRepository).syncAll(any(List.class));
  }
}
