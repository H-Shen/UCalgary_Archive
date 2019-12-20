#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program simulates a full game of two computer players with the previous
strategy.

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

PLAYER1 = 0
PLAYER2 = 1


def simulation(total_score):
    """
    Simulate a turn by computer player and return its total score.
    """
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
    return turn_score


if __name__ == "__main__":

    # initialization
    random.seed()

    # simulation
    player1_score = 0
    player2_score = 0
    first_go = random.randint(PLAYER1, PLAYER2)
    change = False

    print("Player One's score: 0")
    print("Player Two's score: 0")

    if first_go == PLAYER2:
        change = True

    while True:
        if not change:
            print("It's Player One's turn")
            player1_total_turn_score = simulation(player1_score)
            player1_score += player1_total_turn_score
            print("Total turn score = " + str(player1_total_turn_score))
            print("")
            if player1_score >= MAX_SCORE:
                break
            change = True
        else:
            print("It's Player Two's turn")
            player2_total_turn_score = simulation(player2_score)
            player2_score += player2_total_turn_score
            print("Total turn score = " + str(player2_total_turn_score))
            print("")
            if player2_score >= MAX_SCORE:
                break
            change = False

        print("Player One's score: " + str(player1_score))
        print("Player Two's score: " + str(player2_score))

    print("Final score: " + str(player1_score) + " vs " + str(player2_score))
    if player1_score > player2_score:
        print("Player One wins!")
    else:
        print("Player Two wins!")
