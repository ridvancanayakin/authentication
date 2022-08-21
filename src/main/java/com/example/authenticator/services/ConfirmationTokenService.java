package com.example.authenticator.services;

import org.springframework.stereotype.Service;

import com.example.authenticator.entities.ConfirmationToken;
import com.example.authenticator.repositories.ConfirmationTokenRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
	private ConfirmationTokenRepository confirmationTokenRepository;
	
	public void saveConfirmationToken(ConfirmationToken token) {
		confirmationTokenRepository.save(token);
	}
	
}
