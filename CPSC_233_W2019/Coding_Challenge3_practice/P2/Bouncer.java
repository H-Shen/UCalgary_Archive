import java.math.BigDecimal;

public class Bouncer {

    private int    height;
    private double bounciness;

    public Bouncer(double bounciness, int height) {
        if (height > 0) {
            this.height = height;
        } else {
            this.height = 1;
        }
        BigDecimal temp = new BigDecimal(bounciness);
        if (temp.compareTo(BigDecimal.ZERO) > 0 && temp.compareTo(BigDecimal.ONE) < 0) {
            this.bounciness = bounciness;
        } else {
            this.bounciness = 0.5;
        }
    }

    public Bouncer(Bouncer other) {
        this(other.getBounciness(), other.getHeight());
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if (height > 0) {
            this.height = height;
        } else {
            this.height = 1;
        }
    }

    public double getBounciness() {
        return bounciness;
    }

    public void setBounciness(double bounciness) {
        BigDecimal temp = new BigDecimal(bounciness);
        if (temp.compareTo(BigDecimal.ZERO) > 0 && temp.compareTo(BigDecimal.ONE) < 1) {
            this.bounciness = bounciness;
        } else {
            this.bounciness = 0.5;
        }
    }

    public void bounce() {
        height = (int) (getHeight() * getBounciness());
    }

    public int numberOfBounces() {
        int count       = 0;
        int height_copy = getHeight();
        while (height_copy > 0) {
            height_copy = (int) (height_copy * getBounciness());
            ++count;
        }
        return count;
    }

    public boolean equals(Bouncer other) {
        if (height == other.getHeight()) {
            if (new BigDecimal(bounciness).compareTo(new BigDecimal(other.getBounciness())) == 0) {
                return true;
            }
        }
        return false;
    }
}
