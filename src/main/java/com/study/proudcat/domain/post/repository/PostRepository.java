package com.study.proudcat.domain.post.repository;

import com.study.proudcat.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
