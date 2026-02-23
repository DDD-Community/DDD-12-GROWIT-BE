package com.growit.app.resource.controller.mapper;

import com.growit.app.resource.controller.dto.response.SayingResponse;
import com.growit.app.resource.domain.saying.Saying;
import org.springframework.stereotype.Component;

@Component
public class ResourceResponseMapper {

  public SayingResponse toSayingResponse(Saying saying) {
    return new SayingResponse(saying.getMessage(), saying.getAuthor());
  }
}
