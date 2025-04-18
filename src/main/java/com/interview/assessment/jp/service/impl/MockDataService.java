package com.interview.assessment.jp.service.impl;

import com.interview.assessment.jp.entity.Account;
import com.interview.assessment.jp.entity.Transaction;
import com.interview.assessment.jp.entity.User;
import com.interview.assessment.jp.enums.AccountType;
import com.interview.assessment.jp.enums.TransactionType;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Mock Data Service
 */
@Service
public class MockDataService {

        private final ConcurrentHashMap<Long, User> userMap = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<Long, Account> accountMap = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<Long, Transaction> transactionMap = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<Long, List<Transaction>> accountTransactionsMap = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<Long, List<Account>> userAccountsMap = new ConcurrentHashMap<>();

        private final AtomicLong userIdGenerator = new AtomicLong(1);
        private final AtomicLong accountIdGenerator = new AtomicLong(1);
        private final AtomicLong transactionIdGenerator = new AtomicLong(1);

        /**
         * Initialize mock data
         */
        @PostConstruct
        public void init() {
                // Create users
                User user1 = User.builder()
                                .id(userIdGenerator.getAndIncrement())
                                .username("user1")
                                .password("$2a$10$X7uKQvU9umcaHbKl0.uZS.B5o9.D8Ff5UlE/mfm2iDQMIFhsZcqJy") // encrypted
                                                                                                          // "password"
                                .roles(Arrays.asList("USER", "new_app_role"))
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();

                User user2 = User.builder()
                                .id(userIdGenerator.getAndIncrement())
                                .username("user2")
                                .password("$2a$10$X7uKQvU9umcaHbKl0.uZS.B5o9.D8Ff5UlE/mfm2iDQMIFhsZcqJy") // encrypted
                                                                                                          // "password"
                                .roles(Arrays.asList("USER"))
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();

                userMap.put(user1.getId(), user1);
                userMap.put(user2.getId(), user2);

                // Create accounts for user1
                Account savingsAccount = Account.builder()
                                .id(accountIdGenerator.getAndIncrement())
                                .accountNumber("SAV-" + System.currentTimeMillis())
                                .balance(new BigDecimal("5000.00"))
                                .accountType(AccountType.SAVINGS)
                                .userId(user1.getId())
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();

                Account checkingAccount = Account.builder()
                                .id(accountIdGenerator.getAndIncrement())
                                .accountNumber("CHK-" + System.currentTimeMillis())
                                .balance(new BigDecimal("2500.50"))
                                .accountType(AccountType.CHECKING)
                                .userId(user1.getId())
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();

                Account creditAccount = Account.builder()
                                .id(accountIdGenerator.getAndIncrement())
                                .accountNumber("CRE-" + System.currentTimeMillis())
                                .balance(new BigDecimal("1000.00"))
                                .accountType(AccountType.CREDIT)
                                .userId(user1.getId())
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();

                accountMap.put(savingsAccount.getId(), savingsAccount);
                accountMap.put(checkingAccount.getId(), checkingAccount);
                accountMap.put(creditAccount.getId(), creditAccount);

                List<Account> user1Accounts = new ArrayList<>();
                user1Accounts.add(savingsAccount);
                user1Accounts.add(checkingAccount);
                user1Accounts.add(creditAccount);
                userAccountsMap.put(user1.getId(), user1Accounts);

                // Create accounts for user2
                Account user2SavingsAccount = Account.builder()
                                .id(accountIdGenerator.getAndIncrement())
                                .accountNumber("SAV-" + System.currentTimeMillis())
                                .balance(new BigDecimal("3000.00"))
                                .accountType(AccountType.SAVINGS)
                                .userId(user2.getId())
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();

                accountMap.put(user2SavingsAccount.getId(), user2SavingsAccount);

                List<Account> user2Accounts = new ArrayList<>();
                user2Accounts.add(user2SavingsAccount);
                userAccountsMap.put(user2.getId(), user2Accounts);

                // Create transactions for savings account
                List<Transaction> savingsTransactions = new ArrayList<>();

                Transaction transaction1 = Transaction.builder()
                                .id(transactionIdGenerator.getAndIncrement())
                                .transactionType(TransactionType.CREDIT)
                                .amount(new BigDecimal("1000.00"))
                                .description("Salary payment")
                                .transactionDate(LocalDateTime.now().minusDays(5))
                                .accountId(savingsAccount.getId())
                                .createdAt(LocalDateTime.now().minusDays(5))
                                .updatedAt(LocalDateTime.now().minusDays(5))
                                .build();

                Transaction transaction2 = Transaction.builder()
                                .id(transactionIdGenerator.getAndIncrement())
                                .transactionType(TransactionType.DEBIT)
                                .amount(new BigDecimal("500.00"))
                                .description("Shopping expense")
                                .transactionDate(LocalDateTime.now().minusDays(3))
                                .accountId(savingsAccount.getId())
                                .createdAt(LocalDateTime.now().minusDays(3))
                                .updatedAt(LocalDateTime.now().minusDays(3))
                                .build();

                Transaction transaction3 = Transaction.builder()
                                .id(transactionIdGenerator.getAndIncrement())
                                .transactionType(TransactionType.CREDIT)
                                .amount(new BigDecimal("200.00"))
                                .description("Refund")
                                .transactionDate(LocalDateTime.now().minusDays(1))
                                .accountId(savingsAccount.getId())
                                .createdAt(LocalDateTime.now().minusDays(1))
                                .updatedAt(LocalDateTime.now().minusDays(1))
                                .build();

                transactionMap.put(transaction1.getId(), transaction1);
                transactionMap.put(transaction2.getId(), transaction2);
                transactionMap.put(transaction3.getId(), transaction3);

                savingsTransactions.add(transaction1);
                savingsTransactions.add(transaction2);
                savingsTransactions.add(transaction3);
                accountTransactionsMap.put(savingsAccount.getId(), savingsTransactions);

                // Create transactions for checking account
                List<Transaction> checkingTransactions = new ArrayList<>();

                Transaction transaction4 = Transaction.builder()
                                .id(transactionIdGenerator.getAndIncrement())
                                .transactionType(TransactionType.DEBIT)
                                .amount(new BigDecimal("300.00"))
                                .description("Restaurant bill")
                                .transactionDate(LocalDateTime.now().minusDays(4))
                                .accountId(checkingAccount.getId())
                                .createdAt(LocalDateTime.now().minusDays(4))
                                .updatedAt(LocalDateTime.now().minusDays(4))
                                .build();

                Transaction transaction5 = Transaction.builder()
                                .id(transactionIdGenerator.getAndIncrement())
                                .transactionType(TransactionType.DEBIT)
                                .amount(new BigDecimal("150.00"))
                                .description("Movie tickets")
                                .transactionDate(LocalDateTime.now().minusDays(2))
                                .accountId(checkingAccount.getId())
                                .createdAt(LocalDateTime.now().minusDays(2))
                                .updatedAt(LocalDateTime.now().minusDays(2))
                                .build();

                transactionMap.put(transaction4.getId(), transaction4);
                transactionMap.put(transaction5.getId(), transaction5);

                checkingTransactions.add(transaction4);
                checkingTransactions.add(transaction5);
                accountTransactionsMap.put(checkingAccount.getId(), checkingTransactions);
        }

        /**
         * Get current user
         * 
         * @return Current user
         */
        public User getCurrentUser() {
                // Mock current user as user1
                return userMap.get(1L);
        }

        /**
         * Get accounts by user ID
         * 
         * @param userId User ID
         * @return List of accounts
         */
        public List<Account> getAccountsByUserId(Long userId) {
                return userAccountsMap.getOrDefault(userId, new ArrayList<>());
        }

        /**
         * Get account by ID
         * 
         * @param id Account ID
         * @return Account
         */
        public Account getAccountById(Long id) {
                return accountMap.get(id);
        }

        /**
         * Get transactions by account ID
         * 
         * @param accountId Account ID
         * @return List of transactions
         */
        public List<Transaction> getTransactionsByAccountId(Long accountId) {
                return accountTransactionsMap.getOrDefault(accountId, new ArrayList<>());
        }

        /**
         * Get transaction by ID
         * 
         * @param id Transaction ID
         * @return Transaction
         */
        public Transaction getTransactionById(Long id) {
                return transactionMap.get(id);
        }

        /**
         * Create new transaction
         * 
         * @param transaction Transaction
         * @return Created transaction
         */
        public Transaction createTransaction(Transaction transaction) {
                transaction.setId(transactionIdGenerator.getAndIncrement());
                transaction.setCreatedAt(LocalDateTime.now());
                transaction.setUpdatedAt(LocalDateTime.now());

                transactionMap.put(transaction.getId(), transaction);

                List<Transaction> transactions = accountTransactionsMap.getOrDefault(transaction.getAccountId(),
                                new ArrayList<>());
                transactions.add(transaction);
                accountTransactionsMap.put(transaction.getAccountId(), transactions);

                // Update account balance
                Account account = accountMap.get(transaction.getAccountId());
                if (account != null) {
                        if (transaction.getTransactionType() == TransactionType.CREDIT) {
                                account.setBalance(account.getBalance().add(transaction.getAmount()));
                        } else if (transaction.getTransactionType() == TransactionType.DEBIT) {
                                account.setBalance(account.getBalance().subtract(transaction.getAmount()));
                        }
                        account.setUpdatedAt(LocalDateTime.now());
                        accountMap.put(account.getId(), account);
                }

                return transaction;
        }
}