#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program simulates an interactive environment of the Console Hangman Game
and plays the role of 'executioner'.

    Author: Haohu Shen
    Date:   October 2018
"""

import os
import random
import sys

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
    current_answer = ['_' for i in range(len(word_to_guess))]
    bad_guess_so_far = list()
    has_guessed = set()

    print("Welcome to CPSC 231 Console Hangman!")
    print()
    while guess_remaining > 0:

        print("The secret word looks like: ", ''.join(current_answer))

        if len(bad_guess_so_far) > 0:
            print("Your bad guesses so far: ", end='')
            print(' '.join(bad_guess_so_far))

        if guess_remaining > 1:
            print("You have " + str(guess_remaining) + " guesses remaining.")
        else:
            print("You have 1 guess remaining.")

        while True:
            guess = input("What's your next guess? ")
            if len(guess) == 1 and guess.isalpha():
                break
            print("Invalid input. Try again.")

        if guess in word_to_guess:
            for i in range(len(word_to_guess)):
                if word_to_guess[i] == guess:
                    current_answer[i] = guess
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

        if current_answer == word_to_guess:
            win_the_game = True
            break
        print()

    if win_the_game:
        print()
        print("Congratulations!")
        print("You guessed the secret word: " + ''.join(word_to_guess))
    else:
        print("Sorry...")
        print("You didn't guess the secret word: " + ''.join(word_to_guess))
