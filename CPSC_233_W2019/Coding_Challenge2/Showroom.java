import java.util.ArrayList;

public class Showroom {

    private String             name;
    private ArrayList<Vehicle> vehicleList;

    Showroom(String name) {
        this.name = name;
        vehicleList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Vehicle> getVehicleList() {
        if (vehicleList == null) {
            return null;
        }
        ArrayList<Vehicle> result = new ArrayList<>();
        for (Vehicle i : vehicleList) {
            result.add(new Vehicle(i));
        }
        return result;
    }

    public void addVehicle(Vehicle a) {
        vehicleList.add(new Vehicle(a));
    }

    public Vehicle vehicleWithMinFuelEfficiency() {
        int lowMileage = 100;
        if (vehicleList == null) {
            return null;
        }
        for (Vehicle i : vehicleList) {
            if (i.getMileage() < lowMileage) {
                lowMileage = i.getMileage();
            }
        }
        for (Vehicle i : vehicleList) {
            if (i.getMileage() == lowMileage) {
                return new Vehicle(i);
            }
        }
        return null;
    }

    public double getAverageMileageByMake(String maker) {
        double result = 0.0;
        int    count  = 0;
        for (Vehicle i : vehicleList) {
            if (i.getMake().equals(maker)) {
                result += i.getMileage();
                ++count;
            }
        }
        if (count != 0) {
            return result / (double) count;
        }
        return result;
    }
}
