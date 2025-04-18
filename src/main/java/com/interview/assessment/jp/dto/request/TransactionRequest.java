package com.interview.assessment.jp.dto.request;

import com.interview.assessment.jp.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Transaction Request DTO
 */
@Data
public class TransactionRequest {
    /**
     * Transaction Type
     */
    @NotNull(message = "Transaction type cannot be null")
    private TransactionType transactionType;

    /**
     * Transaction Amount
     */
    @NotNull(message = "Transaction amount cannot be null")
    @Positive(message = "Transaction amount must be positive")
    private BigDecimal amount;

    /**
     * Transaction Description
     */
    @NotBlank(message = "Transaction description cannot be blank")
    private String description;
}