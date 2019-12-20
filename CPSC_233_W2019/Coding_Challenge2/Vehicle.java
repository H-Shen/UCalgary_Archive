public class Vehicle {

    private static final int MILEAGE_UPPER_LIMIT = 25;
    private static final int MILEAGE_LOWER_LIMIT = 0;

    private String make;
    private String model;
    private int    mileage;

    public Vehicle(String make, String model, int mileage) {
        this.make = make;
        this.model = model;
        if (mileage >= MILEAGE_LOWER_LIMIT && mileage < MILEAGE_UPPER_LIMIT) {
            this.mileage = mileage;
        }
    }

    public Vehicle(Vehicle other) {
        this(other.getMake().toLowerCase(), other.getModel().toUpperCase(),
                other.getMileage());
    }

    public String getMake() {
        return make.toLowerCase();
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model.toUpperCase();
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        if (mileage >= MILEAGE_LOWER_LIMIT && mileage < MILEAGE_UPPER_LIMIT) {
            this.mileage = mileage;
        }
    }

    public String getFuelEfficiencyCategory() {
        if (mileage > 14) {
            return "best";
        }
        if (mileage > 8 && mileage < 15) {
            return "average";
        }
        return "worst";
    }

}
