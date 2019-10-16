package com.lethiec.bank.formatter;

import com.lethiec.bank.Operation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

class OperationFormatterTests {

    private Instant date = Instant.ofEpochSecond(1571040375);
    private OperationFormatter formatter = new DefaultOperationFormatter();

    @Test
    void depositShouldBeCorrectlyFormatted() {
        final var deposit = Operation.ofDeposit(date, BigDecimal.valueOf(50), BigDecimal.valueOf(0));
        final var formatted = formatter.format(deposit);

        Assertions.assertEquals( "14/10/2019 08:06:15:000 DEPOSIT    (50,000000) 0,000000 -> 50,000000", formatted);
    }

    @Test
    void withdrawalShouldBeCorrectlyFormatted() {
        final var withdrawal = Operation.ofWithdraw(date, BigDecimal.valueOf(25), BigDecimal.valueOf(50));
        final var formatted = formatter.format(withdrawal);

        Assertions.assertEquals( "14/10/2019 08:06:15:000 WITHDRAW   (25,000000) 50,000000 -> 25,000000", formatted);
    }
}
