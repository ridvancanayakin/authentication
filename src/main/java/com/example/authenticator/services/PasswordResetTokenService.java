package com.example.authenticator.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.authenticator.entities.PasswordResetToken;
import com.example.authenticator.repositories.PasswordResetTokenRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PasswordResetTokenService {
	private PasswordResetTokenRepository passwordResetTokenRepository;
	
	
	public void savePasswordResetToken(PasswordResetToken passwordResetToken) {
		passwordResetTokenRepository.save(passwordResetToken);
	}
	
	
	public Optional<PasswordResetToken> getToken(String token){
		return passwordResetTokenRepository.findByToken(token);
	}
	@Transactional
	public int setConfirmedAt(String token) {
        return passwordResetTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
