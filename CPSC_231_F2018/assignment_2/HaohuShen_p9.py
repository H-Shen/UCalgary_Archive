#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program creates an interactive pig game between a human and a computer.

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

# Assume that the human player is PLAYER1.
PLAYER1 = 0
PLAYER2 = 1


def simulation_human(total_score):
    """
    Interact with human in a turn and return the total score.
    """
    turn_score = 0
    while True:
        select = ""
        while True:
            select = input("[r]oll or [h]old? ")
            if select == "r" or select == "h":
                break
        if select == "h":
            break
        outcome = random.randint(MIN_VAL_OF_A_DIE, MAX_VAL_OF_A_DIE)
        print(" - rolled a " + str(outcome))
        if outcome == PIG_OUT:
            turn_score = 0
            print("Pigged out!")
            break
        turn_score += outcome
        if total_score + turn_score >= MAX_SCORE:
            break
    return turn_score


def simulation_pc(total_score):
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

    if first_go == PLAYER2:
        change = True

    print("Your score: 0")
    print("Computer's score: 0")

    while True:
        if not change:
            print("It's your turn")
            player1_total_turn_score = simulation_human(player1_score)
            player1_score += player1_total_turn_score
            print("Total turn score = " + str(player1_total_turn_score))
            print("")
            if player1_score >= MAX_SCORE:
                break
            change = True
        else:
            print("It's computer's turn")
            player2_total_turn_score = simulation_pc(player2_score)
            player2_score += player2_total_turn_score
            print("Total turn score = " + str(player2_total_turn_score))
            print("")
            if player2_score >= MAX_SCORE:
                break
            change = False

        print("Your score: " + str(player1_score))
        print("Computer's score: " + str(player2_score))

    print("Final score: " + str(player1_score) + " vs " + str(player2_score))
    if player1_score > player2_score:
        print("You win!")
    else:
        print("Computer wins!")
