package cpsc331.assignment1;

/**
 * This is the answer for Question 3, Assignment 1. The specification and algorithm of the "Extended Grindelwald Gaggle
 * Computation", as well as the the specification of the code can be found on
 * http://pages.cpsc.ucalgary.ca/~eberly/Courses/CPSC331/2019a/Assignments/A1/assignment_1.pdf
 * <p>
 * Author:  Haohu Shen
 * UCID:    30063099
 */

public class SGrindelwald {

    /**
     * This is an implementation of a recursive algorithm for the "Extended Grindelwald Gaggle Computation" Problem.
     * <p>
     * Precondition:    An integer n is given as input.
     * Postcondition:   The nth Grindelwald number, G_n, is returned as output, an IllegalArgumentException is thrown
     * otherwise.
     * Bound function:  f(n) = n
     *
     * @param n an integer
     * @return the nth Grindelwald number, G_n
     * @throws IllegalArgumentException
     */
    protected static int sGrin(int n) throws IllegalArgumentException {

        // Assertion: An integer n has been given as an argument of the method.
        if (n < 0) {
            // Assertion: A negative integer n has been given as an argument of the method.
            throw new IllegalArgumentException();
        } else if (n == 0) {
            // Assertion: A non-negative integer n has been given as an argument of the method.
            // Assertion: n == 0
            return 1;
        } else if (n == 1) {
            // Assertion: A non-negative integer n has been given as an argument of the method.
            // Assertion: n == 1
            return 2;
        } else if (n == 2) {
            // Assertion: A non-negative integer n has been given as an argument of the method.
            // Assertion: n == 2
            return 3;
        } else if (n == 3) {
            // Assertion: A non-negative integer n has been given as an argument of the method.
            // Assertion: n == 3
            return 4;
        } else if ((n % 2) == 0) {
            // Assertion: A non-negative integer n has been given as an argument of the method.
            // Assertion: (n % 2) == 0
            // Assertion: n >= 4
            return 2 * sGrin(n - 1) - 2 * sGrin(n - 3) + sGrin(n - 4);
        } else {
            // Assertion: A non-negative integer n has been given as an argument of the method.
            // Assertion: (n % 2) != 0
            // Assertion: n >= 4
            return sGrin(n - 1) + 3 * sGrin(n - 2) - 5 * sGrin(n - 3) + 2 * sGrin(n - 4);
        }
        // Assertion: A non-negative integer n has been given as an argument of the method.
        // Assertion: The nth Grindelwald Gaggle number was returned as output.
    }

    /**
     * The main method reads input from the command line, validates the input and passes the valid input to the
     * function 'sGrin'. A message will be shown if the input is invalid.
     *
     * @param args an array of strings from standard input
     */
    public static void main(String[] args) {

        // Assertion: An array of strings from standard input is passed as an argument of the 'main' method.
        if (args.length != 1) {
            // Assertion: The length of the array does not equal to 1
            System.out.println("Gadzooks! One integer input is required.");
            return;
        }

        // Assertion: The length of the array equals to 1.
        int n;
        try {
            n = Integer.parseInt(args[0]);
            // Assertion: The first element in the array can be converted to an 'int' data type.
        } catch (Exception e) {
            // Assertion: The first element in the array cannot be converted to an 'int' data type.
            System.out.println("Gadzooks! One integer input is required.");
            return;
        }

        // Assertion: n is an integer initialized from standard input.
        try {
            System.out.println(sGrin(n));
            // Assertion: n is a non-negative integer.
        } catch (IllegalArgumentException e) {
            // Assertion: n is a negative integer.
            System.out.println("Gadzooks! The integer input cannot be negative.");
        }
    }
}
