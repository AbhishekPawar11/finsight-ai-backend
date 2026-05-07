package com.example.finance_tracker.service;

import java.util.List;

import com.example.finance_tracker.entity.Transaction;
import com.example.finance_tracker.web.response.TransactionResponse;

public interface TransactionService {
   
	public Transaction addTransaction(Transaction transaction);
	
	public List<TransactionResponse> getTransactionByUserId(int userId);
	
	public List<TransactionResponse> addAllTransaction(List<Transaction> transactions);

	public List<TransactionResponse> saveConfirmedTransaction(List<TransactionResponse> dtoList , String username );
	
	public void deleteTransactionSecurely(int transactionId, String username);
	
	public TransactionResponse updateTransactionSecurely(int id, TransactionResponse updateData, String username);
}
