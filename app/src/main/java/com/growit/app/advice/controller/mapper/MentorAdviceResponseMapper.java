package com.growit.app.advice.controller.mapper;

import com.growit.app.advice.controller.dto.response.MentorAdviceResponse;
import com.growit.app.advice.domain.mentor.MentorAdvice;
import org.springframework.stereotype.Component;

@Component
public class MentorAdviceResponseMapper {
  public MentorAdviceResponse toResponse(MentorAdvice mentorAdvice) {
    MentorAdviceResponse.KptResponse kpt =
        new MentorAdviceResponse.KptResponse(
            mentorAdvice.getKpt().getKeep(),
            mentorAdvice.getKpt().getProblem(),
            mentorAdvice.getKpt().getTryNext());

    return new MentorAdviceResponse(mentorAdvice.isChecked(), mentorAdvice.getMessage(), kpt);
  }
}
