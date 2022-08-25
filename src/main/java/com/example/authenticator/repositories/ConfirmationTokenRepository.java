package com.example.authenticator.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.authenticator.entities.ConfirmationToken;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Long> {
	Optional<ConfirmationToken> findByToken(String token);
	
	@Transactional
    @Modifying
    @Query("UPDATE ConfirmationToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token, LocalDateTime confirmedAt);
	
    @Query("SELECT c FROM ConfirmationToken c WHERE c.confirmedAt is null and c.expiresAt <  ?1")
	List<ConfirmationToken> getExpiredTokens(LocalDateTime localDateTime);
}
