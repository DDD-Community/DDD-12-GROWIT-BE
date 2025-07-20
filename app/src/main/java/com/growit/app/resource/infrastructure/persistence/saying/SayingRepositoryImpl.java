package com.growit.app.resource.infrastructure.persistence.saying;

import com.growit.app.resource.domain.saying.Saying;
import com.growit.app.resource.domain.saying.repository.SayingRepository;
import com.growit.app.resource.infrastructure.persistence.saying.source.DBSayingRepository;
import com.growit.app.resource.infrastructure.persistence.saying.source.entity.SayingEntity;
import java.util.List;
import java.util.Optional;
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

  @Override
  public Optional<Saying> findById(String id) {
    return dbSayingRepository.findByUid(id).map(sayingDBMapper::toDomain);
  }

  @Override
  public void save(Saying saying) {
    Optional<SayingEntity> existing = dbSayingRepository.findByUid(saying.getId());
    SayingEntity entity;
    if (existing.isPresent()) {
      entity = existing.get();
      entity.updateByDomain(saying);
    } else {
      entity = sayingDBMapper.toEntity(saying);
    }
    dbSayingRepository.save(entity);
  }
}
