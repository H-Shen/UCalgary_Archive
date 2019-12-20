#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program is the client test code of class GuitarString in module
HaohuShen_p3.

    Author: Haohu Shen
    Date:   November 2018
"""

import stdaudio
import stddraw
from HaohuShen_p3_GuitarString import GuitarString
from picture import Picture

a_string = GuitarString(440.00)
c_string = GuitarString(523.25)

p = Picture('cpsc231-guitar.png')
stddraw.picture(p)
stddraw.show(0.0)

escape = False
while not escape:
    # check for and process events
    stddraw._checkForEvents()
    while stddraw.hasNextKeyTyped():
        key = stddraw.nextKeyTyped()
        if key == chr(27):
            escape = True
        elif key == 'a':
            a_string.pluck()
        elif key == 'c':
            c_string.pluck()

    # simulate and play strings
    y = a_string.tick()
    y += c_string.tick()
    stdaudio.playSample(y)
