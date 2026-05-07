package com.example.finance_tracker.service;

import com.example.finance_tracker.web.response.DashboardAnalyticsDto;

public interface AnalyticsService {
    
	public DashboardAnalyticsDto getMonthlyDashboard(String username, int year , int month);
	
	public String getAiFinancialSummary(String username , int year , int month);
	
}
