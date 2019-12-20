import org.junit.Test;

import static org.junit.Assert.*;

public class DateOfBirthTest {

    private final DateOfBirth testObj = new DateOfBirth("1910", "12", "25");

    @Test
    public void test_getYear_setYear_0() {
        DateOfBirth test = new DateOfBirth("1992", "01", "01");
        test.setYear(null);
        assertEquals(test.getYear(), "1992");
    }

    @Test
    public void test_getYear_setYear_1() {
        DateOfBirth test = new DateOfBirth("1992", "01", "01");
        test.setYear("1799");
        assertEquals(test.getYear(), "1992");
    }

    @Test
    public void test_getYear_setYear_2() {
        DateOfBirth test = new DateOfBirth("1992", "01", "01");
        test.setYear("01993");
        assertEquals(test.getYear(), "1992");
    }

    @Test
    public void test_getYear_setYear_3() {
        DateOfBirth test = new DateOfBirth("1992", "01", "01");
        test.setYear("1993");
        assertEquals(test.getYear(), "1993");
    }

    @Test
    public void test_getMonth_setMonth_0() {
        DateOfBirth test = new DateOfBirth("1992", "01", "01");
        test.setYear("1799");
        assertEquals(test.getYear(), "1992");
    }

    @Test
    public void test_getYear() {
        assertSame("The year of date of birth should be 1910: ", "1910", testObj.getYear());
    }

    @Test
    public void test_setYear() {
        testObj.setYear("1970");
        assertSame("The year of date of birth should set to 1970: ", "1970", testObj.getYear());
    }

    @Test
    public void test_setYear_1() {
        testObj.setYear("1899");
        assertNotSame("The year of date of birth should not be smaller than 1900: ", "1899", testObj.getYear());
    }

    @Test
    public void test_getMonth() {
        assertSame("The month of date of birth should be 12:", "12", testObj.getMonth());
    }

    @Test
    public void test_setMonth() {
        testObj.setMonth("10");
        assertSame("The month of date of birth should be set to 10:", "10", testObj.getMonth());
    }

    @Test
    public void test_setMonth_1() {
        testObj.setMonth("0");
        assertNotSame("The month cannot be less than 1: ", "0", testObj.getMonth());
    }

    @Test
    public void test_setMonth_2() {
        testObj.setMonth("14");
        assertNotSame("The month cannot be greater than 12: ", "14", testObj.getMonth());
    }

    @Test
    public void test_getDay() {
        assertSame("The day of date of birth should be 25:", "25", testObj.getDay());
    }

    @Test
    public void test_setDay() {
        testObj.setDay("17");
        assertSame("The day of date of birth should be set to 17:", "17", testObj.getDay());
    }

    @Test
    public void test_setDay_1() {
        testObj.setDay("35");
        assertNotSame("The Day cannot be greater than 31: ", "35", testObj.getDay());
    }

    @Test
    public void test_setDay_2() {
        testObj.setDay("0");
        assertNotSame("The Day cannot be less than 1: ", "35", testObj.getDay());
    }

    @Test
    public void test_toString() {
        assertEquals("Should display 1910-12-25:", "1910-12-25", testObj.toString());
    }
}