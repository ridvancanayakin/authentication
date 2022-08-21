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
import com.example.authenticator.services.ConfirmationTokenService;
import com.example.authenticator.services.EmailSenderService;
import com.example.authenticator.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private AuthenticationManager authenticationManager;
	private JwtProvider jwtProvider;
	private UserService userService;
	private PasswordEncoder passwordEncoder;
	private EmailSenderService emailSenderService;
	private ConfirmationTokenService confirmationTokenService;
	
	
	public AuthController(AuthenticationManager authenticationManager, JwtProvider jwtProvider, UserService userService,
			PasswordEncoder passwordEncoder, EmailSenderService emailSenderService, ConfirmationTokenService confirmationTokenService) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtProvider = jwtProvider;
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.emailSenderService = emailSenderService;
		this.confirmationTokenService = confirmationTokenService;
	}

	@PostMapping("/login")
	public String login(@RequestBody SignInRequest loginRequest) {
		UsernamePasswordAuthenticationToken authToken = 
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword());
		Authentication auth = authenticationManager.authenticate(authToken);
		SecurityContextHolder.getContext().setAuthentication(auth);
		String jwt = jwtProvider.generateToken(auth);
		return "Bearer "+jwt;
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody SignUpRequest signUpRequest){
		if( userService.getUserByEmail(signUpRequest.getEmail()) !=null ) {
			return new ResponseEntity<>("Email taken.",HttpStatus.BAD_REQUEST);
		}
		User user = new User();
		UserRole role;
		user.setName(signUpRequest.getName());
		user.setSurName(signUpRequest.getSurName());
		user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
		user.setEmail(signUpRequest.getEmail());
		if(signUpRequest.getRole() == null || !signUpRequest.getRole().equals("ADMIN")) {
			role = UserRole.USER;
		} else {
			role = UserRole.ADMIN;
		}
		user.setRole(role);
		user.setEnabled(false);
		user.setLocked(false);
		userService.createUser(user);
		String token = UUID.randomUUID().toString();
		
		ConfirmationToken confirmationToken = new ConfirmationToken(
				token,
				LocalDateTime.now(),
				LocalDateTime.now().plusDays(1),
				user);
		confirmationTokenService.saveConfirmationToken(confirmationToken);
		
		//emailSenderService.sendEmail("canayakin93@gmail.com", "null");
		return new ResponseEntity<>("User created. Verification email sent to " + user.getEmail()+ "token: "+ token,HttpStatus.CREATED);
	}
	
	@Transactional
	@GetMapping("/confirm")
	public String confirm(@RequestParam("token") String token) {
		ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(
				()-> new IllegalStateException("Token not found"));
		if(confirmationToken.getConfirmedAt() != null) {
			throw new IllegalStateException("Email already confirmed");
		}
		LocalDateTime expiredAt = confirmationToken.getExpiresAt();
		if(expiredAt.isBefore(LocalDateTime.now())) {
			throw new IllegalStateException("Token expired");
		}
		confirmationTokenService.setConfirmedAt(token);
		userService.enableAppUser(confirmationToken.getUser().getEmail());
		return "confirmed";
	}
}
