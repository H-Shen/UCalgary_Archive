import org.junit.Test;

import static org.junit.Assert.*;

public class TheatreTest extends FormatTester {

    public static final String CLASSNAME = "Theatre";

    public TheatreTest() {
        super(CLASSNAME, false);
    }

    private void testInterface() {
        String[] instanceVars = {"String title", "int rating"};
        assertTrue("Instance variables should be private with correct name and type.", instanceVariablesArePrivate(instanceVars));

        assertTrue("Class should not have the default constructor.", noDefaultConstructor());
    }


    // Testing constructors
    @Test
    public void test_Constructor_EmptyStringAnd0() {
        testInterface();
        Theatre c = new Theatre("", 0);
        assertEquals("Created movie with empty title and rating 0 - testing title", "", c.getTitle());
        assertEquals("Created movie with empty title and rating 0 - testing rating", 0, c.getRating());
    }

    @Test
    public void test_Constructor_AllLowerCaseAnd10() {
        testInterface();
        Theatre c = new Theatre("despicable me 3", 10);
        assertEquals("Created movie with 'despicable me 3' title and rating 10 - testing title", "DESPICABLE ME 3", c.getTitle());
        assertEquals("Created movie with 'despicable me 3' title and rating 10 - testing rating", 10, c.getRating());
    }

    @Test
    public void test_Constructor_AllUpperCaseAndNegative() {
        testInterface();
        Theatre c = new Theatre("BIG HERO 6", -1);
        assertEquals("Created movie with 'BIG HERO 6' title and rating -1 - testing title", "BIG HERO 6", c.getTitle());
        assertEquals("Created movie with 'BIG HERO 6' title and rating -1 - testing rating", 0, c.getRating());
    }

    @Test
    public void test_Constructor_MixedCaseAndTooBig() {
        testInterface();
        Theatre c = new Theatre("Hotel Transylvania", 11);
        assertEquals("Created movie with 'Hotel Transylvania' title and rating 11 - testing title", "HOTEL TRANSYLVANIA", c.getTitle());
        assertEquals("Created movie with 'Hotel Transylvania' title and rating 1 - testing rating", 0, c.getRating());
    }

    @Test
    public void test_CopyConstructor() {
        testInterface();
        Theatre c  = new Theatre("TEST1", 6);
        Theatre c2 = new Theatre(c);
        assertEquals("Testing Copy Constructor, copying 'TEST1' title and rating 6 - testing title", "TEST1", c2.getTitle());
        assertEquals("Testing Copy Constructor, copying 'TEST2' Copy Constructor' title and rating 6 - testing rating", 6, c2.getRating());
    }

    @Test
    public void test_CopyConstructor2() {
        testInterface();
        Theatre c  = new Theatre("Test2", 8);
        Theatre c2 = new Theatre(c);
        assertEquals("Testing Copy Constructor, copying 'Test2' title and rating 8 - testing title", "TEST2", c2.getTitle());
        assertEquals("Testing Copy Constructor, copying 'Test2' title and rating 8 - testing rating", 8, c2.getRating());
    }

    // Testing setter and getters

    @Test
    public void test_setter_and_getter_title_emptyString() {
        testInterface();
        Theatre c = new Theatre("TestEmptyString", 1);
        c.setTitle("");
        assertEquals("Set title to empty string", "", c.getTitle());
    }

    @Test
    public void test_setter_and_getter_title_allLowerCase() {
        testInterface();
        Theatre c = new Theatre("TestAllLowerCase", 2);
        c.setTitle("despicable me 3");
        assertEquals("Set title to 'despicable me 3'", "DESPICABLE ME 3", c.getTitle());
    }

    @Test
    public void test_setter_and_getter_title_allUpperCase() {
        testInterface();
        Theatre c = new Theatre("TestAllUpperCase", 3);
        c.setTitle("BIG HERO 6");
        assertEquals("Set title to 'BIG HERO 6'", "BIG HERO 6", c.getTitle());
    }

    @Test
    public void test_setter_and_getter_title_MixedCase() {
        testInterface();
        Theatre c = new Theatre("TestMixedCase", 4);
        c.setTitle("Hotel Transylvania");
        assertEquals("Set title to 'Hotel Transylvania'", "HOTEL TRANSYLVANIA", c.getTitle());
    }

    @Test
    public void test_setter_and_getter_rating_zero() {
        testInterface();
        Theatre c = new Theatre("TestZeroRating", 5);
        c.setRating(0);
        assertEquals("Set rating to zero.", 0, c.getRating());
    }

    @Test
    public void test_setter_and_getter_rating_ten() {
        testInterface();
        Theatre c = new Theatre("TestTenRating", 5);
        c.setRating(10);
        assertEquals("Set rating to ten.", 10, c.getRating());
    }

    @Test
    public void test_setter_and_getter_rating_negative() {
        testInterface();
        Theatre c = new Theatre("TestNegativeRating", 3);
        c.setRating(-1);
        assertEquals("Set rating to a negative number that was initialized to 3.", 3, c.getRating());
    }

    @Test
    public void test_setter_and_getter_rating_tooBig() {
        testInterface();
        Theatre c = new Theatre("TestTooBigRating", 5);
        c.setRating(11);
        assertEquals("Set rating to greater than 10 that was initialized to 5.", 5, c.getRating());
    }

    @Test
    public void test_getCategory_F() {
        testInterface();
        Theatre c = new Theatre("TestingCategoryF", 0);
        c.setRating(2);
        assertEquals("Set rating to 2", "F", c.getCategory());
        c.setRating(0);
        assertEquals("Set rating to 0", "F", c.getCategory());
        c.setRating(1);
        assertEquals("Set rating to 1", "F", c.getCategory());
    }

    @Test
    public void test_getCategory_D() {
        testInterface();
        Theatre c = new Theatre("TestingCategoryD", 0);
        c.setRating(3);
        assertEquals("Set rating to 3", "D", c.getCategory());
        c.setRating(4);
        assertEquals("Set rating to 4", "D", c.getCategory());
    }

    @Test
    public void test_getCategory_C() {
        testInterface();
        Theatre c = new Theatre("TestingCategoryC", 0);
        c.setRating(5);
        assertEquals("Set rating to 5", "C", c.getCategory());
        c.setRating(6);
        assertEquals("Set rating to 6", "C", c.getCategory());
    }

    @Test
    public void test_getCategory_B() {
        testInterface();
        Theatre c = new Theatre("TestingCategoryB", 0);
        c.setRating(7);
        assertEquals("Set rating to 7", "B", c.getCategory());
        c.setRating(8);
        assertEquals("Set rating to 8", "B", c.getCategory());
    }

    @Test
    public void test_getCategory_A() {
        testInterface();
        Theatre c = new Theatre("TestingCategoryA", 0);
        c.setRating(9);
        assertEquals("Set rating to 9", "A", c.getCategory());
        c.setRating(10);
        assertEquals("Set rating to 10", "A", c.getCategory());
    }
}


