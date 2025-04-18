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

        // 创建测试用户
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .build();

        // 模拟安全上下文
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(testUser.getUsername());
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        // 模拟MockDataService
        when(mockDataService.getCurrentUser()).thenReturn(testUser);
    }

    @Test
    @DisplayName("应该返回当前用户的所有账户")
    public void should_return_all_accounts_for_current_user() {
        // 准备测试数据
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

        // 模拟仓库返回
        when(accountRepository.findByUser(user)).thenReturn(accounts);
        when(mockDataService.getAccountsByUserId(user.getId())).thenReturn(accounts);

        // 执行测试
        List<AccountResponse> result = accountService.getAccountsByCurrentUser();

        // 验证结果
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
    @DisplayName("应该根据ID返回账户")
    public void should_return_account_by_id() {
        // 准备测试数据
        Long accountId = 1L;
        User user = testUser;

        Account account = Account.builder()
                .id(accountId)
                .accountNumber("100000001")
                .accountType(AccountType.SAVINGS)
                .balance(BigDecimal.valueOf(5000))
                .user(user)
                .build();

        // 模拟仓库返回
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(mockDataService.getAccountById(accountId)).thenReturn(account);

        // 执行测试
        AccountResponse result = accountService.getAccountById(accountId);

        // 验证结果
        assertNotNull(result);
        assertEquals(accountId, result.getId());
        assertEquals("100000001", result.getAccountNumber());
        assertEquals(AccountType.SAVINGS, result.getAccountType());
        assertEquals(BigDecimal.valueOf(5000), result.getBalance());
    }

    @Test
    @DisplayName("当账户不存在时应该抛出异常")
    public void should_throw_exception_when_account_not_found() {
        // 准备测试数据
        Long accountId = 999L;

        // 模拟仓库返回
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        when(mockDataService.getAccountById(accountId)).thenReturn(null);

        // 执行测试并验证结果
        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.getAccountById(accountId);
        });
    }

    @Test
    @DisplayName("当没有找到账户时应该返回空列表")
    public void should_return_empty_list_when_no_accounts_found() {
        // 准备测试数据
        User user = testUser;

        // 模拟仓库返回
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(accountRepository.findByUser(user)).thenReturn(List.of());
        when(mockDataService.getAccountsByUserId(user.getId())).thenReturn(List.of());

        // 执行测试
        List<AccountResponse> result = accountService.getAccountsByCurrentUser();

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}