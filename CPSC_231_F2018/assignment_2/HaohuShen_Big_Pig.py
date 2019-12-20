#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program creates an interactive big pig game between a human and a
computer. The computer's current strategy is simple, which should be
optimized by dynamic programming and value iteration for further development.

    Author: Haohu Shen
    Date:   October 2018
"""

import random

# constants
MIN_VAL_OF_A_DIE = 1
MAX_VAL_OF_A_DIE = 6

MAX_SCORE = 100

# global variables
max_score_each_turn = 25
inning_cnt = 0


def simulation_human():
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

        outcome0 = random.randint(MIN_VAL_OF_A_DIE, MAX_VAL_OF_A_DIE)
        outcome1 = random.randint(MIN_VAL_OF_A_DIE, MAX_VAL_OF_A_DIE)
        print(" - rolled a " + str(outcome0) + " and a " + str(outcome1))
        if outcome0 == 1 and outcome1 != 1:
            turn_score = 0
            print("Pigged out!")
            return turn_score
        if outcome0 != 1 and outcome1 == 1:
            turn_score = 0
            print("Pigged out!")
            return turn_score

        if outcome0 == 1 and outcome1 == 1:
            turn_score = turn_score + 25
        elif outcome0 == outcome1:
            turn_score = turn_score + 4 * outcome0
        else:
            turn_score = turn_score + outcome0 + outcome1

    return turn_score


def simulation_pc_as_first_player():
    """
    Simulate a turn by computer player as the frame and return its total score
    after 100. Only run when both players have the same scores.
    """
    turn_score = 0
    outcome0 = random.randint(MIN_VAL_OF_A_DIE, MAX_VAL_OF_A_DIE)
    outcome1 = random.randint(MIN_VAL_OF_A_DIE, MAX_VAL_OF_A_DIE)
    print(" - rolled a " + str(outcome0) + " and a " + str(outcome1))

    if outcome0 == 1 and outcome1 != 1:
        turn_score = 0
        print("Pigged out!")
        return turn_score
    if outcome0 != 1 and outcome1 == 1:
        turn_score = 0
        print("Pigged out!")
        return turn_score

    if outcome0 == 1 and outcome1 == 1:
        turn_score = turn_score + 25
    elif outcome0 == outcome1:
        turn_score = turn_score + 4 * outcome0
    else:
        turn_score = turn_score + outcome0 + outcome1
    return turn_score


def simulation_pc_as_second_player(my_score, opponent_score):
    """
    Simulate a turn by computer player as the bottom when its opponent's
    score is equal or higher than 100. It will not stop until it is pigged out
    or its score is higher than opponent's.
    """
    turn_score = 0
    while True:
        outcome0 = random.randint(MIN_VAL_OF_A_DIE, MAX_VAL_OF_A_DIE)
        outcome1 = random.randint(MIN_VAL_OF_A_DIE, MAX_VAL_OF_A_DIE)
        print(" - rolled a " + str(outcome0) + " and a " + str(outcome1))

        if outcome0 == 1 and outcome1 != 1:
            turn_score = 0
            print("Pigged out!")
            break
        if outcome0 != 1 and outcome1 == 1:
            turn_score = 0
            print("Pigged out!")
            break

        if outcome0 == 1 and outcome1 == 1:
            turn_score = turn_score + 25
        elif outcome0 == outcome1:
            turn_score = turn_score + 4 * outcome0
        else:
            turn_score = turn_score + outcome0 + outcome1

        if my_score + turn_score > opponent_score:
            break
    return turn_score


def simulation_pc(total_score):
    """
    Simulate a turn by computer player and return its turn score before
    the total score is smaller than 100.
    """

    # strategy
    global inning_cnt
    global max_score_each_turn
    if inning_cnt == 1:
        max_score_each_turn = 25
    elif inning_cnt == 2:
        max_score_each_turn = 30
    elif inning_cnt == 3:
        max_score_each_turn = 32
    else:
        max_score_each_turn = 35

    turn_score = 0
    while True:
        outcome0 = random.randint(MIN_VAL_OF_A_DIE, MAX_VAL_OF_A_DIE)
        outcome1 = random.randint(MIN_VAL_OF_A_DIE, MAX_VAL_OF_A_DIE)
        print(" - rolled a " + str(outcome0) + " and a " + str(outcome1))

        if outcome0 == 1 and outcome1 != 1:
            turn_score = 0
            print("Pigged out!")
            break
        if outcome0 != 1 and outcome1 == 1:
            turn_score = 0
            print("Pigged out!")
            break

        if outcome0 == 1 and outcome1 == 1:
            turn_score = turn_score + 25
        elif outcome0 == outcome1:
            turn_score = turn_score + 4 * outcome0
        else:
            turn_score = turn_score + outcome0 + outcome1

        if turn_score + total_score >= MAX_SCORE:
            break
        if turn_score >= max_score_each_turn:
            break

    return turn_score


if __name__ == "__main__":

    # initialization
    random.seed()
    print("===============BIG PIG===============\n")
    your_score = 0
    computer_score = 0
    print("Your score: " + str(your_score))
    print("Computer's score: " + str(computer_score))

    # simulation
    frame = random.randint(0, 1)
    if frame == 0:
        while True:
            # An inning started from you.
            inning_cnt += 1
            print("----------Inning " + str(inning_cnt) + "----------")
            print("")
            print("It's your turn")

            your_inning_score = simulation_human()
            your_score += your_inning_score

            print("Total turn score = " + str(your_inning_score))
            print("")
            print("Your score: " + str(your_score))
            print("Computer's score: " + str(computer_score))
            print("")
            print("It's computer's turn")

            computer_inning_score = 0
            if your_score >= MAX_SCORE:
                computer_inning_score = simulation_pc_as_second_player(
                    computer_score, your_score)
                computer_score += computer_inning_score
            else:
                computer_inning_score = simulation_pc(computer_score)
                computer_score += computer_inning_score

            print("Total turn score = " + str(computer_inning_score))
            print("")
            print("Your score: " + str(your_score))
            print("Computer's score: " + str(computer_score))
            print("")
            print("----------Inning finished----------")

            if your_score < MAX_SCORE and computer_score < MAX_SCORE:
                continue
            if not (your_score == computer_score and your_score >= MAX_SCORE):
                break
    else:
        while True:
            # An inning started from the computer
            inning_cnt += 1
            print("----------Inning " + str(inning_cnt) + "----------")
            print("")
            print("It's computer's turn")

            computer_inning_score = 0
            if your_score == computer_score and your_score >= MAX_SCORE:
                computer_inning_score = simulation_pc_as_first_player()
                computer_score += computer_inning_score
            elif your_score > computer_score >= MAX_SCORE:
                computer_inning_score = simulation_pc_as_second_player(
                    computer_score, your_score)
                computer_score += computer_inning_score
            else:
                computer_inning_score = simulation_pc(computer_score)
                computer_score += computer_inning_score

            print("Total turn score = " + str(computer_inning_score))
            print("")
            print("Your score: " + str(your_score))
            print("Computer's score: " + str(computer_score))
            print("")
            print("It's your turn")

            your_inning_score = simulation_human()
            your_score += your_inning_score

            print("Total turn score = " + str(your_inning_score))

            print("")
            print("Your score: " + str(your_score))
            print("Computer's score: " + str(computer_score))
            print("")
            print("----------Inning finished----------")

            if your_score < MAX_SCORE and computer_score < MAX_SCORE:
                continue
            if not (your_score == computer_score and your_score >= MAX_SCORE):
                break

    print("")
    if your_score > computer_score:
        print("You win!")
    else:
        print("Computer wins!")
