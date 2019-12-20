/**
 * This program simulates an interactive environment of the Console Hangman Game
 * and plays the role of 'executioner'.
 *
 * @author Haohu Shen
 * @version 1.8.0_161, 10/29/2018
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

class HaohuShen_p3 {

    private static final int    MIN_LENGTH   = 4;
    private static final int    MAX_GUESS    = 8;
    private static       String file_to_read = "cpsc231-lexicon.txt";

    /**
     * Validate the external input arguments, including the total number of arguments.
     * Any invalid element will make the function return False, otherwise return
     * True.
     */
    private static boolean arguments_validation(String[] args) {
        if (args.length == 1) {
            file_to_read = args[0];
            return true;
        }
        return false;
    }

    /**
     * Return a string which is the concatenation of characters in the
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

    /**
     * Return true if all cased characters in s are lowercase no matter
     * how long the string s is, false otherwise.
     */
    private static boolean str_is_alpha(String s) {
        boolean result = true;
        for (int i = 0; i < s.length(); ++i) {
            if (!Character.isAlphabetic(s.charAt(i))) {
                result = false;
                break;
            }
        }
        return result;
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

        System.out.println("Welcome to CPSC 231 Console Hangman!");
        System.out.println();
        int                  guess_remaining  = MAX_GUESS;
        boolean              win_the_game     = false;
        ArrayList<Character> bad_guess_so_far = new ArrayList<>();
        String               guess;
        HashSet<Character>   has_guessed      = new HashSet<>();

        ArrayList<Character> current_answer = new ArrayList<>();
        for (int i = 0; i < word_to_guess.size(); ++i) {
            current_answer.add('_');
        }

        while (guess_remaining > 0) {

            System.out.println("The secret word looks like: " + my_join(current_answer, ""));
            if (!bad_guess_so_far.isEmpty()) {
                System.out.println("Your bad guesses so far: " + my_join(bad_guess_so_far, " "));
            }

            if (guess_remaining > 1) {
                System.out.println("You have " + guess_remaining + " guesses remaining.");
            } else {
                System.out.println("You have 1 guess remaining.");
            }

            while (true) {
                Scanner in = new Scanner(System.in);
                System.out.print("What's your next guess? ");
                guess = in.nextLine();
                if (guess.length() == 1 && str_is_alpha(guess)) {
                    break;
                }
                System.out.println("Invalid input. Try again.");
            }

            if (word_to_guess.contains(guess.charAt(0))) {
                for (int i = 0; i < word_to_guess.size(); ++i) {
                    if (word_to_guess.get(i).equals(guess.charAt(0))) {
                        current_answer.set(i, guess.charAt(0));
                    }
                }
                System.out.print("Nice guess!");
            } else {
                if (!bad_guess_so_far.contains(guess.charAt(0))) {
                    bad_guess_so_far.add(guess.charAt(0));
                }
                System.out.print("Sorry, there is no " + "\"" + guess + "\".");
                --guess_remaining;
            }

            if (has_guessed.contains(guess.charAt(0))) {
                System.out.println(" It has been guessed before. Please guess a new character.");
            } else {
                has_guessed.add(guess.charAt(0));
                System.out.println();
            }

            if (current_answer.equals(word_to_guess)) {
                win_the_game = true;
                break;
            }
            System.out.println();
        }

        if (win_the_game) {
            System.out.println();
            System.out.println("Congratulations!");
            System.out.println("You guessed the secret word: " + my_join(word_to_guess, ""));
        } else {
            System.out.println("Sorry...");
            System.out.println("You didn't guess the secret word: " + my_join(word_to_guess, ""));
        }
    }
}
