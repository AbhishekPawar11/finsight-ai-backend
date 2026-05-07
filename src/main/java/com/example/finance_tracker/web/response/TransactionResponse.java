package com.example.finance_tracker.web.response;

import java.time.LocalDate;

public class TransactionResponse {

	private int id;
	
	private LocalDate date;
	
	private String description;
	
	private Double amount;
	
	private String transactionType;
	
	private String originalCategory;

	private String category;
	
	private String confidence;
	
	
	

	private int userId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getOriginalCategory() {
		return originalCategory;
	}

	public void setOriginalCategory(String originalCategory) {
		this.originalCategory = originalCategory;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getConfidence() {
		return confidence;
	}

	public void setConfidence(String confidence) {
		this.confidence = confidence;
	}
	
	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
}
