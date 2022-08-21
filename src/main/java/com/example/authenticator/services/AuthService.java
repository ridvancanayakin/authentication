package com.example.authenticator.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.authenticator.entities.ConfirmationToken;
import com.example.authenticator.entities.SignInRequest;
import com.example.authenticator.entities.SignUpRequest;
import com.example.authenticator.entities.User;
import com.example.authenticator.entities.UserRole;
import com.example.authenticator.security.JwtProvider;

@Service
public class AuthService {
	
	
	private AuthenticationManager authenticationManager;
	private JwtProvider jwtProvider;
	private UserService userService;
	private PasswordEncoder passwordEncoder;
	private EmailSenderService emailSenderService;
	private ConfirmationTokenService confirmationTokenService;
	
	
	public AuthService(AuthenticationManager authenticationManager, JwtProvider jwtProvider, UserService userService,
			PasswordEncoder passwordEncoder, EmailSenderService emailSenderService, ConfirmationTokenService confirmationTokenService) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtProvider = jwtProvider;
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
		this.emailSenderService = emailSenderService;
		this.confirmationTokenService = confirmationTokenService;
	}
	
	public String login(@RequestBody SignInRequest loginRequest) {
		UsernamePasswordAuthenticationToken authToken = 
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword());
		Authentication auth = authenticationManager.authenticate(authToken);
		SecurityContextHolder.getContext().setAuthentication(auth);
		String jwt = jwtProvider.generateToken(auth);
		return "Bearer "+jwt;
	}
	
	public ResponseEntity<String> register(SignUpRequest signUpRequest){
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
		String link = "http://localhost:8080/auth/confirm?token="+token;
		String mailBody= buildEmail(user.getName(), link);
		
		emailSenderService.sendEmail(user.getEmail(), mailBody);
		return new ResponseEntity<>("User created. Verification email sent to " + user.getEmail()+ "token: "+ token,HttpStatus.CREATED);
	}
	
	@Transactional
	public String confirm(String token) {
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
	
	
	
	 private String buildEmail(String name, String link) {
	        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
	                "\n" +
	                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
	                "\n" +
	                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
	                "    <tbody><tr>\n" +
	                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
	                "        \n" +
	                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
	                "          <tbody><tr>\n" +
	                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
	                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
	                "                  <tbody><tr>\n" +
	                "                    <td style=\"padding-left:10px\">\n" +
	                "                  \n" +
	                "                    </td>\n" +
	                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
	                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
	                "                    </td>\n" +
	                "                  </tr>\n" +
	                "                </tbody></table>\n" +
	                "              </a>\n" +
	                "            </td>\n" +
	                "          </tr>\n" +
	                "        </tbody></table>\n" +
	                "        \n" +
	                "      </td>\n" +
	                "    </tr>\n" +
	                "  </tbody></table>\n" +
	                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
	                "    <tbody><tr>\n" +
	                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
	                "      <td>\n" +
	                "        \n" +
	                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
	                "                  <tbody><tr>\n" +
	                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
	                "                  </tr>\n" +
	                "                </tbody></table>\n" +
	                "        \n" +
	                "      </td>\n" +
	                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
	                "    </tr>\n" +
	                "  </tbody></table>\n" +
	                "\n" +
	                "\n" +
	                "\n" +
	                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
	                "    <tbody><tr>\n" +
	                "      <td height=\"30\"><br></td>\n" +
	                "    </tr>\n" +
	                "    <tr>\n" +
	                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
	                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
	                "        \n" +
	                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 1 day. <p>See you soon</p>" +
	                "        \n" +
	                "      </td>\n" +
	                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
	                "    </tr>\n" +
	                "    <tr>\n" +
	                "      <td height=\"30\"><br></td>\n" +
	                "    </tr>\n" +
	                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
	                "\n" +
	                "</div></div>";
	    }
}
