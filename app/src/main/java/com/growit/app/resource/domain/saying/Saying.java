package com.growit.app.resource.domain.saying;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Saying {
  private String id;
  private String message;
  private String author;
}
