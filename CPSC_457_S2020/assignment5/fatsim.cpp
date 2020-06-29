/**************************************************
 * Last Name:   Shen
 * First Name:  Haohu
 * Student ID:  30063099
 * Course:      CPSC 457
 * Tutorial:    T01
 * Assignment:  5
 * Question:    2
 * Date:        2020.06.23
 *
 * File name: fatsim.cpp
 * Compile by: g++ -Wall -O2 fatsim.cpp -o fatsim
 * Compiled using SSH on linux.cpsc.ucalgary.ca
 **************************************************/

// Routine Description:
// To calculate the blocks in largest file, we can make the directed component
// that includes the null pointer as a m-ary tree with the null pointer as its
// root and the number of such component will be at most 1, then we can use
// breath-first search to obtain the blocks in largest file of the tree which
// is actually the maximal depth of the tree. And the blocks not in any file is
// the number of all blocks reduce the size of the m-ary tree without the root.
//
// I emailed the TA and got the feedback that we can trust the dataset given
// must follow the format and limitation given in the description of the
// assignment, thus less defensive programming is involved here, also for the
// purpose of better performance.

// Include header files here
#include <cctype>
#include <cstdio>
#include <cstdlib>
#include <queue>
#include <vector>

// Define all constants here
constexpr int NUMBER_OF_ARGUMENTS = 1;
constexpr int MINIMUM_NUMBER_OF_ENTRIES = 1;
constexpr int MAXIMUM_NUMBER_OF_ENTRIES = 1000000;
constexpr int NULL_POINTER = -1;
constexpr int OFFSET =
    50; // An offset is the flexibility we give to the size of containers used
// in problem based on the limit of input in order to avoid hard-to-find
// out-of-range issues

// An IO wrapper that quickly read integers from stdin and write to stdout using
// unlocked getchar() and unlocked putchar()
namespace IO {

/**
 * A template function that reads a generic type from STDIN and stores the
 * result into a reference using unlocked getchar()
 *
 * @tparam T    the generic type, normally are int or long long
 * @param t     the reference to a generic type
 * @return      true if an integer can be parsed and stored successfully,
 * otherwise return false, that is, return false when an EOF is encountered
 */

template <typename T> inline bool read(T &t) {
  // Initialize an integer to indicate if the number is negative or not
  int sign = 0;
  // Initialize an integer to receive the ascii of a character from stdin
  int ch = getchar_unlocked();
  // Eat all non-digit characters, stop when an EOF is encountered
  while (!isdigit(ch)) {
    if (ch == EOF) {
      return false;
    }
    // Check if the current character is '-'
    sign |= ch == '-';
    ch = getchar_unlocked();
  }
  // Reset the reference
  t = 0;
  // Repeatedly reading digits and store them to 't' until a non-digit is
  // encountered
  while (isdigit(ch)) {
    t = t * 10 + ch -
        48; // t = t * 10 + (ch - '0'), use 48 to avoid redundant conversion
    ch = getchar_unlocked();
  }
  // If 't' is negative, times it with -1
  if (sign) {
    t = -t;
  }
  return true;
}

/**
 * A template function that recursively write each digit of a number to the
 * stdout with unlocked putchar()
 *
 * @tparam T    the generic type, normally are int or long long
 * @param x     the number to output
 */
template <typename T> inline void write(T x) {
  // If 'x' is a negative number, print '-'
  if (x < 0) {
    x = -x;
    putchar_unlocked('-');
  }
  // Recursively output every digit of 'x'
  if (x > 9) {
    write(x / 10);
  }
  putchar_unlocked(x % 10 + 48); // (x % 10) + '0'
}

/**
 * A template function that recursively write each digit of a number to the
 * stdout by calling write(T x) and then change the line.
 *
 * @tparam T    the generic type, normally are int or long long
 * @param x     the number to output
 */
template <typename T> inline void writeln(T x) {
  write(x);
  putchar_unlocked('\n');
}
} // namespace IO

// We initialize a vector to store all edges from input, that is, every
// (i, entries[i]) will represent a directed edge in the graph
int entries[MAXIMUM_NUMBER_OF_ENTRIES + OFFSET];

// We initialize a node id for our null pointer, we use the value of
// 'number_of_entries' to indicate the null pointer instead of -1
int node_id_of_null_pointer;

// We initialize a reverse_adjacency_list here, for example, if a node A has
// been linked by node B, node C, node D in our original graph, then it will be
// stored as reverse_adjacency_list[node A] = {node B, node C, node C}, that is,
// we reverse the direction of all edges in our graph in reverse_adjacency_list.
// We will use such reverse adjacency list to represent a m-ary tree with
// the null pointer as its root.
std::vector<std::vector<int>> reverse_adjacency_list;
int size_of_m_ary_tree = 0;

/**
 * The function implements a BFS from a root given to obtain the size and the
 * maximal depth of the tree
 *
 * @param node_id   the root of the tree
 * @return          the maximal depth of the tree
 */
inline int bfs(int node_id) {
  int max_depth = -1; // Initialize the depth of the tree
  std::queue<int> q;
  int u;
  int queue_length;
  q.push(node_id);  // Push the root to the queue
  while (!q.empty()) {
    ++max_depth;
    queue_length = static_cast<int>(q.size());
    for (int i = 0; i < queue_length; ++i) {
      u = q.front();
      q.pop();
      for (const auto &v : reverse_adjacency_list[u]) {
        q.push(v);
      }
      ++size_of_m_ary_tree; // Update the size if a node left the queue
    }
  }
  return max_depth;
}

int main(int argc, char *argv[]) {

  // Report and quit if the number of arguments is invalid
  if (argc != NUMBER_OF_ARGUMENTS) {
    fputs_unlocked("The program should not take any arguments!\n", stdout);
    exit(-1);
  }
  // Initialize a variable to store number of entries
  int number_of_entries = 0;
  // Initialize a variable to store every entry being read
  int entry;
  // Read the number for a FAT entry until EOF
  while (IO::read<int>(entry)) {
    entries[number_of_entries] = entry;
    ++number_of_entries;
  }
  if (number_of_entries < MINIMUM_NUMBER_OF_ENTRIES) {
    fputs_unlocked("The program should be at least given 1 entry!\n", stdout);
    exit(-1);
  }
  // We use the value of 'number_of_entries' to indicate the null pointer
  // instead of -1 since we have to use it in the disjoint set
  node_id_of_null_pointer = number_of_entries;
  for (int i = 0; i < number_of_entries; ++i) {
    if (entries[i] == NULL_POINTER) {
      entries[i] = node_id_of_null_pointer;
    }
  }
  // Create the reverse adjacency list
  reverse_adjacency_list.resize(number_of_entries + OFFSET);
  for (int i = 0; i < number_of_entries; ++i) {
    reverse_adjacency_list[entries[i]].emplace_back(i);
  }
  // Since the distance of longest terminated chain is actually the depth of
  // m-ary tree, we do a breadth-first search from the root of the tree, the
  // reason I don't choose a DFS is that it may exceed the maximal depth of
  // recursion
  int blocks_in_largest_file = bfs(node_id_of_null_pointer);
  // Since all blocks not in any file must be not in the m_ary tree, we can
  // directly calculate its value
  int blocks_not_in_any_file = number_of_entries - (size_of_m_ary_tree - 1);
  // Output the result
  fputs_unlocked("blocks in largest file: ", stdout);
  IO::writeln(blocks_in_largest_file);
  fputs_unlocked("blocks not in any file: ", stdout);
  IO::writeln(blocks_not_in_any_file);
  return 0;
}
