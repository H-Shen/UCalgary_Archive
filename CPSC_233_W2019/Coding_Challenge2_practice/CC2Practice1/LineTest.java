import org.junit.Test;

import static org.junit.Assert.*;

public class LineTest {

    @Test
    public void test_length_StartIsTopLeft_EndIsBottomRight() {
        Line   line1  = new Line(new Point(1, 2), new Point(4, 6));
        double length = line1.length();

        assertEquals("Line from point (1,2) to (4,6) should be 5.0", 5.0, length, 0.00001);
    }

    @Test
    public void test_length_StartIsTopRight_EndIsBottomLeft() {
        Line   line1  = new Line(new Point(10, 2), new Point(4, 6));
        double length = line1.length();

        assertEquals("Line from point (10,2) to (4,6) should be 7.21110", 7.21110, length, 0.00001);
    }

    @Test
    public void test_length_StartIsBottomRight_EndIsTopLeft() {
        Line   line1  = new Line(new Point(10, 20), new Point(4, 6));
        double length = line1.length();

        assertEquals("Line from point (10,20) to (4,6) should be 15.2315462", 15.2315462, length, 0.00001);
    }

    @Test
    public void test_length_StartIsBottomLeft_EndIsTopRight() {
        Line   line1  = new Line(new Point(2, 20), new Point(4, 6));
        double length = line1.length();

        assertEquals("Line from point (2,20) to (4,6) should be 14.142135", 14.142135, length, 0.00001);
    }

    @Test
    public void test_PrivacyLeakConstructor_FirstPoint() {
        Point p1    = new Point(2, 20);
        Line  line1 = new Line(p1, new Point(4, 6));

        p1.moveDown(10);

        double length = line1.length();

        assertEquals("Point (2,20) passed in as argument to constructor.  Instance changed afterwards to (2,30) but this should not effect Line object.  Line from point (2,20) to (4,6) should be 14.142135", 14.142135, length, 0.00001);

    }

    @Test
    public void test_PrivacyLeakConstructor_SecondPoint() {
        Point p1    = new Point(2, 20);
        Line  line1 = new Line(new Point(4, 6), p1);

        p1.moveDown(10);

        double length = line1.length();

        assertEquals("Point (2,20) passed in as second argument to constructor.  Instance changed afterwards to (2,30) but this should not effect Line object.  Line from point (2,20) to (4,6) should be 14.142135", 14.142135, length, 0.00001);

    }

    @Test
    public void test_getStart_privacyLeak() {
        Line  line1 = new Line(new Point(20, 2), new Point(4, 6));
        Point p1    = line1.getStart();

        p1.moveDown(10);

        Point p2 = line1.getStart();


        boolean unchanged = p2.equals(new Point(20, 2));

        assertTrue("Was able to change start point of line directly via object returned from getter method.", unchanged);

    }

    @Test
    public void test_getEnd_privacyLeak() {
        Line  line1 = new Line(new Point(20, 2), new Point(3, 6));
        Point p1    = line1.getEnd();

        p1.moveDown(4);

        Point p2 = line1.getEnd();

        boolean unchanged = p2.equals(new Point(3, 6));

        assertTrue("Was able to change end point of line directly via object returned from getter method.", unchanged);

    }

    @Test
    public void test_setEnd_privacyLeak() {
        Line  line1 = new Line(new Point(20, 2), new Point(3, 6));
        Point p1    = new Point(1, 2);

        line1.setEnd(p1);

        p1.moveDown(4);

        Point p2 = line1.getEnd();
        System.out.println(p2);

        boolean unchanged = p2.equals(new Point(1, 2));

        assertTrue("Was able to change end point of line directly via object passed to setter method.", unchanged);
    }

    @Test
    public void test_setStart_privacyLeak() {
        Line  line1 = new Line(new Point(20, 2), new Point(3, 6));
        Point p1    = new Point(4, 10);

        line1.setStart(p1);

        p1.moveDown(4);

        Point p2 = line1.getStart();

        boolean unchanged = p2.equals(new Point(4, 10));

        assertTrue("Was able to change start point of line directly via object passed to setter method.", unchanged);

    }

}
