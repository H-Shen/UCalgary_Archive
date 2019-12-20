#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program simulates a brownian bridge with a default variance and a given
Hurst exponent whose length is 129 using recursion.

    Author: Haohu Shen
    Date:   November 2018
"""

import math
import random
import sys

# constants
LENGTH = 129


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
    a[im] = (a[i0] + a[i1]) / 2 + random.gauss(0, math.sqrt(variance))
    fill_brownian(a, i0, im, variance / scale, scale)
    fill_brownian(a, im, i1, variance / scale, scale)


if __name__ == "__main__":

    # arguments validation
    try:
        assert len(sys.argv) == 2
        hurst_exponent = float(sys.argv[1])
    except:
        print("Arguments invalid, program terminated.")
        sys.exit()

    # initialization
    import stddraw

    a = [0.0 for i in range(LENGTH)]
    variance = 0.05
    scale = 2 ** (2 * hurst_exponent)
    fill_brownian(a, 0, LENGTH - 1, variance, scale)

    # output
    scaleX = 2
    scaleY = 100
    stddraw.setCanvasSize(LENGTH * scaleX, 4 * scaleY)
    stddraw.setPenColor(stddraw.BLACK)
    stddraw.setPenRadius(0.001)
    stddraw.setXscale(0, LENGTH * scaleX)
    stddraw.setYscale(-1 * scaleY, 1 * scaleY)
    stddraw.clear()
    for i in range(LENGTH - 1):
        stddraw.line(i * scaleX, a[i] * scaleY, (i + 1) * scaleX, a[i + 1] * scaleY)
    stddraw.show()


class C:
    def __init__(self, x):
        self.x = x

    def m1(self):
        print(self.x)

    def m2(self):
        print(x)


x = 123
y = C(x)
x = 42
y.m1()
y.m2()
