import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class SavingsAccountTA8Test extends FormatTester {
    public static final String CLASSNAME = "SavingsAccount";

    public SavingsAccountTA8Test() {
        super(CLASSNAME, false);
    }

    private void testInterface() {
        String[] instanceVars = {"double annualInterestRate"};
        assertTrue("Instance variables should be private with correct name and signature.", instanceVariablesArePrivate(instanceVars));

        assertFalse("Should not override getBalance.", hasMethod("double getBalance()"));
        assertFalse("Should not override or call monthEndUpdate.", hasMethod("monthEndUpdate"));
        assertFalse("Should not override or call getCustomer", hasMethod("getCustomer"));
    }


    // test constructors
    @Test
    public void test_creation_defaultConstructor() {
        testInterface();
        SavingsAccount s = new SavingsAccount();
        assertEquals("Expected initial balance to be 0.0", 0.0, s.getBalance(), 0.00001);
        assertEquals("Expected annual interest rate to be 0.05 (5%)", 0.05, s.getAnnualInterestRate(), 0.00001);
    }


    @Test
    public void test_constructorWithInterest() {
        testInterface();
        SavingsAccount s = new SavingsAccount(0.01);

        assertEquals("Unexpected balance", 0.0, s.getBalance(), 0.00001);
        assertEquals("Unexpected interest rate", 0.01, s.getAnnualInterestRate(), 0.00001);
    }

    @Test
    public void test_constructorWithInterestAndBalance() {
        testInterface();
        SavingsAccount s = new SavingsAccount(50.0, 0.15);

        assertEquals("Unexpected balance", 50.0, s.getBalance(), 0.00001);
        assertEquals("Unexpected interest rate", 0.15, s.getAnnualInterestRate(), 0.00001);
    }

    @Test
    public void test_constructorWithCustomerAndBalance() {
        testInterface();
        Customer       c = new Customer("John Doe", 321);
        SavingsAccount s = new SavingsAccount(c, 50.0);

        assertEquals("Unexpected balance", 50.0, s.getBalance(), 0.00001);
        assertEquals("Unexpected interest rate (default)", 0.05, s.getAnnualInterestRate(), 0.00001);
        assertEquals("Unexpected customer", "John Doe", s.getAccountHolder().getName());
    }

    @Test
    public void test_constructorWithCustomerBalanceAndInterestRate() {
        testInterface();
        Customer       c = new Customer("John Doe", 321);
        SavingsAccount s = new SavingsAccount(c, 551.0, 0.7);

        assertEquals("Unexpected balance", 551.0, s.getBalance(), 0.00001);
        assertEquals("Unexpected interest rate", 0.7, s.getAnnualInterestRate(), 0.00001);
        assertEquals("Unexpected customer", "John Doe", s.getAccountHolder().getName());
    }

    // 	test setters and getters
    @Test
    public void test_setAnnualInterestRate_zero() {
        testInterface();
        SavingsAccount s = new SavingsAccount();
        s.setAnnualInterestRate(0.0);
        assertEquals("Changed annual interest rate is 0.0", 0.0, s.getAnnualInterestRate(), 0.000001);
    }

    @Test
    public void test_setAnnualInterestRate_one() {
        testInterface();
        SavingsAccount s = new SavingsAccount();
        s.setAnnualInterestRate(1.0);
        assertEquals("Changed annual interest rate is 1.0", 1.0, s.getAnnualInterestRate(), 0.000001);
    }

    @Test
    public void test_setAnnualInterestRate_negativeRate() {
        testInterface();
        SavingsAccount s = new SavingsAccount();
        s.setAnnualInterestRate(-0.25);
        assertEquals("Annual interest rate should not be negative and should have been left unchanged.", 0.05, s.getAnnualInterestRate(), 0.000001);
    }

    @Test
    public void test_setAnnualInterestRate_biggerThanOne() {
        testInterface();
        SavingsAccount s = new SavingsAccount();
        s.setAnnualInterestRate(1.1);
        assertEquals("Annual interest rate should be at most 1 (100%) and should have been left unchanged.", 0.05, s.getAnnualInterestRate(), 0.000001);
    }

    // testing remaining methods
    @Test
    public void test_getMonthlyFeesAndInterest_defaultRate() {
        testInterface();
        SavingsAccount s = new SavingsAccount();
        s.deposit(100);
        double interest = s.getMonthlyFeesAndInterest();
        assertEquals("Balance ($100) should be unchanged", 100.00, s.getBalance(), 0.00001);
        assertEquals("Testing computed interest", 0.416666667, interest, 0.00001);
    }

    @Test
    public void test_getMonthlyFeesAndInterest_zeroBalance() {
        testInterface();
        SavingsAccount s        = new SavingsAccount();
        double         interest = s.getMonthlyFeesAndInterest();
        assertEquals("Balance ($0) should be unchanged", 0.00, s.getBalance(), 0.00001);
        assertEquals("Testing computed interest", 0.0, interest, 0.00001);
    }

    @Test
    public void test_getMonthlyFeesAndInterest_zeroPercent() {
        testInterface();
        SavingsAccount s = new SavingsAccount();
        s.setAnnualInterestRate(0.0);
        s.deposit(100.0);
        double interest = s.getMonthlyFeesAndInterest();
        assertEquals("Balance ($100) should be unchanged", 100.00, s.getBalance(), 0.00001);
        assertEquals("Testing computed interest", 0.00, interest, 0.00001);
    }

    @Test
    public void test_getMontlyFeesAndInterest_hundredPercent() {
        testInterface();
        SavingsAccount s = new SavingsAccount();
        s.setAnnualInterestRate(1.0);
        s.deposit(100.0);
        double interest = s.getMonthlyFeesAndInterest();
        assertEquals("Balance ($100) should be unchanged", 100.00, s.getBalance(), 0.00001);
        assertEquals("Testing computed interest", 8.33333, interest, 0.00001);
    }

    @Test
    public void test_withdraw_fundsAvailable() {
        testInterface();
        SavingsAccount s = new SavingsAccount();
        s.deposit(500);
        s.setMinimumBalance(100.0);

        s.withdraw(400);

        assertEquals("Savings account with 500.0 balance and 100 min balance, should be able to withdraw 400.", 100.0, s.getBalance(), 0.000001);
    }

    @Test
    public void test_withdraw_WouldGoBelowMinBalance() {
        testInterface();
        SavingsAccount s = new SavingsAccount();
        s.deposit(500);
        s.setMinimumBalance(100.0);

        s.withdraw(401);

        assertEquals("Savings account with 500.0 balance and 100 min balance, can't withdraw 400.", 500.0, s.getBalance(), 0.000001);
    }

    @Test
    public void test_saveToTextFile_allInstanceVariablesInitialized() {
        testInterface();
        SavingsAccount s = new SavingsAccount(new Customer("Test", 123), 100.50, 0.09);
        s.setMinimumBalance(55.55);

        try {
            s.saveToTextFile("test1.txt");
        } catch (IOException ioe) {
            fail("Unexpected exception thrown when trying to save SavingsAccount to file with name 'test1.txt'");
        }

        String[] expectedLinesInFile = {"100.5", s.getAccountNumber(), "Test", "123", "0.09", "55.55"};
        assertFileContent("Saving to file SavingsAccount with balance 100.50, account holder 'Test', 123, interest rate 0.09 and minimum balance 55.55", expectedLinesInFile, "test1.txt");

    }

    @Test
    public void test_saveToTextFile_AccountHolderNull() {
        testInterface();
        SavingsAccount s = new SavingsAccount(100.50, 0.09);
        s.setMinimumBalance(55.55);
        s.setAccountHolder(null);

        try {
            s.saveToTextFile("test2.txt");
        } catch (IOException ioe) {
            fail("Unexpected exception thrown when trying to save SavingsAccount to file with name 'test1.txt'");
        }

        String[] expectedLinesInFile = {"100.5", s.getAccountNumber(), "null", "0.09", "55.55"};
        assertFileContent("Saving to file SavingsAccount with balance 100.50, account holder null, interest rate 0.09 and minimum balance 55.55", expectedLinesInFile, "test2.txt");

    }

    @Test
    public void test_constructorFromTextFile_allInstanceVariablesInFile() {
        testInterface();
        SavingsAccount s           = null;
        String[]       linesInFile = {"100.5", "1234", "Test", "123", "0.09", "55.55"};
        try {
            createFile("readTest1.txt", linesInFile);
            BufferedReader input = new BufferedReader(new FileReader("readTest1.txt"));

            s = new SavingsAccount(input);
            input.close();
        } catch (IOException ioe) {
            fail("Unexpected IOException when creating SavingsAccount from file");
        }
        assertEquals("Testing balance: Reading from file: with balance 100.50, account number 1234, account holder 'Test', 123, interest rate 0.09 and minimum balance 55.55", 100.50, s.getBalance(), 0.00001);
        assertEquals("Testing account number: Reading from file: with balance 100.50, account number 1234, account holder 'Test', 123, interest rate 0.09 and minimum balance 55.55", "1234", s.getAccountNumber());
        assertEquals("Testing account holder name: Reading from file: with balance 100.50, account number 1234, account holder 'Test', 123, interest rate 0.09 and minimum balance 55.55", "Test", s.getAccountHolder().getName());
        assertEquals("Testing interest rate: Reading from file: with balance 100.50, account number 1234, account holder 'Test', 123, interest rate 0.09 and minimum balance 55.55", 0.09, s.getAnnualInterestRate(), 0.00001);
        assertEquals("Testing minimum balance: Reading from file: with balance 100.50, account number 1234, account holder 'Test', 123, interest rate 0.09 and minimum balance 55.55", 55.55, s.getMinimumBalance(), 0.00001);

    }

    @Test
    public void test_constructorFromTextFile_accountHolderNull() {
        testInterface();
        SavingsAccount s           = null;
        String[]       linesInFile = {"100.5", "1234", "null", "0.09", "55.55"};
        try {
            createFile("readTest2.txt", linesInFile);
            BufferedReader input = new BufferedReader(new FileReader("readTest2.txt"));

            s = new SavingsAccount(input);
            input.close();
        } catch (IOException ioe) {
            fail("Unexpected IOException when creating SavingsAccount from file");
        }
        assertEquals("Testing balance: Reading from file: with balance 100.50, account number 1234, account holder 'Test', 123, interest rate 0.09 and minimum balance 55.55", 100.50, s.getBalance(), 0.00001);
        assertEquals("Testing account number: Reading from file: with balance 100.50, account number 1234, account holder 'Test', 123, interest rate 0.09 and minimum balance 55.55", "1234", s.getAccountNumber());
        assertNull("Testing account holder: Reading from file: with balance 100.50, account number 1234, account holder 'Test', 123, interest rate 0.09 and minimum balance 55.55", s.getAccountHolder());
        assertEquals("Testing interest rate: Reading from file: with balance 100.50, account number 1234, account holder 'Test', 123, interest rate 0.09 and minimum balance 55.55", 0.09, s.getAnnualInterestRate(), 0.00001);
        assertEquals("Testing minimum balance: Reading from file: with balance 100.50, account number 1234, account holder 'Test', 123, interest rate 0.09 and minimum balance 55.55", 55.55, s.getMinimumBalance(), 0.00001);

    }

    private void assertFileContent(String message, String[] expectedLinesInFile, String filename) {
        try {
            BufferedReader input = new BufferedReader(new FileReader(filename));
            int            index = 0;
            String         line  = input.readLine();
            while (line != null) {
                if (index >= expectedLinesInFile.length) {
                    fail(message + " Found more lines in file than expected.  Only expected " + expectedLinesInFile.length + " lines");
                }
                assertEquals(message + " testing line " + index + "in output file", expectedLinesInFile[index], line);
                line = input.readLine();
                index++;
            }
            assertEquals(message + " Expected more lines in output file.", expectedLinesInFile.length, index);
            input.close();
        } catch (IOException ioe) {
            fail("Unexpected exception when testing file content.");
        }
    }

    private void createFile(String filename, String[] lines) throws IOException {
        PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
        for (String line : lines) {
            output.println(line);
        }
        output.close();
    }
}
