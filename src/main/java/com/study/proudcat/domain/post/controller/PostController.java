package com.study.proudcat.domain.post.controller;

import com.study.proudcat.domain.post.dto.request.ModifyPostRequest;
import com.study.proudcat.domain.post.dto.request.WritePostRequest;
import com.study.proudcat.domain.post.dto.response.FindPostResponse;
import com.study.proudcat.domain.post.dto.response.FindPostsResponse;
import com.study.proudcat.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/proudcat")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시물 작성", description = "게시물 작성 메서드입니다.")
    @PostMapping
    public ResponseEntity<Void> writePost(@RequestBody WritePostRequest request) {
        postService.writePost(request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "게시물 리스트 조회", description = "전체 게시물 조회 메서드입니다.")
    @GetMapping
    public ResponseEntity<FindPostsResponse> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @Operation(summary = "게시물 상세 조회", description = "게시물 상세 조회 메서드입니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<FindPostResponse> getPostById(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @Operation(summary = "게시물 수정", description = "게시물 수정 메소드입니다.")
    @PutMapping("/{postId}")
    public ResponseEntity<Void> modifyPost(@PathVariable("postId") Long postId, @RequestBody ModifyPostRequest request) {
        postService.modifyPost(postId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "게시물 삭제", description = "게시물 삭제 메소드입니다.")
    @PatchMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable("postId") Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
