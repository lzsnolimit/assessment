package com.interview.assessment.jp.service.impl;

import com.interview.assessment.jp.dto.request.TransactionRequest;
import com.interview.assessment.jp.dto.response.TransactionResponse;
import com.interview.assessment.jp.entity.Account;
import com.interview.assessment.jp.entity.Transaction;
import com.interview.assessment.jp.entity.User;
import com.interview.assessment.jp.enums.TransactionType;
import com.interview.assessment.jp.exception.ResourceNotFoundException;
import com.interview.assessment.jp.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Transaction Service Implementation
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final MockDataService mockDataService;

    @Override
    public List<TransactionResponse> getTransactionsByAccountId(Long accountId) {
        User currentUser = mockDataService.getCurrentUser();
        Account account = mockDataService.getAccountById(accountId);

        if (account == null) {
            throw new ResourceNotFoundException("Account not found");
        }

        // Verify account belongs to current user
        if (!account.getUserId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException("Account not found");
        }

        List<Transaction> transactions = mockDataService.getTransactionsByAccountId(accountId);

        return transactions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionResponse createTransaction(Long accountId, TransactionRequest request) {
        User currentUser = mockDataService.getCurrentUser();
        Account account = mockDataService.getAccountById(accountId);

        if (account == null) {
            throw new ResourceNotFoundException("Account not found");
        }

        // Verify account belongs to current user
        if (!account.getUserId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException("Account not found");
        }

        // For debit transactions, verify sufficient balance
        if (request.getTransactionType() == TransactionType.DEBIT) {
            if (account.getBalance().compareTo(request.getAmount()) < 0) {
                throw new IllegalArgumentException("Insufficient account balance");
            }
        }

        // Create transaction
        Transaction transaction = Transaction.builder()
                .transactionType(request.getTransactionType())
                .amount(request.getAmount())
                .description(request.getDescription())
                .transactionDate(LocalDateTime.now())
                .accountId(accountId)
                .build();

        Transaction createdTransaction = mockDataService.createTransaction(transaction);

        return convertToDto(createdTransaction);
    }

    @Override
    public TransactionResponse getTransactionById(Long id) {
        User currentUser = mockDataService.getCurrentUser();
        Transaction transaction = mockDataService.getTransactionById(id);

        if (transaction == null) {
            throw new ResourceNotFoundException("Transaction not found");
        }

        // Verify transaction belongs to current user's account
        Account account = mockDataService.getAccountById(transaction.getAccountId());
        if (!account.getUserId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException("Transaction not found");
        }

        return convertToDto(transaction);
    }

    /**
     * Convert entity to DTO
     *
     * @param transaction Transaction entity
     * @return Transaction DTO
     */
    private TransactionResponse convertToDto(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .transactionType(transaction.getTransactionType())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate())
                .accountId(transaction.getAccountId())
                .build();
    }
}