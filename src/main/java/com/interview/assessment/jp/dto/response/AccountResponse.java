package com.interview.assessment.jp.dto.response;

import com.interview.assessment.jp.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Account Response DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
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
}