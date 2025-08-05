package com.bank.server.controller.customer;

import com.bank.server.dto.customer.AccountRequest;
import com.bank.server.dto.customer.AccountResponse;
import com.bank.server.dto.customer.AccountPendingResponse;
import com.bank.server.model.customer.Account;
import com.bank.server.service.customer.AccountService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return ResponseEntity.ok(
            accountService.getAllAccounts().stream()
                .map(AccountResponse::new)
                .toList()
        );
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest request) {
        Account account = accountService.createAccountFromRequest(request);
        return ResponseEntity.ok(new AccountResponse(account));
    }

    @GetMapping("/{accountIndex}")
    public ResponseEntity<?> getAccountById(@PathVariable Long accountIndex) {
        return accountService.getAccountByIdOptional(accountIndex)
            .map(account -> ResponseEntity.ok(new AccountResponse(account)))
            .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/number")
    public ResponseEntity<?> getAccountByNumber(@RequestBody AccountRequest request) {
        return accountService.getAccountByNumber(request.getAccountNum())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerIndex}")
    public ResponseEntity<?> getAccountsByCustomer(@PathVariable Long customerIndex) {
        return accountService.getAccountsByCustomerOptional(customerIndex)
            .map(accounts -> ResponseEntity.ok(
                accounts.stream().map(AccountResponse::new).toList()
            ))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<AccountPendingResponse>> getPendingAccounts() {
        List<AccountPendingResponse> list = accountService.getPendingAccounts().stream()
            .map(AccountPendingResponse::new)
            .toList();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{accountIndex}/approve")
    public ResponseEntity<String> approveAccount(@PathVariable Long accountIndex) {
        accountService.approveAccount(accountIndex);
        return ResponseEntity.ok("계좌가 승인되었습니다.");
    }

    @PutMapping("/{accountIndex}/reject")
    public ResponseEntity<String> rejectAccount(@PathVariable Long accountIndex) {
        accountService.rejectAndDeleteAccount(accountIndex);
        return ResponseEntity.ok("계좌가 거절되어 삭제되었습니다.");
    }

    @DeleteMapping("/{accountIndex}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long accountIndex) {
        accountService.deleteAccount(accountIndex);
        return ResponseEntity.ok("계좌가 삭제되었습니다.");
    }
}