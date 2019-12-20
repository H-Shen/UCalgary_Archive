#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
This game is a GUI version of Hangman, including single play, PvP and game
instructions. The module named tkinter and visual are needed to run the game.

Test successfully in macOS Mojave 10.14 and Fedora release 28.
The reasons of some bugs cannot be confirmed while testing in Windows 10.

    Author: Haohu Shen
    Date:   October 2018
"""

import os
import sys

NORMAL = 0
DIFFICULT = 1
HARD = 2
COMPETITON = 3
MAX_GUESS = 8
MIN_LENGTH = 4
MAX_LENGTH = 9
MAX_ITERATION = 6

file_to_read = 'cpsc231-lexicon.txt'
player1_result = {'elapsed_time': 0.0, 'win_count': 0}
player2_result = {'elapsed_time': 0.0, 'win_count': 0}
total_time = 6


def word_family_obtain(word_list, guess_character):
    """
    Partiton word_list according to the position and frequency that
    guess_character appears. Return a dict whose keys are tuples saving
    the indices of guess_character and whose values are lists of words
    that satisfies the corresponding patterns.
    """

    collections = dict()
    for word in word_list:
        position = list()
        for j in range(len(word)):
            if word[j] == guess_character:
                position.append(j)
        position = tuple(position)
        if not position in collections:
            collections[position] = [word]
        else:
            collections[position].append(word)
    return collections


def most_unique_characters_filter(word_list, guess_character):
    """
    Collect all words in word_list where each has most unique characters
    without guess_character and return as a list.
    """

    result = list()
    max_unique = -1
    for i in word_list:
        try:
            character_list = list(i)
            character_list.remove(guess_character)
        except:
            pass
        max_unique = max(len(set(i)), max_unique)

    for i in word_list:
        try:
            character_list = list(i)
            character_list.remove(guess_character)
        except:
            pass
        if len(set(i)) == max_unique:
            result.append(i)
    return result


def largest_family(collections, guess_character):
    """
    Find the family with most members in collections.

    If there is a tie, find the family with the least frequency of
    guess_character.

    If there is still a tie, collect all words in the tied families with
    most unique characters without guess_character as the largest family.

    Return a dict whose keys are tuples saving the indices of guess_character
    and whose values are lists of words that satisfies the corresponding
    patterns.
    """

    filter0 = dict()
    most_members = len(max(collections.values(), key=len))
    for i in collections:
        if len(collections[i]) == most_members:
            filter0[i] = collections[i]
    if len(filter0) == 1:
        return filter0

    filter1 = dict()
    least_frequency = len(min(filter0.keys(), key=len))
    for i in filter0:
        if len(i) == least_frequency:
            filter1[i] = filter0[i]
    if len(filter1) == 1:
        return filter1

    filter2 = dict()
    tmp = list()
    for i in filter1:
        tmp.extend(filter1[i])

    most_unique = most_unique_characters_filter(tmp, guess_character)
    word = random.choice(most_unique)
    position = tuple(i for i in range(len(word)) if word[i] == guess_character)
    filter2[position] = [word]
    return filter2


def place_at_the_center(width, height, window):
    """
    Place the window at the center of the screen.
    """

    diff_x = (window.winfo_screenwidth() - width) / 2
    diff_y = (window.winfo_screenheight() - height) / 2
    window.geometry('%dx%d+%d+%d' % (width, height, diff_x, diff_y))


def single_play():
    """
    Control all branches in single play mode, including initiate normal mode,
    difficult mode and hard mode to as well as print the layout of menu.
    """

    def normal_mode():
        """
        Simulates a normal game of hangman, which is almost identical to
        HaohuShen_p4.py.
        """

        lexicon = list()
        with open(file_to_read, 'r') as f:
            lexicon_set = set()
            for i in f.readlines():
                word = i.rstrip().lstrip()
                if not word in lexicon_set:
                    lexicon_set.add(word)
                    if len(word) >= MIN_LENGTH:
                        lexicon.append(word)

        word_to_guess = list(random.choice(lexicon))
        guess_remaining = MAX_GUESS
        win_the_game = False
        current_word = ['_' for i in range(len(word_to_guess))]
        bad_guess_so_far = list()
        guess = ""
        remain_blanks = 0
        has_guessed = set()

        while True:

            if guess_remaining == 0:
                break

            remain_blanks = current_word.count('_')
            visual.stddraw.clear()
            visual.print_title("Welcome to Hangman Game v0.01!")
            visual.draw_background(8 - guess_remaining)
            visual.remaining_guess(remain_blanks, guess_remaining)
            visual.current_word_show(current_word)
            visual.current_bad_guess_show(bad_guess_so_far)

            while True:
                if not visual.stddraw.hasNextKeyTyped():
                    visual.stddraw.show(10)
                else:
                    guess = visual.stddraw.nextKeyTyped()
                    if guess.isalpha():
                        break

            if guess in word_to_guess:
                for i in range(len(word_to_guess)):
                    if word_to_guess[i] == guess:
                        current_word[i] = guess
                print("Nice guess!", end='')

            else:
                if not guess in bad_guess_so_far:
                    bad_guess_so_far.append(guess)
                print('Sorry, there is no ' + '"' + guess + '".', end='')
                guess_remaining -= 1

            if guess in has_guessed:
                print(' It has been guessed before. Please guess a new' +
                      ' character.')
            else:
                has_guessed.add(guess)
                print()

            if current_word == word_to_guess:
                remain_blanks = 0
                win_the_game = True
                break

        visual.stddraw.clear()
        visual.print_title("Welcome to Hangman Game v0.01!")
        visual.draw_background(8 - guess_remaining)
        visual.remaining_guess(remain_blanks, guess_remaining, False)
        visual.current_word_show(current_word)

        if win_the_game:
            visual.game_win_show(word_to_guess)
        else:
            visual.game_over_show(word_to_guess)
        visual.stddraw.show(10)

        retry = messagebox.askretrycancel(message="Wanna retry?")
        if retry:
            print("Retry...")
            normal_mode()
        else:
            print("Goodbye.")
            sys.exit(0)

    def difficult_mode(hard_version=False):
        """
        Simulates a difficult game of hangman, which is almost identical to
        HaohuShen_Evil_Hangman.py.
        """

        lexicon = list()
        with open(file_to_read, 'r') as f:
            lexicon_set = set()
            for i in f.readlines():
                word = i.rstrip().lstrip()
                if MIN_LENGTH <= len(word) <= MAX_LENGTH:
                    if not word in lexicon_set:
                        lexicon_set.add(word)
                        lexicon.append(word)

        if hard_version:
            lexicon_add = list()
            for i in range(len(lexicon)):
                temp_list = list(lexicon[i])
                index = random.randint(0, len(temp_list) - 1)
                temp_list[index] = temp_list[index].upper()
                lexicon_add.append(''.join(temp_list))
            lexicon.extend(lexicon_add)

        length = random.randint(MIN_LENGTH, MAX_LENGTH)
        word_list = list(filter(lambda x: len(x) == length, lexicon))
        current_word = ['_' for i in range(length)]
        guess_remaining = MAX_GUESS
        win_the_game = False
        correct_guess = False
        bad_guess_so_far = list()
        guess = ""
        remain_blanks = current_word.count('_')
        random_word = ""
        has_guessed = set()

        while True:

            if guess_remaining == 0:
                random_word = random.choice(word_list)
                break

            remain_blanks = current_word.count('_')
            if remain_blanks == 0:
                win_the_game = True
                break

            visual.stddraw.clear()
            visual.print_title("Welcome to Hangman Game v0.01!")
            visual.draw_background(8 - guess_remaining)
            visual.remaining_guess(remain_blanks, guess_remaining)
            visual.current_word_show(current_word)
            visual.current_bad_guess_show(bad_guess_so_far)

            correct_guess = False
            while True:
                if not visual.stddraw.hasNextKeyTyped():
                    visual.stddraw.show(10)
                else:
                    guess = visual.stddraw.nextKeyTyped()
                    if guess.isalpha():
                        break

            if not guess in bad_guess_so_far:

                word_family = word_family_obtain(word_list, guess)
                word_dict = largest_family(word_family, guess)
                word_list = list(word_dict.values())[0]

                if tuple() in word_dict:
                    bad_guess_so_far.append(guess)
                else:
                    position = list(word_dict.keys())[0]
                    for i in position:
                        current_word[i] = guess
                    correct_guess = True

            if correct_guess:
                print("Nice guess!", end='')
            else:
                print('Sorry, there is no ' + '"' + guess + '".', end='')
                guess_remaining -= 1

            if guess in has_guessed:
                print(' It has been guessed before. Please guess a new' +
                      ' character.')
            else:
                has_guessed.add(guess)
                print()

        visual.stddraw.clear()
        visual.print_title("Welcome to Hangman Game v0.01!")
        visual.draw_background(8 - guess_remaining)
        visual.remaining_guess(remain_blanks, guess_remaining, False)
        visual.current_word_show(current_word)

        if win_the_game:
            visual.game_win_show(current_word, True)
        else:
            visual.game_over_show(random_word)
        visual.stddraw.show(10)

        retry = messagebox.askretrycancel(message="Wanna retry?")
        if retry:
            print("Retry...")
            difficult_mode(hard_version)
        else:
            print("Goodbye.")
            sys.exit(0)

    def hard_mode():
        """
        Simulates a difficult game of hangman, which is almost identical to
        HaohuShen_Evil_Hangman.py except word_list is more complicated.
        """
        difficult_mode(True)

    single_play_window = tk.Toplevel(window)
    single_play_window.title("Single Play")
    place_at_the_center(350, 420, single_play_window)

    s = tk.StringVar()
    single_play_title = tk.Label(single_play_window, textvariable=s,
                                 font=('Arial', 15), width=30, height=5)

    single_play_title.place(relx=0.5, rely=0.3, anchor=tk.N, y=-120)
    s.set("Please select the game difficulty:")

    button1 = tk.Button(single_play_window, text='Normal',
                        font=('Arial', 18), width=14, height=2, command=normal_mode)

    button2 = tk.Button(single_play_window, text='Difficult',
                        font=('Arial', 18), width=14, height=2, command=difficult_mode)

    button3 = tk.Button(single_play_window, text='Hard',
                        font=('Arial', 18), width=14, height=2, command=hard_mode)

    button4 = tk.Button(single_play_window, text='Return',
                        font=('Arial', 18), width=14, height=2,
                        command=single_play_window.destroy)

    button1.place(relx=0.5, rely=0.3, anchor=tk.N, y=0)
    button2.place(relx=0.5, rely=0.3, anchor=tk.N, y=60)
    button3.place(relx=0.5, rely=0.3, anchor=tk.N, y=120)
    button4.place(relx=0.5, rely=0.3, anchor=tk.N, y=180)


def competition(player):
    """
    Control the GUI of the hangman competition.
    """

    lexicon = list()
    with open(file_to_read, 'r') as f:
        lexicon_set = set()
        for i in f.readlines():
            word = i.rstrip().lstrip()
            if not word in lexicon_set:
                lexicon_set.add(word)
                if len(word) >= MIN_LENGTH:
                    lexicon.append(word)

    player_result = dict()
    win_count = 0
    start = timer()
    print(player + ":")

    for turn in range(1, MAX_ITERATION):

        word_to_guess = list(random.choice(lexicon))
        lexicon.remove(''.join(word_to_guess))
        guess_remaining = MAX_GUESS
        win_the_game = False
        current_word = ['_' for i in range(len(word_to_guess))]
        bad_guess_so_far = list()
        guess = ""
        remain_blanks = 0
        has_guessed = set()
        print("Iteration: " + str(turn))

        while True:

            if guess_remaining == 0:
                break

            remain_blanks = current_word.count('_')
            visual.stddraw.clear()
            visual.print_title(str(player) + " " * 5 + "ITERATION: " +
                               str(turn) + " " * 5 + "CORRECT: " + str(win_count))

            visual.draw_background(8 - guess_remaining)
            visual.remaining_guess(remain_blanks, guess_remaining, False)
            visual.current_word_show(current_word)
            visual.current_bad_guess_show(bad_guess_so_far)

            while True:
                if not visual.stddraw.hasNextKeyTyped():
                    visual.stddraw.show(10)
                else:
                    guess = visual.stddraw.nextKeyTyped()
                    if guess.isalpha():
                        break

            if guess in word_to_guess:
                for i in range(len(word_to_guess)):
                    if word_to_guess[i] == guess:
                        current_word[i] = guess
                print("Nice guess!", end='')

            else:
                if not guess in bad_guess_so_far:
                    bad_guess_so_far.append(guess)
                print('Sorry, there is no ' + '"' + guess + '".', end='')
                guess_remaining -= 1

            if guess in has_guessed:
                print(' It has been guessed before. Please guess a new' +
                      ' character.')
            else:
                has_guessed.add(guess)
                print()

            if current_word == word_to_guess:
                remain_blanks = 0
                win_the_game = True
                break

        visual.stddraw.clear()
        visual.print_title(str(player) + " " * 5 + "ITERATION: " +
                           str(turn) + " " * 5 + "CORRECT: " + str(win_count))

        visual.draw_background(8 - guess_remaining)
        visual.remaining_guess(remain_blanks, guess_remaining, False)
        visual.current_word_show(current_word)
        visual.stddraw.setFontFamily('Helvetica')
        visual.stddraw.setFontSize(18)

        if win_the_game:
            win_count += 1
            visual.stddraw.text(0.5, 0.08, "Correct!")
        else:
            visual.stddraw.text(0.5, 0.08, "Sorry, wrong answer... "
                                           "The word is '" + ''.join(word_to_guess) + "'")

    end = timer()
    player_result['elapsed_time'] = end - start
    player_result['win_count'] = win_count

    if player == 'PLAYER1':
        messagebox.showinfo(message='Press OK to start the turn of PLAYER2...')

    return player_result


def compare(player1_result, player2_result):
    """
    Return 1 if PLAYER1 wins, return -1 if PLAYER2 wins, return 0 if it's tied.
    """

    if player1_result['win_count'] == player2_result['win_count']:
        if player1_result['elapsed_time'] < player2_result['elapsed_time']:
            return 1
        elif player1_result['elapsed_time'] > player2_result['elapsed_time']:
            return -1
        return 0
    elif player1_result['win_count'] > player2_result['win_count']:
        return 1
    return -1


def summary():
    """
    Show the summary of two players after the competition.
    """

    def quit_summary():
        print("Goodbye.")
        sys.exit(0)

    global player1_result
    global player2_result

    winner = ""
    tmp = compare(player1_result, player2_result)
    if tmp == 1:
        winner = "PLAYER 1 wins!"
    elif tmp == -1:
        winner = "PLAYER 2 wins!"
    else:
        winner = "Draw!"

    summary_window = tk.Toplevel(window)
    summary_window.title("Summary")
    place_at_the_center(320, 600, summary_window)

    s = tk.StringVar()
    player_summary = tk.Label(summary_window, textvariable=s,
                              font=('Arial', 15), width=30, height=20)

    player_summary.place(relx=0.5, rely=0.3, anchor=tk.N, y=-120)
    s.set("PLAYER 1:\n\n" +
          "____________________\n\n" +
          "Correctly guessed:        " +
          str(player1_result['win_count']) + "\n\n" +
          "Elapsed time:         " +
          "%.3f" % (player1_result['elapsed_time']) + 's\n' +
          "\n" +

          "PLAYER 2:\n\n" +
          "____________________\n\n" +
          "Correctly guessed:        " +
          str(player2_result['win_count']) + "\n\n" +
          "Elapsed time:         " +
          "%.3f" % (player2_result['elapsed_time']) + 's\n' +
          "____________________\n\n" +
          winner)

    button = tk.Button(summary_window, text='OK',
                       font=('Arial', 14), width=12, height=2, command=quit_summary)

    button.place(relx=0.5, rely=0.3, anchor=tk.N, y=350)


def countdown():
    """
    Show the countdown before the competition.
    """

    def timer(number):
        def decrease():
            global total_time
            total_time -= 1
            number.config(text=str(total_time))
            number.after(1000, decrease)
            if total_time == -1:
                player_vs_player(countdown_window)

        decrease()

    countdown_window = tk.Toplevel(window)
    countdown_window.title("Countdown")
    place_at_the_center(300, 200, countdown_window)

    s = tk.StringVar()
    indicate = tk.Label(countdown_window, textvariable=s,
                        font=('Arial', 20), width=18, height=3)

    s.set("The game will start in:")
    indicate.place(relx=0.5, rely=0.1, anchor=tk.N, y=0)

    number = tk.Label(countdown_window,
                      font=('Arial', 24), width=10, height=3)

    number.place(relx=0.5, rely=0.1, anchor=tk.N, y=70)
    timer(number)


def player_vs_player(countdown_window):
    """
    Control the GUI of the hangman competition.
    """
    global player1_result, player2_result

    countdown_window.destroy()
    player1_result = competition('PLAYER1')
    player2_result = competition('PLAYER2')
    summary()


def game_instruction():
    """
    Control all branches and layout in the sub-menu which shows the game
    instructions. The main menu will be pop ahead after pressing any
    buttons in the sub-menu in Fedora 28 and the reason cannot be
    confirmed currently.
    """

    def normal_mode():
        messagebox.showinfo(title="Normal Mode",
                            message="Normal Mode:\n\n8 chances of guessing one word, " +
                                    "like a normal hangman game.")

    def difficult_mode():
        messagebox.showinfo(title="Difficult Mode",
                            message="Difficult Mode:\n\n8 chances of guessing one word, " +
                                    "but harder than the normal mode.")

    def hard_more():
        messagebox.showinfo(title="Hard Mode",
                            message="Hard Mode:\n\n15 Chances of guessing, a letter may be " +
                                    "randomly selected and converted to uppercase.")

    def competition_mode():
        messagebox.showinfo(title="Competition Mode: ",
                            message="Competition Mode:\n\nA competition includes two players, " +
                                    "each player plays the turn according to the order. During each turn," +
                                    " the player will be given 5 unique words, 8 chances of guessing " +
                                    "for each word.\n\nA summary will be shown after the competition. The" +
                                    " player who guessed more correct words wins. If there is tie, the " +
                                    "player spent less time wins.")

    instruction_window = tk.Toplevel(window)
    instruction_window.title("Game Instructions")
    place_at_the_center(350, 550, instruction_window)

    s = tk.StringVar()
    instruction_title = tk.Label(instruction_window, textvariable=s,
                                 font=('Arial', 15), width=30, height=5)

    instruction_title.place(relx=0.5, rely=0.3, anchor=tk.N, y=-120)
    s.set("The difficulty of Hangman \nare divived into four modes:\n\nNormal,"
          " Difficult, Hard, Competition.")

    button1 = tk.Button(instruction_window, text='Normal',
                        font=('Arial', 18), width=14, height=2, command=normal_mode)

    button2 = tk.Button(instruction_window, text='Difficult',
                        font=('Arial', 18), width=14, height=2, command=difficult_mode)

    button3 = tk.Button(instruction_window, text='Hard',
                        font=('Arial', 18), width=14, height=2, command=hard_more)

    button4 = tk.Button(instruction_window, text='Competition',
                        font=('Arial', 18), width=14, height=2,
                        command=competition_mode)

    button5 = tk.Button(instruction_window, text='Return',
                        font=('Arial', 18), width=14, height=2,
                        command=instruction_window.destroy)

    button1.place(relx=0.5, rely=0.3, anchor=tk.N, y=0)
    button2.place(relx=0.5, rely=0.3, anchor=tk.N, y=60)
    button3.place(relx=0.5, rely=0.3, anchor=tk.N, y=120)
    button4.place(relx=0.5, rely=0.3, anchor=tk.N, y=180)
    button5.place(relx=0.5, rely=0.3, anchor=tk.N, y=240)


def game_exit():
    result = messagebox.askquestion(message="Sure to quit the game?")
    if result == "yes":
        print("Goodbye.")
        sys.exit(0)


if __name__ == "__main__":

    # arguments validation
    try:
        assert len(sys.argv) == 2 or len(sys.argv) == 3

        if len(sys.argv) == 3:
            # initiate the debugging mode
            if sys.argv[2] == "--debug":
                MAX_GUESS = 8000
            else:
                raise Exception

        file_to_read = sys.argv[1]
        assert os.access(file_to_read, os.F_OK)
        assert os.access(file_to_read, os.R_OK)
    except:
        print("Arguments invalid, program terminated.")
        sys.exit()

    # initialization
    import tkinter as tk
    from tkinter import messagebox
    import random
    import visual
    from timeit import default_timer as timer

    window = tk.Tk()
    window.title("Hangman Game v0.01")
    place_at_the_center(350, 420, window)

    s = tk.StringVar()
    title = tk.Label(window, textvariable=s, font=('Arial', 26),
                     width=15, height=3)

    title.place(relx=0.5, rely=0.3, anchor=tk.N, y=-110)
    s.set("H A N G M 4 N _")

    button1 = tk.Button(window, text='Single Play', font=('Arial', 18),
                        width=14, height=2, command=single_play)

    button2 = tk.Button(window, text='Player VS Player', font=('Arial', 18),
                        width=14, height=2, command=countdown)

    button3 = tk.Button(window, text='Game Instructions', font=('Arial', 18),
                        width=14, height=2, command=game_instruction)

    button4 = tk.Button(window, text='Exit', font=('Arial', 18),
                        width=14, height=2, command=game_exit)

    button1.place(relx=0.5, rely=0.3, anchor=tk.N, y=0)
    button2.place(relx=0.5, rely=0.3, anchor=tk.N, y=60)
    button3.place(relx=0.5, rely=0.3, anchor=tk.N, y=120)
    button4.place(relx=0.5, rely=0.3, anchor=tk.N, y=180)

    window.mainloop()
