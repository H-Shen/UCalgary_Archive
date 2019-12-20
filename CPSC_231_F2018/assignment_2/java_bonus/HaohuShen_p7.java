/**
 * This program simulates a full one player game by computer, starts from 0
 * total score and plays turn by turn until it reaches 100 points.
 *
 * @author Haohu Shen
 * @version 1.8.0_161, 09/24/2018
 */

import java.util.concurrent.ThreadLocalRandom;

public class HaohuShen_p7 {

    // constants
    private static final int MIN_VAL_OF_A_DIE = 1;
    private static final int MAX_VAL_OF_A_DIE = 6;
    private static final int PIG_OUT = 1;
    private static final int MAX_SCORE = 100;
    private static final int MAX_SCORE_EACH_TURN = 20;

    public static void main(String[] args) {

        // simulation
        int total_score = 0;
        while (true) {
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
                if (total_score + turn_score >= MAX_SCORE) {
                    break;
                }
                if (turn_score >= MAX_SCORE_EACH_TURN) {
                    break;
                }
            }
            total_score += turn_score;
            System.out.println("Turn score = " + turn_score);
            System.out.println("New total score = " + total_score);
            if (total_score >= MAX_SCORE) {
                break;
            }
        }
    }
}
