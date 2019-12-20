import org.junit.Test;

import static org.junit.Assert.*;

public class SavingsAccountTA6Test extends FormatTester {
    public static final String CLASSNAME = "SavingsAccount";

    public SavingsAccountTA6Test() {
        super(CLASSNAME, false);
    }

    private void testInterface() {
        String[] instanceVars = {"double annualInterestRate"};
        assertTrue("Instance variables should be private with correct name and signature.", instanceVariablesArePrivate(instanceVars));

        assertFalse("Should not override getBalance.", hasMethod("double getBalance"));
        assertFalse("Should not override or call setBalance.", hasMethod("setBalance"));
        assertFalse("Should not override or call getCustomer.", hasMethod("getCustomer"));
        assertFalse("Should not override or call setCustomer.", hasMethod("setCustomer"));
        assertFalse("Should not override or call monthEndUpdate.", hasMethod("monthEndUpdate"));
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
}
