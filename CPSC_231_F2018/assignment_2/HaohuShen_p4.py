#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program simulates experiments to solve Pepys's problem,
print and compare the probabilities of the two events.

    Author: Haohu Shen
    Date:   September 2018
"""

import random
import sys

# constants
MIN_VAL_OF_A_DIE = 1
MAX_VAL_OF_A_DIE = 6

TEST0_ROLLING_COUNT = 6
TEST1_ROLLING_COUNT = 12
TESTING_TIMES = 10000


def simulation0():
    """
    Simulate getting a 1 at least once when rolling a fair die six times.
    """
    rolling_a_one_at_least_once = 0
    for i in range(TESTING_TIMES):
        rolling_a_one = 0
        for j in range(TEST0_ROLLING_COUNT):
            outcome = random.randint(MIN_VAL_OF_A_DIE, MAX_VAL_OF_A_DIE)
            if outcome == 1:
                rolling_a_one += 1
        if rolling_a_one >= 1:
            rolling_a_one_at_least_once += 1
    return rolling_a_one_at_least_once


def simulation1():
    """
    Simulate getting 1 at least twice when rolling a fair die 12 times.
    """
    rolling_a_one_at_least_twice = 0
    for i in range(TESTING_TIMES):
        rolling_a_one = 0
        for j in range(TEST1_ROLLING_COUNT):
            outcome = random.randint(MIN_VAL_OF_A_DIE, MAX_VAL_OF_A_DIE)
            if outcome == 1:
                rolling_a_one += 1
        if rolling_a_one >= 2:
            rolling_a_one_at_least_twice += 1
    return rolling_a_one_at_least_twice


if __name__ == "__main__":

    # initialization
    random.seed()
    try:
        assert (len(sys.argv) <= 2)
        if len(sys.argv) == 2:
            temp_num = int(sys.argv[1])
            assert (str(temp_num) == sys.argv[1])
            assert (temp_num >= 0)
            TESTING_TIMES = temp_num
    except:
        quit()

    # simulation
    rolling_a_one_at_least_once = simulation0()
    rolling_a_one_at_least_twice = simulation1()

    # calculation
    if TESTING_TIMES == 0:
        p0 = 0
        p1 = 0
    else:
        p0 = rolling_a_one_at_least_once / TESTING_TIMES
        p1 = rolling_a_one_at_least_twice / TESTING_TIMES

    # output
    print("Estimated likelihood of 1 once in 6: " + str(p0))
    print("Estimated likelihood of 1 twice in 12: " + str(p1))
    if p0 > p1:
        print("Therefore, getting a 1 at least once when rolling a fair die "
              "six times is more likely than getting 1 at least twice when "
              "rolling it 12 times.")
    elif p0 < p1:
        print("Therefore, getting 1 at least twice when rolling a fair die 12 "
              "times is much likely than getting a 1 at least once when "
              "rolling it six times.")
    else:
        print("Therefore, getting a 1 at least once when rolling a fair die "
              "six times and getting 1 at least twice when "
              "rolling it 12 times are equally probable.")
