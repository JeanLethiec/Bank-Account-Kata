package com.lethiec.bank;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class Operation {
    public static final String DEPOSIT = "DEPOSIT";
    public static final String WITHDRAW = "WITHDRAW";
    private final String type;
    private final Instant date;
    private final BigDecimal amount;
    private final BigDecimal balanceBefore;
    private final BigDecimal balanceAfter;

    private Operation(String type, Instant date, BigDecimal amount, BigDecimal balanceBefore, BigDecimal balanceAfter) {
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
    }

    public static Operation ofDeposit(Instant date, BigDecimal amount, BigDecimal balanceBefore) {
        return new Operation(DEPOSIT, date, amount, balanceBefore, balanceBefore.add(amount));
    }

    public static Operation ofWithdraw(Instant date, BigDecimal amount, BigDecimal balanceBefore) {
        return new Operation(WITHDRAW, date, amount, balanceBefore, balanceBefore.subtract(amount));
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public BigDecimal getBalanceBefore() {
        return balanceBefore;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return type.equals(operation.type) &&
                date.equals(operation.date) &&
                amount.equals(operation.amount) &&
                balanceBefore.equals(operation.balanceBefore) &&
                balanceAfter.equals(operation.balanceAfter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, date, amount, balanceBefore, balanceAfter);
    }
}
