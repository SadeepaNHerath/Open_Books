package edu.book.socialnetwork.repository;

import edu.book.socialnetwork.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Integer> {
    Optional<TokenEntity> findByToken(String token);
}
