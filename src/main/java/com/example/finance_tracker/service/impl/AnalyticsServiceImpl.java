package com.example.finance_tracker.service.impl;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.finance_tracker.dao.BudgetDao;
import com.example.finance_tracker.dao.TransactionDao;
import com.example.finance_tracker.dao.UserDao;
import com.example.finance_tracker.entity.Budget;
import com.example.finance_tracker.entity.FtUser;
import com.example.finance_tracker.entity.Transaction;
import com.example.finance_tracker.service.AnalyticsService;
import com.example.finance_tracker.web.response.CategorySpendDto;
import com.example.finance_tracker.web.response.DashboardAnalyticsDto;
import com.example.finance_tracker.web.response.TransactionResponse;

@Service
public class AnalyticsServiceImpl implements AnalyticsService{
     
    private final ChatClient chatClient;
    @Autowired
	private TransactionDao transactiondao;
	
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private BudgetDao budgetDao;
    
    
    public AnalyticsServiceImpl(ChatClient.Builder builder) {
    	
    	this.chatClient = builder.build();
    }
    
	@Override
	public DashboardAnalyticsDto getMonthlyDashboard(String username, int year , int month) {
		FtUser user = userDao.findByEmail(username);
		 if(user == null) {
			  throw new RuntimeException("User Not Found");
		 }
		 
		 Double totalSpend = transactiondao.getTotalSpendForMonth(user, year , month);
		  if(totalSpend == null) totalSpend = 0.0;
		  
		 Double totalIncome = transactiondao.getTotalIncomeForMonth(user, year , month);
		  if(totalIncome == null) totalIncome = 0.0;
		  
		  
		  List<Budget> budgets = budgetDao.findByUser(user);
		  
		  List<CategorySpendDto> breakdown = transactiondao.getCategorySpendForMonth(user, year , month);
		  
		
		  List<CategorySpendDto> breakdownWithLimits  = breakdown.stream().map(dto ->{
			              
			  if(budgets != null) {
			  Double limit = budgets.stream().filter(b -> b.getCategory().equals(dto.category()))
					                      .map(Budget::getLimitAmount)
					                      .findFirst()
					                      .orElse(null);
			  return new CategorySpendDto(dto.category(), dto.totalAmount() , limit);
			  }else {
				  return new CategorySpendDto(dto.category(), dto.totalAmount());
			  }
		  }).toList();
		  
		  Transaction transactionEntity = new Transaction();
		  List<TransactionResponse> recentActivity = transactiondao.findTop5ByUserOrderByDateDesc(user)
				                                     .stream()
				                                     .map(tx-> transactionEntity.convertToDto(tx))
				                                     .toList();
		  
		return new DashboardAnalyticsDto(totalSpend, totalIncome , breakdownWithLimits, recentActivity);
	}
    
	
	@Override
	public String getAiFinancialSummary(String username, int year, int month) {
	      
		 DashboardAnalyticsDto analytics = this.getMonthlyDashboard(username, year, month);
		 
		 StringBuilder prompt = new StringBuilder();
		 
		 prompt.append("You are an expert, encouraging financial advisor. ");
	        prompt.append("Analyze this user's monthly spending data and write a short, friendly 3-sentence summary of their financial health, followed by one specific, actionable piece of advice. Do not use markdown formatting, just plain text.Please note - currency in INR.\n\n");
	        prompt.append("Month/Year: ").append(month).append("/").append(year).append("\n");
	        prompt.append("Total Income: $").append(analytics.totalMonthlyIncome()).append("\n");
	        prompt.append("Total Expenses: $").append(analytics.totalMonthlySpend()).append("\n");
	        
	        prompt.append("Top Spending Categories:\n");
	        
	        analytics.categoryBreakdown().stream().limit(5).forEach(cat -> 
	            prompt.append("- ").append(cat.category()).append(": $").append(cat.totalAmount()).append("\n")
	        );
	        
	        
	       
		 
		return chatClient.prompt(prompt.toString()).call().content();
	}

}
