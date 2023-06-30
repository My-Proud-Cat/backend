package com.study.proudcat.domain.post.controller;

import com.study.proudcat.domain.post.dto.request.FindPostRequest;
import com.study.proudcat.domain.post.dto.request.ModifyPostRequest;
import com.study.proudcat.domain.post.dto.request.WritePostRequest;
import com.study.proudcat.domain.post.dto.response.FindPostResponse;
import com.study.proudcat.domain.post.dto.response.PostDetail;
import com.study.proudcat.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/picture")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시물 작성", description = "게시물 작성 메서드입니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> writePost(
            @RequestPart(value = "request") @Parameter(schema =@Schema(type = "string", format = "binary")) WritePostRequest request,
            @RequestPart(value = "image") MultipartFile image) throws IOException {
        postService.writePost(request, image);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "게시물 리스트 조회", description = "전체 게시물 조회 메서드입니다.")
    @GetMapping
    public ResponseEntity<?> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @Operation(summary = "게시물 전체 조회(페이징)", description = "제목으로 검색, 추천순/최신순 정렬 가능")
    @GetMapping("/list/paging")
    public ResponseEntity<?> getPostListPaging(
            FindPostRequest request,
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "3", required = false) int size,
            @RequestParam(value = "sort", defaultValue = "createdAt", required = false) String sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return ResponseEntity.ok(postService.getPostsSearchList(request, pageable));
    }

    @Operation(summary = "게시물 상세 조회", description = "게시물 상세 조회 메서드입니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<FindPostResponse> getPostById(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    @Operation(summary = "게시물 상세조회(댓글, 좋아요)", description = "게시물 상세 조회 메서드입니다. 댓글과 좋아요 수를 포함합니다..")
    @GetMapping("/{postId}/comments")
    public ResponseEntity<PostDetail> getPostWithCommentsByPostId(@PathVariable(name = "postId") Long postId) {
        postService.updatePostView(postId);
        return ResponseEntity.ok(postService.getPostWithCommentsById(postId));
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
