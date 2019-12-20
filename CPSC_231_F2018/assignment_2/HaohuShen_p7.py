#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program simulates a full one player game by computer, starts from 0 total
score and plays turn by turn until it reaches 100 points.

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

    # simulation
    total_score = 0
    while True:
        turn_score = 0
        while True:
            outcome = random.randint(MIN_VAL_OF_A_DIE, MAX_VAL_OF_A_DIE)
            print(" - rolled a " + str(outcome))
            if outcome == PIG_OUT:
                turn_score = 0
                print("Pigged out!")
                break
            turn_score += outcome
            if total_score + turn_score >= MAX_SCORE:
                break
            if turn_score >= MAX_SCORE_EACH_TURN:
                break
        total_score += turn_score
        print("Turn score = " + str(turn_score))
        print("New total score = " + str(total_score))
        if total_score >= MAX_SCORE:
            break
