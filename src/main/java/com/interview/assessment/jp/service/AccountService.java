package com.interview.assessment.jp.service;

import com.interview.assessment.jp.dto.response.AccountResponse;

import java.util.List;

/**
 * Account Service Interface
 */
public interface AccountService {

    /**
     * Get all accounts for the current user
     *
     * @return List of accounts
     */
    List<AccountResponse> getAccountsByCurrentUser();

    /**
     * Get account details by ID
     *
     * @param id Account ID
     * @return Account details
     */
    AccountResponse getAccountById(Long id);
}