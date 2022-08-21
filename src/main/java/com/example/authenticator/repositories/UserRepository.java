package com.example.authenticator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.authenticator.entities.User;


public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);
	
}
