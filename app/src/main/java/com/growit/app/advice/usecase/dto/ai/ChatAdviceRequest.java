package com.growit.app.advice.usecase.dto.ai;
 
import java.util.List;
import lombok.Builder;
import lombok.Getter;
 
@Getter
@Builder
public class ChatAdviceRequest {
  private String userId;
  private String goalId;
  private String goalTitle;
  private String concern;
  private String mode;
  private List<String> recentTodos;
}
