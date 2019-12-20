import org.junit.Test;

import static org.junit.Assert.*;

public class BouncerTestForEclipse extends FormatTester {

    public static final String CLASSNAME = "Bouncer";

    public BouncerTestForEclipse() {
        super(CLASSNAME, true);
    }

    private void testClassDefinition() {
        String[] instanceVars = {"int height", "double bounciness"};
        assertTrue("Instance variables should be private with correct name and type.", instanceVariablesArePrivate(instanceVars));

        assertTrue("Class should not have the default constructor.", noDefaultConstructor());
    }


    // Testing constructors

    @Test
    public void test_Constructor_bounciness_Zero_GoodHeight() {
        testClassDefinition();
        Bouncer c = new Bouncer(0.00, 5);
        assertEquals("Created bouncer with invalid 0% bounciness", .50, c.getBounciness(), 0.000001);
        assertEquals("Created bouncer with valid 5 height.", 5, c.getHeight());
    }

    @Test
    public void test_Constructor_bounciness_100_GoodHeight() {
        testClassDefinition();
        Bouncer c = new Bouncer(1.0, 25);
        assertEquals("Created bouncer with invalid 100% bounciness", .50, c.getBounciness(), 0.000001);
        assertEquals("Created bouncer with valid 25 height.", 25, c.getHeight());
    }

    @Test
    public void test_Constructor_bounciness_99_TooLow() {
        testClassDefinition();
        Bouncer c = new Bouncer(.99, 0);
        assertEquals("Created bouncer with valid 99% bounciness", .99, c.getBounciness(), 0.000001);
        assertEquals("Created bouncer with invalid 0 height.", 1, c.getHeight());
    }

    @Test
    public void test_Constructor_bounciness_45_1() {
        testClassDefinition();
        Bouncer c = new Bouncer(.45, 1);
        assertEquals("Created bouncer with valid 45% bounciness", .45, c.getBounciness(), 0.000001);
        assertEquals("Created bouncer with 1 height.", 1, c.getHeight());
    }

    @Test
    public void test_CopyConstructor() {
        testClassDefinition();
        Bouncer c  = new Bouncer(.32, 5);
        Bouncer c2 = new Bouncer(c);
        assertEquals("Copied bouncer with 32% bounciness", .32, c2.getBounciness(), 0.000001);
        assertEquals("Created bouncer with 5 height.", 5, c2.getHeight());

    }

    @Test
    public void test_CopyConstructor2() {
        testClassDefinition();
        Bouncer c  = new Bouncer(.67, 187);
        Bouncer c2 = new Bouncer(c);
        assertEquals("Copied bouncer with 67% bounciness", .67, c2.getBounciness(), 0.000001);
        assertEquals("Created bouncer with 187.3256 height.", 187, c2.getHeight());

    }


    // Testing setter and getters

    @Test
    public void test_setter_and_getter_bounciness_0() {
        testClassDefinition();
        Bouncer c = new Bouncer(.60, 5);
        c.setBounciness(0);
        assertEquals("Set bounciness to invalid 0%", .50, c.getBounciness(), 0.000001);
    }

    @Test
    public void test_setter_and_getter_bounciness_1() {
        testClassDefinition();
        Bouncer c = new Bouncer(.60, 5);
        c.setBounciness(.01);
        assertEquals("Set bounciness to lowest valid: 1%", .01, c.getBounciness(), 0.000001);
    }

    @Test
    public void test_setter_and_getter_bounciness_99() {
        testClassDefinition();
        Bouncer c = new Bouncer(.60, 5);
        c.setBounciness(.99);
        assertEquals("Set bounciness to highest valid: 99%", .99, c.getBounciness(), 0.000001);
    }

    @Test
    public void test_setter_and_getter_bounciness_100() {
        testClassDefinition();
        Bouncer c = new Bouncer(.6, 5);
        c.setBounciness(100);
        assertEquals("Set bounciness to invalid 100%", .50, c.getBounciness(), 0.000001);
    }

    @Test
    public void test_setter_and_getter_height_zero() {
        testClassDefinition();
        Bouncer c = new Bouncer(.6, 5);
        c.setHeight(0);
        assertEquals("Set height to invalid zero.", 1, c.getHeight());
    }

    @Test
    public void test_setter_and_getter_height_negative() {
        testClassDefinition();
        Bouncer c = new Bouncer(.6, 5);
        c.setHeight(-10);
        assertEquals("Set height to invalid negative ten.", 1, c.getHeight());
    }

    @Test
    public void test_setter_and_getter_height_one() {
        testClassDefinition();
        Bouncer c = new Bouncer(.6, 5);
        c.setHeight(1);
        assertEquals("Set height to a 1.", 1, c.getHeight());
    }

    @Test
    public void test_setter_and_getter_height_12345point6789() {
        testClassDefinition();
        Bouncer c = new Bouncer(.6, 5);
        c.setHeight(12345);
        assertEquals("Set height to large number.", 12345, c.getHeight());
    }

    @Test
    public void test_bounce_fromHeight98() {
        testClassDefinition();
        Bouncer c = new Bouncer(.99, 98);
        c.bounce();

        assertEquals("Checking height after bounce (bounciness is 99%, height was 98)", 97, c.getHeight());
    }

    @Test
    public void test_bounce_fromHeight12345point456() {
        testClassDefinition();
        Bouncer c = new Bouncer(.10, 12345);
        c.bounce();

        assertEquals("Checking height after bounce (bounciness is 10%, height was 12345)", 1234, c.getHeight());
    }

    @Test
    public void test_bounce_fromHeight1() {
        testClassDefinition();
        Bouncer c = new Bouncer(.50, 1);
        c.bounce();

        assertEquals("Checking height after bounce (bounciness is 50%, height was 1)", 0, c.getHeight());
    }

    @Test
    public void test_numberOfBounces_OneBounce() {
        testClassDefinition();
        Bouncer c = new Bouncer(.50, 1);
        assertEquals("bouncer with height 1 and 50% bounce will bounce once.", 1, c.numberOfBounces());
        assertEquals("Expected bouncer height to be unchanged after checking number of bounces.", 1, c.getHeight());
    }

    @Test
    public void test_numberOfBounces_ManyBounces() {
        testClassDefinition();
        Bouncer c = new Bouncer(.99, 12345);
        assertEquals("bouncer with height 12345 and 99% bounce will bounce 538 times.", 538, c.numberOfBounces());
        assertEquals("Expected bouncer height to be unchanged after checking number of bounces.", 12345, c.getHeight());
    }

    @Test
    public void test_numberOfBounces_NoBounces() {
        testClassDefinition();
        Bouncer c = new Bouncer(.99, 1);
        c.bounce();

        assertEquals("bouncer with height 0.0 (after first bounce) will bounce 0 times.", 0, c.numberOfBounces());
        assertEquals("Expected bouncer height to be unchanged after checking number of bounces.", 0, c.getHeight());
    }

    @Test
    public void test_equals_SameObject() {
        testClassDefinition();
        Bouncer c = new Bouncer(.99, 1);
        Bouncer b = c;

        assertEquals("Expect variables that reference the same object to be equal.", true, c.equals(b));
    }

    @Test
    public void test_equals_SameValues() {
        testClassDefinition();
        Bouncer c = new Bouncer(.65, 1234);
        Bouncer b = new Bouncer(.65, 1234);

        assertEquals("Expect bouncers with same height and bounciness to be equal.", true, c.equals(b));
    }

    @Test
    public void test_equals_DifferentHeightSameBounciness() {
        testClassDefinition();
        Bouncer c = new Bouncer(.45, 22);
        Bouncer b = new Bouncer(.45, 2);

        assertEquals("Expect bouncers with different height and same bounciness to be different.", false, c.equals(b));
    }

    @Test
    public void test_equals_DifferentBouncinessSameHeight() {
        testClassDefinition();
        Bouncer c = new Bouncer(.76, 987);
        Bouncer b = new Bouncer(.77, 987);

        assertEquals("Expect bouncers with same height and different bounciness to be different.", false, c.equals(b));
    }
}
