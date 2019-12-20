/**
 * The {@code Admin} class inherits class User and contains methods that initializes a admin user.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class Admin extends User {

    /**
     * The default constructor of Admin class, it sets the property of role to 'ADMIN'.
     *
     * @date 2019/04/08
     */
    public Admin() {
        setRole(Constants.ROLE[0]);
    }
}