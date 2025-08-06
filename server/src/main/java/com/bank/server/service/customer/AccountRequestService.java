package com.bank.server.service.customer;

import com.bank.server.dto.customer.AccountRequestRequest;
import com.bank.server.model.customer.*;
import com.bank.server.repository.customer.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountRequestService {

    private final AccountRequestRepository requestRepository;
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final ProductsRepository productsRepository;

    public List<AccountRequestEntity> getPendingRequests() {
        return requestRepository.findByStatus(AccountRequestEntity.RequestStatus.PENDING);
    }

    @Transactional
    public AccountRequestEntity createRequest(AccountRequestRequest dto) {
        Customer customer = customerRepository.findById(dto.getCustomerIndex())
                .orElseThrow(() -> new IllegalArgumentException("고객을 찾을 수 없습니다."));

        Products product = productsRepository.findById(dto.getProductsIndex())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        Account target = null;
        if (dto.getAccountIndex() != null) {
            target = accountRepository.findById(dto.getAccountIndex())
                    .orElseThrow(() -> new IllegalArgumentException("해당 계좌를 찾을 수 없습니다."));
        }

        AccountRequestEntity request = AccountRequestEntity.builder()
                .customer(customer)
                .products(product)
                .account(target) 
                .type(dto.getRequestType())
                .status(AccountRequestEntity.RequestStatus.PENDING)
                .accountNum(dto.getAccountNum())        
                .accountPassword(dto.getAccountPassword()) 
                .accountBalance(dto.getAccountBalance())   
                .paymentDay(dto.getPaymentDay())   
                .build();

        return requestRepository.save(request);
    }

    @Transactional
    public void processApproval(Long requestId) {
        AccountRequestEntity request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("요청을 찾을 수 없습니다."));

        if (request.getType() == AccountRequestEntity.RequestType.CREATE) {
            // 계좌 생성
            Account account = new Account();
            account.setCustomer(request.getCustomer());
            account.setProduct(request.getProducts());
            account.setAccountNum(request.getAccountNum());
            account.setAccountPassword(request.getAccountPassword()); // 기본값 지정 또는 별도 처리
            account.setAccountBalance(request.getAccountBalance());
            account.setPaymentDay(null); // 필요시 설정

            LocalDate createDate = LocalDate.now();
            account.setAccountCreateDate(createDate);

            if (request.getProducts().getProductsDuration() != null) {
                account.setAccountExpirationDate(createDate.plusMonths(request.getProducts().getProductsDuration()));
            }

            account.setAccountStatus(Account.AccountStatus.ACTIVE);
            accountRepository.save(account);
        }

        else if (request.getType() == AccountRequestEntity.RequestType.DELETE) {
            Account target = request.getAccount();
            if (target == null) {
                throw new IllegalStateException("삭제할 계좌 정보가 없습니다.");
            }
            accountRepository.delete(target);
        }

        request.setStatus(AccountRequestEntity.RequestStatus.APPROVED);
        request.setProcessedAt(LocalDateTime.now());
    }

    @Transactional
    public void processRejection(Long requestId) {
        AccountRequestEntity request = requestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("요청을 찾을 수 없습니다."));

        request.setStatus(AccountRequestEntity.RequestStatus.REJECTED);
    }
}
