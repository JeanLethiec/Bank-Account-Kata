package com.lethiec.bank.printer;

import com.lethiec.bank.Operation;
import com.lethiec.bank.formatter.OperationFormatter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsoleOperationsPrinterTest {

    private PrintStream originalPrintStream;

    private ByteArrayOutputStream arrayOutputStream;

    @Mock
    private OperationFormatter formatter;

    @InjectMocks
    private ConsoleOperationsPrinter printer;

    @BeforeEach
    void setup() {
        originalPrintStream = System.out;
        arrayOutputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(arrayOutputStream));
    }

    @Test
    void printingOperationsShouldOutputOnStdout() {
        Instant date = Instant.ofEpochSecond(1571040375);
        List<Operation> operations = new LinkedList<>();
        final var deposit = Operation.ofDeposit(date, BigDecimal.valueOf(50), BigDecimal.valueOf(0));
        operations.add(deposit);
        final var withdraw = Operation.ofWithdraw(date, BigDecimal.valueOf(100), BigDecimal.valueOf(50));
        operations.add(withdraw);

        final var formattedDeposit = "14/10/2019 08:06:15:000 DEPOSIT    (50,000000) 0,000000 -> 50,000000";
        final var formattedWithdraw = "14/10/2019 08:06:15:000 WITHDRAW   (100,000000) 50,000000 -> -50,000000";

        when(formatter.format(deposit)).thenReturn(formattedDeposit);
        when(formatter.format(withdraw)).thenReturn(formattedWithdraw);

        printer.print(operations);

        verify(formatter, times(2)).format(any(Operation.class));
        verifyNoMoreInteractions(formatter);

        assertEquals(
                formattedDeposit
                        + System.lineSeparator()
                        + formattedWithdraw
                        + System.lineSeparator(),
                arrayOutputStream.toString()
        );
    }

    @AfterEach
    void cleanup() {
        System.setOut(originalPrintStream);
    }
}
