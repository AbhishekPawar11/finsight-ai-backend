package com.example.finance_tracker.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.finance_tracker.entity.FtUser;
import com.example.finance_tracker.entity.Transaction;
import com.example.finance_tracker.web.response.CategorySpendDto;

public interface TransactionDao extends JpaRepository<Transaction, Integer> {
   
	public List<Transaction> findByUserId(int userId);
	
	public Transaction findById(int id);
	
	public Page<Transaction> findByUser(FtUser user ,Pageable pageable);
	
	public boolean existsByUserAndDateAndAmountAndDescription(
			  FtUser user,
			  LocalDate date,
			  Double amount,
			  String description
			);
	
	
	@Query("""
			SELECT new com.example.finance_tracker.web.response.CategorySpendDto(t.category , SUM(t.amount))
			FROM Transaction t
			WHERE t.user = :user AND YEAR(t.date) = :year AND MONTH(t.date) = :month AND t.transactionType = 'debited'
			GROUP BY t.category
			ORDER BY SUM(t.amount) DESC
			""")
	List<CategorySpendDto> getCategorySpendForMonth(@Param("user") FtUser user , @Param("year") int year, @Param("month") int month);
	
	@Query("""
			SELECT SUM(t.amount)
			FROM Transaction t
			WHERE t.user = :user AND YEAR(t.date) = :year AND MONTH(t.date) = :month AND t.transactionType = 'debited'
			""")
	Double getTotalSpendForMonth(@Param("user") FtUser user , @Param("year") int year, @Param("month") int month);
	
	@Query(""" 
			SELECT SUM(t.amount)
			FROM Transaction t
			WHERE t.user = :user AND YEAR(t.date) = :year AND MONTH(t.date) = :month AND t.transactionType = 'credited'
			""")
	Double getTotalIncomeForMonth(@Param("user") FtUser user, @Param("year") int year, @Param("month") int month);
	
	public List<Transaction> findTop5ByUserOrderByDateDesc(FtUser user);
	
	
	@Query("SELECT t FROM Transaction t WHERE t.user = :user AND " +
		       "(LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
		       "OR LOWER(t.category) LIKE LOWER(CONCAT('%', :keyword, '%')))")
		Page<Transaction> findByUserAndKeyword(@Param("user") FtUser user, @Param("keyword") String keyword, Pageable pageable);
}
