package com.example.finance_tracker.web.response;

public record CategorySpendDto(String category , Double totalAmount, Double limitAmount) {	
	
	public CategorySpendDto(String category, Double totalAmount) {
        // Calls the main constructor and sets limitAmount to null
        this(category, totalAmount, null);
        }
}
