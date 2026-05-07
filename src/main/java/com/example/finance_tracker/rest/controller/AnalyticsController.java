package com.example.finance_tracker.rest.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.finance_tracker.service.AnalyticsService;
import com.example.finance_tracker.web.response.DashboardAnalyticsDto;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {
    
	@Autowired
	private AnalyticsService analyticsService;
	
	@GetMapping("/dashboard")
	public ResponseEntity<DashboardAnalyticsDto> getDashboardData(@RequestParam("year") int year , @RequestParam("month") int month , Authentication authentication){
		
		String username = authentication.getName();
		  
		DashboardAnalyticsDto dashboardData = analyticsService.getMonthlyDashboard(username, year , month);
		
		return ResponseEntity.ok(dashboardData);
	}
	
	
	@GetMapping("/summary")
	public ResponseEntity<Map<String , String>> generateAiSummary(@RequestParam int year , @RequestParam int month , Authentication authentication) {
		
		String username = authentication.getName();
		
		String summary = analyticsService.getAiFinancialSummary(username, year, month);
		
		return ResponseEntity.ok(Map.of("summary", summary));
	}
	
	
	
}
