package com.study.proudcat.domain.post.repository;

import com.study.proudcat.domain.post.dto.request.FindPostRequest;
import com.study.proudcat.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> findAllByHearts();

    Page<Post> findAllPostsPage(FindPostRequest request,Pageable pageable);
}
