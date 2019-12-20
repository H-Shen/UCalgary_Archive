public class Circle {
    private Point centre;
    private int   radius;

    public Circle(Point centre, int radius) {
        this.centre = new Point(centre);
        this.radius = radius;
    }

    public double diameter() {
        return 2 * radius;
    }

    public double circumference() {
        return 2 * Math.PI * radius;
    }

    public void moveDown(int amount) {
        centre.moveDown(amount);
    }
}
