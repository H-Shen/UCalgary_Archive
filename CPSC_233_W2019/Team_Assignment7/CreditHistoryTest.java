import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

/**
 * Junit test samples of class CreditHistory
 *
 * @author Group25
 * @version 2019-03-15
 */
public class CreditHistoryTest {

    /**
     * Test if any rating will be added by addRating(int) when all input is out of range.
     * <p>
     * Test setup: 5 random ratings from 6 to 99
     * Execution: addRating(int), getRatings().size()
     */
    @Test
    public void test_addRating_zero_ratings() {
        CreditHistory obj    = new CreditHistory();
        int           length = 5;
        for (int i = 0; i < length; ++i) {
            obj.addRating(ThreadLocalRandom.current().nextInt(6, 100));
        }
        assertEquals("Should return 0 rating.", 0, obj.getRatings().size());
    }

    /**
     * Test if any rating will be added by addRating(int) when all input is valid.
     * <p>
     * Test setup: 5 random ratings from -5 to 5
     * Execution: addRating(int), getRatings().size()
     */
    @Test
    public void test_addRating_five_ratings() {
        CreditHistory obj    = new CreditHistory();
        int           length = 5;
        for (int i = 0; i < length; ++i) {
            obj.addRating(ThreadLocalRandom.current().nextInt(-5, 6));
        }
        assertEquals("Should return " + length + " ratings.", length, obj.getRatings().size());
    }

    /**
     * Test if getRatings() will return an empty array when no input is given.
     * <p>
     * Test setup: empty input
     * Execution: getRatings().toArray()
     */
    @Test
    public void test_getRatings_zero_ratings() {
        CreditHistory obj    = new CreditHistory();
        Integer[]     expect = new Integer[0];
        assertArrayEquals("Should return an empty array.", expect, obj.getRatings().toArray());
    }

    /**
     * Test if getRatings() will return an array with size of 6 when 6 valid inputs are given.
     * <p>
     * Test setup: 6 inputs as an array
     * Execution: addRating(int), getRatings().size()
     */
    @Test
    public void test_getRatings_six_ratings() {
        CreditHistory obj    = new CreditHistory();
        Integer[]     expect = {-4, 3, 2, -4, 1, 5};
        for (Integer i : expect) {
            obj.addRating(i);
        }
        assertEquals("Should return an array whose length is 6.", expect.length, obj.getRatings().size());
    }

    /**
     * Test if getRatings() will return an array with size of 10 when more than 10 valid inputs are given.
     * <p>
     * Test setup: 50 inputs as an array
     * Execution: addRating(int), trimRatings(), getRatings().size()
     */
    @Test
    public void test_getRatings_ten_ratings() {
        CreditHistory obj    = new CreditHistory();
        int           length = 50;
        for (int i = 0; i < length; ++i) {
            obj.addRating(ThreadLocalRandom.current().nextInt(-5, 6));
        }
        obj.trimRatings();
        assertEquals("Should return an array whose length is 10 after trimming.", 10, obj.getRatings().size());
    }

    /**
     * Test if numOfRatings() will return 0 if no input is given before trimming.
     * <p>
     * Test setup: no input
     * Execution: trimRatings(), numOfRatings()
     */
    @Test
    public void test_trimRatings_zero_ratings() {
        CreditHistory obj = new CreditHistory();
        obj.trimRatings();
        assertEquals("Should return 0 rating after trimming.", 0, obj.numOfRatings());
    }

    /**
     * Test if numOfRatings() will return 6 if 6 invalid inputs are given after trimming.
     * <p>
     * Test setup: 6 valid inputs
     * Execution: addRating(i), trimRatings(), numOfRatings()
     */
    @Test
    public void test_trimRatings_five_ratings() {
        CreditHistory obj        = new CreditHistory();
        int[]         ratingList = {3, -4, 1, 3, 2, -5};
        for (int i : ratingList) {
            obj.addRating(i);
        }
        obj.trimRatings();
        assertEquals("Should return 6 ratings after trimming.", 6, obj.numOfRatings());
    }

    /**
     * Test if numOfRatings() will return 10 if 1000 valid inputs are given after trimming.
     * <p>
     * Test setup: 1000 valid inputs
     * Execution: addRating(i), trimRatings(), numOfRatings()
     */
    @Test
    public void test_trimRatings_ten_ratings() {
        CreditHistory obj = new CreditHistory();
        for (int i = 0; i < 1000; ++i) {
            obj.addRating(ThreadLocalRandom.current().nextInt(-5, 6));
        }
        obj.trimRatings();
        assertEquals("Should return 10 ratings after trimming.", 10, obj.numOfRatings());
    }

    /**
     * Test if numOfRatings() will return 0 no input is given without trimming.
     * <p>
     * Test setup: no input
     * Execution: obj.numOfRatings()
     */
    @Test
    public void test_numOfRatings_zero_ratings() {
        CreditHistory obj = new CreditHistory();
        assertEquals("Should return 0 rating.", 0, obj.numOfRatings());
    }

    /**
     * Test if numOfRatings() will return 6 if 6 valid inputs are given without trimming.
     * <p>
     * Test setup: 6 valid inputs
     * Execution: addRating(int), numOfRatings()
     */
    @Test
    public void test_numOfRatings_six_ratings() {
        CreditHistory obj        = new CreditHistory();
        int[]         ratingList = {3, 5, 1, 3, 5, 2};
        for (int i : ratingList) {
            obj.addRating(i);
        }
        assertEquals("Should return 6 ratings.", 6, obj.numOfRatings());
    }

    /**
     * Test if numOfRatings() will return 5 if 2000 invalid inputs and 5 valid inputs are given without trimming.
     * <p>
     * Test setup: 6 valid inputs
     * Execution: addRating(int), numOfRatings()
     */
    @Test
    public void test_numOfRatings_five_ratings() {
        CreditHistory obj = new CreditHistory();
        for (int i = 0; i < 1000; ++i) {
            obj.addRating(ThreadLocalRandom.current().nextInt(6, 100));
        }
        for (int i = 0; i < 5; ++i) {
            obj.addRating(ThreadLocalRandom.current().nextInt(-5, 6));
        }
        for (int i = 0; i < 1000; ++i) {
            obj.addRating(ThreadLocalRandom.current().nextInt(-100, -5));
        }
        assertEquals("Should return 5 ratings.", 5, obj.numOfRatings());
    }

    /**
     * Test if numOfRatings() will return 1000 if 1000 valid inputs are given without trimming.
     * <p>
     * Test setup: 1000 valid inputs
     * Execution: addRating(int), numOfRatings()
     */
    @Test
    public void test_numOfRatings_trimRatings_one_thousand_ratings() {
        CreditHistory obj = new CreditHistory();
        for (int i = 0; i < 1000; ++i) {
            obj.addRating(ThreadLocalRandom.current().nextInt(-5, 6));
        }
        assertEquals("Should return 1000 ratings.", 1000, obj.numOfRatings());
    }

    /**
     * Test if getCreditRating() will return expected value
     * <p>
     * Test setup: 3 valid inputs
     * Execution: addRating(int), getCreditRating()
     */
    @Test
    public void test_getCreditRating_3_ratings_0() {
        CreditHistory obj        = new CreditHistory();
        int[]         ratingList = {4, -1, 3};
        for (int i : ratingList) {
            obj.addRating(i);
        }
        assertEquals(" Should return 1.833.", 1.833, obj.getCreditRating(), 0.001);
    }

    /**
     * Test if getCreditRating() will return expected value
     * <p>
     * Test setup: 3 valid inputs
     * Execution: addRating(int), getCreditRating()
     */
    @Test
    public void test_getCreditRating_3_ratings_1() {
        CreditHistory obj        = new CreditHistory();
        int[]         ratingList = {-2, 4, 2};
        for (int i : ratingList) {
            obj.addRating(i);
        }
        assertEquals(" Should return 2.000.", 2.0000, obj.getCreditRating(), 0.0001);
    }

    /**
     * Test if getCreditRating() will return expected value
     * <p>
     * Test setup: 9 valid inputs
     * Execution: addRating(int), getCreditRating()
     */
    @Test
    public void test_getCreditRating_9_ratings_0() {
        CreditHistory obj        = new CreditHistory();
        int[]         ratingList = {-5, 5, 0, 5, 3, 0, 0, -5, 5};
        for (int i : ratingList) {
            obj.addRating(i);
        }
        assertEquals(" Should return 1.0000.", 1.0000, obj.getCreditRating(), 0.0001);
    }

    /**
     * Test if getCreditRating() will return expected value
     * <p>
     * Test setup: 9 valid inputs
     * Execution: addRating(int), getCreditRating()
     */
    @Test
    public void test_getCreditRating_9_ratings_1() {
        CreditHistory obj        = new CreditHistory();
        int[]         ratingList = {4, 3, 0, -1, -4, 5, 5, 3, 3};
        for (int i : ratingList) {
            obj.addRating(i);
        }
        assertEquals(" Should return 2.267.", 2.267, obj.getCreditRating(), 0.001);
    }

    /**
     * Test if getCreditRating() will return expected value
     * <p>
     * Test setup: 0 inputs
     * Execution: addRating(int), getCreditRating()
     */
    @Test
    public void test_getCreditRating_return_NaN_for_0_rating() {
        CreditHistory obj   = new CreditHistory();
        boolean       isNaN = Double.isNaN(obj.getCreditRating());
        assertTrue("Should return NaN.", isNaN);
    }

    /**
     * Test if getCreditRating() will return expected value after trimming
     * <p>
     * Test setup: 50 inputs
     * Execution: addRating(int), trimRatings(), getCreditRating()
     */
    @Test
    public void test_getCreditRating_50_ratings() {
        CreditHistory obj = new CreditHistory();
        int[] ratingList = {1, 0, 2, 1, 3, -3, 1, 3, -5, -5, 0, -2, -4, 3, -3, -3, -4, 0, 0, -5, 3, -2, 1, 2, -4, 2,
                -2, -3, -2, 3, -1, 3, -4, -4, -1, 0, 3, 3, 4, -5, -2, -5, -2, 3, 4, -3, 3, 4, -5, -2};
        for (int i : ratingList) {
            obj.addRating(i);
        }
        obj.trimRatings();
        assertEquals("Should return -0.32.", obj.getCreditRating(), -0.32, 0.0001);
    }
}