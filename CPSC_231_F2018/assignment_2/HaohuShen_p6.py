#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program simulates the same strategy as previous program but using dynamic
initial scores.

    Author: Haohu Shen
    Date:   September 2018
"""

import random

# constants
MIN_VAL_OF_A_DIE = 1
MAX_VAL_OF_A_DIE = 6

PIG_OUT = 1
MAX_SCORE = 100
MAX_SCORE_EACH_TURN = 20

if __name__ == "__main__":

    # initialization
    random.seed()
    total_score = 0

    # input validation
    try:
        temp_str = input("Enter current score: ")
        total_score = int(temp_str)
        assert (str(total_score) == temp_str)
        assert (total_score >= 0)
    except:
        print("Invalid input! Program terminated.")
        quit()

    # simulation
    turn_score = 0
    if total_score < MAX_SCORE:
        while True:
            outcome = random.randint(MIN_VAL_OF_A_DIE, MAX_VAL_OF_A_DIE)
            print(" - rolled a " + str(outcome))
            if outcome == PIG_OUT:
                turn_score = 0
                print("Pigged out!")
                break
            turn_score += outcome
            if turn_score + total_score >= MAX_SCORE:
                break
            if turn_score >= MAX_SCORE_EACH_TURN:
                break

    # output
    new_score = total_score + turn_score
    print("Turn score = " + str(turn_score))
    print("New total score = " + str(new_score))
