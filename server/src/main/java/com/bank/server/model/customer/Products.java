package com.bank.server.model.customer;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Products")
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productsIndex;

    private Integer productsDuration;  // 단위: 개월

    private Double interestRate;

    private String productType;

    @Column(nullable = false)
    private String productName;
}
