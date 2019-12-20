import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FacultyTest {

    private final Faculty test = new Faculty();

    @Test
    public void test_getCoursesTeaching() {
        assertNotNull("Class should create a faculty user", test.getCoursesTeaching());
    }

    @Test
    public void test_addCoursesTeaching1() {
        test.addCoursesTeaching("CPSC233");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("CPSC233");
        assertEquals("Class should be able to add new courses to ArrayList", expected, test.getCoursesTeaching());
    }

    @Test
    public void test_addCoursesTeaching2() {
        test.addCoursesTeaching("CPSC233");
        test.addCoursesTeaching("STAT213");
        test.addCoursesTeaching("MATH211");
        ArrayList<String> expected = new ArrayList<>();
        expected.add("CPSC233");
        expected.add("STAT213");
        expected.add("MATH211");
        assertEquals("Class should be able to add new courses to ArrayList", expected, test.getCoursesTeaching());
    }
}