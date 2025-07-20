package com.growit.app.resource.domain.saying.repository;

import com.growit.app.resource.domain.saying.Saying;
import java.util.List;
import java.util.Optional;

public interface SayingRepository {
  List<Saying> findAll();

  Optional<Saying> findById(String id);

  void save(Saying saying);
}
