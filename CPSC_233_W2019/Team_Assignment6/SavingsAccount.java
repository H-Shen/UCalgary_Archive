import java.math.BigDecimal;

/**
 * The {@code SavingsAccount} class provides methods of simulating a saving
 * account in the real world.
 * <p>
 *
 * @author Group25
 * @version 2019-03-09
 */

public class SavingsAccount extends BankAccount {

    private double annualInterestRate = 0.05;
    private double minimumBalance;

    /**
     * Default constructor
     */
    SavingsAccount() {
    }

    /**
     * Constructor with annualInterestRate
     *
     * @param annualInterestRate
     */
    SavingsAccount(double annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    /**
     * Constructor with balance and annualInterestRate
     *
     * @param balance
     * @param annualInterestRate
     */
    SavingsAccount(double balance, double annualInterestRate) {
        this(annualInterestRate);
        super.deposit(balance);
    }

    /**
     * Constructor with accountHolder and balance
     *
     * @param accountHolder
     * @param balance
     */
    SavingsAccount(Customer accountHolder, double balance) {
        super(accountHolder, balance);
    }

    /**
     * Constructor with accountHolder, balance and annualInterestRate
     *
     * @param accountHolder
     * @param balance
     * @param annualInterestRate
     */
    SavingsAccount(Customer accountHolder, double balance, double annualInterestRate) {
        this(accountHolder, balance);
        setAnnualInterestRate(annualInterestRate);
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
        return (getAnnualInterestRate() * getBalance()) / 12;
    }
}