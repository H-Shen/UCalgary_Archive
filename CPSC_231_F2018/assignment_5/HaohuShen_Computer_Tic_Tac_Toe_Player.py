#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This program simulates a game between an AI capable of playing Tic-Tac-Toe
and a human player. The game difficulty is split into hard and moderate level.
The player who goes first is randomly chosen.

    Author: Haohu Shen
    Date:   November 2018
"""

import random
from copy import deepcopy

import stddraw

# constants
FIRST_PLAYER_SHAPE = 'X'
SECOND_PLAYER_SHAPE = 'O'
MAX_SQUARES = 9
MAX_ROWS = 3
NUMBER_TO_LIST = {'1': (0, 0), '2': (0, 1), '3': (0, 2),
                  '4': (1, 0), '5': (1, 1), '6': (1, 2),
                  '7': (2, 0), '8': (2, 1), '9': (2, 2)}

NUMBER_TO_LIST_REV = {value: key for key, value in NUMBER_TO_LIST.items()}


class AI_moderate(object):
    def __init__(self):
        """
        Simulate a game versus moderate AI.
        """
        AI_goes_first = decide_first_player()
        stddraw.clear()
        board = [['0' for j in range(MAX_ROWS)] for i in range(MAX_ROWS)]
        AI_win = 1

        if AI_goes_first:
            AI_turn = True
        else:
            AI_turn = False

        while True:

            if not AI_turn:
                buttom_message("Now it's your turn...")

            board_check = has_three(board)
            if board_check != None:
                if AI_goes_first:
                    if board_check[0] == SECOND_PLAYER_SHAPE:
                        AI_win = -1
                elif board_check[0] == FIRST_PLAYER_SHAPE:
                    AI_win = -1
                break
            if is_board_full(board):
                AI_win = 0
                break

            if AI_turn:
                if AI_goes_first:
                    self.AI_step(FIRST_PLAYER_SHAPE, board)
                else:
                    self.AI_step(SECOND_PLAYER_SHAPE, board)
                AI_turn = False

            elif stddraw.mousePressed():
                tmp = mouse_coordinate_capture()
                if tmp != None:
                    pos = coordinate_to_list(tmp[0], tmp[1])
                    if board[pos[0]][pos[1]] == '0':
                        if AI_goes_first:
                            board[pos[0]][pos[1]] = SECOND_PLAYER_SHAPE
                        else:
                            board[pos[0]][pos[1]] = FIRST_PLAYER_SHAPE
                        AI_turn = True

            draw_state(board)
            stddraw.show(10)
            stddraw.clear()

        stddraw.clear()
        draw_state(board)

        if board_check != None:
            winning_line_indicator(board_check)
        if AI_win == -1:
            buttom_message("Congratulations! You win!")
        elif AI_win == 1:
            buttom_message("The computer wins!")
        else:
            buttom_message("Draw!")

        # wait until the gamers close the window manually
        stddraw.show()

    def AI_step(self, player_shape, board):
        """
        Simulate a step of moderate AI by implementing simple blocking or
        connecting policy.
        """
        global FIRST_PLAYER_SHAPE
        global SECOND_PLAYER_SHAPE
        global MAX_ROWS

        if player_shape == FIRST_PLAYER_SHAPE:
            opposite_shape = SECOND_PLAYER_SHAPE
        else:
            opposite_shape = FIRST_PLAYER_SHAPE

        # if there are two player shapes in a row, make it three in a line.
        for i in range(MAX_ROWS):
            if board[i][0] == '0':
                if board[i][1] == player_shape and board[i][2] == player_shape:
                    board[i][0] = player_shape
                    return
            if board[i][1] == '0':
                if board[i][0] == player_shape and board[i][2] == player_shape:
                    board[i][1] = player_shape
                    return
            if board[i][2] == '0':
                if board[i][0] == player_shape and board[i][1] == player_shape:
                    board[i][2] = player_shape
                    return

        # if there are two player shapes in a column, make it three in a line.
        for i in range(MAX_ROWS):
            if board[0][i] == '0':
                if board[1][i] == player_shape and board[2][i] == player_shape:
                    board[0][i] = player_shape
                    return
            if board[1][i] == '0':
                if board[0][i] == player_shape and board[2][i] == player_shape:
                    board[1][i] = player_shape
                    return
            if board[2][i] == '0':
                if board[0][i] == player_shape and board[1][i] == player_shape:
                    board[2][i] = player_shape
                    return

        # if there are two player shapes in the main diagonal,
        # make it three in a line.
        if board[0][0] == '0':
            if board[1][1] == player_shape and board[2][2] == player_shape:
                board[0][0] = player_shape
                return
        if board[1][1] == '0':
            if board[0][0] == player_shape and board[2][2] == player_shape:
                board[1][1] = player_shape
                return
        if board[2][2] == '0':
            if board[0][0] == player_shape and board[1][1] == player_shape:
                board[2][2] = player_shape
                return

        # if there are two player shapes in the sub diagonal,
        # make it three in a line.
        if board[0][2] == '0':
            if board[1][1] == player_shape and board[2][0] == player_shape:
                board[0][2] = player_shape
                return
        if board[1][1] == '0':
            if board[0][2] == player_shape and board[2][0] == player_shape:
                board[1][1] = player_shape
                return
        if board[2][0] == '0':
            if board[0][2] == player_shape and board[1][1] == player_shape:
                board[2][0] = player_shape
                return

        # if there are two opposite shapes in a row, block it.
        for i in range(MAX_ROWS):
            if board[i][0] == '0' and board[i][1] == opposite_shape:
                if board[i][2] == opposite_shape:
                    board[i][0] = player_shape
                    return
            if board[i][0] == opposite_shape and board[i][1] == '0':
                if board[i][2] == opposite_shape:
                    board[i][1] = player_shape
                    return
            if board[i][0] == opposite_shape and board[i][1] == opposite_shape:
                if board[i][2] == '0':
                    board[i][2] = player_shape
                    return

        # if there are two opposite shapes in a column, block it.
        for i in range(MAX_ROWS):
            if board[0][i] == '0' and board[1][i] == opposite_shape:
                if board[2][i] == opposite_shape:
                    board[0][i] = player_shape
                    return
            if board[0][i] == opposite_shape and board[1][i] == '0':
                if board[2][i] == opposite_shape:
                    board[1][i] = player_shape
                    return
            if board[0][i] == opposite_shape and board[1][i] == opposite_shape:
                if board[2][i] == '0':
                    board[2][i] = player_shape
                    return

        # if there are two opposite shapes in the main diagonal, block it.
        if board[0][0] == '0' and board[1][1] == opposite_shape:
            if board[2][2] == opposite_shape:
                board[0][0] = player_shape
                return
        if board[0][0] == opposite_shape and board[1][1] == '0':
            if board[2][2] == opposite_shape:
                board[1][1] = player_shape
                return
        if board[0][0] == opposite_shape and board[1][1] == opposite_shape:
            if board[2][2] == '0':
                board[2][2] = player_shape
                return

        # if there are two opposite shapes in the sub diagonal, block it.
        if board[0][2] == '0' and board[1][1] == opposite_shape:
            if board[2][0] == opposite_shape:
                board[0][2] = player_shape
                return
        if board[0][2] == opposite_shape and board[1][1] == '0':
            if board[2][0] == opposite_shape:
                board[1][1] = player_shape
                return
        if board[0][2] == opposite_shape and board[1][1] == opposite_shape:
            if board[2][0] == '0':
                board[2][0] = player_shape
                return

        # randomly take a step in an empty square
        temp = list()
        for i in range(MAX_ROWS):
            for j in range(MAX_ROWS):
                if board[i][j] == '0':
                    temp.append((i, j))
        pos = random.choice(temp)
        board[pos[0]][pos[1]] = player_shape


class AI_hard(object):
    # save all possibilities for AI not lose when AI goes first and always goes
    # '1' at the first step. Redundant outcomes have been pruned.
    WIN_LIST_0 = ['12539', '12549', '12569', '12579', '12589', '1259734',
                  '1259743', '1259764', '1259784', '13724', '1374928',
                  '1374958', '1374968', '1374985', '13754', '13764', '13784',
                  '13794', '14529', '14539', '14569', '14579', '14589',
                  '1459327', '1459362', '1459372', '1459382', '1592837',
                  '1592847', '1592867', '159287346', '159287364', '1593724',
                  '1593748', '1593768', '1593784', '1594623', '159463728',
                  '159463782', '1594673', '1594683', '1596427', '1596437',
                  '159647328', '159647382', '1596487', '1597326', '1597342',
                  '1597362', '1597382', '159823746', '159823764', '1598243',
                  '1598263', '1598273', '16529', '16539', '16549', '16579',
                  '16589', '1659327', '1659342', '1659372', '1659382',
                  '1732946', '1732956', '1732965', '1732986', '17342', '17352',
                  '17362', '17382', '17392', '18529', '18539', '18549',
                  '18569', '18579', '1859724', '1859734', '1859743', '1859764',
                  '1932745', '1932754', '1932764', '1932784', '19342', '19352',
                  '19362', '19372', '19382']

    # save all possibilities for AI not lose when human goes first.
    # Redundant outcomes have been pruned.
    WIN_LIST_1 = ['152347', '152367', '152374698', '15237486', '15237496',
                  '152387', '152397', '153248', '153268', '153278',
                  '153284697', '15328476', '15328496', '153298', '154723',
                  '15473268', '154732896', '15473298', '154763', '154783',
                  '154793', '15692347', '156923748', '15692387', '15693248',
                  '15693278', '156932847', '15694723', '156947328', '15694783',
                  '156974238', '156974328', '156974832', '15698723',
                  '156987324', '15698743', '157426', '157436', '15746238',
                  '157462893', '15746298', '157486', '157496', '15892347',
                  '15892367', '158923746', '158932476', '158932674',
                  '158932746', '15894723', '158947326', '15894763', '15896327',
                  '15896347', '158963742', '15897426', '15897436', '158974623',
                  '15982347', '15982367', '159823746', '159832', '159842',
                  '159862', '159872', '251347', '251367', '251374689',
                  '25137486', '25137496', '251387', '251397', '253149',
                  '253169', '253179', '253189', '253196487', '25319674',
                  '25319684', '254139', '254169', '254179', '254189',
                  '25419367', '254193786', '25419387', '256317', '256347',
                  '25637149', '25637189', '256371984', '256387', '256397',
                  '257314689', '25731486', '25731496', '25734169', '25734189',
                  '257341986', '257369148', '25736941', '25736981', '25738916',
                  '25738946', '25738961', '257398146', '257398416',
                  '257398641', '258614', '258634', '25864913', '25864931',
                  '25864973', '258674', '258694', '259136487', '25913674',
                  '25913684', '259147368', '25914763', '25914783', '25916347',
                  '259163784', '25916387', '259178364', '259178463',
                  '259178634', '25918734', '25918743', '25918764', '351248',
                  '351268', '351278', '351286479', '35128674', '35128694',
                  '351298', '352149', '352169', '352179', '352189',
                  '352196478', '35219674', '35219684', '35471268', '354712869',
                  '35471298', '35472169', '35472189', '354721968', '354769128',
                  '35476921', '35476981', '354789126', '35478921', '35478961',
                  '354796128', '354796218', '354796812', '35691248',
                  '35691278', '356912874', '356921', '356941', '356971',
                  '356981', '357812', '35782149', '35782169', '357821964',
                  '357842', '357862', '357892', '358712496', '358712694',
                  '358712964', '35872149', '35872169', '358721964', '35874129',
                  '35874169', '358741962', '358769124', '35876921', '35876941',
                  '35879614', '35879624', '358796421', '359614', '359624',
                  '35964218', '35964278', '359642871', '359674', '359684',
                  '451723', '45173268', '451732869', '45173298', '451763',
                  '451783', '451793', '452139', '452169', '452179', '452189',
                  '452197368', '45219763', '45219783', '45371268', '453712869',
                  '45371298', '45372169', '45372189', '453721968', '45376918',
                  '45376928', '45376981', '453789126', '45378921', '45378961',
                  '453796128', '453796218', '453796821', '456218', '456238',
                  '456278', '45628317', '45628371', '45628391', '456298',
                  '457129', '457139', '457169', '457189', '457198263',
                  '45719832', '45719862', '458713', '458723', '45873129',
                  '45873169', '458731962', '458763', '458793', '45912367',
                  '459123786', '45912387', '459136287', '459136782',
                  '459136872', '45916327', '45916372', '45916382', '459178263',
                  '45917832', '45917862', '45918723', '459187362', '45918763',
                  '51283749', '51283769', '51283794', '512846379', '512846739',
                  '512846973', '51286439', '512864739', '51286497',
                  '512873469', '512873649', '512873964', '51289734',
                  '512897463', '51289764', '513724', '513746289', '513746829',
                  '513746982', '513764', '513784', '513794', '514628379',
                  '514628739', '514628937', '514637289', '514637829',
                  '514637982', '51467329', '51467389', '51467392', '514682379',
                  '51468273', '51468293', '514693287', '51469372', '51469382',
                  '516427', '516437', '516473289', '51647382', '51647392',
                  '516487', '516497', '517328469', '517328649', '517328964',
                  '517342', '517362', '517382', '517392', '518237469',
                  '51823764', '51823794', '518243', '518263', '518273',
                  '518293', '519724', '519734', '519746283', '519746328',
                  '519746823', '519764', '519784', '65192347', '651923748',
                  '65192387', '65193248', '65193278', '651932847', '65194728',
                  '65194738', '65194783', '651974238', '651974328',
                  '651974823', '65198723', '651987324', '65198743', '652317',
                  '652347', '652379148', '65237941', '65237981', '652387',
                  '652397', '65391248', '65391278', '653912847', '653921',
                  '653941', '653971', '653981', '654812', '65482719',
                  '65482739', '65482793', '654832', '654872', '654892',
                  '657314289', '657314892', '657314982', '65732149',
                  '65732189', '657321984', '65734129', '65734182', '65734192',
                  '657389142', '65738921', '65738941', '65739812', '657398241',
                  '65739842', '65891327', '65891347', '658913742', '658921',
                  '658931', '658941', '658971', '659317', '659327', '659347',
                  '65937812', '659378241', '65937842', '659387', '751426',
                  '751436', '751468239', '75146832', '75146892', '751486',
                  '751496', '752314689', '75231486', '75231496', '75234169',
                  '75234189', '752341986', '752369148', '75236941', '75236981',
                  '752389146', '75238941', '75238961', '752398146',
                  '752398416', '752398614', '753218', '753248', '753268',
                  '753289146', '75328941', '75328961', '753298', '754129',
                  '754139', '754169', '754189', '754198236', '75419832',
                  '75419862', '756314298', '756314892', '756314982',
                  '75632149', '75632189', '756321984', '75634129', '75634189',
                  '756341982', '756389142', '75638921', '75638941', '75639812',
                  '756398241', '75639842', '75891426', '75891436', '758914632',
                  '758921', '758931', '758941', '758961', '759812', '75982416',
                  '75982436', '759824631', '759832', '759842', '759862',
                  '85192346', '85192367', '85192376', '851932476', '851932647',
                  '851932746', '85194723', '851947326', '85194763', '85196327',
                  '85196347', '851963742', '85197426', '85197436', '851974623',
                  '852416', '852436', '85246137', '85246179', '85246197',
                  '852476', '852496', '853712469', '853712694', '853712964',
                  '85372149', '85372164', '85372194', '85374129', '85374169',
                  '853741962', '853769124', '85376921', '85376941', '85379614',
                  '85379624', '853796421', '854713', '854723', '854739126',
                  '85473921', '85473961', '854763', '854793', '85691723',
                  '856917324', '85691743', '856921', '856931', '856941',
                  '856971', '85791426', '85791436', '857914623', '857921',
                  '857931', '857941', '857961', '859713', '859723', '85973614',
                  '85973624', '859736421', '859743', '859763', '951238',
                  '951248', '951268', '951278', '951287364', '95128743',
                  '95128763', '952136487', '95213674', '95213684', '952147368',
                  '95214763', '95214783', '95216347', '952163784', '95216387',
                  '952178364', '952178436', '952178634', '952187364',
                  '95218743', '95218763', '953614', '953624', '95364812',
                  '953648217', '95364872', '953674', '953684', '95412367',
                  '954123786', '95412387', '954136278', '954136782',
                  '954136872', '95416327', '954163782', '95416387',
                  '954178263', '95417832', '95417862', '95418723', '954187362',
                  '95418763', '956317', '956327', '956347', '95637812',
                  '956378214', '95637842', '956387', '957812', '95782614',
                  '95782634', '957826413', '957832', '957842', '957862',
                  '958713', '958723', '95873614', '95873624', '958736412',
                  '958743', '958763']

    def __init__(self):
        """
        Simulate a game versus hard AI.
        """
        AI_goes_first = decide_first_player()
        stddraw.clear()
        board = [['0' for j in range(MAX_ROWS)] for i in range(MAX_ROWS)]
        log = ""
        log_position = 0
        AI_win = 1

        if AI_goes_first:
            AI_turn = True
            win_list = deepcopy(self.WIN_LIST_0)
        else:
            AI_turn = False
            win_list = deepcopy(self.WIN_LIST_1)

        while True:

            if not AI_turn:
                buttom_message("Now it's your turn...")

            board_check = has_three(board)
            if board_check != None:
                if AI_goes_first:
                    if board_check[0] == SECOND_PLAYER_SHAPE:
                        AI_win = -1
                elif board_check[0] == FIRST_PLAYER_SHAPE:
                    AI_win = -1
                break
            if is_board_full(board):
                AI_win = 0
                break

            if AI_turn:
                # update win_list
                win_list_update = list()
                for i in win_list:
                    if i.startswith(log):
                        win_list_update.append(i)
                win_list = deepcopy(win_list_update)

                if AI_goes_first:
                    self.AI_step(win_list[0][log_position],
                                 FIRST_PLAYER_SHAPE, board)
                else:
                    self.AI_step(win_list[0][log_position],
                                 SECOND_PLAYER_SHAPE, board)

                log += win_list[0][log_position]
                log_position += 1
                AI_turn = False

            elif stddraw.mousePressed():
                tmp = mouse_coordinate_capture()
                if tmp != None:
                    pos = coordinate_to_list(tmp[0], tmp[1])
                    if board[pos[0]][pos[1]] == '0':
                        if AI_goes_first:
                            board[pos[0]][pos[1]] = SECOND_PLAYER_SHAPE
                        else:
                            board[pos[0]][pos[1]] = FIRST_PLAYER_SHAPE
                        log += NUMBER_TO_LIST_REV[pos]
                        log_position += 1
                        AI_turn = True

            draw_state(board)
            stddraw.show(10)
            stddraw.clear()

        stddraw.clear()
        draw_state(board)

        if board_check != None:
            winning_line_indicator(board_check)
        if AI_win == -1:
            buttom_message("Congratulations! You win!")
        elif AI_win == 1:
            buttom_message("The computer wins!")
        else:
            buttom_message("Draw!")

        # wait until the gamers close the window manually
        stddraw.show()

    def AI_step(self, number_string, player_shape, board):
        """
        Simulate a step of hard AI, using Brute-Force to store all
        possibilities to win in advance.
        """
        i, j = NUMBER_TO_LIST[number_string]
        board[i][j] = player_shape


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


def decide_first_player():
    if random.randint(0, 1) == 1:
        print("You are the first player.")
        return False
    print("The computer is the first player.")
    return True


if __name__ == '__main__':

    # selection of game difficulty
    select_num = 0
    while True:
        print("Please select the difficulty level: ")
        print()
        print("1. Moderate")
        print("2. Hard")
        print()
        select = input("Select: ")
        try:
            select_num = int(select)
            assert str(select_num) == select
            assert 1 <= select_num <= 2
        except:
            print("Input invalid, please select again.")
        else:
            print()
            break

    # initialization of the canvas
    stddraw.setCanvasSize(600, 700)
    stddraw.setXscale(0, 30)
    stddraw.setYscale(-5, 30)

    if select_num == 1:
        AI_moderate()
    else:
        AI_hard()
