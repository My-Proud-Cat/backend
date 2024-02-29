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
import com.study.proudcat.domain.user.entity.User;
import com.study.proudcat.domain.user.repository.RedisRepository;
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
	private final RedisRepository redisRepository;
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

		redisRepository.saveRefreshToken(refreshToken, userDetails.getId());

		return TokenResponse.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	@Transactional
	public TokenResponse reissueToken(ReissueTokenRequest request) {
		Object userIdObj = redisRepository.get(request.refreshToken());
		if (userIdObj == null) {
			throw new RestApiException(ErrorCode.REFRESH_TOKEN_EXPIRED);
		}
		Long userId = Long.parseLong(userIdObj.toString());
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new RestApiException(ErrorCode.NO_TARGET));
		UserDetailsImpl userDetails = UserDetailsImpl.from(user);

		String newAccessToken = tokenProvider.createAccessToken(userDetails);
		String newRefreshToken = tokenProvider.createRefreshToken(userDetails);

		redisRepository.delete(request.refreshToken());
		redisRepository.saveRefreshToken(newRefreshToken, userId);
		return new TokenResponse(newAccessToken, newRefreshToken);
	}

	@Transactional
	public void logout(String refreshToken, String authorizationToken, Long userId) {
		if (redisRepository.hasKey(refreshToken)) {
			Long userIdByRefresh = Long.valueOf(redisRepository.get(refreshToken).toString());
			if (userIdByRefresh.equals(userId)) {
				String accessToken = tokenProvider.getTokenBearer(authorizationToken);
				redisRepository.delete(refreshToken);
				redisRepository.saveBlackList(accessToken, "accessToken");
			} else {
				throw new RestApiException(ErrorCode.LOGOUT_FORBIDDEN);
			}
		} else {
			throw new RestApiException(ErrorCode.REFRESH_TOKEN_EXPIRED);
		}
	}
}
