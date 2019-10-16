package com.lethiec.bank.printer;

import com.lethiec.bank.formatter.OperationFormatter;
import com.lethiec.bank.Operation;

import java.util.List;

public class ConsoleOperationsPrinter implements OperationsPrinter {
    private final OperationFormatter operationFormatter;

    public ConsoleOperationsPrinter(OperationFormatter operationFormatter) {
        this.operationFormatter = operationFormatter;
    }

    @Override
    public void print(List<Operation> operations) {
        operations.forEach(operation -> System.out.println(operationFormatter.format(operation)));
    }
}
