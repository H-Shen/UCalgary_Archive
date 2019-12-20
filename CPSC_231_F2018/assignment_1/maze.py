#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program instructs Karel to search the beeper in a maze.

    Date:   September 2018
"""

from karel import *


def turn_right():
    turn_left()
    turn_left()
    turn_left()


begin_karel_program()

while not beepers_present():
    if front_is_clear():
        if right_is_clear():
            turn_right()
        move()

    else:
        if right_is_clear():
            turn_right()
            move()
        else:
            turn_left()

end_karel_program()
