package com.example.finance_tracker.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.finance_tracker.dao.UserDao;
import com.example.finance_tracker.entity.Role;
import com.example.finance_tracker.entity.FtUser;

@Service
public class FtUserDetailsService implements UserDetailsService {
  
	@Autowired
	private UserDao userdao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		 FtUser user = userdao.findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException(username);
		}
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		
		List<Role> roles = user.getRoles();
		for(Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getRole()));
		}
		
		User authenticatedUser = new User(user.getEmail() , user.getPassword(),authorities);
		
		return authenticatedUser;
	}

}
