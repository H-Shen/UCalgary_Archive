/**
 * Class representing a point in a quadrant.  The x- and y-coordinates in the point
 * are integers.  A point can be moved up, down, left and right and you can inquire about
 * the quadrant the point is in.
 */
public class Point {
    public static final int MAX = 100;
    public static final int MIN = -100;

    int xcoord;
    int ycoord = 0;

    public Point() {
    }

    public Point(int x, int y) {
        setXCoord(x);
        setYCoord(y);
    }

    public Point(Point toCopy) {
        xcoord = toCopy.xcoord;
        ycoord = toCopy.ycoord;
    }

    public int getXCoord() {
        return xcoord;
    }

    /**
     * Sets the xcoordinate of this point to the provided value if it falls within the
     * MAX and MIN value set for points.
     *
     * @param xValue the value for the xcoordinate of this point.
     */
    public void setXCoord(int xValue) {
        if (xValue >= MIN && xValue <= MAX) {
            xcoord = xValue;
        }
    }

    public int getYCoord() {
        return ycoord;
    }

    /**
     * Sets the ycoordinate of this point to the provided value if it falls within the
     * MAX and MIN value set for points.
     *
     * @param yValue the value for the ycoordinate of this point.
     */
    public void setYCoord(int yValue) {
        if (yValue >= MIN && yValue <= MAX) {
            ycoord = yValue;
        }
    }

    /**
     * Move the point up by the specified amount.
     *
     * @param amount the number to add to the ycoord of this point.
     */
    public void moveUp(int amount) {
        setYCoord(ycoord + amount);
    }

    /**
     * Move the point down by the specified amount.
     *
     * @param amount the number to subtract from the ycoord of this point.
     */
    public void moveDown(int amount) {
        setYCoord(ycoord - amount);
    }

    /**
     * Move the point right (or east) the specified amount.
     *
     * @param amount the number to add to the xcoord of this point.
     */
    public void moveRight(int amount) {
        setXCoord(xcoord + amount);
    }

    /**
     * Move the point left (or west) by the specified amount.
     *
     * @param amount the number to subtract from the xcoord of this point.
     */
    public void moveLeft(int amount) {
        setXCoord(xcoord - amount);
    }

    /**
     * Computes and returns the euclidean distance between this point and the point provided
     * as an argument.
     *
     * @param other the other end point to compute the distance to.
     * @return the euclidean distance between this point and the one provided as an argument.
     */
    public double distance(Point other) {
        int    xdiff    = xcoord - other.xcoord;
        int    ydiff    = ycoord - other.ycoord;
        int    xsquare  = xdiff * xdiff;
        int    ysquare  = ydiff * ydiff;
        double distance = Math.sqrt(xsquare + ysquare);
        return distance;
    }

    /**
     * Checks if this point and the one provided as an argument represent
     * the same coordinate (ie have the same x- and y-coordinate).
     *
     * @param other the point to compare self with.
     * @return true if the other point is the same coordinate as this point.
     */
    public boolean equals(Point other) {
        return xcoord == other.xcoord && ycoord == other.ycoord;
    }

    /**
     * Creates a string representation of this point.  The string format
     * is (<x-coordinate>,<y-coordinate>).
     *
     * @return string representation of this point.
     */
    public String toString() {
        return "(" + xcoord + "," + ycoord + ")";
    }
}