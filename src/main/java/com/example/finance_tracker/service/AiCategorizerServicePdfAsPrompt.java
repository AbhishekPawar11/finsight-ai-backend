package com.example.finance_tracker.service;

import java.util.List;

import com.example.finance_tracker.entity.Transaction;

public interface AiCategorizerServicePdfAsPrompt {
   
  public List<Transaction> processStatement(byte[] pdfBytes , String userCustomRule , String userName);
}
