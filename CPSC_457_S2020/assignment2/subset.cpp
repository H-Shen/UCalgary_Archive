/**********************************************************
 * Last Name:   Shen
 * First Name:  Haohu
 * Student ID:  30063099
 * Course:      CPSC 457
 * Tutorial:    T01
 * Assignment:  2
 * Question:    1
 * Date:        2020.05.26
 *
 * File name: subset.cpp
 * Compile by: g++ -lpthread -Wall -O2 subset.cpp -o subset
 * Compiled using SSH on linux.cpsc.ucalgary.ca
 *********************************************************/

// Include header files
#include <cassert>   // assert
#include <cstdio>    // fprintf, stderr, stdout
#include <cstdlib>   // exit
#include <pthread.h> // pthread_t, pthread_join, pthread_create, pthread_exit
#include <string>    // string
#include <vector>    // vector

// Define all aliases here
using ll = long long;
using ull = unsigned long long;

// Define all constants here
constexpr int NUMBER_OF_ARGUMENTS = 2;
constexpr int MAXIMAL_THREADS = 32;
const char USAGE[] = "Usage: ./subset <number_of_threads>\n"
                     "Notice: Data must given from standard input.\n";

// Define all global variables here
int number_of_threads;
std::vector<ll> numbers;

// Define the data structure we use for each thread
// It indicates that: for the current thread, we are testing all combinations
// that in [start_index, end_index)
struct Interval {
  // Argument constructor
  Interval(ll startIndex, ll endIndex, int threadIndex)
      : start_index(startIndex), end_index(endIndex),
        thread_index(threadIndex) {}

  // Class members
  ll start_index;
  ll end_index;
  int thread_index;
  // Store the solutions here calculated by this thread, which will help
  // us avoid the race condition
  ll solution = 0;
};

/**
 * The function checks if a string given is a valid integer under the context of
 * this problem, return false if the given string contains any character other
 * than digits or contains redundant leading zeros.
 *
 * @param str   the string to test
 * @return      true if a string is a valid integer under the context of this
 *              problem, false otherwise
 */
inline static bool is_valid_integer(const std::string &str) {
  // Check if str contains any characters other than digits
  for (const char &ch : str) {
    if (!isdigit(ch)) {
      return false;
    }
  }
  // Check if str contains leading zeroes
  if (str.size() > 1 && str.front() == '0') {
    return false;
  }
  // Check if str is an integer i such that 1 <= i <= MAXIMAL_THREADS
  try {
    // Update the global variable
    number_of_threads = std::stoi(str);
    if (number_of_threads < 1 || number_of_threads > MAXIMAL_THREADS) {
      return false;
    }
  } catch (...) {
    // Return false if any exception happens, such as a number out of
    // range of INT32 is given
    return false;
  }
  return true;
}

/**
 * The function will test of if any combination in [interval->start_index,
 * interval->end_index), calculate all its possible combinations and add to
 * solution.
 *
 * @param interval
 * @return
 */
void *test_range(void *interval) {
  ll sum;
  ull bits;
  auto interval_ptr = reinterpret_cast<Interval *>(interval);
  for (ll i = interval_ptr->start_index; i < interval_ptr->end_index; ++i) {
    sum = 0;
    bits = i;
    for (const auto &number : numbers) {
      // check lowest bit
      if (bits & 1ULL) {
        sum += number;
      }
      bits >>= 1ULL; // shift bits to the right
    }
    if (sum == 0) {
      ++interval_ptr->solution;
    }
  }
  // terminate the current calling thread
  pthread_exit(nullptr);
}

/**
 * Split the range [lower_bound, upper_bound) into numerous intervals
 * base on the threads given and let each interval can be as equal as possible.
 *
 * @param lower_bound   The lower bound of the test range
 * @param upper_bound   The upper bound of the test range
 * @return              An interval that represents the range
 */
inline static std::vector<Interval> make_intervals(ll lower_bound,
                                                   ll upper_bound) {

  std::vector<Interval> result;
  int thread_index = 0;
  // Case 1: The lower bound equals to the upper bound
  if (lower_bound == upper_bound) {
    result.emplace_back(Interval(lower_bound, upper_bound + 1, thread_index));
    // If the lower_bound equals to upper_bound, we only need 1 thread
    // no matter what the number of threads given the user is
    number_of_threads = static_cast<int>(result.size());
    return result;
  }
  // Case 2: We are only given 1 thread
  if (number_of_threads == 1) {
    result.emplace_back(Interval(lower_bound, upper_bound, thread_index));
    return result;
  }
  ll length = upper_bound - lower_bound + 1;
  // Case 3: The number of threads assigned by the user is larger than
  // all combinations we are going to test
  if (length <= number_of_threads) {
    // If the number of threads given is larger than the number of
    // combinations we are going to test, we will allocate one thread to
    // handle one combination, that is, we will reduce the threads
    // we are actually going to use
    for (; lower_bound <= upper_bound - 1; ++lower_bound, ++thread_index) {
      result.emplace_back(Interval(lower_bound, lower_bound + 1, thread_index));
    }
    number_of_threads = static_cast<int>(result.size());
    return result;
  }
  // Case 4: Each thread can handle an interval
  ll length_of_intervals =
      static_cast<ll>((static_cast<double>(length) / number_of_threads));
  for (int i = 1; i <= number_of_threads - 1; ++i, ++thread_index) {
    result.emplace_back(
        Interval(lower_bound, lower_bound + length_of_intervals, thread_index));
    lower_bound += length_of_intervals;
  }
  result.emplace_back(
      Interval(result.back().end_index, upper_bound, thread_index));

  // Debugging purpose: the number of intervals
  // must equal to the length of combinations
  assert(static_cast<int>(result.size()) == number_of_threads);

  return result;
}

int main(int argc, char *argv[]) {

  // Check if the number of arguments is correct
  // Report, show the usage and quit if the number of arguments is incorrect
  if (argc < NUMBER_OF_ARGUMENTS) {
    fprintf(stderr, "Too few arguments provided!\n"
                    "You must provide the number of threads!\n\n");
    fputs(USAGE, stderr);
    exit(-1);
  } else if (argc > NUMBER_OF_ARGUMENTS) {
    fprintf(stderr, "Too many arguments provided!\n"
                    "You must provide the number of threads!\n\n");
    fputs(USAGE, stderr);
    exit(-1);
  }
  // Check if the second argument is a number range 1..32
  // Report, show the usage and quit if the number of arguments is incorrect
  if (!is_valid_integer(argv[1])) {
    fprintf(stderr, "Illegal number of threads!\n"
                    "The number of threads should between 1 and 32!\n\n");
    fputs(USAGE, stderr);
    exit(-1);
  }
  // Receive integers from standard input and store to the container until
  // reaching EOF
  ll temp;
  while (scanf("%lld", &temp) != EOF) {
    numbers.emplace_back(temp);
  }
  // Display the data given
  fprintf(stdout, "Using %d thread(s) on %zd numbers.\n", number_of_threads,
          numbers.size());

  // Create argv[1] threads in a container
  std::vector<pthread_t> thread_list(number_of_threads);
  // Make intervals according to the number of threads and the
  // number of combinations
  ll lower_bound = 1;
  ll upper_bound = static_cast<ll>(1ULL << numbers.size());
  std::vector<Interval> intervals = make_intervals(lower_bound, upper_bound);

  // Test all combinations in every range stored in 'intervals' by creating
  // threads for each interval, notice all ranges are right-unbounded
  auto iter = intervals.begin();
  for (auto &i : thread_list) {
    // Create the thread for an interval
    // Report and quit if we failed to create any thread
    if (pthread_create(&i, nullptr, test_range, static_cast<void *>(&*iter))) {
      fprintf(stderr, "Failed on creating threads.\n");
      exit(-1);
    }
    // Update the iterator to point to the next interval
    std::advance(iter, 1);
  }
  // Waits for all threads finish their jobs
  // Report and quit if pthread_join() fails
  for (auto &i : thread_list) {
    if (pthread_join(i, nullptr)) {
      fprintf(stderr, "Failed on joining threads.\n");
      exit(-1);
    }
  }

  // Output the final result, which is summation of solutions from
  // each thread
  ll total_solution = 0;
  for (const auto &i : intervals) {
    total_solution += i.solution;
  }
  fprintf(stdout, "Subsets found: %lld\n", total_solution);
  return 0;
}
