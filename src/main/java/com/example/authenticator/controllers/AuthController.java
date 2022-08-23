package com.example.authenticator.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.authenticator.entities.PasswordResetRequest;
import com.example.authenticator.entities.SignInRequest;
import com.example.authenticator.entities.SignUpRequest;
import com.example.authenticator.services.AuthService;


@RestController
@RequestMapping("/auth")
public class AuthController {
	
	
	private AuthService authService;

	public AuthController(AuthService authService) {
		super();
		this.authService = authService;
	}

	@PostMapping("/login")
	public String login(@RequestBody SignInRequest loginRequest) {
		return authService.login(loginRequest);
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody SignUpRequest signUpRequest){
		return authService.register(signUpRequest);
	}
	
	@Transactional
	@GetMapping("/confirm")
	public String confirm(@RequestParam("token") String token) {
		return authService.confirm(token);
	}
	
	@Transactional
	@GetMapping("/forgot")
	public ResponseEntity<String>  forgotPassword(@RequestParam("email") String  email) {
		return authService.forgotPassword(email);
	}
	
	@Transactional
	@PostMapping("/reset")
	public String  resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
		return authService.resetPassword(passwordResetRequest);
	}
}
