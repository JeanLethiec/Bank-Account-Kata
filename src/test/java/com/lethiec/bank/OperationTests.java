package com.lethiec.bank;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OperationTests {
    private final Instant date = Instant.ofEpochSecond(1571040375);

    @ParameterizedTest
    @CsvSource({"1,2", "10,20", "455,795"})
    void newDepositShouldBeCorrectlyInitialized(BigDecimal amount, BigDecimal balanceBefore) {
        Operation deposit = Operation.ofDeposit(date, amount, balanceBefore);

        assertEquals(amount, deposit.getAmount());
        assertEquals(balanceBefore, deposit.getBalanceBefore());
        assertEquals(balanceBefore.add(amount), deposit.getBalanceAfter());
        assertEquals(date, deposit.getDate());
        assertEquals(Operation.DEPOSIT, deposit.getType());
    }

    @ParameterizedTest
    @CsvSource({"1,2", "10,20", "455,795"})
    void newWithdrawShouldBeCorrectlyInitialized(BigDecimal amount, BigDecimal balanceBefore) {
        Operation deposit = Operation.ofWithdraw(date, amount, balanceBefore);

        assertEquals(amount, deposit.getAmount());
        assertEquals(balanceBefore, deposit.getBalanceBefore());
        assertEquals(balanceBefore.subtract(amount), deposit.getBalanceAfter());
        assertEquals(date, deposit.getDate());
        assertEquals(Operation.WITHDRAW, deposit.getType());
    }
}
