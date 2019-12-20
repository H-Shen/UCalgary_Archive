import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The {@code UserTest} class for testing User class.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class UserTest {

    static {
        Database.initialization();
    }

    private final User student = new Student();
    private final User admin   = new Admin();
    private final User faculty = new Faculty();

    @Test
    public void test_getUuid1() {
        student.setUuid("This-is-a-test-uuid-for-method-get-uuid-and-set-uuid");
        assertNotNull("UUID can not be empty", student.getUuid());
    }

    @Test
    public void test_getUuid2() {
        admin.setUuid("This-is-a-test-uuid-for-method-get-uuid-and-set-uuid");
        assertNotNull("UUID can not be empty", admin.getUuid());
    }

    @Test
    public void test_setUuid1() {
        student.setUuid(null);
        assertFalse("UUID must be valid", Validation.isUuidValid(student.getUuid()));
    }


    @Test
    public void test_setUuid2() {
        student.setUuid("54321");
        String expected = "54321";
        assertEquals("The expected UUID should be the same as the actual one", expected, student.getUuid());
    }

    @Test
    public void test_setUuid3() {
        admin.setUuid("54321");
        admin.setUuid("12345");
        admin.setUuid("99999");
        String expected = "54321";
        assertEquals("The expected UUID cannot be changed after being initialized", expected, admin.getUuid());
    }

    @Test
    public void test_getRole1() {
        String expected = "STUDENT";
        assertEquals("Class should return a String which indicates the role of the User", expected, student.getRole());
    }

    @Test
    public void test_getRole2() {
        String expected = "FACULTY";
        assertEquals("Class should return a String which indicates the role of the User", expected, faculty.getRole());
    }

    @Test
    public void test_getRole3() {
        String expected = "ADMIN";
        assertEquals("Class should return a String which indicates the role of the User", expected, admin.getRole());
    }

    @Test
    public void test_setRole1() {
        student.setRole("this is not a role");
        String expected = "this is not a role";
        assertNotEquals("The ROLE must be ADMIN or FACULTY or STUDENT", expected, student.getRole());
    }

    @Test
    public void test_setRole2() {
        faculty.setRole("this is not a role");
        String expected = "this is not a role";
        assertNotEquals("The ROLE must be ADMIN or FACULTY or STUDENT", expected, faculty.getRole());
    }

    @Test
    public void test_setRole3() {
        admin.setRole("this is not a role");
        String expected = "this is not a role";
        assertNotEquals("The ROLE must be ADMIN or FACULTY or STUDENT", expected, admin.getRole());
    }

    @Test
    public void test_setRole4() {
        student.setRole("FACULTY");
        String expected = "FACULTY";
        assertEquals("The ROLE should be changed into FACULTY", expected, student.getRole());
    }

    @Test
    public void test_setRole5() {
        faculty.setRole("STUDENT");
        String expected = "STUDENT";
        assertEquals("The ROLE should be changed into STUDENT", expected, faculty.getRole());
    }

    @Test
    public void test_setRole6() {
        admin.setRole("STUDENT");
        String expected = "STUDENT";
        assertEquals("The ROLE should be changed into STUDENT", expected, admin.getRole());
    }

    @Test
    public void test_setRole7() {
        admin.setRole("STUDENT");
        admin.setRole("FACULTY");
        String expected = "FACULTY";
        assertEquals("The ROLE should be changed into FACULTY", expected, admin.getRole());
    }

    @Test
    public void test_getUsername1() {
        student.setUsername("little baby");
        String expected = "little baby";
        assertEquals("Should return a String which is modified by setting method", expected, student.getUsername());
    }

    @Test
    public void test_getUsername2() {
        admin.setUsername("big baby");
        String expected = "big baby";
        assertEquals("Should return a String which is modified by setting method", expected, admin.getUsername());
    }

    @Test
    public void test_setUsername1() {
        admin.setUsername("blue baby");
        admin.setUsername("purple bay");
        admin.setUsername("red baby");
        String expected = "red baby";
        assertEquals("Username should be able to be modified multiple times", expected, admin.getUsername());
    }

    @Test
    public void test_getFullName1() {
        student.setFullName("LITTLE BABY");
        String expected = "LITTLE BABY";
        assertEquals("The method should return a full name", expected, student.getFullName());
    }

    @Test
    public void test_setFullName1() {
        student.setUsername("little baby");
        String expected = "little baby";
        assertNotEquals("The method should check if the full name is valid", expected, student.getFullName());
    }

    @Test
    public void test_setFullName2() {
        student.setUsername(null);
        assertNull("A null can not be taken as a full name", student.getFullName());
    }


    @Test
    public void test_getGender1() {
        student.setGender("M");
        assertNotNull("Get M", student.getGender());
    }

    @Test
    public void test_getGender2() {
        student.setGender("F");
        assertNotNull("Get F", student.getGender());
    }

    @Test
    public void test_setGender1() {
        student.setGender("M");
        student.setGender("F");
        assertEquals("Gender can be modified", "F", student.getGender());
    }

    @Test
    public void test_setGender2() {
        student.setGender("0.3 female and 0.7 male");
        assertNull("Gender must be M or F", student.getGender());
    }

    @Test
    public void test_getDateOfBirth() {
        student.setDateOfBirth(new DateOfBirth("2000", "10", "01"));
        assertNotNull(student.getDateOfBirth());
    }

    @Test
    public void test_getEmailAddress() {
        student.setEmailAddress("sdfkjh@me.com");
        assertNotNull(student.getEmailAddress());
    }

    @Test
    public void test_setEmailAddress() {
        student.setEmailAddress("sdfkjh@me.com");
        student.setEmailAddress("111111@icloud.com");
        assertEquals("111111@icloud.com", student.getEmailAddress());
    }

    @Test
    public void test_getPhoneNumber() {
        student.setPhoneNumber("403-110-1100");
        assertNotNull(student.getPhoneNumber());
    }

    @Test
    public void test_setPhoneNumber() {
        student.setPhoneNumber("403");
        assertNull("Phone number format invalid", student.getPhoneNumber());
    }

    @Test
    public void test_getPassword() {
        student.setPassword("Jn_9284@PP");
        assertNotNull(student.getPassword());
    }

    @Test
    public void test_setPassword() {
        student.setPassword("1234");
        assertNull("The length of password cannot be smaller than 6", student.getPassword());
    }

    @Test
    public void test_getAddress() {
        student.setAddress("4150B");
        assertNotNull(student.getAddress());
    }

    @Test
    public void test_setAddress() {
        student.setAddress("12345");
        student.setAddress("5421");
        assertNotNull(student.getAddress());
    }

    @Test
    public void test_compareTo1() {
        assertTrue("If compared to null, should return -1", 0 > student.compareTo(null));
    }

    @Test
    public void test_toString1() {
        student.setUsername("username");
        student.setAddress("Live in somewhere");
        student.setDateOfBirth(new DateOfBirth("1998", "10", "01"));
        assertNotNull(student.toString());
    }
}