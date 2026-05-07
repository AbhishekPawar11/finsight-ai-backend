package com.example.finance_tracker.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_budget")
public class Budget {
  
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private FtUser user;

    @Column(nullable = false)
    private String category;

    @Column(name = "limit_amount", nullable = false)
    private Double limitAmount;
    
    public Budget() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public Double getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(Double limitAmount) {
		this.limitAmount = limitAmount;
	}

	public Budget(int id, FtUser user, String category, Double limitAmount) {
		super();
		this.id = id;
		this.user = user;
		this.category = category;
		this.limitAmount = limitAmount;
	}
    
    
}
