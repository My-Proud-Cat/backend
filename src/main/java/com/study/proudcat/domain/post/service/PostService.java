package com.study.proudcat.domain.post.service;

import com.study.proudcat.domain.post.dto.request.FindPostRequest;
import com.study.proudcat.domain.post.dto.request.ModifyPostRequest;
import com.study.proudcat.domain.post.dto.request.WritePostRequest;
import com.study.proudcat.domain.post.dto.response.FindPostResponse;
import com.study.proudcat.domain.post.dto.response.FindPostsResponse;
import com.study.proudcat.domain.post.dto.response.PostDetail;
import com.study.proudcat.domain.post.dto.response.PostListResponse;
import com.study.proudcat.domain.post.entity.Post;
import com.study.proudcat.domain.post.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public void writePost(WritePostRequest request) {
        log.info("PostService writePost run..");
        postRepository.save(request.toEntity());
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
    public Page<PostListResponse> getPostsSearchList(FindPostRequest request, int page, int size) {
        log.info("PostService getPostsSearchList run..");
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findAllPostsPage(request, pageRequest);
        return postPage.map(PostListResponse::from);
    }

    @Transactional(readOnly = true)
    public FindPostResponse getPostById(Long postId) {
        log.info("PostService getPostById run..");
        Post post = getPostEntity(postId);

        return FindPostResponse.from(post);
    }

    @Transactional(readOnly = true)
    public PostDetail getPostWithCommentsById(Long postId) {
        log.info("PostService getPostWithCommentsById run..");
        Post post = getPostEntity(postId);

        return PostDetail.from(post);
    }

    @Transactional
    public void updatePostView(Long postId) {
        log.info("PostService updatePostView run..");
        postRepository.updateView(postId);
    }

    @Transactional
    public void modifyPost(Long postId, ModifyPostRequest request) {
        log.info("PostService modifyPost run..");
        Post post = getPostEntity(postId);
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
