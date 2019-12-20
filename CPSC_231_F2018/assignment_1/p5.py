#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program instructs Karel to create a checkerboard by travelling each
street back and forth from down to up and putting a beeper alternatively.

    Date:   September 2018
"""

from karel import *


def face_north():
    """
    Make Karel face to north.
    """
    while not facing_north():
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


def put_a_beeper_every_other_step():
    """
    Make Karel move by putting a beeper or passing the
    current position alternatively.
    """
    if not beepers_present():
        move()
        put_beeper()
    else:
        move()


begin_karel_program()

put_beeper()
while front_is_clear():
    put_a_beeper_every_other_step()
face_north()
while beepers_in_bag():
    if not front_is_clear():
        end_karel_program()
    put_a_beeper_every_other_step()
    face_west()
    while front_is_clear():
        put_a_beeper_every_other_step()
    face_north()
    if not front_is_clear():
        end_karel_program()
    put_a_beeper_every_other_step()
    face_east()
    while front_is_clear():
        put_a_beeper_every_other_step()
    face_north()

end_karel_program()
