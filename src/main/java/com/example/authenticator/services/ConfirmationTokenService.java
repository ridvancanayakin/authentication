package com.example.authenticator.services;

import java.time.LocalDate;
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
	
	public List<ConfirmationToken> getExpiredTokens(){
		return confirmationTokenRepository.getExpiredTokens(LocalDateTime.now());
		
	}

	public List<ConfirmationToken> getTokensConfirmedAfter(LocalDateTime localDateTime) {
		return confirmationTokenRepository.getTokensConfirmedAfter(localDateTime);
	}
	
	public List<ConfirmationToken> getConfirmedTokensAtSpecificDate (LocalDate localDate){
		return confirmationTokenRepository.getConfirmedTokensAtSpecificDate(localDate.atStartOfDay(), localDate.plusDays(1).atStartOfDay());
	}
	
}
