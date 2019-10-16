package com.lethiec.bank.printer;

import com.lethiec.bank.Operation;

import java.util.List;

public interface OperationsPrinter {
    void print(List<Operation> operations);
}
