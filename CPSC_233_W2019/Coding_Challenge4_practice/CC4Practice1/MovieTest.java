import org.junit.Test;

import static org.junit.Assert.*;

public class MovieTest {

    public static final String CLASSNAME = "Movie";
    public static final String FILENAME  = CLASSNAME + ".java";


    // checks if rating is the only private thing in the class.
    private boolean instanceVariablesArePrivate() {
        boolean ratingIsOnlyPrivate = true;
        boolean ratingIsPrivate     = false;


        try {
            BufferedReader in   = new BufferedReader(new FileReader(FILENAME));
            String         line = in.readLine();
            while (line != null) {
                if (line.contains("private")) {
                    line = line.trim();
                    if (line.length() >= 18) {
                        line = line.substring(0, 18);
                        if (line.equals("private int rating")) {
                            ratingIsPrivate = true;
                        } else {
                            ratingIsOnlyPrivate = false;
                        }
                    } else {
                        ratingIsOnlyPrivate = false;
                    }
                }
                line = in.readLine();
            }
            in.close();
        } catch (FileNotFoundException e) {
            ratingIsPrivate = false;
        } catch (IOException e) {
            ratingIsPrivate = false;
        }
        return ratingIsPrivate && ratingIsOnlyPrivate;
    }

    private boolean hasMethod(String signature) {
        boolean contains = false;

        try {
            BufferedReader in   = new BufferedReader(new FileReader(FILENAME));
            String         line = in.readLine();
            while (line != null) {
                if (line.contains(signature)) {
                    contains = true;
                }
                line = in.readLine();
            }
            in.close();
        } catch (FileNotFoundException e) {
            contains = false;
        } catch (IOException e) {
            contains = false;
        }
        return contains;

    }

    private boolean toStrInvokesParentToStr() {
        boolean callsGetter = false;
        boolean callsParent = false;


        try {
            BufferedReader in   = new BufferedReader(new FileReader(FILENAME));
            String         line = in.readLine();
            while (line != null) {
                if (line.contains("toString")) {
                    // This should be start of toString method
                    while (!line.contains("}")) {
                        line = in.readLine();
                        if (line.contains("getTitle") || line.contains("getLength")) {
                            callsGetter = true;
                        }
                        if (line.contains("super.toString()")) {
                            callsParent = true;
                        }
                    }
                }
                line = in.readLine();
            }
            in.close();
        } catch (FileNotFoundException e) {
            callsParent = false;
        } catch (IOException e) {
            callsParent = false;
        }
        return callsParent && !callsGetter;
    }

    private void testInterface() {
        assertTrue("Should only have one instance variable (rating).", instanceVariablesArePrivate());
        assertTrue("Should override toString and it should invoke parent toString (not getter methods in parent).", toStrInvokesParentToStr());
        assertFalse("Should not override getCategory.", hasMethod("char getCategory"));
        assertFalse("Should not override (or call) getTitle.", hasMethod("getTitle"));
        assertFalse("Should not override (or call) getLength.", hasMethod("getLength"));
        assertFalse("Should not override (or call) setTitle.", hasMethod("setTitle"));
        assertFalse("Should not override (or call) setLength.", hasMethod("setLength"));

    }


    // Testing constructors
    @Test
    public void test_Constructor_RatingMin0() {
        testInterface();
        Movie c = new Movie("", 1, 0);
        assertEquals("Created movie with empty title, length 1 and rating 0 - testing title", "", c.getTitle());
        assertEquals("Created movie with empty title, length 1 and rating 0 - testing length", 1, c.getLength());
        assertEquals("Created movie with empty title, length 1 and rating 0 - testing rating", 0, c.getRating());
    }

    @Test
    public void test_Constructor_RatingMax10() {
        testInterface();
        Movie c = new Movie("despicable me 3", 200, 10);
        assertEquals("Created movie with 'despicable me 3' title, length 200 and rating 10 - testing title", "DESPICABLE ME 3", c.getTitle());
        assertEquals("Created movie with 'despicable me 3' title, length 200 and rating 10 - testing length", 200, c.getLength());
        assertEquals("Created movie with 'despicable me 3' title, length 200 and rating 10 - testing rating", 10, c.getRating());
    }

    @Test
    public void test_Constructor_NegativeRating() {
        testInterface();
        Movie c = new Movie("BIG HERO 6", 12, -1);
        assertEquals("Created movie with 'BIG HERO 6' title, length 12 and rating -1 - testing title", "BIG HERO 6", c.getTitle());
        assertEquals("Created movie with 'BIG HERO 6' title, length 12 and rating -1 - testing length", 12, c.getLength());
        assertEquals("Created movie with 'BIG HERO 6' title, length 12 and rating -1 - testing rating", 0, c.getRating());
    }

    @Test
    public void test_Constructor_TooBigRating() {
        testInterface();
        Movie c = new Movie("Hotel Transylvania", 41, 11);
        assertEquals("Created movie with 'Hotel Transylvania' title, length 41 and rating 11 - testing title", "HOTEL TRANSYLVANIA", c.getTitle());
        assertEquals("Created movie with 'Hotel Transylvania' title, length 41 and rating 11 - testing length", 41, c.getLength());
        assertEquals("Created movie with 'Hotel Transylvania' title, length 41 and rating 11 - testing rating", 0, c.getRating());
    }

    @Test
    public void test_CopyConstructor() {
        testInterface();
        Movie c  = new Movie("TEST1", 145, 6);
        Movie c2 = new Movie(c);
        assertEquals("Testing Copy Constructor, copying 'TEST1' title, length 145 and rating 6 - testing title", "TEST1", c2.getTitle());
        assertEquals("Testing Copy Constructor, copying 'TEST1' title, length 145 and rating 6 - testing length", 145, c2.getLength());
        assertEquals("Testing Copy Constructor, copying 'TEST1' title, length 145 and rating 6 - testing rating", 6, c2.getRating());
    }

    // Testing setter and getters


    @Test
    public void test_setter_and_getter_rating_zero() {
        testInterface();
        Movie c = new Movie("TestZeroRating", 11, 5);
        c.setRating(0);
        assertEquals("Set rating to zero.", 0, c.getRating());
    }

    @Test
    public void test_setter_and_getter_rating_ten() {
        testInterface();
        Movie c = new Movie("TestTenRating", 11, 5);
        c.setRating(10);
        assertEquals("Set rating to ten.", 10, c.getRating());
    }

    @Test
    public void test_setter_and_getter_rating_negative() {
        testInterface();
        Movie c = new Movie("TestNegativeRating", 11, 3);
        c.setRating(-1);
        assertEquals("Set rating to a negative number that was initialized to 3.", 3, c.getRating());
    }

    @Test
    public void test_setter_and_getter_rating_tooBig() {
        testInterface();
        Movie c = new Movie("TestTooBigRating", 11, 5);
        c.setRating(11);
        assertEquals("Set rating to greater than 10 that was initialized to 5.", 5, c.getRating());
    }

    @Test
    public void test_toString() {
        testInterface();
        Movie c = new Movie("TESTING_TO_STRING", 20, 5);
        assertEquals("title: TESTING_TO_STRING, rating 5 and length 20", "Title: TESTING_TO_STRING Length: 20 Rating: 5", c.toString());
    }

}
