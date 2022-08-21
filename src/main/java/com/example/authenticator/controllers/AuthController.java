package com.example.authenticator.controllers;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.authenticator.entities.ConfirmationToken;
import com.example.authenticator.entities.SignInRequest;
import com.example.authenticator.entities.SignUpRequest;
import com.example.authenticator.entities.User;
import com.example.authenticator.entities.UserRole;
import com.example.authenticator.entities.SignInRequest;
import com.example.authenticator.security.JwtProvider;
import com.example.authenticator.services.AuthService;
import com.example.authenticator.services.ConfirmationTokenService;
import com.example.authenticator.services.EmailSenderService;
import com.example.authenticator.services.UserService;

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
}
