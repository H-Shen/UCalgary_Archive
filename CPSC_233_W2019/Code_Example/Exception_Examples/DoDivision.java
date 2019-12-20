import java.util.Scanner;

public class DoDivision {
    private int    numerator;
    private int    denominator;
    private double quotient;

    public static void main(String[] args) {
        DoDivision doIt = new DoDivision();

        try {
            doIt.doNormalCase();
        } catch (DivideByZeroException e) {
            System.out.println(e.getMessage());
            doIt.giveSecondChance();
        }

        System.out.println("End of Program.");
    }

    public void doNormalCase() throws DivideByZeroException {
        System.out.println("Enter numerator:");
        Scanner keyboard = new Scanner(System.in);
        numerator = keyboard.nextInt();

        System.out.println("Enter denominator:");
        denominator = keyboard.nextInt();

        if (denominator == 0)
            throw new DivideByZeroException();

        quotient = numerator / (double) denominator;
        System.out.println(numerator + "/" + denominator +
                " = " + quotient);
    }

    public void giveSecondChance() {
        System.out.println("Try Again:");
        System.out.println("Enter numerator:");
        Scanner keyboard = new Scanner(System.in);

        numerator = keyboard.nextInt();
        System.out.println("Enter denominator:");
        System.out.println("Be sure the denominator is not zero.");
        denominator = keyboard.nextInt();

        if (denominator == 0) {
            System.out.println("I cannot do division by zero.");
            System.out.println("Since I cannot do what you want,");
            System.out.println("the program will now end.");
            System.exit(0);
        }

        quotient = ((double) numerator) / denominator;
        System.out.println(numerator + "/" + denominator +
                " = " + quotient);
    }
}







