public class Ball extends Bouncer {

    private int bounciness = 50;

    public Ball(int bounciness, double height) {
        super(height);
        setBounciness(bounciness);
    }

    public Ball(Ball toCopy) {
        super(toCopy);
        setBounciness(toCopy.getBounciness());
    }

    public int getBounciness() {
        return bounciness;
    }

    public void setBounciness(int bounciness) {
        if (bounciness > 0 && bounciness < 100) {
            this.bounciness = bounciness;
        } else {
            this.bounciness = 50;
        }
    }

    @Override
    public double heightAfterBounces(int numOfBounces) {
        double heightCopy = getHeight();
        for (int i = 0; i < numOfBounces; ++i) {
            heightCopy = heightCopy * getBounciness() / 100;
        }
        if (heightCopy < 1) {
            return 0;
        }
        return heightCopy;
    }

    @Override
    public String toString() {
        return "[Ball] " + super.toString() + " Bounciness: " + getBounciness() + "%";
    }
}
