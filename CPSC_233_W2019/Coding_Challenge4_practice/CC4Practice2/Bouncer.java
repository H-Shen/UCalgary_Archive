public abstract class Bouncer {

    private double height = 1.0;

    public Bouncer(double height) {
        setHeight(height);
    }

    public Bouncer(Bouncer toCopy) {
        setHeight(toCopy.getHeight());
    }

    protected double getHeight() {
        return height;
    }

    protected void setHeight(double height) {
        this.height = (height > 0) ? height : 1.0;
    }

    public void bounce() {
        this.height = heightAfterBounces(1);    // this.height could be <= 0
        //setHeight(heightAfterBounces(1));
    }

    public abstract double heightAfterBounces(int numOfBounces);

    public int numberOfBounces() {
        int    count       = 0;
        double height_copy = getHeight();
        while (height_copy >= 1) {
            ++count;
            height_copy = heightAfterBounces(count);
        }
        return count;
    }

    @Override
    public String toString() {
        return "Height: " + getHeight() + " Number of bounces: " + numberOfBounces();
    }
}
