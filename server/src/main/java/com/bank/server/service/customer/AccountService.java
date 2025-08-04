package com.bank.server.service.customer;

import com.bank.server.model.customer.Account;
import com.bank.server.repository.customer.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountByNumber(String accountNum) {
        return accountRepository.findByAccountNum(accountNum);
    }

    public Optional<List<Account>> getAccountsByStatusOptional(String status) {
        List<Account> result = accountRepository.findByAccountStatus(status);
        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }

    public Optional<List<Account>> getAccountsByCustomerOptional(Long customerIndex) {
        List<Account> result = accountRepository.findByCustomer_CustomerIndex(customerIndex);
        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }
}