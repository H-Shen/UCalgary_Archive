import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The {@code AdminTextUITest} class for testing AdminTextUI.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class AdminTextUITest {

    // initialization
    static {
        Database.releaseAllResources();
        Database.initialization();
    }

    @Test
    public void modifyUsername_test() {
        Admin  test        = (Admin) RandomUserGenerator.createRandomUser("ADMIN");
        String newUsername = RandomUserGenerator.createRandomUsername();
        test.setUsername(newUsername);

        assertEquals(newUsername, test.getUsername());
        assertFalse(test.getUsername().trim().isEmpty());
    }


    @Test
    public void modifyFullName_test() {
        Admin  test        = (Admin) RandomUserGenerator.createRandomUser("ADMIN");
        String newFullname = RandomUserGenerator.createRandomFullName();
        test.setFullName(newFullname);
        assertTrue(test.getFullName().matches("^[A-Z ]*$"));
        assertTrue(test.getFullName().length() >= 2 && test.getFullName().length() <= 40);
    }

    @Test
    public void modifyGender_test() {
        Admin  test      = (Admin) RandomUserGenerator.createRandomUser("ADMIN");
        String newGender = RandomUserGenerator.createRandomGender();
        test.setGender(newGender);
        assertEquals("ADMIN", test.getRole());
    }

    @Test
    public void modifyAddress_test() {
        Admin  test       = (Admin) RandomUserGenerator.createRandomUser("ADMIN");
        String newAddress = RandomUserGenerator.createRandomAddress();
        test.setAddress(newAddress);
        assertFalse(test.getAddress().isEmpty());
        assertTrue(test.getAddress().length() <= 50);
        assertTrue(test.getAddress().matches("^[-0-9a-zA-Z ,.]+$"));
    }
}