import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The {@code CourseAndGradeTest} class for testing CourseAndGrade.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class CourseAndGradeTest {

    // initialization
    static {
        Database.releaseAllResources();
        Database.initialization();
    }

    private final CourseAndGrade test = new CourseAndGrade("AFST301", "2");

    @Test
    public void test_compareTo1() {
        CourseAndGrade test2  = new CourseAndGrade("AFST301", "2");
        int            actual = test.compareTo(test2);
        assertEquals("Class should return 0 since the compared classes are exactly the same", 0, actual);
    }

    @Test
    public void test_compareTo2() {
        CourseAndGrade test2  = new CourseAndGrade("CPSC233", "3");
        int            actual = test.compareTo(test2);
        assertNotEquals("Class should not return 0 since the compared classes are not the same", 0, actual);

    }

    @Test(expected = NullPointerException.class)
    public void test_compareTo3() {
        CourseAndGrade test2  = new CourseAndGrade("test2", "1");
        int            actual = test.compareTo(test2);
        assertNotEquals("Class should raise an exception instead since test2 is not a valid course id", 0, actual);
    }

    @Test
    public void test_getCourseId() {
        assertEquals("AFST301", test.getCourseId());
    }

    @Test
    public void test_setCourseId() {
        test.setCourseId("CPSC233");
        assertSame("CPSC233", test.getCourseId());

    }

    @Test
    public void test_getGrade1() {
        assertSame("2", test.getGrade());
    }

    @Test
    public void test_setGrade2() {
        test.setGrade("3");
        assertSame("Class should change the grade into setting number 3", "3", test.getGrade());
    }


    @Test
    public void test_setGrade3() {
        test.setGrade("");
        assertSame("Class should not change the grade into an invalid number", "2", test.getGrade());

    }

    @Test
    public void test_setGrade4() {
        test.setGrade("8");
        assertSame("Class should not change the grade into in invalid number", "2", test.getGrade());
    }

    @Test
    public void test_setGrade5() {
        test.setGrade("test");
        assertSame("Class should not change the grade into a string", "2", test.getGrade());
    }

    @Test
    public void test_toString() {
        assertEquals("toString should show the CourseId and grade", "AFST301 : 2", test.toString());
    }
}