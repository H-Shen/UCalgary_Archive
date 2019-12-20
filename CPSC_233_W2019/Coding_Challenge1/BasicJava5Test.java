import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;

public class BasicJava5Test {
    String filename = "BasicJava5.java";

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
    public void test_isDigit_0() {
        assertFalse("Don't use the Character class in your code.", usesLibrary("Character"));

        boolean expected = true;
        boolean actual   = BasicJava5.isDigit('0');

        assertEquals("Checking if 0 is a digit", expected, actual);
    }

    @Test
    public void test_isDigit_1() {
        assertFalse("Don't use the Character class in your code.", usesLibrary("Character"));

        boolean expected = true;
        boolean actual   = BasicJava5.isDigit('1');

        assertEquals("Checking if 1 is a digit", expected, actual);
    }

    @Test
    public void test_isDigit_9() {
        assertFalse("Don't use the Character class in your code.", usesLibrary("Character"));

        boolean expected = true;
        boolean actual   = BasicJava5.isDigit('9');

        assertEquals("Checking if 9 is a digit", expected, actual);
    }

    @Test
    public void test_isDigit_$() {
        assertFalse("Don't use the Character class in your code.", usesLibrary("Character"));

        boolean expected = false;
        boolean actual   = BasicJava5.isDigit('$');

        assertEquals("Checking if # is a digit", expected, actual);
    }

    @Test
    public void test_isDigit_a() {
        assertFalse("Don't use the Character class in your code.", usesLibrary("Character"));

        boolean expected = false;
        boolean actual   = BasicJava5.isDigit('a');

        assertEquals("Checking if a is a digit", expected, actual);
    }


    @Test
    public void test_computePolynomial_0_0_RoundDownNegativeNumber() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));

        int expected = -34;
        int actual   = BasicJava5.computePolynomial(0.0);

        assertEquals("Value of polynomial at 0.0", expected, actual);
    }

    @Test
    public void test_computePolynomial_5_717798_noRounding() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));

        int expected = 0;
        int actual   = BasicJava5.computePolynomial(5.717798);
        assertEquals("Value of polynomial at 5.717798", expected, actual);
    }

    @Test
    public void test_computePolynomial_SmallNegativeNum_roundUpPositiveNum() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));

        int expected = 381027;
        int actual   = BasicJava5.computePolynomial(-876.0);

        assertEquals("Value of polynomial at -876.0", expected, actual);
    }

    @Test
    public void test_computePolynomial_LargeNum_RoundDown() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));

        int expected = 48792343;
        int actual   = BasicJava5.computePolynomial(9875.5);

        assertEquals("Value of polynomial at 9876.0", expected, actual);
    }

    @Test
    public void test_countOnes_0() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));
        assertFalse("Don't use the Integer library or the word Integer in your code.", usesLibrary("Math"));
        assertFalse("Don't use the StringBuilder library or the word StringBuilder in your code.", usesLibrary("StringBuilder"));


        int expected = 0;
        int actual   = BasicJava5.countOnes(0);

        assertEquals("testing 0", expected, actual);
    }

    @Test
    public void test_countOnes_AllOnes() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));
        assertFalse("Don't use the Integer library or the word Integer in your code.", usesLibrary("Math"));
        assertFalse("Don't use the StringBuilder library or the word StringBuilder in your code.", usesLibrary("StringBuilder"));


        int expected = 8;
        int actual   = BasicJava5.countOnes(0b11111111);

        assertEquals("testing 1111 1111 (256)", expected, actual);
    }

    @Test
    public void test_countOnes_FirstAndLastOnly() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));
        assertFalse("Don't use the Integer library or the word Integer in your code.", usesLibrary("Math"));
        assertFalse("Don't use the StringBuilder library or the word StringBuilder in your code.", usesLibrary("StringBuilder"));


        int expected = 2;
        int actual   = BasicJava5.countOnes(0b100000000001);

        assertEquals("testing 1000 0000 0000 0001 (32.769)", expected, actual);
    }

    @Test
    public void test_countOnes_NegativeNumber() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));
        assertFalse("Don't use the Integer library or the word Integer in your code.", usesLibrary("Math"));
        assertFalse("Don't use the StringBuilder library or the word StringBuilder in your code.", usesLibrary("StringBuilder"));

        int expected = 3;
        int actual   = BasicJava5.countOnes(-0b1011);

        assertEquals("testing negative 1011 (-11)", expected, actual);
    }

    @Test
    public void test_charsUnusedInString_bothEmptyString() {
        boolean expected = true;
        boolean actual   = BasicJava5.charsUnusedInString("", "");

        assertEquals("both empty string", expected, actual);
    }

    @Test
    public void test_charsUnusedInString_firstEmptyString() {
        boolean expected = true;
        boolean actual   = BasicJava5.charsUnusedInString("", "abcdefghijklmnopqrstuvwxyz");

        assertEquals("first is empty string", expected, actual);
    }

    @Test
    public void test_charsUnusedInString_NoneUsed() {
        boolean expected = true;
        boolean actual   = BasicJava5.charsUnusedInString("This is a test", "xbc");

        assertEquals("'This is a test', 'xbc'", expected, actual);
    }

    @Test
    public void test_charsUnusedInString_oneUsed() {
        boolean expected = false;
        boolean actual   = BasicJava5.charsUnusedInString("This is a test", "bipqn");

        assertEquals("'This is a test', 'bipqn'", expected, actual);
    }

    @Test
    public void test_charsUnusedInString_upperAndLowerCase() {
        boolean expected = false;
        boolean actual   = BasicJava5.charsUnusedInString("Want to do another test", "wstpq");

        assertEquals("'Want to do another test', 'wstpq'", expected, actual);
    }


}
