package com.interview.assessment.jp.controller.v1;

import com.interview.assessment.jp.dto.response.AccountResponse;
import com.interview.assessment.jp.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Account Controller
 */
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "Account related APIs")
public class AccountController {

    private final AccountService accountService;

    /**
     * Get all accounts of the current user
     *
     * @return List of accounts
     */
    @GetMapping
    @Operation(summary = "Get all accounts of the current user", description = "Returns the list of all accounts for the current user")
    public ResponseEntity<List<AccountResponse>> getAccounts() {
        return ResponseEntity.ok(accountService.getAccountsByCurrentUser());
    }

    /**
     * Get account details by ID
     *
     * @param id Account ID
     * @return Account details
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get account details by ID", description = "Returns account details for the given ID")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }
}