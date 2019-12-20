import java.math.BigDecimal;

/**
 * The {@code SavingsAccount} class provides methods of simulating a saving
 * account in the real world.
 * <p>
 * All methods ensure that the balance in a bank account is never overdrafted
 * (a negative amount) and that only positive amounts are deposited/withdrawn.
 * <p>
 *
 * @author Haohu Shen
 * @version 2019-03-02
 */

public class SavingsAccount extends BankAccount {

    private static final double DEFAULT_ANNUAL_INTEREST_RATE = 0.05;
    private              double annualInterestRate           = DEFAULT_ANNUAL_INTEREST_RATE;
    private              double minimumBalance;

    /**
     * Default constructor
     */
    public SavingsAccount() {

    }

    /**
     * Constructor with the annual interest rate
     *
     * @param annualInterestRate
     */
    public SavingsAccount(double annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    /**
     * Constructor with the accountHolder, the balance and the annual interest
     * rate.
     *
     * @param accountHolder
     * @param balance
     * @param annualInterestRate
     */
    public SavingsAccount(Customer accountHolder, double balance, double annualInterestRate) {
        this(annualInterestRate);
        super.deposit(balance);
        super.setAccountHolder(new Customer(accountHolder));
    }

    /**
     * Getter of annual interest rate
     *
     * @return annualInterestRate
     */
    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    /**
     * Setter of annual interest rate
     *
     * @param annualInterestRate
     */
    public void setAnnualInterestRate(double annualInterestRate) {
        BigDecimal temp = new BigDecimal(annualInterestRate);
        if (temp.compareTo(BigDecimal.ZERO) >= 0) {
            if (temp.compareTo(BigDecimal.ONE) <= 0) {
                this.annualInterestRate = annualInterestRate;
            }
        }
    }

    /**
     * Setter of annual minimum balance
     *
     * @param minimumBalance
     */
    public void setMinimumBalance(double minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    /**
     * Override the withdraw method from the parent class, it verifies that
     * there are sufficient funds in the account for the withdraw before
     * invoking the withdraw method in the parent class.
     *
     * @param money the money to withdraw
     */
    @Override
    public void withdraw(double money) {
        BigDecimal temp = new BigDecimal(super.getBalance()).subtract(new BigDecimal(money));
        if (temp.compareTo(new BigDecimal(minimumBalance)) >= 0) {
            super.withdraw(money);
        }
    }

    /**
     * Calculates and returns the amount of interest earned on the balance in the
     * SavingsAccount on a monthly basis
     */
    @Override
    public double getMonthlyFeesAndInterest() {
        return super.getBalance() * annualInterestRate / 12.0;
    }
}
