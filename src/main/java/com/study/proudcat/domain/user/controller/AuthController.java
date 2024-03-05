package com.study.proudcat.domain.user.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.proudcat.domain.user.dto.LoginRequest;
import com.study.proudcat.domain.user.dto.ReissueTokenRequest;
import com.study.proudcat.domain.user.dto.TokenResponse;
import com.study.proudcat.domain.user.dto.UserResponse;
import com.study.proudcat.domain.user.service.AuthService;
import com.study.proudcat.infra.security.auth.UserDetailsImpl;
import com.study.proudcat.infra.utils.HttpResponseUtil;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final AuthService authService;

	@Operation(summary = "로그인", description = "이메일, 비밀번호 입력 후 로그인 성공시 jwt 발급 됨")
	@PostMapping("/login")
	public void login(@RequestBody LoginRequest request, HttpServletResponse response) throws
		IOException {
		TokenResponse tokenResponse = authService.login(request);

		Cookie refreshTokenCookie = new Cookie("refreshToken", tokenResponse.getRefreshToken());
		refreshTokenCookie.setSecure(true);
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setPath("/");

		response.addCookie(refreshTokenCookie);
		HttpResponseUtil.setSuccessResponse(response, HttpStatus.OK, tokenResponse.getAccessToken());
	}

	@Operation(summary = "토큰 재발급", description = "accessToken, refreshToken을 재발급하는 기능")
	@PostMapping("/reissue")
	public ResponseEntity<TokenResponse> reissue(ReissueTokenRequest request) {
		return ResponseEntity.ok(authService.reissueToken(request));
	}

	@Operation(summary = "로그아웃") // TODO: 오류 해결
	@GetMapping("/logout")
	public ResponseEntity<?> logout(
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		HttpServletRequest request,
		//@RequestHeader("Authorization") String authorizationToken,
		@CookieValue(name = "refreshToken") String refreshToken){
		String at = request.getHeader("Authorization");
		authService.logout(refreshToken, at, userDetails.getId());
		return ResponseEntity.ok("로그아웃 되었습니다.");
	}

	@Operation(summary = "사용자 정보 테스트 api", description = "로그인한 사용자의 닉네임을 반환하는 테스트 api")
	@GetMapping("/user-detail")
	public ResponseEntity<UserResponse> getUserDetail(
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return ResponseEntity.ok(new UserResponse(userDetails.getId(), userDetails.getEmail(), userDetails.getNickname()));
	}
}
