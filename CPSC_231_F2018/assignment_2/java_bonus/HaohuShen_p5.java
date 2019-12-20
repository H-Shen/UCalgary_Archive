/**
 * This program simulates a computer's turn of pig.
 * It will not stop rolling until accumulating 20 or more points in the turn.
 *
 * @author Haohu Shen
 * @version 1.8.0_161, 09/24/2018
 */

import java.util.concurrent.ThreadLocalRandom;

public class HaohuShen_p5 {

    // constants
    private static final int MIN_VAL_OF_A_DIE = 1;
    private static final int MAX_VAL_OF_A_DIE = 6;
    private static final int PIG_OUT = 1;
    private static final int MAX_SCORE = 20;

    public static void main(String[] args) {

        //simulation
        int turn_score = 0;
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
            if (turn_score >= MAX_SCORE) {
                break;
            }
        }

        // output
        System.out.println("Turn score = " + turn_score);
    }
}
