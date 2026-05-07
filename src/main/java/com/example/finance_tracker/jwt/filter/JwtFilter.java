package com.example.finance_tracker.jwt.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.example.finance_tracker.jwt.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtFilter extends BasicAuthenticationFilter{

	
	public JwtFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request , HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
			  String jwtToken  = authorizationHeader.substring(7);
			  
			  JwtUtil util = new JwtUtil();
			  
			  String payload = util.ValidateToken(jwtToken);
			  
			  JsonParser jsonparser = JsonParserFactory.getJsonParser();
			  
			  Map<String , Object> map = jsonparser.parseMap(payload);
			  
			  String username = (String)map.get("USER_NAME");
			  
			  List<String> roles = (List<String>)map.get("ROLES");
			  
			  List<GrantedAuthority> authorities = new ArrayList<>();
			  
			   int i = roles.size()-1;
			   
			   while(i>=0) {
				   authorities.add(new SimpleGrantedAuthority(roles.get(i)));
				   i--;
			   }
			   
			   UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
			   
			   
			SecurityContext context = SecurityContextHolder.getContext();
			   
			 context.setAuthentication(auth);
		}
		 
		chain.doFilter(request, response);
	}

}
