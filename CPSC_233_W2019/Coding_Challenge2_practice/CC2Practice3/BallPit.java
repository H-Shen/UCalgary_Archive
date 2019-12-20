import java.util.ArrayList;

public class BallPit {

    private String          name;
    private ArrayList<Ball> ballList = new ArrayList<>();

    public BallPit(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Ball> getBallList() {
        ArrayList<Ball> result = new ArrayList<>();
        for (Ball i : ballList) {
            result.add(new Ball(i));
        }
        return result;
    }

    public void addBall(Ball ball) {
        ballList.add(new Ball(ball));
    }

    public Ball getBallWithMostBounces() {
        Ball result     = null;
        int  maxBounces = -1;
        for (Ball i : ballList) {
            if (i.numberOfBounces() > maxBounces) {
                maxBounces = i.numberOfBounces();
            }
        }
        for (Ball i : ballList) {
            if (i.numberOfBounces() == maxBounces) {
                return new Ball(i);
            }
        }
        return result;
    }
}
