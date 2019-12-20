#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program simulates an interactive Hangman Game GUI version using a separate
module. The AI implements the 'largest word family' algorithm. The (partial)
stickman and gallows, the (partially revealed) secret word, and the list of
incorrect guesses will be shown on the window. An appropriate text message will
be shown after the game is won or lost.

    Author: Haohu Shen
    Date:   October 2018
"""

import os
import random
import sys

import visual

MIN_LENGTH = 4
MAX_LENGTH = 9
MAX_GUESS = 8
file_to_read = 'cpsc231-lexicon.txt'


def word_family_obtain(word_list, guess_character):
    """
    Partiton word_list according to the position and frequency that
    guess_character appears. Return a dict whose keys are tuples saving
    the indices of guess_character and whose values are lists of words
    that satisfies the corresponding patterns.
    """

    collections = dict()
    for word in word_list:
        position = list()
        for j in range(len(word)):
            if word[j] == guess_character:
                position.append(j)
        position = tuple(position)
        if not position in collections:
            collections[position] = [word]
        else:
            collections[position].append(word)
    return collections


def most_unique_characters_filter(word_list, guess_character):
    """
    Collect all words in word_list where each has most unique characters
    without guess_character and return as a list.
    """

    result = list()
    max_unique = -1
    for i in word_list:
        try:
            character_list = list(i)
            character_list.remove(guess_character)
        except:
            pass
        max_unique = max(len(set(i)), max_unique)

    for i in word_list:
        try:
            character_list = list(i)
            character_list.remove(guess_character)
        except:
            pass
        if len(set(i)) == max_unique:
            result.append(i)
    return result


def largest_family(collections, guess_character):
    """
    Find the family with most members in collections.

    If there is a tie, find the family with the least frequency of
    guess_character.

    If there is still a tie, collect all words in the tied families with
    most unique characters without guess_character as the largest family.

    Return a dict whose keys are tuples saving the indices of guess_character
    and whose values are lists of words that satisfies the corresponding
    patterns.
    """

    filter0 = dict()
    most_members = len(max(collections.values(), key=len))
    for i in collections:
        if len(collections[i]) == most_members:
            filter0[i] = collections[i]
    if len(filter0) == 1:
        return filter0

    filter1 = dict()
    least_frequency = len(min(filter0.keys(), key=len))
    for i in filter0:
        if len(i) == least_frequency:
            filter1[i] = filter0[i]
    if len(filter1) == 1:
        return filter1

    filter2 = dict()
    tmp = list()
    for i in filter1:
        tmp.extend(filter1[i])

    most_unique = most_unique_characters_filter(tmp, guess_character)
    """
    If most_unique has more than one elements, then randomly select one as the
    answer, because every element in most_unique has the same 'weight' after
    three cycles of filter, based on the strategies in the description of the
    assignment.
    """
    word = random.choice(most_unique)
    position = tuple(i for i in range(len(word)) if word[i] == guess_character)
    filter2[position] = [word]
    return filter2


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
            if MIN_LENGTH <= len(word) <= MAX_LENGTH:
                if not word in lexicon_set:
                    lexicon_set.add(word)
                    lexicon.append(word)

    length = random.randint(MIN_LENGTH, MAX_LENGTH)
    word_list = list(filter(lambda x: len(x) == length, lexicon))
    current_word = ['_' for i in range(length)]
    guess_remaining = MAX_GUESS
    win_the_game = False
    correct_guess = False
    bad_guess_so_far = list()
    guess = ""
    remain_blanks = current_word.count('_')
    random_word = ""
    has_guessed = set()

    while True:

        if guess_remaining == 0:
            random_word = random.choice(word_list)
            break

        remain_blanks = current_word.count('_')
        if remain_blanks == 0:
            win_the_game = True
            break

        visual.stddraw.clear()
        visual.print_title("Welcome to CPSC 231 Evil Hangman!")
        visual.draw_background(8 - guess_remaining)
        visual.remaining_guess(remain_blanks, guess_remaining)
        visual.current_word_show(current_word)
        visual.current_bad_guess_show(bad_guess_so_far)

        correct_guess = False
        while True:
            if not visual.stddraw.hasNextKeyTyped():
                visual.stddraw.show(10.0)
            else:
                guess = visual.stddraw.nextKeyTyped()
                if guess.isalpha():
                    break

        if not guess in bad_guess_so_far:
            word_family = word_family_obtain(word_list, guess)
            word_dict = largest_family(word_family, guess)
            word_list = list(word_dict.values())[0]
            if tuple() in word_dict:
                bad_guess_so_far.append(guess)
            else:
                position = list(word_dict.keys())[0]
                for i in position:
                    current_word[i] = guess
                correct_guess = True

        if correct_guess:
            print("Nice guess!", end='')
        else:
            print('Sorry, there is no ' + '"' + guess + '".', end='')
            guess_remaining -= 1

        if guess in has_guessed:
            print(' It has been guessed before. Please guess a new' +
                  ' character.')
        else:
            has_guessed.add(guess)
            print()

    visual.stddraw.clear()
    visual.print_title("Welcome to CPSC 231 Evil Hangman!")
    visual.draw_background(8 - guess_remaining)
    visual.remaining_guess(remain_blanks, guess_remaining, False)
    visual.current_word_show(current_word)

    if win_the_game:
        visual.game_win_show(current_word, True)
    else:
        visual.game_over_show(random_word)

    print("Press anykey to exit...")
    while not visual.stddraw.hasNextKeyTyped():
        visual.stddraw.show(10.0)
