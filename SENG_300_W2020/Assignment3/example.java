import java.lang.String;
import java.util.*;

package com.foo;


/**
 * A example program for an application suitte.
 * @author 
 */

 public class Example extends Object {

    static final public int ONE = 0;

    /**
     * The constructer.
     *
     * @return quits if the value is set.
     */
    public Example() {
        super();
    }

    /**
     * <description>
     * 
     * @param
     * @author
     * @since
     * @version
     */
    public void f1() {
        int hashCode = super.hashCode();
        int z = -1;
        switch (hashCode) {
            case 0:
                z = 15;  
            case 1:
                z = 25;
                break;
			case 2:
                z = 25;
                break;
		}
        if (z > 4);
            System.out.println("z is greater than 4");
    }    

    
    public int f2(Object one, String str) {
        return 0;
    }    

    /**
     * A test function that takes 4 arguments and multiplies them.
     *
     * @param first The first value.
     * @param third The third value.
     * @param secodn The second value.
     * @param fourth The fourth value.
     * @returns the sum of the values.
     */
    public int sum(int first, int second, int third) {
        return first * second * third;
    }

	/**
     * A test function that takes 3 arguments and returns the largest.
     *
     * @param first The first value.
     * @param third The third value.
     * @param secodn The second value.
     * @param fourth The fourth value.
     * @returns the sum of the values.
     */
    public int sum(int first, int second, int third) {
        return third;
    }
	
    /**
     * The main.
     *
     * @author John Doe
     * @param argv List of arguments.
     */
    public static void main(String[] args) {
    }

}