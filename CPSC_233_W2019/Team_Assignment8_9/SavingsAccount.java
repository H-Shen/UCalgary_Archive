import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;

/**
 * The {@code SavingsAccount} class provides methods of simulating a saving
 * account in the real world.
 * <p>
 *
 * @author Group25
 * @version 2019-03-29
 */

public class SavingsAccount extends BankAccount {

    private static final double DEFAULT_ANNUAL_INTEREST_RATE = 0.05;
    private static final int    MONTH_IN_A_YEAR              = 12;
    private              double annualInterestRate           = DEFAULT_ANNUAL_INTEREST_RATE;
    private              double minimumBalance;

    /**
     * Default constructor
     */
    public SavingsAccount() {
    }

    /**
     * Constructor that takes BufferedReader and reads annual interest rate, minimum balance and other information of
     * bank account from it
     *
     * @param br
     */
    public SavingsAccount(BufferedReader br) throws IOException {
        super(br);
        setAnnualInterestRate(Double.parseDouble(br.readLine()));
        setMinimumBalance(Double.parseDouble(br.readLine()));
    }

    /**
     * Constructor with annualInterestRate
     *
     * @param annualInterestRate
     */
    public SavingsAccount(double annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    /**
     * Constructor with balance and annualInterestRate
     *
     * @param balance
     * @param annualInterestRate
     */
    public SavingsAccount(double balance, double annualInterestRate) {
        this(annualInterestRate);
        super.deposit(balance);
    }

    /**
     * Constructor with accountHolder and balance
     *
     * @param accountHolder
     * @param balance
     */
    public SavingsAccount(Customer accountHolder, double balance) {
        super(accountHolder, balance);
    }

    /**
     * Constructor with accountHolder, balance and annualInterestRate
     *
     * @param accountHolder
     * @param balance
     * @param annualInterestRate
     */
    public SavingsAccount(Customer accountHolder, double balance, double annualInterestRate) {
        this(accountHolder, balance);
        setAnnualInterestRate(annualInterestRate);
    }

    /**
     * The method stores annual interest rate, minimum balance and other information of bank account to filename
     */
    @Override
    public void saveToTextFile(String filename) throws IOException {

        super.saveToTextFile(filename);

        // using try-with-resources statement to make sure PrintWriter pw can close automatically no matter
        // what kind of exceptions will be raised, since pw.close() could possibly raise NullPointerException.
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(filename, true))) {
            pw.println(getAnnualInterestRate());
            pw.println(getMinimumBalance());
        } catch (Exception e) {
        }
    }

    /**
     * Getter of annualInterestRate
     *
     * @return annualInterestRate
     */
    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    /**
     * Setter of annualInterestRate
     *
     * @param annualInterestRate
     */
    public void setAnnualInterestRate(double annualInterestRate) {
        BigDecimal temp = new BigDecimal(annualInterestRate);
        if (temp.compareTo(BigDecimal.ZERO) >= 0 && temp.compareTo(BigDecimal.ONE) <= 0) {
            this.annualInterestRate = annualInterestRate;
        }
    }

    /**
     * Getter of minimum balance
     *
     * @return minimumBalance
     */
    public double getMinimumBalance() {
        return minimumBalance;
    }

    /**
     * Setter of minimumBalance
     *
     * @param minimumBalance
     */
    public void setMinimumBalance(double minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    /**
     * Override the withdraw in base class, re-define the method of withdraw in savings account
     *
     * @param money
     */
    @Override
    public void withdraw(double money) {
        BigDecimal temp = new BigDecimal(getBalance()).subtract(new BigDecimal(money));
        if (temp.compareTo(new BigDecimal(getMinimumBalance())) >= 0) {
            super.withdraw(money);
        } else {
            BankApp.showError();
        }
    }

    /**
     * Override the getMonthlyFeesAndInterest in base class, re-define the method of
     * calculation of monthlyFeesAndInterest in savings account
     *
     * @return monthly fees and interest
     */
    @Override
    public double getMonthlyFeesAndInterest() {
        return (getAnnualInterestRate() * getBalance()) / MONTH_IN_A_YEAR;
    }
}