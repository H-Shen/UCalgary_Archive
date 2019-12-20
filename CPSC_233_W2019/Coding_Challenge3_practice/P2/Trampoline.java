import java.math.BigDecimal;

public class Trampoline extends Bouncer {

    private int weightOfJumper;

    public Trampoline(int weightOfJumper, double bouniciness, int height) {
        super(bouniciness, height);
        setWeightOfJumper(weightOfJumper);
    }

    public Trampoline(Trampoline other) {
        this(other.getWeightOfJumper(), other.getBounciness(),
                other.getHeight());
    }

    public int getWeightOfJumper() {
        return weightOfJumper;
    }

    public void setWeightOfJumper(int weight) {
        if (weight >= 50 && weight <= 300) {
            weightOfJumper = weight;
        } else {
            weightOfJumper = 140;
        }
    }

    @Override
    public double getBounciness() {

        BigDecimal temp;

        if (getWeightOfJumper() < 100) {
            temp = new BigDecimal(super.getBounciness() * 0.75);
        } else if (getWeightOfJumper() > 200) {
            temp = new BigDecimal(super.getBounciness() * 1.20);

        } else if (getWeightOfJumper() > 150) {
            temp = new BigDecimal(super.getBounciness() * 1.10);
        } else {
            return super.getBounciness();
        }

        if (temp.compareTo(BigDecimal.ONE) >= 0) {
            return 0.99;
        }
        return temp.doubleValue();
    }
}
