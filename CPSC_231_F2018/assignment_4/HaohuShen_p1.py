#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program imports a lexicon and accepts a user-specified word, then
performs a lookup for the word in the lexicon. If the word is found, outputs
the rank of the word in the lexicon, otherwise indicates the user the word is
not found. Meanwhile, outputs the word's unique letters in lowercase
alphabetically.

    Author: Haohu Shen
    Date:   October 2018
"""

import os
import sys

if __name__ == "__main__":

    # initialization
    file_to_read = "cpsc231-lexicon.txt"
    word_to_check = ""
    lexicon_set = set()
    lexicon = list()

    # arguments validation
    try:
        assert len(sys.argv) == 3
        file_to_read = sys.argv[1]
        word_to_check = sys.argv[2]
        assert os.access(file_to_read, os.F_OK)
        assert os.access(file_to_read, os.R_OK)
    except:
        print("Arguments invalid, program terminated.")
        quit()

    # import the lexicon without duplicate words
    with open(file_to_read, 'r') as f:
        for i in f.readlines():
            word = i.rstrip().lstrip().lower()
            if not word in lexicon_set:
                lexicon_set.add(word)
                lexicon.append(word)

    # Collect unique letters in word_to_check regardless of the distinction
    # between uppercase letters and lowercase letters. Digits would be ignored.
    character_list = list()
    no_duplicate = set()
    word_to_check = word_to_check.lower()
    for i in word_to_check:
        if i.isalpha() and (not i in no_duplicate):
            no_duplicate.add(i)
            character_list.append(i)

    # output
    if word_to_check in lexicon_set:
        rank = lexicon.index(word_to_check) + 1
        suffix = ""
        if rank % 100 == 11 or rank % 100 == 12 or rank % 100 == 13:
            suffix = "th"
        elif rank % 10 == 1:
            suffix = "st"
        elif rank % 10 == 2:
            suffix = "nd"
        elif rank % 10 == 3:
            suffix = "rd"
        else:
            suffix = "th"
        print('According to our lexicon, "' + word_to_check + '" is the ' +
              str(rank) + suffix + ' most common word in the contemporary '
                                   'American English.')
    else:
        print('According to our lexicon, "' + word_to_check + '" is not in the ' +
              str(len(lexicon)) + ' most common words of contemporary American '
                                  'English.')
    print()
    print('It contains the following letters:')

    character_list.sort()
    for i in character_list:
        print(' ' + i, end='')
    print()
