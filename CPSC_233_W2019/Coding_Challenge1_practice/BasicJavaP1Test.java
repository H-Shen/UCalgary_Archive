import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;

public class BasicJavaP1Test {
    String filename = "BasicJavaP1.java";

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
    public void test_floor_3_5() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));

        long expected = 3;
        long actual   = BasicJavaP1.floor(3.5);

        assertEquals("Floor of 3.5", expected, actual);
    }

    @Test
    public void test_floor_0_01() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));

        long expected = 0;
        long actual   = BasicJavaP1.floor(0.01);

        assertEquals("Floor of 0.01", expected, actual);
    }

    @Test
    public void test_conversion_32() {
        double expected = 0;
        double actual   = BasicJavaP1.conversion(32.0);

        assertEquals("Converting from 32 F", expected, actual, 0.0001);
    }

    @Test
    public void test_conversion_0() {
        double expected = -17.77778;
        double actual   = BasicJavaP1.conversion(0.0);

        assertEquals("Converting from 0 F", expected, actual, 0.0001);
    }

    @Test
    public void test_conversion_96() {
        double expected = 35.55556;
        double actual   = BasicJavaP1.conversion(96.0);

        assertEquals("Converting from 96 F", expected, actual, 0.0001);
    }

    @Test
    public void test_conversion_69_8() {
        double expected = 21.0;
        double actual   = BasicJavaP1.conversion(69.8);

        assertEquals("Converting from 69.8 F", expected, actual, 0.0001);
    }

    @Test
    public void test_conversion_minus20() {
        double expected = -28.8889;
        double actual   = BasicJavaP1.conversion(-20.0);

        assertEquals("Converting from -20.0 F", expected, actual, 0.0001);
    }

    @Test
    public void test_willRoundUp_0() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));

        boolean expected = false;
        boolean actual   = BasicJavaP1.willRoundUp(0.0);

        assertEquals("0.0 does not round up", expected, actual);
    }

    @Test
    public void test_willRoundUp_0_5() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));

        boolean expected = true;
        boolean actual   = BasicJavaP1.willRoundUp(0.5);

        assertEquals("Testing 0.5", expected, actual);
    }

    @Test
    public void test_willRoundUp_0_4999999() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));

        boolean expected = false;
        boolean actual   = BasicJavaP1.willRoundUp(0.4999999);

        assertEquals("testing 0.4999999", expected, actual);
    }

    @Test
    public void test_willRoundUp_1_0() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));

        boolean expected = false;
        boolean actual   = BasicJavaP1.willRoundUp(1.0);

        assertEquals("testing 1.0", expected, actual);
    }

    @Test
    public void test_willRoundUp_23_674() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));

        boolean expected = true;
        boolean actual   = BasicJavaP1.willRoundUp(23.674);

        assertEquals("testing 23.674", expected, actual);
    }

    @Test
    public void test_addDigits_12345() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));
        assertFalse("Don't use the Integer library or the word Integer in your code.", usesLibrary("Math"));
        assertFalse("Don't use the StringBuilder library or the word StringBuilder in your code.", usesLibrary("StringBuilder"));

        int expected = 15;
        int actual   = BasicJavaP1.addDigits(12345);

        assertEquals("testing 12345", expected, actual);
    }

    @Test
    public void test_addDigits_0() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));
        assertFalse("Don't use the Integer library or the word Integer in your code.", usesLibrary("Math"));
        assertFalse("Don't use the StringBuilder library or the word StringBuilder in your code.", usesLibrary("StringBuilder"));
        int expected = 0;
        int actual   = BasicJavaP1.addDigits(0);

        assertEquals("testing 0", expected, actual);
    }

    @Test
    public void test_addDigits_98765() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));
        assertFalse("Don't use the Integer library or the word Integer in your code.", usesLibrary("Math"));
        assertFalse("Don't use the StringBuilder library or the word StringBuilder in your code.", usesLibrary("StringBuilder"));
        int expected = 35;
        int actual   = BasicJavaP1.addDigits(98765);

        assertEquals("testing 98765", expected, actual);
    }

    @Test
    public void test_addDigits_10() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));
        assertFalse("Don't use the Integer library or the word Integer in your code.", usesLibrary("Math"));
        assertFalse("Don't use the StringBuilder library or the word StringBuilder in your code.", usesLibrary("StringBuilder"));
        int expected = 1;
        int actual   = BasicJavaP1.addDigits(10);

        assertEquals("testing 10", expected, actual);
    }

    @Test
    public void test_addDigits_911111() {
        assertFalse("Don't use the Math library or the word Math in your code.", usesLibrary("Math"));
        assertFalse("Don't use the Integer library or the word Integer in your code.", usesLibrary("Math"));
        assertFalse("Don't use the StringBuilder library or the word StringBuilder in your code.", usesLibrary("StringBuilder"));
        int expected = 14;
        int actual   = BasicJavaP1.addDigits(911111);

        assertEquals("testing 911111", expected, actual);
    }

    @Test
    public void test_sumRange_5_10() {
        int expected = 35;
        int actual   = BasicJavaP1.sumRange(5, 10);

        assertEquals("testing summing from 5 to 10", expected, actual);
    }

    @Test
    public void test_sumRange_0_1() {
        int expected = 0;
        int actual   = BasicJavaP1.sumRange(0, 1);

        assertEquals("testing summing from 0 to 1", expected, actual);
    }

    @Test
    public void test_sumRange_minus2_6() {
        int expected = 12;
        int actual   = BasicJavaP1.sumRange(-2, 6);

        assertEquals("testing summing from -2 to 6", expected, actual);
    }

    @Test
    public void test_sumRange_5_1() {
        int expected = 14;
        int actual   = BasicJavaP1.sumRange(5, 1);

        assertEquals("testing summing from 5 downto 1 (include 5, exclude 1)", expected, actual);
    }

    @Test
    public void test_countChar_emptyString() {
        int expected = 0;
        int actual   = BasicJavaP1.countChar("", 'a');

        assertEquals("Testing how many times 'a' appears in empty string", expected, actual);
    }

    @Test
    public void test_countChar_StringWithSpaces() {
        int expected = 3;
        int actual   = BasicJavaP1.countChar("   ", ' ');

        assertEquals("Testing how many times spaces appear in '   '", expected, actual);
    }

    @Test
    public void test_countChar_StringWithMultipleChars() {
        int expected = 3;
        int actual   = BasicJavaP1.countChar("this is a test", 't');

        assertEquals("Testing how many times 't' appear in 'this is a test'", expected, actual);
    }


}
