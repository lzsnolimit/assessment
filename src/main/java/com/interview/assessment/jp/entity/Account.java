package com.interview.assessment.jp.entity;

import com.interview.assessment.jp.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Account Entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    /**
     * Account ID
     */
    private Long id;

    /**
     * Account Number
     */
    private String accountNumber;

    /**
     * Account Balance
     */
    private BigDecimal balance;

    /**
     * Account Type
     */
    private AccountType accountType;

    /**
     * User ID
     */
    private Long userId;

    /**
     * Created At
     */
    private LocalDateTime createdAt;

    /**
     * Updated At
     */
    private LocalDateTime updatedAt;
}