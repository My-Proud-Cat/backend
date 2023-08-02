package com.study.proudcat.infra.security.jwt;

import com.study.proudcat.infra.security.CustomUserDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            String atk = authorization.substring(7);
            try {
                Subject subject = jwtTokenProvider.getSubject(atk);
                String requestURI = request.getRequestURI();

                // Request URI가 /auth/reissue인 경우에만 허용한다
                if (subject.type().equals("RTK") && !requestURI.equals("/auth/reissue")) {
                    throw new JwtException("토큰을 확인하세요");
                }

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(subject.email());
                Authentication token = new UsernamePasswordAuthenticationToken(
                        userDetails, "", userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(token);
            } catch (JwtException e) {
                request.setAttribute("exception", e.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}
