package com.interview.assessment.jp.service.impl;

import com.interview.assessment.jp.dto.response.AccountResponse;
import com.interview.assessment.jp.entity.Account;
import com.interview.assessment.jp.entity.User;
import com.interview.assessment.jp.exception.ResourceNotFoundException;
import com.interview.assessment.jp.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Account Service Implementation
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final MockDataService mockDataService;

    @Override
    public List<AccountResponse> getAccountsByCurrentUser() {
        User currentUser = mockDataService.getCurrentUser();
        List<Account> accounts = mockDataService.getAccountsByUserId(currentUser.getId());

        return accounts.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AccountResponse getAccountById(Long id) {
        User currentUser = mockDataService.getCurrentUser();
        Account account = mockDataService.getAccountById(id);

        if (account == null) {
            throw new ResourceNotFoundException("Account not found");
        }

        // Verify account belongs to current user
        if (!account.getUserId().equals(currentUser.getId())) {
            throw new ResourceNotFoundException("Account not found");
        }

        return convertToDto(account);
    }

    /**
     * Convert entity to DTO
     *
     * @param account Account entity
     * @return Account DTO
     */
    private AccountResponse convertToDto(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .accountType(account.getAccountType())
                .build();
    }
}