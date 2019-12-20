public class Rectangle {
    private Point topLeft;
    private int   width;
    private int   height;

    public Rectangle(Point topLeft, int width, int height) {
        this.topLeft = new Point(topLeft);
        this.width = width;
        this.height = height;
    }

    public void moveDown(int amount) {
        topLeft.moveDown(amount);
    }

    public double circumference() {
        return 2 * width + 2 * height;
    }

    public double diameter() {
        Point bottomRight = new Point(topLeft.getXCoord() + width, topLeft.getYCoord() + height);
        return topLeft.distance(bottomRight);
    }
}
