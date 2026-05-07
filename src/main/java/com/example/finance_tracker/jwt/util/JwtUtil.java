package com.example.finance_tracker.jwt.util;

import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class JwtUtil {
   
	public String generateToken(String username , Collection<? extends GrantedAuthority> authortites) {
		 
		String[] roles = new String[authortites.size()];
		int i = 0;
		
		 for(GrantedAuthority grantedAuthority :  authortites) {
			  roles[i] = grantedAuthority.getAuthority();
			  i++;
		 }
		
		String jwtToken = JWT.create()
				             .withClaim("USER_NAME" , username)
				             .withArrayClaim("ROLES", roles)
				             .withExpiresAt(new Date(System.currentTimeMillis()+60*60*1000))
				             .sign(Algorithm.HMAC256("secret"));
		
		return jwtToken;
	}
	
	public String ValidateToken(String token) {
		
		String payLoad = JWT.require(Algorithm.HMAC256("secret"))
				            .build()
				            .verify(token)
				            .getPayload();
		
		byte[] decodedbyteArray = Base64.getDecoder().decode(payLoad);
		
		String decodePayLoad = new String(decodedbyteArray);
		
		return decodePayLoad;
	}
}
