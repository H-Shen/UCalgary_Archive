import org.junit.Test;

import static org.junit.Assert.*;

public class FileExercisesTest {

    private void createFile(String filename, String text) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            PrintWriter    output = new PrintWriter(writer);
            output.print(text);
            output.close();
        } catch (IOException ioe) {
            fail("Unable to set up test environment, tried to (re)create file " + filename);
        }
    }

    private void createFile(String filename, double[] nums) {
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
    }

    private void createFile(String filename, int[] header, int[][] nums) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(filename));
            for (int headerNum : header) {
                out.writeInt(headerNum);
            }
            for (int rowIndex = 0; rowIndex < nums.length; rowIndex++) {
                int[] row = nums[rowIndex];
                for (int colIndex = 0; colIndex < row.length; colIndex++) {
                    out.writeInt(row[colIndex]);
                }
            }
            out.close();
        } catch (IOException ioe) {
            fail("Unable to set up test environment, tried to create file " + filename);
        }
    }

    @Test
    public void test_checkNumber_NegativeNum_PositiveNum() {
        try {
            FileExercises f1  = new FileExercises();
            int           num = f1.parseNonNegativeInt("47843");
            assertEquals("Expected string '47843' to be converted to integer 47843", 47843, num);
        } catch (IOException ioe) {
            fail("Did not expect exception since 47843 is a positive integer.");
        }
    }

    @Test
    public void test_checkNumber_NegativeNum_Zero() {
        try {
            FileExercises f1  = new FileExercises();
            int           num = f1.parseNonNegativeInt("0");
            assertEquals("Expected string '0' to be converted to integer 0", 0, num);
        } catch (IOException ioe) {
            fail("Did not expect exception since 47843 is a positive integer.");
        }
    }

    @Test
    public void test_checkNumber_NegativeNum_NegativeNum() {
        try {
            FileExercises f1  = new FileExercises();
            int           num = f1.parseNonNegativeInt("-47843");
            fail("Expected exception since '-47842' is not non-negative.  Instead got: " + num);
        } catch (IOException ioe) {
        }
    }

    @Test
    public void test_checkNumber_NegativeNum_LastCharNotADigit() {
        try {
            FileExercises f1  = new FileExercises();
            int           num = f1.parseNonNegativeInt("4784a");
            fail("Expected exception since '4784a' is not an integer.  Instead got: " + num);
        } catch (IOException ioe) {
        }
    }


    @Test
    public void test_is3ByteRGB_emptyFile() {
        int[]   header  = {0, 0};
        int[][] content = new int[0][0];
        createFile("testFile1.bin", header, content);

        FileExercises f1     = new FileExercises();
        boolean       actual = f1.is3ByteRGB("testFile1.bin");

        File f = new File("testFile1.bin");
        f.delete();
        assertTrue("Expected empty file to result in true", actual);
    }

    @Test
    public void test_is3ByteRGB_nonExistantFile() {
        File aFile = new File("testFile2.bin");
        aFile.delete();

        FileExercises f1     = new FileExercises();
        boolean       actual = f1.is3ByteRGB("testFile2.bin");

        assertFalse("Expected false if file does not exist", actual);
    }

    @Test
    public void test_is3ByteRGB_OnePixelIn3Byte() {
        int[]   header  = {1, 1};
        int[][] content = {{255, 255, 255}};
        createFile("testFile3.bin", header, content);

        FileExercises f1     = new FileExercises();
        boolean       actual = f1.is3ByteRGB("testFile3.bin");

        File f = new File("testFile3.bin");
        f.delete();

        assertTrue("Expected true if file contains single pixel with RGB 255,255,255", actual);
    }

    @Test
    public void test_is3ByteRGB_OnePixelNon3Byte() {
        int[]   header  = {1, 1};
        int[][] content = {{256, 256, 256}};
        createFile("testFile.bin", header, content);

        FileExercises f1     = new FileExercises();
        boolean       actual = f1.is3ByteRGB("testFile.bin");

        File f = new File("testFile.bin");
        f.delete();

        assertFalse("Expected false if file contains single pixel with RGB 256,256,256", actual);
    }

    @Test
    public void test_is3ByteRGB_ManyPixelsIn3Byte() {
        int[] header = {10, 5};
        int[][] content = {{255, 255, 255, 0, 0, 0, 255, 0, 0, 0, 0, 255, 1, 2, 3},
                {4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18},
                {19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33},
                {34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48},
                {49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63},
                {64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78},
                {79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93},
                {100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114},
                {115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129},
                {130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144}};
        createFile("testFile.bin", header, content);

        FileExercises f1     = new FileExercises();
        boolean       actual = f1.is3ByteRGB("testFile.bin");

        File f = new File("testFile.bin");
        f.delete();

        assertTrue("Expected true if file contains many pixels with all colour values between 0 and 255 (inclusive)", actual);
    }

    @Test
    public void test_is3ByteRGB_ManyPixelsOneNon3Byte() {
        int[] header = {10, 5};
        int[][] content = {{255, 255, 255, 0, 0, 0, 255, 0, 0, 0, 0, 255, 1, 2, 3},
                {4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18},
                {19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33},
                {34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48},
                {49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63},
                {64, 65, 66, 67, 68, 69, 70, 71, 2000, 73, 74, 75, 76, 77, 78},
                {79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93},
                {100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114},
                {115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129},
                {130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144}};
        createFile("testFile.bin", header, content);

        FileExercises f1     = new FileExercises();
        boolean       actual = f1.is3ByteRGB("testFile.bin");

        File f = new File("testFile.bin");
        f.delete();

        assertFalse("Expected false if file contains many pixels with one colour values greater than 255", actual);
    }

    @Test
    public void test_append_emptyList() {
        double[] nums = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0};
        createFile("test1.bin", nums);


        int[] numsToAppend = new int[0];

        FileExercises f1 = new FileExercises();
        f1.append(numsToAppend, "test1.bin");


        try {
            DataInputStream in     = new DataInputStream(new FileInputStream("test1.bin"));
            int             header = in.readInt();
            assertEquals("Expected first thing in file to be the int 10.", 10, header);
            for (int index = 0; index < 10; index++) {
                double num = in.readDouble();
                assertEquals("Expected next 10 values to remain the doubles between 1.0 and 10.0 (in order)", index + 1, num, 0.00001);
            }
            in.close();
        } catch (IOException ioe) {
            fail("Unexpected exception when reading test file: " + ioe.getMessage());
        }

        File f              = new File("test1.bin");
        long actualLength   = f.length();
        long expectedLength = 4 + 8 * 10;
        f.delete();
        assertEquals("Expected length of file (In bytes) to be unchanged due to append (since list was empty and nothing should have been added).", expectedLength, actualLength);
    }

    @Test
    public void test_append_nullList() {
        double[] nums = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0};
        createFile("test2.bin", nums);


        int[] numsToAppend = null;

        FileExercises f1 = new FileExercises();
        f1.append(numsToAppend, "test2.bin");


        try {
            DataInputStream in     = new DataInputStream(new FileInputStream("test2.bin"));
            int             header = in.readInt();
            assertEquals("Expected first thing in file to be the int 10.", 10, header);
            for (int index = 0; index < 10; index++) {
                double num = in.readDouble();
                assertEquals("Expected next 10 values to remain the doubles between 1.0 and 10.0 (in order)", index + 1, num, 0.00001);
            }
            in.close();
        } catch (IOException ioe) {
            fail("Unexpected exception when reading test file: " + ioe.getMessage());
        }

        File f              = new File("test2.bin");
        long actualLength   = f.length();
        long expectedLength = 4 + 8 * 10;
        f.delete();
        assertEquals("Expected length of file (In bytes) to be unchanged due to append (since list was empty and nothing should have been added).", expectedLength, actualLength);
    }

    @Test
    public void test_append_oneElementList() {
        double[] nums = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0};
        createFile("test3.bin", nums);


        int[] numsToAppend = {-17};

        FileExercises f1 = new FileExercises();
        f1.append(numsToAppend, "test3.bin");


        try {
            DataInputStream in = new DataInputStream(new FileInputStream("test3.bin"));
            // original data
            int header = in.readInt();
            assertEquals("Expected first thing in file to be the int 10.", 10, header);
            for (int index = 0; index < 10; index++) {
                double num = in.readDouble();
                assertEquals("Expected next 10 values to remain the doubles between 1.0 and 10.0 (in order)", index + 1, num, 0.00001);
            }
            //appended data
            int actual = in.readInt();
            assertEquals("Expected number appended to file to be only number in the list", -17, actual);

            in.close();
        } catch (IOException ioe) {
            fail("Unexpected exception when reading test file: " + ioe.getMessage());
        }

        File f              = new File("test3.bin");
        long actualLength   = f.length();
        long expectedLength = 4 + 8 * 10 + 4;
        f.delete();
        assertEquals("Expected length of file (In bytes) to have 4 additional bytes for the additional int.", expectedLength, actualLength);
    }

    @Test
    public void test_append_manyElementList() {
        double[] nums = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0};
        createFile("test4.bin", nums);


        int[] numsToAppend = {-17, 41, 1234, -712, 0};

        FileExercises f1 = new FileExercises();
        f1.append(numsToAppend, "test4.bin");


        try {
            DataInputStream in = new DataInputStream(new FileInputStream("test4.bin"));
            // original data
            int header = in.readInt();
            assertEquals("Expected first thing in file to be the int 10.", 10, header);
            for (int index = 0; index < 10; index++) {
                double num = in.readDouble();
                assertEquals("Expected next 10 values to remain the doubles between 1.0 and 10.0 (in order)", index + 1, num, 0.00001);
            }
            //appended data
            int actual = in.readInt();
            assertEquals("Expected first number appended to file to be -17", -17, actual);
            actual = in.readInt();
            assertEquals("Expected second number appended to file to be 41", 41, actual);
            actual = in.readInt();
            assertEquals("Expected third number appended to file to be 1234", 1234, actual);
            actual = in.readInt();
            assertEquals("Expected fourth number appended to file to be -712", -712, actual);
            actual = in.readInt();
            assertEquals("Expected fifth number appended to file to be 0", 0, actual);

            in.close();
        } catch (IOException ioe) {
            fail("Unexpected exception when reading test file: " + ioe.getMessage());
        }

        File f              = new File("test4.bin");
        long actualLength   = f.length();
        long expectedLength = 4 + 8 * 10 + 5 * 4;
        f.delete();
        assertEquals("Expected length of file (In bytes) to have 5 times 4 additional bytes for the additional integers.", expectedLength, actualLength);
    }

    @Test
    public void test_encrypt_FileDoesNotExist() {
        File f = new File("testEncryptIn.txt");
        f.delete();
        FileExercises f1 = new FileExercises();
        try {
            f1.encrypt(1, "testEncryptIn.txt", "testEncryptOut.txt");
            fail("Expected FileNotFoundException since the provided file does not exist.");
        } catch (FileNotFoundException fnfe) {
            // passed the test, since file does not exist.
        }

    }

    @Test
    public void test_encrypt_EmptyFile() {
        createFile("testEncryptIn1.txt", "");
        FileExercises f1 = new FileExercises();
        try {
            f1.encrypt(1, "testEncryptIn1.txt", "testEncryptOut1.txt");
        } catch (FileNotFoundException fnfe) {
            fail("Did not expected FileNotFoundException since the provided file exists: " + fnfe.getMessage());
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader("testEncryptOut1.txt"));
            String         line   = reader.readLine();
            assertNull("Reading from empty file should result in null", line);
            reader.close();
        } catch (FileNotFoundException fnfe) {
            fail("Expected empty file to be created for output: " + fnfe.getMessage());
        } catch (IOException ioe) {
            fail("Unexpected IO exception during test: " + ioe.getMessage());
        }
        File f = new File("testEncryptIn1.txt");
        f.delete();

    }

    @Test
    public void test_encrypt_OneCharacterFile() {
        createFile("testEncryptIn2.txt", "z");
        FileExercises f1 = new FileExercises();
        try {
            f1.encrypt(1, "testEncryptIn2.txt", "testEncryptOut2.txt");
        } catch (FileNotFoundException fnfe) {
            fail("Did not expected FileNotFoundException since the provided file exists: " + fnfe.getMessage());
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader("testEncryptOut2.txt"));
            String         line   = reader.readLine();
            assertEquals("Expected first line in encrypted file to have length 1", 1, line.length());
            assertEquals("Expected single character in file, 'z', to be encrypted to 'a' with shift 1", 'a', line.charAt(0));
            line = reader.readLine();
            assertNull("After reading first line with single character, expected file to be empty and read should result in null", line);
            reader.close();
        } catch (FileNotFoundException fnfe) {
            fail("Expected file to be created for output: " + fnfe.getMessage());
        } catch (IOException ioe) {
            fail("Unexpected IO exception during test: " + ioe.getMessage());
        }
        File f = new File("testEncryptIn2.txt");
        f.delete();
    }

    @Test
    public void test_encrypt_ManyLinesAlphabeticOnly() {
        createFile("testEncryptIn3.txt", "HelloThereThisIsATest\nSecondLineInTheTest\nAndAThirdLine");
        FileExercises f1 = new FileExercises();
        try {
            f1.encrypt(8, "testEncryptIn3.txt", "testEncryptOut3.txt");
        } catch (FileNotFoundException fnfe) {
            fail("Did not expected FileNotFoundException since the provided file exists: " + fnfe.getMessage());
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader("testEncryptOut3.txt"));
            String         line   = reader.readLine();
            assertEquals("Expected first line in encrypted file to have length 21", 21, line.length());
            assertEquals("Expected 'HelloThereThisIsATest' to be encrypted (with shift 8) to 'PmttwBpmzmBpqaQaIBmab'", "PmttwBpmzmBpqaQaIBmab", line);
            line = reader.readLine();
            assertEquals("Expected second line in encrypted file to have length 19", 19, line.length());
            assertEquals("Expected 'SecondLineInTheTest' to be encrypted (with shift 8) to 'AmkwvlTqvmQvBpmBmab'", "AmkwvlTqvmQvBpmBmab", line);
            line = reader.readLine();
            assertEquals("Expected third line in encrypted file to have length 13", 13, line.length());
            assertEquals("Expected 'AndAThirdLine' to be encrypted (with shift 8) to 'IvlIBpqzlTqvm'", "IvlIBpqzlTqvm", line);
            line = reader.readLine();
            assertNull("After reading three lines, expected file to be empty and read should result in null", line);
            reader.close();
        } catch (FileNotFoundException fnfe) {
            fail("Expected file to be created for output: " + fnfe.getMessage());
        } catch (IOException ioe) {
            fail("Unexpected IO exception during test: " + ioe.getMessage());
        }
        File f = new File("testEncryptIn3.txt");
        f.delete();
    }

    @Test
    public void test_encrypt_ManyLinesNonAlphabeticOnly() {
        createFile("testEncryptIn4.txt", "?><!@23490()\n@~!78:',.\n;*&67 5%$\n123498");
        FileExercises f1 = new FileExercises();
        try {
            f1.encrypt(8, "testEncryptIn4.txt", "testEncryptOut4.txt");
        } catch (FileNotFoundException fnfe) {
            fail("Did not expected FileNotFoundException since the provided file exists: " + fnfe.getMessage());
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader("testEncryptOut4.txt"));
            String         line   = reader.readLine();
            assertEquals("Expected first line '?><!@23490()' to be unchanged after encryption", "?><!@23490()", line);
            line = reader.readLine();
            assertEquals("Expected second line '@~!78:',.' to be unchanged after encryption", "@~!78:',.", line);
            line = reader.readLine();
            assertEquals("Expected third line ';*&67 5%$' to be unchanged after encryption", ";*&67 5%$", line);
            line = reader.readLine();
            assertEquals("Expected fourth line '123498' to be unchanged after encryption", "123498", line);
            line = reader.readLine();
            assertNull("After reading three lines, expected file to be empty and read should result in null", line);
            reader.close();
        } catch (FileNotFoundException fnfe) {
            fail("Expected file to be created for output: " + fnfe.getMessage());
        } catch (IOException ioe) {
            fail("Unexpected IO exception during test: " + ioe.getMessage());
        }
        File f = new File("testEncryptIn4.txt");
        f.delete();
    }

    @Test
    public void test_encrypt_ManyLinesMixOfAlphaAndNonAlpha() {
        createFile("testEncryptIn5.txt", "This is another test. It has\n3 lines: a mix of\nalphabetic and <> alphabetic.");
        FileExercises f1 = new FileExercises();
        try {
            f1.encrypt(5, "testEncryptIn5.txt", "testEncryptOut5.txt");
        } catch (FileNotFoundException fnfe) {
            fail("Did not expected FileNotFoundException since the provided file exists: " + fnfe.getMessage());
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader("testEncryptOut5.txt"));
            String         line   = reader.readLine();
            assertEquals("Expected first line was 'This is another test. It has' with shift 5", "Ymnx nx fstymjw yjxy. Ny mfx", line);
            line = reader.readLine();
            assertEquals("Expected second line was '3 lines: a mix of' with shift 5", "3 qnsjx: f rnc tk", line);
            line = reader.readLine();
            assertEquals("Expected third line was 'alphabetic and <> alphabetic.' with shift 5", "fqumfgjynh fsi <> fqumfgjynh.", line);
            line = reader.readLine();
            assertNull("After reading three lines, expected file to be empty and read should result in null", line);
            reader.close();
        } catch (FileNotFoundException fnfe) {
            fail("Expected file to be created for output: " + fnfe.getMessage());
        } catch (IOException ioe) {
            fail("Unexpected IO exception during test: " + ioe.getMessage());
        }
        File f = new File("testEncryptIn4.txt");
        f.delete();
    }

}
