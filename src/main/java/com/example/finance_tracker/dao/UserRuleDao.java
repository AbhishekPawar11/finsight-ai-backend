package com.example.finance_tracker.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.finance_tracker.entity.FtUser;
import com.example.finance_tracker.entity.UserRule;

public interface UserRuleDao extends JpaRepository<UserRule, Integer> {
      
	public boolean existsByUserAndTransactionDescription(FtUser user , String TransactionDescription);

	public List<UserRule> findByUser(FtUser user);
	
}
