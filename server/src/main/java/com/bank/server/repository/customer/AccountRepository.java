package com.bank.server.repository.customer;

import com.bank.server.model.customer.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCustomer_CustomerIndex(Long customerIndex);
    Optional<Account> findByAccountNum(String accountNum);
    List<Account> findByAccountStatusIn(List<Account.AccountStatus> statuses);
}
