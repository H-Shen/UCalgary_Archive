import org.junit.Test;

import java.io.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class GuessingGameTest {
    String filename = "GuessingGame.java";
    // Needed when testing code that reads/writes to console.
    private PrintStream standardOut = System.out;
    private PrintStream testingOut  = System.out;
    private InputStream standardIn = System.in;
    private InputStream testingIn  = System.in;

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

    private void createInputFile(String[] lines, String filename) {
        try {
            PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
            for (String line : lines) {
                output.println(line);
            }
            output.close();
        } catch (IOException ioe) {
            fail("Problem in test environment.  Unable to create required test file " + filename + ".");
        }
    }

    private String[] readLinesFromFile(String filename) {
        try {
            BufferedReader    input = new BufferedReader(new FileReader(filename));
            String            line  = input.readLine();
            ArrayList<String> lines = new ArrayList<String>();
            while (line != null) {
                lines.add(line);
                line = input.readLine();
            }
            return lines.toArray(new String[lines.size()]);
        } catch (FileNotFoundException fnfe) {
            fail("Problem in test environment.  Unable to open file with program output " + filename + ".");
            return null;
        } catch (IOException ioe) {
            fail("Problem in test environment.  Unable to read file with program output " + filename + ".");
            return null;
        }
    }

    private void testSetup(String inputFilename, String outputFilename) {
        try {
            testingIn = new FileInputStream(inputFilename);
            testingOut = new PrintStream(outputFilename);
            System.setIn(testingIn);
            System.setOut(testingOut);
        } catch (FileNotFoundException fnfe) {
            fail("Problem in test environment.  Unable to open file with test input " + inputFilename + ".");
        }
    }

    private void testCleanup() {
        try {
            testingIn.close();
        } catch (IOException ioe) {
            // don't care about this one.  If I can't close input for test it likely won't affect test.");
        }

        testingOut.close();

        System.setIn(standardIn);
        System.setOut(standardOut);
        testingIn = standardIn;
        testingOut = standardOut;
    }


    @Test
    public void test_guessOneTooLow() {
        String[] lines = {"19"};
        createInputFile(lines, "testIn1.txt");
        testSetup("testIn1.txt", "testOut1.txt");
        boolean actual          = GuessingGame.getAndCheckGuess(20);
        boolean expected        = false;
        String  expectedMessage = "Too low";
        testCleanup();

        lines = readLinesFromFile("testOut1.txt");
        if (lines == null || lines.length != 1) {
            fail("The program did not produce the correct output. There should be a single prompt followed by exactly one message as output after entering a guess.  "
                    + "(The prompt should not end with a new line.)");
        }
        if (!lines[0].contains(":")) {
            fail("The program did not produce the correct output. There was no colon (':') which is required after the prompt.");
        }

        String actualMessage = lines[0].split(":")[1];
        actualMessage = actualMessage.trim();
        assertEquals("Testing message. The number to guess was 20 and guess provided was 19.", expectedMessage, actualMessage);
        assertEquals("Testing return value.  The number to guess was 20 and guess provided was 19.", expected, actual);
    }

    @Test
    public void test_guessMuchTooLow() {
        String[] lines = {"1"};
        createInputFile(lines, "testIn2.txt");
        testSetup("testIn2.txt", "testOut2.txt");
        boolean actual          = GuessingGame.getAndCheckGuess(11);
        boolean expected        = false;
        String  expectedMessage = "Too low";
        testCleanup();

        lines = readLinesFromFile("testOut2.txt");
        if (lines == null || lines.length != 1) {
            fail("The program did not produce the correct output. There should be a single prompt followed by exactly one message as output after entering a guess.  "
                    + "(The prompt should not end with a new line.)");
        }
        if (!lines[0].contains(":")) {
            fail("The program did not produce the correct output. There was no colon (':') which is required after the prompt.");
        }

        String actualMessage = lines[0].split(":")[1];
        actualMessage = actualMessage.trim();
        assertEquals("Testing message.  The number to guess was 11 and guess provided was 1.", expectedMessage, actualMessage);
        assertEquals("Testing return value.  The number to guess was 11 and guess provided was 1.", expected, actual);
    }

    @Test
    public void test_guessOneTooHigh() {
        String[] lines = {"12"};
        createInputFile(lines, "testIn3.txt");
        testSetup("testIn3.txt", "testOut3.txt");
        boolean actual          = GuessingGame.getAndCheckGuess(11);
        boolean expected        = false;
        String  expectedMessage = "Too high";
        testCleanup();

        lines = readLinesFromFile("testOut3.txt");
        if (lines == null || lines.length != 1) {
            fail("The program did not produce the correct output. There should be a single prompt followed by exactly one message as output after entering a guess.  "
                    + "(The prompt should not end with a new line.)");
        }
        if (!lines[0].contains(":")) {
            fail("The program did not produce the correct output. There was no colon (':') which is required after the prompt.");
        }

        String actualMessage = lines[0].split(":")[1];
        actualMessage = actualMessage.trim();
        assertEquals("Testing message. The number to guess was 11 and guess provided was 12.", expectedMessage, actualMessage);
        assertEquals("Testing return value. The number to guess was 11 and guess provided was 12.", expected, actual);
    }

    @Test
    public void test_guessMuchTooHigh() {
        String[] lines = {"19"};
        createInputFile(lines, "testIn4.txt");
        testSetup("testIn4.txt", "testOut4.txt");
        boolean actual          = GuessingGame.getAndCheckGuess(1);
        boolean expected        = false;
        String  expectedMessage = "Too high";
        testCleanup();

        lines = readLinesFromFile("testOut4.txt");
        if (lines == null || lines.length != 1) {
            fail("The program did not produce the correct output. There should be a single prompt followed by exactly one message as output after entering a guess.  "
                    + "(The prompt should not end with a new line.)");
        }
        if (!lines[0].contains(":")) {
            fail("The program did not produce the correct output. There was no colon (':') which is required after the prompt.");
        }

        String actualMessage = lines[0].split(":")[1];
        actualMessage = actualMessage.trim();
        assertEquals("Testing message. The number to guess was 1 and guess provided was 19.", expectedMessage, actualMessage);
        assertEquals("Testing return value.  The number to guess was 1 and guess provided was 19.", expected, actual);
    }

    @Test
    public void test_guessOf1Correct() {
        String[] lines = {"1"};
        createInputFile(lines, "testIn5.txt");
        testSetup("testIn5.txt", "testOut5.txt");
        boolean actual          = GuessingGame.getAndCheckGuess(1);
        boolean expected        = true;
        String  expectedMessage = "You guessed it";
        testCleanup();

        lines = readLinesFromFile("testOut5.txt");
        if (lines == null || lines.length != 1) {
            fail("The program did not produce the correct output. There should be a single prompt followed by exactly one message as output after entering a guess.  "
                    + "(The prompt should not end with a new line.)");
        }
        if (!lines[0].contains(":")) {
            fail("The program did not produce the correct output. There was no colon (':') which is required after the prompt.");
        }

        String actualMessage = lines[0].split(":")[1];
        actualMessage = actualMessage.trim();
        assertEquals("Testing message. The number to guess was 1 and guess provided was 1.", expectedMessage, actualMessage);
        assertEquals("Testing return value. The number to guess was 1 and guess provided was 1.", expected, actual);
    }

    @Test
    public void test_guessOf20Correct() {
        String[] lines = {"20"};
        createInputFile(lines, "testIn6.txt");
        testSetup("testIn6.txt", "testOut6.txt");
        boolean actual          = GuessingGame.getAndCheckGuess(20);
        boolean expected        = true;
        String  expectedMessage = "You guessed it";
        testCleanup();

        lines = readLinesFromFile("testOut6.txt");
        if (lines == null || lines.length != 1) {
            fail("The program did not produce the correct output. There should be a single prompt followed by exactly one message as output after entering a guess.  "
                    + "(The prompt should not end with a new line.)");
        }
        if (!lines[0].contains(":")) {
            fail("The program did not produce the correct output. There was no colon (':') which is required after the prompt.");
        }

        String actualMessage = lines[0].split(":")[1];
        actualMessage = actualMessage.trim();
        assertEquals("Testing message. The number to guess was 20 and guess provided was 20.", expectedMessage, actualMessage);
        assertEquals("Testing return value. The number to guess was 20 and guess provided was 20.", expected, actual);
    }

    @Test
    public void test_guessOf11Correct() {
        String[] lines = {"11"};
        createInputFile(lines, "testIn7.txt");
        testSetup("testIn7.txt", "testOut7.txt");
        boolean actual          = GuessingGame.getAndCheckGuess(11);
        boolean expected        = true;
        String  expectedMessage = "You guessed it";
        testCleanup();

        lines = readLinesFromFile("testOut7.txt");
        if (lines == null || lines.length != 1) {
            fail("The program did not produce the correct output. There should be a single prompt followed by exactly one message as output after entering a guess.  "
                    + "(The prompt should not end with a new line.)");
        }
        if (!lines[0].contains(":")) {
            fail("The program did not produce the correct output. There was no colon (':') which is required after the prompt.");
        }

        String actualMessage = lines[0].split(":")[1];
        actualMessage = actualMessage.trim();
        assertEquals("Testing message. The number to guess was 11 and guess provided was 11.", expectedMessage, actualMessage);
        assertEquals("Testing return value. The number to guess was 11 and guess provided was 11.", expected, actual);
    }

    @Test
    public void test_InvalidGuess0() {
        String[] lines = {"0"};
        createInputFile(lines, "testIn8.txt");
        testSetup("testIn8.txt", "testOut8.txt");
        boolean actual          = GuessingGame.getAndCheckGuess(11);
        boolean expected        = false;
        String  expectedMessage = "Guess is not valid";
        testCleanup();

        lines = readLinesFromFile("testOut8.txt");
        if (lines == null || lines.length != 1) {
            fail("The program did not produce the correct output. There should be a single prompt followed by exactly one message as output after entering a guess.  "
                    + "(The prompt should not end with a new line.)");
        }
        if (!lines[0].contains(":")) {
            fail("The program did not produce the correct output. There was no colon (':') which is required after the prompt.");
        }

        String actualMessage = lines[0].split(":")[1];
        actualMessage = actualMessage.trim();
        assertEquals("Testing message. The number to guess was 11 and guess provided was 0.", expectedMessage, actualMessage);
        assertEquals("Testing return value. The number to guess was 11 and guess provided was 0.", expected, actual);
    }

    @Test
    public void test_InvalidGuess21() {
        String[] lines = {"21"};
        createInputFile(lines, "testIn9.txt");
        testSetup("testIn9.txt", "testOut9.txt");
        boolean actual          = GuessingGame.getAndCheckGuess(20);
        boolean expected        = false;
        String  expectedMessage = "Guess is not valid";
        testCleanup();

        lines = readLinesFromFile("testOut9.txt");
        if (lines == null || lines.length != 1) {
            fail("The program did not produce the correct output. There should be a single prompt followed by exactly one message as output after entering a guess.  "
                    + "(The prompt should not end with a new line.)");
        }
        if (!lines[0].contains(":")) {
            fail("The program did not produce the correct output. There was no colon (':') which is required after the prompt.");
        }

        String actualMessage = lines[0].split(":")[1];
        actualMessage = actualMessage.trim();
        assertEquals("Testing message. The number to guess was 20 and guess provided was 21.", expectedMessage, actualMessage);
        assertEquals("Testing return value. The number to guess was 20 and guess provided was 21.", expected, actual);
    }

}
