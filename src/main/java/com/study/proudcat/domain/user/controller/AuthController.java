package com.study.proudcat.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.proudcat.domain.user.dto.LoginRequest;
import com.study.proudcat.domain.user.dto.ReissueTokenRequest;
import com.study.proudcat.domain.user.dto.TokenResponse;
import com.study.proudcat.domain.user.service.AuthService;
import com.study.proudcat.infra.security.auth.UserDetailsImpl;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	@Operation(summary = "로그인", description = "이메일, 비밀번호 입력 후 로그인 성공시 jwt 발급 됨")
	@PostMapping("/login")
	public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request){
		return ResponseEntity.ok(authService.login(request));
	}

	@Operation(summary = "토큰 재발급", description = "accessToken, refreshToken을 재발급하는 기능")
	@PostMapping("/reissue")
	public ResponseEntity<TokenResponse> reissue(ReissueTokenRequest request) {
		return ResponseEntity.ok(authService.reissueToken(request));
	}

	@Operation(summary = "로그아웃")
	@GetMapping("/logout")
	public ResponseEntity<?> logout(@AuthenticationPrincipal UserDetailsImpl userDetails){
		authService.logout(userDetails);
		return ResponseEntity.ok("로그아웃 되었습니다.");
	}

	@Operation(summary = "사용자 정보 테스트 api", description = "로그인한 사용자의 닉네임을 반환하는 테스트 api")
	@GetMapping("/user-detail")
	public ResponseEntity<String> getUserDetail(
		@AuthenticationPrincipal UserDetailsImpl userPrincipalDetail) {
		return ResponseEntity.ok(userPrincipalDetail.getNickname());
	}
}
