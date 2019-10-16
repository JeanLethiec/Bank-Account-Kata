package com.lethiec.bank;

import com.lethiec.bank.exceptions.AccountNotFoundException;
import com.lethiec.bank.exceptions.InvalidAmountException;
import com.lethiec.bank.printer.OperationsPrinter;

import java.math.BigDecimal;
import java.time.Clock;

public class DefaultBankService implements BankService {
    private final BankRepository repository;
    private final Clock clock;

    public DefaultBankService(BankRepository repository, Clock clock) {
        this.repository = repository;
        this.clock = clock;
    }

    @Override
    public void doDeposit(String accountId, BigDecimal amount) throws AccountNotFoundException, InvalidAmountException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }

        final var balance = repository.getBalance(accountId);

        if(balance.isEmpty())
            throw new AccountNotFoundException();

        final var operation = Operation.ofDeposit(clock.instant(), amount, balance.get());

        repository.addOperation(accountId, operation);
    }

    @Override
    public void doWithdraw(String accountId, BigDecimal amount) throws AccountNotFoundException, InvalidAmountException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException();
        }

        final var balance = repository.getBalance(accountId);

        if(balance.isEmpty())
            throw new AccountNotFoundException();

        if (balance.get().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException();
        }

        final var operation = Operation.ofWithdraw(clock.instant(), amount, balance.get());

        repository.addOperation(accountId, operation);
    }

    @Override
    public void print(String accountId, OperationsPrinter operationsPrinter) throws AccountNotFoundException {
        final var operations = repository
                .getOperations(accountId)
                .orElseThrow(AccountNotFoundException::new);
        operationsPrinter.print(operations);
    }
}
