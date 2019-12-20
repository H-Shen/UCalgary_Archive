import org.junit.Test;

import static org.junit.Assert.*;

public class StreamingMediaTest {
    public static final String CLASSNAME = "StreamingMedia";
    public static final String FILENAME  = CLASSNAME + ".java";

    private boolean hasRequiredAbstractMethods(String[] abstractMethods) {
        boolean[] methodsAbstract = new boolean[abstractMethods.length];
        for (int index = 0; index < abstractMethods.length; index++) {
            methodsAbstract[index] = false;
        }
        boolean classIsAbstract = false;

        try {
            BufferedReader in   = new BufferedReader(new FileReader(FILENAME));
            String         line = in.readLine();
            while (line != null) {
                if (line.contains("public abstract class " + CLASSNAME)) {
                    classIsAbstract = true;
                } else {
                    for (int index = 0; index < abstractMethods.length; index++) {
                        String stmt = "public abstract " + abstractMethods[index];
                        if (line.contains(stmt)) {
                            methodsAbstract[index] = true;
                        }
                    }
                }
                line = in.readLine();
            }
            in.close();
        } catch (FileNotFoundException e) {
            classIsAbstract = false;
        } catch (IOException e) {
            classIsAbstract = false;
        }

        boolean allAbstract = classIsAbstract;
        for (boolean b : methodsAbstract) {
            allAbstract = allAbstract && b;
        }
        return allAbstract;

    }

    private boolean hasRequiredProtectedMethods(String[] protectedMethods) {
        boolean[] methodsProtected = new boolean[protectedMethods.length];
        for (int index = 0; index < protectedMethods.length; index++) {
            methodsProtected[index] = false;
        }
        boolean allProtected = true;

        try {
            BufferedReader in   = new BufferedReader(new FileReader(FILENAME));
            String         line = in.readLine();
            while (line != null) {
                for (int index = 0; index < protectedMethods.length; index++) {
                    String stmt = "protected " + protectedMethods[index];
                    if (line.contains(stmt)) {
                        methodsProtected[index] = true;
                    }
                }

                line = in.readLine();
            }
            in.close();
        } catch (FileNotFoundException e) {
            allProtected = false;
        } catch (IOException e) {
            allProtected = false;
        }

        for (boolean b : methodsProtected) {
            allProtected = allProtected && b;
        }
        return allProtected;
    }

    private boolean instanceVariablesArePrivate(String[] instanceVars) {
        boolean[] varsPrivate = new boolean[instanceVars.length];
        for (int index = 0; index < instanceVars.length; index++) {
            varsPrivate[index] = false;
        }
        boolean allPrivate = true;


        try {
            BufferedReader in   = new BufferedReader(new FileReader(FILENAME));
            String         line = in.readLine();
            while (line != null) {
                if (line.contains("private")) {
                    line = line.trim();
                    for (int index = 0; index < instanceVars.length; index++) {
                        String stmt = "private " + instanceVars[index];
                        if (line.length() >= stmt.length()) {
                            line = line.substring(0, stmt.length());
                            if (line.equals(stmt)) {
                                varsPrivate[index] = true;
                            }
                        }
                    }
                }
                line = in.readLine();
            }
            in.close();
        } catch (FileNotFoundException e) {
            allPrivate = false;
        } catch (IOException e) {
            allPrivate = false;
        }

        for (boolean b : varsPrivate) {
            allPrivate = allPrivate && b;
        }
        return allPrivate;
    }

    private boolean noDefaultConstructor() {
        boolean  noDefault = true;
        String[] versions  = new String[9];
        versions[0] = "public " + CLASSNAME + "()";
        versions[1] = "public " + CLASSNAME + " ()";
        versions[2] = "public " + CLASSNAME + " ( )";
        versions[3] = "protected " + CLASSNAME + "()";
        versions[4] = "protected " + CLASSNAME + " ()";
        versions[5] = "protected " + CLASSNAME + " ( )";
        versions[6] = CLASSNAME + "()";
        versions[7] = CLASSNAME + " ()";
        versions[8] = CLASSNAME + " ( )";

        try {
            BufferedReader in   = new BufferedReader(new FileReader(FILENAME));
            String         line = in.readLine();
            while (line != null) {
                for (String stmt : versions) {
                    if (line.contains(stmt)) {
                        noDefault = false;
                    }
                }
                line = in.readLine();
            }
            in.close();
        } catch (FileNotFoundException e) {
            noDefault = false;
        } catch (IOException e) {
            noDefault = false;
        }
        return noDefault;
    }

    private void testClassDefinition() {
        String[] instanceVars = {"String title", "int length"};
        assertTrue("Instance variables should be private with correct name and type.", instanceVariablesArePrivate(instanceVars));

        assertTrue("Class should not have the default constructor.", noDefaultConstructor());

        String[] abstractMethods = {"int getRating()"};
        assertTrue("Class should have abstract method getRating that returns an int.", hasRequiredAbstractMethods(abstractMethods));

        String[] protectedMethods = {"void setLength", "void setTitle"};
        assertTrue("Class should have protected method setLength and setTitle.", hasRequiredProtectedMethods(protectedMethods));

    }

    // Testing constructors
    @Test
    public void test_Constructor_EmptyStringAnd1() {
        testClassDefinition();

        SM c = new SM("", 1);
        assertEquals("Created media with empty title and length 1 - testing title", "", c.getTitle());
        assertEquals("Created media with empty title and length 1 - testing length", 1, c.getLength());
    }

    @Test
    public void test_Constructor_AllLowerCaseAnd11() {
        testClassDefinition();

        SM c = new SM("despicable me 3", 11);
        assertEquals("Created media with 'despicable me 3' title and length 11 - testing title", "DESPICABLE ME 3", c.getTitle());
        assertEquals("Created media with 'despicable me 3' title and length 11 - testing length", 11, c.getLength());
    }

    @Test
    public void test_Constructor_AllUpperCaseAndZero() {
        testClassDefinition();
        SM c = new SM("BIG HERO 6", 0);
        assertEquals("Created media with 'BIG HERO 6' title and length 0 - testing title", "BIG HERO 6", c.getTitle());
        assertEquals("Created media with 'BIG HERO 6' title and length 0 - testing length", 10, c.getLength());
    }

    @Test
    public void test_Constructor_MixedCaseAndNegative() {
        testClassDefinition();
        SM c = new SM("Hotel Transylvania", -1);
        assertEquals("Created media with 'Hotel Transylvania' title and length -1 - testing title", "HOTEL TRANSYLVANIA", c.getTitle());
        assertEquals("Created media with 'Hotel Transylvania' title and length -1 - testing length", 10, c.getLength());
    }

    @Test
    public void test_CopyConstructor() {
        testClassDefinition();
        SM c  = new SM("TEST1", 6);
        SM c2 = new SM(c);
        assertEquals("Testing Copy Constructor, copying 'TEST1' title and length 6 - testing title", "TEST1", c2.getTitle());
        assertEquals("Testing Copy Constructor, copying 'TEST1' Copy Constructor' title and length 6 - testing length", 6, c2.getLength());
    }

    @Test
    public void test_CopyConstructor2() {
        testClassDefinition();
        SM c  = new SM("Test2", 111);
        SM c2 = new SM(c);
        assertEquals("Testing Copy Constructor, copying 'Test2' title and length 111 - testing title", "TEST2", c2.getTitle());
        assertEquals("Testing Copy Constructor, copying 'Test2' title and length 111 - testing length", 111, c2.getLength());
    }

    @Test
    public void test_setter_and_getter_title_emptyString() {
        testClassDefinition();
        SM c = new SM("TestEmptyString", 1);
        c.setTitle("");
        assertEquals("Set title to empty string", "", c.getTitle());
    }

    // Testing setter and getters

    @Test
    public void test_setter_and_getter_title_allLowerCase() {
        testClassDefinition();
        SM c = new SM("TestAllLowerCase", 2);
        c.setTitle("despicable me 3");
        assertEquals("Set title to 'despicable me 3'", "DESPICABLE ME 3", c.getTitle());
    }

    @Test
    public void test_setter_and_getter_title_allUpperCase() {
        testClassDefinition();
        SM c = new SM("TestAllUpperCase", 3);
        c.setTitle("BIG HERO 6");
        assertEquals("Set title to 'BIG HERO 6'", "BIG HERO 6", c.getTitle());
    }

    @Test
    public void test_setter_and_getter_title_MixedCase() {
        testClassDefinition();
        SM c = new SM("TestMixedCase", 4);
        c.setTitle("Hotel Transylvania");
        assertEquals("Set title to 'Hotel Transylvania'", "HOTEL TRANSYLVANIA", c.getTitle());
    }

    @Test
    public void test_setter_and_getter_length_zero() {
        testClassDefinition();
        SM c = new SM("TestZeroLength", 5);
        c.setLength(0);
        assertEquals("Set length to invalid zero from 5.", 5, c.getLength());
    }

    @Test
    public void test_setter_and_getter_length_ten() {
        testClassDefinition();
        SM c = new SM("TestTenLength", 5);
        c.setLength(10);
        assertEquals("Set length to ten.", 10, c.getLength());
    }

    @Test
    public void test_setter_and_getter_length_negative() {
        testClassDefinition();
        SM c = new SM("TestNegativeLength", 3);
        c.setLength(-1);
        assertEquals("Set length to a negative number that was initialized to 3.", 3, c.getLength());
    }

    @Test
    public void test_getCategory_F() {
        testClassDefinition();
        SM c = new SM("TestingCategoryF", 0);
        c.rating = 2;
        assertEquals("Set rating to 2", 'F', c.getCategory());
        c.rating = 0;
        assertEquals("Set rating to 0", 'F', c.getCategory());
        c.rating = 1;
        assertEquals("Set rating to 1", 'F', c.getCategory());
    }

    @Test
    public void test_getCategory_D() {
        testClassDefinition();
        SM c = new SM("TestingCategoryD", 0);
        c.rating = 3;
        assertEquals("Set rating to 3", 'D', c.getCategory());
        c.rating = 4;
        assertEquals("Set rating to 4", 'D', c.getCategory());
    }

    @Test
    public void test_getCategory_C() {
        testClassDefinition();
        SM c = new SM("TestingCategoryC", 0);
        c.rating = 5;
        assertEquals("Set rating to 5", 'C', c.getCategory());
        c.rating = 6;
        assertEquals("Set rating to 6", 'C', c.getCategory());
    }

    @Test
    public void test_getCategory_B() {
        testClassDefinition();
        SM c = new SM("TestingCategoryB", 0);
        c.rating = 7;
        assertEquals("Set rating to 7", 'B', c.getCategory());
        c.rating = 8;
        assertEquals("Set rating to 8", 'B', c.getCategory());
    }

    @Test
    public void test_getCategory_A() {
        testClassDefinition();
        SM c = new SM("TestingCategoryA", 0);
        c.rating = 9;
        assertEquals("Set rating to 9", 'A', c.getCategory());
        c.rating = 10;
        assertEquals("Set rating to 10", 'A', c.getCategory());
    }

    @Test
    public void test_toString() {
        testClassDefinition();
        SM c = new SM("TESTING_TO_STRING", 20);
        assertEquals("title: TESTING_TO_STRING and length 20", "Title: TESTING_TO_STRING Length: 20", c.toString());
    }

    public class SM extends StreamingMedia {
        double monthly;
        int    rating;

        public SM(String name, int length) {
            super(name, length);
        }

        public SM(SM c) {
            super(c);
        }

        public int getRating() {
            return rating;
        }
    }
}
