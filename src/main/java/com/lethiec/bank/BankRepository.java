package com.lethiec.bank;

import com.lethiec.bank.exceptions.AccountNotFoundException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface BankRepository {
    Optional<List<Operation>> getOperations(String accountId) throws AccountNotFoundException;

    Optional<BigDecimal> getBalance(String accountId) throws AccountNotFoundException;

    void addOperation(String accountId, Operation operation) throws AccountNotFoundException;
}
