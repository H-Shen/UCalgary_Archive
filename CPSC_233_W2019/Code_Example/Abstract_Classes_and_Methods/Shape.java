public abstract class Shape {
    private Point location;

    public Shape(Point aLocation) {
        location = aLocation;
    }

    protected Point getLocation() {
        return new Point(location);
    }

    public void moveDown(int amount) {
        location.moveDown(amount);
    }

    public abstract double circumference();

    public abstract double diameter();

    public String toString() {
        return location.toString() +
                "diameter: " + diameter();
    }

}
