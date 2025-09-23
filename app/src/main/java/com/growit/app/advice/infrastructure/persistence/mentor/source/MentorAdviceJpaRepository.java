package com.growit.app.advice.infrastructure.persistence.mentor.source;

import com.growit.app.advice.infrastructure.persistence.mentor.source.entity.MentorAdviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MentorAdviceJpaRepository extends JpaRepository<MentorAdviceEntity, Long>, MentorAdviceCustomRepository {

}
