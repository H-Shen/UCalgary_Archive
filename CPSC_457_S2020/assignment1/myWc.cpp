/*******************************************************
 * Last Name:   Shen
 * First Name:  Haohu
 * Student ID:  30063099
 * Course:      CPSC 457
 * Tutorial:    T01
 * Assignment:  1
 * Question:    5
 * Date:        2020.05.18
 *
 * File name: myWc.cpp
 * Compile by: g++ -std=c++17 -Wall -O2 myWc.cpp -o myWc
 * Compiled using SSH on linux.cpsc.ucalgary.ca
 ******************************************************/

// Include header files
#include <cctype>  // isspace
#include <cstdint> // uint64_t
#include <cstdio>  // fprintf, printf
#include <cstdlib> // exit

#include <unistd.h> // STDIN_FILENO, read

// Define all constants
constexpr int NUMBER_OF_ARGUMENTS = 1;
constexpr int BUFFER_SIZE = 1024 * 1024;

// Define the input buffer whose size is 1MiB
char input_buffer[BUFFER_SIZE];

int main(int argc, char *argv[]) {

  // Since input must be read from standard input, no file names should
  // be given. Thus we will check if the number of arguments given is correct.
  // Exit -1 if it is incorrect.
  if (argc != NUMBER_OF_ARGUMENTS) {
    fprintf(stderr, "illegal number of arguments!\n"
                    "myWc only supports reading from stdin!\n");
    exit(-1);
  }

  // Our boolean mini-state, true = we are in a middle of a word
  bool inside_word = false;
  // setup 3 counters
  uint64_t word_count = 0;
  uint64_t line_count = 0;
  uint64_t char_count = 0;

  // Read file to input_buffer and count words again and again until EOF
  // Set a counter to obtain the bytes being read
  ssize_t bytes_read;
  while (true) {
    // Read standard input to input_buffer and
    bytes_read = read(STDIN_FILENO, &input_buffer, BUFFER_SIZE);
    // Case 1: Quit the loop if reading end-of-file
    if (bytes_read == 0) {
      break;
    }
    // Case 2: Exit -1 with an error message if an error is encountered
    else if (bytes_read < 0) {
      fprintf(stderr, "Error occurred during processing the file!\n");
      exit(-1);
    }
    // Case 3: Otherwise, process input_buffer
    for (ssize_t i = 0; i != bytes_read; ++i) {
      // count every character
      ++char_count;
      // count new lines
      if (input_buffer[i] == '\n') {
        ++line_count;
      }
      if (isspace(input_buffer[i])) {
        inside_word = false;
      } else {
        // update word count if starting a new word
        if (!inside_word) {
          ++word_count;
        }
        inside_word = true;
      }
    }
  }

  // print results
  printf("%7lu %7lu %7lu\n", line_count, word_count, char_count);

  return 0;
}
