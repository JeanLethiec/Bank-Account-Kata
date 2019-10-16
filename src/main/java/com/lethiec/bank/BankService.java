package com.lethiec.bank;

import com.lethiec.bank.exceptions.AccountNotFoundException;
import com.lethiec.bank.exceptions.InvalidAmountException;
import com.lethiec.bank.printer.OperationsPrinter;

import java.math.BigDecimal;

public interface BankService {
    void doWithdraw(String accountId, BigDecimal amount) throws AccountNotFoundException, InvalidAmountException;

    void doDeposit(String accountId, BigDecimal amount) throws AccountNotFoundException, InvalidAmountException;

    void print(String accountId, OperationsPrinter operationsPrinter) throws AccountNotFoundException;
}
