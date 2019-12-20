/**
 * The {@code Customer} class provides static methods of simulating a customer
 * the real world.
 * <p>
 *
 * @author Group25
 * @version 2019-02-08
 */
public class Customer {

    private String name;
    private int    ID;

    /**
     * Constructor with name and ID
     *
     * @param name
     * @param ID
     */
    public Customer(String name, int ID) {
        this.name = name;
        this.ID = ID;
    }

    /**
     * Default constructor
     */
    public Customer() {

    }

    /**
     * Copy constructor
     *
     * @param other
     */
    public Customer(Customer other) {
        this.name = other.getName();
        this.ID = other.getID();
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
        return name + " " + ID;
    }
}
