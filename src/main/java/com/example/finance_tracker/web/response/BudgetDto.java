package com.example.finance_tracker.web.response;

import com.example.finance_tracker.entity.FtUser;

public class BudgetDto {
  
	private FtUser user;
	
	private String category;
	
	private Double limit;

	public FtUser getUser() {
		return user;
	}

	public void setUser(FtUser user) {
		this.user = user;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Double getLimit() {
		return limit;
	}

	public void setLimit(Double limit) {
		this.limit = limit;
	}
	
	
	
}
