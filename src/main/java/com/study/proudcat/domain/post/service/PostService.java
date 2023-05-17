package com.study.proudcat.domain.post.service;

import com.study.proudcat.domain.post.dto.request.ModifyPostRequest;
import com.study.proudcat.domain.post.dto.request.WritePostRequest;
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
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void writePost(WritePostRequest request) {
        log.info("PostService writePost run..");
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post post = request.toEntity(user);
        log.info("Post : {}", post);
        postRepository.save(request.toEntity(user));
    }

    @Transactional(readOnly = true)
    public FindPostsResponse getAllPosts() {
        log.info("PostService getAllPosts run..");
        List<FindPostResponse> posts = postRepository.findAll()
                .stream()
                .map(FindPostResponse::from)
                .toList();

        return new FindPostsResponse(posts);
    }

    @Transactional(readOnly = true)
    public FindPostResponse getPostById(Long postId) {
        log.info("PostService getPostById run..");
        Post post = getPostEntity(postId);

        return FindPostResponse.from(post);
    }

    @Transactional
    public void modifyPost(Long postId, ModifyPostRequest request) {
        log.info("PostService modifyPost run..");
        Post post = getPostEntity(postId);
        if (!post.isSameWriter(request.getEmail())) {
            throw new IllegalArgumentException("You are not the writer of this post");
        }

        post.modify(request.getTitle(), request.getDescribe());
    }

    @Transactional
    public void deletePost(Long postId) {
        log.info("PostService deletePost run..");
        Post post = getPostEntity(postId);

        post.delete();
    }

    private Post getPostEntity(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }
}
