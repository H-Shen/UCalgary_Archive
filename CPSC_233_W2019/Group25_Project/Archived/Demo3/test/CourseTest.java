import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;

public class CourseTest {

    // initialization
    static {
        Database.releaseAllResources();
        Database.initializePublicInformation();
    }

    private final Course test = new Course();

    @Test
    public void test_purser() {
        assertNotNull(Course.purser("asdf"));
    }

    @Test
    public void test_removeStudentsWhoAreTaking() {
        test.addStudentsWhoAreTaking("79d697ab-5fed-4807-a08b-59ab9c6d3803");
        HashSet<String> expected = new HashSet<>();
        expected.add("79d697ab-5fed-4807-a08b-59ab9c6d3803");
        expected.remove("79d697ab-5fed-4807-a08b-59ab9c6d3803\n");
        assertEquals(expected, test.getStudentsWhoAreTaking());
    }

    @Test
    public void test_getStudentsWhoAreTaking() {
        test.addStudentsWhoAreTaking("79d697ab-5fed-4807-a08b-59ab9c6d3803");
        assertNotNull(test.getStudentsWhoAreTaking());
    }

    @Test
    public void test_addStudentsWhoAreTaking() {
        test.addStudentsWhoAreTaking("79d697ab-5fed-4807-a08b-59ab9c6d3803");
        HashSet<String> expected = new HashSet<>();
        expected.add("79d697ab-5fed-4807-a08b-59ab9c6d3803");
        assertEquals(expected, test.getStudentsWhoAreTaking());
    }

    @Test
    // the user course cannot set a course id that is in the database, setter will used only when the user want to
    // create a new course
    public void test_getCourseId() {
        test.setCourseId("CPSC231");
        assertNull(test.getCourseId());
    }

    @Test
    public void test_setCourseId_1() {
        test.setCourseId("CPSC2311");
        assertSame("CPSC2311", test.getCourseId());
    }

    @Test
    public void test_getCourseName() {
        test.setCourseName("Computer Graphics");
        assertNotNull(test.getCourseName());
    }

    @Test
    public void test_setCourseName() {
        test.setCourseName("Computer Graphics");
        assertSame("Computer Graphics", test.getCourseName());
    }

    @Test
    public void test_getCourseDescription() {
        test.setCourseDescription("asdkfjhaskldjfhliasfhea");
        assertNotNull(test.getCourseDescription());
    }

    @Test
    public void test_setCourseDescription() {
        test.setCourseDescription("asdfaklsdjfhlksajdfhlaksfsdf");
        assertSame("asdfaklsdjfhlksajdfhlaksfsdf", test.getCourseDescription());
    }

    @Test
    public void test_getCourseUnits() {
        test.setCourseUnits("1.1");
        assertNotNull(test.getCourseUnits());
    }

    @Test
    public void test_setCourseUnits() {
        test.setCourseUnits("1.1");
        assertSame("1.1", test.getCourseUnits());
    }

    @Test
    public void test_getPrerequisitesAsString() {
        test.setPrerequisitesAsNew("CPSC313");
        assertNotNull(test.getPrerequisitesAsString());
    }

    @Test
    public void test_getAntirequisitesAsString() {
        test.setPrerequisitesAsNew("CPSC313");
        assertNotNull(test.getPrerequisitesAsString());
    }

    @Test
    public void test_getPrerequisites() {
        HashSet<String> test2 = new HashSet<>();
        test2.add("CPSC331");
        test.setPrerequisites(test2);
        assertNotNull(test.getPrerequisites());
    }

    @Test
    public void test_setPrerequisites() {
        HashSet<String> test2 = new HashSet<>();
        test2.add("CPSC331");
        test.setPrerequisites(test2);
        assertEquals(test2, test.getPrerequisites());


    }

    @Test
    public void test_setPrerequisitesAsNew() {
        test.setAntirequisitesAsNew("");
        assertNotSame("", test.getAntirequisitesAsString());
    }

    @Test
    public void test_getAntirequisites() {
        HashSet<String> test2 = new HashSet<>();
        test2.add("MATH211");
        test.setAntirequisites(test2);
        assertNotNull(test.getAntirequisites());
    }

    @Test
    public void test_setAntirequisites() {
        HashSet<String> test2 = new HashSet<>();
        test2.add("MATH211");
        test.setAntirequisites(test2);
        assertNotNull(test.getAntirequisites());
    }

    @Test
    public void test_setAntirequisitesAsNew() {
        test.setAntirequisitesAsNew("");
        assertNotSame("", test.getAntirequisitesAsString());
    }

    @Test
    public void test_getCanBeRepeated() {
        test.setCanBeRepeated("YES");
        assertNotNull(test.getCanBeRepeated());
    }

    @Test
    public void test_setCanBeRepeated() {
        test.setCanBeRepeated("YES");
        assertSame("YES", test.getCanBeRepeated());
    }

}