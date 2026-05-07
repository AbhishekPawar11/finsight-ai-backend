package com.example.finance_tracker.service.impl;

import java.beans.Encoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.finance_tracker.dao.UserDao;
import com.example.finance_tracker.entity.FtUser;
import com.example.finance_tracker.service.FtUserService;

@Service
public class FtUserServiceImpl implements FtUserService{
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PasswordEncoder encoder;

	@Override
	public FtUser createUser(FtUser user) {
		 user.setPassword(encoder.encode(user.getPassword()));
		FtUser createdUser = userDao.save(user);
		createdUser.setPassword(null);
		return createdUser;
	}

	@Override
	public FtUser updateUser(FtUser user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FtUser deleteUser(FtUser user) {
		// TODO Auto-generated method stub
		return null;
	}

}
