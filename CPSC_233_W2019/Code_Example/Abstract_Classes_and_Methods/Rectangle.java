public class Rectangle extends Shape {
    private int width;
    private int height;

    public Rectangle(Point topLeft, int width, int height) {
        super(topLeft);
        this.width = width;
        this.height = height;
    }

    public double circumference() {
        return 2 * width + 2 * height;
    }

    public double diameter() {
        Point topLeft     = getLocation();
        Point bottomRight = new Point(topLeft.getXCoord() + width, topLeft.getYCoord() + height);
        return topLeft.distance(bottomRight);
    }
}
