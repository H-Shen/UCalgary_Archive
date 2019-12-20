#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This Karel the Robot program instructs Karel to move outside, pick up
the paper and return to the original position.
Run this program with the world described in p1-newspaper.w.

    Author: Haohu Shen
    Date:   September 2018
"""

from karel import *


def face_north():
    """
    make Karel face to north
    """
    while not facing_north():
        turn_left()


def face_south():
    """
    make Karel face to south
    """
    while not facing_south():
        turn_left()


def face_east():
    """
    make Karel face to east
    """
    while not facing_east():
        turn_left()


def face_west():
    """
    make Karel face to west
    """
    while not facing_west():
        turn_left()


begin_karel_program()

face_north()
move()
move()
face_west()
move()
move()
face_south()
move()
move()
face_east()
move()
pick_beeper()
face_west()
move()
face_north()
move()
move()
face_east()
move()
move()
face_south()
move()
move()
face_east()

end_karel_program()
