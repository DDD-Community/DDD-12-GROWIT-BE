package com.growit.app.user.domain.repository;

import com.growit.app.user.domain.entity.UserEntity;
import com.growit.app.user.domain.vo.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID>, UserQueryRepository {
    boolean existsByEmail(Email email);
}
