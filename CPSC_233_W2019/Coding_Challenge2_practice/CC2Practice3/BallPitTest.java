import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BallPitTest {

    @Test
    public void test_ConstructorAndGetter() {
        BallPit c = new BallPit("Test Constructor and Getter");
        assertEquals("Testing constructor and getter", "Test Constructor and Getter", c.getName());
    }

    @Test
    public void test_addBall_addingOne() {
        BallPit c = new BallPit("Test");
        Ball    m = new Ball(.45, 2);
        c.addBall(m);
        ArrayList<Ball> list = c.getBallList();
        Ball            m2   = null;

        if (list.size() > 0) {
            m2 = list.get(0);
        }


        assertEquals("BallPit only has one ball (.45,2) - testing size.", 1, list.size());
        assertEquals("BallPit only has one ball (.45,2) - testing bounciness.", .45, m2.getBounciness(), 0.000001);
        assertEquals("BallPit only has one ball (.45,2)- testing height.", 2, m2.getHeight());
    }

    @Test
    public void test_addBall_addingMany() {
        BallPit c  = new BallPit("Test");
        Ball    m1 = new Ball(.45, 1);
        Ball    m2 = new Ball(.50, 2);
        Ball    m3 = new Ball(.55, 3);
        Ball    m4 = new Ball(.65, 5);
        Ball    m5 = new Ball(.76, 10);
        Ball    m6 = new Ball(.32, 3);
        c.addBall(m1);
        c.addBall(m2);
        c.addBall(m3);
        c.addBall(m4);
        c.addBall(m5);
        c.addBall(m6);

        ArrayList<Ball> list = c.getBallList();

        assertEquals("Expected list of size 6 after adding 6 balls", 6, list.size());
        assertEquals("Ball 1 test - testing bounciness", .45, list.get(0).getBounciness(), 0.000001);
        assertEquals("Ball 2 test - testing bounciness", .50, list.get(1).getBounciness(), 0.000001);
        assertEquals("Ball 3 test - testing bounciness", .55, list.get(2).getBounciness(), 0.000001);
        assertEquals("Ball 4 test - testing bounciness", .65, list.get(3).getBounciness(), 0.000001);
        assertEquals("Ball 5 test - testing bounciness", .76, list.get(4).getBounciness(), 0.000001);
        assertEquals("Ball 6 test - testing bounciness", .32, list.get(5).getBounciness(), 0.000001);
    }

    @Test
    public void test_addBall_addingOne_EncapsulationTest() {
        BallPit c = new BallPit("Test");
        Ball    m = new Ball(.45, 5);
        c.addBall(m);
        m.setBounciness(99);
        ArrayList<Ball> list = c.getBallList();
        Ball            m2   = null;

        if (list.size() > 0) {
            m2 = list.get(0);
        }

        assertEquals("BallPit only has one ball (.45,5)- testing encapsulation (changed bounciness of original).", .45, m2.getBounciness(), 0.000001);
    }

    @Test
    public void test_getBallList_Encapsulation() {
        BallPit c  = new BallPit("Test");
        Ball    m1 = new Ball(.45, 1);
        Ball    m2 = new Ball(.50, 2);
        Ball    m3 = new Ball(.55, 3);
        Ball    m4 = new Ball(.65, 5);
        Ball    m5 = new Ball(.76, 10);
        Ball    m6 = new Ball(.32, 3);
        c.addBall(m1);
        c.addBall(m2);
        c.addBall(m3);
        c.addBall(m4);
        c.addBall(m5);
        c.addBall(m6);

        ArrayList<Ball> list = c.getBallList();

        list.get(0).setBounciness(.01);
        list.get(1).setBounciness(.02);
        list.get(2).setBounciness(.03);
        list.get(3).setBounciness(.04);
        list.get(4).setBounciness(.05);
        list.get(5).setBounciness(.06);

        list = c.getBallList();


        assertEquals("Ball 1 test - testing bounciness", .45, list.get(0).getBounciness(), 0.000001);
        assertEquals("Ball 2 test - testing bounciness", .50, list.get(1).getBounciness(), 0.000001);
        assertEquals("Ball 3 test - testing bounciness", .55, list.get(2).getBounciness(), 0.000001);
        assertEquals("Ball 4 test - testing bounciness", .65, list.get(3).getBounciness(), 0.000001);
        assertEquals("Ball 5 test - testing bounciness", .76, list.get(4).getBounciness(), 0.000001);
        assertEquals("Ball 6 test - testing bounciness", .32, list.get(5).getBounciness(), 0.000001);

    }

    @Test
    public void test_GetBallWithMostBounces_emptyList() {
        BallPit c = new BallPit("test");
        assertEquals("No balls added to list.", null, c.getBallWithMostBounces());
    }

    @Test
    public void test_GetBallWithMostBounces_OneBallInBallPit() {
        BallPit c = new BallPit("test");
        Ball    m = new Ball(.50, 5);
        c.addBall(m);
        Ball mostBounces = c.getBallWithMostBounces();
        assertEquals("BallPit only has one ball (.50,5) - testing bounciness.", .50, mostBounces.getBounciness(), 0.000001);
        assertEquals("BallPit only has one ball (.50.5)- testing height.", 5, mostBounces.getHeight());
    }

    @Test
    public void test_GetBallWithMostBounces_ListHasTwoBallsWithSameNumOfBounces() {
        BallPit c  = new BallPit("test");
        Ball    m1 = new Ball(.50, 2);
        Ball    m2 = new Ball(.25, 4);
        c.addBall(m1);
        c.addBall(m2);

        Ball mostBounces = c.getBallWithMostBounces();

        assertEquals("BallPit only has two balls with same number of bounces, expected to get first (.50,2) - testing bounciness.", .50, mostBounces.getBounciness(), 0.000001);
        assertEquals("BallPit only has two balls with same height, expected to get first added (.50,2) - testing height.", 2, mostBounces.getHeight());
    }

    @Test
    public void test_GetBallWithMostBounces_highestInMiddle() {
        BallPit c  = new BallPit("test");
        Ball    m1 = new Ball(.75, 2); // 3 bounces
        Ball    m2 = new Ball(.66, 5); // 4 bounces
        Ball    m3 = new Ball(.99, 1); // 1 bounce
        c.addBall(m1);
        c.addBall(m2);
        c.addBall(m3);

        Ball mostBounces = c.getBallWithMostBounces();

        assertEquals("BallPit has three balls with most bounces in middle (.66,2) - testing bounciness.", .66, mostBounces.getBounciness(), 0.000001);
        assertEquals("BallPit has three balls with mostBounces height in middle (.66,2)- testing height.", 5, mostBounces.getHeight());
    }

    @Test
    public void test_GetBallWithMostBounces_LastHasMost() {
        BallPit c  = new BallPit("test");
        Ball    m1 = new Ball(.99, 1);
        Ball    m2 = new Ball(.50, 2);
        Ball    m3 = new Ball(.55, 3);
        Ball    m4 = new Ball(.65, 5);
        Ball    m5 = new Ball(.76, 10);
        c.addBall(m1);
        c.addBall(m2);
        Ball m6 = new Ball(.98, 9);
        c.addBall(m3);
        c.addBall(m4);
        c.addBall(m5);
        c.addBall(m6);

        Ball mostBounces = c.getBallWithMostBounces();

        assertEquals("BallPit has three balls with mostBounces height at end (.98,9) - testing bounciness.", .98, mostBounces.getBounciness(), 0.000001);
        assertEquals("BallPit has three balls with mostBounces height at end (.98,9)- testing height.", 9, mostBounces.getHeight());

        mostBounces.setBounciness(.33);
        Ball b = c.getBallList().get(5);

        assertEquals("BallPit has three balls with mostBounces height at end (.98,9)- testing encapsulation.", .98, b.getBounciness(), 0.000001);
    }

}
