package com.example.authenticator.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.authenticator.entities.User;
import com.example.authenticator.repositories.UserRepository;


@Service
public class UserService {
	UserRepository userRepository;

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
	
}
