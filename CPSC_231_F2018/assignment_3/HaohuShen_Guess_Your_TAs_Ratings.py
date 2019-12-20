#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
The program guesses TAs' ratings by implementing Slope One algorithm.

    Author: Haohu Shen
    Date:   October 2018
"""

import os
import sys
from copy import deepcopy
from fractions import Fraction
from math import floor

file_movie_list = ''
file_class_rating = ''
file_TA_rating_list = ''
out_TA_rating_list = ''
movie_count = 0
TA_name = ''


def arguments_validation():
    """
    Validate the external input arguments, including the total number of
    arguments as well as the existence and the reading permission of files.
    Any invalid element will make the function return False, otherwise return
    True.
    """
    global file_movie_list
    global file_class_rating
    global file_TA_rating_list
    global out_TA_rating_list

    try:
        assert (len(sys.argv) == 5)
        file_movie_list = sys.argv[1]
        file_class_rating = sys.argv[2]
        file_TA_rating_list = sys.argv[3]
        out_TA_rating_list = sys.argv[4]
        assert (os.access(file_movie_list, os.F_OK))
        assert (os.access(file_class_rating, os.F_OK))
        assert (os.access(file_TA_rating_list, os.F_OK))
        assert (os.access(file_movie_list, os.R_OK))
        assert (os.access(file_class_rating, os.R_OK))
        assert (os.access(file_TA_rating_list, os.R_OK))
    except:
        return False
    return True


def avg_diff(class_rating_list, m, n):
    """
    Calculate the average difference between mth item and nth item.
    """
    result = Fraction(0, 1)
    for i in range(len(class_rating_list)):
        result = result + class_rating_list[i][m] - class_rating_list[i][n]
    result = result / len(class_rating_list)
    return result


if __name__ == "__main__":

    if not arguments_validation():
        print("Arguments invalid, program terminated.")
        quit()

    # initialization
    movie_list = list()
    with open(file_movie_list, 'r') as f:
        for i in f.readlines():
            movie_name = i.rstrip().lstrip()
            if len(movie_name) != 0:
                movie_list.append(movie_name)
    movie_count = len(movie_list)

    class_rating_list = list()
    with open(file_class_rating, 'r') as f:
        tmp_list = f.readlines()
        current_line_is_name = True
        for i in tmp_list:
            if current_line_is_name:
                current_line_is_name = False
            else:
                rating_list = list(map(int, i.rstrip().split(',')))
                class_rating_list.append(deepcopy(rating_list))
                current_line_is_name = True

    TA_rating_list = list()
    with open(file_TA_rating_list, 'r') as f:
        tmp_list = f.readlines()
        TA_name = tmp_list[0].rstrip()
        rating_list = list(map(int, tmp_list[1].rstrip().split(',')))
        TA_rating_list = deepcopy(rating_list)

    # preprocess, would cost some time
    avg_diff_map = dict()
    for m in range(movie_count - 1):
        for n in range(m + 1, movie_count):
            avg_diff_map[(m, n)] = avg_diff(class_rating_list, m, n)
            avg_diff_map[(n, m)] = -avg_diff_map[(m, n)]

    TA_rating_list_not_hide = list()
    TA_rating_list_hide_idx = list()
    for i in range(len(TA_rating_list)):
        if TA_rating_list[i] != -1:
            TA_rating_list_not_hide.append((TA_rating_list[i], i))
        else:
            TA_rating_list_hide_idx.append(i)

    # process the result
    result = list()
    for i in range(len(TA_rating_list_hide_idx)):
        guess = Fraction(0, 1)
        for j in TA_rating_list_not_hide:
            guess = guess + j[0] - avg_diff_map[(j[1], TA_rating_list_hide_idx[i])]
        guess /= len(TA_rating_list_not_hide)

        if guess - floor(guess) < Fraction(1, 2):
            guess = floor(guess)
        else:
            guess = floor(guess) + 1

        if guess < 0:
            guess = 0
        elif guess > 5:
            guess = 5

        result.append(guess)

    pos = 0
    for i in TA_rating_list_hide_idx:
        TA_rating_list[i] = result[pos]
        pos += 1

    # output
    with open(out_TA_rating_list, 'w') as f:
        f.write(TA_name + '\n')
        f.write(','.join(list(map(str, TA_rating_list))) + '\n')
