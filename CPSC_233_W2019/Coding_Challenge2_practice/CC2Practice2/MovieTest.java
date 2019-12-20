import org.junit.Test;

import static org.junit.Assert.*;

public class MovieTest {

    // Testing constructors
    @Test
    public void test_Constructor_EmptyStringAnd0() {
        Movie c = new Movie("", 0);
        assertEquals("Created movie with empty title and rating 0 - testing title", "", c.getTitle());
        assertEquals("Created movie with empty title and rating 0 - testing rating", 0, c.getRating());
    }

    @Test
    public void test_Constructor_AllLowerCaseAnd10() {
        Movie c = new Movie("despicable me 3", 10);
        assertEquals("Created movie with 'despicable me 3' title and rating 10 - testing title", "DESPICABLE ME 3", c.getTitle());
        assertEquals("Created movie with 'despicable me 3' title and rating 10 - testing rating", 10, c.getRating());
    }

    @Test
    public void test_Constructor_AllUpperCaseAndNegative() {
        Movie c = new Movie("BIG HERO 6", -1);
        assertEquals("Created movie with 'BIG HERO 6' title and rating -1 - testing title", "BIG HERO 6", c.getTitle());
        assertEquals("Created movie with 'BIG HERO 6' title and rating -1 - testing rating", 0, c.getRating());
    }

    @Test
    public void test_Constructor_MixedCaseAndTooBig() {
        Movie c = new Movie("Hotel Transylvania", 11);
        assertEquals("Created movie with 'Hotel Transylvania' title and rating 11 - testing title", "HOTEL TRANSYLVANIA", c.getTitle());
        assertEquals("Created movie with 'Hotel Transylvania' title and rating 1 - testing rating", 0, c.getRating());
    }

    @Test
    public void test_CopyConstructor() {
        Movie c  = new Movie("TEST1", 6);
        Movie c2 = new Movie(c);
        assertEquals("Testing Copy Constructor, copying 'TEST1' title and rating 6 - testing title", "TEST1", c2.getTitle());
        assertEquals("Testing Copy Constructor, copying 'TEST2' Copy Constructor' title and rating 6 - testing rating", 6, c2.getRating());
    }

    @Test
    public void test_CopyConstructor2() {
        Movie c  = new Movie("Test2", 8);
        Movie c2 = new Movie(c);
        assertEquals("Testing Copy Constructor, copying 'Test2' title and rating 8 - testing title", "TEST2", c2.getTitle());
        assertEquals("Testing Copy Constructor, copying 'Test2' title and rating 8 - testing rating", 8, c2.getRating());
    }

    // Testing setter and getters

    @Test
    public void test_setter_and_getter_title_emptyString() {
        Movie c = new Movie("TestEmptyString", 1);
        c.setTitle("");
        assertEquals("Set title to empty string", "", c.getTitle());
    }

    @Test
    public void test_setter_and_getter_title_allLowerCase() {
        Movie c = new Movie("TestAllLowerCase", 2);
        c.setTitle("despicable me 3");
        assertEquals("Set title to 'despicable me 3'", "DESPICABLE ME 3", c.getTitle());
    }

    @Test
    public void test_setter_and_getter_title_allUpperCase() {
        Movie c = new Movie("TestAllUpperCase", 3);
        c.setTitle("BIG HERO 6");
        assertEquals("Set title to 'BIG HERO 6'", "BIG HERO 6", c.getTitle());
    }

    @Test
    public void test_setter_and_getter_title_MixedCase() {
        Movie c = new Movie("TestMixedCase", 4);
        c.setTitle("Hotel Transylvania");
        assertEquals("Set title to 'Hotel Transylvania'", "HOTEL TRANSYLVANIA", c.getTitle());
    }

    @Test
    public void test_setter_and_getter_rating_zero() {
        Movie c = new Movie("TestZeroRating", 5);
        c.setRating(0);
        assertEquals("Set rating to zero.", 0, c.getRating());
    }

    @Test
    public void test_setter_and_getter_rating_ten() {
        Movie c = new Movie("TestTenRating", 5);
        c.setRating(10);
        assertEquals("Set rating to ten.", 10, c.getRating());
    }

    @Test
    public void test_setter_and_getter_rating_negative() {
        Movie c = new Movie("TestNegativeRating", 3);
        c.setRating(-1);
        assertEquals("Set rating to a negative number that was initialized to 3.", 3, c.getRating());
    }

    @Test
    public void test_setter_and_getter_rating_tooBig() {
        Movie c = new Movie("TestTooBigRating", 5);
        c.setRating(11);
        assertEquals("Set rating to greater than 10 that was initialized to 5.", 5, c.getRating());
    }

    @Test
    public void test_getCategory_F() {
        Movie c = new Movie("TestingCategoryF", 0);
        c.setRating(2);
        assertEquals("Set rating to 2", 'F', c.getCategory());
        c.setRating(0);
        assertEquals("Set rating to 0", 'F', c.getCategory());
        c.setRating(1);
        assertEquals("Set rating to 1", 'F', c.getCategory());
    }

    @Test
    public void test_getCategory_D() {
        Movie c = new Movie("TestingCategoryD", 0);
        c.setRating(3);
        assertEquals("Set rating to 3", 'D', c.getCategory());
        c.setRating(4);
        assertEquals("Set rating to 4", 'D', c.getCategory());
    }

    @Test
    public void test_getCategory_C() {
        Movie c = new Movie("TestingCategoryC", 0);
        c.setRating(5);
        assertEquals("Set rating to 5", 'C', c.getCategory());
        c.setRating(6);
        assertEquals("Set rating to 6", 'C', c.getCategory());
    }

    @Test
    public void test_getCategory_B() {
        Movie c = new Movie("TestingCategoryB", 0);
        c.setRating(7);
        assertEquals("Set rating to 7", 'B', c.getCategory());
        c.setRating(8);
        assertEquals("Set rating to 8", 'B', c.getCategory());
    }

    @Test
    public void test_getCategory_A() {
        Movie c = new Movie("TestingCategoryA", 0);
        c.setRating(9);
        assertEquals("Set rating to 9", 'A', c.getCategory());
        c.setRating(10);
        assertEquals("Set rating to 10", 'A', c.getCategory());
    }
}
