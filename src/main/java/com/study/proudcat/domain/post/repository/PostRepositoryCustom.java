package com.study.proudcat.domain.post.repository;

import com.study.proudcat.domain.post.entity.Post;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> findAllByHearts();
}
