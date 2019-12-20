import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class StudentTest {

    private final Student test = new Student();

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
        test.addCurrentCourses("CPSC 231");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("CPSC233");
        assertNotEquals(expected, test.getCurrentCoursesList());
    }

    @Test
    public void test_addCurrentCourses_1() {
        assertNotNull(test.getCurrentCoursesList());
    }
}