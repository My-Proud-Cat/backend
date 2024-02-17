package com.study.proudcat.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.proudcat.domain.user.dto.SignupRequest;
import com.study.proudcat.domain.user.dto.UserResponse;
import com.study.proudcat.domain.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	@Operation(summary = "회원 가입")
	@PostMapping("/sign-up")
	public ResponseEntity<UserResponse> signup(@RequestBody SignupRequest request) {
		return ResponseEntity.ok(userService.signup(request));
	}
}
