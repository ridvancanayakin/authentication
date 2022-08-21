package com.example.authenticator.entities;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.Data;

@Data
public class SignUpRequest {
	private String name;
	private String surName;
	private String password;
	private String email;
	private String role;

}
