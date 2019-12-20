import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class PlayTest extends FormatTester {
    public static final String CLASSNAME = "Play";

    public PlayTest() {
        super(CLASSNAME, false);
    }

    private void testInterface() {
        String[] instanceVars = {"String writer", "int yearWritten"};
        assertTrue("Instance variables should be private with correct name and type.", instanceVariablesArePrivate(instanceVars));

        assertTrue("Class should not have the default constructor.", noDefaultConstructor());

        assertFalse("Should not override getRating.", hasMethod("int getRating"));
        assertFalse("Should not override getTitle.", hasMethod("String getTitle"));
    }

    private Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month - 1, day, 0, 0, 0);
        return cal.getTime();
    }

    // Testing constructors
    @Test
    public void test_Constructor_EmptyStringAndToday() {
        testInterface();
        Play c = new Play("TestingPlay", 0, "", 2018);
        assertEquals("Created play - testing theatre instance vars (title)", "TESTINGPLAY", c.getTitle());
        assertEquals("Created play - testing theatre instance vars (rating)", 0, c.getRating());
        assertEquals("Created play - empty string Writer, 2018 as year written", "", c.getWriter());
        assertEquals("Created play - empty string Writer, 2018 as year written", 2018, c.getYearWritten());
    }

    @Test
    public void test_Constructor_LastYear() {
        testInterface();
        Play c = new Play("TestingPlay", 0, "Screen Writer", 2017);
        assertEquals("Created play - testing theatre instance vars (title)", "TESTINGPLAY", c.getTitle());
        assertEquals("Created play - testing theatre instance vars (rating)", 0, c.getRating());
        assertEquals("Created play ", "Screen Writer", c.getWriter());
        assertEquals("Created play - 2017 as year written ", 2017, c.getYearWritten());
    }

    @Test
    public void test_Constructor_NearFutureDateWritten() {
        testInterface();
        Play c = new Play("TestingPlay", 0, "Screen Writer", 2020);
        assertEquals("Created play - testing theatre instance vars (title)", "TESTINGPLAY", c.getTitle());
        assertEquals("Created play - testing theatre instance vars (rating)", 0, c.getRating());
        assertEquals("Created play - testing Writer", "Screen Writer", c.getWriter());
        assertEquals("Created play - next year as year written, expecting to default to 2018", 2018, c.getYearWritten());
    }

    @Test
    public void test_CopyConstructor() {
        testInterface();
        Play c = new Play("TestingPlay", 0, "Writer", 2017);
        assertEquals("Created play - testing theatre instance vars (title)", "TESTINGPLAY", c.getTitle());
        assertEquals("Created play - testing theatre instance vars (rating)", 0, c.getRating());
        assertEquals("Created play - testing Writer", "Writer", c.getWriter());
        assertEquals("Created play - testing year", 2017, c.getYearWritten());
    }

    // getters and setters
    @Test
    public void test_getter_and_setter_yearWritten_lastYear() {
        testInterface();
        Play c = new Play("TestingPlay", 0, "Screen Writer", 2018);

        c.setYearWritten(2017);

        assertEquals("Created play with 2018 as year written then set to 2017", 2017, c.getYearWritten());
    }

    @Test
    public void test_getter_and_setter_yearWritten_NearFutureDateWritten() {
        testInterface();
        Play c = new Play("TestingPlay", 0, "Screen Writer", 2018);
        c.setYearWritten(2020);

        assertEquals("Created play then set next year as year written (expected 2018 to remain as year written).", 2018, c.getYearWritten());
    }

    @Test
    public void test_getter_and_setter_yearWritten_100YearsAgo() {
        testInterface();
        Play c = new Play("TestingPlay", 0, "Screen Writer", 2018);
        c.setYearWritten(1018);
        assertEquals("Created play with 2018 as year written then set year 1018 as year written).", 1018, c.getYearWritten());
    }

    @Test
    public void test_getter_and_setter_Writer() {
        testInterface();
        Play c = new Play("TestingPlay", 0, "Old Writer", 2018);
        c.setWriter("Changed Writer");
        assertEquals("Created play then changed Writer.", "Changed Writer", c.getWriter());
    }

    @Test
    public void test_getCategory_F() {
        testInterface();
        Play c = new Play("TestingCategoryF", 0, "Writer", 2018);

        c.setRating(2);
        assertEquals("Set rating to 2, year written to 2018's", "Modern", c.getCategory());

        c.setRating(0);
        c.setYearWritten(1955);
        assertEquals("Set rating to 0, year written to 1955", "Contemporary", c.getCategory());

        c.setRating(1);
        c.setYearWritten(1010);
        assertEquals("Set rating to 1", "Classic", c.getCategory());
    }

    @Test
    public void test_getCategory_Boundary() {
        testInterface();
        Play c = new Play("TestingCategoryF", 0, "Writer", 2018);

        c.setRating(3);
        c.setYearWritten(1969);
        assertEquals("Set rating to 3, year to 1969", "Modern", c.getCategory());

        c.setRating(4);
        c.setYearWritten(1968);
        assertEquals("Set rating to 4, year to 1968", "Contemporary", c.getCategory());
    }

}
