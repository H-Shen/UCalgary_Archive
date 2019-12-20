import java.math.BigDecimal;

/**
 * The {@code ChequingAccount} class inherits class BankAccount and provides static methods of simulating a bank
 * account the real world, with negative balance, overdraft possible.
 * <p>
 *
 * @author Group25
 * @version 2019-03-09
 */

public class ChequingAccount extends BankAccount {

    private double overdraftFee = 1.0;
    private double overdraftAmount;

    /**
     * Constructor with accountHolder, balance and overdraftFee
     *
     * @param accountHolder
     * @param balance
     * @param overdraftFee
     */
    ChequingAccount(Customer accountHolder, double balance, double overdraftFee) {
        super(accountHolder, balance);
        setOverdraftFee(overdraftFee);
    }

    /**
     * Getter of overdraftfee
     *
     * @return overdraftfee
     */
    public double getOverdraftFee() {
        return overdraftFee;
    }

    /**
     * Setter of overdraftFee
     *
     * @param overdraftFee
     */
    public void setOverdraftFee(double overdraftFee) {
        if (new BigDecimal(overdraftFee).compareTo(BigDecimal.ZERO) > 0) {
            this.overdraftFee = overdraftFee;
        }
    }

    /**
     * Getter of overdraftAmount
     *
     * @return overdraftAmount
     */
    public double getOverdraftAmount() {
        return overdraftAmount;
    }

    /**
     * Setter of overdraftAmount
     *
     * @param overdraftAmount
     */
    public void setOverdraftAmount(double overdraftAmount) {
        if (new BigDecimal(overdraftAmount).compareTo(BigDecimal.ZERO) > 0) {
            this.overdraftAmount = overdraftAmount;
        }
    }

    /**
     * Override the sufficientFunds in base class, re-define the criteria of sufficient funds in chequing account
     *
     * @param money
     * @return true if the amount specified (for withdrawal) will result in a balance that might be less than zero,
     * but never more than overdraftAmount below zero, false otherwise.
     */
    @Override
    public boolean sufficientFunds(double money) {
        BigDecimal balanceCopy   = new BigDecimal(getBalance());
        BigDecimal moneyCopy     = new BigDecimal(money);
        BigDecimal overdraftCopy = new BigDecimal(getOverdraftAmount());
        return (balanceCopy.compareTo(moneyCopy) >= 0 || moneyCopy.subtract(balanceCopy).compareTo(overdraftCopy) <= 0);
    }

    /**
     * Override the withdraw in base class, re-define the method of withdraw in chequing account
     *
     * @param money
     */
    @Override
    public void withdraw(double money) {
        if (new BigDecimal(getBalance()).compareTo(new BigDecimal(money)) < 0) {
            money += getOverdraftFee();
        }
        super.withdraw(money);
    }

    /**
     * Override the getMonthlyFeesAndInterest in base class, re-define the method of
     * calculation of monthlyFeesAndInterest in chequing account
     *
     * @return monthly fees and interest
     */
    @Override
    public double getMonthlyFeesAndInterest() {
        double balance = getBalance();
        return (new BigDecimal(balance).compareTo(BigDecimal.ZERO) < 0) ? balance * 0.2 : 0.0;
    }
}
