package com.growit.app.user.domain.repository;

import com.growit.app.user.domain.entity.UserEntity;
import com.growit.app.user.domain.vo.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

  Optional<UserEntity> findByEmail(Email email);

  boolean existsByEmail(com.growit.app.user.domain.vo.Email email);
}
