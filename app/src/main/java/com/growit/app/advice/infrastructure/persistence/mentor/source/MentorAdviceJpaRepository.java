package com.growit.app.advice.infrastructure.persistence.mentor.source;

import com.growit.app.advice.infrastructure.persistence.mentor.source.entity.MentorAdviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MentorAdviceJpaRepository
    extends JpaRepository<MentorAdviceEntity, Long>, MentorAdviceCustomRepository {}
