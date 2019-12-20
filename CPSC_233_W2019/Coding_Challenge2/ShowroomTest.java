import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class ShowroomTest {

    @Test
    public void test_ConstructorAndGetter() {
        Showroom s = new Showroom("Test Constructor and Getter");
        assertEquals("Testing constructor and getter", "Test Constructor and Getter", s.getName());
    }

    @Test
    public void test_addVehicle_addingOne() {
        Showroom s = new Showroom("Test");
        Vehicle  v = new Vehicle("make", "TEST", 10);
        s.addVehicle(v);
        ArrayList<Vehicle> list = s.getVehicleList();
        Vehicle            v2   = null;

        if (list.size() > 0) {
            v2 = list.get(0);
        }

        assertEquals("Showroom only has one Vehicle ('Test',10) - testing size.", 1, list.size());
        assertEquals("Showroom only has one Vehicle ('Test',10) - testing model.", "TEST", v2.getModel());
        assertEquals("Showroom only has one Vehicle ('Test',10)- testing mileage.", 10, v2.getMileage());
    }

    @Test
    public void test_addVehicle_addingMany() {
        Showroom s  = new Showroom("Test");
        Vehicle  v1 = new Vehicle("make", "VEHICLE1", 7);
        Vehicle  v2 = new Vehicle("make", "VEHICLE2", 7);
        Vehicle  v3 = new Vehicle("make", "VEHICLE3", 16);
        Vehicle  v4 = new Vehicle("make", "VEHICLE4", 10);
        Vehicle  v5 = new Vehicle("make", "VEHICLE5", 9);
        Vehicle  v6 = new Vehicle("make", "VEHICLE6", 18);
        s.addVehicle(v1);
        s.addVehicle(v2);
        s.addVehicle(v3);
        s.addVehicle(v4);
        s.addVehicle(v5);
        s.addVehicle(v6);

        ArrayList<Vehicle> list = s.getVehicleList();

        assertEquals("Expected list of size 6 after adding 6 Vehicles", 6, list.size());
        assertEquals("Vehicle 1 test - testing model", "VEHICLE1", list.get(0).getModel());
        assertEquals("Vehicle 2 test - testing mode2", "VEHICLE2", list.get(1).getModel());
        assertEquals("Vehicle 3 test - testing mode3", "VEHICLE3", list.get(2).getModel());
        assertEquals("Vehicle 4 test - testing mode4", "VEHICLE4", list.get(3).getModel());
        assertEquals("Vehicle 5 test - testing mode5", "VEHICLE5", list.get(4).getModel());
        assertEquals("Vehicle 6 test - testing mode6", "VEHICLE6", list.get(5).getModel());
    }

    @Test
    public void test_addVehicle_addingOne_EncapsulationTest() {
        Showroom s = new Showroom("Test");
        Vehicle  v = new Vehicle("make", "TEST", 9);
        s.addVehicle(v);
        v.setModel("Changed Model");
        ArrayList<Vehicle> list = s.getVehicleList();
        Vehicle            v2   = null;

        if (list.size() > 0) {
            v2 = list.get(0);
        }

        assertEquals("Showroom only has one Vehicle ('Test',5)- testing encapsulation (changed model of original).", "TEST", v2.getModel());
    }

    @Test
    public void test_getVehicleList_EncapsulationChangeVehiclesInList() {
        Showroom s  = new Showroom("Test");
        Vehicle  v1 = new Vehicle("make", "VEHICLE1", 7);
        Vehicle  v2 = new Vehicle("make", "VEHICLE2", 17);
        Vehicle  v3 = new Vehicle("make", "VEHICLE3", 15);
        Vehicle  v4 = new Vehicle("make", "VEHICLE4", 10);
        Vehicle  v5 = new Vehicle("make", "VEHICLE5", 9);
        Vehicle  v6 = new Vehicle("make", "VEHICLE6", 20);
        s.addVehicle(v1);
        s.addVehicle(v2);
        s.addVehicle(v3);
        s.addVehicle(v4);
        s.addVehicle(v5);
        s.addVehicle(v6);

        ArrayList<Vehicle> list = s.getVehicleList();

        list.get(0).setModel("Changed1");
        list.get(1).setModel("Changed2");
        list.get(2).setModel("Changed3");
        list.get(3).setModel("Changed4");
        list.get(4).setModel("Changed5");
        list.get(5).setModel("Changed6");

        list = s.getVehicleList();


        assertEquals("Vehicle 1 test - testing model", "VEHICLE1", list.get(0).getModel());
        assertEquals("Vehicle 2 test - testing mode2", "VEHICLE2", list.get(1).getModel());
        assertEquals("Vehicle 3 test - testing mode3", "VEHICLE3", list.get(2).getModel());
        assertEquals("Vehicle 4 test - testing mode4", "VEHICLE4", list.get(3).getModel());
        assertEquals("Vehicle 5 test - testing mode5", "VEHICLE5", list.get(4).getModel());
        assertEquals("Vehicle 6 test - testing mode6", "VEHICLE6", list.get(5).getModel());

    }

    @Test
    public void test_getVehicleList_EncapsulationChangeReturnedList() {
        Showroom s  = new Showroom("Test");
        Vehicle  v1 = new Vehicle("make", "VEHICLE1", 7);
        Vehicle  v2 = new Vehicle("make", "VEHICLE2", 17);
        Vehicle  v3 = new Vehicle("make", "VEHICLE3", 15);
        Vehicle  v4 = new Vehicle("make", "VEHICLE4", 10);
        Vehicle  v5 = new Vehicle("make", "VEHICLE5", 9);
        Vehicle  v6 = new Vehicle("make", "VEHICLE6", 20);
        s.addVehicle(v1);
        s.addVehicle(v2);
        s.addVehicle(v3);
        s.addVehicle(v4);
        s.addVehicle(v5);
        s.addVehicle(v6);

        ArrayList<Vehicle> list = s.getVehicleList();
        list.add(new Vehicle("encapsulation", "encapsulation", 7));

        assertEquals("Should not be able to add to showroom by getting vehicle list", 6, s.getVehicleList().size());
    }

    @Test
    public void test_vehicleWithMinFuelEfficiency_emptyList() {
        Showroom s = new Showroom("test");
        assertEquals("No vehicles added to list.", null, s.vehicleWithMinFuelEfficiency());
    }

    @Test
    public void test_vehicleWithMinFuelEfficiency_OneVehicleInShowroom() {
        Showroom s = new Showroom("test");
        Vehicle  v = new Vehicle("make", "TEST1", 10);
        s.addVehicle(v);
        Vehicle lowest = s.vehicleWithMinFuelEfficiency();
        assertEquals("Showroom only has one Vehicle ('Test1',10) - testing model.", "TEST1", lowest.getModel());
        assertEquals("Showroom only has one Vehicle ('Test1',10)- testing mileage.", 10, lowest.getMileage());
    }

    @Test
    public void test_vehicleWithMinFuelEfficiency_ListHasTwoVehiclesWithSameMileage() {
        Showroom s  = new Showroom("test");
        Vehicle  v1 = new Vehicle("make", "VEHICLE1", 13);
        Vehicle  v2 = new Vehicle("make", "VEHICLE2", 13);
        s.addVehicle(v1);
        s.addVehicle(v2);

        Vehicle lowest = s.vehicleWithMinFuelEfficiency();

        assertEquals("Showroom only has two Vehicles with same mileage, expected to get first added ('Vehicle1') - testing model.", "VEHICLE1", lowest.getModel());
        assertEquals("Showroom only has two Vehicles with same mileage, expected to get first added ('Vehicle1')- testing mileage.", 13, lowest.getMileage());

    }

    @Test
    public void test_vehicleWithMinFuelEfficiency_LowestInMiddle() {
        Showroom s  = new Showroom("test");
        Vehicle  v1 = new Vehicle("make", "VEHICLE1", 13);
        Vehicle  v2 = new Vehicle("make", "VEHICLE2", 11);
        Vehicle  v3 = new Vehicle("make", "VEHICLE3", 12);
        s.addVehicle(v1);
        s.addVehicle(v2);
        s.addVehicle(v3);

        Vehicle lowest = s.vehicleWithMinFuelEfficiency();


        assertEquals("Showroom has three Vehicles with lowest mileage in middle ('Vehicle2',11) - testing model.", "VEHICLE2", lowest.getModel());
        assertEquals("Showroom has three Vehicles with lowest mileage in middle ('Vehicle2',11)- testing mileage.", 11, lowest.getMileage());
        assertFalse("Showroom has three Vehicles with lowest mileage in middle ('Vehicle2',11)- testing encapsulation.", lowest == v2);
    }

    @Test
    public void test_vehicleWithMinFuelEfficiency_LastIsLowestAndEncapsulation() {
        Showroom s  = new Showroom("test");
        Vehicle  v1 = new Vehicle("make", "VEHICLE1", 17);
        Vehicle  v2 = new Vehicle("make", "VEHICLE2", 18);
        Vehicle  v3 = new Vehicle("make", "VEHICLE3", 15);
        Vehicle  v4 = new Vehicle("make", "VEHICLE4", 14);
        Vehicle  v5 = new Vehicle("make", "VEHICLE5", 19);
        Vehicle  v6 = new Vehicle("make", "VEHICLE6", 10);
        s.addVehicle(v1);
        s.addVehicle(v2);
        s.addVehicle(v3);
        s.addVehicle(v4);
        s.addVehicle(v5);
        s.addVehicle(v6);

        Vehicle lowest = s.vehicleWithMinFuelEfficiency();

        assertEquals("Showroom has six Vehicles with lowest mileage at end ('Vehicle6',10) - testing model.", "VEHICLE6", lowest.getModel());
        assertEquals("Showroom has six Vehicles with lowest mileage at end ('Vehicle6',10)- testing mileage.", 10, lowest.getMileage());

        lowest.setModel("Changed");
        Vehicle b = s.getVehicleList().get(5);

        assertEquals("Showroom has six Vehicles with lowest mileage at end ('Vehicle6',10)- testing encapsulation.", "VEHICLE6", b.getModel());
    }

    // testing averageMileageByMake
    @Test
    public void test_averageMileageByMake_emptyList() {
        Showroom s = new Showroom("test");
        assertEquals("No vehicles added to list.", 0.0, s.getAverageMileageByMake("test"), 0.000001);
    }

    @Test
    public void test_averageMileageByMake_OneVehicleInShowroomSameMake() {
        Showroom s               = new Showroom("test");
        Vehicle  v               = new Vehicle("test make", "TEST1", 10);
        double   expectedAverage = 10.0;

        s.addVehicle(v);

        double actualAverage = s.getAverageMileageByMake("test make");

        assertEquals("Showroom only has one Vehicle ('test make', 'Test1',10), asking for average of make 'test make'", expectedAverage, actualAverage, 0.000001);
    }

    @Test
    public void test_averageMileageByMake_OneVehicleInShowroomDifferentMake() {
        Showroom s               = new Showroom("test");
        Vehicle  v               = new Vehicle("wrong make", "TEST1", 10);
        double   expectedAverage = 0.0;

        s.addVehicle(v);

        double actualAverage = s.getAverageMileageByMake("test make");

        assertEquals("Showroom only has one Vehicle ('wrong make', 'Test1',10), asking for average of make 'test make'", expectedAverage, actualAverage, 0.000001);
    }

    @Test
    public void test_averageMileageByMake_ManyInList() {
        Showroom s               = new Showroom("test");
        double   expectedAverage = 13.25;

        Vehicle v1  = new Vehicle("make1", "VEHICLE1", 17);
        Vehicle v2  = new Vehicle("make2", "VEHICLE2", 18);
        Vehicle v3  = new Vehicle("make2", "VEHICLE3", 15);
        Vehicle v4  = new Vehicle("make1", "VEHICLE4", 14);
        Vehicle v5  = new Vehicle("make3", "VEHICLE5", 19);
        Vehicle v6  = new Vehicle("make3", "VEHICLE6", 10);
        Vehicle v7  = new Vehicle("make3", "VEHICLE6", 5);
        Vehicle v8  = new Vehicle("make1", "VEHICLE6", 9);
        Vehicle v9  = new Vehicle("make2", "VEHICLE6", 25);
        Vehicle v10 = new Vehicle("make2", "VEHICLE6", 20);
        s.addVehicle(v1);
        s.addVehicle(v2);
        s.addVehicle(v3);
        s.addVehicle(v4);
        s.addVehicle(v5);
        s.addVehicle(v6);
        s.addVehicle(v7);
        s.addVehicle(v8);
        s.addVehicle(v9);
        s.addVehicle(v10);


        double actualAverage = s.getAverageMileageByMake("make2");

        assertEquals("Showroom only has many Vehicle some of correct make.  Fuel efficiency of correct make: 18,15,25,20', expecte,10), asking for average of make 'test make'", expectedAverage, actualAverage, 0.000001);
    }

    // testing getVehiclesByCategoryAndMake

}
