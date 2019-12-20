import java.util.ArrayList;

//Assignment requirements: provide trace of this code.
public class Bank {
    private ArrayList<BankAccount> accounts = new ArrayList<BankAccount>();

    public static void main(String[] args) {
        Bank        myBank   = new Bank();
        BankAccount account1 = new BankAccount(0.0, "0001");
        BankAccount account2 = new BankAccount(10.0, "0002");

        myBank.addAccount(account1);
        myBank.addAccount(account2);

        account1.deposit(50.0);

        System.out.println(myBank.toString());
    }

    public void addAccount(BankAccount account) {
        accounts.add(account);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (BankAccount account : accounts) {
            builder.append(account.toString());
            builder.append("\n");
        }
        return builder.toString();
    }
}
