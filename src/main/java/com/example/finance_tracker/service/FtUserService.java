package com.example.finance_tracker.service;

import com.example.finance_tracker.entity.FtUser;

public interface FtUserService {
   
	public FtUser createUser(FtUser user);
	
	public FtUser updateUser(FtUser user);
	
	public FtUser deleteUser(FtUser user);
	
	
}
