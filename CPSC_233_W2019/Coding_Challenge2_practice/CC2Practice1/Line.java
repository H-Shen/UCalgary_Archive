public class Line {

    private Point start;
    private Point end;

    public Line(Point start, Point end) {
        this.start = new Point(start);
        this.end = new Point(end);
    }

    public Point getStart() {
        return new Point(start);
    }

    public void setStart(Point start) {
        this.start = new Point(start);
    }

    public Point getEnd() {
        return new Point(end);
    }

    public void setEnd(Point end) {
        this.end = new Point(end);
    }

    public double length() {
        return start.distance(end);
    }

}
