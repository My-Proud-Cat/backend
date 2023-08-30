package com.study.proudcat.infra.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.proudcat.domain.user.dto.UserResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final ObjectMapper objectMapper;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long accessExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    public String createRefreshToken(UserResponse userResponse) throws JsonProcessingException {
        Subject rtkSubject = Subject.rtk(
                userResponse.userId(),
                userResponse.email(),
                userResponse.nickname());

        return createToken(rtkSubject, refreshExpiration);
    }

    public String createAccessToken(UserResponse userResponse) throws JsonProcessingException {
        Subject atkSubject = Subject.atk(
                userResponse.userId(),
                userResponse.email(),
                userResponse.nickname());
        return createToken(atkSubject, accessExpiration);
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

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private String getUserEmail(String token) throws JsonProcessingException {
        return getSubject(token).email();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) throws JsonProcessingException {
        final String username = getUserEmail(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
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
