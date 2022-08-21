package com.example.authenticator.entities;

import lombok.Data;

@Data
public class SignInRequest {
	String email;
	String password;
}
