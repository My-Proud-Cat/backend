package com.study.proudcat.domain.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.study.proudcat.domain.user.dto.LoginRequest;
import com.study.proudcat.domain.user.dto.SignupRequest;
import com.study.proudcat.domain.user.dto.TokenResponse;
import com.study.proudcat.domain.user.dto.UserResponse;
import com.study.proudcat.domain.user.entity.RefreshToken;
import com.study.proudcat.domain.user.entity.Role;
import com.study.proudcat.domain.user.entity.User;
import com.study.proudcat.domain.user.repository.RefreshTokenRepository;
import com.study.proudcat.domain.user.repository.UserRepository;
import com.study.proudcat.infra.exception.ErrorCode;
import com.study.proudcat.infra.exception.RestApiException;
import com.study.proudcat.infra.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider tokenProvider;


    @Transactional
    public UserResponse signup(SignupRequest request) {
        log.info("AuthService signup run..");
        if (userRepository.existsByEmail(request.getEmail())) {
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
                .build();
        User savedUser = userRepository.save(user);
        return UserResponse.of(savedUser);
    }

    @Transactional
    public TokenResponse login(LoginRequest request) throws JsonProcessingException {
        log.info("AuthService login run..");
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RestApiException(ErrorCode.WRONG_EMAIL_OR_PASSWORD));

        // 비밀번호가 틀릴 경우
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new RestApiException(ErrorCode.WRONG_EMAIL_OR_PASSWORD);
        UserResponse userResponse = UserResponse.of(user);

        String atk = tokenProvider.createAccessToken(userResponse);
        String rtk = tokenProvider.createRefreshToken(userResponse);
        refreshTokenRepository.save(RefreshToken.builder()
                .key(request.getEmail())
                .value(rtk)
                .build());

        return TokenResponse.builder()
                .accessToken(atk)
                .refreshToken(rtk)
                .build();
    }

    @Transactional(readOnly = true)
    public TokenResponse reissueAtk(UserResponse userResponse) throws JsonProcessingException {
        RefreshToken refreshToken = refreshTokenRepository.findByKey(userResponse.email())
                .orElseThrow(() -> new RestApiException(ErrorCode.NO_TARGET));

        if (Objects.isNull(refreshToken)) throw new RestApiException(ErrorCode.TOKEN_EXPIRED);
        String accessToken = tokenProvider.createAccessToken(userResponse);
        return new TokenResponse(accessToken, refreshToken.getValue());
    }

    @Transactional
    public void logout(String email) throws JsonProcessingException {
        RefreshToken refreshToken = refreshTokenRepository.findByKey(email)
                .orElseThrow(() -> new RestApiException(ErrorCode.NO_TARGET));
        refreshTokenRepository.delete(refreshToken);
    }
}
