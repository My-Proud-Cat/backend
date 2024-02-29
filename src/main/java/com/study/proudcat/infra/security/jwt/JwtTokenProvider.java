package com.study.proudcat.infra.security.jwt;

import static org.springframework.util.StringUtils.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.study.proudcat.domain.user.repository.RedisRepository;
import com.study.proudcat.infra.exception.ErrorCode;
import com.study.proudcat.infra.exception.RestApiException;
import com.study.proudcat.infra.security.auth.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {
	private static final String AUTHENTICATION_CLAIM_NAME = "roles";
	private static final String AUTHENTICATION_SCHEME = "Bearer ";
	private final RedisRepository redisRepository;

	@Value("${application.security.jwt.secret-key}")
	private String secretKey;
	@Value("${application.security.jwt.expiration}")
	private int accessExpiration;
	@Value("${application.security.jwt.refresh-token.expiration}")
	private int refreshExpiration;

	public String createRefreshToken(UserDetailsImpl userDetails) {
		return createToken(userDetails, refreshExpiration);
	}

	public String createAccessToken(UserDetailsImpl userDetails) {
		return createToken(userDetails, accessExpiration);
	}

	private String createToken(UserDetailsImpl userDetails, int tokenExpiration) {
		String authorities = null;
		if (userDetails.getAuthorities() != null) {
			authorities = userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		}

		return Jwts.builder()
			.subject(userDetails.getUsername())
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + tokenExpiration))
			.claim(AUTHENTICATION_CLAIM_NAME, authorities)
			.claim("id", userDetails.getId())
			.claim("nickname", userDetails.getNickname())
			.signWith(getSignInKey())
			.compact();
	}

	public Authentication getAuthentication(String accessToken) {
		Claims claims = verifyAndExtractClaims(accessToken);
		Collection<? extends GrantedAuthority> authorities = null;
		if (claims.get(AUTHENTICATION_CLAIM_NAME) != null) {
			authorities = Arrays.stream(claims.get(AUTHENTICATION_CLAIM_NAME)
					.toString()
					.split(","))
				.map(SimpleGrantedAuthority::new)
				.toList();
		}

		UserDetailsImpl principal = UserDetailsImpl.builder()
			.id(getUserId(accessToken))
			.email(claims.getSubject())
			.nickname(getUserNickname(accessToken))
			.authorities(authorities)
			.build();
		return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
	}

	private Claims verifyAndExtractClaims(String token) {
		return Jwts.parser()
			.verifyWith(getSignInKey())
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}

	public void validateToken(String token) {
		if (redisRepository.hasKeyBlackList(token)) {
			throw new RestApiException(ErrorCode.ALREADY_LOGGED_OUT);
		}
		Jwts.parser()
			.verifyWith(getSignInKey())
			.build()
			.parse(token);
	}

	public Long getUserId(String token) {
		Claims claims = verifyAndExtractClaims(token);
		return claims.get("id", Long.class);
	}

	public String getUserNickname(String token) {
		Claims claims = verifyAndExtractClaims(token);
		return claims.get("nickname", String.class);
	}

	public String getTokenBearer(String bearerTokenHeader) {
		if (hasText(bearerTokenHeader) && bearerTokenHeader.startsWith(AUTHENTICATION_SCHEME)) {
			return bearerTokenHeader.substring(AUTHENTICATION_SCHEME.length());
		}
		return null;
	}

	private SecretKey getSignInKey() {
		byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
