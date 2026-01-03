package com.growit.app.advice.domain.chatadvice.service;

import com.growit.app.advice.domain.chatadvice.ChatAdvice;

public interface ChatAdviceValidator {
  void validateCanSendMessage(ChatAdvice chatAdvice);

  void validateConversation(String userMessage, Integer week);
}
