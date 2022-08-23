package com.example.authenticator.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.aspectj.util.GenericSignature.ArrayTypeSignature;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
		Map<Long, UserDetailsImpl> map = new HashMap<>();
		
	    for (UserDetailsImpl user : userService.getActiveUsers()) {
	        map.put(user.getId(), user);
	    }
	    List<UserDetailsImpl> list = new ArrayList<>(map.values());
		return list;
	}
	
	@GetMapping("/registeredWithin/{hour}")
	public List<User> getUsersRegisteredWithin(@PathVariable String hour){
		return userService.getUsersRegisteredWithin(Long.parseLong(hour));
	}
	
	
	@GetMapping("/unregistered")
	public List<User> getUnregisteredUsers(){
		return userService.getUnregisteredUsers();
	}
	
	@GetMapping("/avarageTime")
	public double avarageTime(@RequestParam("year") String year, @RequestParam("month") String month, @RequestParam("day") String day){
		return userService.getAvarageTime(year, month, day);
	}
}
