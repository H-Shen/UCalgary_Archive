#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program simulates rolling a single six-sided die and
print the result of the roll to the terminal.

    Author: Haohu Shen
    Date:   September 2018
"""

import random

# constants
MIN_VAL_OF_A_DIE = 1
MAX_VAL_OF_A_DIE = 6

if __name__ == "__main__":
    # initialization
    random.seed()

    # simulation
    result = random.randint(MIN_VAL_OF_A_DIE, MAX_VAL_OF_A_DIE)
    output = " - rolled a " + str(result)
    print(output)
