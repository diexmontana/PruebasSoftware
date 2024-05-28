package ATMCaseStudy;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ATMTest {
    private ATM atm;
    private BankDatabase bankDatabase;
    private CashDispenser cashDispenser;
    private Screen screen;
    private DepositSlot depositSlot;
    private Keypad keypad;
    private Account account;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    
    @Before
    public void setUp() {
        atm = new ATM();
        bankDatabase = new BankDatabase();
        cashDispenser = new CashDispenser();
        screen = new Screen();
        depositSlot = new DepositSlot();
        keypad = new Keypad();
        account = new Account(12345, 54321, 1000.0, 1200.0);
        //System.setOut(new PrintStream(outContent));
    }

    // RQF01: Prueba para mostrar mensaje de bienvenida y pedir número de cuenta
    @Test
    public void testWelcomeMessage() {
        screen.displayMessageLine("\nWelcome!");
        screen.displayMessage("\nPlease enter your bank account number: ");
        //Comprobar la salida de pantalla
        assertEquals("\nWelcome!\nPlease enter your bank account number: ", outContent.toString().trim());
    }

    // RQF02: Prueba para pedir al usuario que introduzca su PIN
    @Test
    public void testRequestPIN() {
        screen.displayMessage("\nPlease enter your PIN: ");
        assertEquals("\nPlease enter your PIN: ", outContent.toString().trim());
    }

  
    // RQF03: Validar el número de cuenta con el PIN correspondiente
    @Test
    public void testValidateAccountNumber() {
        // Autenticación de cuenta correcta
        assertTrue(bankDatabase.authenticateUser(12345, 54321));
        // Autenticación de cuenta incorrecta
        assertFalse(bankDatabase.authenticateUser(67890, 54321));
    }
    

    // RQF04: Validar el PIN
    @Test
    public void testValidatePIN() {
        // Autenticación correcta
        assertTrue(account.validatePIN(54321));
        // Autenticación incorrecta
        assertFalse(account.validatePIN(12345));
    }

    // RQF05: Prueba para mostrar mensaje si la autenticación falla
    @Test
    public void testFailedAuthenticationMessage() {
        boolean authenticated = bankDatabase.authenticateUser(12345, 12345);
        if (!authenticated) {
            screen.displayMessage("\nInvalid account number or PIN code. Please try again.");
        }
        assertEquals("\nInvalid account number or PIN code. Please try again.", outContent.toString().trim());
    }

    // RQF06: Prueba para mostrar el menú principal después de autenticación exitosa
    @Test
    public void testMainMenuAfterAuthentication() {
        boolean authenticated = bankDatabase.authenticateUser(12345, 54321);
        if (authenticated) {
            screen.displayMessageLine("\nMain Menu:");
            screen.displayMessageLine("1 - View my balance");
            screen.displayMessageLine("2 - Withdraw cash");
            screen.displayMessageLine("3 - Deposit funds");
            screen.displayMessageLine("4 - Exit\n");
        }
        assertEquals("\nMain Menu:\n1 - View my balance\n2 - Withdraw cash\n3 - Deposit funds\n4 - Exit\n", outContent.toString().trim());
    }

    // RQF07: Prueba para las opciones del menú principal
    @Test
    public void testMainMenu() {
        screen.displayMessageLine("\nMain Menu:");
        screen.displayMessageLine("1 - View my balance");
        screen.displayMessageLine("2 - Withdraw cash");
        screen.displayMessageLine("3 - Deposit funds");
        screen.displayMessageLine("4 - Exit\n");
        screen.displayMessage("Choose an option: ");
        assertEquals("\nMain Menu:\n1 - View my balance\n2 - Withdraw cash\n3 - Deposit funds\n4 - Exit\nChoose an option: ", outContent.toString().trim());
    }

    // RQF08: Prueba para manejar opción inválida en el menú principal
    @Test
    public void testInvalidMenuOption() {
        int input = 5; // Opción inválida
        if (input < 1 || input > 4) {
            screen.displayMessage("\nInvalid selection. Try again.");
        }
        assertEquals("\nInvalid selection. Try again.", outContent.toString().trim());
    }

    // RQF09: Prueba para la solicitud de saldo
    @Test
    public void testDisplayBalance() {
        screen.displayMessage(" - Available balance: ");
        screen.dispalyDollarAmount(account.getAvailableBalance());
        screen.displayMessage("\n - Total balance:");
        screen.dispalyDollarAmount(account.getTotalBalance());
        assertEquals(" - Available balance: \n" + account.getAvailableBalance() + "\n - Total balance:\n" + account.getTotalBalance(), outContent.toString().trim());
    }

    // RQF10: Prueba para el menú de retiro
    @Test
    public void testWithdrawalMenu() {
        screen.displayMessageLine("\nWithdrawal menu:");
        screen.displayMessageLine("1 - $20");
        screen.displayMessageLine("2 - $40");
        screen.displayMessageLine("3 - $60");
        screen.displayMessageLine("4 - $100");
        screen.displayMessageLine("5 - $200");
        screen.displayMessageLine("6 - Cancel transaction");
        screen.displayMessage("\nChoose a withdrawal amount: ");
        assertEquals("\nWithdrawal menu:\n1 - $20\n2 - $40\n3 - $60\n4 - $100\n5 - $200\n6 - Cancel transaction\nChoose a withdrawal amount: ", outContent.toString().trim());
    }

    // RQF11: Prueba para manejar opción inválida en el menú de retiro
    @Test
    public void testInvalidWithdrawalOption() {
        int input = 7; // Opción inválida
        if (input < 1 || input > 6) {
            screen.displayMessage("\nInvalid selection. Try again.");
        }
        assertEquals("\nInvalid selection. Try again.", outContent.toString().trim());
    }

    // RQF12: Prueba para verificar si el monto a retirar es mayor que el saldo disponible
    @Test
    public void testWithdrawalAmountExceedsBalance() {
        double initialBalance = account.getAvailableBalance();
        int withdrawalAmount = 2000; // Monto mayor al saldo disponible

        if (withdrawalAmount > initialBalance) {
            screen.displayMessageLine("\nInsufficient funds in your account." + "\n\nPlease choose a smaller amount.");
        }
        assertEquals("\nInsufficient funds in your account.\n\nPlease choose a smaller amount.", outContent.toString().trim());
    }

    // RQF13: Prueba para verificar si el dispensador de efectivo tiene suficientes billetes
    @Test
    public void testCashDispenser() {
        assertTrue(cashDispenser.isSufficientCashAvailable(100));
        assertFalse(cashDispenser.isSufficientCashAvailable(20000));
    }

    // RQF14: Prueba para restar el monto de retiro del saldo de la cuenta
    @Test
    public void testDebit() {
        double initialBalance = account.getAvailableBalance();
        int withdrawalAmount = 20; // Monto de retiro

        if (initialBalance >= withdrawalAmount && cashDispenser.isSufficientCashAvailable(withdrawalAmount)) {
            account.depit(withdrawalAmount);
            cashDispenser.dispenseCash(withdrawalAmount);
            assertEquals(initialBalance - withdrawalAmount, account.getAvailableBalance(), 0.001);
        } else {
            screen.displayMessageLine("Insufficient funds or insufficient cash in the dispenser.");
            assertEquals("Insufficient funds or insufficient cash in the dispenser.", outContent.toString().trim());
        }
    }

    // RQF15: Prueba para mostrar mensaje recordando al usuario que tome el dinero
    @Test
    public void testTakeCashMessage() {
        screen.displayMessageLine("\nYour cash has been dispensed. Please take your cash now.");
        assertEquals("\nYour cash has been dispensed. Please take your cash now.", outContent.toString().trim());
    }

    // RQF16: Prueba para solicitar monto de depósito
    @Test
    public void testRequestDepositAmount() {
        screen.displayMessage("\nPlease enter a deposit amount in CENTS (or 0 to cancel): ");
        assertEquals("\nPlease enter a deposit amount in CENTS (or 0 to cancel): ", outContent.toString().trim());
    }

    // RQF17: Prueba para manejar cancelación de depósito
    @Test
    public void testCancelDeposit() {
        int input = 0; // Usuario cancela el depósito
        if (input == 0) {
            screen.displayMessageLine("\nCanceling transaction...");
        }
        assertEquals("\nCanceling transaction...", outContent.toString().trim());
    }

    // RQF18: Prueba para solicitar al usuario que introduzca el sobre de depósito
    @Test
    public void testInsertDepositEnvelope() {
        screen.displayMessage("\nPlease insert a deposit envelope containing the specified amount.");
        assertEquals("\nPlease insert a deposit envelope containing the specified amount.", outContent.toString().trim());
    }

    // RQF19: Prueba para volver a mostrar el menú principal después de una transacción exitosa
    @Test
    public void testReturnToMainMenuAfterTransaction() {
        screen.displayMessageLine("\nMain Menu:");
        screen.displayMessageLine("1 - View my balance");
        screen.displayMessageLine("2 - Withdraw cash");
        screen.displayMessageLine("3 - Deposit funds");
        screen.displayMessageLine("4 - Exit\n");
        screen.displayMessage("Choose an option: ");
        assertEquals("\nMain Menu:\n1 - View my balance\n2 - Withdraw cash\n3 - Deposit funds\n4 - Exit\nChoose an option: ", outContent.toString().trim());
    }

    // RQF20: Prueba para mostrar mensaje de agradecimiento al finalizar la sesión
    @Test
    public void testEndSession() {
        screen.displayMessage("Thank you! Goodbye!");
        screen.displayMessage("\nWelcome! Please enter your bank account number: ");
        assertEquals("Thank you! Goodbye!\nWelcome! Please enter your bank account number: ", outContent.toString().trim());
    }
    
}

