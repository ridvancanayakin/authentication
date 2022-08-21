package com.example.authenticator.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.authenticator.entities.ConfirmationToken;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Long> {
	Optional<ConfirmationToken> findByToken(String token);
}
