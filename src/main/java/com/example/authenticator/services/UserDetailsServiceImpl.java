package com.example.authenticator.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.authenticator.entities.User;
import com.example.authenticator.repositories.UserRepository;
import com.example.authenticator.security.UserDetailsImpl;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private UserRepository userRepository;
	
	
	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		if( user == null ) {
			throw new UsernameNotFoundException("Could not found the user.");
		}
		UserDetailsImpl userDetails = new UserDetailsImpl(user);
		return userDetails;
	}
	
	public UserDetails loadUserById(Long id) {
		User user= userRepository.findById(id).get();
		return new UserDetailsImpl(user);
	}

}
