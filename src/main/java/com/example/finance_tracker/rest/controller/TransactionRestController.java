package com.example.finance_tracker.rest.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.finance_tracker.dao.TransactionDao;
import com.example.finance_tracker.dao.UserDao;
import com.example.finance_tracker.entity.FtUser;
import com.example.finance_tracker.entity.Transaction;
import com.example.finance_tracker.service.AiCategorizerService;
import com.example.finance_tracker.service.AiCategorizerServicePdfAsPrompt;
import com.example.finance_tracker.service.PdfParserService;
import com.example.finance_tracker.service.TransactionService;
import com.example.finance_tracker.web.response.TransactionResponse;

@RestController
@RequestMapping("/transaction")
public class TransactionRestController {
    
	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private UserDao  userDao;
	
	@Autowired
	private TransactionDao transactionDao;
	
	@Autowired
	private PdfParserService pdfParserService;
	
	@Autowired
	private AiCategorizerService aiCategorizerService;
	
	@Autowired
	private AiCategorizerServicePdfAsPrompt aiCategorizerServicePdfAsPrompt;
	
	
	@PostMapping
	public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction ,Authentication authentication){
		  String username  = authentication.getName();
	      FtUser user  = userDao.findByEmail(username); 
		  transaction.setUser(user);
		Transaction addedTransaction = transactionService.addTransaction(transaction);
		return new ResponseEntity<Transaction>(addedTransaction , HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<Page<TransactionResponse>> getTransactionByUserId(@RequestParam int page , @RequestParam int size , @RequestParam String keyword ,  Authentication authentication){
		String username = authentication.getName();
		
        FtUser user = userDao.findByEmail(username);
        
        Pageable pageable = PageRequest.of(page, size , Sort.by("date").descending());
        
        Page<Transaction> transactions;
        
        if(keyword != null && !keyword.trim().isEmpty()) {
        	 transactions = transactionDao.findByUserAndKeyword(user, keyword, pageable);
        }else {
        	transactions = transactionDao.findByUser(user , pageable);
        }
        
        Transaction transactionEntity = new Transaction(); 
        
		Page<TransactionResponse> transactionDtoList = transactions.map(tx -> 
				                                                	 transactionEntity.convertToDto(tx) 
				                                                   );
		return new ResponseEntity<Page<TransactionResponse>>(transactionDtoList , HttpStatus.OK);
	}
	
//	@PostMapping("/uploadStatement")
//	public ResponseEntity<List<Transaction>> uploadStatement(@RequestParam("file") MultipartFile file,
//			                                 @RequestParam("userPrompt") String userPrompt,
//			                                 @RequestParam("userId") int userId,
//			                                 @RequestParam("password") String password){
//		
//		   List<Transaction> transactions = pdfParserService.extractTransactions(file, password);
//		   
//		   List<Transaction> categorized = aiCategorizerService.categorizeInBatches(transactions, userPrompt);
//	   
//		   categorized.forEach(tx -> {
//		   
//		   Optional<FtUser> o = userDao.findById(userId);
//		   tx.setUser(o.get());	   
//	   });
//		   
//		  List<TransactionResponse> savedTransactions = transactionService.addAllTransaction(categorized);
//		   
//		   return new ResponseEntity<List<Transaction>>(transactions , HttpStatus.OK);
//	}
	
	
	@PostMapping("/uploadStatement")
	public ResponseEntity<List<TransactionResponse>> uploadStatement(@RequestParam("file") MultipartFile file,
			                                 @RequestParam("userPrompt") String userPrompt,
			                                 @RequestParam("password") String password,
			                                 Authentication authentication){
		
		   byte[] decryptPdf = pdfParserService.unlockPdf(file, password);
		   
		   List<Transaction> categorized = aiCategorizerServicePdfAsPrompt.processStatement(decryptPdf, userPrompt , authentication.getName());
	        
		  Transaction transactionEntity = new Transaction();
		   
		  List<TransactionResponse> transactionsForReview = categorized.stream()
				                                                       .map(tx -> transactionEntity.convertToDto(tx) )
				                                                       .toList();
		   
		   return new ResponseEntity<List<TransactionResponse>>( transactionsForReview, HttpStatus.OK);
	}
	
	
	@PostMapping("/confirm")
	public ResponseEntity<List<TransactionResponse>> confirmAndSaveTransaction(@RequestBody List<TransactionResponse> confirmedTransaction, Authentication authentication){
		
		String loggedInUser = authentication.getName();
		
		List<TransactionResponse> savedTransaction = transactionService.saveConfirmedTransaction(confirmedTransaction, loggedInUser);
		
		return new ResponseEntity<List<TransactionResponse>>(savedTransaction , HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable int id, Authentication authentication) {
        // IMPORTANT: Verify the transaction actually belongs to the logged-in user before deleting!
        transactionService.deleteTransactionSecurely(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
	
	@PutMapping("/{id}")
	public ResponseEntity<TransactionResponse> updateTransaction(
	        @PathVariable int id, 
	        @RequestBody TransactionResponse transactionData, 
	        Authentication authentication) {
	    
	    TransactionResponse updatedTx = transactionService.updateTransactionSecurely(id, transactionData, authentication.getName());
	    return ResponseEntity.ok(updatedTx);
	}
}
