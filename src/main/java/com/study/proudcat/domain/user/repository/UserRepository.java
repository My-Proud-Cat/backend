package com.study.proudcat.domain.user.repository;

import com.study.proudcat.domain.user.entity.Provider;
import com.study.proudcat.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndProvider(String email, Provider provider);

    boolean existsByEmailAndProvider(String email, Provider provider);

    boolean existsByNickname(String nickname);
}
