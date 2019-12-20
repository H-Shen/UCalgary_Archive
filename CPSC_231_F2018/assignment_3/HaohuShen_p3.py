#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program saves all movie items and its relative rating data into multiple
arrays, calculates every movie's average rating and variance, as well as
sorts the arrays and print out contents according tothe requirements.
Fractions would be used to express average ratings and variances in order to
avoid absolute errors while being sorted.

    Author: Haohu Shen
    Date:   October 2018
"""

import os
import sys
from copy import deepcopy
from fractions import Fraction


def arguments_validation():
    """
    Validate the external input arguments, including the total number of
    arguments as well as the existence and the reading permission of files.
    Any invalid element will make the function return False, otherwise return
    True.
    """
    global file_movie_list
    global file_class_rating

    try:
        assert (len(sys.argv) == 3)
        file_movie_list = sys.argv[1]
        file_class_rating = sys.argv[2]
        assert (os.access(file_movie_list, os.F_OK))
        assert (os.access(file_class_rating, os.F_OK))
        assert (os.access(file_movie_list, os.R_OK))
        assert (os.access(file_class_rating, os.R_OK))
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


def calculate_average_rating():
    """
    Calculate the average rating of each movie in movie_list and assign the
    result to the corresponding index in average_point_cnt. The average rating
    of the movies had been never seen would be ignored.
    """
    global movie_count
    global stats

    for i in range(movie_count):
        if stats[i][6] == 0:
            continue
        total_points = stats[i][1] * 1 + stats[i][2] * 2 + \
                       stats[i][3] * 3 + stats[i][4] * 4 + \
                       stats[i][5] * 5
        stats[i][7] = Fraction(total_points, stats[i][6])


def calculate_variance():
    """
    Calculate the variance of each movie in movie_list using the formula in
    the description and assign the result to the corresponding index in
    variance_cnt. When only 1 person rated the movie (N = 1) would cause
    the program terminated immediately.
    """
    global movie_count
    global stats

    for i in range(movie_count):
        if stats[i][6] == 1:
            print("Rating data invalid, program terminated.")
            quit()
        numerator = stats[i][1] * (1 - stats[i][7]) ** 2 + \
                    stats[i][2] * (2 - stats[i][7]) ** 2 + \
                    stats[i][3] * (3 - stats[i][7]) ** 2 + \
                    stats[i][4] * (4 - stats[i][7]) ** 2 + \
                    stats[i][5] * (5 - stats[i][7]) ** 2 + \
                    stats[i][0] * (0 - stats[i][7]) ** 2
        denominator = stats[i][6] - 1
        stats[i][8] = Fraction(numerator, denominator)


def average_number_of_movies_seen():
    """
    Calculate the average number of movies the class had seen.
    If no students see movies or no movies in the movie list, a zero
    will be returned. The return values is a string.
    """
    global movie_count
    global student_count
    global stats

    if student_count == 0 or movie_count == 0:
        return '0'

    total_movies_seen = 0
    for i in stats:
        total_movies_seen += i[6]

    if total_movies_seen % student_count == 0:
        return str(total_movies_seen // student_count)
    return "%.3f" % (total_movies_seen / student_count)


def most_popular_movies():
    """
    Sort the movies by the most number of nonzero ratings, store and
    return the result as a list including no more than five entries.
    """
    global movie_count
    global movie_list
    global stats

    result = list()
    if movie_count == 0:
        return result

    for i in range(movie_count):
        result.append((movie_list[i], stats[i][6]))

    result.sort(key=lambda x: -x[1])
    result_movie_list = list()
    for i in range(movie_count):
        result_movie_list.append(result[i][0])

    if movie_count <= 5:
        return result_movie_list
    return result_movie_list[:5]


def least_popular_movies():
    """
    Sort the movies by the most number of zero ratings, store and
    return the result as a list including no more than five entries.
    """
    global movie_count
    global movie_list
    global stats

    result = list()
    if movie_count == 0:
        return result

    for i in range(movie_count):
        result.append((movie_list[i], stats[i][0]))

    result.sort(key=lambda x: -x[1])
    result_movie_list = list()
    for i in range(movie_count):
        result_movie_list.append(result[i][0])

    if movie_count <= 5:
        return result_movie_list
    return result_movie_list[:5]


def highest_rated_movies():
    """
    Sort the movies by the highest average ratings with at least 10 nonzero
    ratings, store and return the result as a list including no more than
    five entries.
    """
    global movie_count
    global movie_list
    global stats

    result = list()
    if movie_count == 0:
        return result

    temp_list = list()
    for i in range(movie_count):
        if stats[i][6] >= 10:
            temp_list.append((movie_list[i], stats[i][7]))

    temp_list.sort(key=lambda x: -x[1])
    for i in range(len(temp_list)):
        result.append(temp_list[i][0])

    if len(result) <= 5:
        return result
    return result[:5]


def lowest_rated_movies():
    """
    Sort the movies by the lowest average ratings with at least 10 nonzero
    ratings, store and return the result as a list including no more than
    five entries.
    """
    global movie_count
    global movie_list
    global stats

    result = list()
    if movie_count == 0:
        return list

    temp_list = list()
    for i in range(movie_count):
        if stats[i][6] >= 10:
            temp_list.append((movie_list[i], stats[i][7]))

    temp_list.sort(key=lambda x: x[1])
    for i in range(len(temp_list)):
        result.append(temp_list[i][0])

    if len(result) <= 5:
        return result
    return result[:5]


def most_contentious_movies():
    """
    Sort the movies by the highest variance ratings with at least 10 nonzero
    ratings, store and return the result as a list including no more than
    five entries.
    """
    global movie_count
    global movie_list
    global stats

    result = list()
    if movie_count == 0:
        return result

    temp_list = list()
    for i in range(movie_count):
        if stats[i][6] >= 10:
            temp_list.append((movie_list[i], stats[i][8]))

    temp_list.sort(key=lambda x: -x[1])
    for i in range(len(temp_list)):
        result.append(temp_list[i][0])

    if len(result) <= 5:
        return result
    return result[:5]


file_movie_list = ''
file_class_rating = ''
movie_count = 100
item_count = 9
student_count = 0

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

    """
    build a 2D array for statistics, each entry includes a rating array of
    the corresponding movie, as well as the nonzero rating count, the average
    rating and the variance. For the ith movie in the list:

        stats[i][0] for the count of zero ratings
        stats[i][1] for the count of one ratings
        stats[i][2] for the count of two ratings
        stats[i][3] for the count of three ratings
        stats[i][4] for the count of four ratings
        stats[i][5] for the count of five ratings
        stats[i][6] for the count of nonzero ratings
        stats[i][7] for the average rating
        stats[i][8] for the variance
    """
    stats = [[0 for j in range(item_count)] for i in range(movie_count)]

    # statistics
    with open(file_class_rating, 'r') as f:
        current_line_is_name = True
        for i in f.readlines():
            if current_line_is_name:
                student_count += 1
                current_line_is_name = False
            else:
                rating_list = list()
                if not is_valid(i):
                    print("Rating data invalid, program terminated.")
                    quit()
                for i in range(movie_count):
                    if rating_list[i] == 0:
                        stats[i][0] += 1
                        continue
                    stats[i][rating_list[i]] += 1
                    stats[i][6] += 1
                current_line_is_name = True

    calculate_average_rating()
    calculate_variance()

    # output
    average_num = average_number_of_movies_seen()
    print("The average student in CPSC 231 has seen " + average_num +
          " of the " + str(movie_count) + " movies.")
    print("")

    most_popular_list = most_popular_movies()
    if len(most_popular_list) == 0:
        print("No most popular movies available.")
    elif len(most_popular_list) == 1:
        print("The most popular movie was:")
        print("  " + most_popular_list[0])
    else:
        print("The most popular movies were:")
        for i in most_popular_list:
            print("  " + i)
    print("")

    least_popular_list = least_popular_movies()
    if len(least_popular_list) == 0:
        print("No least popular movies available.")
    elif len(least_popular_list) == 1:
        print("The least popular movie was:")
        print("  " + least_popular_list[0])
    else:
        print("The least popular movies were:")
        for i in least_popular_list:
            print("  " + i)
    print("")

    highest_rated_list = highest_rated_movies()
    if len(highest_rated_list) == 0:
        print("No highest rated movies available.")
    elif len(highest_rated_list) == 1:
        print("The highest rated movie was:")
        print("  " + highest_rated_list[0])
    else:
        print("The highest rated movies were:")
        for i in highest_rated_list:
            print("  " + i)
    print("")

    lowest_rated_list = lowest_rated_movies()
    if len(lowest_rated_list) == 0:
        print("No lowest rated movies available.")
    elif len(lowest_rated_list) == 1:
        print("The lowest rated movie was:")
        print("  " + lowest_rated_list[0])
    else:
        print("The lowest rated movies were:")
        for i in lowest_rated_list:
            print("  " + i)
    print("")

    most_contentious_list = most_contentious_movies()
    if len(most_contentious_list) == 0:
        print("No most contentious movies available.")
    elif len(most_contentious_list) == 1:
        print("The most contentious movie was:")
        print("  " + most_contentious_list[0])
    else:
        print("The most contentious movies were:")
        for i in most_contentious_list:
            print("  " + i)
