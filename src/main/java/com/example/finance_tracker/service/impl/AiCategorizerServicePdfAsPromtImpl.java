package com.example.finance_tracker.service.impl;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import com.example.finance_tracker.dao.UserDao;
import com.example.finance_tracker.dao.UserRuleDao;
import com.example.finance_tracker.entity.FtUser;
import com.example.finance_tracker.entity.Transaction;
import com.example.finance_tracker.entity.UserRule;
import com.example.finance_tracker.service.AiCategorizerServicePdfAsPrompt;

@Service
public class AiCategorizerServicePdfAsPromtImpl implements AiCategorizerServicePdfAsPrompt{

	private final ChatClient chatClient;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserRuleDao userRuleDao;
	
	public AiCategorizerServicePdfAsPromtImpl(ChatClient.Builder builder) {
		 this.chatClient = builder
				           .defaultSystem("""
				                    You are a highly accurate financial data extractor. 
				                    Extract every transaction from the provided bank statement.
				                    Categorize each transaction into one of the following allowed categories:
				                    Categories allowed: [Food, Groceries, Transfer, Travel, Utilities, Entertainment, Medical, Shopping, Others].
				                    If a transaction does not fit clearly, use 'Others'.
				                    
				                    Instructions:
		                              - Return a JSON array of transactions.
		                              - For each transaction, add a "category" field with one of the categories listed above.
		                              - If you are not sure about the correct category, add a "confidence" field and set it to "LOW". Otherwise, set "confidence" to "HIGH".
		                              - Return only the JSON, no explanation.
                                      - Ingore the user field and keep it empty while returning the response
                                      - If transaction  description contains 'Paid' then in transactionType column add 'debited' or if description contains 'Received' or 'Credited' the add 'credited'.
                                      - CRITICAL DATE RULE: No matter what format the date is in the PDF (e.g., "May 06", "05/06/2025"),you MUST return the date strictly in the format "YYYY-MM-DD" (e.g., "2025-05-06").
				                    """)
				           .build();
	}
	
	@Override
	public List<Transaction> processStatement(byte[] pdfBytes, String userCustomRule , String userName) {
		
		FtUser user = userDao.findByEmail(userName);
		
		List<UserRule>  userRules = userRuleDao.findByUser(user);
		
		StringBuilder userRulesPrompt = new StringBuilder();
		if(!userRules.isEmpty()) {
			for(UserRule rule : userRules) {
				
				userRulesPrompt.append("-If description contains '")
				               .append(rule.getTransactionDescription())
				               .append("',ALWAYS categorize as '")
				               .append(rule.getTargetCategory())
				               .append(".\n");
			}
		}
		
		
		Resource pdfResource = new ByteArrayResource(pdfBytes);
		
		return chatClient.prompt()
				         .user(u-> u
				        		 .text("Please analyze this statement. Apply this custom rule: " + userCustomRule + "\n" + "Apply these historical user preferences strictly:\n" + userRulesPrompt.toString())
				        		 .media(MediaType.APPLICATION_PDF , pdfResource)
				          ).call()
				           .entity(new ParameterizedTypeReference<List<Transaction>>() {});
	
	}

}
