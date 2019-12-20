import org.junit.Test;

import static org.junit.Assert.*;

public class ChequingAccountTA6Test extends FormatTester {
    public static final String CLASSNAME = "ChequingAccount";

    public ChequingAccountTA6Test() {
        super(CLASSNAME, false);
    }

    private void testInterface() {
        String[] instanceVars = {"double overdraftFee", "double overdraftAmount"};
        assertTrue("Instance variables should be private with correct name and signature.", instanceVariablesArePrivate(instanceVars));

        assertTrue("Class should not have the default constructor.", noDefaultConstructor());

        assertFalse("Should not override getBalance.", hasMethod("double getBalance"));
        assertFalse("Should not override or call setBalance.", hasMethod("setBalance"));
        assertFalse("Should not override or call getCustomer.", hasMethod("getCustomer"));
        assertFalse("Should not override or call setCustomer.", hasMethod("setCustomer"));
        assertFalse("Should not override or call monthEndUpdate.", hasMethod("monthEndUpdate"));
        assertFalse("Should not override or call transfer", hasMethod("transfer"));
        assertFalse("Should not override or call deposit", hasMethod("deposit"));
    }

    @Test
    public void test_constructorWithCustomerBalanceAndOverdraftFee_valid() {
        testInterface();
        Customer        c = new Customer("John Doe", 321);
        ChequingAccount s = new ChequingAccount(c, 551.0, 0.7);

        assertEquals("Unexpected balance", 551.0, s.getBalance(), 0.00001);
        assertEquals("Unexpected overdraft fee", 0.7, s.getOverdraftFee(), 0.00001);
        assertEquals("Unexpected customer", "John Doe", s.getAccountHolder().getName());
    }

    @Test
    public void test_constructorWithCustomerBalanceAndOverdraftFee_invalid() {
        testInterface();
        Customer        c = new Customer("John Doe", 321);
        ChequingAccount s = new ChequingAccount(c, 551.0, -0.01);

        assertEquals("Unexpected balance", 551.0, s.getBalance(), 0.00001);
        assertEquals("Unexpected overdraft fee", 1.0, s.getOverdraftFee(), 0.00001);
        assertEquals("Unexpected customer", "John Doe", s.getAccountHolder().getName());
    }

    @Test
    public void test_withdraw_sufficientBalance() {
        Customer        c = new Customer("John Doe", 321);
        ChequingAccount b = new ChequingAccount(c, 100, 10);
        b.withdraw(10);
        assertEquals("Account has $100, withdraw $10", 90, b.getBalance(), 0.000001);
    }

    @Test
    public void test_withdraw_overdraft() {
        Customer        c = new Customer("John Doe", 321);
        ChequingAccount b = new ChequingAccount(c, 1000, 13);
        b.setOverdraftAmount(50);
        b.withdraw(1030);
        assertEquals("Account has $1000, overdraft amount is $50, withdraw $1030.  (Withdraw in parent should call (overridden) sufficientFunds method)",
                -43.0, b.getBalance(), 0.000001);
    }

    @Test
    public void test_withdraw_overdraftToAllowedOverdraftAmount() {
        Customer        c = new Customer("John Doe", 321);
        ChequingAccount b = new ChequingAccount(c, 1000, 10);
        b.setOverdraftAmount(50);
        b.withdraw(1040);
        assertEquals("Account has $1000, overdraft amount is $40, fee is $10 withdraw $1040. (Withdraw in parent should call (overridden) sufficientFunds method)",
                -50.0, b.getBalance(), 0.000001);
    }

    @Test
    public void test_withdraw_overdraftMoreThanAllowedOverdraftAmount() {
        Customer        c = new Customer("John Doe", 321);
        ChequingAccount b = new ChequingAccount(c, 1000, 13);
        b.setOverdraftAmount(50);
        b.withdraw(1051);
        assertEquals("Account has $1000, overdraft amount is $50, withdraw $1051", 1000, b.getBalance(), 0.000001);
    }

    @Test
    public void test_getMonthlyFeesAndInterest_OverdraftBalance() {
        Customer        c = new Customer("John Doe", 321);
        ChequingAccount b = new ChequingAccount(c, 1000, 10);
        b.setOverdraftAmount(50);
        b.withdraw(1040);
        assertEquals("Expected fee due to overdraft of -50", -10.0, b.getMonthlyFeesAndInterest(), 0.00001);
        assertEquals("Account balance should not have changed due to calling getMonthlyFeesAndInterest",
                -50.0, b.getBalance(), 0.000001);
    }

    @Test
    public void test_getMonthlyFeesAndInterest_noOverdraft() {
        Customer        c = new Customer("John Doe", 321);
        ChequingAccount b = new ChequingAccount(c, 1000, 13);
        b.setOverdraftAmount(50);
        b.withdraw(800);
        assertEquals(0.0, b.getMonthlyFeesAndInterest(), 0.00001);
        assertEquals("Account balance should not have changed due to calling getMonthlyFeesAndInterest", 200.0, b.getBalance(), 0.000001);
        b.monthEndUpdate();
        assertEquals("Balance should not changed after calling monthEndUpdate since fee was 0.0", 200.0, b.getBalance(), 0.00001);
    }

    @Test
    public void test_getMonthlyFeesAndInterest_zeroBalance() {
        Customer        c = new Customer("John Doe", 321);
        ChequingAccount b = new ChequingAccount(c, 1000, 13);
        b.setOverdraftAmount(50);
        b.withdraw(1000);
        assertEquals(0.0, b.getMonthlyFeesAndInterest(), 0.00001);
        assertEquals("Account balance should not have changed due to calling getMonthlyFeesAndInterest", 0.0, b.getBalance(), 0.000001);
    }

    // Test that monthEndUpdate calls overridden method correctly
    @Test
    public void test_monthEndUpdate_InParent_overdraftBalance() {
        Customer        c = new Customer("John Doe", 321);
        ChequingAccount b = new ChequingAccount(c, 1000, 10);
        b.setOverdraftAmount(50);
        b.withdraw(1040);
        b.monthEndUpdate();
        assertEquals("Balance should have changed after calling monthEndUpdate, expected balance before update: -50",
                -60.0, b.getBalance(), 0.00001);
    }

    @Test
    public void test_monthEndUpdate_InParent_noOverdraft() {
        Customer        c = new Customer("John Doe", 321);
        ChequingAccount b = new ChequingAccount(c, 1000, 13);
        b.setOverdraftAmount(50);
        b.withdraw(800);
        b.monthEndUpdate();
        assertEquals("Balance should not changed after calling monthEndUpdate since fee was 0.0 on balance $200", 200.0, b.getBalance(), 0.00001);
    }

    //testing that transfer is invoking withdraw and deposit (and/or (overridden) sufficientFunds method)

    @Test
    public void test_transfer_overdraft() {
        Customer        c = new Customer("John Doe", 321);
        ChequingAccount b = new ChequingAccount(c, 1000, 13);
        SavingsAccount  s = new SavingsAccount();

        b.setOverdraftAmount(50);
        b.transfer(1030, s);
        assertEquals("Transferred 1030 from account with has $1000, overdraft amount is $50. testing balance in from account (chequing). (Transfer in BankAccount should invoke withdraw and deposit)",
                -43.0, b.getBalance(), 0.000001);
        assertEquals("Transferred 1030 to account with $0 balance", 1030, s.getBalance(), 0.000001);
    }
}
