import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.Assert.*;

public class RecursionExercisesTest {

    //Check the submission file for the words "for" and "while"
    public void testCheckWords() {
        Scanner scan = null;
        try {
            scan = new Scanner(new FileInputStream("RecursionExercises.java"));
        } catch (FileNotFoundException e) {
            fail("RecursionExercise.java not found");
        }

        while (scan.hasNext()) {
            String line = scan.nextLine();

            if (line.contains("for") || line.contains("while")) {
                fail("Found word \"for\" or \"while\": " + line);
            }
        }

        scan.close();
    }

    // Testing factorial

    @Test
    public void test_factorial_negative() {
        testCheckWords();
        RecursionExercises se = new RecursionExercises();
        assertEquals("Testing factorial of negative number", 0, se.factorial(-1));
        assertEquals("Testing factorial of negative number", 0, se.factorial(-2));
        assertEquals("Testing factorial of negative number", 0, se.factorial(-5));
        assertEquals("Testing factorial of negative number", 0, se.factorial(-10));
    }

    @Test
    public void test_factorial_zero() {
        testCheckWords();
        RecursionExercises se = new RecursionExercises();
        assertEquals("Testing factorial of zero", 1, se.factorial(0));
    }

    @Test
    public void test_factorial_one() {
        testCheckWords();
        RecursionExercises se = new RecursionExercises();
        assertEquals("Testing factorial of one", 1, se.factorial(1));
    }

    @Test
    public void test_factorial_ten() {
        testCheckWords();
        RecursionExercises se = new RecursionExercises();
        assertEquals("Testing factorial of ten", 3628800, se.factorial(10));
    }

    // Testing sum1

    @Test
    public void test_sum1_negative() {
        testCheckWords();
        RecursionExercises se = new RecursionExercises();
        assertEquals("Testing summing of negative number", 0, se.sum1(-1));
        assertEquals("Testing summing of negative number", 0, se.sum1(-2));
        assertEquals("Testing summing of negative number", 0, se.sum1(-5));
        assertEquals("Testing summing of negative number", 0, se.sum1(-10));
    }

    @Test
    public void test_sum1_zero() {
        testCheckWords();
        RecursionExercises se = new RecursionExercises();
        assertEquals("Testing summing of zero", 0, se.sum1(0));
    }

    @Test
    public void test_sum1_one() {
        testCheckWords();
        RecursionExercises se = new RecursionExercises();
        assertEquals("Testing summing numbers from zero to one that are not divisible by 3", 1, se.sum1(1));
    }

    @Test
    public void test_sum1_three() {
        testCheckWords();
        RecursionExercises se = new RecursionExercises();
        assertEquals("Testing summing numbers from zero to three that are not divisible by 3", 3, se.sum1(3));
    }

    @Test
    public void test_sum1_ten() {
        testCheckWords();
        RecursionExercises se = new RecursionExercises();
        assertEquals("Testing summing non-divisible by 3 numbers from zero to ten", 37, se.sum1(10));
    }


    //Testing sum2

    @Test
    public void test_sum2_ZeroTo9() {
        testCheckWords();
        RecursionExercises se   = new RecursionExercises();
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }

        assertEquals("Testing sum2 for {0, 1, ..., 9}.", 27, se.sum2(list));

    }

    @Test
    public void test_sum2_OnlyNotDivisibleBy3() {
        testCheckWords();
        RecursionExercises se   = new RecursionExercises();
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i <= 100; i++) {
            list.add(i * 3 + 1);
            list.add(i * 3 + 2);
        }

        assertEquals("Testing sum2 for {1,2,4,5,7,8,10,11, ...,  301,302}.", 30603, se.sum2(list));

    }

    @Test
    public void test_sum2_OnlyDivisibleBy3() {
        testCheckWords();
        RecursionExercises se   = new RecursionExercises();
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 75; i++) {
            list.add(i * 3);
        }

        assertEquals("Testing sum2 for {0,3,6,9,...,225}.", 0, se.sum2(list));

    }

    @Test
    public void test_sum2_EmptyList() {
        //Test empty lsit
        testCheckWords();
        RecursionExercises se   = new RecursionExercises();
        ArrayList<Integer> list = new ArrayList<Integer>();


        assertEquals("Testing sum2 for {}.", 0, se.sum2(list));

    }

    @Test
    public void test_sum2_NullList() {
        testCheckWords();
        RecursionExercises se = new RecursionExercises();

        assertEquals("Testing sum2 for null list.", 0, se.sum2(null));

    }

    // testing duplicateVowels
    @Test
    public void test_duplicateVowels_NullString() {
        testCheckWords();
        RecursionExercises se = new RecursionExercises();

        assertEquals("Testing duplicateVowels for null string.", null, se.duplicateVowels(null));

    }

    @Test
    public void test_duplicateVowels_EmptyString() {
        testCheckWords();
        RecursionExercises se = new RecursionExercises();

        assertEquals("Testing duplicateVowels for empty string.", "", se.duplicateVowels(""));

    }

    @Test
    public void test_duplicateVowels_OnlyVowels() {
        testCheckWords();
        RecursionExercises se = new RecursionExercises();

        assertEquals("Testing duplicateVowels for string with only vowels.", "aaeeiiuuaaeeiiyyoooo", se.duplicateVowels("aeiuaeiyoo"));

    }

    @Test
    public void test_duplicateVowels_NoVowels() {
        testCheckWords();
        RecursionExercises se = new RecursionExercises();

        assertEquals("Testing duplicateVowels for string with no vowels.", "sqwrtplkjhgfdszxcvbnm", se.duplicateVowels("sqwrtplkjhgfdszxcvbnm"));

    }

    @Test
    public void test_duplicateVowels_VowelsAndConsonants() {
        testCheckWords();
        RecursionExercises se = new RecursionExercises();

        assertEquals("Testing duplicateVowels for string with vowels and consonants.", "IIs iis thiis AA striing wiith vooweels?", se.duplicateVowels("Is is this A string with vowels?"));
    }


}
