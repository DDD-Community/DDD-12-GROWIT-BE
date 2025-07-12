package com.growit.app.fake.resource;

import com.growit.app.resource.domain.saying.Saying;

public class SayingFixture {

  public static Saying defaultSaying() {
    return new SayingBuilder().build();
  }

  public static Saying customSaying(String id, String message, String from) {
    return new SayingBuilder()
        .id(id != null ? id : "custom-id")
        .message(message != null ? message : "기본 메시지")
        .from(from != null ? from : "기본 작성자")
        .build();
  }
}

class SayingBuilder {
  private String id = "saying-001";
  private String message = "성공은 매일 반복되는 작은 노력들의 합이다냥!";
  private String from = "그로냥";

  public SayingBuilder id(String id) {
    this.id = id;
    return this;
  }

  public SayingBuilder message(String message) {
    this.message = message;
    return this;
  }

  public SayingBuilder from(String from) {
    this.from = from;
    return this;
  }

  public Saying build() {
    return Saying.builder().id(id).message(message).from(from).build();
  }
}
