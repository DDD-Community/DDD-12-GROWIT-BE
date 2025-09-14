package com.growit.app.resource.usecase;

import com.growit.app.resource.infrastructure.discord.DiscordWebhookClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateInvitationUseCase {
  private final DiscordWebhookClient discordWebhookClient;

  public void execute(String phone) {
    discordWebhookClient.sendInvitation(phone);
  }
}
