package com.growit.app.advice.domain.mentor.service;

import com.growit.app.advice.domain.mentor.MentorAdvice;

public interface MentorService {
  MentorAdvice getMentorAdvice(String userId, String goalId);
}
