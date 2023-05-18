package com.study.proudcat.domain.comment.repository;

import com.study.proudcat.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
