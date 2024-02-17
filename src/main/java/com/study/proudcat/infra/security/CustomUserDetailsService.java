package com.study.proudcat.infra.security;

import com.study.proudcat.domain.user.entity.Provider;
import com.study.proudcat.domain.user.entity.User;
import com.study.proudcat.domain.user.repository.UserRepository;
import com.study.proudcat.infra.security.auth.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndProvider(username, Provider.LOCAL)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email and provider : " + username));
        return UserDetailsImpl.from(user);
    }
}
