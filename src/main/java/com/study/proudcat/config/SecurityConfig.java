package com.study.proudcat.config;

import com.study.proudcat.infra.security.jwt.JwtAccessDeniedHandler;
import com.study.proudcat.infra.security.jwt.JwtAuthenticationEntryPoint;
import com.study.proudcat.infra.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(
                        "/h2-console/**",
                        "/favicon.ico",
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/webjars/**",
                        /* swagger v3 */
                        "/v3/api-docs/**",
                        "/swagger-ui/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()   //csr 방식으로 사용하므로 csrf 보호 설정 비활성화

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // h2-console을 위한 설정
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // Spring security는 기본적으로 세션을 사용
                // 여기서는 사용하지 않기에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeHttpRequests()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .anyRequest().permitAll()   //우선 인증/인가 적용 없이도 모두 접근할 수 있도록 설정

                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
