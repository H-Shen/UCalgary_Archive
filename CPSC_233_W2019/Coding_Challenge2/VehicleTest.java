import org.junit.Test;

import static org.junit.Assert.*;

public class VehicleTest {

    // Testing constructors

    @Test
    public void test_Constructor_EmptyStringAnd0() {
        Vehicle v = new Vehicle("", "", 0);
        assertEquals("Created Vehicle with empty make, empty model and mileage 0 - testing make", "", v.getMake());
        assertEquals("Created Vehicle with empty make, empty model and mileage 0 - testing model", "", v.getModel());
        assertEquals("Created Vehicle with empty make, empty model and mileage 0 - testing mileage", 0, v.getMileage());
    }

    @Test
    public void test_Constructor_AllLowerCaseAnd10() {
        Vehicle v = new Vehicle("honda", "civic", 10);
        assertEquals("Created Vehicle with 'honda' make 'civic' model and mileage 10 - testing make", "honda", v.getMake());
        assertEquals("Created Vehicle with 'honda' make 'civic' model and mileage 10 - testing model", "CIVIC", v.getModel());
        assertEquals("Created Vehicle with 'honda' make, 'civic' model and mileage 10 - testing mileage", 10, v.getMileage());
    }

    @Test
    public void test_Constructor_AllUpperCaseAndNegative() {
        Vehicle v = new Vehicle("TOYOTA", "CAMRY", -1);
        assertEquals("Created Vehicle with 'TOYOTA' make, 'CAMRY' model and mileage -1 - testing make", "toyota", v.getMake());
        assertEquals("Created Vehicle with 'TOYOTA' make, 'CAMRY' model and mileage -1 - testing model", "CAMRY", v.getModel());
        assertEquals("Created Vehicle with 'TOYOTA' make, 'CAMRY' model and mileage -1 - testing mileage", 0, v.getMileage());
    }

    @Test
    public void test_Constructor_MixedCaseAndTooBig() {
        Vehicle v = new Vehicle("Toyota", "Corolla", 35);
        assertEquals("Created Vehicle with 'Toyota' make, 'Corolla' model and mileage 35 - testing model", "toyota", v.getMake());
        assertEquals("Created Vehicle with 'Toyota' make, 'Corolla' model and mileage 35 - testing model", "COROLLA", v.getModel());
        assertEquals("Created Vehicle with 'Toyota' make, 'Corolla' model and mileage 35 - testing mileage", 0, v.getMileage());
    }

    @Test
    public void test_CopyConstructor() {
        Vehicle v  = new Vehicle("make1", "TEST1", 10);
        Vehicle v2 = new Vehicle(v);
        assertEquals("Testing Copy Constructor, copying 'make1' make, 'TEST1' model and mileage 10 - testing make", "make1", v2.getMake());
        assertEquals("Testing Copy Constructor, copying 'make1' make, 'TEST1' model and mileage 10 - testing model", "TEST1", v2.getModel());
        assertEquals("Testing Copy Constructor, copying 'make1' make, 'TEST2' Copy Constructor' model and mileage 10 - testing mileage", 10, v2.getMileage());
    }

    @Test
    public void test_CopyConstructor2() {
        Vehicle v  = new Vehicle("MAKE2", "Test2", 20);
        Vehicle v2 = new Vehicle(v);
        assertEquals("Testing Copy Constructor, copying 'MAKE2' make 'Test2' model and mileage 20 - testing make", "make2", v2.getMake());
        assertEquals("Testing Copy Constructor, copying 'MAKE2' make 'Test2' model and mileage 20 - testing model", "TEST2", v2.getModel());
        assertEquals("Testing Copy Constructor, copying 'MAKE2' make, 'Test2' model and mileage 20 - testing mileage", 20, v2.getMileage());
    }


    // Testing setter and getters

    @Test
    public void test_setter_and_getter_model_emptyString() {
        Vehicle v = new Vehicle("Test", "TestEmptyString", 7);
        v.setModel("");
        assertEquals("Set model to empty string", "", v.getModel());
    }

    @Test
    public void test_setter_and_getter_model_allLowerCase() {
        Vehicle v = new Vehicle("Test", "TestAllLowerCase", 2);
        v.setModel("civic");
        assertEquals("Set model to 'civic'", "CIVIC", v.getModel());
    }

    @Test
    public void test_setter_and_getter_model_allUpperCase() {
        Vehicle v = new Vehicle("Test", "TestAllUpperCase", 14);
        v.setModel("CITY");
        assertEquals("Set model to 'CITY'", "CITY", v.getModel());
    }

    @Test
    public void test_setter_and_getter_model_MixedCase() {
        Vehicle v = new Vehicle("Test", "TestMixedCase", 10);
        v.setModel("Corolla");
        assertEquals("Set model to 'COROLLA'", "COROLLA", v.getModel());
    }

    @Test
    public void test_setter_and_getter_make_emptyString() {
        Vehicle v = new Vehicle("TestEmptyString", "TestEmptyString", 7);
        v.setMake("");
        assertEquals("Set make to empty string", "", v.getMake());
    }

    @Test
    public void test_setter_and_getter_make_allLowerCase() {
        Vehicle v = new Vehicle("TestAllLowerCase", "TestAllLowerCase", 2);
        v.setMake("ford");
        assertEquals("Set make to 'ford'", "ford", v.getMake());
    }

    @Test
    public void test_setter_and_getter_make_allUpperCase() {
        Vehicle v = new Vehicle("TestAllUpperCase", "TestAllUpperCase", 14);
        v.setMake("HYUNDAI");
        assertEquals("Set model to 'HYUNDAI'", "hyundai", v.getMake());
    }

    @Test
    public void test_setter_and_getter_make_MixedCase() {
        Vehicle v = new Vehicle("TestMixedCase", "TestMixedCase", 10);
        v.setMake("Volks Wagon");
        assertEquals("Set model to 'Volks Wagon'", "volks wagon", v.getMake());
    }

    @Test
    public void test_setter_and_getter_mileage_one() {
        Vehicle v = new Vehicle("testone", "TestOneMileage", 5);
        v.setMileage(1);
        assertEquals("Set mileage to one.", 1, v.getMileage());
    }

    @Test
    public void test_setter_and_getter_mileage_ten() {
        Vehicle v = new Vehicle("TestTen", "TestTenMileage", 17);
        v.setMileage(10);
        assertEquals("Set mileage to ten.", 10, v.getMileage());
    }

    @Test
    public void test_setter_and_getter_mileage_negative() {
        Vehicle v = new Vehicle("TestNegative", "TestNegativeMileage", 13);
        v.setMileage(-1);
        assertEquals("Set mileage to a negative number that was initialized to 13.", 13, v.getMileage());
    }

    @Test
    public void test_setter_and_getter_mileage_tooBig() {
        Vehicle v = new Vehicle("TestTooBig", "TestTooBigMileage", 12);
        v.setMileage(45);
        assertEquals("Set mileage to greater than 25 that was initialized to 12.", 12, v.getMileage());
    }

    @Test
    public void test_getFuelEfficiency_worst() {
        Vehicle v = new Vehicle("Fiat", "City", 0);
        v.setMileage(6);
        assertEquals("Efficiency for mileage 6 is worst", "worst", v.getFuelEfficiencyCategory());

    }

    @Test
    public void test_getFuelEfficiency_average() {
        Vehicle v = new Vehicle("Fiat", "City", 0);
        v.setMileage(13);
        assertEquals("Efficiency for mileage 13 is average", "average", v.getFuelEfficiencyCategory());
        v.setMileage(9);
        assertEquals("Efficiency for mileage 9 is average", "average", v.getFuelEfficiencyCategory());

    }

    @Test
    public void test_getFuelEfficiency_best() {
        Vehicle v = new Vehicle("Fiat", "City", 0);
        v.setMileage(20);
        assertEquals("Efficiency for mileage 20 is best", "best", v.getFuelEfficiencyCategory());
        v.setMileage(22);
        assertEquals("Efficiency for mileage 22 is best", "best", v.getFuelEfficiencyCategory());
    }

}
