import org.junit.Test;

import static org.junit.Assert.*;

public class TrampolineTest extends FormatTester {
    public static final String CLASSNAME = "Trampoline";

    public TrampolineTest() {
        super(CLASSNAME, false);
    }

    private void testClassDefinition() {
        String[] instanceVars = {"int weightOfJumper"};
        assertTrue("Instance variables should be private with correct name and type.", instanceVariablesArePrivate(instanceVars));

        assertTrue("Class should not have the default constructor.", noDefaultConstructor());

        assertFalse("Should not override bounce.", hasMethod("void bounce()"));
        assertFalse("Should not override numberOfBounces.", hasMethod("int numberOfBounces()"));
        assertFalse("Should not override (or call) setHeight.", hasMethod("setHeight"));
    }

    // Testing constructors

    @Test
    public void test_Constructor_weight_49_GoodHeight() {
        testClassDefinition();
        Trampoline c = new Trampoline(49, 5.0);
        assertEquals("Created trampoline with invalid weight 49", 140, c.getWeightOfJumper());
        assertEquals("Created trampoline with valid 5.0 height.", 5.0, c.getHeight(), 0.000001);
    }

    @Test
    public void test_Constructor_weight_301_GoodHeight() {
        testClassDefinition();
        Trampoline c = new Trampoline(301, 25.31);
        assertEquals("Created trampoline with invalid 301 weight", 140, c.getWeightOfJumper());
        assertEquals("Created trampoline with valid 25.31 height.", 25.31, c.getHeight(), 0.000001);
    }

    @Test
    public void test_Constructor_weight_99_TooLow() {
        testClassDefinition();
        Trampoline c = new Trampoline(99, 0.0);
        assertEquals("Created trampoline with valid 99 weight", 99, c.getWeightOfJumper());
        assertEquals("Created trampoline with invalid 0.0 height.", 1.0, c.getHeight(), 0.000001);
    }

    @Test
    public void test_CopyConstructor() {
        testClassDefinition();
        Trampoline c  = new Trampoline(300, 5.23);
        Trampoline c2 = new Trampoline(c);
        assertEquals("Copied trampoline with 300 weight", 300, c2.getWeightOfJumper());
        assertEquals("Created trampoline with 5.23 height.", 5.23, c2.getHeight(), 0.000001);
    }

    @Test
    public void test_CopyConstructor2() {
        testClassDefinition();
        Trampoline c  = new Trampoline(50, 187.3256);
        Trampoline c2 = new Trampoline(c);
        assertEquals("Copied trampoline with 50 weight", 50, c2.getWeightOfJumper());
        assertEquals("Created trampoline with 187.3256 height.", 187.3256, c2.getHeight(), 0.000001);
    }


// Testing setter and getters

    @Test
    public void test_setter_and_getter_weight_0() {
        testClassDefinition();
        Trampoline c = new Trampoline(60, 5.0);
        c.setWeightOfJumper(0);
        assertEquals("Set weight to invalid 0%", 140, c.getWeightOfJumper());
    }

    @Test
    public void test_setter_and_getter_weight_50() {
        testClassDefinition();
        Trampoline c = new Trampoline(60, 5.0);
        c.setWeightOfJumper(50);
        assertEquals("Set weight to lowest valid: 50", 50, c.getWeightOfJumper());
    }

    @Test
    public void test_setter_and_getter_weight_99() {
        testClassDefinition();
        Trampoline c = new Trampoline(60, 5.0);
        c.setWeightOfJumper(300);
        assertEquals("Set weight to highest valid: 300", 300, c.getWeightOfJumper());
    }

    @Test
    public void test_setter_and_getter_weight_301() {
        testClassDefinition();
        Trampoline c = new Trampoline(60, 5.0);
        c.setWeightOfJumper(301);
        assertEquals("Set weight to invalid 301", 140, c.getWeightOfJumper());
    }

    @Test
    public void test_bounce_fromHeight98() {
        testClassDefinition();
        Trampoline c = new Trampoline(300, 98.0);
        c.bounce();

        assertEquals("Checking height after bounce (weight is 300, height was 98.0)", 1.1371428, c.getHeight(), 0.0000001);
    }

    @Test
    public void test_bounce_fromHeight12345point456() {
        testClassDefinition();
        Trampoline c = new Trampoline(50, 12345.456);
        c.bounce();

        assertEquals("Checking height after bounce (weight is 50, height was 12345.456)", 35.41558857, c.getHeight(), 0.0000001);
    }

    @Test
    public void test_bounce_fromHeight1() {
        testClassDefinition();
        Trampoline c = new Trampoline(50, 1.0);
        c.bounce();

        assertEquals("Checking height after bounce (weight is 50, height was 1.0)", 0.0, c.getHeight(), 0.0000001);
    }

    @Test
    public void test_numberOfBounces_OneBounce() {
        testClassDefinition();
        Trampoline c = new Trampoline(50, 1.0);
        assertEquals("Trampoline with height 1.0 and jumper weight 50 bounce will bounce once.", 1, c.numberOfBounces());
        assertEquals("Expected trampoline height to be unchanged after checking number of bounces.", 1.0, c.getHeight(), 0.0000001);
    }

    @Test
    public void test_numberOfBounces_ManyBounces() {
        testClassDefinition();
        Trampoline c = new Trampoline(300, 9456789.0);
        assertEquals("Trampoline with height 9456789.0 and jumper weight 300 will bounce 4 times.", 4, c.numberOfBounces());
        assertEquals("Expected trampoline height to be unchanged after checking number of bounces.", 9456789.0, c.getHeight(), 0.0000001);
    }

    @Test
    public void test_numberOfBounces_NoBounces() {
        testClassDefinition();
        Trampoline c = new Trampoline(99, 1.0);
        c.bounce();

        assertEquals("Trampoline with height 0.0 (after first bounce) will bounce 0 times.", 0, c.numberOfBounces());
        assertEquals("Expected trampoline height to be unchanged after checking number of bounces.", 0.0, c.getHeight(), 0.0000001);
    }

    // ToString

    @Test
    public void test_toString() {
        testClassDefinition();
        assertTrue("Should override toString and it should invoke parent toString (not getter methods in parent).", toStrInvokesParentToStr());

        Trampoline c = new Trampoline(99, 12345.678);

        assertEquals("height: 12345.678 and 99 weight", "[Trampoline] Height: 12345.678 Number of bounces: 2", c.toString());
    }
}
