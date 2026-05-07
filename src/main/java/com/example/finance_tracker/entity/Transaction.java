package com.example.finance_tracker.entity;

import java.time.LocalDate;

import com.example.finance_tracker.web.response.TransactionResponse;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="transactions")
public class Transaction {
   
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private LocalDate date;
	
	private String description;
	
	private Double amount;
	
	private String transactionType;
	
	private String category;
	
	private String confidence;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id" , nullable = false)
	private FtUser user;
	
	public Transaction() {};

	public Transaction(int id, LocalDate date, String description, Double amount,String transactionType, String category, FtUser user , String confidence) {
		super();
		this.id = id;
		this.date = date;
		this.description = description;
		this.amount = amount;
		this.transactionType = transactionType;
		this.category = category;
		this.user = user;
	    this.confidence = confidence;
	}

	
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

	public FtUser getUser() {
		return user;
	}

	public void setUser(FtUser user) {
		this.user = user;
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

	public TransactionResponse convertToDto(Transaction transaction) {
		TransactionResponse dto = new TransactionResponse();
		dto.setId(transaction.getId());
		dto.setDate(transaction.getDate());
		dto.setAmount(transaction.getAmount());
		dto.setTransactionType(transaction.getTransactionType());
		dto.setDescription(transaction.getDescription());
		dto.setCategory(transaction.getCategory());
		dto.setUserId(transaction.getUser().getId());
		dto.setConfidence(transaction.getConfidence());
		return dto;
	}
	
	
}
