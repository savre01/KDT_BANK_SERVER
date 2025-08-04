package com.bank.server.controller.customer;

import com.bank.server.dto.customer.AccountRequest;
import com.bank.server.dto.customer.AccountResponse;
import com.bank.server.model.customer.Account;
import com.bank.server.model.customer.Customer;
import com.bank.server.model.customer.Products;
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

    @PostMapping("/number")
    public ResponseEntity<?> getAccountByNumber(@RequestBody AccountRequest request) {
        return accountService.getAccountByNumber(request.getAccountNum())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/status")
    public ResponseEntity<?> getAccountsByStatus(@RequestBody AccountRequest request) {
        return accountService.getAccountsByStatusOptional(request.getAccountStatus())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerIndex}")
    public ResponseEntity<?> getAccountsByCustomer(@PathVariable Long customerIndex) {
        return accountService.getAccountsByCustomerOptional(customerIndex)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest request) {
        Account account = new Account();
        Customer customer = new Customer();
        customer.setCustomerIndex(request.getCustomerIndex());
        account.setCustomer(customer);
        if (request.getProductsIndex() != null) {
            Products product = new Products();
            product.setProductsIndex(request.getProductsIndex());
            account.setProduct(product);
        }
        account.setAccountNum(request.getAccountNum());
        account.setAccountBalance(request.getAccountBalance());
        account.setAccountCreateDate(request.getAccountCreateDate());
        account.setAccountExpirationDate(request.getAccountExpirationDate());
        account.setPaymentDay(request.getPaymentDay());
        account.setAccountStatus(request.getAccountStatus());
        return ResponseEntity.ok(new AccountResponse(accountService.createAccount(account)));
    }

}
