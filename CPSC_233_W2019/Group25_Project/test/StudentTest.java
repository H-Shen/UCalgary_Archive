import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * The {@code StudentTest} class for testing Student class.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class StudentTest {

    private final Student test = new Student();
    private final User    user = new Student();

    @Test
    public void test_removeFromCurrentCoursesList() {
        test.addCurrentCourses("CPSC231");
        test.addCurrentCourses("CPSC233");
        test.removeFromCurrentCoursesList("CPSC233");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("CPSC231");
        expected.add("CPSC233");
        expected.remove("CPSC233");
        assertEquals(expected, test.getCurrentCoursesList());
    }

    @Test
    public void test_getTakenCoursesListAsString() {
        assertNotNull(test.getTakenCoursesListAsString());
    }

    @Test
    public void test_getCurrentCoursesListAsString() {
        assertNotNull(test.getCurrentCoursesListAsString());
    }

    @Test
    public void test_getTakenCoursesList() {
        assertNotNull(test.getTakenCoursesList());
    }

    @Test
    public void test_getCurrentCoursesList() {
        assertNotNull(test.getCurrentCoursesList());
    }

    @Test
    public void test_addCurrentCourses() {
        test.addCurrentCourses("CPSC231");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("CPSC233");
        assertNotEquals(expected, test.getCurrentCoursesList());
    }

    @Test
    public void test_addCurrentCourses_1() {
        assertNotNull(test.getCurrentCoursesList());
    }

    @Test
    public void test_modifyUsername_2() {
        user.setUsername("demoStudent");
        user.setUsername("     ");
        assertNotEquals("New username is invaid (username should not change)", "     ", user.getUsername());
    }

    @Test
    public void test_modifyFullName_1() {
        user.setFullName("SAMIR CHIN");
        user.setFullName("SAM CHAN");
        assertEquals("Full name should be modified to SAM CHAN", "SAM CHAN", user.getFullName());
    }

    @Test
    public void test_modifyRole_1() {
        user.setRole(Constants.ROLE[0]);
        user.setRole(Constants.ROLE[2]);
        assertEquals("Role should be modified from ADMIN to STUDENT", "STUDENT", user.getRole());
    }

    @Test
    public void test_modifyRole_2() {
        user.setRole(Constants.ROLE[0]);
        user.setRole("PROFESSOR");
        assertNotEquals("Role should not be modified", "PROFESSOR", user.getRole());
    }

    @Test
    public void test_modifyGender_1() {
        user.setGender("F");
        user.setGender("M");
        assertEquals("Gender should be modified to M", "M", user.getGender());
    }

    @Test
    public void test_modifyGender_2() {
        user.setGender("M");
        user.setGender("O");
        assertNotEquals("Gender cannot be modified (must only be M or F", "O", user.getGender());
    }

    @Test
    public void test_modifyAddress_1() {
        user.setAddress("KAJSHASHIUAHUSHSA");
        user.setAddress("ASDJSDAFGKWEAFLKSDF");
        assertEquals("Address should be modified to ASDJSDAFGKWEAFLKSDF", "ASDJSDAFGKWEAFLKSDF", user.getAddress());

    }

    @Test
    public void test_modifyAddress_2() {
        user.setAddress("!@#$%^&*");
        assertNotEquals("Address cannot be modified (Illegal characters)", "!@#$%^&*", user.getAddress());
    }

    @Test
    public void test_modifyPhoneNumber_1() {
        user.setPhoneNumber("999-999-9999");
        user.setPhoneNumber("403-123-4567");
        assertEquals("Phone number should be modified to 403-123-4567", "403-123-4567", user.getPhoneNumber());
    }

    @Test
    public void test_modifyPhoneNumber_2() {
        user.setPhoneNumber("999-999-9999");
        user.setPhoneNumber("4031234567");
        assertNotEquals("Phone number should not be modified (format not valid)", "4031234567", user.getPhoneNumber());
    }

    @Test
    public void test_modifyDateOfBirth_1() {
        user.setDateOfBirth(new DateOfBirth("1985", "01", "01"));
        user.setDateOfBirth(new DateOfBirth("2010", "09", "21"));
        assertNotNull("Date of birth should be modified to 2010-09-21", user.getDateOfBirth());
    }

    @Test
    public void test_modifyDateOfBirth_2() {
        user.setDateOfBirth(new DateOfBirth("1985", "01", "01"));
        user.setDateOfBirth(new DateOfBirth("0000", "00", "00"));
        assertNotEquals("Date of birth should not be modified (format not valid)", "0000-00-00", user.getDateOfBirth());
    }

    @Test
    public void test_modifyEmailAddress_1() {
        user.setEmailAddress("9sa8dy98asdy9@9adsu90asud.cn");
        user.setEmailAddress("group25@ucalgary.ca");
        assertEquals("Email address should be modified to group25@ucalgary.ca", "group25@ucalgary.ca", user.getEmailAddress());
    }

    @Test
    public void test_modifyEmailAddress_2() {
        user.setEmailAddress("9sa8dy98asdy9@9adsu90asud.cn");
        user.setEmailAddress("@ucalgary.ca");
        assertNotEquals("Email address should be not modified (format not valid)", "@ucalgary.ca", user.getEmailAddress());
    }

    @Test
    public void test_modifyPassword_1() {
        user.setPassword("demodemo");
        user.setPassword("omedomed");
        assertEquals("Password should be modified to omedomed", "omedomed", user.getPassword());
    }

    @Test
    public void test_modifyPassword_2() {
        user.setPassword("demodemo");
        user.setPassword("demo");
        assertNotEquals("Password should be modified", "demo", user.getPassword());
    }
}