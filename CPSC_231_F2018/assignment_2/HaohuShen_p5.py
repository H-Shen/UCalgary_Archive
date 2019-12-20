#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program simulates a computer's turn of pig. It will not stop rolling
until accumulating 20 or more points in the turn.

    Author: Haohu Shen
    Date:   September 2018
"""

import random

# constants
MIN_VAL_OF_A_DIE = 1
MAX_VAL_OF_A_DIE = 6

PIG_OUT = 1
MAX_SCORE = 20

if __name__ == "__main__":

    # initialization
    random.seed()

    # simulation
    turn_score = 0
    while True:
        outcome = random.randint(MIN_VAL_OF_A_DIE, MAX_VAL_OF_A_DIE)
        print(" - rolled a " + str(outcome))
        if outcome == PIG_OUT:
            turn_score = 0
            print("Pigged out!")
            break
        turn_score += outcome
        if turn_score >= MAX_SCORE:
            break

    # output
    print("Turn score = " + str(turn_score))
