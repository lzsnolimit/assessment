package com.interview.assessment.jp.dto.response;

import com.interview.assessment.jp.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    /**
     * Transaction ID
     */
    private Long id;

    /**
     * Transaction Type
     */
    private TransactionType transactionType;

    /**
     * Transaction Amount
     */
    private BigDecimal amount;

    /**
     * Transaction Description
     */
    private String description;

    /**
     * Transaction Date
     */
    private LocalDateTime transactionDate;

    /**
     * Account ID
     */
    private Long accountId;
}