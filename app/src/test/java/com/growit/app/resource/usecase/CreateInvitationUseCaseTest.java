package com.growit.app.resource.usecase;

import static org.mockito.Mockito.*;

import com.growit.app.resource.infrastructure.discord.DiscordWebhookClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateInvitationUseCaseTest {

  @Mock private DiscordWebhookClient discordWebhookClient;

  @InjectMocks private CreateInvitationUseCase useCase;

  @Test
  void givenValidPhone_whenExecute_thenSendDiscordWebhook() {
    // given
    String phone = "010-1234-5678";

    // when
    useCase.execute(phone);

    // then
    verify(discordWebhookClient, times(1)).sendInvitation(phone);
  }
}
