package com.study.proudcat.domain.user.controller;

import com.study.proudcat.domain.user.dto.LoginRequest;
import com.study.proudcat.domain.user.dto.SignupRequest;
import com.study.proudcat.domain.user.dto.TokenDto;
import com.study.proudcat.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {
        log.info("AuthController signup run..");
        authService.signup(request);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequest request) {
        log.info("AuthController login run..");
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/signin")
    public String sign() {
        log.info("실행됨");
        return "hello";
    }
}
