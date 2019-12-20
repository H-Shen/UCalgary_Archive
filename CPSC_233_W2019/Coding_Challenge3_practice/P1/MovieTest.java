import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

public class MovieTest extends FormatTester {
    public static final String CLASSNAME = "Movie";

    public MovieTest() {
        super(CLASSNAME, false);
    }

    private void testInterface() {
        String[] instanceVars = {"String director", "Date releaseDate"};
        assertTrue("Instance variables should be private with correct name and type.", instanceVariablesArePrivate(instanceVars));

        assertTrue("Class should not have the default constructor.", noDefaultConstructor());

        assertFalse("Should not override getRating.", hasMethod("int getRating()"));
        assertFalse("Should not override getTitle.", hasMethod("String getTitle()"));
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
        Date  now = new Date();
        Movie c   = new Movie("TestingMovie", 0, "", now);
        assertEquals("Created movie - testing theatre instance vars (title)", "TESTINGMOVIE", c.getTitle());
        assertEquals("Created movie - testing theatre instance vars (rating)", 0, c.getRating());
        assertEquals("Created movie - empty string director, today as release date", "", c.getDirector());
        assertEquals("Created movie - empty string director, today as release date", now, c.getReleaseDate());
    }

    @Test
    public void test_Constructor_LastYear() {
        testInterface();
        Date  d = getDate(2018, 1, 1);
        Movie c = new Movie("TestingMovie", 0, "Ava DuVernay", d);
        assertEquals("Created movie - testing theatre instance vars (title)", "TESTINGMOVIE", c.getTitle());
        assertEquals("Created movie - testing theatre instance vars (rating)", 0, c.getRating());
        assertEquals("Created movie - empty string director, Jan 1, 2018 as release date", "Ava DuVernay", c.getDirector());
        assertEquals("Created movie - empty string director, Jan 1, 2019 as release date", d, c.getReleaseDate());
    }

    @Test
    public void test_Constructor_NearFutureReleaseDate() {
        testInterface();
        Date  d = getDate(2019, 12, 11);
        Movie c = new Movie("TestingMovie", 0, "Ava DuVernay", d);
        assertEquals("Created movie - testing theatre instance vars (title)", "TESTINGMOVIE", c.getTitle());
        assertEquals("Created movie - testing theatre instance vars (rating)", 0, c.getRating());
        assertEquals("Created movie - testing director", "Ava DuVernay", c.getDirector());
        assertEquals("Created movie - next year as release date", d, c.getReleaseDate());
    }

    @Test
    public void test_Constructor_FarFutureReleaseDate() {
        testInterface();
        Date  d = getDate(2023, 12, 11);
        Movie c = new Movie("TestingMovie", 0, "Ava DuVernay", d);
        assertEquals("Created movie - testing theatre instance vars (title)", "TESTINGMOVIE", c.getTitle());
        assertEquals("Created movie - testing theatre instance vars (rating)", 0, c.getRating());
        assertEquals("Created movie - testing director", "Ava DuVernay", c.getDirector());
        assertEquals("Created movie - testing invalid date, year 2023 as release date", new Date().getTime(), c.getReleaseDate().getTime(), 100 * 60); // allow 6 second difference from now.
    }

    @Test
    public void test_CopyConstructor() {
        testInterface();
        Date  d = getDate(2017, 12, 11);
        Movie c = new Movie("TestingMovie", 0, "Director", d);
        assertEquals("Created movie - testing theatre instance vars (title)", "TESTINGMOVIE", c.getTitle());
        assertEquals("Created movie - testing theatre instance vars (rating)", 0, c.getRating());
        assertEquals("Created movie - testing director", "Director", c.getDirector());
        assertEquals("Created movie - testing date", d, c.getReleaseDate());
    }

    // getters and setters
    @Test
    public void test_getter_and_setter_releaseDate_lastYear() {
        testInterface();
        Date  d = getDate(2018, 1, 1);
        Movie c = new Movie("TestingMovie", 0, "Ava DuVernay", new Date());

        c.setReleaseDate(d);

        assertEquals("Created movie with today as release date then set to Jan 1, 2018", d, c.getReleaseDate());
    }

    @Test
    public void test_getter_and_setter_releaseDate_NearFutureReleaseDate() {
        testInterface();
        Date  d = getDate(2019, 12, 11);
        Movie c = new Movie("TestingMovie", 0, "Ava DuVernay", new Date());
        c.setReleaseDate(d);

        assertEquals("Created movie then set next year as release date", d, c.getReleaseDate());
    }

    @Test
    public void test_getter_and_setter_releaseDate_FarFutureReleaseDate() {
        testInterface();
        Date  d     = getDate(2023, 12, 11);
        Date  today = new Date();
        Movie c     = new Movie("TestingMovie", 0, "Ava DuVernay", today);
        c.setReleaseDate(d);

        assertEquals("Created movie with today as release date then set year 2023 as release date (expected today to remain as release date).", today, c.getReleaseDate());
    }

    @Test
    public void test_getter_and_setter_director() {
        testInterface();
        Date  today = new Date();
        Movie c     = new Movie("TestingMovie", 0, "Old Director", today);
        c.setDirector("Changed Director");
        assertEquals("Created movie then changed director.", "Changed Director", c.getDirector());
    }

    @Test
    public void test_getCategory_F() {
        testInterface();
        Date  today = new Date();
        Movie c     = new Movie("TestingCategoryF", 0, "Director", today);
        c.setRating(2);
        assertEquals("Set rating to 2", today.toString() + "-F", c.getCategory());
        c.setRating(0);
        assertEquals("Set rating to 0", today.toString() + "-F", c.getCategory());
        c.setRating(1);
        assertEquals("Set rating to 1", today.toString() + "-F", c.getCategory());
    }

    @Test
    public void test_getCategory_D() {
        testInterface();
        Date  today = new Date();
        Movie c     = new Movie("TestingCategoryF", 0, "Director", today);
        c.setRating(3);
        assertEquals("Set rating to 3", today.toString() + "-D", c.getCategory());
        c.setRating(4);
        assertEquals("Set rating to 4", today.toString() + "-D", c.getCategory());
    }

    @Test
    public void test_getCategory_C() {
        testInterface();
        Date  today = new Date();
        Movie c     = new Movie("TestingCategoryF", 0, "Director", today);
        c.setRating(5);
        assertEquals("Set rating to 5", today.toString() + "-C", c.getCategory());
        c.setRating(6);
        assertEquals("Set rating to 6", today.toString() + "-C", c.getCategory());
    }

    @Test
    public void test_getCategory_B() {
        testInterface();
        Date  today = new Date();
        Movie c     = new Movie("TestingCategoryF", 0, "Director", today);
        c.setRating(7);
        assertEquals("Set rating to 7", today.toString() + "-B", c.getCategory());
        c.setRating(8);
        assertEquals("Set rating to 8", today.toString() + "-B", c.getCategory());
    }

    @Test
    public void test_getCategory_A() {
        testInterface();
        Date  today = new Date();
        Movie c     = new Movie("TestingCategoryF", 0, "Director", today);
        c.setRating(9);
        assertEquals("Set rating to 9", today.toString() + "-A", c.getCategory());
        c.setRating(10);
        assertEquals("Set rating to 10", today.toString() + "-A", c.getCategory());
    }

}
