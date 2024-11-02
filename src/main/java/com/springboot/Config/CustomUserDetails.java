package com.springboot.Config;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.springboot.ContactManager.entities.Userr;

public class CustomUserDetails implements UserDetails {

	
	private Userr user;
	
	public CustomUserDetails(Userr user) {
		super();
		this.user = user;
	}
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority=new SimpleGrantedAuthority(user.getRole());
		return List.of(simpleGrantedAuthority);
	}



	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
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
