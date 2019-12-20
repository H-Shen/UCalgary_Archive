#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program saves all movie items and its relative rating data into multiple
arrays, calculates the similarity of ratings of the user and other raters.
Output a list of personalized movie recommendations according to the order in
the original movie list.

    Author: Haohu Shen
    Date:   October 2018
"""

import os
import sys
from copy import deepcopy
from functools import cmp_to_key

# constants
RATING_TO_VALUE = {0: 0, 1: -5, 2: -3, 3: 1, 4: 3, 5: 5}
MAX_RECOMMENDATION = 12


class Person(object):
    """
    Define a class named Person to save all relative data of an individual.
    """

    # initializer
    def __init__(self):
        self._rating_list = None
        self._similarity = 0

    # getter
    def get_rating_list(self):
        return self._rating_list

    def get_similarity(self):
        return self._similarity

    # setter
    def set_rating_list(self, rating_list):
        self._rating_list = deepcopy(rating_list)

    def set_similarity(self, similarity):
        self._similarity = similarity


def arguments_validation():
    """
    Validate the external input arguments, including the total number of
    arguments as well as the existence and the reading permission of files.
    Any invalid element will make the function return False, otherwise return
    True.
    """
    global file_movie_list
    global file_class_rating
    global file_my_rating_list

    try:
        assert (len(sys.argv) == 4)
        file_movie_list = sys.argv[1]
        file_class_rating = sys.argv[2]
        file_my_rating_list = sys.argv[3]
        assert (os.access(file_movie_list, os.F_OK))
        assert (os.access(file_class_rating, os.F_OK))
        assert (os.access(file_my_rating_list, os.F_OK))
        assert (os.access(file_movie_list, os.R_OK))
        assert (os.access(file_class_rating, os.R_OK))
        assert (os.access(file_my_rating_list, os.R_OK))
    except:
        return False
    return True


def is_valid(rating_string):
    """
    Validate every element in the rating list, including the length of list
    and whether every element should be an integer between 0 and 5. Any
    invalid element will make the function return False, otherwise return
    True. The global variable rating_list will be changed.
    """
    global rating_list
    global movie_count

    rating_list = deepcopy(rating_string)
    try:
        rating_list = list(map(int, rating_list.rstrip().split(',')))
        if len(rating_list) != movie_count:
            return False
        for i in rating_list:
            if i < 0 or i > 5:
                return False
    except:
        return False
    return True


def similarity(a, b):
    return sum(a[i] * b[i] for i in range(len(a)))


def data_conversion(rating_list):
    global RATING_TO_VALUE
    for i in range(len(rating_list)):
        rating_list[i] = RATING_TO_VALUE[rating_list[i]]
    return rating_list


file_movie_list = ''
file_class_rating = ''
file_my_rating_list = ''
movie_count = 0

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

    # parse file_my_rating_list
    my_list = list()
    my_name = ""
    with open(file_my_rating_list, 'r') as f:
        tmp_list = f.readlines()
        my_name = tmp_list[0].rstrip()
        if not is_valid(tmp_list[1]):
            print("Rating data invalid, program terminated.")
            quit()
    my_list = data_conversion(list(map(int, tmp_list[1].rstrip().split(','))))

    # parse other people's rating list
    other_person_rating_list = list()
    with open(file_class_rating, 'r') as f:
        tmp_list = f.readlines()
        current_line_is_name = True
        username = ""
        for i in tmp_list:
            if current_line_is_name:
                username = i.lstrip().rstrip()
                current_line_is_name = False
            else:
                if not is_valid(i):
                    print("Rating data invalid, program terminated.")
                    quit()
                if username == my_name:
                    current_line_is_name = True
                    continue
                rating_list = list(map(int, i.rstrip().split(',')))
                rating_list = data_conversion(rating_list)
                tmp_person = Person()
                tmp_person.set_rating_list(rating_list)
                tmp_person.set_similarity(similarity(rating_list, my_list))
                other_person_rating_list.append(tmp_person)
                current_line_is_name = True

    # calculation of the maximal similarity
    max_similarity = max(other_person_rating_list, key=cmp_to_key(lambda x, y:
                                                                  x.get_similarity() - y.get_similarity())).get_similarity()

    # obtain the recommended movies
    recommend = list()
    for i in other_person_rating_list:
        if i.get_similarity() == max_similarity:
            max_rate = -1
            for j in range(movie_count):
                if my_list[j] == 0 and i.get_rating_list()[j] != 0:
                    max_rate = max(max_rate, i.get_rating_list()[j])
            for j in range(movie_count):
                if my_list[j] == 0 and i.get_rating_list()[j] != 0:
                    if i.get_rating_list()[j] == max_rate:
                        recommend.append(movie_list[j])
    recommend = sorted(set(recommend), key=lambda x: movie_list.index(x))

    # output
    print("Hello " + my_name + "!")
    print("")
    print("From your ratings of our " + str(movie_count) + " movies, "
                                                           "our CPSC 231 class believes that you might also like:")
    for i in range(len(recommend)):
        if i == MAX_RECOMMENDATION:
            break
        print("  " + recommend[i])
