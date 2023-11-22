package com.digcoin.snapx.domain.trade.enums;

public enum BaseAccountsCategory {

    AVAILABLE(BaseAccountsFinancialType.DEBIT),

    FROZEN(BaseAccountsFinancialType.DEBIT);

    private final BaseAccountsFinancialType financialType;

    BaseAccountsCategory(BaseAccountsFinancialType financialType) {
        this.financialType = financialType;
    }

    public BaseAccountsFinancialType getFinancialType() {
        return financialType;
    }
}
