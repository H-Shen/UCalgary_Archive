import org.junit.Test;

import static org.junit.Assert.*;

public class BomberAircraftTest extends FormatTester {

    public static final String CLASSNAME = "BomberAircraft";
    public static final String FILENAME  = CLASSNAME + ".java";

    public BomberAircraftTest() {
        super(CLASSNAME, false);
    }

    private void testInterface() {
        String[] instanceVars = {"int payload"};
        assertTrue("Instance variables should be private with correct name and type.", instanceVariablesArePrivate(instanceVars));

        assertFalse("Should not override (or call) getName.", hasMethod("getName"));
        assertFalse("Should not override (or call) setName.", hasMethod("setName"));
        assertFalse("Should not override (or call) setOrigin.", hasMethod("setOrigin"));
        assertFalse("Should not override (or call) setSpeed.", hasMethod("setSpeed"));
        assertFalse("Should not override (or call) getOrigin.", hasMethod("getOrigin"));
        assertFalse("Should not override (or call) getSpeed.", hasMethod("getSpeed"));
        assertFalse("Should not override (or call) getAircraftCategoryBySpeed.", hasMethod("getAircraftCategoryBySpeed"));
        assertFalse("Should not override (or call) toString.", hasMethod("toString"));
    }


    // Testing constructors
    @Test
    public void test_Constructor_PayloadMin0() {
        testInterface();

        BomberAircraft t = new BomberAircraft("mirage", "FRANCE", 300, 0);
        assertEquals("Created BomberAircraft with payload 0 - testing name", "mirage", t.getName());
        assertEquals("Created BomberAircraft with payload 0 - testing origin", "FRANCE", t.getOrigin());
        assertEquals("Created BomberAircraft with payload 0 - testing speed", 300, t.getSpeed());
        assertEquals("Created BomberAircraft with payload 0 - testing payload", 0, t.getPayload());
    }


    @Test
    public void test_Constructor_PositivePayload() {
        testInterface();

        BomberAircraft t = new BomberAircraft("mirage", "FRANCE", 300, 5);
        assertEquals("Created BomberAircraft with payload 5 - testing name", "mirage", t.getName());
        assertEquals("Created BomberAircraft with payload 5 - testing origin", "FRANCE", t.getOrigin());
        assertEquals("Created BomberAircraft with payload 5 - testing speed", 300, t.getSpeed());
        assertEquals("Created BomberAircraft with payload 5 - testing payload", 5, t.getPayload());
    }

    @Test
    public void test_CopyConstructor() {
        BomberAircraft t  = new BomberAircraft("name1", "TEST1", 100, 5);
        BomberAircraft t2 = new BomberAircraft(t);
        assertEquals("Testing Copy Constructor, copying 'name1' name, 'TEST1' origin and speed 100 - testing name", "name1", t2.getName());
        assertEquals("Testing Copy Constructor, copying 'name1' name, 'TEST1' origin and speed 100 - testing origin", "TEST1", t2.getOrigin());
        assertEquals("Testing Copy Constructor, copying 'name1' name, 'TEST1' Copy Constructor' origin and speed 100 - testing speed", 100, t2.getSpeed());
        assertEquals("Testing Copy Constructor, payload 5 - testing payload", 5, t2.getPayload());
    }

    // Testing setter and getters


    @Test
    public void test_setter_and_getter_payload_zero() {
        testInterface();
        BomberAircraft t = new BomberAircraft("name", "ORIGIN", 500, 5);
        t.setPayload(0);
        assertEquals("Set payload to zero from 5.", 0, t.getPayload());
    }


    @Test
    public void test_setter_and_getter_payload_positive() {
        testInterface();
        BomberAircraft t = new BomberAircraft("name", "ORIGIN", 100, 5);
        t.setPayload(7);
        assertEquals("Set payload to 7 that was initialized to 5.", 7, t.getPayload());
    }

    @Test
    public void test_getCategory_Lighter() {
        testInterface();
        BomberAircraft t = new BomberAircraft("name", "ORIGIN", 100, 1);
        assertEquals("Set payload 1", "LIGHTER", t.getCategory());
    }

    @Test
    public void test_getCategory_Medium() {
        testInterface();
        BomberAircraft t = new BomberAircraft("name", "ORIGIN", 50, 2);
        assertEquals("Set payload 2", "MEDIUM", t.getCategory());
    }

    @Test
    public void test_getCategory_Heavier() {
        testInterface();
        BomberAircraft t = new BomberAircraft("name", "ORIGIN", 700, 8);
        assertEquals("Set payload 8", "HEAVIER", t.getCategory());
        t.setPayload(7);
        assertEquals("Set payload 7", "HEAVIER", t.getCategory());
        t.setPayload(6);
        assertEquals("Set payload 6", "HEAVIER", t.getCategory());
    }

    // Testing that methods in parents correctly invoke abstract/overridden method.


    @Test
    public void test_getAircraftCategoryBySpeed_A_HEAVIER() {
        testInterface();
        BomberAircraft v = new BomberAircraft("BomberAircraft Name", "BomberAircraft Origin", 450, 7);
        v.setSpeed(500);
        assertEquals("Category for speed 500 is A", "HEAVIER with A", v.getAircraftCategoryBySpeed());
    }


    @Test
    public void test_getAircraftCategoryBySpeed_B_MEDIUM() {
        testInterface();
        BomberAircraft v = new BomberAircraft("mirage", "FRANCE", 0, 2);
        v.setSpeed(700);
        assertEquals("Category for speed 700 is B", "MEDIUM with B", v.getAircraftCategoryBySpeed());
        v.setSpeed(650);
        v.setPayload(8);
        assertEquals("Catogory for speed 650 is B", "HEAVIER with B", v.getAircraftCategoryBySpeed());

    }

    @Test
    public void test_getAircraftCategoryBySpeed_C_LIGHT() {
        testInterface();
        BomberAircraft v = new BomberAircraft("mirage", "FRANCE", 800, 1);
        v.setSpeed(850);
        assertEquals("Category for speed 850 is C", "LIGHTER with C", v.getAircraftCategoryBySpeed());
        v.setSpeed(600);
        assertEquals("Category for speed 600 is B", "LIGHTER with B", v.getAircraftCategoryBySpeed());
    }

    @Test
    public void test_toString() {
        testInterface();
        BomberAircraft v = new BomberAircraft("test", "TEST ORIGIN", 100, 1);
        assertEquals("Testing toString for bomber with Test name, TEST ORIGIN,100 speed and 1 payload",
                "Name: test Category: LIGHTER with A", v.toString());
    }

    @Test
    public void test_toString2() {
        testInterface();
        BomberAircraft v = new BomberAircraft("mirage", "FRANCE", 1000, 3);
        assertEquals("Testing toString for bomber with mirage name, FRANCE ORIGIN,100 speed and 3 payload",
                "Name: mirage Category: HEAVIER with C", v.toString());
    }
}
