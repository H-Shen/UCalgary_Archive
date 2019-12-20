public class Circle extends Shape {
    private int radius;

    public Circle(Point centre, int radius) {
        super(centre);
        this.radius = radius;
    }

    public static void main(String[] args) {
        Circle c = new Circle(new Point(10, 20), 15);
        c.moveDown(20);
        System.out.println(c.diameter());

        System.out.println(c);

        Shape s = new Circle(new Point(10, 10), 20);
        System.out.println(s.diameter());
    }

    public double diameter() {
        return 2 * radius;
    }

    public double circumference() {
        return 2 * Math.PI * radius;
    }

    public void temp(Shape s) {

    }

}
