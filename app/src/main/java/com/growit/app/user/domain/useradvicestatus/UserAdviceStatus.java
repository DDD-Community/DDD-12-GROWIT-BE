package com.growit.app.user.domain.useradvicestatus;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor()
@AllArgsConstructor
public class UserAdviceStatus {
  private String userId;
  private LocalDate lastSeenDate;
}
