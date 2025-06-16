package com.growit.app.common.dto;

public record Response<T>(T data) {
  public static <T> Response<T> ok(T data) {
    return new Response<>(data);
  }

  public static <T> Response<T> fail() {
    return new Response<>(null);
  }
}
