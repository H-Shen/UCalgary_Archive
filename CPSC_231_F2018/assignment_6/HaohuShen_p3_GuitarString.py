#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program defines plucking a single guitar string by implementing the
Karplus-Strong algorithm.

    Author: Haohu Shen
    Date:   November 2018
"""

import sys
from collections import deque
from math import ceil
from random import uniform


class GuitarString(object):
    """
    Simulate a guitar string that vibrates at the given fundamental
    frequency (pitch) f.
    """

    def __init__(self, f):
        try:
            self.f = f
            self.p = ceil(44100 / self.f)
            assert self.p >= 2
        except:
            print("Arguments invalid, program terminated.")
            sys.exit()

        self.wavetable = deque([0.0 for i in range(self.p)])

    def pluck(self):
        """
        Pluck the guitar string by replacing all values in the wavetable with
        white noise. Use random values between -0.5 and +0.5.
        """
        for i in range(self.p):
            self.wavetable[i] = uniform(-0.5, 0.5)

    def tick(self):
        """
        Advance the Karplus-Strong simulation by one step, returning the
        sample value that was computed and added to the wavetable.
        """
        val = self.wavetable.popleft()
        val = (val + self.wavetable[0]) * 0.996 / 2
        self.wavetable.append(val)
        return val
