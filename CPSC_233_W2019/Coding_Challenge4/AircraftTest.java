import org.junit.Test;

import static org.junit.Assert.*;

public class AircraftTest extends FormatTester {

    public static final String CLASSNAME = "Aircraft";

    public AircraftTest() {
        super(CLASSNAME, false);
    }

    private void testClassDefinition() {
        String[] instanceVars = {"String name", "String origin", "int speed"};
        assertTrue("Instance variables should be private with correct name and type.", instanceVariablesArePrivate(instanceVars));

        assertTrue("Class should not have the default constructor.", noDefaultConstructor());

        String[] abstractProtectedMethods = {"String getCategory()"};
        assertTrue("Class should have abstract method getCategory that returns a String.",
                hasRequiredAbstractMethods(abstractProtectedMethods, "protected"));


    }

    @Test
    public void test_Constructor_EmptyStringAnd0() {
        testClassDefinition();
        Aircraft v = new VM("", "", 0);
        assertEquals("Created Aircraft with empty name, empty origin and speed 0 - testing name", "", v.getName());
        assertEquals("Created Aircraft with empty name, empty origin and speed 0 - testing origin", "", v.getOrigin());
        assertEquals("Created Aircraft with empty name, empty origin and speed 0 - testing speed", 0, v.getSpeed());
    }

    // Testing constructors

    @Test
    public void test_Constructor_AllLowerCaseAnd300() {
        testClassDefinition();
        Aircraft v = new VM("mirage", "france", 300);
        assertEquals("Created Aircraft with 'mirage' name 'france' origin and speed 300 - testing name", "mirage", v.getName());
        assertEquals("Created Aircraft with 'mirage' name 'france' origin and speed 300 - testing origin", "FRANCE", v.getOrigin());
        assertEquals("Created Aircraft with 'mirage' name, 'france' origin and speed 300 - testing speed", 300, v.getSpeed());
    }

    @Test
    public void test_Constructor_AllUpperCaseAndNegative() {
        testClassDefinition();
        Aircraft v = new VM("SAAB", "SWEDEN", -1);
        assertEquals("Created Aircraft with 'SAAB' name, 'SWEDEN' origin and speed -1 - testing name", "saab", v.getName());
        assertEquals("Created Aircraft with 'SAAB' name, 'SWEDEN' origin and speed -1 - testing origin", "SWEDEN", v.getOrigin());
        assertEquals("Created Aircraft with 'SAAB' name, 'SWEDEN' origin and speed -1 - testing speed", 0, v.getSpeed());
    }

    @Test
    public void test_Constructor_MixedCase() {
        testClassDefinition();
        Aircraft v = new VM("Mirage", "France", 350);
        assertEquals("Created Aircraft with 'Mirage' name, 'France' origin and speed 350 - testing name", "mirage", v.getName());
        assertEquals("Created Aircraft with 'Mirage' name, 'France' origin and speed 350 - testing origin", "FRANCE", v.getOrigin());
        assertEquals("Created Aircraft with 'Mirage' name, 'France' origin and speed 350 - testing speed", 350, v.getSpeed());
    }

    @Test
    public void test_CopyConstructor() {
        testClassDefinition();
        Aircraft v  = new VM("name1", "TEST1", 100);
        Aircraft v2 = new VM(v);
        assertEquals("Testing Copy Constructor, copying 'name1' name, 'TEST1' origin and speed 100 - testing name", "name1", v2.getName());
        assertEquals("Testing Copy Constructor, copying 'name1' name, 'TEST1' origin and speed 100 - testing origin", "TEST1", v2.getOrigin());
        assertEquals("Testing Copy Constructor, copying 'name1' name, 'TEST1' Copy Constructor' origin and speed 100 - testing speed", 100, v2.getSpeed());
    }

    @Test
    public void test_CopyConstructor2() {
        testClassDefinition();
        Aircraft v  = new VM("NAME2", "Test2", 200);
        Aircraft v2 = new VM(v);
        assertEquals("Testing Copy Constructor, copying 'NAME2' name 'Test2' origin and speed 200 - testing name", "name2", v2.getName());
        assertEquals("Testing Copy Constructor, copying 'NAME2' name 'Test2' origin and speed 200 - testing origin", "TEST2", v2.getOrigin());
        assertEquals("Testing Copy Constructor, copying 'NAME2' name, 'Test2' origin and speed 200 - testing speed", 200, v2.getSpeed());
    }

    @Test
    public void test_setter_and_getter_origin_emptyString() {
        testClassDefinition();
        Aircraft v = new VM("Test", "TestEmptyString", 700);
        v.setOrigin("");
        assertEquals("Set origin to empty string", "", v.getOrigin());
    }


    // Testing setter and getters

    @Test
    public void test_setter_and_getter_origin_allLowerCase() {
        testClassDefinition();
        Aircraft v = new VM("Test", "TestAllLowerCase", 200);
        v.setOrigin("france");
        assertEquals("Set origin to 'france'", "FRANCE", v.getOrigin());
    }

    @Test
    public void test_setter_and_getter_origin_allUpperCase() {
        testClassDefinition();
        Aircraft v = new VM("Test", "TestAllUpperCase", 140);
        v.setOrigin("USA");
        assertEquals("Set origin to 'USA'", "USA", v.getOrigin());
    }

    @Test
    public void test_setter_and_getter_origin_MixedCase() {
        testClassDefinition();
        Aircraft v = new VM("Test", "TestMixedCase", 100);
        v.setOrigin("Germany");
        assertEquals("Set origin to 'GERMANY'", "GERMANY", v.getOrigin());
    }

    @Test
    public void test_setter_and_getter_name_emptyString() {
        testClassDefinition();
        Aircraft v = new VM("TestEmptyString", "TestEmptyString", 700);
        v.setName("");
        assertEquals("Set name to empty string", "", v.getName());
    }

    @Test
    public void test_setter_and_getter_name_allLowerCase() {
        testClassDefinition();
        Aircraft v = new VM("TestAllLowerCase", "TestAllLowerCase", 200);
        v.setName("cobra");
        assertEquals("Set name to 'cobra'", "cobra", v.getName());
    }

    @Test
    public void test_setter_and_getter_name_allUpperCase() {
        testClassDefinition();
        Aircraft v = new VM("TestAllUpperCase", "TestAllUpperCase", 140);
        v.setName("COBRA");
        assertEquals("Set name to 'COBRA'", "cobra", v.getName());
    }

    @Test
    public void test_setter_and_getter_name_MixedCase() {
        testClassDefinition();
        Aircraft v = new VM("TestMixedCase", "TestMixedCase", 100);
        v.setName("Fighting Falcon");
        assertEquals("Set name to 'Fighting Falcon'", "fighting falcon", v.getName());
    }

    @Test
    public void test_setter_and_getter_speed_three_hundard() {
        testClassDefinition();
        Aircraft v = new VM("testone", "TestOneSpeed", 500);
        v.setSpeed(300);
        assertEquals("Set speed to three hundard.", 300, v.getSpeed());
    }

    @Test
    public void test_setter_and_getter_speed_four_fifty() {
        testClassDefinition();
        Aircraft v = new VM("TestTen", "TestTenSpeed", 170);
        v.setSpeed(450);
        assertEquals("Set speed to four hundard and fifty.", 450, v.getSpeed());
    }

    @Test
    public void test_setter_and_getter_speed_negative() {
        testClassDefinition();
        Aircraft v = new VM("TestNegative", "TestNegativeSpeed", 375);
        v.setSpeed(-1);
        assertEquals("Set speed to a negative number that was initialized to 375.", 375, v.getSpeed());
    }

    @Test
    public void test_getAircraftCategoryBySpeed_A() {
        testClassDefinition();
        VM v = new VM("name", "origin", 0);
        v.next = "Testing";
        v.setSpeed(450);
        assertEquals("Category for speed 450 is A", "Testing with A", v.getAircraftCategoryBySpeed());

    }

    @Test
    public void test_getAircraftCategoryBySpeed_B() {
        testClassDefinition();
        VM v = new VM("name", "origin", 700);
        v.next = "Testing";
        v.setSpeed(650);
        assertEquals("Category for speed 650 is B", "Testing with B", v.getAircraftCategoryBySpeed());

    }

    @Test
    public void test_getAircraftCategoryBySpeed_C() {
        testClassDefinition();
        VM v = new VM("name", "origin", 0);
        v.next = "Testing";
        v.setSpeed(1000);
        assertEquals("Category for speed 1000 is C", "Testing with C", v.getAircraftCategoryBySpeed());
    }

    @Test
    public void test_toString() {
        testClassDefinition();
        VM v = new VM("mirage", "france", 300);
        v.next = "test1";
        assertEquals("Testing toString for Aircraft with mirage name, france country and 300 speed",
                "Name: mirage Category: test1 with A", v.toString());
    }

    @Test
    public void test_toString2() {
        testClassDefinition();
        VM v = new VM("saab", "Sweden", 2000);
        v.next = "test2";
        assertEquals("Testing toString for Aircraft with saab name, Sweden country and 2000 speed",
                "Name: saab Category: test2 with C", v.toString());
    }

    public class VM extends Aircraft {
        String next = "";

        public VM(String name, String origin, int speed) {
            super(name, origin, speed);
        }

        public VM(Aircraft c) {
            super(c);
        }

        public String getCategory() {
            return next;
        }
    }

}
