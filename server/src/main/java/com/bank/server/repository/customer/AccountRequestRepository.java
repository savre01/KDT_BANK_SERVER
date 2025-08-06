package com.bank.server.repository.customer;

import com.bank.server.model.customer.AccountRequestEntity;
import com.bank.server.model.customer.AccountRequestEntity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRequestRepository extends JpaRepository<AccountRequestEntity, Long> {
    List<AccountRequestEntity> findByStatus(RequestStatus status);
}