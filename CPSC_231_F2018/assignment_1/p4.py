#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program instructs Karel to find the midpoint of the 1st street.

    Date:   September 2018
"""

from karel import *


def turn_around():
    """
    Make Karel turn around.
    """
    for i in range(2):
        turn_left()


def pick_beepers_on_half_street():
    """
    Pick all beepers on the half or nearly half of the street.
        pre-condition:  Karel is standing on the half of the street.
        post-condition: Karel has picked half of beepers on the street.
    """
    while front_is_clear():
        if beepers_present():
            pick_beeper()
        move()


def pick_beepers_on_another_half_street():
    """
    Pick all beepers on another half of the street.
        pre-condition:  After the execution of pick_beepers_on_half_street()
        post-condition: Karel has picked another half of beepers on the street.
    """
    if beepers_present():
        pick_beeper()
    turn_around()
    while front_is_clear():
        move()
    turn_around()
    while beepers_present():
        pick_beeper()
        move()


begin_karel_program()

# Handle the corner case when the world has only 1 avenue.
if not front_is_clear():
    put_beeper()
    end_karel_program()

# Put a beeper in the left corner and the right corner respectively.
for i in range(2):
    while front_is_clear():
        move()
    put_beeper()
    turn_around()
    move()

# Travel through the 1st street back and forth, putting a deeper when Karel is
# beside a beeper, stop when no empty avenues.
while beepers_in_bag():
    if beepers_present():
        turn_around()
        move()
        pick_beepers_on_half_street()
        pick_beepers_on_another_half_street()
        put_beeper()
        end_karel_program()
    while not beepers_present():
        move()
    turn_around()
    move()
    put_beeper()
    move()

end_karel_program()
