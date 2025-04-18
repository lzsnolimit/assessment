package com.interview.assessment.jp.repository;

import com.interview.assessment.jp.entity.Account;
import com.interview.assessment.jp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Account entity operations
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Find all accounts by user
     * 
     * @param user the user whose accounts to find
     * @return list of accounts belonging to the user
     */
    List<Account> findByUser(User user);

    /**
     * Find account by account number
     * 
     * @param accountNumber the account number to search for
     * @return an Optional containing the found account or empty
     */
    Optional<Account> findByAccountNumber(String accountNumber);

    /**
     * Find all accounts by user ID
     * 
     * @param userId the user ID whose accounts to find
     * @return list of accounts belonging to the user ID
     */
    List<Account> findByUser_Id(Long userId);

    /**
     * Check if account exists by account number
     * 
     * @param accountNumber the account number to check
     * @return true if account exists, false otherwise
     */
    boolean existsByAccountNumber(String accountNumber);
}