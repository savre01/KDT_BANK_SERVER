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

    @Column(precision = 15, scale = 2)
    private BigDecimal accountBalance;

    private LocalDate accountCreateDate;

    private LocalDate accountExpirationDate;

    private Integer paymentDay; // 매달 납부일

    private String accountStatus = "ACTIVE";
}
