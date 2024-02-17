package com.study.proudcat.domain.user.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.study.proudcat.domain.user.dto.LoginRequest;
import com.study.proudcat.domain.user.dto.ReissueTokenRequest;
import com.study.proudcat.domain.user.dto.TokenResponse;
import com.study.proudcat.domain.user.entity.RefreshToken;
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
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtTokenProvider tokenProvider;
	private final AuthenticationManager authenticationManager;


	@Transactional
	public TokenResponse login(LoginRequest request) {
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

	@Transactional
	public void logout(UserDetailsImpl userDetails) {
	    RefreshToken refreshToken = refreshTokenRepository.findByUserId(userDetails.getId())
	            .orElseThrow(() -> new RestApiException(ErrorCode.NO_TARGET));
	    refreshTokenRepository.delete(refreshToken);
	}
}
