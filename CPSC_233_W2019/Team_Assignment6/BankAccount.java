import java.math.BigDecimal;

/**
 * The {@code BankAccount} class provides static methods of simulating a bank
 * account the real world. Since it is generic, it is abstract and cannot be instantiated.
 * <p>
 *
 * @author Group25
 * @version 2019-03-09
 */

public abstract class BankAccount {

    private double   balance;
    private String   accountNumber = "0001";
    private Customer accountHolder;

    /**
     * Default constructor
     */
    public BankAccount() {
    }

    /**
     * Constructor with balance
     *
     * @param balance
     */
    BankAccount(double balance) {
        this.balance = balance;
    }

    /**
     * Constructor with balance and accountNumber
     *
     * @param balance
     * @param accountNumber
     */
    public BankAccount(double balance, String accountNumber) {
        this(balance);
        this.accountNumber = accountNumber;
    }

    /**
     * Constructor with accountHolder and balance
     *
     * @param accountHolder
     * @param balance
     */
    BankAccount(Customer accountHolder, double balance) {
        this(balance);
        setAccountHolder(accountHolder);
    }

    /**
     * Copy constructor
     *
     * @param other
     */
    BankAccount(BankAccount other) {
        this(other.getBalance(), other.getAccountNumber());
        setAccountHolder(other.getAccountHolder());
    }

    /**
     * Getter of balance
     *
     * @return balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Getter of account number
     *
     * @return account number
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Getter of account holder
     *
     * @return a swallow copy of the account holder
     */
    public Customer getAccountHolder() {
        return accountHolder;
    }

    /**
     * Setter of account holder
     *
     * @param accountHolder
     */
    public void setAccountHolder(Customer accountHolder) {
        this.accountHolder = accountHolder;
    }


    @Override
    public String toString() {
        return "(" + getAccountHolder() + ") " + getAccountNumber() + ": " + getBalance();
    }

    /**
     * Define the way to deposit, balance will be only changed if money > 0
     *
     * @param money
     */
    public void deposit(double money) {
        if (new BigDecimal(money).compareTo(BigDecimal.ZERO) > 0) {
            balance += money;
        }
    }

    /**
     * Define the way to withdraw
     *
     * @param money
     */
    public void withdraw(double money) {
        if (new BigDecimal(money).compareTo(BigDecimal.ZERO) > 0 && sufficientFunds(money)) {
            balance -= money;
        }
    }

    /**
     * Define the way to transfer to another account
     *
     * @param amount
     * @param other
     */
    public void transfer(double amount, BankAccount other) {
        if (sufficientFunds(amount)) {
            withdraw(amount);
            other.deposit(amount);
        }
    }

    /**
     * Update the balance with monthly interest.
     */
    public void monthEndUpdate() {
        BigDecimal money = new BigDecimal(getMonthlyFeesAndInterest());
        if (money.compareTo(BigDecimal.ZERO) > 0) {
            deposit(money.doubleValue());
        } else {
            balance += money.doubleValue();
        }
    }

    /**
     * Check if the money can be withdraw from the account
     *
     * @param money
     * @return true if balance >= money, otherwise false
     */
    public boolean sufficientFunds(double money) {
        return (new BigDecimal(balance).compareTo(new BigDecimal(money)) >= 0);
    }

    /**
     * Define an abstract method of MonthlyFeesAndInterest getter
     *
     * @return a monthly fee and interest defined by subclasses
     */
    protected abstract double getMonthlyFeesAndInterest();
}