import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The {@code RandomUserGeneratorTest} class for testing RandomUserGenerator class.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class RandomUserGeneratorTest {

    static {
        Database.releaseAllResources();
        Database.initialization();
    }

    @Test
    public void test_createRandomUuid() {
        String test;
        for (int i = 0; i < 100; ++i) {
            test = RandomUserGenerator.createRandomUuid();
            assertNotNull("Class should create a random UUID", test);
            assertTrue(Validation.isUuidValid(test));
        }
    }

    @Test
    public void test_createRandomUsername() {
        String test;
        for (int i = 0; i < 100; ++i) {
            test = RandomUserGenerator.createRandomUsername();
            assertNotNull("Class should create a random user name", RandomUserGenerator.createRandomUsername());
            assertTrue(Validation.isUsernameValid(test));
        }
    }

    @Test
    public void test_createRandomRole() {
        String test;
        for (int i = 0; i < 100; ++i) {
            test = RandomUserGenerator.createRandomRole();
            assertNotNull("Class should create a random role", test);
            assertTrue(Validation.isRoleValid(test));
        }
    }

    @Test
    public void test_createRandomFullName() {
        String test;
        for (int i = 0; i < 100; ++i) {
            test = RandomUserGenerator.createRandomFullName();
            assertNotNull("Class should create a random full name", test);
            assertTrue(Validation.isFullNameValid(test));
        }
    }

    @Test
    public void test_createRandomGender() {
        String test;
        for (int i = 0; i < 100; ++i) {
            test = RandomUserGenerator.createRandomGender();
            assertNotNull("Class should create a random gender", test);
            assertTrue(Validation.isGenderValid(test));
        }
    }

    @Test
    public void test_createRandomDateOfBirth() {
        DateOfBirth test;
        for (int i = 0; i < 100; ++i) {
            test = RandomUserGenerator.createRandomDateOfBirth();
            assertNotNull("Class should create a random date of birth", test);
            assertTrue(Validation.isDateOfBirthValid(test));
        }
    }

    @Test
    public void test_createRandomEmailAddress() {
        String test;
        for (int i = 0; i < 100; ++i) {
            test = RandomUserGenerator.createRandomEmailAddress();
            assertNotNull("Class should create a random E-mail address", test);
            assertTrue(Validation.isEmailAddressValid(test));
        }
    }

    @Test
    public void test_createRandomPhoneNumber() {
        String test;
        for (int i = 0; i < 100; ++i) {
            test = RandomUserGenerator.createRandomPhoneNumber();
            assertNotNull("Class should create a random phone number", test);
            assertTrue(Validation.isPhoneNumberValid(test));
        }
    }

    @Test
    public void test_createRandomAddress() {
        String test;
        for (int i = 0; i < 100; ++i) {
            test = RandomUserGenerator.createRandomAddress();
            assertNotNull("Class should create a random address", test);
            assertTrue(Validation.isAddressValid(test));
        }
    }

    @Test
    public void test_createRandomPassword() {
        String test;
        for (int i = 0; i < 100; ++i) {
            test = RandomUserGenerator.createRandomPassword();
            assertNotNull("Class should create a random password", test);
            assertTrue(Validation.isPasswordValid(test));
        }
    }

    @Test
    public void test_createRandomUser1() {
        Database.initializeAccounts();
        User expected = new Admin();
        User test     = RandomUserGenerator.createRandomUser("ADMIN");
        assertSame("Class should create an Admin user", test.getRole(), expected.getRole());
    }

    @Test
    public void test_createRandomUser2() {
        Database.initializeAccounts();
        User expected = new Student();
        User test     = RandomUserGenerator.createRandomUser("STUDENT");
        assertSame("Class should create a Student user", test.getRole(), expected.getRole());
    }

    @Test
    public void test_createRandomUser3() {
        Database.initializeAccounts();
        User expected = new Faculty();
        User test     = RandomUserGenerator.createRandomUser("FACULTY");
        assertSame("Class should create a Faculty user", test.getRole(), expected.getRole());
    }

    @Test
    public void test_selectRandomUser1() {
        User   test         = RandomUserGenerator.selectRandomUser("ADMIN");
        String expectedRole = "ADMIN";
        assertEquals("Class should select an Admin account", expectedRole, test.getRole());
    }

    @Test
    public void test_selectRandomUser2() {
        String expectedRole = "STUDENT";
        User   test         = RandomUserGenerator.selectRandomUser("STUDENT");
        assertEquals("Class should select a Student account", expectedRole, test.getRole());
    }

    @Test
    public void test_selectRandomUser3() {
        String expectedRole = "FACULTY";
        User   test         = RandomUserGenerator.selectRandomUser("FACULTY");
        assertEquals("Class should select a Faculty account", expectedRole, test.getRole());
    }
}