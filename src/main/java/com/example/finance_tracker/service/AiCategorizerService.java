package com.example.finance_tracker.service;

import java.util.List;

import com.example.finance_tracker.entity.Transaction;

public interface AiCategorizerService {
  
	public List<Transaction> categorizeInBatches(List<Transaction> transactions , String userPrompt);
}
