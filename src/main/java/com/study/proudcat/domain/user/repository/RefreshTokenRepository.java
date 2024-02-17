package com.study.proudcat.domain.user.repository;

import com.study.proudcat.domain.user.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByRefresh(String refreshToken);
}
