package cpsc331.assignment1;

/**
 * This is the answer for Question 13, Assignment 1. The specification and algorithm of the "Extended Grindelwald Gaggle
 * Computation", as well as the the specification of the code can be found on
 * http://pages.cpsc.ucalgary.ca/~eberly/Courses/CPSC331/2019a/Assignments/A1/assignment_1.pdf
 * <p>
 * Author:  Haohu Shen
 * UCID:    30063099
 */

public class FGrindelwald {

    /**
     * This is an implementation of another algorithm for the "Extended Grindelwald Gaggle Computation" Problem,
     * where the technique, Dynamic Programming, is used in the algorithm.
     * <p>
     * Precondition:    An integer n is given as input.
     * Postcondition:   The nth Grindelwald number, G_n, is returned as output.
     * An IllegalArgumentException is thrown otherwise.
     * <p>
     * Upperbound of running times T_{fGrin}(n):
     * T_{fGrin}(n) = 2    (if n = 0)
     * T_{fGrin}(n) = 3    (if n = 1)
     * T_{fGrin}(n) = 4    (if n = 2)
     * T_{fGrin}(n) = 5    (if n = 3)
     * T_{fGrin}(n) = 4*n  (if n >= 4)
     *
     * @param n an integer
     * @return the nth Grindelwald number, G_n
     * @throws IllegalArgumentException
     */
    protected static int fGrin(int n) throws IllegalArgumentException {

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
        } else {
            // Assertion: A non-negative integer n has been given as an argument of the method.
            // Assertion: n >= 4
            int[] G = new int[n + 1];
            G[0] = 1;
            G[1] = 2;
            G[2] = 3;
            G[3] = 4;
            int i = 4;
            while (i <= n) {

                // Assertions of the loop invariant:
                // 1. n is an input integer such that n >= 4
                // 2. G is a variable integer array and its length is n + 1
                // 3. i is an integer variable such that 4 <= i <= n + 1
                // 4. F[j] is the jth Grindelwald number for every integer j such that 0 <= j <= i - 1
                //
                // Bound function: f(n) = n - i + 1

                if ((i % 2) == 0) {
                    G[i] = 2 * G[i - 1] - 2 * G[i - 3] + G[i - 4];
                } else {
                    G[i] = G[i - 1] + 3 * G[i - 2] - 5 * G[i - 3] + 2 * G[i - 4];
                }
                i = i + 1;
            }
            return G[n];
        }
        // Assertion: A non-negative integer n has been given as an argument of the method.
        // Assertion: The nth Grindelwald Gaggle number was returned as output.
    }

    /**
     * The main method reads input from the command line, validates the input and passes the valid input to the
     * function 'fGrin'. A message will be shown if the input is invalid.
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
            System.out.println(fGrin(n));
            // Assertion: n is a non-negative integer.
        } catch (IllegalArgumentException e) {
            // Assertion: n is a negative integer.
            System.out.println("Gadzooks! The integer input cannot be negative.");
        }
    }
}
