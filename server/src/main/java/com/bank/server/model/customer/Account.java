package com.bank.server.model.customer;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productsIndex")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    private AccountStatus accountStatus = AccountStatus.ACTIVE;

    public enum AccountStatus {
        ACTIVE,     // 실제 계좌만 존재
        SUSPENDED   // (선택) 정지 상태 등 확장 가능
    }
}
