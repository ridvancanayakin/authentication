package com.example.authenticator.services;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.authenticator.entities.ConfirmationToken;
import com.example.authenticator.entities.User;
import com.example.authenticator.repositories.UserRepository;
import com.example.authenticator.security.UserDetailsImpl;


@Service
public class UserService {
	UserRepository userRepository;
	ConfirmationTokenService confirmationTokenService;
	
    @Autowired
    private SessionRegistry sessionRegistry;

	public UserService(UserRepository userRepository, ConfirmationTokenService confirmationTokenService) {
		this.userRepository = userRepository;
		this.confirmationTokenService = confirmationTokenService;
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

	public List<User> getUsersRegisteredWithin(Long hour) {
		
		List<ConfirmationToken> confirmationTokens= confirmationTokenService.getTokensConfirmedAfter(LocalDateTime.now().minusHours(hour));
		List<User> result = new ArrayList<>();
		for( ConfirmationToken token:confirmationTokens ) {
				result.add(token.getUser());
		}		
		return result;
	}

	public List<User> getUnregisteredUsers() {
		List<ConfirmationToken> confirmationTokens= confirmationTokenService.getExpiredTokens();
		List<User> result = new ArrayList<>();
		for( ConfirmationToken token:confirmationTokens ) {
				result.add(token.getUser());
		}		
		return result;
	}

	public double getAvarageTime(String year, String month, String day) {
		LocalDate date = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month),  Integer.parseInt(day));
		List<ConfirmationToken> confirmationTokens= confirmationTokenService.getAllToken();
		List<Long> result = new ArrayList<>();
		for( ConfirmationToken token:confirmationTokens ) {
			if(token.getCreatedAt().toLocalDate().equals(date) && token.getConfirmedAt()!= null) {
				Long duration = Duration.between(token.getCreatedAt(), token.getConfirmedAt()).toSeconds();
				result.add(duration);
			}
		}

		if( result.size() != 0  ) {
			Long sum = 0L;
			Long num = 0L;
			for( int i =0; i<result.size(); i++ ) {
				sum = sum+result.get(i);
				num = num+1;
			}
			return sum/num;
		}
		return 0;
	}
	
	
}
