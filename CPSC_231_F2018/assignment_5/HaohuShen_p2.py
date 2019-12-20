#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program simulates a visual and interactive environment that allows a
human player to play a variant of the match-three game, Taffy Tangle.

    Author: Haohu Shen
    Date:   November 2018
"""

import abc
import random
from collections import deque
from copy import deepcopy
from math import sin, cos, pi

import stddraw
from color import Color

# constant
TAFFY_NAME_LIST = ['Diamond', 'Parallelogram', 'Pentagon',
                   'Square', 'Triangle', 'Circle']


class Taffy(abc.ABC):
    """
    Define the base class of taffies.
    """

    def __init__(self, x, y, color):
        self._x = x
        self._y = y
        self._color = color

    def get_x(self):
        return self._x

    def get_y(self):
        return self._y

    def set_x(self, x):
        self._x = x

    def set_y(self, y):
        self._y = y

    @abc.abstractmethod
    def draw(self):
        raise NotImplementedError


class Circle(Taffy):

    def __init__(self, x, y):
        super(Circle, self).__init__(x, y, Color(234, 50, 35))
        self._r = 40

    def get_r(self):
        return self._r

    def draw(self):
        stddraw.setPenColor(self._color)
        stddraw.filledCircle(self._x, self._y, self._r)
        stddraw.setPenColor(stddraw.BLACK)
        stddraw.circle(self._x, self._y, self._r)


class Triangle(Taffy):

    def __init__(self, x, y):
        super(Triangle, self).__init__(x, y, Color(247, 201, 69))

    def draw(self):
        x_set = [self._x - 35, self._x + 35, self._x]
        y_set = [self._y - 10 - 35 / pow(3, 0.5),
                 self._y - 10 - 35 / pow(3, 0.5),
                 self._y - 10 + 70 / pow(3, 0.5)]

        stddraw.setPenColor(self._color)
        stddraw.filledPolygon(x_set, y_set)
        stddraw.setPenColor(stddraw.BLACK)
        stddraw.polygon(x_set, y_set)


class Square(Taffy):

    def __init__(self, x, y):
        super(Square, self).__init__(x, y, Color(255, 253, 84))
        self._r = 35

    def draw(self):
        x_set = [self._x - self._r, self._x + self._r,
                 self._x + self._r, self._x - self._r]
        y_set = [self._y - self._r, self._y - self._r,
                 self._y + self._r, self._y + self._r]

        stddraw.setPenColor(self._color)
        stddraw.filledPolygon(x_set, y_set)
        stddraw.setPenColor(stddraw.BLACK)
        stddraw.polygon(x_set, y_set)


class Pentagon(Taffy):

    def __init__(self, x, y):
        super(Pentagon, self).__init__(x, y, Color(224, 138, 232))
        self._r = 40

    def draw(self):
        x_set = list()
        y_set = list()
        for edge in range(1, 6):
            x_set.append(self._x + self._r * sin(0.4 * (edge - 1) * pi))
            y_set.append(self._y + self._r * cos(0.4 * (edge - 1) * pi))

        stddraw.setPenColor(self._color)
        stddraw.filledPolygon(x_set, y_set)
        stddraw.setPenColor(stddraw.BLACK)
        stddraw.polygon(x_set, y_set)


class Diamond(Taffy):

    def __init__(self, x, y):
        super(Diamond, self).__init__(x, y, Color(117, 250, 76))
        self._r = 40

    def draw(self):
        x_set = [self._x - self._r, self._x, self._x + self._r, self._x]
        y_set = [self._y, self._y + self._r, self._y, self._y - self._r]

        stddraw.setPenColor(self._color)
        stddraw.filledPolygon(x_set, y_set)
        stddraw.setPenColor(stddraw.BLACK)
        stddraw.polygon(x_set, y_set)


class Parallelogram(Taffy):

    def __init__(self, x, y):
        super(Parallelogram, self).__init__(x, y, Color(0, 32, 245))
        self._h = 60

    def draw(self):
        x_set = list()
        y_set = list()

        x_set.append(self._x - pow(3, 0.5) * self._h / 2 + 10)
        x_set.append(self._x + (2 - pow(3, 0.5)) * self._h / 2 + 10)
        x_set.append(self._x + pow(3, 0.5) * self._h / 2 - 10)
        x_set.append(self._x - (2 - pow(3, 0.5)) * self._h / 2 - 10)

        y_set.append(self._y - self._h / 2)
        y_set.append(self._y - self._h / 2)
        y_set.append(self._y + self._h / 2)
        y_set.append(self._y + self._h / 2)

        stddraw.setPenColor(self._color)
        stddraw.filledPolygon(x_set, y_set)
        stddraw.setPenColor(stddraw.BLACK)
        stddraw.polygon(x_set, y_set)


def point_in_taffy(taffy):
    """
    Check if a point is in the taffy.
    """
    if taffy.get_x() - 50 < stddraw.mouseX() < taffy.get_x() + 50:
        if taffy.get_y() - 50 < stddraw.mouseY() < taffy.get_y() + 50:
            return True
    return False


def array_to_canvas(i, j):
    """
    Convert the array[i][j] to the corresponding coordinates in the canvas.
    """
    return (100 * j, 100 * (8 - i))


def draw_red_frame():
    """
    Draw the red frame selector if no other red frame selectors exist in the
    canvas.
    """
    global red_frame_position

    if red_frame_position:
        pos = array_to_canvas(red_frame_position[0], red_frame_position[1])
        stddraw.setPenColor(stddraw.RED)
        stddraw.square(pos[0], pos[1], 45)


def mouse_coordinate_capture():
    """
    Capture the mouse-click position, if it is legal, then assign
    red_frame_position the center of the grid which is clicked and return
    True, otherwise return False.
    """
    global red_frame_position
    global board

    if stddraw.mousePressed():
        for i in range(9):
            for j in range(7):
                if board[i][j] != '0':
                    if point_in_taffy(board[i][j]):
                        red_frame_position = (i, j)
                        return True
    return False


def swap(i0, j0, i1, j1, board):
    """
    Swap the location of two taffies in the board and return it,
    an exception will be raised if any argument is invalid.
    """
    try:
        assert 0 <= i0 <= len(board) - 1
        assert 0 <= i1 <= len(board) - 1
        assert 0 <= j0 <= len(board[0]) - 1
        assert 0 <= j1 <= len(board[0]) - 1
    except:
        raise Exception

    temp_x, temp_y = board[i0][j0].get_x(), board[i0][j0].get_y()
    board[i0][j0].set_x(board[i1][j1].get_x())
    board[i0][j0].set_y(board[i1][j1].get_y())
    board[i1][j1].set_x(temp_x)
    board[i1][j1].set_y(temp_y)
    board[i0][j0], board[i1][j1] = board[i1][j1], board[i0][j0]


def if_taffy_same_type(taffy0, taffy1):
    """
    Judge if two taffies are the same type.
    """
    if type(taffy0).__name__ == type(taffy1).__name__:
        if type(taffy0).__name__ in TAFFY_NAME_LIST:
            return True
    return False


def at_least_three_continuous(array):
    """
    Return if there are at least three same type of taffies in the array.
    """
    continuous_count = 0
    previous_element = None

    for i in range(len(array)):
        if previous_element == None:
            previous_element = array[i]
            continuous_count += 1
        elif if_taffy_same_type(array[i], previous_element):
            continuous_count += 1
        else:
            if continuous_count >= 3:
                return True
            previous_element = array[i]
            continuous_count = 1

    return continuous_count >= 3


def at_least_three_continuous_in_matrix(board):
    """
    Return if there are at least three same type of taffies on a match
    in the matrix.
    """
    try:
        rows = len(board)
        cols = len(board[0])
    except:
        return False

    # check rows
    for i in range(rows):
        if at_least_three_continuous(board[i]):
            return True

    # check columns
    for i in range(cols):
        temp = list()
        for j in range(rows):
            temp.append(board[j][i])
        if at_least_three_continuous(temp):
            return True

    return False


def at_least_three_continuous_subarray(a):
    """
    Judge if there are at least three same type of taffies in the array
    Save each satisfied continuous subarray by its head and tail indices as
    a tuple to a list and return the list.
    """
    result = list()
    continuous_count = 0
    previous_element = None
    left = 0
    right = 0

    for i in range(len(a)):
        if previous_element == None:
            previous_element = a[i]
            continuous_count += 1
        elif if_taffy_same_type(a[i], previous_element):
            continuous_count += 1
            right += 1
        else:
            if continuous_count >= 3:
                result.append((left, right))
            left = i
            right = i
            previous_element = a[i]
            continuous_count = 1

    if continuous_count >= 3:
        result.append((left, right))

    return result


def cancel_or_swap():
    """
    Judge if the player wants to cancel a move or swap taffies.
    If no three in a match after the swap, an undoing swap will automatically
    happen.
    """
    global red_frame_position
    global board

    if stddraw.mousePressed() and red_frame_position:
        i0, j0 = red_frame_position
        for i in range(9):
            for j in range(7):
                if board[i][j] != '0' and point_in_taffy(board[i][j]):
                    if not (i == i0 and j == j0):
                        red_frame_position = None
                        if i == i0 - 1 and j == j0:
                            swap(i0, j0, i, j, board)
                            if at_least_three_continuous_in_matrix(board):
                                return "swap"
                            stddraw.clear()
                            draw_state()
                            stddraw.show(200)
                            swap(i, j, i0, j0, board)
                        elif i == i0 + 1 and j == j0:
                            swap(i0, j0, i, j, board)
                            if at_least_three_continuous_in_matrix(board):
                                return "swap"
                            stddraw.clear()
                            draw_state()
                            stddraw.show(200)
                            swap(i, j, i0, j0, board)
                        elif i == i0 and j == j0 - 1:
                            swap(i0, j0, i, j, board)
                            if at_least_three_continuous_in_matrix(board):
                                return "swap"
                            stddraw.clear()
                            draw_state()
                            stddraw.show(200)
                            swap(i, j, i0, j0, board)
                        elif i == i0 and j == j0 + 1:
                            swap(i0, j0, i, j, board)
                            if at_least_three_continuous_in_matrix(board):
                                return "swap"
                            stddraw.clear()
                            draw_state()
                            stddraw.show(200)
                            swap(i, j, i0, j0, board)
                        return "cancel"
    return None


def board_initialization():
    """
    Initialize a board with 9*7 taffies where there are no three on a match.
    """
    global board

    row_subarray = deque()
    col_subarray = deque()
    for i in range(9):
        row_subarray.clear()
        for j in range(7):
            pos = array_to_canvas(i, j)

            if i > 1:
                col_subarray.clear()
                col_subarray.append(type(board[i - 2][j]).__name__)
                col_subarray.append(type(board[i - 1][j]).__name__)

            while True:
                obj_name = random.choice(TAFFY_NAME_LIST)
                if obj_name == 'Circle':
                    obj = Circle(pos[0], pos[1])
                elif obj_name == 'Triangle':
                    obj = Triangle(pos[0], pos[1])
                elif obj_name == 'Square':
                    obj = Square(pos[0], pos[1])
                elif obj_name == 'Pentagon':
                    obj = Pentagon(pos[0], pos[1])
                elif obj_name == 'Diamond':
                    obj = Diamond(pos[0], pos[1])
                else:
                    obj = Parallelogram(pos[0], pos[1])

                if i <= 1:
                    if len(row_subarray) == 2:
                        if row_subarray[0] == row_subarray[1] == obj_name:
                            continue
                        row_subarray.append(obj_name)
                        row_subarray.popleft()
                    else:
                        row_subarray.append(obj_name)
                else:
                    if col_subarray[0] == col_subarray[1] == obj_name:
                        continue
                    if len(row_subarray) == 2:
                        if row_subarray[0] == row_subarray[1] == obj_name:
                            continue
                        row_subarray.append(obj_name)
                        row_subarray.popleft()
                        col_subarray.append(obj_name)
                        col_subarray.popleft()
                    else:
                        row_subarray.append(obj_name)
                        col_subarray.append(obj_name)
                board[i][j] = obj
                break


def mark_conditional_taffies():
    """
    Mark all continuous taffies which have at least three of same type on a
    match as 'X' in a temporary board.
    """
    global board

    temp_board = [['0' for i in range(7)] for j in range(9)]
    for i in range(9):
        temp = at_least_three_continuous_subarray(board[i])
        if len(temp) != 0:
            for pair in temp:
                for index in range(pair[0], pair[1] + 1):
                    temp_board[i][index] = 'X'

    for i in range(7):
        temp_list = list()
        for j in range(9):
            temp_list.append(board[j][i])
        temp = at_least_three_continuous_subarray(temp_list)
        if len(temp) != 0:
            for pair in temp:
                for index in range(pair[0], pair[1] + 1):
                    temp_board[index][i] = 'X'

    return temp_board


def eliminate_condition_taffies():
    """
    Eliminate all continuous taffies which have at least three of same type on
    a match in the board and update the score.
    """
    global board
    global score

    temp_board = mark_conditional_taffies()
    is_eliminated = False
    for i in range(9):
        for j in range(7):
            if temp_board[i][j] == 'X':
                board[i][j] = '0'
                is_eliminated = True
                score += 1
    return is_eliminated


def random_taffy_generation(i, j):
    """
    Generate a taffy of random type at board[i][j] and return the instance.
    """
    global TAFFY_NAME_LIST

    pos = array_to_canvas(i, j)
    obj_name = random.choice(TAFFY_NAME_LIST)
    if obj_name == 'Circle':
        obj = Circle(pos[0], pos[1])
    elif obj_name == 'Triangle':
        obj = Triangle(pos[0], pos[1])
    elif obj_name == 'Square':
        obj = Square(pos[0], pos[1])
    elif obj_name == 'Pentagon':
        obj = Pentagon(pos[0], pos[1])
    elif obj_name == 'Diamond':
        obj = Diamond(pos[0], pos[1])
    else:
        obj = Parallelogram(pos[0], pos[1])
    return obj


def falling_in_a_unit(taffy_list):
    """
    Animate falling of a taffy from (x0, taffy.get_y()) to (x0, get_y() - 100).
    """
    if len(taffy_list) > 0:
        for i in range(2):
            stddraw.clear()
            for taffy in taffy_list:
                taffy.set_y(taffy.get_y() - 50)
                taffy.draw()
            draw_state()
            stddraw.show(5)
        return taffy_list


def board_refill():
    """
    Refill the board by using random_taffy_generation(i, j).
    Return nothing if the board is already full.
    """
    global board

    depth = 0
    for i in board:
        if '0' in i:
            depth += 1
    if depth == 0:
        return

    # generate random taffies
    upper_board = [['0' for i in range(7)] for j in range(depth)]
    temp_taffy_save = list()
    taffy_list = list()
    for i in range(7):
        for j in range(9):
            if board[j][i] == '0':
                upper_board[j][i] = random_taffy_generation(j, i)
                temp_taffy_save.append(deepcopy(upper_board[j][i]))
                upper_board[j][i].set_y(upper_board[j][i].get_y() + 100 * depth)
                taffy_list.append(upper_board[j][i])

    # simulate the gravity
    for i in range(depth):
        taffy_list = falling_in_a_unit(taffy_list)

    # board update
    pos = 0
    for i in range(7):
        for j in range(9):
            if board[j][i] == '0':
                board[j][i] = temp_taffy_save[pos]
                pos += 1


def gravity_animation():
    """
    Animate the falling of taffies after the execution of
    eliminate_condition_taffies().
    """
    global board

    falling_taffy_list = list()
    for i in range(7):
        a = list()
        for j in range(9):
            a.append(board[j][i])
        a_bak = deepcopy(a)
        for j in range(9):
            if a[j] != '0':
                a[j] = [j, i, 0]
                a_bak[j] = [j, i, 0]
        for j in range(8, -1, -1):
            if a[j] != '0':
                k = j
                while True:
                    try:
                        if a[k + 1] != '0':
                            break
                    except:
                        break
                    a[k][2] += 1
                    a[k], a[k + 1] = a[k + 1], a[k]
                    k += 1
        for j in a:
            if j != '0' and j[2] != 0:
                falling_taffy_list.append(j)

    if len(falling_taffy_list) > 0:
        for i in range(len(falling_taffy_list)):
            i0, j0 = falling_taffy_list[i][0], falling_taffy_list[i][1]
            falling_taffy_list[i] = [board[i0][j0], falling_taffy_list[i][2]]
        while True:
            need_to_check = False
            temp_list = list()
            for i in range(len(falling_taffy_list)):
                if falling_taffy_list[i][1] != 0:
                    need_to_check = True
                    temp_list.append(falling_taffy_list[i][0])
                    falling_taffy_list[i][1] -= 1
            if not need_to_check:
                break
            falling_in_a_unit(temp_list)

        # board update
        for i in range(7):
            temp_list = list()
            pos = 0
            for j in range(8, -1, -1):
                if board[j][i] != '0':
                    temp_list.append(board[j][i])
            for j in range(8, -1, -1):
                try:
                    board[j][i] = temp_list[pos]
                    board[j][i].set_y(100 * (8 - j))
                    pos += 1
                except:
                    board[j][i] = '0'


def detect_if_a_valid_move_exists():
    """
    Detect if a valid move exists by Brute-Force Algorithm when the board
    is full of taffies and no three on a match.
    """
    global board
    board_copy = deepcopy(board)
    for i in range(9):
        for j in range(7):
            # swap with the upper taffy
            try:
                swap(i, j, i - 1, j, board_copy)
            except:
                pass
            else:
                if at_least_three_continuous_in_matrix(board_copy):
                    return True
                swap(i - 1, j, i, j, board_copy)
            # swap with the lower taffy
            try:
                swap(i, j, i + 1, j, board_copy)
            except:
                pass
            else:
                if at_least_three_continuous_in_matrix(board_copy):
                    return True
                swap(i + 1, j, i, j, board_copy)
            # swap with the left taffy
            try:
                swap(i, j, i, j - 1, board_copy)
            except:
                pass
            else:
                if at_least_three_continuous_in_matrix(board_copy):
                    return True
                swap(i, j - 1, i, j, board_copy)
            # swap with the right taffy
            try:
                swap(i, j, i, j + 1, board_copy)
            except:
                pass
            else:
                if at_least_three_continuous_in_matrix(board_copy):
                    return True
                swap(i, j + 1, i, j, board_copy)
    return False


def draw_state():
    """
    Draw the board and the game status after its update.
    """
    global red_frame_position
    global board
    global score
    global is_game_over
    global moves_remaining

    # draw the board
    for i in range(9):
        for j in range(7):
            if board[i][j] != '0':
                board[i][j].draw()

    # draw the status
    stddraw.line(-50, -50, 650, -50)
    stddraw.setFontSize(30)
    stddraw.text(75, -100, "Score: " + str(score))
    if is_game_over:
        stddraw.text(450, -100, "GAME OVER!")
    else:
        stddraw.text(450, -100, "Moves remaining: " + str(moves_remaining))
    if red_frame_position:
        draw_red_frame()


if __name__ == '__main__':

    # initialization of the canvas
    stddraw.setCanvasSize(700, 1000)
    stddraw.setXscale(-50, 650)
    stddraw.setYscale(-150, 850)
    stddraw.setPenRadius(0.001)

    # initialization of the board
    board = [['0' for i in range(7)] for j in range(9)]
    red_frame_position = None
    score = 0
    is_game_over = False
    moves_remaining = 30
    board_initialization()

    while moves_remaining != 0 and detect_if_a_valid_move_exists():
        while True:
            stddraw.clear()
            draw_state()
            if mouse_coordinate_capture():
                break
            stddraw.show(10)
        while True:
            stddraw.clear()
            draw_state()
            temp = cancel_or_swap()
            if temp == 'swap':
                stddraw.clear()
                draw_state()
                stddraw.show(100)
                moves_remaining -= 1
                while True:
                    if eliminate_condition_taffies():
                        stddraw.show(100)
                        stddraw.clear()
                        draw_state()
                        stddraw.show(500)
                        gravity_animation()
                        stddraw.clear()
                        draw_state()
                        stddraw.show(500)
                        stddraw.clear()
                        board_refill()
                        stddraw.show(300)
                    else:
                        break
                break
            elif temp == "cancel":
                break
            stddraw.show(10)
    stddraw.clear()
    is_game_over = True
    draw_state()

    # wait for the player to close the window manually.
    stddraw.show()
