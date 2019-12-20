public class Trampoline extends Bouncer {

    private int weightOfJumper = 140;

    public Trampoline(int weightOfJumper, double height) {
        super(height);
        setWeightOfJumper(weightOfJumper);
    }

    public Trampoline(Trampoline toCopy) {
        super(toCopy);
        setWeightOfJumper(toCopy.getWeightOfJumper());
    }

    public int getWeightOfJumper() {
        return weightOfJumper;
    }

    public void setWeightOfJumper(int weight) {
        if (weight >= 50 && weight <= 300) {
            this.weightOfJumper = weight;
        } else {
            this.weightOfJumper = 140;
        }
    }

    @Override
    public double heightAfterBounces(int numOfBounces) {
        double heightCopy = getHeight();
        for (int i = 0; i < numOfBounces; ++i) {
            double bounciness = (getWeightOfJumper() + heightCopy) / (3.5 * heightCopy);
            heightCopy = heightCopy * bounciness / 100.0;
        }
        if (heightCopy < 1) {
            return 0;
        }
        return heightCopy;
    }

    @Override
    public String toString() {
        return "[Trampoline] " + super.toString();
    }
}
