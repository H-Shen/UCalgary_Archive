#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program creates a static analog clock displaying the currect Mountain
Time by using functions in the library stddraw. The library is downloaded
from https://introcs.cs.princeton.edu/python/code/dist/introcs-1.0.zip.

    Author: Haohu Shen
    Date:   October 2018
"""

import time
from math import sin, cos, radians

import stddraw

# constants
R = 0.5
DISTANCE = R - 0.1
SEC_HAND = R - 0.1
MIN_HAND = R - 0.18
HOUR_HAND = R - 0.24

# obtain current time
current_time = time.localtime()
hour = current_time.tm_hour
minute = current_time.tm_min
second = current_time.tm_sec

# draw outer and inner circles
stddraw.setPenRadius(0.005)
stddraw.circle(R, R, R - 0.01)
stddraw.circle(R, R, R - 0.03)

# draw numbers to indicate hours
stddraw.setFontSize(32)
for i in range(1, 12 + 1):
    angle = radians(i * 30)
    stddraw.text(R + DISTANCE * sin(angle), R + DISTANCE * cos(angle), str(i))

# draw second hand
stddraw.setPenColor(stddraw.BOOK_RED)
stddraw.setPenRadius(0.005)
angle = radians(second * 6)
stddraw.line(R, R, R + SEC_HAND * sin(angle), R + SEC_HAND * cos(angle))

stddraw.setPenColor(stddraw.BLACK)

# draw minute hand
stddraw.setPenRadius(0.007)
increment = second / 60
angle = radians((minute + increment) * 6)
stddraw.line(R, R, R + MIN_HAND * sin(angle), R + MIN_HAND * cos(angle))

# draw hour hand
stddraw.setPenRadius(0.015)
hour %= 12
increment = (second + minute * 60) / 3600
angle = radians((hour + increment) * 30)
stddraw.line(R, R, R + HOUR_HAND * sin(angle), R + HOUR_HAND * cos(angle))

stddraw.show()
stddraw.clear()
