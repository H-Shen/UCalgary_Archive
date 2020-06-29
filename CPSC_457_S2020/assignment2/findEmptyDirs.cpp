/**************************************************************
 * Last Name:   Shen
 * First Name:  Haohu
 * Student ID:  30063099
 * Course:      CPSC 457
 * Tutorial:    T01
 * Assignment:  2
 * Question:    3
 * Date:        2020.05.31
 *
 * File name: findEmptyDirs.cpp
 * Compile by: g++ -Wall -O2 findEmptyDirs.cpp -o findEmptyDirs
 * Compiled using SSH on linux.cpsc.ucalgary.ca
 *************************************************************/

// Include all headers here
#include <algorithm> // sort
#include <cstdio>    // fprintf, fputs
#include <cstdlib>   // exit
#include <cstring>   // strcmp
#include <dirent.h>  // DIR, dirent, readdir
#include <stack>     // stack
#include <string>    // string
#include <vector>    // vector

// Include all constants here
constexpr int NUMBER_OF_ARGUMENTS = 1;
const char USAGE[] = "Usage: ./findEmptyDirs\n";

int main(int argc, char *argv[]) {

  // Check if the number of arguments is correct
  // Report and quit if the number of arguments is incorrect
  if (argc != NUMBER_OF_ARGUMENTS) {
    fprintf(stderr, "Illegal number of arguments!\n\n");
    fputs(USAGE, stderr);
    exit(-1);
  }

  // Initialize a container to store all empty directories
  std::vector<std::string> empty_directory_list;
  // Initialize a stack for our Breadth-First Search algorithm
  std::stack<std::string> s;
  // Initialize a string to temporarily store the path during BFS
  std::string path;
  // Initialize a string to temporarily store the current path during BFS
  std::string dirname;
  // Initialize a DIR pointer to receive the directory stream being handled
  DIR *dir;
  // Initialize a pointer to directory file format
  dirent *de;
  // Initialize a boolean that indicates if the current directory is empty
  bool current_directory_is_empty;

  // In order to recursively find all sub-directories in the current
  // directory and list all empty directories (include the current
  // directory) using Breadth-First Search.
  s.push(".");
  while (!s.empty()) {
    dirname = s.top();
    s.pop();
    dir = opendir(dirname.c_str());
    // Pass if it is not a directory
    if (dir) {
      // Reset the boolean before reading the files in the directory
      current_directory_is_empty = true;
      while (true) {
        // Get the next directory entry in 'dir'
        de = readdir(dir);
        // Case 1: If the pointer is null, that is,
        // the end of the directory is reached or on error occurs,
        // break the loop
        if (!de) {
          break;
        }
        // Case 2: Skip if the sub directory entry being read is the alias
        // of current directory or the parent directory
        if (strcmp(de->d_name, ".") == 0 || strcmp(de->d_name, "..") == 0) {
          continue;
        }
        // Case 3: We encounter a file here, we don't know if
        // it is a directory or a file type, but we can make sure that
        // current directory is not empty, thus we will change the
        // boolean flag
        current_directory_is_empty = false;
        // Append a '/' to get the completed path
        path = dirname + '/' + de->d_name;
        // Push the result to the back of the stack
        s.push(path);
      }
      // After the BFS traversal on the level of current directory,
      // we store the path of it if it is empty
      if (current_directory_is_empty) {
        empty_directory_list.emplace_back(dirname);
      }
      // Close the stream to the 'dir'
      closedir(dir);
    }
  }
  // Sort the result in lexicographic order for better looking
  std::sort(empty_directory_list.begin(), empty_directory_list.end());
  // Output the result
  for (const auto &i : empty_directory_list) {
    fprintf(stdout, "%s\n", i.c_str());
  }
  return 0;
}
