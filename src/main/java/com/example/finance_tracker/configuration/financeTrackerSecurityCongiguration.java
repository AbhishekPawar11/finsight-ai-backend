package com.example.finance_tracker.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.finance_tracker.jwt.filter.JwtFilter;

@Configuration
public class financeTrackerSecurityCongiguration {
    
	@Autowired
	private UserDetailsService userService;
	
	@Bean
	public SecurityFilterChain defaultSecurity(HttpSecurity http , AuthenticationManager manager) throws Exception {
		http.csrf(c -> c.disable());
		
		http.authorizeHttpRequests(request-> request.requestMatchers(HttpMethod.POST,"/user/register","/user/authentication").permitAll()
				                                    .requestMatchers(HttpMethod.GET , "/transaction/{userId}").permitAll()
				                                    .anyRequest().authenticated());
		
		http.userDetailsService(userService);
		
		http.sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		http.addFilter(new JwtFilter(manager));
		
		http.cors(c-> {});
		return http.build();
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration c) throws Exception {
		return c.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder getEncoder() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
}
