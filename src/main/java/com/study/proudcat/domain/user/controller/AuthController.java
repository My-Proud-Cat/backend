package com.study.proudcat.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.study.proudcat.domain.user.dto.*;
import com.study.proudcat.domain.user.service.AuthService;
import com.study.proudcat.infra.security.jwt.JwtTokenProvider;
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
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponse> signup(@RequestBody SignupRequest request) {
        log.info("AuthController signup run..");
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) throws JsonProcessingException {
        log.info("AuthController login run..");
        UserResponse userResponse = authService.login(request);
        return ResponseEntity.ok(jwtTokenProvider.createTokenByLogin(userResponse));
    }

    @GetMapping("/signin")
    public String sign() {
        log.info("실행됨");
        return "hello";
    }

    //access 토큰 재발급
    @GetMapping("/reissue")
    public TokenResponse reissue(
            @AuthenticationPrincipal UserPrincipalDetail userPrincipalDetail
            )throws JsonProcessingException {
        UserResponse userResponse = UserResponse.of(userPrincipalDetail.user());
        return jwtTokenProvider.reissueAtk(userResponse);
    }

    @GetMapping("/testLogin")
    public String loginTest(@AuthenticationPrincipal UserPrincipalDetail userPrincipalDetail) {
        return "logined member : " + userPrincipalDetail.getUsername();
    }
}
