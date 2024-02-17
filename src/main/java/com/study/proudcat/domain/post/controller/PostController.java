package com.study.proudcat.domain.post.controller;

import java.io.IOException;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.study.proudcat.domain.post.dto.request.ModifyPostRequest;
import com.study.proudcat.domain.post.dto.request.WritePostRequest;
import com.study.proudcat.domain.post.dto.response.PostDetails;
import com.study.proudcat.domain.post.service.PostService;
import com.study.proudcat.infra.security.auth.UserDetailsImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/picture")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@Operation(summary = "게시물 작성(이미지 포함)", description = "게시물 작성 메서드입니다. 이미지 첨부를 포함합니다.")
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> writePost(
		@RequestPart(value = "request") @Parameter(schema = @Schema(type = "string", format = "binary")) WritePostRequest request,
		@RequestPart(value = "image") MultipartFile image,
		@AuthenticationPrincipal UserDetailsImpl user) throws IOException {
		postService.writePost(request, image, user.getUsername());
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "게시물 전체 조회(페이징)", description = "제목으로 검색, 추천순/최신순 정렬 가능")
	@GetMapping("/list/paging")
	public ResponseEntity<?> getPostListPaging(
		@RequestParam(value = "title", required = false) String title,
		@RequestParam(value = "sort", defaultValue = "createdAt", required = false) String sort) {
		Pageable pageable = PageRequest.of(0, 20, Sort.by(sort));
		return ResponseEntity.ok(postService.getPostsSearchList(title, pageable));
	}

	@Operation(summary = "게시물 상세조회(댓글, 좋아요)", description = "게시물 상세 조회 메서드입니다. 댓글과 좋아요 수를 포함합니다..")
	@GetMapping("/{postId}/comments")
	public ResponseEntity<PostDetails> getPostWithCommentsByPostId(@PathVariable(name = "postId") Long postId) {
		postService.updatePostView(postId);
		return ResponseEntity.ok(postService.getPostWithCommentsById(postId));
	}

	@Operation(summary = "게시물 수정", description = "게시물 수정 메소드입니다.")
	@PutMapping("/{postId}")
	public ResponseEntity<Void> modifyPost(@PathVariable("postId") Long postId, @RequestBody ModifyPostRequest request,
		@AuthenticationPrincipal UserDetailsImpl user) {
		postService.modifyPost(postId, request, user.getUsername());
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "게시물 삭제", description = "게시물 삭제 메소드입니다.")
	@DeleteMapping("/{postId}")
	public ResponseEntity<Void> deletePost(@PathVariable("postId") Long postId,
		@AuthenticationPrincipal UserDetailsImpl user) {
		postService.deletePost(postId, user.getUsername());
		return ResponseEntity.noContent().build();
	}
}
