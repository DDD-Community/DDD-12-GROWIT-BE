package com.growit.app.resource.domain.saying.repository;

import com.growit.app.resource.domain.saying.Saying;
import java.util.List;

public interface SayingRepository {
  List<Saying> findAll();
}
