import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class CustomerTA8Test {
    // test constructors

    @Test
    public void testDefaultConstructor() {
        Customer c = new Customer();
        assertEquals("Expected initial ID to be 0", 0, c.getID(), 0.00001);
    }

    @Test
    public void testConstructorWithParams() {
        Customer c = new Customer("John Doe", 876);
        assertEquals("Expected name to be John Doe", "John Doe", c.getName());
        assertEquals("Expected id to be 876", 876, c.getID(), 0.0001);
    }

    @Test
    public void testCopyConstructor() {
        Customer oldCustomer = new Customer("Alice Black", 314);
        Customer newCustomer = new Customer(oldCustomer);
        assertEquals("Expected name to be Alice Black", "Alice Black", newCustomer.getName());
        assertEquals("Expected id to be 314", 314, newCustomer.getID(), 0.0001);
    }

    @Test
    public void test_toString_test1() {
        Customer c = new Customer("Alice Black", 314);
        assertEquals("Alice Black 314", c.toString());
    }

    @Test
    public void test_toString_test2() {
        Customer c = new Customer("Mona Lisa", 61);
        assertEquals("Mona Lisa 61", c.toString());
    }

    @Test
    public void test_saveToTextFile_allInstanceVariablesInitialized() {
        Customer c = new Customer("Testing Saving", 1);

        try {
            PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter("testc1.txt")));
            c.save(output);
            output.close();
        } catch (IOException ioe) {
            fail("Unexpected exception thrown when trying to save Customer to file with name 'testc1.txt'");
        }

        String[] expectedLinesInFile = {"Testing Saving", "1"};
        assertFileContent("Saving to file Customer 'Testing Saving', 1", expectedLinesInFile, "testc1.txt");

    }

    @Test
    public void test_constructorFromTextFile_allInstanceVariablesInFile() {
        Customer c           = null;
        String[] linesInFile = {"Hello", "42"};
        try {
            createFile("readCustomerTest1.txt", linesInFile);
            BufferedReader input = new BufferedReader(new FileReader("readCustomerTest1.txt"));

            c = new Customer(input);
            input.close();
        } catch (IOException ioe) {
            fail("Unexpected IOException when creating Customer from file");
        }
        assertEquals("Testing name: Reading from file: 'Hello', 42", "Hello", c.getName());
        assertEquals("Testing id: Reading from file: 'Hello', 42", 42, c.getID());

    }

    @Test
    public void test_constructorFromTextFile_null() {
        Customer c           = null;
        String[] linesInFile = {"null"};
        try {
            createFile("readCustomerTest2.txt", linesInFile);
            BufferedReader input = new BufferedReader(new FileReader("readCustomerTest2.txt"));

            c = new Customer(input);
            input.close();
            fail("Expected constructor to thrown an exception since word in file was null, but no exception was thrown." + c.toString());
        } catch (IOException ioe) {
            assertEquals("Expected IOException with message 'Customer is null in file'", "Customer is null in file", ioe.getMessage());
        }
    }

    @Test
    public void test_constructorFromTextFile_idMissing() {
        Customer c           = null;
        String[] linesInFile = {"Missing ID"};
        try {
            createFile("readCustomerTest3.txt", linesInFile);
            BufferedReader input = new BufferedReader(new FileReader("readCustomerTest3.txt"));

            c = new Customer(input);
            input.close();
            fail("Expected constructor to thrown an exception since ID is missing, but no exception was thrown." + c.toString());
        } catch (IOException ioe) {
            assertEquals("Expected IOException with message 'No customer ID found in file'", "No customer ID found in file", ioe.getMessage());
        }
    }

    private void createFile(String filename, String[] lines) throws IOException {
        PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
        for (String line : lines) {
            output.println(line);
        }
        output.close();
    }

    private void assertFileContent(String message, String[] expectedLinesInFile, String filename) {
        try {
            BufferedReader input = new BufferedReader(new FileReader(filename));
            int            index = 0;
            String         line  = input.readLine();
            while (line != null) {
                if (index >= expectedLinesInFile.length) {
                    fail(message + " Found more lines in file than expected.  Only expected " + expectedLinesInFile.length + " lines");
                }
                assertEquals(message + " testing line " + index + "in output file", expectedLinesInFile[index], line);
                line = input.readLine();
                index++;
            }
            assertEquals(message + " Expected more lines in output file.", expectedLinesInFile.length, index);
            input.close();
        } catch (IOException ioe) {
            fail("Unexpected exception when testing file content.");
        }
    }
}