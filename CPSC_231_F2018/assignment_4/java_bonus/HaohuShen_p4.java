/**
 * This program simulates an interactive Hangman Game GUI version using a
 * separate module. The (partial) stickman and gallows, the (partially revealed)
 * secret word, and the list of incorrect guesses will be shown on the window.
 * An appropriate text message will be shown after the game is won or lost.
 * <p>
 * Compilation:  javac -cp stdlib.jar: HaohuShen_p4.javai (Linux / macOS)
 * Execution:    java  -cp stdlib.jar: HaohuShen_p4 (Linux / macOS)
 * <p>
 * Compilation and execution in Windows 10:
 * java -cp stdlib.jar HaohuShen_p4.java
 * <p>
 * Dependencies: stdlib.jar
 * <p>
 * stdlib.jar is downloaded from:
 * https://introcs.cs.princeton.edu/java/stdlib/stdlib.jar
 * And it is also uploaded as a part of assignment4.
 *
 * @author Haohu Shen
 * @version 1.8.0_161, 10/29/2018
 */

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;

class HaohuShen_p4 {

    private static final int    MIN_LENGTH           = 4;
    private static final int    MAX_GUESS            = 8;
    private static final int    BAD_GUESS_MAX_LENGTH = 17;
    private static       String file_to_read         = "cpsc231-lexicon.txt";

    /**
     * Draw the (partial) hangman and the gallow according to the corresponding step. The floor
     * will be drawn at the beginning of the game.
     */
    private static void draw_background(int step) {

        // predefine points
        double x  = 0.37, y = 0.3;
        double x0 = x + 0.15, y0 = y + 0.5;
        double x1 = x0 - 0.195, y1 = y0 - 0.15 - 0.03;
        double x2 = x0 - 0.195, y2 = y0 - 0.3;
        double x3 = x0 - 0.195 - 0.02, y3 = y0 - 0.1 + 0.02;
        double x4 = x0 - 0.195 + 0.015, y4 = y0 - 0.1 + 0.02;
        double x5 = x0 - 0.195, y5 = y0 - 0.110;

        // floor
        StdDraw.setPenRadius(0.002);
        StdDraw.line(x, y, x - 0.3, y);
        StdDraw.line(x, y, x + 0.5, y);
        StdDraw.line(x - 0.2, y, x - 0.2, y + 0.06);
        StdDraw.line(x + 0.2, y, x + 0.2, y + 0.06);
        StdDraw.line(x - 0.2, y + 0.06, x + 0.2, y + 0.06);

        if (step >= 1) {
            // head
            StdDraw.setPenRadius();
            StdDraw.circle(x0 - 0.195, y0 - 0.1, 0.05);
        }
        if (step >= 2) {
            // torso
            StdDraw.line(x0 - 0.195, y0 - 0.15, x0 - 0.195, y0 - 0.3);
        }
        if (step >= 3) {
            // left arm
            StdDraw.line(x1, y1, x1 - 0.06, y1 - 0.06);
        }
        if (step >= 4) {
            // right arm
            StdDraw.line(x1, y1, x1 + 0.06, y1 - 0.06);
        }
        if (step >= 5) {
            // left leg
            StdDraw.line(x2, y2, x2 - 0.04, y2 - 0.08);
        }
        if (step >= 6) {
            // right leg
            StdDraw.line(x2, y2, x2 + 0.04, y2 - 0.08);
        }
        if (step >= 7) {
            // gallows
            StdDraw.setPenRadius(0.002);
            StdDraw.line(x + 0.15, y + 0.06, x + 0.15, y + 0.5);
            StdDraw.line(x0, y0, x0 - 0.2, y0);
            StdDraw.line(x0 - 0.2, y0, x0 - 0.2, y0 - 0.05);
        }
        if (step >= 8) {
            // face
            StdDraw.setPenRadius(0.002);
            StdDraw.line(x3, y3, x3 + 0.005, y3 + 0.007);
            StdDraw.line(x3, y3, x3 + 0.005, y3 - 0.007);
            StdDraw.line(x3, y3, x3 - 0.005, y3 + 0.007);
            StdDraw.line(x3, y3, x3 - 0.005, y3 - 0.007);
            StdDraw.line(x4, y4, x4 + 0.005, y4 + 0.007);
            StdDraw.line(x4, y4, x4 + 0.005, y4 - 0.007);
            StdDraw.line(x4, y4, x4 - 0.005, y4 + 0.007);
            StdDraw.line(x4, y4, x4 - 0.005, y4 - 0.007);
            StdDraw.line(x5, y5, x5 - 0.015, y5);
            StdDraw.line(x5, y5, x5 + 0.015, y5);
        }
    }

    /**
     * Print the title of the game on the upper of the window.
     */
    private static void print_title() {
        double x = 0.5, y = 0.35;
        StdDraw.setFont(new Font("Helvetica", Font.PLAIN, 23));
        StdDraw.text(x, y + 0.55, "Welcome to CPSC 231 Visual Hangman!");
    }

    /**
     * Show the remaining guess in the canvas.
     */
    private static void remaining_guess(int remain_blanks, int guess_remain, boolean show_prompt) {
        double x = 0.5, y = 0.35;
        StdDraw.setFont(new Font("Helvetica", Font.PLAIN, 18));

        if (guess_remain > 1) {
            StdDraw.text(x + 0.25, y + 0.42, "Remain: " + guess_remain + " Guesses");
        } else {
            StdDraw.text(x + 0.25, y + 0.42, "Remain: " + guess_remain + " Guess");
        }

        if (remain_blanks > 1) {
            StdDraw.text(x + 0.25, y + 0.35, "Remain: " + remain_blanks + " Blanks");
        } else {
            StdDraw.text(x + 0.25, y + 0.35, "Remain: " + remain_blanks + " Blank");
        }
        StdDraw.setFont(new Font("Courier", Font.PLAIN, 14));
        if (show_prompt) {
            StdDraw.text(x + 0.25, y + 0.27, "Press your next guess...");
        }
    }

    /**
     * Show the current guessed characters in the canvas.
     */
    private static void current_word_show(ArrayList<Character> current_word) {
        double x = 0.5, y = 0.18;
        StdDraw.setFont(new Font("Helvetica", Font.PLAIN, 30));
        StdDraw.text(x, y, my_join(current_word, " "));
    }

    /**
     * Show the incorrect guessed characters in the canvas.
     */
    private static void current_bad_guess_show(ArrayList<Character> bad_guess_so_far) {
        double x = 0.5, y = 0.08;
        StdDraw.setFont(new Font("Helvetica", Font.PLAIN, 18));

        if (bad_guess_so_far.size() > BAD_GUESS_MAX_LENGTH) {
            StdDraw.text(x, y, "Your bad guesses so far :   " + my_join(bad_guess_so_far, ""));
        } else {
            StdDraw.text(x, y, "Your bad guesses so far :   " + my_join(bad_guess_so_far, " "));
        }
    }

    /**
     * Show the contents indicates that the game is over in the canvas.
     */
    private static void game_over_show(ArrayList<Character> word) {
        double x = 0.5, y = 0.08;
        StdDraw.setFont(new Font("Helvetica", Font.PLAIN, 18));
        StdDraw.text(x, y, "Sorry... You didn't guess the secret word: " + my_join(word, ""));
    }

    /**
     * Show the contents indicates the user wins the game in the canvas.
     */
    private static void game_win_show(ArrayList<Character> word) {
        double x = 0.5, y = 0.08;
        StdDraw.setFont(new Font("Helvetica", Font.PLAIN, 18));
        StdDraw.text(x, y, "Congratulations! You guessed the secret word: " + my_join(word, ""));
    }

    /**
     * Validate the external input arguments, including the total number of arguments.
     * Invalid input or invalid number of arguments will make the function return false,
     * otherwise return true.
     */
    private static boolean arguments_validation(String[] args) {
        if (args.length == 1) {
            file_to_read = args[0];
            return true;
        }
        return false;
    }

    /**
     * Return a string which is the concatenation of the characters in the
     * array list. The separator string between elements is a.
     */
    private static String my_join(ArrayList<Character> s, String a) {

        StringBuilder result = new StringBuilder();
        if (s.isEmpty()) {
            return result.toString();
        }
        result = new StringBuilder(s.get(0).toString());
        for (int i = 1; i < s.size(); ++i) {
            result.append(a).append(s.get(i).toString());
        }
        return result.toString();
    }

    public static void main(String[] args) {

        if (!arguments_validation(args)) {
            System.out.println("Arguments invalid, program terminated.");
            System.exit(0);
        }

        // initialization
        ArrayList<String> lexicon = new ArrayList<>();

        try {
            File            in          = new File(file_to_read);
            FileInputStream is          = new FileInputStream(in);
            BufferedReader  br          = new BufferedReader(new InputStreamReader(is));
            HashSet<String> lexicon_set = new HashSet<>();
            String          word;
            while (true) {
                word = br.readLine();
                if (word == null) {
                    break;
                }
                if (!lexicon_set.contains(word)) {
                    lexicon_set.add(word);
                    if (word.length() >= MIN_LENGTH) {
                        lexicon.add(word);
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("File data invalid, program terminated.");
            System.exit(0);
        }

        String tmp = lexicon.get(ThreadLocalRandom.current().nextInt(0, lexicon.size()));
        ArrayList<Character> word_to_guess = new ArrayList<>();
        for (int i = 0; i < tmp.length(); ++i) {
            word_to_guess.add(tmp.charAt(i));
        }

        // interaction
        int                  guess_remaining  = MAX_GUESS;
        boolean              win_the_game     = false;
        ArrayList<Character> bad_guess_so_far = new ArrayList<>();
        Character            guess;
        int                  remain_blanks    = 0;
        HashSet<Character>   has_guessed      = new HashSet<>();

        ArrayList<Character> current_word = new ArrayList<>();
        for (int i = 0; i < word_to_guess.size(); ++i) {
            current_word.add('_');
        }

        StdDraw.enableDoubleBuffering();
        while (true) {

            if (guess_remaining == 0) {
                break;
            }

            remain_blanks = Collections.frequency(current_word, '_');
            StdDraw.clear();
            print_title();
            draw_background(8 - guess_remaining);
            remaining_guess(remain_blanks, guess_remaining, true);
            current_word_show(current_word);
            current_bad_guess_show(bad_guess_so_far);

            while (true) {
                if (!StdDraw.hasNextKeyTyped()) {
                    StdDraw.show();
                    StdDraw.pause(10);
                } else {
                    guess = StdDraw.nextKeyTyped();
                    if (Character.isAlphabetic(guess)) {
                        break;
                    }
                }
            }

            if (word_to_guess.contains(guess)) {
                for (int i = 0; i < word_to_guess.size(); ++i) {
                    if (word_to_guess.get(i).equals(guess)) {
                        current_word.set(i, guess);
                    }
                }
                System.out.print("Nice guess!");
            } else {
                if (!bad_guess_so_far.contains(guess)) {
                    bad_guess_so_far.add(guess);
                }
                System.out.print("Sorry, there is no " + "\"" + guess + "\".");
                --guess_remaining;
            }

            if (has_guessed.contains(guess)) {
                System.out.println(" It has been guessed before. Please guess a new character.");
            } else {
                has_guessed.add(guess);
                System.out.println();
            }

            if (current_word.equals(word_to_guess)) {
                win_the_game = true;
                break;
            }
        }

        StdDraw.clear();
        print_title();
        draw_background(8 - guess_remaining);
        remaining_guess(remain_blanks, guess_remaining, false);
        current_word_show(current_word);

        if (win_the_game) {
            game_win_show(word_to_guess);
        } else {
            game_over_show(word_to_guess);
        }

        System.out.println("Press anykey to exit...");
        while (!StdDraw.hasNextKeyTyped()) {
            StdDraw.show();
            StdDraw.pause(10);
        }
        System.exit(0);
    }
}
