package com.example.finance_tracker.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.finance_tracker.entity.FtUser;

public interface UserDao extends JpaRepository<FtUser, Integer> {
    
	public FtUser findByEmail(String email);
	
}
