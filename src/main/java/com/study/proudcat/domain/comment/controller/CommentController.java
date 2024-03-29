package com.study.proudcat.domain.comment.controller;

import com.study.proudcat.domain.comment.dto.CommentRequest;
import com.study.proudcat.domain.comment.service.CommentService;
import com.study.proudcat.infra.security.auth.UserDetailsImpl;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/proudcat")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "게시물에 댓글을 작성하는 메서드입니다.")
    @PostMapping("{postId}/comments")
    public ResponseEntity<Void> writeComment(
            @PathVariable(name = "postId") Long postId,
            @RequestBody CommentRequest request,
            @AuthenticationPrincipal UserDetailsImpl user
            ) {
        commentService.writeComment(postId, request, user.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "댓글 삭제", description = "댓글을 삭제하는 메서드입니다.")
    @DeleteMapping("{postId}/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId,
            @AuthenticationPrincipal UserDetailsImpl user
    ) {
        commentService.deleteComment(postId, commentId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
