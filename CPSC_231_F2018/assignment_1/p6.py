#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program instructs Karel to find and pick up the second-largest pile
of beepers.

    Date:   September 2018
"""

from karel import *


def face_north():
    """
    Make Karel face to north.
    """
    while not facing_north():
        turn_left()


def face_south():
    """
    Make Karel face to south.
    """
    while not facing_south():
        turn_left()


def face_east():
    """
    Make Karel face to east.
    """
    while not facing_east():
        turn_left()


def face_west():
    """
    Make Karel face to west.
    """
    while not facing_west():
        turn_left()


def turn_around():
    """
    Make Karel turn around.
    """
    for i in range(2):
        turn_left()


begin_karel_program()

# Allocate all beepers to the corresponding avenue.
while front_is_clear():
    move()
    if beepers_present():
        while beepers_present():
            pick_beeper()
        face_north()
        put_beeper()
        while beepers_in_bag():
            move()
            put_beeper()
        face_south()
        while front_is_clear():
            move()
        face_east()

# Make Karel go to the upper-right corner.
face_north()
while front_is_clear():
    move()
face_west()

# Sweep streets one by one, up to down, and stop when discover a beeper.
while not beepers_present():
    if not front_is_clear():
        face_south()
        move()
        face_east()
        if not front_is_clear():
            turn_around()
    move()

# Collect all beepers in this avenue and transfer them to the north.
face_south()
while front_is_clear():
    pick_beeper()
    move()
pick_beeper()
face_north()
while front_is_clear():
    move()
while beepers_in_bag():
    put_beeper()
face_south()
move()
face_west()
while front_is_clear():
    move()
face_east()

# Sweep streets one by one, from the current street to down,
# and stop when discover a beeper.
while not beepers_present():
    if not front_is_clear():
        face_south()
        move()
        face_east()
        if not front_is_clear():
            turn_around()
    move()

# Make Karel pick all beepers in this avenue.
face_south()
while front_is_clear():
    pick_beeper()
    move()
pick_beeper()

# Make Karel go to the lower-left corner.
face_west()
while front_is_clear():
    move()

# Make Karel move through the 1st street and sweep the respective
# avenue step by step.
face_east()
while beepers_in_bag():
    if not front_is_clear():
        if not left_is_clear():
            end_karel_program()
        if not right_is_clear():
            end_karel_program()
    face_north()
    while front_is_clear():
        move()
        if beepers_present():
            pick_beeper()
            face_south()
            while front_is_clear():
                move()
            put_beeper()
            face_north()
    face_south()
    while front_is_clear():
        move()
    face_east()
    move()

end_karel_program()
