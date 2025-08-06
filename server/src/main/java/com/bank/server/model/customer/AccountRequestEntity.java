package com.bank.server.model.customer;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerIndex", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productsIndex")
    private Products products;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountIndex") // 삭제 요청일 경우 대상 계좌
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestType type; // CREATE, DELETE

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    private LocalDateTime requestedAt;

    private LocalDateTime processedAt;

    // 🔽 추가 필드들 (CREATE 요청에 필요)
    private String accountNum;

    private String accountPassword;

    @Column(precision = 15, scale = 2)
    private BigDecimal accountBalance;

    private Integer paymentDay;

    public enum RequestType {
        CREATE, DELETE
    }

    public enum RequestStatus {
        PENDING, APPROVED, REJECTED
    }

    @PrePersist
    public void prePersist() {
        this.requestedAt = LocalDateTime.now();
    }
}