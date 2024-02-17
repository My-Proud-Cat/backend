package com.study.proudcat.domain.user.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.proudcat.domain.user.dto.LoginRequest;
import com.study.proudcat.domain.user.dto.ReissueTokenRequest;
import com.study.proudcat.domain.user.dto.SignupRequest;
import com.study.proudcat.domain.user.dto.TokenResponse;
import com.study.proudcat.domain.user.dto.UserResponse;
import com.study.proudcat.domain.user.entity.Provider;
import com.study.proudcat.domain.user.entity.RefreshToken;
import com.study.proudcat.domain.user.entity.Role;
import com.study.proudcat.domain.user.entity.User;
import com.study.proudcat.domain.user.repository.RefreshTokenRepository;
import com.study.proudcat.domain.user.repository.UserRepository;
import com.study.proudcat.infra.exception.ErrorCode;
import com.study.proudcat.infra.exception.RestApiException;
import com.study.proudcat.infra.security.auth.UserDetailsImpl;
import com.study.proudcat.infra.security.jwt.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtTokenProvider tokenProvider;
	private final AuthenticationManager authenticationManager;

	@Transactional
	public UserResponse signup(SignupRequest request) {
		log.info("AuthService signup run..");
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

	@Transactional
	public TokenResponse login(LoginRequest request) {
		log.info("AuthService login run..");
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
			request.getEmail(),
			request.getPassword()
		);

		Authentication authenticate = authenticationManager.authenticate(authentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl)authenticate.getPrincipal();
		String accessToken = tokenProvider.createAccessToken(userDetails);
		String refreshToken = tokenProvider.createAccessToken(userDetails);

		refreshTokenRepository.save(RefreshToken.builder()
			.userId(userDetails.getId())
			.refresh(refreshToken)
			.build());

		return TokenResponse.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	@Transactional
	public TokenResponse reissueToken(ReissueTokenRequest request) {
		RefreshToken refreshToken = refreshTokenRepository.findByRefresh(request.refreshToken())
			.orElseThrow(() -> new RestApiException(ErrorCode.NO_TARGET));

		User user = userRepository.findById(refreshToken.getUserId())
			.orElseThrow(() -> new RestApiException(ErrorCode.NO_TARGET));
		UserDetailsImpl userDetails = UserDetailsImpl.from(user);

		String newAccessToken = tokenProvider.createAccessToken(userDetails);
		String newRefreshToken = tokenProvider.createRefreshToken(userDetails);

		refreshTokenRepository.delete(refreshToken);
		refreshTokenRepository.save(RefreshToken.builder()
			.userId(userDetails.getId())
			.refresh(newRefreshToken)
			.build());
		return new TokenResponse(newAccessToken, newRefreshToken);
	}

	// @Transactional
	// public void logout(String email) throws JsonProcessingException {
	//     RefreshToken refreshToken = refreshTokenRepository.findById(email)
	//             .orElseThrow(() -> new RestApiException(ErrorCode.NO_TARGET));
	//     refreshTokenRepository.delete(refreshToken);
	// }
}
