package com.example.finance_tracker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class UserRule {
  
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String transactionDescription;
	
	private String targetCategory;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id" , nullable=false)
	private FtUser user;
     
	public UserRule() {}
	
	public UserRule(int id, String transactionDescription, String targetCategory, FtUser user) {
		super();
		this.id = id;
		this.transactionDescription = transactionDescription;
		this.targetCategory = targetCategory;
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	public String getTargetCategory() {
		return targetCategory;
	}

	public void setTargetCategory(String targetCategory) {
		this.targetCategory = targetCategory;
	}

	public FtUser getUser() {
		return user;
	}

	public void setUser(FtUser user) {
		this.user = user;
	}
	
	
	
}
