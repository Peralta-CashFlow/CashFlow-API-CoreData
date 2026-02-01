package com.cashflow.coredata.domain.enums;

import lombok.Getter;

@Getter
public enum FinancialType {

    E("Expense"),
    I("Income");

    private final String description;

    FinancialType(String description) { this.description = description; }

}
