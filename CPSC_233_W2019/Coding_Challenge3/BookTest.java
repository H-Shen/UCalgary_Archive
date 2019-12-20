import org.junit.Test;

import static org.junit.Assert.*;

public class BookTest extends FormatTester {

    public static final String CLASSNAME = "Book";

    public BookTest() {
        super(CLASSNAME, false);
    }


    private void testClassDefinition() {
        String[] instanceVars = {"String title", "int numberOfWords", "int numPages"};
        assertTrue("Instance variables should be private with correct name and type.", instanceVariablesArePrivate(instanceVars));

        assertTrue("Class should not have the default constructor.", noDefaultConstructor());

    }

    // Testing constructors
    @Test
    public void test_Constructor_numberOfPages_valid() {
        testClassDefinition();
        Book c = new Book("Black Beauty", 0, 1);
        assertEquals("Created book with valid one page", 1, c.getNumPages());
        assertEquals("Created book with title 'Black Beauty'", "Black Beauty", c.getTitle());
        c = new Book("Black Beauty", 0, 500000);
        assertEquals("Created book with valid 500,000 pages", 500000, c.getNumPages());
    }

    @Test
    public void test_Constructor_numberOfPages_invalid() {
        testClassDefinition();
        Book c = new Book("Heidi", 0, -1);
        assertEquals("Created book with an invalid number of pages, expected default of 1 page", 1, c.getNumPages());
        assertEquals("Created book with title 'Heidi'", "Heidi", c.getTitle());
    }

    @Test
    public void test_Constructor_numberOfWords_invalid() {
        testClassDefinition();
        Book c = new Book("Harry Potter", -1, 15);
        assertEquals("Created book with an invalid number of words, expected default of 0 words", 0, c.getNumberOfWords());
        assertEquals("Created book with title 'Harry Potter'", "Harry Potter", c.getTitle());
        c = new Book("Harry Potter", 500001, 15);
        assertEquals("Created book with an invalid 500001 number of words, expected default of 0 words", 0, c.getNumberOfWords());
    }

    @Test
    public void test_Constructor_numberOfWords_valid() {
        testClassDefinition();
        Book c = new Book("Heidi", 0, 15);
        assertEquals("Created book with an valid 0 number of words", 0, c.getNumberOfWords());
        c = new Book("Heidi", 500000, 15);
        assertEquals("Created book with an valid 500,000 number of words", 500000, c.getNumberOfWords());
        c = new Book("Heidi", 123456, 15);
        assertEquals("Created book with an valid 123,456 number of words", 123456, c.getNumberOfWords());
    }

    @Test
    public void testCopyConstructor() {
        testClassDefinition();
        Book c  = new Book("The Fifth Season", 80000, 100);
        Book c1 = new Book(c);
        assertEquals("Created book with title The Fifth Season", "The Fifth Season", c1.getTitle());
        assertEquals("Created book with numberOfWords 80000", 80000, c1.getNumberOfWords());
        assertEquals("Created book with numPages 100", 100, c1.getNumPages());
    }

    @Test
    public void testCopyConstructor2() {
        testClassDefinition();
        Book c  = new Book("Weapons of Math Destruction", 400000, 200);
        Book c1 = new Book(c);
        assertEquals("Created book with title Weapons of Math Destruction", "Weapons of Math Destruction", c1.getTitle());
        assertEquals("Created book with numberOfWords 400000", 400000, c1.getNumberOfWords());
        assertEquals("Created book with numPages 200", 200, c1.getNumPages());
    }

    @Test
    public void test_getter_and_setter_numPages_zero_BadNumber() {
        testClassDefinition();
        Book c = new Book("Heidi", 5000, 50);
        c.setNumPages(0);
        assertEquals("Created book with 50 pages, then set to 0 pages.", 1, c.getNumPages());
    }

    // test setters and getters

    @Test
    public void test_getter_and_setter_numPages_one_GoodNumber() {
        testClassDefinition();
        Book c = new Book("Heidi", 5000, 50);
        c.setNumPages(1);
        assertEquals("Created book with a valid number of pages", 1, c.getNumPages());
    }

    @Test
    public void test_getter_and_setter_numPages_many_GoodNumber() {
        testClassDefinition();
        Book c = new Book("Heidi", 5000, 1);
        c.setNumPages(10);
        assertEquals("Created book with a valid number of pages", 10, c.getNumPages());
    }

    @Test
    public void test_getter_and_setter_numberOfWords_negative() {
        testClassDefinition();
        Book c = new Book("Heidi", 5000, 1);
        c.setNumberOfWords(-1);
        assertEquals("Tried to change number of words from 5000 to -1, which is invalid", 5000, c.getNumberOfWords());
    }

    @Test
    public void test_getter_and_setter_numberOfWords_tooLarge() {
        testClassDefinition();
        Book c = new Book("Heidi", 4000, 1);
        c.setNumberOfWords(500001);
        assertEquals("Tried to change number of words from 4000 to 500,001, which is invalid", 4000, c.getNumberOfWords());
    }

    @Test
    public void test_getter_and_setter_numberOfWords_0() {
        testClassDefinition();
        Book c = new Book("Heidi", 5000, 1);
        c.setNumberOfWords(0);
        assertEquals("Tried to change number of words from 5000 to 0", 0, c.getNumberOfWords());
    }

    @Test
    public void test_getter_and_setter_numberOfWords_500000() {
        testClassDefinition();
        Book c = new Book("Heidi", 5000, 1);
        c.setNumberOfWords(500000);
        assertEquals("Tried to change number of words from 5000 to 500,000", 500000, c.getNumberOfWords());
    }

    @Test
    public void test_getter_and_setter_title_emptyString() {
        testClassDefinition();
        Book c = new Book("Heidi", 5000, 1);
        c.setTitle("Pippi Longstockings");
        assertEquals("Tried to change title from 'Heidi' to 'Pippi Longstockings'", "Pippi Longstockings", c.getTitle());
    }

    // test minutesToConsume
    @Test
    public void test_minutesToConsume_smallestRatio() {
        testClassDefinition();
        Book c = new Book("The Steerswoman", 9500, 1);
        c.setNumPages(500);
        assertEquals("Ratio: 9500/500", 250, c.minutesToConsume());
        c.setNumPages(9500);
        assertEquals("Ratio: 9500/9500", 4750, c.minutesToConsume());
        c.setNumPages(700);
        assertEquals("Ratio: 9500/700", 350, c.minutesToConsume());
    }

    @Test
    public void test_minutesToConsume_ratioInSecondInterval() {
        testClassDefinition();
        Book c = new Book("The Steerswoman", 9500, 1);
        c.setNumPages(475);
        assertEquals("Ratio: 9500/499", 475, c.minutesToConsume());
        c.setNumberOfWords(396);
        c.setNumPages(4);
        assertEquals("Ratio: 396/4", 4, c.minutesToConsume());
    }

    @Test
    public void test_minutesToConsume_ratioInThirdInterval() {
        testClassDefinition();
        Book c = new Book("The Steerswoman", 100000, 1);
        c.setNumPages(1000);
        assertEquals("Ratio: 400000/1000", 2000, c.minutesToConsume());
        c.setNumberOfWords(498);
        c.setNumPages(2);
        assertEquals("Ratio: 498/2", 4, c.minutesToConsume());
    }

    @Test
    public void test_minutesToConsume_largestRatio() {
        testClassDefinition();
        Book c = new Book("The Steerswoman", 500, 1);
        c.setNumPages(2);
        assertEquals("Ratio: 500/2", 8, c.minutesToConsume());
        c.setNumberOfWords(500000);
        c.setNumPages(100);
        assertEquals("Ratio: 500000", 400, c.minutesToConsume());
    }

    // test toString
    @Test
    public void test_toString() {
        testClassDefinition();

        Book c = new Book("Neverwhere", 75000, 100);
        assertEquals("Neverwhere, 75000 words, 100 pages", "Title: Neverwhere Difficulty: Extra Challenge Pages: 100", c.toString());
    }

    @Test
    public void test_toString2() {
        testClassDefinition();

        Book c = new Book("Peter Pan", 400, 10);
        assertEquals("Peter Pan, 400 words, 10 pages", "Title: Peter Pan Difficulty: Easy Pages: 10", c.toString());
    }

    // Test difficulty
    @Test
    public void test_difficulty_easy() {
        testClassDefinition();
        MockBook b = new MockBook();
        b.next = 1;
        assertEquals("minutes to consume is 4, expected difficulty Easy", "Easy", b.difficulty());
        b.next = 29;
        assertEquals("minutes to consume is 29, expected difficulty Easy", "Easy", b.difficulty());
    }

    @Test
    public void test_difficulty_moderate() {
        testClassDefinition();
        MockBook b = new MockBook();
        b.next = 30;
        assertEquals("minutes to consume is 30", "Moderate", b.difficulty());
        b.next = 119;
        assertEquals("minutes to consume is 119", "Moderate", b.difficulty());
    }

    @Test
    public void test_difficulty_hard() {
        testClassDefinition();
        MockBook b = new MockBook();
        b.next = 120;
        assertEquals("minutes to consume is 120", "Hard", b.difficulty());
        b.next = 239;
        assertEquals("minutes to consume is 239", "Hard", b.difficulty());
    }

    @Test
    public void test_difficulty_extra_challenge() {
        testClassDefinition();
        MockBook b = new MockBook();
        b.next = 240;
        assertEquals("minutes to consume is 240", "Extra Challenge", b.difficulty());
        b.next = 500;
        assertEquals("minutes to consume is 500", "Extra Challenge", b.difficulty());
        b.next = 12345676;
        assertEquals("minutes to consume is 12345676", "Extra Challenge", b.difficulty());
    }

    // to facilitate testing of difficulty
    private class MockBook extends Book {
        int next = 0;

        public MockBook() {
            super("", 1, 1);
        }

        public int minutesToConsume() {
            return next;
        }
    }
}