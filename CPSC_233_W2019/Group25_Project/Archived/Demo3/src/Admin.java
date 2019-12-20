/**
 * The {@code Admin} class inherits class User and contains methods that initializes a admin user.
 *
 * @author Group25
 * @date 2019/3/23
 */
public class Admin extends User {

    /**
     * Default constructor of Admin class.
     */
    public Admin() {
        setRole(Constants.ROLE[0]);
    }

}