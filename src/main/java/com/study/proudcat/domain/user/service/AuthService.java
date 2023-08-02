package com.study.proudcat.domain.user.service;

import com.study.proudcat.domain.user.dto.LoginRequest;
import com.study.proudcat.domain.user.dto.SignupRequest;
import com.study.proudcat.domain.user.dto.TokenDto;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void signup(SignupRequest request) {
        log.info("AuthService signup run..");
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RestApiException(ErrorCode.DUPLICATED_EMAIL);
        }
        //닉네임 중복 허용 ??
        User user = User.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        User savedUser = userRepository.save(user);
    }

    @Transactional
    public TokenDto login(LoginRequest request) {

        // 검증 (사용자 비밀번호 체크)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 인증 정보 기반으로 JWT 생성
        TokenDto tokenDto = TokenDto.builder()
                .grantType("Bearer")
                .accessToken(jwtTokenProvider.generateToken(authentication))
                .refreshToken(jwtTokenProvider.generateRefreshToken(authentication))
                .build();

        // RefreshToken 저장
        saveRefreshToken(authentication.getName(), tokenDto.getRefreshToken());

        // 토큰 발급
        return tokenDto;
    }

    private void saveRefreshToken(String name, String token) {
        RefreshToken refreshToken = RefreshToken.builder()
                .key(name)
                .value(token)
                .build();
        refreshTokenRepository.save(refreshToken);

    }
}
