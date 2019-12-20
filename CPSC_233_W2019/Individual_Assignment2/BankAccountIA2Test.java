import org.junit.Test;

import static org.junit.Assert.*;

public class BankAccountIA2Test {
    // test constructors
    @Test
    public void test_creation() {
        BankAccount b = new BankAccount();
        assertEquals("Expected initial balance to be 0.0", 0.0, b.getBalance(), 0.00001);

        String actualAccountNumber = b.getAccountNumber();
        assertEquals("Account number should have exactly length 4 (4 digits)", 4, actualAccountNumber.length());
        int num = Integer.parseInt(actualAccountNumber);
        assertTrue("Account number should be a number between 1 and 9999 (inclusive)", 1 <= num && num <= 9999);
    }

    @Test
    public void test_constructorWithBalance() {
        BankAccount b = new BankAccount(50.0);
        assertEquals("Unexpected balance", 50.0, b.getBalance(), 0.00001);

        String actualAccountNumber = b.getAccountNumber();
        assertEquals("Account number should have exactly length 4 (4 digits)", 4, actualAccountNumber.length());
        int num = Integer.parseInt(actualAccountNumber);
        assertTrue("Account number should be a number between 1 and 9999 (inclusive)", 1 <= num && num <= 9999);
    }

    @Test
    public void test_constructorWithBalance_negative() {
        BankAccount b = new BankAccount(-50.0);
        assertEquals("Unexpected balance", -50.0, b.getBalance(), 0.00001);

        String actualAccountNumber = b.getAccountNumber();
        assertEquals("Account number should have exactly length 4 (4 digits)", 4, actualAccountNumber.length());
        int num = Integer.parseInt(actualAccountNumber);
        assertTrue("Account number should be a number between 1 and 9999 (inclusive)", 1 <= num && num <= 9999);
    }

    @Test
    public void test_constructorWithBalance_zero() {
        BankAccount b = new BankAccount(0.0);
        assertEquals("Unexpected balance", 0.0, b.getBalance(), 0.00001);

        String actualAccountNumber = b.getAccountNumber();
        assertEquals("Account number should have exactly length 4 (4 digits)", 4, actualAccountNumber.length());
        int num = Integer.parseInt(actualAccountNumber);
        assertTrue("Account number should be a number between 1 and 9999 (inclusive)", 1 <= num && num <= 9999);
    }

    @Test
    public void test_constructorWithBalanceAndAccountNumber() {
        BankAccount b = new BankAccount(100.0, "1234");
        assertEquals("Unexpected balance, created with argument 100.0 and '1234'", 100.0, b.getBalance(), 0.00001);
        assertEquals("Unexpected account number, created with argument 100.0 and '1234'", "1234", b.getAccountNumber());
    }

    @Test
    public void test_constructorWithBalanceAndAccountNumber_shortNumber() {
        BankAccount b = new BankAccount(100.0, "987");
        assertEquals("Unexpected balance, created with argument 100.0 and '987'", 100.0, b.getBalance(), 0.00001);
        assertEquals("Unexpected account number, created with argument 100.0 and '987'", "987", b.getAccountNumber());
    }

    @Test
    public void test_constructorWithBalanceAndAccountNumber_longNumber() {
        BankAccount b = new BankAccount(100.0, "78654");
        assertEquals("Unexpected balance, created with argument 100.0 and '78654'", 100.0, b.getBalance(), 0.00001);
        assertEquals("Unexpected account number, created with argument 100.0 and '78654'", "78654", b.getAccountNumber());
    }

    // Testing deposit

    @Test
    public void test_deposit() {
        BankAccount b = new BankAccount();
        b.deposit(10.25);
        assertEquals("Deposited 10.25.", 10.25, b.getBalance(), 0.000001);
    }

    @Test
    public void test_deposit_negativeAmount() {
        BankAccount b = new BankAccount();
        b.deposit(-10.25);
        assertEquals("Tried to deposit a negative amount, balance should remain unchanged.", 0.00, b.getBalance(), 0.000001);
    }

    // testing withdraw
    @Test
    public void test_withdraw_sufficientBalance() {
        BankAccount b = new BankAccount();
        b.deposit(500.00);
        b.withdraw(44.25);
        assertEquals("Withdrew 44.25 after depositing 500.00", 455.75, b.getBalance(), 0.000001);
    }

    @Test
    public void test_withdraw_insufficientFunds() {
        BankAccount b = new BankAccount();
        b.deposit(5.00);
        b.withdraw(5.01);
        assertEquals("Withdrew 5.01 after depositing 5.00", 5.00, b.getBalance(), 0.000001);
    }

    @Test
    public void test_withdraw_negativeAmount() {
        BankAccount b = new BankAccount();
        b.deposit(5.00);
        b.withdraw(-1.0);
        assertEquals("Withdrew -1 after depositing 5.00 (nothing should happen)", 5.00, b.getBalance(), 0.000001);
    }

    @Test
    public void test_withdraw_entireBalance() {
        BankAccount b = new BankAccount(5.00);
        b.withdraw(5.00);
        assertEquals("Withdrew 5.0 from account with start balance 5.0", 0.00, b.getBalance(), 0.000001);
    }

    @Test
    public void test_toString() {
        BankAccount b = new BankAccount(101.56, "3426");
        assertEquals("Expected to string to return <account number>: <balance>", "3426: 101.56", b.toString());
    }
}