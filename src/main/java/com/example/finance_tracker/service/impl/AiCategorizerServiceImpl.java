package com.example.finance_tracker.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.finance_tracker.entity.Transaction;
import com.example.finance_tracker.service.AiCategorizerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AiCategorizerServiceImpl implements AiCategorizerService {
	
//	@Autowired
//	private ChatClient chatClient;
 

	@Override
	public List<Transaction> categorizeInBatches(List<Transaction> transactions, String userPrompt) {
		  List<List<Transaction>> batches = partitionList(transactions, 15);
		  
		  List<Transaction> allCategorized = new ArrayList<>();
		  for(List<Transaction> batch : batches) {
			  
			  String prompt = buildPromptWithUserRule(batch, userPrompt);
			  
			  //String aiResponse = chatClient.prompt().user(prompt).call().content();
			  //List<Transaction> categorizedBatch = parseCategorizedJson(aiResponse);
			  
			  //allCategorized.addAll(categorizedBatch);
			  return null;
		  }
		
		return allCategorized;
	}
	
	
	public List<List<Transaction>> partitionList(List<Transaction> transactions , int batchSize ){
		 List<List<Transaction>> batches = new ArrayList<>();
		for(int i = 0; i<transactions.size();i+=batchSize) {
			   
			  int end = Math.min(i+batchSize, transactions.size());
			   
			  batches.add(transactions.subList(i, end));
		}
		return batches;
	}
	
	public String buildPromptWithUserRule(List<Transaction> transactions , String userPrompt) {
		 String jsonPayload = convertToJson(transactions);
		 
		 return"""
		            You are a financial assistant.

		            Categorize each transaction from the list below into one of the following categories:
		            [Food, Groceries, Transfer, Travel, Utilities, Entertainment, Medical, Shopping, Others].

		            Also, use the user's custom rule provided below to help with more accurate categorization.

		            User Rule:
		            "%s"

		            Instructions:
		            - Return a JSON array of transactions.
		            - For each transaction, add a "category" field with one of the categories listed above.
		            - If you are not sure about the correct category, add a "confidence" field and set it to "LOW". Otherwise, set "confidence" to "HIGH".
		            - Return only the JSON, no explanation.

		            Transactions:
		            %s
		            """.formatted(userPrompt, jsonPayload); 
	}
	
	
	public String convertToJson(List<Transaction> transactions) {
		try {
			ObjectMapper om = new ObjectMapper();
			return om.writeValueAsString(transactions);
		}catch(JsonProcessingException e) {
			throw new RuntimeException("Failed to convert transactions to JSON", e);
		}
		
		
	}
	
	public List<Transaction> parseCategorizedJson(String aiResponse){
		try {
			ObjectMapper om = new ObjectMapper();
			return Arrays.asList(om.readValue(aiResponse, Transaction[].class));
		} catch (Exception e) {
			throw new RuntimeException("Failed to parse AI response JSON", e);
		}
	}
	

}
