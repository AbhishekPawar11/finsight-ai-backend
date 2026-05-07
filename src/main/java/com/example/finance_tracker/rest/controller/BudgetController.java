package com.example.finance_tracker.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.finance_tracker.dao.BudgetDao;
import com.example.finance_tracker.dao.UserDao;
import com.example.finance_tracker.entity.Budget;
import com.example.finance_tracker.entity.FtUser;
import com.example.finance_tracker.web.response.BudgetDto;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/budget")
public class BudgetController {
    
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private BudgetDao budgetDao;
	
	@PostMapping
    @Transactional
    public ResponseEntity<Void> setBudget(@RequestBody BudgetDto req, Authentication authentication) {
		
        FtUser user = userDao.findByEmail(authentication.getName());

        // Check if a budget for this category already exists
        Budget budget = budgetDao.findByUserAndCategory(user, req.getCategory());
        
           if(budget == null) { 
        	   budget = new Budget();
           }

        budget.setUser(user);
        budget.setCategory(req.getCategory());
        budget.setLimitAmount(req.getLimit());

        budgetDao.save(budget);
        return ResponseEntity.ok().build();
    }
}
