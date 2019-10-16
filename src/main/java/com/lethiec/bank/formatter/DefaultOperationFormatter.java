package com.lethiec.bank.formatter;

import com.lethiec.bank.Operation;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class DefaultOperationFormatter implements OperationFormatter {
    private static final String OPERATION_FORMAT = "%s %-10s %s";
    private static final String BALANCE_FORMAT = "(%f) %f -> %f";
    private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss:SSS";

    @Override
    public String format(Operation operation) {
        final var formattedBalance = formatAccountBalance(
                operation.getAmount(),
                operation.getBalanceBefore(),
                operation.getBalanceAfter());

        return String.format(OPERATION_FORMAT, formatDate(operation.getDate()), operation.getType(), formattedBalance);
    }

    private String formatDate(Instant date) {
        return LocalDateTime.ofInstant(date, ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    private String formatAccountBalance(BigDecimal amount, BigDecimal balanceBefore, BigDecimal balanceAfter) {
        return String.format(BALANCE_FORMAT, amount, balanceBefore, balanceAfter);
    }
}
