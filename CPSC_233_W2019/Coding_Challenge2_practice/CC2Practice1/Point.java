public class Point {

    private int xcoord;
    private int ycoord;

    public Point(int xcoord, int ycoord) {
        if (xcoord >= 0) {
            this.xcoord = xcoord;
        }
        if (ycoord >= 0) {
            this.ycoord = ycoord;
        }
    }

    public Point(Point other) {
        xcoord = other.getXCoord();
        ycoord = other.getYCoord();
    }

    public int getXCoord() {
        return xcoord;
    }

    public void setXCoord(int xcoord) {
        if (xcoord >= 0) {
            this.xcoord = xcoord;
        }
    }

    public int getYCoord() {
        return ycoord;
    }

    public void setYCoord(int ycoord) {
        if (ycoord >= 0) {
            this.ycoord = ycoord;
        }
    }

    public void moveUp(int dist) {
        ycoord -= dist;
        if (ycoord < 0) {
            ycoord = 0;
        }
    }

    public void moveDown(int dist) {
        ycoord += dist;
    }

    public void moveRight(int dist) {
        xcoord += dist;
    }

    public void moveLeft(int dist) {
        xcoord -= dist;
        if (xcoord < 0) {
            xcoord = 0;
        }
    }

    public double distance(Point other) {
        double temp =
                (xcoord - other.getXCoord()) * (xcoord - other.getXCoord())
                        + (ycoord - other.getYCoord()) * (ycoord - other.getYCoord());
        return Math.sqrt(temp);
    }

    public boolean equals(Point other) {
        return (xcoord == other.getXCoord() && ycoord == other.ycoord);
    }
}
