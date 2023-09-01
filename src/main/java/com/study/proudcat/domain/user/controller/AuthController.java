package com.study.proudcat.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.study.proudcat.domain.user.dto.*;
import com.study.proudcat.domain.user.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원 가입")
    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> signup(@RequestBody SignupRequest request) {
        log.info("AuthController signup run..");
        return ResponseEntity.ok(authService.signup(request));
    }

    @Operation(summary = "로그인", description = "이메일, 비밀번호 입력 후 로그인 성공시 jwt 발급 됨")
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) throws JsonProcessingException {
        log.info("AuthController login run..");
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "accessToken 재발급", description = "accessToken을 재발급하는 기능(refreshToken은 그대로)")
    @GetMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(
            @AuthenticationPrincipal UserPrincipalDetail userPrincipalDetail
    ) throws JsonProcessingException {

        UserResponse userResponse = UserResponse.of(userPrincipalDetail.user());
        return ResponseEntity.ok(authService.reissueAtk(userResponse));
    }

    @GetMapping("/user-detail")
    public ResponseEntity<String> getUserDetail(@AuthenticationPrincipal UserPrincipalDetail userPrincipalDetail) {
        return ResponseEntity.ok(userPrincipalDetail.getUserNickname());
    }
}
