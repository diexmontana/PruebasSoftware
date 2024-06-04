package ATMCaseStudy;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

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
    }

    // CP01
    @Test
    public void testWelcomeMessage() {
      
    	screen.displayMessageLine("\nWelcome!");
        screen.displayMessage("\nPlease enter your bank account number: ");
        
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add("\nWelcome!");
        expectedMessages.add("\nPlease enter your bank account number: ");

        assertEquals(expectedMessages, screen.getMessages());
    }
    
    private String getOutputAndReset() {
        String output = outContent.toString().trim();
        outContent.reset();
        return output;
    }

    // CP02: Prueba para pedir al usuario que introduzca su PIN
    @Test
    public void testRequestPIN() {
    	screen.displayMessage("\nPlease enter your PIN: ");
        
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add("\nPlease enter your PIN: ");

        assertEquals(expectedMessages, screen.getMessages());
    }

    // CP03: Validar el número de cuenta con el PIN correspondiente
    @Test
    public void testValidateAccountNumber() {
        // Autenticación de cuenta correcta
        assertTrue(bankDatabase.authenticateUser(12345, 54321));
        // Autenticación de cuenta incorrecta
        assertFalse(bankDatabase.authenticateUser(67890, 54321));
    }
    

    // CP04: Validar el PIN
    @Test
    public void testValidatePIN() {
        // Autenticación correcta
        assertTrue(account.validatePIN(54321));
        // Autenticación incorrecta
        assertFalse(account.validatePIN(12345));
    }

    // CP05: Prueba para mostrar mensaje si la autenticación falla
    @Test
    public void testFailedAuthenticationMessage() {
    	// Intenta autenticar un usuario con credenciales incorrectas
        boolean authenticated = bankDatabase.authenticateUser(12345, 12345);
        // Si la autenticación falla, muestra el mensaje de error
        if (!authenticated) {
            screen.displayMessage("\nInvalid account number or PIN code. Please try again.");
        }
        // Verifica si el mensaje mostrado coincide con el mensaje esperado
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add("\nInvalid account number or PIN code. Please try again.");
        assertEquals(expectedMessages, screen.getMessages());
    }

    // CP06: Prueba para mostrar el menú principal después de autenticación exitosa
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

        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add("\nMain Menu:");
        expectedMessages.add("1 - View my balance");
        expectedMessages.add("2 - Withdraw cash");
        expectedMessages.add("3 - Deposit funds");
        expectedMessages.add("4 - Exit\n");

        assertEquals(expectedMessages, screen.getMessages());
    }

    // CP07: Prueba para las opciones del menú principal
    @Test
    public void testMainMenu() {
    	screen.displayMessageLine("\nMain Menu:");
        screen.displayMessageLine("1 - View my balance");
        screen.displayMessageLine("2 - Withdraw cash");
        screen.displayMessageLine("3 - Deposit funds");
        screen.displayMessageLine("4 - Exit\n");
        screen.displayMessage("Choose an option: ");
        
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add("\nMain Menu:");
        expectedMessages.add("1 - View my balance");
        expectedMessages.add("2 - Withdraw cash");
        expectedMessages.add("3 - Deposit funds");
        expectedMessages.add("4 - Exit\n");
        expectedMessages.add("Choose an option: ");

        assertEquals(expectedMessages, screen.getMessages());
    }

    // CP08: Prueba para manejar opción inválida en el menú principal
    @Test
    public void testInvalidMenuOption() {
    	 int input = 5; // Opción inválida
    	    if (input < 1 || input > 4) {
    	        screen.displayMessage("\nInvalid selection. Try again.");
    	    }
    	    
    	    List<String> expectedMessages = new ArrayList<>();
    	    if (input < 1 || input > 4) {
    	        expectedMessages.add("\nInvalid selection. Try again.");
    	    }

    	    assertEquals(expectedMessages, screen.getMessages());
    }
    
    
    // CP09: Prueba para la solicitud de saldo
    @Test
    public void testDisplayBalance() {
    	 double expectedAvailableBalance = account.getAvailableBalance();
    	 double expectedTotalBalance = account.getTotalBalance();

    	 // Ejecutar el método que se está probando
    	 screen.displayMessage(" - Available balance: ");
    	 screen.dispalyDollarAmount(expectedAvailableBalance);
    	 screen.displayMessage("\n - Total balance:");
    	 screen.dispalyDollarAmount(expectedTotalBalance);

    	 // Verificar que los saldos coincidan
    	 assertEquals(expectedAvailableBalance, account.getAvailableBalance(), 0.001);
    	 assertEquals(expectedTotalBalance, account.getTotalBalance(), 0.001);
    }

    // CP10: Prueba para el menú de retiro
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
        
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add("\nWithdrawal menu:");
        expectedMessages.add("1 - $20");
        expectedMessages.add("2 - $40");
        expectedMessages.add("3 - $60");
        expectedMessages.add("4 - $100");
        expectedMessages.add("5 - $200");
        expectedMessages.add("6 - Cancel transaction");
        expectedMessages.add("\nChoose a withdrawal amount: ");

        assertEquals(expectedMessages, screen.getMessages());
    }

    // CP11: Prueba para manejar opción inválida en el menú de retiro
    @Test
    public void testInvalidWithdrawalOption() {
    	int input = 7; // Opción inválida
        if (input < 1 || input > 6) {
            screen.displayMessage("\nInvalid selection. Try again.");
        }

        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add("\nInvalid selection. Try again.");

        assertEquals(expectedMessages, screen.getMessages());
    }

    // CP12: Prueba para verificar si el monto a retirar es mayor que el saldo disponible
    @Test
    public void testWithdrawalAmountExceedsBalance() {
    	double initialBalance = account.getAvailableBalance();
        int withdrawalAmount = 2000; // Monto mayor al saldo disponible

        if (withdrawalAmount > initialBalance) {
            screen.displayMessageLine("\nInsufficient funds in your account." + "\n\nPlease choose a smaller amount.");
        }
        
        List<String> expectedMessages = new ArrayList<>();
        if (withdrawalAmount > initialBalance) {
            expectedMessages.add("\nInsufficient funds in your account." + "\n\nPlease choose a smaller amount.");
        }

        assertEquals(expectedMessages, screen.getMessages());
    }

    // CP13: Prueba para verificar si el dispensador de efectivo tiene suficientes billetes
    @Test
    public void testCashDispenser() {
        assertTrue(cashDispenser.isSufficientCashAvailable(100));
        assertFalse(cashDispenser.isSufficientCashAvailable(20000));
    }

    // CP14: Prueba para restar el monto de retiro del saldo de la cuenta
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

    // CP15: Prueba para mostrar mensaje recordando al usuario que tome el dinero
    @Test
    public void testTakeCashMessage() {
    	screen.displayMessageLine("\nYour cash has been dispensed. Please take your cash now.");
        
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add("\nYour cash has been dispensed. Please take your cash now.");

        assertEquals(expectedMessages, screen.getMessages());
    }

    // CP16: Prueba para solicitar monto de depósito
    @Test
    public void testRequestDepositAmount() {
    	screen.displayMessage("\nPlease enter a deposit amount in CENTS (or 0 to cancel): ");

        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add("\nPlease enter a deposit amount in CENTS (or 0 to cancel): ");

        assertEquals(expectedMessages, screen.getMessages());
    }

    // CP17: Prueba para manejar cancelación de depósito
    @Test
    public void testCancelDeposit() {
    	int input = 0; // Usuario cancela el depósito
        if (input == 0) {
            screen.displayMessageLine("\nCanceling transaction...");
        }
        
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add("\nCanceling transaction...");

        assertEquals(expectedMessages, screen.getMessages());
    }

    // CP18: Prueba para solicitar al usuario que introduzca el sobre de depósito
    @Test
    public void testInsertDepositEnvelope() {
    	screen.displayMessage("\nPlease insert a deposit envelope containing the specified amount.");
        
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add("\nPlease insert a deposit envelope containing the specified amount.");

        assertEquals(expectedMessages, screen.getMessages());
    }
    
}

