package com.study.proudcat.infra.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.proudcat.domain.user.dto.TokenResponse;
import com.study.proudcat.domain.user.dto.UserResponse;
import com.study.proudcat.domain.user.entity.RefreshToken;
import com.study.proudcat.domain.user.repository.RefreshTokenRepository;
import com.study.proudcat.infra.exception.ErrorCode;
import com.study.proudcat.infra.exception.RestApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final ObjectMapper objectMapper;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long accessExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public TokenResponse reissueAtk(UserResponse userResponse) throws JsonProcessingException {
        String rtk = refreshTokenRepository.findByKey(userResponse.email()).toString();
        if(Objects.isNull(rtk)) throw new RestApiException(ErrorCode.TOKEN_EXPIRED);
        Subject atkSubject = Subject.atk(
                userResponse.userId(),
                userResponse.email(),
                userResponse.nickname());
        String atk = createToken(atkSubject, accessExpiration);
        return new TokenResponse(atk, null);
    }

    public TokenResponse createTokenByLogin(UserResponse userResponse) throws JsonProcessingException {
        Subject atkSubject = Subject.atk(
                userResponse.userId(),
                userResponse.email(),
                userResponse.nickname());
        Subject rtkSubject = Subject.rtk(
                userResponse.userId(),
                userResponse.email(),
                userResponse.nickname());
        String atk = createToken(atkSubject, accessExpiration);
        String rtk = createToken(rtkSubject, refreshExpiration);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(userResponse.email())
                .value(rtk).build();
        refreshTokenRepository.save(refreshToken);

        return new TokenResponse(atk, rtk);
    }

    private String createToken(Subject subject, long tokenExpiration) throws JsonProcessingException {
        String subjectStr = objectMapper.writeValueAsString(subject);
        log.info("subjectStr = {}", subjectStr);
        Claims claims = Jwts.claims()
                .setSubject(subjectStr);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Subject getSubject(String atk) throws JsonProcessingException {
        String subjectStr = extractAllClaims(atk).getSubject();
        return objectMapper.readValue(subjectStr, Subject.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
