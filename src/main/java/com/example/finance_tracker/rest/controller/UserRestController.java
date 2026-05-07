package com.example.finance_tracker.rest.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.finance_tracker.entity.FtUser;
import com.example.finance_tracker.jwt.util.JwtUtil;
import com.example.finance_tracker.service.FtUserService;
import com.example.finance_tracker.web.response.TokenResponse;

import ch.qos.logback.core.subst.Token;

@RestController
@RequestMapping("/user")
public class UserRestController {
   
   @Autowired
   private FtUserService userService;
   
   @Autowired
   private AuthenticationManager manager;
   
   @Autowired
   private JwtUtil util;
	
    @PostMapping("/register")
    public ResponseEntity<FtUser> registerUser(@RequestBody FtUser user){
    	FtUser createdUser = userService.createUser(user);
    	ResponseEntity<FtUser> res = new ResponseEntity<FtUser>(createdUser,HttpStatus.OK);
    	return res;
    }
   
    @PostMapping("/authentication")
	public ResponseEntity<TokenResponse>  loginUser(@RequestBody FtUser user){
		UsernamePasswordAuthenticationToken reqAuth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
		
		try {
			Authentication authenticated = manager.authenticate(reqAuth);
			
			Collection<? extends GrantedAuthority> authorities = authenticated.getAuthorities();
			
		    String token = util.generateToken(user.getEmail(), authorities);
		    
		    TokenResponse res = new TokenResponse();
		    res.setToken(token);
			ResponseEntity<TokenResponse> response = new ResponseEntity<TokenResponse>(res , HttpStatus.OK);
			
			return response;
			
		} catch (Exception e) {
			e.printStackTrace();
		    throw(e);
		}
	}
}
