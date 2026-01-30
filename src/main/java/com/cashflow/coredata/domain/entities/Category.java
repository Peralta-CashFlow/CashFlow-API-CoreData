package com.cashflow.coredata.domain.entities;

import com.cashflow.coredata.domain.enums.FinancialType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "tb_category")
public class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String color;

    @Column
    private String icon;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false, name = "user_id")
    private long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FinancialType type;

}
