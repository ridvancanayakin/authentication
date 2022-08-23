package com.example.authenticator.entities;

import lombok.Data;
@Data
public class PasswordResetRequest {

	private String token;
	private String password;
	
}
