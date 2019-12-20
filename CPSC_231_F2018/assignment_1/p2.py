#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This Karel the Robot program instructs Karel to write the initials of the
author by putting beepers.
This program is tested in the world described in p2-empty-16x8.w and
p2-empty-25x10.w.

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

move()

# Write the letter 'H'
face_north()
for i in range(7):
    if front_is_clear():
        move()
        put_beeper()
face_south()
for i in range(3):
    if front_is_clear():
        move()
face_east()
for i in range(3):
    if front_is_clear():
        move()
        put_beeper()
move()
face_north()
for i in range(3):
    if front_is_clear():
        move()
face_south()
for i in range(7):
    if front_is_clear():
        put_beeper()
        move()

# Write the letter 'S'
face_north()
move()
face_east()
move()
move()
face_north()
for i in range(1):
    if front_is_clear():
        move()
        put_beeper()
face_south()
move()
face_east()
for i in range(3):
    if front_is_clear():
        move()
        put_beeper()
move()
face_north()
for i in range(2):
    if front_is_clear():
        move()
        put_beeper()
move()
face_west()
for i in range(3):
    if front_is_clear():
        move()
        put_beeper()
move()
face_north()
for i in range(2):
    if front_is_clear():
        move()
        put_beeper()
move()
face_east()
for i in range(3):
    if front_is_clear():
        move()
        put_beeper()
move()

end_karel_program()
