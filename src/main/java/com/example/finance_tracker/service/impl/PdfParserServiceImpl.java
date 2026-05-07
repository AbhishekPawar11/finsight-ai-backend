package com.example.finance_tracker.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.finance_tracker.entity.Transaction;
import com.example.finance_tracker.service.PdfParserService;

@Service
public class PdfParserServiceImpl implements PdfParserService {

	@Override
	public List<Transaction> extractTransactions(MultipartFile file, String password) {
		 List<Transaction> transactions = new ArrayList<>();
		 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH);
		 
		 try(InputStream input = file.getInputStream();
		     PDDocument document = PDDocument.load(input, password);){
			 
			 PDFTextStripper stripper = new PDFTextStripper();
			 String text = stripper.getText(document);
			 String[] lines = text.split("\\r?\\n");
			 
			 Transaction currentTransaction = null;
			 
			 for(int i = 0;i <lines.length;i++){
				 String line = lines[i].trim();
				 
				 if(line.matches("^[A-Za-z]{3} \\d{1,2}, \\d{4}$")) {
					  if(currentTransaction != null) {
						  transactions.add(currentTransaction);
					  }
					  
					  currentTransaction = new Transaction();
					  currentTransaction.setDate(LocalDate.parse(line,formatter));
					  currentTransaction.setCategory("Uncategorized");
				 } else if(line.startsWith("Paid to") || line.startsWith("Received from")) {
					 
					 if (currentTransaction != null) {
		                    currentTransaction.setDescription(line);
		                }
					 
				 }else if(line.matches("(?i).*Debit INR.*") || line.matches("(?i).*Credit INR.*")) {
					  if(currentTransaction != null) { 
						  if(line != null && !line.trim().isEmpty()) {
							  try {
					 String type = line.toLowerCase().contains("debit") ? "debit" : "credit";
					  String amtStr = line.replaceAll("[^\\d.]", "");
					  double amount = Double.parseDouble(amtStr);
					  currentTransaction.setAmount(type.equals("debit") ?  -amount : amount );
							  }
					  catch (NumberFormatException e) {
					        System.out.println("Invalid number format: " + line);
					        continue; // or handle gracefully
					    }
						  }
					  }
				 }
				 
			 }
			 if (currentTransaction != null) {
		            transactions.add(currentTransaction);
		        }
		 } catch(Exception e) {
			 throw new RuntimeException("Failed to parse password-protected PDF: " + e.getMessage(), e);
		 }
		 
		 return transactions;
		
	}
	
	
	public byte[] unlockPdf(MultipartFile file , String password) {
		
		   try(InputStream input = file.getInputStream();
		     PDDocument document = PDDocument.load(input, password);){
			   
			   document.setAllSecurityToBeRemoved(true);
			   ByteArrayOutputStream baos = new ByteArrayOutputStream();
			   
			   document.save(baos);
			   
			   return baos.toByteArray();
		   }catch(Exception e) {
			   throw new RuntimeException("Failed to parse password-protected PDF: " + e.getMessage(), e);
		   }
	}

}
