package com.example.authenticator.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	public Optional<ConfirmationToken> getToken(String token){
		return confirmationTokenRepository.findByToken(token);
	}
	@Transactional
	public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
	public List<ConfirmationToken> getAllToken(){
		return confirmationTokenRepository.findAll();
	}
	
}
