#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program provides a set of helper functions of drawing capabilities in
the Hangman Games using stddraw.

    Author: Haohu Shen
    Date:   October 2018
"""

import stddraw

# constants
BAD_GUESS_MAX_LENGTH = 17


def draw_background(step, x=0.37, y=0.3):
    """
    Draw the (partial) hangman and the gallow according to the corresponding
    step. The floor will be drawn at the beginning of the game.
    """

    # predefine points
    x0, y0 = x + 0.15, y + 0.5
    x1, y1 = x0 - 0.195, y0 - 0.15 - 0.03
    x2, y2 = x0 - 0.195, y0 - 0.3
    x3, y3 = x0 - 0.195 - 0.02, y0 - 0.1 + 0.02
    x4, y4 = x0 - 0.195 + 0.015, y0 - 0.1 + 0.02
    x5, y5 = x0 - 0.195, y0 - 0.110

    # floor
    stddraw.setPenRadius(0.002)
    stddraw.line(x, y, x - 0.3, y)
    stddraw.line(x, y, x + 0.5, y)
    stddraw.line(x - 0.2, y, x - 0.2, y + 0.06)
    stddraw.line(x + 0.2, y, x + 0.2, y + 0.06)
    stddraw.line(x - 0.2, y + 0.06, x + 0.2, y + 0.06)

    if step >= 1:
        # head
        stddraw.setPenRadius()
        stddraw.circle(x0 - 0.195, y0 - 0.1, 0.05)
    if step >= 2:
        # torso
        stddraw.line(x0 - 0.195, y0 - 0.15, x0 - 0.195, y0 - 0.3)
    if step >= 3:
        # left arm
        stddraw.line(x1, y1, x1 - 0.06, y1 - 0.06)
    if step >= 4:
        # right arm
        stddraw.line(x1, y1, x1 + 0.06, y1 - 0.06)
    if step >= 5:
        # left leg
        stddraw.line(x2, y2, x2 - 0.04, y2 - 0.08)
    if step >= 6:
        # right leg
        stddraw.line(x2, y2, x2 + 0.04, y2 - 0.08)
    if step >= 7:
        # gallows
        stddraw.setPenRadius(0.002)
        stddraw.line(x + 0.15, y + 0.06, x + 0.15, y + 0.5)
        stddraw.line(x0, y0, x0 - 0.2, y0)
        stddraw.line(x0 - 0.2, y0, x0 - 0.2, y0 - 0.05)
    if step >= 8:
        # face
        stddraw.setPenRadius(0.002)
        stddraw.line(x3, y3, x3 + 0.005, y3 + 0.007)
        stddraw.line(x3, y3, x3 + 0.005, y3 - 0.007)
        stddraw.line(x3, y3, x3 - 0.005, y3 + 0.007)
        stddraw.line(x3, y3, x3 - 0.005, y3 - 0.007)
        stddraw.line(x4, y4, x4 + 0.005, y4 + 0.007)
        stddraw.line(x4, y4, x4 + 0.005, y4 - 0.007)
        stddraw.line(x4, y4, x4 - 0.005, y4 + 0.007)
        stddraw.line(x4, y4, x4 - 0.005, y4 - 0.007)
        stddraw.line(x5, y5, x5 - 0.015, y5)
        stddraw.line(x5, y5, x5 + 0.015, y5)


def print_title(title, x=0.5, y=0.35):
    """
    Print the title of the game on the upper of the window.
    """
    stddraw.setFontFamily('Helvetica')
    stddraw.setFontSize(23)
    stddraw.text(x, y + 0.55, title)


def draw_boundary():
    """
    Print the boundary line between the interface of two players.
    """
    stddraw.setPenRadius(0.001)
    stddraw.line(1, 0, 1, 1)


def remaining_guess(remain_blanks, guess_remaining, show_prompt=True):
    """
    Show the remaining guess in the canvas.
    """
    stddraw.setFontFamily('Helvetica')
    stddraw.setFontSize(18)

    x, y = 0.5, 0.35
    if guess_remaining > 1:
        stddraw.text(x + 0.25, y + 0.42,
                     "Remain: " + str(guess_remaining) + " Guesses")
    else:
        stddraw.text(x + 0.25, y + 0.42,
                     "Remain: " + str(guess_remaining) + " Guess")

    if remain_blanks > 1:
        stddraw.text(x + 0.25, y + 0.35,
                     "Remain: " + str(remain_blanks) + " Blanks")
    else:
        stddraw.text(x + 0.25, y + 0.35,
                     "Remain: " + str(remain_blanks) + " Blank")

    stddraw.setFontFamily('Courier')
    stddraw.setFontSize(14)

    if show_prompt:
        stddraw.text(x + 0.25, y + 0.27, "Press your next guess...")


def current_word_show(current_word, x=0.5, y=0.18):
    """
    Show the current guessed characters in the canvas.
    """
    stddraw.setFontFamily('Helvetica')
    stddraw.setFontSize(30)

    stddraw.text(x, y, ' '.join(current_word))


def current_bad_guess_show(bad_guess_so_far, x=0.5, y=0.08):
    """
    Show the incorrect guessed characters in the canvas.
    """
    global BAD_GUESS_MAX_LENGTH

    stddraw.setFontFamily('Helvetica')
    stddraw.setFontSize(18)

    if len(bad_guess_so_far) > BAD_GUESS_MAX_LENGTH:
        stddraw.text(x, y, "Your bad guesses so far :   " +
                     ''.join(bad_guess_so_far))
    else:
        stddraw.text(x, y, "Your bad guesses so far :   " +
                     ' '.join(bad_guess_so_far))


def game_over_show(word_to_guess, x=0.5, y=0.08):
    """
    Show the contents indicates that the game is over in the canvas.
    """
    stddraw.setFontFamily('Helvetica')
    stddraw.setFontSize(18)
    stddraw.text(x, y, "Sorry... You didn't guess the secret word: " +
                 ''.join(word_to_guess))


def game_win_show(word_to_guess, evil_version=False, x=0.5, y=0.08):
    """
    Show the contents indicates the user wins the game in the canvas.
    """
    stddraw.setFontFamily('Helvetica')
    stddraw.setFontSize(18)

    if evil_version:
        stddraw.text(x, y, "Awesome (^O^)/!! You guessed the secret word: " +
                     ''.join(word_to_guess))
    else:
        stddraw.text(x, y, "Congratulations! You guessed the secret word: " +
                     ''.join(word_to_guess))
