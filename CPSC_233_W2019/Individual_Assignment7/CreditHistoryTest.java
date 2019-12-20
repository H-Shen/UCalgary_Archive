import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

/**
 * Junit test samples of class CreditHistory
 *
 * @author Haohu Shen
 * @version 2019-03-10
 */
public class CreditHistoryTest {

    @Test
    public void test_addRating_zero_ratings() {
        CreditHistory obj    = new CreditHistory();
        int           length = 5;
        for (int i = 0; i < length; ++i) {
            obj.addRating(ThreadLocalRandom.current().nextInt(6, 100));
        }
        assertEquals("Should return 0 rating.", 0, obj.getRatings().size());
    }

    @Test
    public void test_addRating_five_ratings() {
        CreditHistory obj    = new CreditHistory();
        int           length = 5;
        for (int i = 0; i < length; ++i) {
            obj.addRating(ThreadLocalRandom.current().nextInt(-5, 6));
        }
        assertEquals("Should return " + length + " ratings.", length, obj.getRatings().size());
    }

    @Test
    public void test_getRatings_zero_ratings() {
        CreditHistory obj    = new CreditHistory();
        Integer[]     expect = new Integer[0];
        assertArrayEquals("Should return an empty array.", expect, obj.getRatings().toArray());
    }

    @Test
    public void test_getRatings_six_ratings() {
        CreditHistory obj    = new CreditHistory();
        Integer[]     expect = {-4, 3, 2, -4, 1, 5};
        for (Integer i : expect) {
            obj.addRating(i);
        }
        assertEquals("Should return an array whose length is 6.", expect.length, obj.getRatings().size());
    }

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

    @Test
    public void test_trimRatings_zero_ratings() {
        CreditHistory obj = new CreditHistory();
        obj.trimRatings();
        assertEquals("Should return 0 rating after trimming.", 0, obj.numOfRatings());
    }

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

    @Test
    public void test_trimRatings_ten_ratings() {
        CreditHistory obj = new CreditHistory();
        for (int i = 0; i < 1000; ++i) {
            obj.addRating(ThreadLocalRandom.current().nextInt(-5, 6));
        }
        obj.trimRatings();
        assertEquals("Should return 10 ratings after trimming.", 10, obj.numOfRatings());
    }

    @Test
    public void test_numOfRatings_zero_ratings() {
        CreditHistory obj = new CreditHistory();
        assertEquals("Should return 0 rating.", 0, obj.numOfRatings());
    }

    @Test
    public void test_numOfRatings_six_ratings() {
        CreditHistory obj        = new CreditHistory();
        int[]         ratingList = {3, 5, 1, 3, 5, 2};
        for (int i : ratingList) {
            obj.addRating(i);
        }
        assertEquals("Should return 6 ratings.", 6, obj.numOfRatings());
    }

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

    @Test
    public void test_numOfRatings_trimRatings_one_thousand_ratings() {
        CreditHistory obj = new CreditHistory();
        for (int i = 0; i < 1000; ++i) {
            obj.addRating(ThreadLocalRandom.current().nextInt(-5, 6));
        }
        assertEquals("Should return 1000 ratings.", 1000, obj.numOfRatings());
    }
}