package com.bank.server.model.customer;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerIndex", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productsIndex")
    private Products product;

    @Column(nullable = false, unique = true)
    private String accountNum;

    private String accountPassword;

    @Column(precision = 15, scale = 2)
    private BigDecimal accountBalance;

    private LocalDate accountCreateDate;

    private LocalDate accountExpirationDate;

    private Integer paymentDay; // 매달 납부일

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false)
    private AccountStatus accountStatus = AccountStatus.PENDING;

    // 내부 enum으로 선언
    public enum AccountStatus {
        PENDING,
        ACTIVE,
        DELETE_PENDING
    }
}
