package com.example.authenticator.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.authenticator.entities.User;
import com.example.authenticator.repositories.UserRepository;
import com.example.authenticator.security.UserDetailsImpl;


@Service
public class UserService {
	UserRepository userRepository;
	
    @Autowired
    private SessionRegistry sessionRegistry;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}


	public User getUser(Long userId) {
		return userRepository.findById(userId).orElse(null);
	}

	public User updateUser(Long userId, User newUser) {
		Optional<User> user = userRepository.findById(userId);
		if(user.isPresent()) {
			User foundUser = user.get();
			foundUser.setName(newUser.getName());
			foundUser.setSurName(newUser.getSurName());
			foundUser.setEmail(newUser.getEmail());
			foundUser.setPassword(newUser.getPassword());
			return userRepository.save(foundUser);
		} else {
			return null;
		}
	}

	public void deleteUser(Long userId) {
		userRepository.deleteById(userId);
		
	}
	
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public void createUser(User user) {
		userRepository.save(user);
		
	}
	@Transactional
	public int enableAppUser(String email) {
        return userRepository.enableUser(email);
    }

	public List<UserDetailsImpl> getActiveUsers() {
	    return sessionRegistry.getAllPrincipals()
	            .stream()
	            .filter(principal -> principal instanceof UserDetailsImpl)
	            .map(UserDetailsImpl.class::cast)
	            .collect(Collectors.toList());
	}
	
	
}
