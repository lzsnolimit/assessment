package com.interview.assessment.jp.entity;

import com.interview.assessment.jp.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction Entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
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

    /**
     * Created At
     */
    private LocalDateTime createdAt;

    /**
     * Updated At
     */
    private LocalDateTime updatedAt;
}