#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program maps keys on the keyboard to guitar strings corresponding to
music to music notes and allows the user to play three full octaves of notes.

    Author: Haohu Shen
    Date:   November 2018
"""

import sys
from collections import deque
from math import ceil
from random import uniform

import stddraw
from picture import Picture
from stdaudio import playSample

# constant
LENGTH = 37


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

        # use a local variable instead of a method to accelerate g.fluck()
        self._append = self.wavetable.append

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
        val = (val + self.wavetable[0]) * 0.498
        self._append(val)
        return val


if __name__ == "__main__":

    # initialization
    freq_list = list()
    count = 0
    if_quit = False
    for i in range(4):
        A_freq = 110 * (2 ** i)
        if if_quit:
            break
        for j in range(12):
            freq_list.append(pow(2, j / 12) * A_freq)
            count += 1
            if count == LENGTH:
                if_quit = True
                break

    string_obj0 = GuitarString(freq_list[0])
    string_obj1 = GuitarString(freq_list[1])
    string_obj2 = GuitarString(freq_list[2])
    string_obj3 = GuitarString(freq_list[3])
    string_obj4 = GuitarString(freq_list[4])
    string_obj5 = GuitarString(freq_list[5])
    string_obj6 = GuitarString(freq_list[6])
    string_obj7 = GuitarString(freq_list[7])
    string_obj8 = GuitarString(freq_list[8])
    string_obj9 = GuitarString(freq_list[9])
    string_obj10 = GuitarString(freq_list[10])
    string_obj11 = GuitarString(freq_list[11])
    string_obj12 = GuitarString(freq_list[12])
    string_obj13 = GuitarString(freq_list[13])
    string_obj14 = GuitarString(freq_list[14])
    string_obj15 = GuitarString(freq_list[15])
    string_obj16 = GuitarString(freq_list[16])
    string_obj17 = GuitarString(freq_list[17])
    string_obj18 = GuitarString(freq_list[18])
    string_obj19 = GuitarString(freq_list[19])
    string_obj20 = GuitarString(freq_list[20])
    string_obj21 = GuitarString(freq_list[21])
    string_obj22 = GuitarString(freq_list[22])
    string_obj23 = GuitarString(freq_list[23])
    string_obj24 = GuitarString(freq_list[24])
    string_obj25 = GuitarString(freq_list[25])
    string_obj26 = GuitarString(freq_list[26])
    string_obj27 = GuitarString(freq_list[27])
    string_obj28 = GuitarString(freq_list[28])
    string_obj29 = GuitarString(freq_list[29])
    string_obj30 = GuitarString(freq_list[30])
    string_obj31 = GuitarString(freq_list[31])
    string_obj32 = GuitarString(freq_list[32])
    string_obj33 = GuitarString(freq_list[33])
    string_obj34 = GuitarString(freq_list[34])
    string_obj35 = GuitarString(freq_list[35])
    string_obj36 = GuitarString(freq_list[36])

    p = Picture('cpsc231-guitar.png')
    stddraw.picture(p)
    stddraw.show(0.0)

    mapping_function = dict()
    mapping_function['q'] = string_obj0
    mapping_function['2'] = string_obj1
    mapping_function['w'] = string_obj2
    mapping_function['e'] = string_obj3
    mapping_function['4'] = string_obj4
    mapping_function['r'] = string_obj5
    mapping_function['5'] = string_obj6
    mapping_function['t'] = string_obj7
    mapping_function['y'] = string_obj8
    mapping_function['7'] = string_obj9
    mapping_function['u'] = string_obj10
    mapping_function['8'] = string_obj11
    mapping_function['i'] = string_obj12
    mapping_function['9'] = string_obj13
    mapping_function['o'] = string_obj14
    mapping_function['p'] = string_obj15
    mapping_function['-'] = string_obj16
    mapping_function['['] = string_obj17
    mapping_function['='] = string_obj18
    mapping_function['z'] = string_obj19
    mapping_function['x'] = string_obj20
    mapping_function['d'] = string_obj21
    mapping_function['c'] = string_obj22
    mapping_function['f'] = string_obj23
    mapping_function['v'] = string_obj24
    mapping_function['g'] = string_obj25
    mapping_function['b'] = string_obj26
    mapping_function['n'] = string_obj27
    mapping_function['j'] = string_obj28
    mapping_function['m'] = string_obj29
    mapping_function['k'] = string_obj30
    mapping_function[','] = string_obj31
    mapping_function['.'] = string_obj32
    mapping_function[';'] = string_obj33
    mapping_function['/'] = string_obj34
    mapping_function['\''] = string_obj35
    mapping_function[' '] = string_obj36

    # play and output
    while True:
        stddraw._checkForEvents()
        while stddraw.hasNextKeyTyped():
            char = stddraw.nextKeyTyped()
            if char == chr(27):
                sys.exit(0)
            if char in mapping_function:
                mapping_function[char].pluck()

        y = string_obj0.tick() + string_obj1.tick() + string_obj2.tick() + \
            string_obj3.tick() + string_obj4.tick() + string_obj5.tick() + \
            string_obj6.tick() + string_obj7.tick() + string_obj8.tick() + \
            string_obj9.tick() + string_obj10.tick() + string_obj11.tick() + \
            string_obj12.tick() + string_obj13.tick() + string_obj14.tick() + \
            string_obj15.tick() + string_obj16.tick() + string_obj17.tick() + \
            string_obj18.tick() + string_obj19.tick() + string_obj20.tick() + \
            string_obj21.tick() + string_obj22.tick() + string_obj23.tick() + \
            string_obj24.tick() + string_obj25.tick() + string_obj26.tick() + \
            string_obj27.tick() + string_obj28.tick() + string_obj29.tick() + \
            string_obj30.tick() + string_obj31.tick() + string_obj32.tick() + \
            string_obj33.tick() + string_obj34.tick() + string_obj35.tick() + \
            string_obj36.tick()

        playSample(y)
