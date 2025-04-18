package com.interview.assessment.jp.service;

import com.interview.assessment.jp.dto.request.TransactionRequest;
import com.interview.assessment.jp.dto.response.TransactionResponse;

import java.util.List;

/**
 * Transaction Service Interface
 */
public interface TransactionService {

    /**
     * Get all transactions for an account
     *
     * @param accountId Account ID
     * @return List of transactions
     */
    List<TransactionResponse> getTransactionsByAccountId(Long accountId);

    /**
     * Create a new transaction
     *
     * @param accountId Account ID
     * @param request   Transaction request
     * @return Created transaction
     */
    TransactionResponse createTransaction(Long accountId, TransactionRequest request);

    /**
     * Get transaction details by ID
     *
     * @param id Transaction ID
     * @return Transaction details
     */
    TransactionResponse getTransactionById(Long id);
}