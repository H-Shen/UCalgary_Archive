#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program generates 20 seconds of crashing ocean waves using blending
method and plays the sound through the computer's audio output.

This version replaces the original solution of problem2, the latter one is
obsoleted and should not be graded.

    Author: Haohu Shen
    Date:   November 2018
"""

import random
from decimal import Decimal
from math import sin, pi, sqrt

import stdaudio


def in_range(array):
    """
    Return True if every value in array is between -1 and 1 with the precision
    defaulting to 28 places. Otherwise return False.
    """
    for i in array:
        if not (Decimal(-1) <= Decimal(i) <= Decimal(1)):
            return False
    return True


def fill_brownian(a, i0, i1, variance, scale):
    """
    Recursive function that fills an array with values forming a
    Brownian bridge between indices i0 and i1 (exclusive).
    Assumes that a[i0] and a[i1] are values of the fixed endpoints.
    :param a: Array to fill with Brownian bridge.
    :param i0: Index of first endpoint.
    :param i1: Index of second endpoint
    :param variance: Variance of the normal distribution from
    which to sample a displacement.
    :param scale: Scale factor to reduce variance for next level.
    """
    if i0 == i1 - 1:
        return

    im = (i0 + i1) // 2
    a[im] = (a[i0] + a[i1]) / 2 + random.gauss(0, sqrt(variance))
    fill_brownian(a, i0, im, variance / scale, scale)
    fill_brownian(a, im, i1, variance / scale, scale)


def reset_to_zero(a):
    """
    Reset all elements in the array to 0.0 and return.
    """
    for i in range(len(a)):
        a[i] = 0.0
    return a


def nb(variance, scale, chunk_size, sps, interval):
    """
    Generate a brownian bridge whose length, variance and scale are assigned.
    """
    a = [0.0 for i in range(sps * interval)]

    for i in range(1, 100 + 1):

        begin = (i - 1) * chunk_size
        end = i * chunk_size

        if i == 100:
            end -= 1

        # check if every element in the noise array meets the requirement.
        while True:
            fill_brownian(a, begin, end, variance / scale, scale)
            if in_range(a[begin:end]):
                break
            a[begin:end] = reset_to_zero(a[begin:end])

    return a


def nw(amplitude, length):
    """
    Generate an array of given random values with a given amplitude and length.
    """
    return [random.uniform(-amplitude, amplitude) for i in range(length)]


if __name__ == "__main__":

    # initialization
    sps = 44100
    chunk_size = 8820
    variance = 0.05
    hurst_exponent = 0.5
    f = 0.25
    amplitude = 0.25
    scale = 2 ** (2 * hurst_exponent)
    interval = 20

    # generate the brown noises
    nb_array = nb(variance, scale, chunk_size, sps, interval)

    # generate the white noises
    nw_array = nw(amplitude, sps * interval)

    # blend two types of noises
    result = list()
    for i in range(1, sps * interval + 1):
        t = i / sps
        s = sin(pi * f * t) ** 6
        result.append((1 - s) * nb_array[i - 1] + s * nw_array[i - 1])

    # output
    stdaudio.playSamples(result)
