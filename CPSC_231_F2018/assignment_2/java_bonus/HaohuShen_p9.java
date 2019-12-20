/**
 * This program creates an interactive pig game between a human and a computer.
 *
 * @author Haohu Shen
 * @version 1.8.0_161, 09/24/2018
 */

import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class HaohuShen_p9 {

    // constants
    private static final int MIN_VAL_OF_A_DIE = 1;
    private static final int MAX_VAL_OF_A_DIE = 6;
    private static final int PIG_OUT = 1;
    private static final int MAX_SCORE = 100;
    private static final int MAX_SCORE_EACH_TURN = 20;

    // Assume that the human player is PLAYER1.
    private static final int PLAYER1 = 0;
    private static final int PLAYER2 = 1;

    public static int simulation_human(int total_score) {
        /** Interact with human in a turn and return the total score. **/
        int turn_score = 0;
        while (true) {
            StringBuffer select = new StringBuffer();
            while (true) {
                Scanner in = new Scanner(System.in);
                System.out.print("[r]oll or [h]old? ");
                select.append(in.nextLine());
                if (select.toString().equals("h")) {
                    break;
                } else if (select.toString().equals("r")) {
                    break;
                }
                select.delete(0, select.length());
            }
            if (select.toString().equals("h")) {
                break;
            }
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
        }
        return turn_score;
    }

    public static int simulation_pc(int total_score) {
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
                PLAYER1, PLAYER2);
        boolean change = false;

        if (first_go == PLAYER2) {
            change = true;
        }

        System.out.println("Your score: 0");
        System.out.println("Computer's score: 0");

        while (true) {
            if (!change) {
                System.out.println("It's your turn");
                player1_turn_score = simulation_human(player1_score);
                player1_score += player1_turn_score;
                System.out.println("Total turn score = " + player1_turn_score);
                System.out.println();
                if (player1_score >= MAX_SCORE) {
                    break;
                }
                change = true;
            } else {
                System.out.println("It's computer's turn");
                player2_turn_score = simulation_pc(player2_score);
                player2_score += player2_turn_score;
                System.out.println("Total turn score = " + player2_turn_score);
                System.out.println();
                if (player2_score >= MAX_SCORE) {
                    break;
                }
                change = false;
            }
            System.out.println("Your score: " + player1_score);
            System.out.println("Computer's score: " + player2_score);
        }
        System.out.println("Final score: " + player1_score + " vs " +
                player2_score);
        if (player1_score > player2_score) {
            System.out.println("You win!");
        } else {
            System.out.println("Computer wins!");
        }
    }
}
