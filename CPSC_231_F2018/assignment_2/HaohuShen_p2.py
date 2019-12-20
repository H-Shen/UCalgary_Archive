#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program simulates rolling a single six-sided die by given times and
calculate the expected value then print it to the terminal. Any unexpected
input will not be accepted and produce empty results.

    Author: Haohu Shen
    Date:   September 2018
"""

import random
import sys

# constants
MIN_VAL_OF_A_DIE = 1
MAX_VAL_OF_A_DIE = 6

if __name__ == "__main__":

    # initialization
    random.seed()
    frequency_save = dict()
    for i in range(MIN_VAL_OF_A_DIE, MAX_VAL_OF_A_DIE + 1):
        frequency_save[i] = 0
    test_count = 0

    # checking input
    try:
        assert (len(sys.argv) == 2)
        test_count = int(sys.argv[1])
        assert (str(test_count) == sys.argv[1])
        assert (test_count >= 0)
    except:
        quit()

    # simulation
    for i in range(test_count):
        outcome = random.randint(MIN_VAL_OF_A_DIE, MAX_VAL_OF_A_DIE)
        frequency_save[outcome] += 1

    # calculation
    result = 0
    for i in frequency_save:
        result += i * frequency_save[i]
    if test_count == 0:
        result = 0.0
    else:
        result /= test_count

    # output
    print("Rolling ", end="")
    if test_count <= 1:
        print(sys.argv[1] + " time...")
    else:
        print(sys.argv[1] + " times...")
    print("Estimated expectation: " + "%.3f" % result)
