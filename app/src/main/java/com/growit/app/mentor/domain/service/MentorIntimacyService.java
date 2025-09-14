package com.growit.app.mentor.domain.service;

import org.springframework.stereotype.Service;

@Service
public class MentorIntimacyService {

  public void validateUserId(String userId) {
    if (userId == null || userId.trim().isEmpty()) {
      throw new IllegalArgumentException("사용자 ID는 필수입니다.");
    }
  }
}
