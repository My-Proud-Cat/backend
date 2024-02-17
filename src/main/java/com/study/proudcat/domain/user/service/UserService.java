package com.study.proudcat.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.proudcat.domain.user.dto.SignupRequest;
import com.study.proudcat.domain.user.dto.UserResponse;
import com.study.proudcat.domain.user.entity.Provider;
import com.study.proudcat.domain.user.entity.Role;
import com.study.proudcat.domain.user.entity.User;
import com.study.proudcat.domain.user.repository.UserRepository;
import com.study.proudcat.infra.exception.ErrorCode;
import com.study.proudcat.infra.exception.RestApiException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public UserResponse signup(SignupRequest request) {
		if (userRepository.existsByEmailAndProvider(request.getEmail(), Provider.LOCAL)) {
			throw new RestApiException(ErrorCode.DUPLICATED_EMAIL);
		}
		if (userRepository.existsByNickname(request.getNickname())) {
			throw new RestApiException(ErrorCode.DUPLICATED_NICKNAME);
		}

		User user = User.builder()
			.email(request.getEmail())
			.nickname(request.getNickname())
			.password(passwordEncoder.encode(request.getPassword()))
			.role(Role.ROLE_USER)
			.provider(Provider.LOCAL)
			.build();
		User savedUser = userRepository.save(user);
		return UserResponse.of(savedUser);
	}
}
