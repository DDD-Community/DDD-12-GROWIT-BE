package com.growit.app.resource.infrastructure.discord.dto;

public record DiscordField(String name, String value, boolean inline) {
  public static DiscordField of(String name, String value, boolean inline) {
    return new DiscordField(name, value, inline);
  }

  public static DiscordField of(String name, String value) {
    return new DiscordField(name, value, false);
  }
}
