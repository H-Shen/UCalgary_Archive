import org.junit.Test;

import static org.junit.Assert.*;

public class BallTest {
    // Testing constructors

    @Test
    public void test_Constructor_bounciness_Zero_GoodHeight() {
        Ball c = new Ball(0.00, 5);
        assertEquals("Created ball with invalid 0% bounciness", .50, c.getBounciness(), 0.000001);
        assertEquals("Created ball with valid 5 height.", 5, c.getHeight());
    }

    @Test
    public void test_Constructor_bounciness_100_GoodHeight() {
        Ball c = new Ball(1.0, 25);
        assertEquals("Created ball with invalid 100% bounciness", .50, c.getBounciness(), 0.000001);
        assertEquals("Created ball with valid 25 height.", 25, c.getHeight());
    }

    @Test
    public void test_Constructor_bounciness_99_TooLow() {
        Ball c = new Ball(.99, 0);
        assertEquals("Created ball with valid 99% bounciness", .99, c.getBounciness(), 0.000001);
        assertEquals("Created ball with invalid 0 height.", 1, c.getHeight());
    }

    @Test
    public void test_Constructor_bounciness_45_1() {
        Ball c = new Ball(.45, 1);
        assertEquals("Created ball with valid 45% bounciness", .45, c.getBounciness(), 0.000001);
        assertEquals("Created ball with 1 height.", 1, c.getHeight());
    }

    @Test
    public void test_CopyConstructor() {
        Ball c  = new Ball(.32, 5);
        Ball c2 = new Ball(c);
        assertEquals("Copied ball with 32% bounciness", .32, c2.getBounciness(), 0.000001);
        assertEquals("Created ball with 5.23 height.", 5, c2.getHeight());

    }

    @Test
    public void test_CopyConstructor2() {
        Ball c  = new Ball(.67, 187);
        Ball c2 = new Ball(c);
        assertEquals("Copied ball with 67% bounciness", .67, c2.getBounciness(), 0.000001);
        assertEquals("Created ball with 187.3256 height.", 187, c2.getHeight());

    }


    // Testing setter and getters

    @Test
    public void test_setter_and_getter_bounciness_0() {
        Ball c = new Ball(.60, 5);
        c.setBounciness(0);
        assertEquals("Set bounciness to invalid 0%", .50, c.getBounciness(), 0.000001);
    }

    @Test
    public void test_setter_and_getter_bounciness_1() {
        Ball c = new Ball(.60, 5);
        c.setBounciness(.01);
        assertEquals("Set bounciness to lowest valid: 1%", .01, c.getBounciness(), 0.000001);
    }

    @Test
    public void test_setter_and_getter_bounciness_99() {
        Ball c = new Ball(.60, 5);
        c.setBounciness(.99);
        assertEquals("Set bounciness to highest valid: 99%", .99, c.getBounciness(), 0.000001);
    }

    @Test
    public void test_setter_and_getter_bounciness_100() {
        Ball c = new Ball(.6, 5);
        c.setBounciness(100);
        assertEquals("Set bounciness to invalid 100%", .50, c.getBounciness(), 0.000001);
    }

    @Test
    public void test_setter_and_getter_height_zero() {
        Ball c = new Ball(.6, 5);
        c.setHeight(0);
        assertEquals("Set height to invalid zero.", 1, c.getHeight());
    }

    @Test
    public void test_setter_and_getter_height_negative() {
        Ball c = new Ball(.6, 5);
        c.setHeight(-10);
        assertEquals("Set height to invalid negative ten.", 1, c.getHeight());
    }

    @Test
    public void test_setter_and_getter_height_one() {
        Ball c = new Ball(.6, 5);
        c.setHeight(1);
        assertEquals("Set height to a 1.", 1, c.getHeight());
    }

    @Test
    public void test_setter_and_getter_height_12345point6789() {
        Ball c = new Ball(.6, 5);
        c.setHeight(12345);
        assertEquals("Set height to large number.", 12345, c.getHeight());
    }

    @Test
    public void test_bounce_fromHeight98() {
        Ball c = new Ball(.99, 98);
        c.bounce();

        assertEquals("Checking height after bounce (bounciness is 99%, height was 98)", 97, c.getHeight());
    }

    @Test
    public void test_bounce_fromHeight12345point456() {
        Ball c = new Ball(.10, 12345);
        c.bounce();

        assertEquals("Checking height after bounce (bounciness is 10%, height was 12345)", 1234, c.getHeight());
    }

    @Test
    public void test_bounce_fromHeight1() {
        Ball c = new Ball(.50, 1);
        c.bounce();

        assertEquals("Checking height after bounce (bounciness is 50%, height was 1)", 0, c.getHeight());
    }

    @Test
    public void test_numberOfBounces_OneBounce() {
        Ball c = new Ball(.50, 1);
        assertEquals("Ball with height 1 and 50% bounce will bounce once.", 1, c.numberOfBounces());
        assertEquals("Expected ball height to be unchanged after checking number of bounces.", 1, c.getHeight());
    }

    @Test
    public void test_numberOfBounces_ManyBounces() {
        Ball c = new Ball(.99, 12345);
        assertEquals("Ball with height 12345 and 99% bounce will bounce 538 times.", 538, c.numberOfBounces());
        assertEquals("Expected ball height to be unchanged after checking number of bounces.", 12345, c.getHeight());
    }

    @Test
    public void test_numberOfBounces_NoBounces() {
        Ball c = new Ball(.99, 1);
        c.bounce();

        assertEquals("Ball with height 0.0 (after first bounce) will bounce 0 times.", 0, c.numberOfBounces());
        assertEquals("Expected ball height to be unchanged after checking number of bounces.", 0, c.getHeight());
    }

    @Test
    public void test_equals_SameObject() {
        Ball c = new Ball(.99, 1);
        Ball b = c;

        assertEquals("Expect variables that reference the same object to be equal.", true, c.equals(b));
    }

    @Test
    public void test_equals_SameValues() {
        Ball c = new Ball(.65, 1234);
        Ball b = new Ball(.65, 1234);

        assertEquals("Expect balls with same height and bounciness to be equal.", true, c.equals(b));
    }

    @Test
    public void test_equals_DifferentHeightSameBounciness() {
        Ball c = new Ball(.45, 22);
        Ball b = new Ball(.45, 2);

        assertEquals("Expect balls with different height and same bounciness to be different.", false, c.equals(b));
    }

    @Test
    public void test_equals_DifferentBouncinessSameHeight() {
        Ball c = new Ball(.76, 987);
        Ball b = new Ball(.77, 987);

        assertEquals("Expect balls with same height and different bounciness to be different.", false, c.equals(b));
    }
}
