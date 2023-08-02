package com.study.proudcat.domain.user.service;

import com.study.proudcat.domain.user.dto.LoginRequest;
import com.study.proudcat.domain.user.dto.SignupRequest;
import com.study.proudcat.domain.user.dto.UserResponse;
import com.study.proudcat.domain.user.entity.Role;
import com.study.proudcat.domain.user.entity.User;
import com.study.proudcat.domain.user.repository.UserRepository;
import com.study.proudcat.infra.exception.ErrorCode;
import com.study.proudcat.infra.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse signup(SignupRequest request) {
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
        return UserResponse.of(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse login(LoginRequest request) {
        log.info("AuthService login run..");
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RestApiException(ErrorCode.WRONG_EMAIL_OR_PASSWORD));

        boolean matches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword());
        if(!matches) throw new RestApiException(ErrorCode.WRONG_EMAIL_OR_PASSWORD);
        return UserResponse.of(user);
    }
}
