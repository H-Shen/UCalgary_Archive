import java.util.ArrayList;

//Assignment requirements: provide trace of this code.
public class Bank {
    private ArrayList<BankAccount> accounts  = new ArrayList<BankAccount>();
    private ArrayList<Customer>    customers = new ArrayList<Customer>();

    public static void main(String[] args) {
        Bank myBank = new Bank();

        Customer c1 = new Customer("Alison Brown", 123);
        myBank.addCustomer(c1);

        BankAccount b1 = new BankAccount(c1, 100.00);
        myBank.addAccount(b1);

        Customer c2 = b1.getAccountHolder();
        c2.setName("Charles Green");
        myBank.addCustomer(c2);

        System.out.println(myBank.toString());

        Customer c3 = new Customer("Alan Turing", 1945);
        myBank.addCustomer(c3);
        BankAccount b2 = new BankAccount(c3, 50.0);
        myBank.addAccount(b2);

        System.out.println(myBank.toString());

        c3.setName("Grace Hopper");
        System.out.println(myBank.toString());
    }

    public void addAccount(BankAccount account) {
        accounts.add(account);
    }

    public void addCustomer(Customer aCustomer) {
        customers.add(aCustomer);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (BankAccount account : accounts) {
            builder.append(account.toString());
            builder.append("\n");
        }
        builder.append("\n");
        for (Customer aCustomer : customers) {
            builder.append(aCustomer.toString());
            builder.append("\n");
        }
        return builder.toString();
    }
}
