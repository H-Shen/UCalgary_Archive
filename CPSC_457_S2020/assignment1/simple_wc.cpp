/**********************************************
 * Last Name:   <your last name>
 * First Name:  <your first name>
 * Student ID:  <your student ID number>
 * Course:      CPSC 457
 * Tutorial:    <your tutorial sction>
 * Assignment:  1
 *
 * File name: simple_wc.cpp
 *********************************************/

#include <stdio.h>
#include <unistd.h>
#include <ctype.h>
#include <cstdint>

int main( int, char **)
{
  int fd = STDIN_FILENO; // input will be standard input

  // our boolean mini-state, true = we are in a middle of a word
  bool inside_word = false;
  // setup 3 counters
  uint64_t word_count = 0;
  uint64_t line_count = 0;
  uint64_t char_count = 0;
  // read file character by character and count words
  while(1) {
    // read in a single character
    char c;
    if( read( fd, & c, 1) < 1) break; // quit loop on EOF
    // count every character
    char_count ++;
    // count new lines
    if( c == '\n') {
      line_count ++;
    }
    // update our state
    if( isspace(c)) {
      inside_word = false;
    }
    else {
      // update word count if starting a new word
      if( ! inside_word) word_count ++;
      inside_word = true;
    }
  }

  // print results
  printf( "%7lu %7lu %7lu\n",
	  line_count,
	  word_count,
	  char_count
	  );

  return 0;
}
