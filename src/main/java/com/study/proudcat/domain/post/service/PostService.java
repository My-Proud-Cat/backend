package com.study.proudcat.domain.post.service;

import com.study.proudcat.domain.post.dto.request.WritePostReqeust;
import com.study.proudcat.domain.post.dto.response.FindPostResponse;
import com.study.proudcat.domain.post.dto.response.FindPostsResponse;
import com.study.proudcat.domain.post.entity.Post;
import com.study.proudcat.domain.post.repository.PostRepository;
import com.study.proudcat.domain.user.entity.User;
import com.study.proudcat.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void writePost(WritePostReqeust request) {
        log.info("PostService writePost run..");
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post post = request.toEntity(user);
        log.info("Post : {}", post);
        postRepository.save(request.toEntity(user));
    }

    @Transactional
    public FindPostsResponse getAllPosts() {
        log.info("PostService getAllPosts run..");
        List<FindPostResponse> posts = postRepository.findAll()
                .stream()
                .map(FindPostResponse::from)
                .toList();

        return new FindPostsResponse(posts);
    }

    @Transactional
    public FindPostResponse getPostById(Long postId) {
        log.info("PostService getPostById run..");
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        return FindPostResponse.from(post);
    }
}
