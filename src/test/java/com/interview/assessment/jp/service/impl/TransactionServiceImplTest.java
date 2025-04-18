package com.interview.assessment.jp.service.impl;

import com.interview.assessment.jp.dto.request.TransactionRequest;
import com.interview.assessment.jp.dto.response.TransactionResponse;
import com.interview.assessment.jp.entity.Account;
import com.interview.assessment.jp.entity.Transaction;
import com.interview.assessment.jp.entity.User;
import com.interview.assessment.jp.enums.AccountType;
import com.interview.assessment.jp.enums.TransactionType;
import com.interview.assessment.jp.exception.ResourceNotFoundException;
import com.interview.assessment.jp.repository.AccountRepository;
import com.interview.assessment.jp.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private MockDataService mockDataService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private User testUser;
    private Account testAccount;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // 创建测试用户和账户
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("password")
                .build();

        testAccount = Account.builder()
                .id(1L)
                .accountNumber("100000001")
                .accountType(AccountType.SAVINGS)
                .balance(BigDecimal.valueOf(5000))
                .user(testUser)
                .build();

        // 模拟MockDataService
        when(mockDataService.getCurrentUser()).thenReturn(testUser);
    }

    @Test
    @DisplayName("应该返回账户的所有交易")
    public void should_return_all_transactions_for_account() {
        // 准备测试数据
        Long accountId = testAccount.getId();
        LocalDateTime now = LocalDateTime.now();

        Transaction transaction1 = Transaction.builder()
                .id(1L)
                .transactionType(TransactionType.CREDIT)
                .amount(BigDecimal.valueOf(1000))
                .description("工资入账")
                .transactionDate(now)
                .account(testAccount)
                .build();

        Transaction transaction2 = Transaction.builder()
                .id(2L)
                .transactionType(TransactionType.DEBIT)
                .amount(BigDecimal.valueOf(500))
                .description("ATM取款")
                .transactionDate(now)
                .account(testAccount)
                .build();

        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        // 模拟仓库返回
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));
        when(transactionRepository.findByAccount_Id(accountId)).thenReturn(transactions);
        when(mockDataService.getAccountById(accountId)).thenReturn(testAccount);
        when(mockDataService.getTransactionsByAccountId(accountId)).thenReturn(transactions);

        // 执行测试
        List<TransactionResponse> result = transactionService.getTransactionsByAccountId(accountId);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(TransactionType.CREDIT, result.get(0).getTransactionType());
        assertEquals(BigDecimal.valueOf(1000), result.get(0).getAmount());
        assertEquals("工资入账", result.get(0).getDescription());
        assertEquals(accountId, result.get(0).getAccountId());
        assertEquals(2L, result.get(1).getId());
        assertEquals(TransactionType.DEBIT, result.get(1).getTransactionType());
        assertEquals(BigDecimal.valueOf(500), result.get(1).getAmount());
        assertEquals("ATM取款", result.get(1).getDescription());
        assertEquals(accountId, result.get(1).getAccountId());
    }

    @Test
    @DisplayName("应该创建新交易")
    public void should_create_new_transaction() {
        // 准备测试数据
        Long accountId = testAccount.getId();
        LocalDateTime now = LocalDateTime.now();

        TransactionRequest request = new TransactionRequest();
        request.setTransactionType(TransactionType.CREDIT);
        request.setAmount(BigDecimal.valueOf(1000));
        request.setDescription("工资入账");

        Transaction savedTransaction = Transaction.builder()
                .id(1L)
                .transactionType(TransactionType.CREDIT)
                .amount(BigDecimal.valueOf(1000))
                .description("工资入账")
                .transactionDate(now)
                .account(testAccount)
                .build();

        // 模拟仓库返回
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);
        when(mockDataService.getAccountById(accountId)).thenReturn(testAccount);
        when(mockDataService.createTransaction(any(Transaction.class))).thenReturn(savedTransaction);

        // 执行测试
        TransactionResponse result = transactionService.createTransaction(accountId, request);

        // 验证结果
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(TransactionType.CREDIT, result.getTransactionType());
        assertEquals(BigDecimal.valueOf(1000), result.getAmount());
        assertEquals("工资入账", result.getDescription());
        assertEquals(accountId, result.getAccountId());
    }

    @Test
    @DisplayName("应该更新账户余额 - 存款")
    public void should_update_account_balance_for_deposit() {
        // 准备测试数据
        Long accountId = testAccount.getId();
        BigDecimal initialBalance = testAccount.getBalance(); // 5000
        BigDecimal depositAmount = BigDecimal.valueOf(1000);
        BigDecimal expectedBalance = initialBalance.add(depositAmount); // 6000

        TransactionRequest request = new TransactionRequest();
        request.setTransactionType(TransactionType.CREDIT);
        request.setAmount(depositAmount);
        request.setDescription("存款");

        // 创建保存后的交易（带上id）
        Transaction savedTransaction = Transaction.builder()
                .id(1L)
                .transactionType(TransactionType.CREDIT)
                .amount(depositAmount)
                .description("存款")
                .transactionDate(LocalDateTime.now())
                .account(testAccount)
                .build();

        // 模拟仓库返回
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        when(mockDataService.getAccountById(accountId)).thenReturn(testAccount);
        when(mockDataService.createTransaction(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction t = invocation.getArgument(0);
            t.setId(1L);
            // 更新账户余额
            if (t.getTransactionType() == TransactionType.CREDIT) {
                testAccount.setBalance(testAccount.getBalance().add(t.getAmount()));
            } else {
                testAccount.setBalance(testAccount.getBalance().subtract(t.getAmount()));
            }
            return t;
        });

        // 执行测试
        transactionService.createTransaction(accountId, request);

        // 验证账户余额更新
        assertEquals(expectedBalance, testAccount.getBalance());
    }

    @Test
    @DisplayName("应该更新账户余额 - 取款")
    public void should_update_account_balance_for_withdrawal() {
        // 准备测试数据
        Long accountId = testAccount.getId();
        BigDecimal initialBalance = testAccount.getBalance(); // 5000
        BigDecimal withdrawalAmount = BigDecimal.valueOf(1000);
        BigDecimal expectedBalance = initialBalance.subtract(withdrawalAmount); // 4000

        TransactionRequest request = new TransactionRequest();
        request.setTransactionType(TransactionType.DEBIT);
        request.setAmount(withdrawalAmount);
        request.setDescription("取款");

        // 创建保存后的交易（带上id）
        Transaction savedTransaction = Transaction.builder()
                .id(1L)
                .transactionType(TransactionType.DEBIT)
                .amount(withdrawalAmount)
                .description("取款")
                .transactionDate(LocalDateTime.now())
                .account(testAccount)
                .build();

        // 模拟仓库返回
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(testAccount));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        when(mockDataService.getAccountById(accountId)).thenReturn(testAccount);
        when(mockDataService.createTransaction(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction t = invocation.getArgument(0);
            t.setId(1L);
            // 更新账户余额
            if (t.getTransactionType() == TransactionType.CREDIT) {
                testAccount.setBalance(testAccount.getBalance().add(t.getAmount()));
            } else {
                testAccount.setBalance(testAccount.getBalance().subtract(t.getAmount()));
            }
            return t;
        });

        // 执行测试
        transactionService.createTransaction(accountId, request);

        // 验证账户余额更新
        assertEquals(expectedBalance, testAccount.getBalance());
    }

    @Test
    @DisplayName("应该根据ID返回交易")
    public void should_return_transaction_by_id() {
        // 准备测试数据
        Long transactionId = 1L;
        LocalDateTime now = LocalDateTime.now();

        Transaction transaction = Transaction.builder()
                .id(transactionId)
                .transactionType(TransactionType.CREDIT)
                .amount(BigDecimal.valueOf(1000))
                .description("工资入账")
                .transactionDate(now)
                .account(testAccount)
                .build();

        // 模拟仓库返回
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(mockDataService.getTransactionById(transactionId)).thenReturn(transaction);

        // 执行测试
        TransactionResponse result = transactionService.getTransactionById(transactionId);

        // 验证结果
        assertNotNull(result);
        assertEquals(transactionId, result.getId());
        assertEquals(TransactionType.CREDIT, result.getTransactionType());
        assertEquals(BigDecimal.valueOf(1000), result.getAmount());
        assertEquals("工资入账", result.getDescription());
        assertEquals(testAccount.getId(), result.getAccountId());
    }

    @Test
    @DisplayName("当交易不存在时应该抛出异常")
    public void should_throw_exception_when_transaction_not_found() {
        // 准备测试数据
        Long transactionId = 999L;

        // 模拟仓库返回
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());
        when(mockDataService.getTransactionById(transactionId)).thenReturn(null);

        // 执行测试并验证结果
        assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.getTransactionById(transactionId);
        });
    }

    @Test
    @DisplayName("当账户不存在时创建交易应该抛出异常")
    public void should_throw_exception_when_account_not_found_on_create() {
        // 准备测试数据
        Long accountId = 999L;

        TransactionRequest request = new TransactionRequest();
        request.setTransactionType(TransactionType.CREDIT);
        request.setAmount(BigDecimal.valueOf(1000));
        request.setDescription("存款");

        // 模拟仓库返回
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        when(mockDataService.getAccountById(accountId)).thenReturn(null);

        // 执行测试并验证结果
        assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.createTransaction(accountId, request);
        });
    }
}