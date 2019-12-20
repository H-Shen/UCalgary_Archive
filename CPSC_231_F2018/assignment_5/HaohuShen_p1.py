#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program simulates a visual and interactive Python program that will
facilitate a game of Tic-Tac-Toe between two human players.

    Author: Haohu Shen
    Date:   November 2018
"""

import stddraw

# constants
FIRST_PLAYER_SHAPE = 'X'
SECOND_PLAYER_SHAPE = 'O'
MAX_SQUARES = 9
MAX_ROWS = 3


def width_convert(line_width):
    """
    Adjust the width of the shape to conform to line_width.
    Influenced by Adrian Chen from
    https://piazza.com/class/jl5qtmxzr4s4dg?cid=491.
    """
    return line_width * 600 / (30 * 256)


def draw_lines():
    """
    Draw the line frames of grids.
    """
    stddraw.setPenColor(stddraw.BLACK)
    line_width = 0.1
    stddraw.setPenRadius(line_width)
    stddraw.line(10, 0, 10, 30)
    stddraw.line(20, 0, 20, 30)
    stddraw.line(0, 10, 30, 10)
    stddraw.line(0, 20, 30, 20)
    stddraw.line(0, 0, 30, 0)


def total_square(board):
    """
    Count the number of squares that have been used.
    """
    count = 0
    for i in range(3):
        for j in range(3):
            if board[i][j] != '0':
                count += 1
    return count


def is_board_full(board):
    """
    Check if all squares in the board are used, return True if it is,
    otherwise False.
    """
    global MAX_SQUARES
    return total_square(board) == MAX_SQUARES


def draw_circle(mouse_x, mouse_y):
    """
    Draw the shape of 'O' at the designated location.
    """
    stddraw.setPenColor(stddraw.RED)
    stddraw.setPenRadius(width_convert(0.1))
    stddraw.circle(mouse_x, mouse_y, 3)


def draw_cross(mouse_x, mouse_y):
    """
    Draw the shape of 'X' at the designated location.
    """
    stddraw.setPenColor(stddraw.BLUE)
    stddraw.setPenRadius(0.05)
    stddraw.line(mouse_x - 3, mouse_y - 3, mouse_x + 3, mouse_y + 3)
    stddraw.line(mouse_x - 3, mouse_y + 3, mouse_x + 3, mouse_y - 3)


def coordinate_to_list(mouse_x, mouse_y):
    """
    Convert the location in the grid to the corresponding location in the
    2D array.
    """
    convert_x = {5: 0, 15: 1, 25: 2}
    convert_y = {5: 2, 15: 1, 25: 0}
    mouse_x = convert_x[mouse_x]
    mouse_y = convert_y[mouse_y]

    return (mouse_y, mouse_x)


def list_to_coordinate(i, j):
    """
    Convert the location [i][j] in the 2D array to the corresponding location
    in the grid.
    """
    convert_j = {0: 5, 1: 15, 2: 25}
    convert_i = {2: 5, 1: 15, 0: 25}

    mouse_y = convert_i[i]
    mouse_x = convert_j[j]

    return (mouse_x, mouse_y)


def has_three(board):
    """
    Check if there is a three in a line. Return which player has a three in
    a line, what type of the line as well as the position of the line if there
    is, otherwise return None.
    """
    global MAX_ROWS
    global FIRST_PLAYER_SHAPE
    global SECOND_PLAYER_SHAPE

    # rows
    for i in range(MAX_ROWS):
        s = ''.join(board[i])
        if 'XXX' in s:
            return (FIRST_PLAYER_SHAPE, 'row', i)
        if 'OOO' in s:
            return (SECOND_PLAYER_SHAPE, 'row', i)

    # col
    for i in range(MAX_ROWS):
        s = ""
        for j in range(MAX_ROWS):
            s += board[j][i]
        if 'XXX' in s:
            return (FIRST_PLAYER_SHAPE, 'col', i)
        if 'OOO' in s:
            return (SECOND_PLAYER_SHAPE, 'col', i)

    # main diagonal
    s = ""
    for i in range(MAX_ROWS):
        s += board[i][i]
    if 'XXX' in s:
        return (FIRST_PLAYER_SHAPE, 'main diagonal')
    if 'OOO' in s:
        return (SECOND_PLAYER_SHAPE, 'main diagonal')

    # sub diagonal
    s = ""
    for i in range(MAX_ROWS):
        s += board[i][MAX_ROWS - 1 - i]
    if 'XXX' in s:
        return (FIRST_PLAYER_SHAPE, 'sub diagonal')
    if 'OOO' in s:
        return (SECOND_PLAYER_SHAPE, 'sub diagonal')

    return None


def mouse_coordinate_capture():
    """
    Capture the mouse-click position, if it is legal, then return the center
    of the grid which is clicked, otherwise return None.
    """
    mouse_x = int(stddraw.mouseX())
    mouse_y = int(stddraw.mouseY())
    effective_range_click = False

    for i in range(0, 30, 10):
        if i < mouse_x < i + 10:
            mouse_x = (2 * i + 10) // 2
            for j in range(0, 30, 10):
                if j < mouse_y < j + 10:
                    mouse_y = (2 * j + 10) // 2
                    effective_range_click = True

    if effective_range_click:
        return (mouse_x, mouse_y)
    return None


def draw_state(board):
    """
    Draw the state of the board.
    """
    global MAX_ROWS
    global FIRST_PLAYER_SHAPE
    global SECOND_PLAYER_SHAPE

    draw_lines()
    for i in range(MAX_ROWS):
        for j in range(MAX_ROWS):
            if board[i][j] == FIRST_PLAYER_SHAPE:
                tmp = list_to_coordinate(i, j)
                draw_cross(tmp[0], tmp[1])
            elif board[i][j] == SECOND_PLAYER_SHAPE:
                tmp = list_to_coordinate(i, j)
                draw_circle(tmp[0], tmp[1])


def buttom_message(s):
    """
    Show a message under the board.
    """
    stddraw.setPenColor(stddraw.BLACK)
    stddraw.setFontSize(25)
    stddraw.text(15, -2.5, s)


def winning_line_indicator(board_check):
    """
    If there is a winner, draw the line of the three.
    """
    stddraw.setPenColor(stddraw.DARK_GRAY)
    line_width = 0.03
    stddraw.setPenRadius(line_width)

    conv_i = {2: 5, 1: 15, 0: 25}
    conv_j = {0: 5, 1: 15, 2: 25}

    if board_check[1] == 'main diagonal':
        stddraw.line(5, 25, 25, 5)
    elif board_check[1] == 'sub diagonal':
        stddraw.line(5, 5, 25, 25)
    elif board_check[1] == 'row':
        stddraw.line(5, conv_i[board_check[2]], 25, conv_i[board_check[2]])
    else:
        stddraw.line(conv_j[board_check[2]], 25, conv_j[board_check[2]], 5)


if __name__ == '__main__':

    # initialization
    board = [['0' for j in range(MAX_ROWS)] for i in range(MAX_ROWS)]
    is_first_player_winner = -1
    stddraw.setCanvasSize(600, 700)
    stddraw.setXscale(0, 30)
    stddraw.setYscale(-5, 30)
    first_player_turn = True
    board_check = None

    while True:

        if first_player_turn:
            buttom_message("Now it's the first player's turn...")
        else:
            buttom_message("Now it's the second player's turn...")

        board_check = has_three(board)
        if board_check != None:
            if board_check[0] == SECOND_PLAYER_SHAPE:
                is_first_player_winner = 1
            break

        if is_board_full(board):
            is_first_player_winner = 0
            break

        if stddraw.mousePressed():
            tmp = mouse_coordinate_capture()
            if tmp != None:
                pos = coordinate_to_list(tmp[0], tmp[1])
                if board[pos[0]][pos[1]] == '0':
                    if first_player_turn:
                        board[pos[0]][pos[1]] = FIRST_PLAYER_SHAPE
                        first_player_turn = False
                    else:
                        board[pos[0]][pos[1]] = SECOND_PLAYER_SHAPE
                        first_player_turn = True
        draw_state(board)
        stddraw.show(10)
        stddraw.clear()

    stddraw.clear()
    draw_state(board)

    if board_check != None:
        winning_line_indicator(board_check)

    if is_first_player_winner == -1:
        buttom_message("The first player wins!")
    elif is_first_player_winner == 1:
        buttom_message("The second player wins!")
    else:
        buttom_message("Draw!")

    # wait until the gamers close the window manually
    stddraw.show()
