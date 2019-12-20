#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
Double beepers.

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
    turn_left()
    turn_left()


begin_karel_program()

move()
face_south()
while beepers_present():
    pick_beeper()
    face_north()
    move()
    put_beeper()
    put_beeper()
    face_south()
    move()
face_north()
move()
while beepers_present():
    pick_beeper()
    face_south()
    move()
    put_beeper()
    face_north()
    move()

end_karel_program()
