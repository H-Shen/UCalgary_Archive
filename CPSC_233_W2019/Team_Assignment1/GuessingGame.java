import java.util.Random;
import java.util.Scanner;

/**
 * The class provides static methods to run a guessing game. The computer or the user chooses a
 * random number between 1 and 20. The user continues providing guesses till they have guessed the
 * number. Any invalid input will be handled as an exception, or let the user know if their guess
 * was correct, too low or too high.
 *
 * @author Group 25
 */
public class GuessingGame {

    private static final int MAX_TRY   = 5;
    private static final int MAX_VALUE = 20;
    private static final int MIN_VALUE = 1;

    /**
     * This method checks if the input or the argument given by the user is a valid integer
     * and it is between 1 and 20. Invalid possible input includes:
     * <p>
     * 1. a string contains non-digit characters
     * 2. an empty string
     * 3. any other data types
     * 4. an integer which is out of range of int datatype
     * 5. an integer with redundant leading zeroes
     *
     * @param s this is the string input given by the user
     * @return true if the input can be converted a valid integer and false otherwise.
     */
    private static boolean isInputValid(String s) {
        boolean isValid = true;
        try {
            int guess = Integer.parseInt(s);
            if (s.compareTo(Integer.toString(guess)) != 0) {
                throw new IllegalArgumentException();
            }
            if (guess < MIN_VALUE || guess > MAX_VALUE) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            isValid = false;
        }
        return isValid;
    }

    /**
     * This method prompts the user for a guess.  It will only prompt the user ones.
     * The argument provided is the number the user is supposed to guess.
     * <p>
     * If the user enters a number too high (higher than numberToGuess) then print 'Too high'.  If
     * the user enters a number too low (lower than numberToGuess) then print 'Too low'.  If the
     * user guesses the number, then print 'You guessed it'.
     * <p>
     * The number to guess is assumed to be a number between 1 and 20 (but this method doesn't check
     * if numberToGuess provided is indeed between 1 and 20).  If the user enters a number that is
     * not between 1 and 20 (it is less than 1 or greater than 20) then the message to the user
     * will be 'Guess is not valid' and no other message is printed. Any invalid input given by the
     * user will cause a re-prompt.
     *
     * @param numberToGuess this is the number the user is required to guess.
     * @return true if the guess is correct and equal to numberToGuess and false otherwise.
     */
    public static boolean getAndCheckGuess(int numberToGuess) {

        Scanner reader = new Scanner(System.in);
        System.out.print("Enter your guess: ");
        String guessStr = reader.nextLine();

        // check the validity of the input, re-prompt if the user input is invalid.
        while (!isInputValid(guessStr)) {
            System.out.println("Guess is not valid");
            System.out.print("Enter your guess: ");
            guessStr = reader.nextLine();
        }

        int     guess        = Integer.parseInt(guessStr);
        boolean correctGuess = false;

        if (guess > numberToGuess) {
            System.out.println("Too high");
        } else if (guess < numberToGuess) {
            System.out.println("Too low");
        } else {
            System.out.println("You guessed it");
            correctGuess = true;
        }
        return correctGuess;
    }

    /**
     * When running this program, it will call getAndCheckGuess with a random number to guess.
     * If you want to test with a particular number to guess, provide the number to guess
     * as an argument to this program.  For example, if you want the numberToGuess to be 17,
     * run this program as: java GuessingGame 17
     * <p>
     * This program will call isInputValid to check the validity of input, then call
     * getAndCheckGuess and print the value returned by that method/function. If any invalid
     * argument is given, the program will throw an IllegalArgumentException and be terminated.
     *
     * @param args the command line argument provided. If one is provided it is assumed to be the
     *             number to guess in the game.
     */
    public static void main(String[] args) {

        try {
            int numToGuess;
            if (args.length == 0) {
                // nextInt will return a number between 0 and 20 (exclusive 20).  Adding 1 results
                // in a number between 1 and 20 (inclusive).
                Random rand = new Random();
                numToGuess = rand.nextInt(MAX_VALUE) + 1;
            } else if (args.length == 1 && isInputValid(args[0])) {
                // get the number provided as a command line argument and use it as the number to
                // guess for the game.
                numToGuess = Integer.parseInt(args[0]);
            } else {
                throw new IllegalArgumentException();
            }

            boolean hasAnswer = false;
            for (int i = 0; i < MAX_TRY; ++i) {
                if (getAndCheckGuess(numToGuess)) {
                    hasAnswer = true;
                    break;
                }
            }

            // output the result according to the fact the user guessed correctly or not.
            if (hasAnswer) {
                System.out.println("Well done!");
            } else {
                System.out.println("The number to guess was " + numToGuess);
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
