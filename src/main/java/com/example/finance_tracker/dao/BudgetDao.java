package com.example.finance_tracker.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.finance_tracker.entity.Budget;
import com.example.finance_tracker.entity.FtUser;

public interface BudgetDao extends JpaRepository<Budget, Integer>{
     
	public List<Budget> findByUser(FtUser user);
	
	public Budget findByUserAndCategory(FtUser user , String category);
}
