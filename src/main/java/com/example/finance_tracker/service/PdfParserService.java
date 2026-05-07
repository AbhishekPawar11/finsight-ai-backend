package com.example.finance_tracker.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.finance_tracker.entity.Transaction;

public interface PdfParserService {
    
	public List<Transaction> extractTransactions(MultipartFile file , String Password);
	
	public byte[] unlockPdf(MultipartFile file , String password);
}
