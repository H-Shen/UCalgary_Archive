import org.junit.Test;

import static org.junit.Assert.*;

public class FighterAircraftTest extends FormatTester {

    public static final String CLASSNAME = "FighterAircraft";
    public static final String FILENAME  = CLASSNAME + ".java";

    public FighterAircraftTest() {
        super(CLASSNAME, false);
    }

    private void testInterface() {
        String[] instanceVars = {"char bvrTechnology"};
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
    public void test_Constructor_BVRTechnology_Y() {
        testInterface();

        FighterAircraft t = new FighterAircraft("mirage", "FRANCE", 300, 'Y');
        assertEquals("Created FighterAircraft with bvrTechnology Y - testing name", "mirage", t.getName());
        assertEquals("Created FighterAircraft with bvrTechnology Y - testing origin", "FRANCE", t.getOrigin());
        assertEquals("Created FighterAircraft with bvrTechnology Y - testing speed", 300, t.getSpeed());
        assertEquals("Created FighterAircraft with bvrTechnology Y - testing bvrTechnology", 'Y', t.getBVRTechnology());
    }


    @Test
    public void test_Constructor_BVRTechnology_N() {
        testInterface();

        FighterAircraft t = new FighterAircraft("mirage", "FRANCE", 300, 'N');
        assertEquals("Created FighterAircraft with bvrTechnology N - testing name", "mirage", t.getName());
        assertEquals("Created FighterAircraft with bvrTechnology N - testing origin", "FRANCE", t.getOrigin());
        assertEquals("Created FighterAircraft with bvrTechnology N - testing speed", 300, t.getSpeed());
        assertEquals("Created FighterAircraft with bvrTechnology N - testing bvrTechnology", 'N', t.getBVRTechnology());
    }

    @Test
    public void test_CopyConstructor() {
        FighterAircraft t  = new FighterAircraft("name1", "TEST1", 100, 'Y');
        FighterAircraft t2 = new FighterAircraft(t);
        assertEquals("Testing Copy Constructor, copying 'name1' name, 'TEST1' origin and speed 100 - testing name", "name1", t2.getName());
        assertEquals("Testing Copy Constructor, copying 'name1' name, 'TEST1' origin and speed 100 - testing origin", "TEST1", t2.getOrigin());
        assertEquals("Testing Copy Constructor, copying 'name1' name, 'TEST1' Copy Constructor' origin and speed 100 - testing speed", 100, t2.getSpeed());
        assertEquals("Testing Copy Constructor, bvrTechnology Y - testing bvrTechnology", 'Y', t2.getBVRTechnology());
    }

    // Testing setter and getters


    @Test
    public void test_setter_and_getter_bvrTechnology_Y() {
        testInterface();
        FighterAircraft t = new FighterAircraft("name", "ORIGIN", 500, 'N');
        t.setBVRTechnology('Y');
        assertEquals("Set bvrTechnology to Y from N.", 'Y', t.getBVRTechnology());
    }


    @Test
    public void test_setter_and_getter_bvrTechnology_N() {
        testInterface();
        FighterAircraft t = new FighterAircraft("name", "ORIGIN", 100, 'Y');
        t.setBVRTechnology('N');
        assertEquals("Set bvrTechnology to N that was initialized to Y.", 'N', t.getBVRTechnology());
    }

    @Test
    public void test_getCategory_Excellent() {
        testInterface();
        FighterAircraft t = new FighterAircraft("name", "ORIGIN", 100, 'Y');
        assertEquals("Set bvrTechnology Y", "EXCELLENT", t.getCategory());
    }

    @Test
    public void test_getCategory_Normal() {
        testInterface();
        FighterAircraft t = new FighterAircraft("name", "ORIGIN", 50, 'N');
        assertEquals("Set bvrTechnology N", "NORMAL", t.getCategory());
    }


    // Testing that methods in parents correctly invoke abstract/overridden method.


    @Test
    public void test_getAircraftCategoryBySpeed_A_EXCELLENT() {
        testInterface();
        FighterAircraft v = new FighterAircraft("FighterAircraft Name", "FighterAircraft Origin", 450, 'Y');
        v.setSpeed(500);
        assertEquals("Category for speed 500 is A", "EXCELLENT with A", v.getAircraftCategoryBySpeed());
    }

    @Test
    public void test_getAircraftCategoryBySpeed_B_NORMAL() {
        testInterface();
        FighterAircraft v = new FighterAircraft("mirage", "FRANCE", 0, 'N');
        v.setSpeed(700);
        assertEquals("Category for speed 700 is B", "NORMAL with B", v.getAircraftCategoryBySpeed());

    }

    @Test
    public void test_toString() {
        testInterface();
        FighterAircraft v = new FighterAircraft("test", "TEST ORIGIN", 100, 'Y');
        assertEquals("Testing toString for fighter with test name, TEST ORIGIN,100 speed and Y bvrTechnology",
                "Name: test Category: EXCELLENT with A", v.toString());
    }

    @Test
    public void test_toString2() {
        testInterface();
        FighterAircraft v = new FighterAircraft("mirage", "FRANCE", 1000, 'N');
        assertEquals("Testing toString for fighter with mirage name, FRANCE ORIGIN,100 speed and N bvrTechnology",
                "Name: mirage Category: NORMAL with C", v.toString());
    }

}
