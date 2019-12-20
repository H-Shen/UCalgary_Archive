import java.util.Scanner;

public class TwoCatchesDemo {
    public static void main(String[] args) {
        try {
            System.out.println("Enter number of widgets produced:");
            Scanner keyboard = new Scanner(System.in);
            int     widgets  = keyboard.nextInt();

            if (widgets < 0)
                throw new NegativeNumberException("widgets");

            System.out.println("How many were defective?");
            int defective = keyboard.nextInt();

            if (defective < 0)
                throw new NegativeNumberException("defective widgets");

            double ratio = exceptionalDivision(widgets, defective);
            System.out.println("One in every  " + ratio +
                    " widgets is defective.");
        } catch (DivideByZeroException e) {
            System.out.println("Congratulations! A perfect record!");
        } catch (NegativeNumberException e) {
            System.out.println("Cannot have a negative number of " +
                    e.getMessage());
        } catch (Exception e) {
            System.out.println("Fuck!");
        }


        System.out.println("End of program.");
    }

    public static double exceptionalDivision(double numerator, double denominator)
            throws DivideByZeroException {
        if (denominator == 0)
            throw new DivideByZeroException();
        return numerator / denominator;
    }
}
