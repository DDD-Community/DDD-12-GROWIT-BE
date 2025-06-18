package com.growit.app.user.infrastructure.service.persistence.user.source;

import com.growit.app.user.infrastructure.service.persistence.user.source.entity.UserEntity;
import com.growit.app.user.domain.user.vo.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DBUserRepository extends JpaRepository<UserEntity, UUID>, UserQueryRepository {
    boolean existsByEmail(Email email);
}
