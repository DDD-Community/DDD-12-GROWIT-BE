package com.growit.app.resource.infrastructure.persistence.saying;

import com.growit.app.resource.domain.saying.Saying;
import com.growit.app.resource.domain.saying.repository.SayingRepository;
import com.growit.app.resource.infrastructure.persistence.saying.source.DBSayingRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class SayingRepositoryImpl implements SayingRepository {
  private final DBSayingRepository dbSayingRepository;
  private final SayingDBMapper sayingDBMapper;

  @Override
  public List<Saying> findAll() {
    return dbSayingRepository.findAll().stream().map(sayingDBMapper::toDomain).toList();
  }
}
