import org.junit.Test;

import static org.junit.Assert.*;

public class AudioBookTest extends FormatTester {
    public static final String CLASSNAME = "AudioBook";

    public AudioBookTest() {
        super(CLASSNAME, false);
    }


    private void testClassDefinition() {
        String[] instanceVars = {"int sizeInKB", "double playbackRatio"};
        assertTrue("Instance variables should be private with correct name and type.", instanceVariablesArePrivate(instanceVars));

        assertTrue("Class should not have the default constructor.", noDefaultConstructor());

        assertFalse("Should not override getTitle.", hasMethod("getTitle"));
        assertFalse("Should not override (or call) difficulty.", hasMethod("difficulty"));
        assertFalse("Should not override (or call) setTitle.", hasMethod("setTitle"));
        assertFalse("Should not override (or call) setNumberOfWords.", hasMethod("setNumberOfWords"));
        assertFalse("Should not override (or call) getNumberOfWords.", hasMethod("getNumberOfWords"));
        assertFalse("Should not override (or call) getNumPages", hasMethod("getNumPages"));
    }

    // Testing constructors
    @Test
    public void test_Constructor_sizeInKB_Zero_GoodNumber() {
        testClassDefinition();
        AudioBook c = new AudioBook("Black Beauty", 15000, 0);
        assertEquals("Created audio book with valid sizeInKB of 0", 0, c.getSizeInKB());
        assertEquals("Created audio book with title 'Black Beauty'", "Black Beauty", c.getTitle());
        assertEquals("Created audio book with 15000 words", 15000, c.getNumberOfWords());
        assertEquals("Expecting audio book to always have 1 pages", 1, c.getNumPages());
    }

    @Test
    public void test_Constructor_sizeInKB_LessThanZero_BadNumber() {
        testClassDefinition();
        AudioBook c = new AudioBook("Heidi", 10123, -1);
        assertEquals("Created book with an invalid size of -1", 0, c.getSizeInKB());
        assertEquals("Created audio book with title 'Heidi'", "Heidi", c.getTitle());
        assertEquals("Created audio book with 10123 words", 10123, c.getNumberOfWords());
        assertEquals("Expecting audio book to always have 1 pages", 1, c.getNumPages());
    }

    @Test
    public void test_Constructor_sizeInKB_GoodNumber() {
        testClassDefinition();
        AudioBook c = new AudioBook("Anne of Green Gables", 150000, 100);
        assertEquals("Created book with a valid size of 100", 100, c.getSizeInKB());
        assertEquals("Created audio book with title 'Anne of Green Gables'", "Anne of Green Gables", c.getTitle());
        assertEquals("Created audio book with 150000 words", 150000, c.getNumberOfWords());
        assertEquals("Expecting audio book to always have 1 page", 1, c.getNumPages());
    }

    @Test
    public void testCopyConstructor() {
        testClassDefinition();
        AudioBook c = new AudioBook("The Fifth Season", 80000, 1920000);
        c.setPlaybackRatio(1.4);
        AudioBook c1 = new AudioBook(c);
        assertEquals("Created book with title The Fifth Season", "The Fifth Season", c1.getTitle());
        assertEquals("Created book with numberOfWords 80000", 80000, c1.getNumberOfWords());
        assertEquals("Created book with sizeInKB 1920000", 1920000, c1.getSizeInKB());
        assertEquals("Created book with playbackRatio 1.4", 1.4, c1.getPlaybackRatio(), 0.000001);
    }

    @Test
    public void testCopyConstructor2() {
        testClassDefinition();
        AudioBook c = new AudioBook("I, Robot", 500000, 123456789);
        c.setPlaybackRatio(0.5);
        AudioBook c1 = new AudioBook(c);
        assertEquals("Created book with title I, Robot", "I, Robot", c1.getTitle());
        assertEquals("Created book with numberOfWords 500000", 500000, c1.getNumberOfWords());
        assertEquals("Created book with sizeInKB 123456789", 123456789, c1.getSizeInKB());
        assertEquals("Created book with playbackRatio 0.5", 0.5, c1.getPlaybackRatio(), 0.000001);
    }

    // test setters and getters

    @Test
    public void test_setter_playbackRatio_zero_BadNumber() {
        testClassDefinition();
        AudioBook c = new AudioBook("Heidi", 5000, 1920);
        c.setPlaybackRatio(0);
        assertEquals("Set to invalid playback ratio 0.0", 1.0, c.getPlaybackRatio(), 0.000001);
    }

    @Test
    public void test_setter_playbackRatio_negativeNumber() {
        testClassDefinition();
        AudioBook c = new AudioBook("Heidi", 5000, 1920);
        c.setPlaybackRatio(-0.01);
        assertEquals("Set to invalid playback ratio -.0.1", 1.0, c.getPlaybackRatio(), 0.000001);
    }

    @Test
    public void test_setter_playbackRatio_1_1() {
        testClassDefinition();
        AudioBook c = new AudioBook("Heidi", 5000, 1920);
        c.setPlaybackRatio(1.1);
        assertEquals("Set to valid playback ratio 1.1", 1.1, c.getPlaybackRatio(), 0.00001);
    }

    @Test
    public void test_setter_playbackRatio_verySmallValidNumber() {
        testClassDefinition();
        AudioBook c = new AudioBook("Heidi", 5000, 1920);
        c.setPlaybackRatio(0.0001);
        assertEquals("Set to valid playback ratio 0.0001", 0.0001, c.getPlaybackRatio(), 0.00001);
    }

    // test minutesToConsume
    @Test
    public void test_minutesToConsume_fastRatio() {
        testClassDefinition();
        AudioBook c = new AudioBook("The Steerswoman", 10000, 192000);
        c.setPlaybackRatio(1.25);
        assertEquals("size 192000, playback ratio 1.25 Minutes to Consume 80", 80, c.minutesToConsume());
    }

    @Test
    public void test_minutesToConsume_slowRatio() {
        testClassDefinition();
        AudioBook c = new AudioBook("The Steerswoman", 10000, 192000);
        c.setPlaybackRatio(0.25);
        assertEquals("size 192000, playback ratio 0.25", 400, c.minutesToConsume());
    }

    @Test
    public void test_difficulty_UsesOverridenMethod() {
        testClassDefinition();
        AudioBook b = new AudioBook("Testing Difficulty", 100, 50000);
        b.setPlaybackRatio(1.0);
        assertEquals("Audio book with size 50,000 and ratio 1.0, expected difficulty Easy", "Easy", b.difficulty());
    }

    @Test
    public void test_difficulty_UsesOverridenMethod2() {
        testClassDefinition();
        AudioBook b = new AudioBook("Testing Difficulty", 1000, 500000);
        b.setPlaybackRatio(0.5);
        assertEquals("Audio book with size 500,000 and ratio 0.5, expected difficulty Extra Challenge", "Extra Challenge", b.difficulty());
    }

}