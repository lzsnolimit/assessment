package com.interview.assessment.jp.repository;

import com.interview.assessment.jp.entity.Account;
import com.interview.assessment.jp.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Transaction entity operations
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

        /**
         * Find all transactions by account
         * 
         * @param account the account whose transactions to find
         * @return list of transactions for the account
         */
        List<Transaction> findByAccount(Account account);

        /**
         * Find all transactions by account ID
         * 
         * @param accountId the account ID whose transactions to find
         * @return list of transactions for the account ID
         */
        List<Transaction> findByAccount_Id(Long accountId);

        /**
         * Find all transactions by account ID and transaction date between start and
         * end dates
         * 
         * @param accountId the account ID
         * @param startDate the start date
         * @param endDate   the end date
         * @return list of transactions matching the criteria
         */
        List<Transaction> findByAccount_IdAndTransactionDateBetween(
                        Long accountId, LocalDateTime startDate, LocalDateTime endDate);

        /**
         * Find transactions for a specific month and year
         * 
         * @param accountId the account ID
         * @param year      the year
         * @param month     the month (1-12)
         * @return list of transactions for the specified month and year
         */
        @Query("SELECT t FROM Transaction t WHERE t.account.id = :accountId " +
                        "AND YEAR(t.transactionDate) = :year AND MONTH(t.transactionDate) = :month " +
                        "ORDER BY t.transactionDate DESC")
        List<Transaction> findByAccountIdAndYearAndMonth(
                        @Param("accountId") Long accountId,
                        @Param("year") int year,
                        @Param("month") int month);
}