package com.growit.app.resource.infrastructure.discord.dto;

import java.util.List;

public record DiscordEmbed(
    String title, String description, String timestamp, List<DiscordField> fields) {
  public static DiscordEmbed of(
      String title, String description, String timestamp, List<DiscordField> fields) {
    return new DiscordEmbed(title, description, timestamp, fields);
  }
}
