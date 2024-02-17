package com.study.proudcat.infra.security.auth;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.study.proudcat.domain.user.entity.Provider;
import com.study.proudcat.domain.user.entity.User;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDetailsImpl implements UserDetails {
	private Long id;
	private String email;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	private Provider provider;

	@Builder
	public UserDetailsImpl(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities, Provider provider) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
		this.provider = provider;
	}

	public static UserDetailsImpl from(User user) {
		List<GrantedAuthority> authorities = user.getRole() != null ?
			List.of(new SimpleGrantedAuthority(user.getRole().name()))
			: null;
		return UserDetailsImpl.builder()
			.id(user.getId())
			.email(user.getEmail())
			.password(user.getPassword())
			.provider(user.getProvider())
			.authorities(authorities)
			.build();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
