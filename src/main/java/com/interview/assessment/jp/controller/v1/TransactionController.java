package com.interview.assessment.jp.controller.v1;

import com.interview.assessment.jp.dto.request.TransactionRequest;
import com.interview.assessment.jp.dto.response.TransactionResponse;
import com.interview.assessment.jp.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Transaction Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "Transactions", description = "Transaction related APIs")
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Get all transactions for an account
     *
     * @param accountId Account ID
     * @return List of transactions
     */
    @GetMapping("/accounts/{accountId}/transactions")
    @Operation(summary = "Get all transactions for an account", description = "Returns a list of all transactions for the specified account")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByAccountId(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getTransactionsByAccountId(accountId));
    }

    /**
     * Create a new transaction
     *
     * @param accountId Account ID
     * @param request   Transaction request data
     * @return Created transaction
     */
    @PostMapping("/accounts/{accountId}/transactions")
    @Operation(summary = "Create a new transaction", description = "Creates a new transaction for the specified account")
    public ResponseEntity<TransactionResponse> createTransaction(@PathVariable Long accountId,
            @Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(transactionService.createTransaction(accountId, request));
    }

    /**
     * Get transaction details
     *
     * @param id Transaction ID
     * @return Transaction details
     */
    @GetMapping("/transactions/{id}")
    @Operation(summary = "Get transaction details", description = "Returns transaction details for the given ID")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }
}