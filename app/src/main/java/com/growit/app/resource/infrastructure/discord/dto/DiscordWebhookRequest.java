package com.growit.app.resource.infrastructure.discord.dto;

import java.util.List;

public record DiscordWebhookRequest(String content, List<DiscordEmbed> embeds) {
  public static DiscordWebhookRequest of(String content, List<DiscordEmbed> embeds) {
    return new DiscordWebhookRequest(content, embeds);
  }
}
