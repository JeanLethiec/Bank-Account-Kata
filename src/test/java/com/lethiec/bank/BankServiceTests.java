package com.lethiec.bank;

import com.lethiec.bank.exceptions.AccountNotFoundException;
import com.lethiec.bank.exceptions.InvalidAmountException;
import com.lethiec.bank.printer.OperationsPrinter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankServiceTests {

    @InjectMocks
    private DefaultBankService bankService;

    @Mock
    private BankRepository repository;

    @Mock
    private OperationsPrinter operationsPrinter;

    @Mock
    private Clock clock;

    private final Instant date = Instant.ofEpochSecond(1571040375);
    private final String accountId = "0";

    @ParameterizedTest
    @ValueSource(strings = {"1", "10", "100", "1000"})
    void depositShouldAddMoney(BigDecimal amount) throws AccountNotFoundException, InvalidAmountException {
        when(repository.getBalance(accountId)).thenReturn(Optional.of(BigDecimal.ZERO));
        when(clock.instant()).thenReturn(date);
        final var expectedDeposit = Operation.ofDeposit(date, amount, BigDecimal.ZERO);
        final var operationCaptor = ArgumentCaptor.forClass(Operation.class);
        final var accountIdCaptor = ArgumentCaptor.forClass(String.class);

        bankService.doDeposit(accountId, amount);

        verify(repository).addOperation(accountIdCaptor.capture(), operationCaptor.capture());
        assertEquals(expectedDeposit, operationCaptor.getValue());
        assertEquals(accountId, accountIdCaptor.getValue());

        verifyNoMoreInteractions(repository);
    }

    @Test
    void givenAnInvalidAccountInDepositShouldThrowException() {
        assertThrows(AccountNotFoundException.class, () -> bankService.doDeposit(accountId, BigDecimal.valueOf(1)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "-100"})
    void givenAnInvalidAmountInDepositShouldThrowException(BigDecimal amount) {
        assertThrows(InvalidAmountException.class, () -> bankService.doDeposit(accountId, amount));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "10", "100", "1000"})
    void withdrawShouldRemoveMoney(BigDecimal amount) throws AccountNotFoundException, InvalidAmountException {
        when(repository.getBalance(accountId)).thenReturn(Optional.of(amount));
        when(clock.instant()).thenReturn(date);
        final var expectedWithdraw = Operation.ofWithdraw(date, amount, amount);
        final var operationCaptor = ArgumentCaptor.forClass(Operation.class);
        final var accountIdCaptor = ArgumentCaptor.forClass(String.class);

        bankService.doWithdraw(accountId, amount);

        verify(repository).addOperation(accountIdCaptor.capture(), operationCaptor.capture());
        assertEquals(expectedWithdraw, operationCaptor.getValue());
        assertEquals(accountId, accountIdCaptor.getValue());

        verifyNoMoreInteractions(repository);
    }

    @Test
    void givenAnInvalidAccountInWithdrawShouldThrowException() {
        assertThrows(AccountNotFoundException.class, () -> bankService.doWithdraw(accountId, BigDecimal.valueOf(1)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "-100"})
    void givenAnInvalidAmountInWithdrawShouldThrowException(BigDecimal amount) {
        assertThrows(InvalidAmountException.class, () -> bankService.doWithdraw(accountId, amount));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "10", "100"})
    void givenAWithdrawAmountSuperiorToBalanceShouldThrowException(BigDecimal amount) throws AccountNotFoundException {
        when(repository.getBalance(accountId)).thenReturn(Optional.of(amount.subtract(BigDecimal.ONE)));

        assertThrows(InvalidAmountException.class, () -> bankService.doWithdraw(accountId, amount));
    }

    @Test
    void shouldPrintAccountHistory() throws AccountNotFoundException {
        final var operations = Arrays.asList(
                Operation.ofDeposit(date, BigDecimal.valueOf(50), BigDecimal.valueOf(0)),
                Operation.ofWithdraw(date, BigDecimal.valueOf(100), BigDecimal.valueOf(50))
        );
        when(repository.getOperations(accountId)).thenReturn(Optional.of(operations));

        bankService.print(accountId, operationsPrinter);

        verify(operationsPrinter).print(operations);
        verifyNoMoreInteractions(operationsPrinter);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void givenAnInvalidAccountInPrintShouldThrowException() {
        assertThrows(AccountNotFoundException.class, () -> {
            bankService.print(accountId, operationsPrinter);
        });
    }
}
