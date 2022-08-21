package com.example.authenticator.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {
	@Value("${authentication.key}")
	private String KEY;
	@Value("${expires.in}")
	private long EXPIRES_IN;
	
	public String generateToken(Authentication auth) {
		UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
		Date expireDate = new Date( new Date().getTime()+EXPIRES_IN );
		
		return Jwts.builder().setSubject(Long.toString(userDetails.getId()))
				.setIssuedAt(new Date()).setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS512, KEY).compact();
	}
	
	Long getUserIdFromJwt(String token) {
		Claims claims = getBody(token);
		if( claims != null ) {
			return Long.parseLong(claims.getSubject());
		}
		return null;
	}
	
	Claims getBody(String token) {
		Claims claims = null;
		try {
			claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			return null;
		}
		return claims;
	}
	
	boolean isTokenExpired(Claims claims) {
		if( claims !=  null) {
			Date expiration = claims.getExpiration();
			return expiration.before(new Date());
		} else {
			return true;			
		}
	}
	
	boolean validateToken (String token) {
		return !isTokenExpired(getBody(token));
	}
	
}
