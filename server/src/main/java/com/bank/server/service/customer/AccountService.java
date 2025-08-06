package com.bank.server.service.customer;

import com.bank.server.dto.customer.AccountRequest;
import com.bank.server.model.customer.Account;
import com.bank.server.model.customer.Customer;
import com.bank.server.model.customer.Products;
import com.bank.server.repository.customer.AccountRepository;
import com.bank.server.repository.customer.CustomerRepository;
import com.bank.server.repository.customer.ProductsRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final ProductsRepository productsRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountByNumber(String accountNum) {
        return accountRepository.findByAccountNum(accountNum);
    }

    public Optional<Account> getAccountByIdOptional(Long id) {
        Optional<Account> optional = accountRepository.findById(id);

        optional.ifPresent(a -> {
            a.getCustomer().getCustomerName(); // Lazy 초기화
            if (a.getProduct() != null) {
                a.getProduct().getProductName();
            }
        });
        return optional;
    }

    public Optional<List<Account>> getAccountsByCustomerOptional(Long customerIndex) {
        List<Account> result = accountRepository.findByCustomer_CustomerIndex(customerIndex);
        return result.isEmpty() ? Optional.empty() : Optional.of(result);
    }

    public List<Account> getPendingAccounts() {
        return accountRepository.findByAccountStatusIn(
                List.of(Account.AccountStatus.PENDING, Account.AccountStatus.DELETE_PENDING)
        );
    }

    @Transactional
    public Account createAccountFromRequest(AccountRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerIndex())
                .orElseThrow(() -> new IllegalArgumentException("고객을 찾을 수 없습니다."));

        Products product = productsRepository.findById(request.getProductsIndex())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        Account account = new Account();
        account.setCustomer(customer);
        account.setProduct(product);
        account.setAccountNum(request.getAccountNum());
        account.setAccountPassword(request.getAccountPassword());
        account.setAccountBalance(request.getAccountBalance());
        account.setAccountExpirationDate(null);
        account.setPaymentDay(request.getPaymentDay());

        account.setAccountStatus(
                request.getAccountStatus() != null
                        ? request.getAccountStatus()
                        : Account.AccountStatus.PENDING
        );

        return accountRepository.save(account);
    }

    @Transactional
    public void approveAccount(Long accountIndex) {
        Account account = getAccountById(accountIndex);
        Account.AccountStatus previousStatus = account.getAccountStatus();

        if (previousStatus == Account.AccountStatus.ACTIVE) {
            throw new IllegalStateException("이미 활성화된 계좌입니다.");
        }

        // 🔹 생성 승인인 경우에만 생성일 및 만료일 설정
        if (previousStatus == Account.AccountStatus.PENDING) {
            LocalDate createDate = LocalDate.now();
            account.setAccountCreateDate(createDate);

            Products product = account.getProduct();
            if (product != null && product.getProductsDuration() != null) {
                account.setAccountExpirationDate(createDate.plusMonths(product.getProductsDuration()));
            } else {
                account.setAccountExpirationDate(null);
            }
        }

        // 🔹 상태는 무조건 ACTIVE 로 변경 (PENDING 또는 DELETE_PENDING 모두)
        account.setAccountStatus(Account.AccountStatus.ACTIVE);
    }


    @Transactional
    public void rejectAndDeleteAccount(Long accountIndex) {
        Account account = getAccountById(accountIndex);
        accountRepository.delete(account);
    }
    private Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("계좌를 찾을 수 없습니다."));
    }

    @Transactional
    public void deleteAccount(Long accountIndex) {
        Account account = getAccountById(accountIndex);
        accountRepository.delete(account);
    }

}