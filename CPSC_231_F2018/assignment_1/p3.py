#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This Karel the Robot program instructs Karel to move into one corner of the
cross-shaped garden, move and put beepers along the interior walls.

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


def put_beepers_along_interior_walls():
    """
    Make Karel put beepers by moving along the interior walls and
    change the direction if there is no interior wall.
        pre-condition:  Karel is standing on a corner of the garden.
        post-condition: Karel has put beepers along the garden.
    """
    face_south()
    while not left_is_clear():
        put_beeper()
        move()
    face_east()
    move()
    face_north()
    move()
    while front_is_clear():
        put_beeper()
        move()
    face_east()
    while not left_is_clear():
        put_beeper()
        move()
    face_north()
    move()
    face_west()
    move()
    while front_is_clear():
        put_beeper()
        move()
    face_north()
    while not left_is_clear():
        put_beeper()
        move()
    face_west()
    move()
    face_south()
    move()
    while front_is_clear():
        put_beeper()
        move()
    face_west()
    while not left_is_clear():
        put_beeper()
        move()
    face_south()
    move()
    face_east()
    move()
    while not beepers_present():
        put_beeper()
        move()


begin_karel_program()

# Find the right-hand interior walls by traversing back and forth
# each avenue. Once any right-hand interior wall is found, make
# Karel move into one corner of the cross-shaped garden along this wall.
# Then put all beepers along the interior walls.
while beepers_in_bag():
    face_north()
    while front_is_clear():
        move()
        if not right_is_clear():
            while front_is_clear():
                move()
            put_beepers_along_interior_walls()
            end_karel_program()
    turn_around()
    while front_is_clear():
        move()
    face_east()
    if not front_is_clear():
        end_karel_program()
    move()

end_karel_program()
