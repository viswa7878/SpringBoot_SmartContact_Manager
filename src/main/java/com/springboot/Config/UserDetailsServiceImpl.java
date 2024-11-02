package com.springboot.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.springboot.ContactManager.entities.Userr;
import com.springboot.dao.UserRepository;

public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("username"+username.toString());
       Userr userbyname=userRepository.getUserByUsername(username);
       System.out.println("val"+userbyname.toString());
		
       if(userbyname==null)
       {
    	   throw new UsernameNotFoundException("count found user");
       }
       
       CustomUserDetails customUserDetails=new CustomUserDetails(userbyname);
		return customUserDetails;
	}

	
}
