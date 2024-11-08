package edu.book.socialnetwork.domain.user.repository;

import edu.book.socialnetwork.domain.user.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    Optional<RoleEntity> findByName(String role);
}
