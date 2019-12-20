#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program simulates an interactive Hangman Game GUI version using a
separate module. The (partial) stickman and gallows, the (partially revealed)
secret word, and the list of incorrect guesses will be shown on the window.
An appropriate text message will be shown after the game is won or lost.

    Author: Haohu Shen
    Date:   October 2018
"""

import os
import random
import sys

import visual

MIN_LENGTH = 4
MAX_GUESS = 8
file_to_read = 'cpsc231-lexicon.txt'

if __name__ == "__main__":

    # arguments validation
    try:
        assert len(sys.argv) == 2
        file_to_read = sys.argv[1]
        assert os.access(file_to_read, os.F_OK)
        assert os.access(file_to_read, os.R_OK)
    except:
        print("Arguments invalid, program terminated.")
        sys.exit()

    # initialization
    lexicon = list()

    with open(file_to_read, 'r') as f:
        lexicon_set = set()
        for i in f.readlines():
            word = i.rstrip().lstrip()
            if not word in lexicon_set:
                lexicon_set.add(word)
                if len(word) >= MIN_LENGTH:
                    lexicon.append(word)

    word_to_guess = list(random.choice(lexicon))
    guess_remaining = MAX_GUESS
    win_the_game = False
    current_word = ['_' for i in range(len(word_to_guess))]
    bad_guess_so_far = list()
    guess = ""
    remain_blanks = 0
    has_guessed = set()

    while True:

        if guess_remaining == 0:
            break

        remain_blanks = current_word.count('_')
        visual.stddraw.clear()
        visual.print_title("Welcome to CPSC 231 Visual Hangman!")
        visual.draw_background(8 - guess_remaining)
        visual.remaining_guess(remain_blanks, guess_remaining)
        visual.current_word_show(current_word)
        visual.current_bad_guess_show(bad_guess_so_far)

        while True:
            if not visual.stddraw.hasNextKeyTyped():
                visual.stddraw.show(10.0)
            else:
                guess = visual.stddraw.nextKeyTyped()
                if guess.isalpha():
                    break

        if guess in word_to_guess:
            for i in range(len(word_to_guess)):
                if word_to_guess[i] == guess:
                    current_word[i] = guess
            print("Nice guess!", end='')

        else:
            if not guess in bad_guess_so_far:
                bad_guess_so_far.append(guess)
            print('Sorry, there is no ' + '"' + guess + '".', end='')
            guess_remaining -= 1

        if guess in has_guessed:
            print(' It has been guessed before. Please guess a new' +
                  ' character.')
        else:
            has_guessed.add(guess)
            print()

        if current_word == word_to_guess:
            remain_blanks = 0
            win_the_game = True
            break

    visual.stddraw.clear()
    visual.print_title("Welcome to CPSC 231 Visual Hangman!")
    visual.draw_background(8 - guess_remaining)
    visual.remaining_guess(remain_blanks, guess_remaining, False)
    visual.current_word_show(current_word)

    if win_the_game:
        visual.game_win_show(word_to_guess)
    else:
        visual.game_over_show(word_to_guess)

    print("Press anykey to exit...")
    while not visual.stddraw.hasNextKeyTyped():
        visual.stddraw.show(10.0)
