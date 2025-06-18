package com.growit.app.user.infrastructure.persistence.jobrole.source;
import com.growit.app.user.infrastructure.persistence.jobrole.source.entity.JobRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DBJobRoleRepository extends JpaRepository<JobRoleEntity, String> {
}
