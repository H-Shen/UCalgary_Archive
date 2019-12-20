#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program simulates rolling a single six-sided die by given times and
calculate the expected rolls then print it to the terminal. Any unexpected
input will not be accepted and produce empty results.

    Author: Haohu Shen
    Date:   September 2018
"""

import random
import sys

# constants
MIN_VAL_OF_A_DIE = 1
MAX_VAL_OF_A_DIE = 6
PIG_OUT_SIGN = 1

if __name__ == "__main__":

    # initialization
    random.seed()
    turns_count = 0
    total_number_of_rolls = 0

    # checking input
    try:
        assert (len(sys.argv) == 2)
        turns_count = int(sys.argv[1])
        assert (str(turns_count) == sys.argv[1])
        assert (turns_count >= 0)
    except:
        quit()

    # simulation
    for i in range(turns_count):
        current_number_of_rolls = 0
        while True:
            outcome = random.randint(MIN_VAL_OF_A_DIE, MAX_VAL_OF_A_DIE)
            current_number_of_rolls += 1
            if outcome == PIG_OUT_SIGN:
                break
        total_number_of_rolls += current_number_of_rolls

    # calculation
    if turns_count == 0:
        result = 0.0
    else:
        result = total_number_of_rolls / turns_count

    # output
    if turns_count <= 1:
        print("Simulating " + sys.argv[1] + " turn...")
    else:
        print("Simulating " + sys.argv[1] + " turns...")
    print("Estimated expectation: " + "%.3f" % result)
