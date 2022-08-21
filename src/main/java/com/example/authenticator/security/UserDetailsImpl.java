package com.example.authenticator.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.authenticator.entities.User;
import com.example.authenticator.entities.UserRole;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDetailsImpl implements UserDetails {

	private Long id;
	private String name;
	private String SurName;
	private String password;
	private String email;
	private Boolean locked;
	private Boolean enabled;
	private String role;
	private Collection<? extends GrantedAuthority> authorities;
	
	public UserDetailsImpl ( User user ) {
		this.id = user.getId();
		this.name = user.getName();
		this.SurName= user.getSurName();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.locked = user.getLocked();
		this.enabled = user.getEnabled();
		authorities =   Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()));
	}
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return !locked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}



	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return email;
	}



	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

}
