/**
 * This program simulates the same strategy as previous program but using
 * dynamic initial scores.
 *
 * @author Haohu Shen
 * @version 1.8.0_161, 09/24/2018
 */

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.math.BigInteger;

public class HaohuShen_p6 {

    // constants
    private static final int MIN_VAL_OF_A_DIE = 1;
    private static final int MAX_VAL_OF_A_DIE = 6;
    private static final int PIG_OUT = 1;
    private static final int MAX_SCORE = 100;
    private static final int MAX_SCORE_EACH_TURN = 20;

    public static void main(String[] args) {

        // initialization
        Scanner in = new Scanner(System.in);
        BigInteger input_score = new BigInteger("0");
        System.out.print("Enter current score: ");
        String temp_str = in.nextLine();

        // input validation
        try {
            input_score = input_score.add(new BigInteger(temp_str));
            if (input_score.compareTo(new BigInteger("0")) == -1) {
                throw new IllegalArgumentException();
            }
            if (!input_score.toString().equals(temp_str)) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            System.out.println("Invalid input! Program terminated.");
            System.exit(0);
        }

        // simulation

        // handle the case when input_score >= 100
        if (input_score.compareTo(new BigInteger("100")) >= 0) {
            //output
            System.out.println("Turn score = 0");
            System.out.println("New total score = " + input_score);
            return;
        }

        int total_score = input_score.intValue();
        int turn_score = 0;
        if (total_score < MAX_SCORE) {
            while (true) {
                int outcome = ThreadLocalRandom.current().nextInt(
                        MIN_VAL_OF_A_DIE, MAX_VAL_OF_A_DIE + 1);
                System.out.println(" - rolled a " + outcome);
                if (outcome == PIG_OUT) {
                    turn_score = 0;
                    System.out.println("Pigged out!");
                    break;
                }
                turn_score += outcome;
                if (turn_score + total_score >= MAX_SCORE) {
                    break;
                }
                if (turn_score >= MAX_SCORE_EACH_TURN) {
                    break;
                }
            }
        }

        // output
        int new_score = total_score + turn_score;
        System.out.println("Turn score = " + turn_score);
        System.out.println("New total score = " + new_score);
    }
}
