import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

/**
 * The {@code BankAccount} class provides static methods of simulating a bank
 * account the real world. Since it is generic, it is abstract and cannot be instantiated.
 * <p>
 *
 * @author Group25
 * @version 2019-03-29
 */

public abstract class BankAccount {

    private static final String   DEFAULT_ACCOUNT_NUMBER = "0001";
    private              double   balance;
    private              String   accountNumber          = DEFAULT_ACCOUNT_NUMBER;
    private              Customer accountHolder;

    /**
     * Default constructor
     */
    public BankAccount() {
    }

    /**
     * Constructor that takes BufferedReader and reads balance, accountNumber and accountHolder from it
     *
     * @param br
     */
    public BankAccount(BufferedReader br) throws IOException {
        this(Double.parseDouble(br.readLine()), br.readLine());
        try {
            setAccountHolder(new Customer(br));
        } catch (IOException e) {
            setAccountHolder(null);
        }
    }

    /**
     * Constructor with balance
     *
     * @param balance
     */
    public BankAccount(double balance) {
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
    public BankAccount(Customer accountHolder, double balance) {
        this(balance);
        setAccountHolder(accountHolder);
    }

    /**
     * Copy constructor
     *
     * @param other
     */
    public BankAccount(BankAccount other) {
        this(other.getBalance(), other.getAccountNumber());
        setAccountHolder(other.getAccountHolder());
    }

    /**
     * The method stores balance, accountNumber and information of accountHolder to filename
     */
    public void saveToTextFile(String filename) throws IOException {

        // using try-with-resources statement to make sure PrintWriter pw can close automatically no matter
        // what kind of exceptions will be raised, since pw.close() could possibly raise NullPointerException.
        try (PrintWriter pw = new PrintWriter(filename)) {
            pw.println(getBalance());
            pw.println(getAccountNumber());
            Customer temp = getAccountHolder();
            if (temp == null) {
                pw.println("null");
            } else {
                temp.save(pw);
            }
        } catch (Exception e) {
        }
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
        } else {
            BankApp.showError();
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
        } else {
            BankApp.showError();
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