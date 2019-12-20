/**
 * This program simulates a full game of two computer players with the previous
 * strategy.
 *
 * @author Haohu Shen
 * @version 1.8.0_161, 09/24/2018
 */

import java.util.concurrent.ThreadLocalRandom;

public class HaohuShen_p8 {

    // constants
    private static final int MIN_VAL_OF_A_DIE = 1;
    private static final int MAX_VAL_OF_A_DIE = 6;
    private static final int PIG_OUT = 1;
    private static final int MAX_SCORE = 100;
    private static final int MAX_SCORE_EACH_TURN = 20;
    private static final int PLAYER1 = 0;
    private static final int PLAYER2 = 1;

    public static int simulation(int total_score) {
        /** Simulate a turn by computer player and return its total score. **/
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
        return turn_score;
    }

    public static void main(String[] args) {

        // simulation
        int player1_score = 0;
        int player2_score = 0;
        int player1_turn_score = 0;
        int player2_turn_score = 0;
        int first_go = ThreadLocalRandom.current().nextInt(
                PLAYER1, PLAYER2 + 1);
        boolean change = false;

        System.out.println("Player One's score: 0");
        System.out.println("Player Two's score: 0");

        if (first_go == PLAYER2) {
            change = true;
        }

        while (true) {
            if (!change) {
                System.out.println("It's Player One's turn");
                player1_turn_score = simulation(player1_score);
                player1_score += player1_turn_score;
                System.out.println("Total turn score = " + player1_turn_score);
                System.out.println();
                if (player1_score >= MAX_SCORE) {
                    break;
                }
                change = true;
            } else {
                System.out.println("It's Player Two's turn");
                player2_turn_score = simulation(player2_score);
                player2_score += player2_turn_score;
                System.out.println("Total turn score = " + player2_turn_score);
                System.out.println();
                if (player2_score >= MAX_SCORE) {
                    break;
                }
                change = false;
            }
            System.out.println("Player One's score: " + player1_score);
            System.out.println("Player Two's score: " + player2_score);
        }
        System.out.println("Final score: " + player1_score + " vs " +
                player2_score);
        if (player1_score > player2_score) {
            System.out.println("Player One wins!");
        } else {
            System.out.println("Player Two wins!");
        }
    }
}
