import org.junit.Test;

import static org.junit.Assert.*;

public class BouncerTest extends FormatTester {

    public static final String CLASSNAME = "Bouncer";

    public BouncerTest() {
        super(CLASSNAME, false);
    }

    private void testClassDefinition() {
        String[] instanceVars = {"double height"};
        assertTrue("Instance variables should be private with correct name and type.", instanceVariablesArePrivate(instanceVars));

        assertTrue("Class should not have the default constructor.", noDefaultConstructor());

        String[] abstractMethods = {"double heightAfterBounces(int numOfBounces)"};
        assertTrue("Class should have abstract method heightAfterBounces that returns a double (minimize whitespace in signature).", hasRequiredAbstractMethods(abstractMethods));

        String[] protectedMethods = {"void setHeight", "double getHeight"};
        assertTrue("Class should have protected method setHeight and getHeight.", hasRequiredProtectedMethods(protectedMethods));

    }

    @Test
    public void test_Constructor_GoodHeight() {
        testClassDefinition();
        Bouncer c = new BouncerMock(25.31);
        assertEquals("Created bouncer with valid 25.31 height.", 25.31, c.getHeight(), 0.000001);
    }


    // Testing constructors

    @Test
    public void test_Constructor_TooLow() {
        testClassDefinition();
        Bouncer c = new BouncerMock(0.0);
        assertEquals("Created bouncer with invalid 0.0 height.", 1.0, c.getHeight(), 0.000001);
    }

    @Test
    public void test_Constructor_SmallestHeight() {
        testClassDefinition();
        Bouncer c = new BouncerMock(1.0);
        assertEquals("Created bouncer with 1.0 height.", 1.0, c.getHeight(), 0.000001);
    }

    @Test
    public void test_CopyConstructor() {
        testClassDefinition();
        BouncerMock c  = new BouncerMock(5.23);
        Bouncer     c2 = new BouncerMock(c);
        assertEquals("Created bouncer with 5.23 height.", 5.23, c2.getHeight(), 0.000001);

    }

    @Test
    public void test_setter_and_getter_height_zero() {
        testClassDefinition();
        Bouncer c = new BouncerMock(5.0);
        c.setHeight(0.0);
        assertEquals("Set height to invalid zero.", 1.0, c.getHeight(), 0.0000001);
    }

// Testing setter and getters

    @Test
    public void test_setter_and_getter_height_negative() {
        testClassDefinition();
        Bouncer c = new BouncerMock(5.0);
        c.setHeight(-10.0);
        assertEquals("Set height to invalid negative ten.", 1.0, c.getHeight(), 0.0000001);
    }

    @Test
    public void test_setter_and_getter_height_one() {
        testClassDefinition();
        Bouncer c = new BouncerMock(5.0);
        c.setHeight(1.0);
        assertEquals("Set height to a 1.0.", 1.0, c.getHeight(), 0.0000001);
    }

    @Test
    public void test_setter_and_getter_height_12345point6789() {
        testClassDefinition();
        Bouncer c = new BouncerMock(5.0);
        c.setHeight(12345.6789);
        assertEquals("Set height to large number.", 12345.6789, c.getHeight(), 0.0000001);
    }

    @Test
    public void test_bounce_fromHeight98() {
        testClassDefinition();
        BouncerMock c = new BouncerMock(98.0);
        c.heightAfterBounces[1] = 97.02;
        c.bounce();

        assertEquals("Bounce updated height to value returned by heightAfterBounces(1) -> 97.02", 97.02, c.getHeight(), 0.0000001);
    }


    // Test bounce

    @Test
    public void test_bounce_fromHeight12345point456() {
        testClassDefinition();
        BouncerMock c = new BouncerMock(12345.456);
        c.heightAfterBounces[1] = 1234.5456;
        c.bounce();

        assertEquals("Bounce updated height to value returned by heightAfterBounces(1) -> 1234.5456", 1234.5456, c.getHeight(), 0.0000001);
    }

    @Test
    public void test_numberOfBounces_OneBounce() {
        testClassDefinition();
        BouncerMock c = new BouncerMock(1.0);
        c.heightAfterBounces[1] = 0.0;
        assertEquals("Height is 0 after first bounce, so will bounce once.", 1, c.numberOfBounces());
        assertEquals("Expected bouncer height to be unchanged after checking number of bounces.", 1.0, c.getHeight(), 0.0000001);
    }


    // Number of bounces

    @Test
    public void test_numberOfBounces_ManyBounces() {
        testClassDefinition();
        BouncerMock c = new BouncerMock(12345.678);
        c.heightAfterBounces[1] = 1234.5678;
        c.heightAfterBounces[2] = 123.45678;
        c.heightAfterBounces[3] = 121.0;
        c.heightAfterBounces[4] = 100.0;
        c.heightAfterBounces[5] = 50.0;
        c.heightAfterBounces[6] = 1.145;
        c.heightAfterBounces[7] = 1.013;
        c.heightAfterBounces[8] = 1.0;
        c.heightAfterBounces[9] = 0.0;

        assertEquals("heightAfterBounces 1 to 8 return number >= 1, and heightAfterBounce 9 return 0.", 9, c.numberOfBounces());
        assertEquals("Expected bouncer height to be unchanged after checking number of bounces.", 12345.678, c.getHeight(), 0.0000001);
    }

    @Test
    public void test_numberOfBounces_NoBounces() {
        testClassDefinition();
        BouncerMock c = new BouncerMock(1.0);
        c.heightAfterBounces[1] = 0.0;
        c.bounce();

        assertEquals("Bouncer with height 0.0 (after first bounce) will bounce 0 times.", 0, c.numberOfBounces());
        assertEquals("Expected bouncer height to be unchanged after checking number of bounces.", 0.0, c.getHeight(), 0.0000001);
    }

    @Test
    public void test_toString() {
        testClassDefinition();
        BouncerMock c = new BouncerMock(12345.678);
        c.heightAfterBounces[1] = 1234.5678;
        c.heightAfterBounces[2] = 123.45678;
        c.heightAfterBounces[3] = 121.0;
        c.heightAfterBounces[4] = 100.0;
        c.heightAfterBounces[5] = 50.0;
        c.heightAfterBounces[6] = 1.145;
        c.heightAfterBounces[7] = 1.013;
        c.heightAfterBounces[8] = 1.0;
        c.heightAfterBounces[9] = 0.0;
        assertEquals("height: 12345.678 and 9 bounces", "Height: 12345.678 Number of bounces: 9", c.toString());
    }

    // ToString

    public class BouncerMock extends Bouncer {
        double[] heightAfterBounces = new double[10];

        public BouncerMock(double height) {
            super(height);
        }

        public BouncerMock(BouncerMock c) {
            super(c);
        }

        public double heightAfterBounces(int numOfBounces) {
            return heightAfterBounces[numOfBounces];
        }
    }
}
