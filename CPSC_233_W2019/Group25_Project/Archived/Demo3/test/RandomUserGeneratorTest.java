import org.junit.Test;

import static org.junit.Assert.*;

public class RandomUserGeneratorTest {

    @Test
    public void test_createRandomUuid() {
        assertNotNull("Class should create a random UUID", RandomUserGenerator.createRandomUuid());
    }

    @Test
    public void test_createRandomUsername() {
        assertNotNull("Class should create a random user name", RandomUserGenerator.createRandomUsername());
    }

    @Test
    public void test_createRandomRole() {
        assertNotNull("Class should create a random role", RandomUserGenerator.createRandomRole());
    }

    @Test
    public void test_createRandomFullName() {
        assertNotNull("Class should create a random full name", RandomUserGenerator.createRandomFullName());
    }

    @Test
    public void test_createRandomGender() {
        assertNotNull("Class should create a random gender", RandomUserGenerator.createRandomGender());
    }

    @Test
    public void test_createRandomDateOfBirth() {
        assertNotNull("Class should create a random date of birth", RandomUserGenerator.createRandomDateOfBirth());
    }

    @Test
    public void test_createRandomEmailAddress() {
        assertNotNull("Class should create a random E-mail address", RandomUserGenerator.createRandomEmailAddress());
    }

    @Test
    public void test_createRandomPhoneNumber() {
        assertNotNull("Class should create a random phone number", RandomUserGenerator.createRandomPhoneNumber());
    }

    @Test
    public void test_createRandomAddress() {
        assertNotNull("Class should create a random address", RandomUserGenerator.createRandomEmailAddress());
    }

    @Test
    public void test_createRandomPassword() {
        assertNotNull("Class should create a random password", RandomUserGenerator.createRandomPassword());
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