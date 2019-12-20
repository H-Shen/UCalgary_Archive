import java.math.BigDecimal;

/**
 * The {@code BankAccount} class provides static methods of simulating a bank
 * accountin the real world.
 * <p>
 * All methods ensure that the balance in a bank account is never overdrafted
 * (a negative amount) and that only positive amounts are deposited/withdrawn.
 * <p>
 *
 * @author Haohu Shen
 * @version 2019-03-02
 */
public abstract class BankAccount {

    // set a default account number if no account numbers provided
    private static final String   DEFAULT_ACCOUNT_NUMBER = "0001";
    private              double   balance;
    private              String   accountNumber;
    private              Customer accountHolder          = null;

    /**
     * Default constructor
     */
    public BankAccount() {
        accountNumber = DEFAULT_ACCOUNT_NUMBER;
    }

    /**
     * Constructor with balance
     *
     * @param balance
     */
    public BankAccount(double balance) {
        this.balance = balance;
        accountNumber = DEFAULT_ACCOUNT_NUMBER;
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
     * Constructor with holder and balance
     *
     * @param holder
     * @param balance
     */
    public BankAccount(Customer holder, double balance) {
        this(balance);
        accountHolder = holder;
    }

    /**
     * Copy constructor
     *
     * @param other
     */
    public BankAccount(BankAccount other) {
        this(other.getAccountHolder(), other.getBalance());
        this.accountNumber = other.getAccountNumber();
    }

    protected abstract double getMonthlyFeesAndInterest();

    /**
     * Calls getMonthlyFeesAndInterest and updates the balance by the amount return.
     */
    public void monthEndUpdate() {
        BigDecimal temp = new BigDecimal(getMonthlyFeesAndInterest());
        if (temp.compareTo(BigDecimal.ZERO) >= 0) {
            deposit(temp.doubleValue());
        } else {
            withdraw(-temp.doubleValue());
        }
    }

    /**
     * Getter of account holder
     *
     * @return holder of the account
     */
    public Customer getAccountHolder() {
        return accountHolder;
    }

    /**
     * Setter of account holder
     *
     * @param other
     */
    public void setAccountHolder(Customer other) {
        if (accountHolder == null) {
            accountHolder = new Customer();
        }
        accountHolder = new Customer(other);
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
     * Withdraw money from the current account. Negative withdrawal will be
     * ignored.
     *
     * @param money the money to withdraw
     */
    public void withdraw(double money) {
        if (new BigDecimal(money).compareTo(new BigDecimal("0.0")) > 0 &&
                new BigDecimal(money).compareTo(new BigDecimal(balance)) <= 0) {
            balance -= money;
        }
    }

    /**
     * Transfer money from the current account to another. The transfer only
     * happens ifthe account has enough funds.
     *
     * @param money the money to transfer
     */
    public void transfer(double money, BankAccount other) {
        if (new BigDecimal(money).compareTo(new BigDecimal("0.0")) > 0 &&
                new BigDecimal(money).compareTo(new BigDecimal(balance)) <= 0) {
            balance -= money;
            other.deposit(money);
        }
    }

    @Override
    public String toString() {
        return "(" + accountHolder.getName() + " " + accountHolder.getID() + ") "
                + accountNumber + ": " + balance;
    }
}
