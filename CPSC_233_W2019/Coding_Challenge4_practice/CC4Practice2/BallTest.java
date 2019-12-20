import org.junit.Test;

import static org.junit.Assert.*;

public class BallTest extends FormatTester {
    public static final String CLASSNAME = "Ball";

    public BallTest() {
        super(CLASSNAME, false);
    }

    private void testClassDefinition() {
        String[] instanceVars = {"int bounciness"};
        assertTrue("Instance variables should be private with correct name and type.", instanceVariablesArePrivate(instanceVars));

        assertTrue("Class should not have the default constructor.", noDefaultConstructor());

        assertFalse("Should not override bounce.", hasMethod("void bounce()"));
        assertFalse("Should not override numberOfBounces.", hasMethod("int numberOfBounces()"));
        assertFalse("Should not override (or call) setHeight.", hasMethod("setHeight"));
    }


    // Testing constructors

    @Test
    public void test_Constructor_bounciness_Zero_GoodHeight() {
        testClassDefinition();
        Ball c = new Ball(0, 5.0);
        assertEquals("Created ball with invalid 0% bounciness", 50, c.getBounciness());
        assertEquals("Created ball with valid 5.0 height.", 5.0, c.getHeight(), 0.000001);
    }

    @Test
    public void test_Constructor_bounciness_100_GoodHeight() {
        testClassDefinition();
        Ball c = new Ball(100, 25.31);
        assertEquals("Created ball with invalid 100% bounciness", 50, c.getBounciness());
        assertEquals("Created ball with valid 25.31 height.", 25.31, c.getHeight(), 0.000001);
    }

    @Test
    public void test_Constructor_bounciness_99_TooLow() {
        testClassDefinition();
        Ball c = new Ball(99, 0.0);
        assertEquals("Created ball with valid 99% bounciness", 99, c.getBounciness());
        assertEquals("Created ball with invalid 0.0 height.", 1.0, c.getHeight(), 0.000001);
    }

    @Test
    public void test_CopyConstructor() {
        testClassDefinition();
        Ball c  = new Ball(32, 5.23);
        Ball c2 = new Ball(c);
        assertEquals("Copied ball with 32% bounciness", 32, c2.getBounciness());
        assertEquals("Created ball with 5.23 height.", 5.23, c2.getHeight(), 0.000001);
    }

    @Test
    public void test_CopyConstructor2() {
        testClassDefinition();
        Ball c  = new Ball(67, 187.3256);
        Ball c2 = new Ball(c);
        assertEquals("Copied ball with 67% bounciness", 67, c2.getBounciness());
        assertEquals("Created ball with 187.3256 height.", 187.3256, c2.getHeight(), 0.000001);
    }


// Testing setter and getters

    @Test
    public void test_setter_and_getter_bounciness_0() {
        testClassDefinition();
        Ball c = new Ball(60, 5.0);
        c.setBounciness(0);
        assertEquals("Set bounciness to invalid 0%", 50, c.getBounciness());
    }

    @Test
    public void test_setter_and_getter_bounciness_1() {
        testClassDefinition();
        Ball c = new Ball(60, 5.0);
        c.setBounciness(1);
        assertEquals("Set bounciness to lowest valid: 1%", 1, c.getBounciness());
    }

    @Test
    public void test_setter_and_getter_bounciness_99() {
        testClassDefinition();
        Ball c = new Ball(60, 5.0);
        c.setBounciness(99);
        assertEquals("Set bounciness to highest valid: 99%", 99, c.getBounciness());
    }

    @Test
    public void test_setter_and_getter_bounciness_100() {
        testClassDefinition();
        Ball c = new Ball(60, 5.0);
        c.setBounciness(100);
        assertEquals("Set bounciness to invalid 100%", 50, c.getBounciness());
    }

    @Test
    public void test_bounce_fromHeight98() {
        testClassDefinition();
        Ball c = new Ball(99, 98.0);
        c.bounce();

        assertEquals("Checking height after bounce (bounciness is 99%, height was 98.0)", 97.02, c.getHeight(), 0.0000001);
    }

    @Test
    public void test_bounce_fromHeight12345point456() {
        testClassDefinition();
        Ball c = new Ball(10, 12345.456);
        c.bounce();

        assertEquals("Checking height after bounce (bounciness is 10%, height was 12345.456)", 1234.5456, c.getHeight(), 0.0000001);
    }

    @Test
    public void test_bounce_fromHeight1() {
        testClassDefinition();
        Ball c = new Ball(50, 1.0);
        c.bounce();

        assertEquals("Checking height after bounce (bounciness is 50%, height was 1.0)", 0.0, c.getHeight(), 0.0000001);
    }

    @Test
    public void test_numberOfBounces_OneBounce() {
        testClassDefinition();
        Ball c = new Ball(50, 1.0);
        assertEquals("Ball with height 1.0 and 50% bounce will bounce once.", 1, c.numberOfBounces());
        assertEquals("Expected ball height to be unchanged after checking number of bounces.", 1.0, c.getHeight(), 0.0000001);
    }

    @Test
    public void test_numberOfBounces_ManyBounces() {
        testClassDefinition();
        Ball c = new Ball(99, 12345.678);
        assertEquals("Ball with height 12345.678 and 99% bounce will bounce 938 times.", 938, c.numberOfBounces());
        assertEquals("Expected ball height to be unchanged after checking number of bounces.", 12345.678, c.getHeight(), 0.0000001);
    }

    @Test
    public void test_numberOfBounces_NoBounces() {
        testClassDefinition();
        Ball c = new Ball(99, 1.0);
        c.bounce();

        assertEquals("Ball with height 0.0 (after first bounce) will bounce 0 times.", 0, c.numberOfBounces());
        assertEquals("Expected ball height to be unchanged after checking number of bounces.", 0.0, c.getHeight(), 0.0000001);
    }

    // ToString

    @Test
    public void test_toString() {
        testClassDefinition();
        assertTrue("Should override toString and it should invoke parent toString (not getter methods in parent).", toStrInvokesParentToStr());


        Ball c = new Ball(99, 12345.678);

        assertEquals("height: 12345.678 and 99% bounciness, 938 bounces", "[Ball] Height: 12345.678 Number of bounces: 938 Bounciness: 99%", c.toString());
    }
}
