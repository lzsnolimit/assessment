package com.interview.assessment.jp.service.impl;

import com.interview.assessment.jp.dto.response.AccountResponse;
import com.interview.assessment.jp.entity.Account;
import com.interview.assessment.jp.entity.User;
import com.interview.assessment.jp.enums.AccountType;
import com.interview.assessment.jp.exception.ResourceNotFoundException;
import com.interview.assessment.jp.repository.AccountRepository;
import com.interview.assessment.jp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private MockDataService mockDataService;

    @InjectMocks
    private AccountServiceImpl accountService;

    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);

        // Create test user
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .build();

        // Mock security context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(testUser.getUsername());
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        // Mock MockDataService
        when(mockDataService.getCurrentUser()).thenReturn(testUser);
    }

    @Test
    @DisplayName("Should return all accounts for current user")
    public void should_return_all_accounts_for_current_user() {
        // Prepare test data
        User user = testUser;

        Account account1 = Account.builder()
                .id(1L)
                .accountNumber("100000001")
                .accountType(AccountType.SAVINGS)
                .balance(BigDecimal.valueOf(5000))
                .user(user)
                .build();

        Account account2 = Account.builder()
                .id(2L)
                .accountNumber("100000002")
                .accountType(AccountType.CHECKING)
                .balance(BigDecimal.valueOf(3000))
                .user(user)
                .build();

        List<Account> accounts = Arrays.asList(account1, account2);

        // Mock repository response
        when(accountRepository.findByUser(user)).thenReturn(accounts);
        when(mockDataService.getAccountsByUserId(user.getId())).thenReturn(accounts);

        // Execute test
        List<AccountResponse> result = accountService.getAccountsByCurrentUser();

        // Verify results
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("100000001", result.get(0).getAccountNumber());
        assertEquals(AccountType.SAVINGS, result.get(0).getAccountType());
        assertEquals(BigDecimal.valueOf(5000), result.get(0).getBalance());
        assertEquals(2L, result.get(1).getId());
        assertEquals("100000002", result.get(1).getAccountNumber());
        assertEquals(AccountType.CHECKING, result.get(1).getAccountType());
        assertEquals(BigDecimal.valueOf(3000), result.get(1).getBalance());
    }

    @Test
    @DisplayName("Should return account by ID")
    public void should_return_account_by_id() {
        // Prepare test data
        Long accountId = 1L;
        User user = testUser;

        Account account = Account.builder()
                .id(accountId)
                .accountNumber("100000001")
                .accountType(AccountType.SAVINGS)
                .balance(BigDecimal.valueOf(5000))
                .user(user)
                .build();

        // Mock repository response
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(mockDataService.getAccountById(accountId)).thenReturn(account);

        // Execute test
        AccountResponse result = accountService.getAccountById(accountId);

        // Verify results
        assertNotNull(result);
        assertEquals(accountId, result.getId());
        assertEquals("100000001", result.getAccountNumber());
        assertEquals(AccountType.SAVINGS, result.getAccountType());
        assertEquals(BigDecimal.valueOf(5000), result.getBalance());
    }

    @Test
    @DisplayName("Should throw exception when account not found")
    public void should_throw_exception_when_account_not_found() {
        // Prepare test data
        Long accountId = 999L;

        // Mock repository response
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        when(mockDataService.getAccountById(accountId)).thenReturn(null);

        // Execute test and verify results
        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.getAccountById(accountId);
        });
    }

    @Test
    @DisplayName("Should return empty list when no accounts found")
    public void should_return_empty_list_when_no_accounts_found() {
        // Prepare test data
        User user = testUser;

        // Mock repository response
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(accountRepository.findByUser(user)).thenReturn(List.of());
        when(mockDataService.getAccountsByUserId(user.getId())).thenReturn(List.of());

        // Execute test
        List<AccountResponse> result = accountService.getAccountsByCurrentUser();

        // Verify results
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}