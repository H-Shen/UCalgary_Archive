import org.junit.Test;

import java.util.Random;
import java.util.Scanner;

import static org.junit.Assert.*;

public class FileExercisesTest {

    private String getRandomFilename() {
        return "test" + (new Random()).nextInt(100000);
    }

    private String createFile(String text) {
        String filename = getRandomFilename() + ".txt";
        try {
            PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
            output.print(text);
            output.close();
        } catch (IOException ioe) {
            fail("Unable to set up test environment, tried to (re)create file " + filename);
        }
        return filename;
    }

    private String createFile(double[] nums) {
        String filename = getRandomFilename() + ".bin";
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(filename));
            out.writeInt(nums.length);
            for (int counter = 0; counter < nums.length; counter++) {
                out.writeDouble(nums[counter]);
            }
            out.close();
        } catch (IOException ioe) {
            fail("Unable to set up test environment, tried to create file " + filename);
        }
        return filename;
    }

    private String createFile(int[] nums) {
        String filename = getRandomFilename() + ".bin";
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(filename));
            out.writeInt(nums.length);
            for (int counter = 0; counter < nums.length; counter++) {
                out.writeInt(nums[counter]);
            }
            out.close();
        } catch (IOException ioe) {
            fail("Unable to set up test environment, tried to create file " + filename);
        }
        return filename;
    }

    //Test with valid files

    @Test
    public void test_checkMonth_Zero() {
        try {
            FileExercises f1 = new FileExercises();
            f1.checkMonth(0);
            fail("0 is not a valid month of the year,  expected an IOException, but no exception was thrown.");
        } catch (IOException ioe) {
            assertEquals("Expected message in IOException to be 'negative number'", "Invalid month number", ioe.getMessage());
        }
    }

    @Test
    public void test_checkMonth_13() {
        try {
            FileExercises f1 = new FileExercises();
            f1.checkMonth(13);
            fail("13 is not a valid month of the year,  expected an IOException, but no exception was thrown.");
        } catch (IOException ioe) {
            assertEquals("Expected message in IOException to be 'negative number'", "Invalid month number", ioe.getMessage());
        }
    }

    @Test
    public void test_checkMonth_1() {
        try {
            FileExercises f1  = new FileExercises();
            int           num = f1.checkMonth(1);
            assertEquals("Called checkMonth with 1", 1, num);
        } catch (IOException ioe) {
            fail("Called checkMonth with 1, expected 1 to be returned, instead there was an IOException with message " + ioe.getMessage());
        }
    }

    @Test
    public void test_checkMonth_12() {
        try {
            FileExercises f1  = new FileExercises();
            int           num = f1.checkMonth(12);
            assertEquals("Called checkMonth with 12", 12, num);
        } catch (IOException ioe) {
            fail("Called checkMonth with 12, expected 12 to be returned, instead there was an IOException with message " + ioe.getMessage());
        }
    }

    @Test
    public void test_append_validFile() {

        //Initialize FileExercises
        FileExercises fl = new FileExercises();

        String content  = "one\n";
        String filename = createFile(content);


        // run the test
        fl.append("abc", "def", filename);

        //Read from the output file
        try {
            //Initialize scanner
            Scanner scan = new Scanner(new FileInputStream(filename));

            if (scan.hasNext()) {
                //Read the first line in the file
                String line = scan.nextLine();
                assertEquals("Expected 'one' to remain on first line in file", "one", line);
            } else {
                //If there is nothing in the file, then the test fails
                fail("The file to append to has instead has all content removed.");
            }

            if (scan.hasNext()) {
                //Read the second line in the file
                String line = scan.nextLine();
                assertEquals("Expected 'abc,def' to be added to the file", "abc,def", line);
            } else {
                //If there is nothing in the file, then the test fails
                fail("The output file does not have additional line that should have been appended to file.");
            }
            if (scan.hasNext()) {
                fail("The file to append to had more lines than expected");
            }
            scan.close();
        } catch (FileNotFoundException fnfe) {
            //If cannot open output file, then test fails
            fail("Cannot open the file that should have been appended to.");
        }

        File f = new File(filename);
        f.delete();
    }


    @Test
    public void test_append_createNewFile() {

        //Initialize FileExercises
        FileExercises fl = new FileExercises();

        String filename = getRandomFilename();
        // run the test
        fl.append("john", "doe", filename);

        //Read from the output file
        try {
            //Initialize scanner
            Scanner scan = new Scanner(new FileInputStream(filename));

            if (scan.hasNext()) {
                //Read the first line in the file
                String line = scan.nextLine();
                assertEquals("Expected name to append to be first line in file since the file did not exist", "john,doe", line);
            } else {
                //If there is nothing in the file, then the test fails
                fail("The output file was created as expected but not contain anything.");
            }

            if (scan.hasNext()) {
                fail("The file to append to had more lines than expected");
            }
            scan.close();

        } catch (FileNotFoundException fnfe) {
            //If cannot open output file, then test fails
            fail("Cannot open the output file that should have been created for this test.");
        }

        File f = new File(filename);
        if (!f.delete()) {
            fail("Unable to delete file created for test.  Make sure to manually delete " + filename + " to prevent further failed tests.");
        }
    }


    @Test
    public void test_getLetters_MultipleLinesSpacesAndTabs() {
        String        inFilename  = createFile("Hello\nand\nwelcome\nTo the\nUniversity of Calgary");
        String        outFilename = getRandomFilename() + ".txt";
        FileExercises fl          = new FileExercises();
        try {
            fl.getLetters(inFilename, outFilename);
        } catch (FileNotFoundException fnfe) {
            fail("Unexpected FileNotFoundException since the file  was just created and should exist: " + fnfe.getMessage());
        }

        //Expected output
        String expected = "hawttuoc";

        //Read from the output file
        try {
            //Initialize scanner
            Scanner scan = new Scanner(new FileInputStream(outFilename));

            if (scan.hasNext()) {
                //Read the first line in the file
                String line = scan.nextLine();
                assertEquals("Unexpected string of first letters for file with content 'Hello\nand\nwelcome\nTo the\nUniversity of Calgary'.", expected, line);
            } else {
                //If there is nothing in the file, then the test fails
                fail("The output file is empty.");
            }
            scan.close();
        } catch (FileNotFoundException fnfe) {
            //If cannot open output file, then test fails
            fail("Cannot open the output file.");
        }

        File f = new File(inFilename);
        f.delete();
        f = new File(outFilename);
        f.delete();
    }

    @Test
    public void test_getLetters_FileDoesNotExist() {
        String inFilename  = getRandomFilename() + ".txt";
        String outFilename = getRandomFilename() + ".txt";
        File   f           = new File(inFilename);
        f.delete();
        FileExercises fl = new FileExercises();
        try {
            fl.getLetters(inFilename, outFilename);
            fail("Expected FileNotFoundException since file does not exist.");
        } catch (FileNotFoundException fnfe) {

        }
        f = new File(inFilename);
        f.delete();
        f = new File(outFilename);
        f.delete();
    }

    @Test
    public void test_getLetters_EmptyFile() {
        String        inFilename  = createFile("");
        String        outFilename = getRandomFilename() + ".txt";
        FileExercises fl          = new FileExercises();
        try {
            fl.getLetters(inFilename, outFilename);

        } catch (FileNotFoundException fnfe) {
            fail("Unexpected FileNotFoundException since file test.txt should exist (it was just created): " + fnfe.getMessage());
        }

        //Read from the output file
        try {
            //Initialize scanner
            Scanner scan = new Scanner(new FileInputStream(outFilename));

            if (scan.hasNext()) {
                //Read the first line in the file
                String line = scan.nextLine();
                fail("Expected the output file to be empty, instead the first line was: " + line);
            } else {
                //If there is nothing in the file, then the test fails
            }
            scan.close();
        } catch (FileNotFoundException fnfe) {
            //If cannot open output file, then test fails
            fail("Cannot open the output file.");
        }

        File f = new File(inFilename);
        f.delete();
        f = new File(outFilename);
        f.delete();
    }

    @Test
    public void test_getLetters_singleWordNoWhiteSpaceInFile() {
        String inFilename  = createFile("one");
        String outFilename = getRandomFilename() + ".txt";

        //Initialize FileExercises
        FileExercises fl = new FileExercises();
        try {
            fl.getLetters(inFilename, outFilename);
        } catch (FileNotFoundException fnfe) {
            fail("Unexpected FileNotFoundException since file test.txt should exist (it was just created): " + fnfe.getMessage());
        }


        //Expected output
        String expected = "o";

        //Read from the output file
        try {
            //Initialize scanner
            Scanner scan = new Scanner(new FileInputStream(outFilename));

            if (scan.hasNext()) {
                //Read the first line in the file
                String line = scan.nextLine();
                assertEquals("Unexpected text in output file when input file has single word 'one' in it.", expected, line);
            } else {
                //If there is nothing in the file, then the test fails
                fail("The output file is empty.");
            }
            scan.close();
        } catch (FileNotFoundException fnfe) {
            //If cannot open output file, then test fails
            fail("Cannot open the output file.");
        }

        File f = new File("test3.txt");
        f.delete();
        f = new File("t3.txt");
        f.delete();

    }

    //Test with input file name as null

    @Test
    public void test_getLetters_nullInputFile() {
        String        outFilename = getRandomFilename() + ".txt";
        FileExercises fl          = new FileExercises();
        try {
            fl.getLetters(null, outFilename);
            fail("Expected FileNotFoundException since null was passed as input filename.");
        } catch (FileNotFoundException fnfe) {
        }
        File f = new File(outFilename);
        f.delete();
    }

    @Test
    public void test_numberSearch_noNumberInFile() {
        String inFilename = createFile("This is a test\nfor counting this word\nand here and there is a few tests\nand another test");

        //Initialize FileExercises
        FileExercises fl = new FileExercises();

        // run the test
        try {
            boolean actual = fl.numberSearch(3.5, inFilename);
            assertFalse("The number 3.5 does not appear in the file", actual);
        } catch (FileNotFoundException fnfe) {
            fail("File exists but caught FileNotFoundException: " + fnfe.getMessage());
        }

        File f = new File(inFilename);
        f.delete();
    }

    @Test
    public void test_numberSearch_numberOnceAtEndOfFile() {

        String inFilename = createFile("Hello World\nSome more text 2\n And some4 numbers\n some number3.7");

        //Initialize FileExercises
        FileExercises fl = new FileExercises();

        // run the test
        try {
            boolean actual = fl.numberSearch(3.7, inFilename);
            assertTrue("The number 3.7 appears once in the file", actual);
        } catch (FileNotFoundException fnfe) {
            fail("File exists so no FileNotFoundException expected: " + fnfe.getMessage());
        }

        File f = new File(inFilename);
        f.delete();
    }


    @Test
    public void test_numberSearch_numberManyTimesInFile1() {
        String        inFilename = createFile("3.53.53.53.5");
        FileExercises fl         = new FileExercises();

        try {
            boolean actual = fl.numberSearch(3.5, inFilename);
            assertTrue("The number 3.5 appears in the file", actual);
        } catch (FileNotFoundException fnfe) {
            fail("File exists so no FileNotFoundException expected: " + fnfe.getMessage());
        }
        File f = new File(inFilename);
        f.delete();
    }

    @Test
    public void test_numberSearch_invalidFileName() {
        FileExercises fl = new FileExercises();

        String inFilename = getRandomFilename() + ".txt";
        File   f          = new File(inFilename);
        f.delete();

        try {
            fl.numberSearch(33.7, inFilename);
            fail("File does not exist so FileNotFoundException expected");
        } catch (FileNotFoundException fnfe) {
            // expected, passed the test
        }
        f = new File(inFilename);
        f.delete();
    }


    @Test
    public void test_sumEvenIntegers_invalidFileName() {

        FileExercises fl = new FileExercises();

        String filename = getRandomFilename() + ".bin";
        File   f        = new File(filename);
        f.delete();

        // run the test
        int actual = fl.sumEvenIntegers(filename);

        // verify result
        assertEquals("The file does not exist", -1, actual);
        f = new File(filename);
        f.delete();
    }

    @Test
    public void test_sumEvenIntegers_oneNumInFile() {
        int[]  nums     = {4};
        String filename = createFile(nums);

        //Initialize FileExercises
        FileExercises fl = new FileExercises();

        // run the test
        int actual = fl.sumEvenIntegers(filename);

        // verify result
        assertEquals("The file contains one number: 4", 4, actual);
        File f = new File(filename);
        f.delete();
    }

    @Test
    public void test_sumEvenIntegers_manyNumsInFile() {
        int[]  nums     = {2, 4, 6, 8, 1, 3, 5, 7, 9, 11};
        String filename = createFile(nums);

        FileExercises fl = new FileExercises();

        int actual = fl.sumEvenIntegers(filename);

        assertEquals("The file contains many numbers", 20, actual);
        File f = new File(filename);
        f.delete();
    }

    @Test
    public void test_sumEvenIntegers_zeroNumbersInFile() {
        int[]  nums     = {};
        String filename = createFile(nums);

        //Initialize FileExercises
        FileExercises fl = new FileExercises();

        // run the test
        int actual = fl.sumEvenIntegers(filename);

        // verify result
        assertEquals("The file contains zero numbers", -1, actual);
        File f = new File(filename);
        f.delete();
    }

}
