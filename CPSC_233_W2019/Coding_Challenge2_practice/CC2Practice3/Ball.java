import java.math.BigDecimal;

public class Ball {

    private static final double DEFAULT_BOUNCINESS = 0.5;
    private static final double UPPER_BOUNCINESS   = 1.0;
    private static final double LOWER_BOUNCINESS   = 0.0;
    private static final int    DEFAULT_HEIGHT     = 1;
    private static final int    LOWER_HEIGHT       = 0;
    private              double bounciness;
    private              int    height;

    public Ball(double bounciness, int height) {
        if (isBouncinessValid(bounciness)) {
            this.bounciness = bounciness;
        } else {
            this.bounciness = DEFAULT_BOUNCINESS;
        }

        if (isHeightValid(height)) {
            this.height = height;
        } else {
            this.height = DEFAULT_HEIGHT;
        }
    }

    public Ball(Ball other) {
        this.bounciness = other.bounciness;
        this.height = other.height;
    }

    // helper func to make sure the double value is between 0-1 (exclusive)
    private boolean isBouncinessValid(double bounciness) {
        if (new BigDecimal(bounciness).compareTo(new BigDecimal(LOWER_BOUNCINESS)) > 0) {
            if (new BigDecimal(bounciness).compareTo(new BigDecimal(UPPER_BOUNCINESS)) < 0) {
                return true;
            }
        }
        return false;
    }

    // helper func to make sure the height is greater than 0
    private boolean isHeightValid(int height) {
        return (height > LOWER_HEIGHT);
    }

    public double getBounciness() {
        return bounciness;
    }

    public void setBounciness(double bounciness) {
        if (isBouncinessValid(bounciness)) {
            this.bounciness = bounciness;
        } else {
            this.bounciness = DEFAULT_BOUNCINESS;
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        if (isHeightValid(height)) {
            this.height = height;
        } else {
            this.height = DEFAULT_HEIGHT;
        }
    }

    public void bounce() {
        height = (int) (height * bounciness);
    }

    public int numberOfBounces() {
        int heightCopy = height;
        int result     = 0;
        while (heightCopy > 0) {
            heightCopy = (int) (heightCopy * bounciness);
            ++result;
        }
        return result;
    }

    public boolean equals(Ball other) {
        if (new BigDecimal(other.getBounciness()).equals(new BigDecimal(getBounciness()))) {
            if (other.getHeight() == height) {
                return true;
            }
        }
        return false;
    }
}
