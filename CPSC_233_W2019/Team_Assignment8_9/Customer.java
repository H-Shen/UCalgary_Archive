import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The {@code Customer} class provides static methods of simulating a customer
 * the real world.
 * <p>
 *
 * @author Group25
 * @version 2019-03-24
 */
public class Customer {

    private String name;
    private int    ID;

    /**
     * Default constructor
     */
    public Customer() {
    }

    /**
     * Constructor with name and ID
     *
     * @param name
     * @param ID
     */
    public Customer(String name, int ID) {
        setName(name);
        setID(ID);
    }

    /**
     * Copy constructor
     *
     * @param other
     */
    public Customer(Customer other) {
        this(other.getName(), other.getID());
    }

    /**
     * Constructor that reads the name and the ID from BufferedReader
     *
     * @param br
     */
    public Customer(BufferedReader br) throws IOException {
        String name;
        String ID;
        if ((name = br.readLine()) != null) {
            if (name.contains("null")) {
                throw new IOException("Customer is null in file");
            } else {
                setName(name);
            }
            if ((ID = br.readLine()) == null) {
                throw new IOException("No customer ID found in file");
            } else {
                setID(Integer.valueOf(ID));
            }
        }
    }

    /**
     * Store name and id to PrintWriter
     *
     * @param pw
     */
    public void save(PrintWriter pw) throws IOException {
        pw.println(getName());
        pw.println(getID());
    }

    /**
     * Getter of name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter of name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter of ID
     *
     * @return ID
     */
    public int getID() {
        return ID;
    }

    /**
     * Setter of ID
     *
     * @param ID
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return getName() + " " + getID();
    }
}
