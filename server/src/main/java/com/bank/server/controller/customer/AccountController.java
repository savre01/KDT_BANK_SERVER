package com.bank.server.controller.customer;

import com.bank.server.dto.customer.AccountRequest;
import com.bank.server.dto.customer.AccountResponse;
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

    @DeleteMapping("/{accountIndex}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountIndex) {
        accountService.deleteAccount(accountIndex);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/customer/{customerIndex}")
    public ResponseEntity<?> getAccountsByCustomer(@PathVariable Long customerIndex) {
        return accountService.getAccountsByCustomerOptional(customerIndex)
            .map(accounts -> ResponseEntity.ok(
                accounts.stream().map(AccountResponse::new).toList()
            ))
            .orElse(ResponseEntity.notFound().build());
    }
}
