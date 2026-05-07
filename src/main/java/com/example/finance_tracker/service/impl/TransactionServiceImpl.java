package com.example.finance_tracker.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.finance_tracker.dao.TransactionDao;
import com.example.finance_tracker.dao.UserDao;
import com.example.finance_tracker.dao.UserRuleDao;
import com.example.finance_tracker.entity.FtUser;
import com.example.finance_tracker.entity.Transaction;
import com.example.finance_tracker.entity.UserRule;
import com.example.finance_tracker.service.TransactionService;
import com.example.finance_tracker.web.response.TransactionResponse;

import jakarta.transaction.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService  {
   
	@Autowired
	private TransactionDao transactionDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserRuleDao userRuleDao;
	
	@Override
	public Transaction addTransaction(Transaction transaction) {
		 
		return transactionDao.save(transaction);
	}

	@Override
	public List<TransactionResponse> getTransactionByUserId(int userId) {
		 Transaction transactionEntity = new Transaction();
		List<Transaction> transactions =  transactionDao.findByUserId(userId);
		List<TransactionResponse> transactionDtos =  transactions.stream()
				                                     .map(transaction -> transactionEntity.convertToDto(transaction))
				                                     .collect(Collectors.toList());
		return transactionDtos;
	}
  
	
	 public List<TransactionResponse> addAllTransaction(List<Transaction> transactions){
		 Transaction transactionEntity = new Transaction();
		 List<Transaction> added = transactionDao.saveAll(transactions);
		 List<TransactionResponse> transactionsDtos = added.stream()
				                                      .map(transaction -> transactionEntity.convertToDto(transaction))
				                                      .collect(Collectors.toList());
		 return transactionsDtos;
	 }
	 
	 
	 @Override
	 @Transactional
	 public List<TransactionResponse> saveConfirmedTransaction(List<TransactionResponse> dtoList , String username ){
		 
		 //fetch the login user from db
		 FtUser currentUser  = userDao.findByEmail(username);
		                              
		 
		 List<Transaction>  entitiesToSave = new ArrayList<>();
		 
		 for(TransactionResponse dto : dtoList) {
			  
			 if(dto.getOriginalCategory() != null && !dto.getCategory().equals(dto.getOriginalCategory())) {
				   
				 boolean ruleExists = userRuleDao.existsByUserAndTransactionDescription(currentUser, dto.getDescription());
				 
				 if(!ruleExists) {
					 UserRule rule = new UserRule();
					   rule.setTargetCategory(dto.getCategory());
					   rule.setTransactionDescription(dto.getDescription());
					   rule.setUser(currentUser);
					   userRuleDao.save(rule);
				 }
			 }
			 
			 boolean isDuplicate = transactionDao.existsByUserAndDateAndAmountAndDescription(currentUser, dto.getDate(), dto.getAmount(), dto.getDescription());
			 
			 if(!isDuplicate) {
				 Transaction tx = new Transaction();
				 tx.setId(0);
				 tx.setDate(dto.getDate());
				 tx.setAmount(dto.getAmount());
				 tx.setDescription(dto.getDescription());
				 tx.setCategory(dto.getCategory());
				 tx.setConfidence(dto.getConfidence());
				 tx.setTransactionType(dto.getTransactionType());
				 tx.setUser(currentUser);
				 
				 entitiesToSave.add(tx);
			 }
		 }
		 
		 
		 //save list to db
		 List<Transaction> savedEntities = transactionDao.saveAll(entitiesToSave);
		 
		 Transaction transactionEntity = new Transaction();
		 
		 return savedEntities.stream()
				             .map(tx -> transactionEntity.convertToDto(tx))
				             .toList();
		 
	 }

	@Override
	@Transactional
	public void deleteTransactionSecurely(int transactionId, String username) {
		// 1. Get the authenticated user
	    FtUser currentUser = userDao.findByEmail(username);
	            

	    // 2. Find the specific transaction
	    Transaction transaction = transactionDao.findById(transactionId);
	            

	    // 3. The Security Check: Does this user own this transaction?
	    if (transaction.getUser().getId() != currentUser.getId()) {
	        throw new SecurityException("You do not have permission to delete this transaction");
	    }

	    // 4. Safe to delete
	    transactionDao.delete(transaction);
		
	}
	
	@Override
	@Transactional
	public TransactionResponse updateTransactionSecurely(int id, TransactionResponse updateData, String username) {
	    FtUser currentUser = userDao.findByEmail(username);
	           

	    Transaction transaction = transactionDao.findById(id);
	           

	    if (transaction.getUser().getId() != currentUser.getId()) {
	        throw new SecurityException("You do not have permission to edit this transaction");
	    }

	    // Apply the updates
	    transaction.setDate(updateData.getDate());
	    transaction.setDescription(updateData.getDescription());
	    transaction.setAmount(updateData.getAmount());
	    transaction.setCategory(updateData.getCategory());

	    // The Confidence flag might be null if it's a manual entry, but if they edit an AI entry, 
	    // we should upgrade its confidence to HIGH because a human just verified it!
	    transaction.setConfidence("HIGH");

	    Transaction savedTx = transactionDao.save(transaction);
	    
	    Transaction transactionEntity = new Transaction();
	    // Use the same helper method you wrote earlier
	    return transactionEntity.convertToDto(savedTx);
	}
}
