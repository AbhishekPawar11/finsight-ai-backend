package com.example.finance_tracker.web.response;

import java.util.List;
public record DashboardAnalyticsDto(
		Double totalMonthlySpend,
		Double totalMonthlyIncome,
		List<CategorySpendDto> categoryBreakdown,
		List<TransactionResponse> recentTransactions
		) {}
