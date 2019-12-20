import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;

public class BasicJavaP2Test {
    String filename = "BasicJavaP2.java";

    private boolean containsImportStatement() {
        boolean containsImport = false;

        try {
            BufferedReader in   = new BufferedReader(new FileReader(filename));
            String         line = in.readLine();
            while (line != null && !containsImport) {
                if (line.matches("import+\\s.*")) {
                    containsImport = true;
                }
                line = in.readLine();
            }
            in.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return containsImport;
    }

    /**
     * Checks if the specified library is used anywhere in the code tested.  It checks
     * for the word exactly.  If there is a variable name that contains the library name,
     * this will result in a false positive.
     */
    private boolean usesLibrary(String libraryName) {
        boolean usesLibrary = false;

        try {
            BufferedReader in   = new BufferedReader(new FileReader(filename));
            String         line = in.readLine();
            while (line != null && !usesLibrary) {
                if (line.contains(libraryName)) {
                    usesLibrary = true;
                }
                line = in.readLine();
            }
            in.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return usesLibrary;
    }

    /**
     * Checks if the coding construct is used in the class we're testing.  It expects the
     * construct to be preceded and followed by white space or parenthesis.
     */
    private boolean usesConstruct(String constructName) {
        boolean usesConstruct = false;

        try {
            BufferedReader in   = new BufferedReader(new FileReader(filename));
            String         line = in.readLine();
            while (line != null && !usesConstruct) {
                if (line.matches(".*\\s+if+[\\s+,(].*")) {
                    usesConstruct = true;
                }
                line = in.readLine();
            }
            in.close();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return usesConstruct;
    }

    @Test
    public void test_isUpper_A() {
        assertFalse("Don't use the Character class in your code.", usesLibrary("Character"));

        boolean expected = true;
        boolean actual   = BasicJavaP2.isUpper('A');

        assertEquals("Checking if A is upper case letter", expected, actual);
    }

    @Test
    public void test_isUpper_Z() {
        assertFalse("Don't use the Character class in your code.", usesLibrary("Character"));

        boolean expected = true;
        boolean actual   = BasicJavaP2.isUpper('Z');

        assertEquals("Checking if Z is upper case letter", expected, actual);
    }

    @Test
    public void test_isUpper_F() {
        assertFalse("Don't use the Character class in your code.", usesLibrary("Character"));

        boolean expected = true;
        boolean actual   = BasicJavaP2.isUpper('F');

        assertEquals("Checking if F is upper case letter", expected, actual);
    }

    @Test
    public void test_isUpper_a() {
        assertFalse("Don't use the Character class in your code.", usesLibrary("Character"));

        boolean expected = false;
        boolean actual   = BasicJavaP2.isUpper('a');

        assertEquals("Checking if a is upper case letter", expected, actual);
    }

    @Test
    public void test_isUpper_7() {
        assertFalse("Don't use the Character class in your code.", usesLibrary("Character"));

        boolean expected = false;
        boolean actual   = BasicJavaP2.isUpper('7');

        assertEquals("Checking if 7 is upper case letter", expected, actual);
    }


    @Test
    public void test_computePolynomial_0_0() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));

        double expected = 28;
        double actual   = BasicJavaP2.computePolynomial(0.0);

        assertEquals("Value of polynomial at 0.0", expected, actual, 0.00001);
    }

    @Test
    public void test_computePolynomial_100_15() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));

        double expected = 9857.7225;
        double actual   = BasicJavaP2.computePolynomial(100.15);

        assertEquals("Value of polynomial at 100.15", expected, actual, 0.00001);
    }

    @Test
    public void test_computePolynomial_negativeNum() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));

        double expected = 31.990756;
        double actual   = BasicJavaP2.computePolynomial(-1.234);

        assertEquals("Value of polynomial at -1.234", expected, actual, 0.00001);
    }

    @Test
    public void test_computePolynomial_SmallNegativeNum() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));

        double expected = 97555156.0;
        double actual   = BasicJavaP2.computePolynomial(-9876.0);

        assertEquals("Value of polynomial at -9876.0", expected, actual, 0.00001);
    }

    @Test
    public void test_computePolynomial_LargeNum() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));

        double expected = 97515652.0;
        double actual   = BasicJavaP2.computePolynomial(9876.0);

        assertEquals("Value of polynomial at 9876.0", expected, actual, 0.00001);
    }

    @Test
    public void test_floorAfterMult_MultResultsInWholeNum() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));

        long expected = 6;
        long actual   = BasicJavaP2.floorAfterMult(2, 3.0);

        assertEquals("Value of floor(2 * 3.0)", expected, actual);
    }

    @Test
    public void test_floorAfterMult_MultResultsInNegativeNum() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));

        long expected = -6;
        long actual   = BasicJavaP2.floorAfterMult(2, -3.0);

        assertEquals("Value of floor(2 * -3.0)", expected, actual);
    }

    @Test
    public void test_floorAfterMult_MultResultsInDecimal() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));

        long expected = 6;
        long actual   = BasicJavaP2.floorAfterMult(2, 3.25);

        assertEquals("Value of floor(2 * 3.25)", expected, actual);
    }

    @Test
    public void test_floorAfterMult_checkIfCastAfterMult() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));

        long expected = 31;
        long actual   = BasicJavaP2.floorAfterMult(5, 6.2);

        assertEquals("Value of floor(5 * 6.2)", expected, actual);
    }

    @Test
    public void test_floorAfterMult_checkIfCastAfterMultLargeDiff() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));

        long expected = 34;
        long actual   = BasicJavaP2.floorAfterMult(5, 6.999);

        assertEquals("Value of floor(5 * 6.999)", expected, actual);
    }

    @Test
    public void test_containsAllChars_bothEmptyString() {
        boolean expected = true;
        boolean actual   = BasicJavaP2.containsAllChars("", "");

        assertEquals("Testing countainsAllChars - both empty string", expected, actual);
    }

    @Test
    public void test_containsAllChars_firstIsEmptyString() {
        boolean expected = false;
        boolean actual   = BasicJavaP2.containsAllChars("", "a");

        assertEquals("Testing countainsAllChars - empty string, non-empty string", expected, actual);
    }

    @Test
    public void test_containsAllChars_secondEmptyString() {
        boolean expected = true;
        boolean actual   = BasicJavaP2.containsAllChars(" ", "");

        assertEquals("Testing countainsAllChars - non-empty string, empty string", expected, actual);
    }

    @Test
    public void test_containsAllChars_containsAll() {
        boolean expected = true;
        boolean actual   = BasicJavaP2.containsAllChars("This is a test", "ti sh");

        assertEquals("Testing countainsAllChars - 'This is a test', 'ti sh'", expected, actual);
    }

    @Test
    public void test_containsAllChars_containsNone() {
        boolean expected = false;
        boolean actual   = BasicJavaP2.containsAllChars("Another string for testing", "c/tmkl");

        assertEquals("Testing countainsAllChars - 'Another string for testing', 'c/tmkl'", expected, actual);
    }

    @Test
    public void test_containsAllChars_containsSome() {
        boolean expected = false;
        boolean actual   = BasicJavaP2.containsAllChars("This is a test", "ispa");

        assertEquals("Testing countainsAllChars - 'This is a test', 'ispa'", expected, actual);
    }


}
