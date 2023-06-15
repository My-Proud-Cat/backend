package com.study.proudcat.domain.post.repository;

import com.study.proudcat.domain.post.dto.request.FindPostRequest;
import com.study.proudcat.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<Post> findAllPostsPage(FindPostRequest request, Pageable pageable);
}
