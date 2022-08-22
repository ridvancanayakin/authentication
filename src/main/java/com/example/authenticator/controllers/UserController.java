package com.example.authenticator.controllers;

import java.util.List;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.authenticator.entities.User;
import com.example.authenticator.security.UserDetailsImpl;
import com.example.authenticator.services.UserService;

import lombok.Data;


@RestController
@RequestMapping("users")
public class UserController {
	private UserService userService;

	
	public UserController( UserService userService ) {
		this.userService = userService;
	}
	
	@GetMapping
	public List<User> getAllUsers(){
		return userService.getAllUsers();
	}
	
	
	@GetMapping("/{userId}")
	public User getUserById( @PathVariable Long userId ) {
		return userService.getUser(userId);
	}
	
	@PutMapping("/{userId}")
	public User updateUser(@PathVariable Long userId, @RequestBody User newUser) {
		return userService.updateUser(userId, newUser);
		
	}
	
	@DeleteMapping("/{userId}")
	public void deleteUser( @PathVariable Long userId ) {
		userService.deleteUser(userId);
	}
	
	@GetMapping("/active")
    public List<UserDetailsImpl> getActiveUsers() {
		return userService.getActiveUsers();
	}
}
