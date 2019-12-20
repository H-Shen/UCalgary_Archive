import java.math.BigDecimal;

/**
 * The {@code BankAccount} class provides static methods of simulating a bank account
 * in the real world.
 * <p>
 * All methods ensure that the balance in a bank account is never overdrafted
 * (a negative amount) and that only positive amounts are deposited/withdrawn.
 * <p>
 *
 * @author Group 25
 */
public class BankAccount {

    // set a default account number if no account numbers provided
    private static final String DEFAULT_ACCOUNT_NUMBER = "0001";
    private              double balance;
    private              String accountNumber;

    public BankAccount() {
        accountNumber = DEFAULT_ACCOUNT_NUMBER;
    }

    public BankAccount(double balance) {
        this.balance = balance;
        accountNumber = DEFAULT_ACCOUNT_NUMBER;
    }

    public BankAccount(double balance, String accountNumber) {
        this(balance);
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    @Override
    public String toString() {
        return accountNumber + ": " + balance;
    }

    /**
     * Deposit money to the current account. Negative deposit will be ignored.
     *
     * @param money the money to deposit
     */
    public void deposit(double money) {
        if (new BigDecimal(money).compareTo(new BigDecimal("0.0")) > 0) {
            balance += money;
        }
    }

    /**
     * Withdraw money from the current account. Negative withdrawal will be ignored.
     *
     * @param money the money to withdraw
     */
    public void withdraw(double money) {
        if (new BigDecimal(money).compareTo(new BigDecimal("0.0")) > 0) {
            if (new BigDecimal(money).compareTo(new BigDecimal(balance)) <= 0) {
                balance -= money;
            }
        }
    }
}
