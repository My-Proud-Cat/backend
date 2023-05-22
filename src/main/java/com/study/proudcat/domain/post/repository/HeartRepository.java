package com.study.proudcat.domain.post.repository;

import com.study.proudcat.domain.post.entity.Heart;
import com.study.proudcat.domain.post.entity.Post;
import com.study.proudcat.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    Optional<Heart> findHeartByPostAndUser(Post post, User user);
}
