package com.study.proudcat.domain.post.controller;

import com.study.proudcat.domain.post.service.HeartService;
import com.study.proudcat.infra.security.auth.UserDetailsImpl;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/proudcat")
@RequiredArgsConstructor
public class HeartController {
    private final HeartService heartService;

    @Operation(summary = "게시물 좋아요", description = "게시물 좋아요 버튼 클릭 메소드입니다. 이미 클릭한 버튼을 다시 클릭하면 좋아요가 취소됩니다.")
    @GetMapping("/{postId}/heart")
    public ResponseEntity<?> addHeart(
            @PathVariable(name = "postId") Long postId,
            @AuthenticationPrincipal UserDetailsImpl user
            ) {
        boolean isPushed = heartService.pushHeartBtn(postId, user.getId());
        return ResponseEntity.ok(isPushed);
    }
}
