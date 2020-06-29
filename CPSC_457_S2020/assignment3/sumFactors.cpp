/**********************************************************************
 * Last Name:   Shen
 * First Name:  Haohu
 * Student ID:  30063099
 * Course:      CPSC 457
 * Tutorial:    T01
 * Assignment:  3
 * Question:    1
 * Date:        2020.06.05
 *
 * File name: sumFactors.cpp
 * Compile by: g++ -lpthread -Wall -O3 sumFactors.cpp -o sumFactors -lm
 * Compiled using SSH on linux.cpsc.ucalgary.ca
 *********************************************************************/

// Routine Description:
//
// In order to parallelize the existing solution, we split the current algorithm
// into 2 stages WITHOUT changing it. From the original algorithm
// **int64_t getSmallestDivisor(int64_t n)** we can see that 'n' has been
// categorized into 4 cases, that is:
//
// 1) n is less than or equal to 3
// 2) n is greater than 3 and n can be divided by 2
// 3) n is greater than 3 and n cannot be divided by 2 but n can be divided by 3
// 4) n is greater than or equal to 5, n cannot be divided by 2 or 3
//
// After the observation we know that the Case 1), 2), 3) only takes O(1) time
// to execute, while the Case 4) takes O(sqrt(n)) time. Let's call Case 1), 2),
// 3) are trivial, and Case 4) is non-trivial.
//
// Therefore, on the first stage, we will directly read all inputs and
// categorize them since creating and using threads on at most 10000 integers at
// this stage are not efficient due to the time to allocate the workload, create
// and join threads. If the input is categorized into the first three cases,
// then the result can be calculated directly and we will sum it up to our
// final result. (the reason why I don't use multi-threads to read numbers from
// stdin is because the frequent lock/unlock, creation and termination of
// threads are overhead for this stage).
//
// Meanwhile, for those inputs that belong to the Case 4), we store it in a
// vector and handle them on the second stage.
//
// On the second stage, we process one number at a time in the vector mentioned
// before and use all threads to process that number, from the algorithm
// we know we will test the factors 5+6*0, 5+6*0+2, 5+6*1, 5+6*1+2,...,
// 5+6*t, 5+6*t+2 and the algorithm will stop as long as the smallest factor
// from them is found.
//
// When the 'WHILE' loop of the original algorithm is executed,
// it must be the case that i = 5+6*t <= sqrt(n), since t >= 0, it must be the
// case that n >= 5^2 = 25. And sqrt(n) is the maximal number of loops to check
// in the 'WHILE' loop.
//
// Thus, when we parallelize the algorithm, we will process all possible
// situations of 'n' such that n < 25 and n belongs to Case 4) out of the loop,
// that is, n = 5, 7, 11, 13, 17, 19, 23. We can see that all possible
// situations of 'n' are primes, thus we will return 0 directly as long as n <
// 25 in Case 4).
//
// On the other hand, for cases n >= 25, we will split the range of 't' (which
// is calculated base on 'n') into multiple intervals that each interval has
// roughly same length such that each thread processes an interval and each
// thread has a unique ID starts from 0. Since the algorithm stops once a factor
// that satisfies the condition is found, we will terminate a thread if a factor
// is found the factor is smaller than the number that the thread is checking.
//
// In order to improve the efficiency, when we process every number in the
// second stage using multi-threads, we re-use the threads by using the thread
// barrier. Additionally, we implement lock-less programming by using
// std::atomic as our synchronization mechanism since it has better performance.
//
// Here is a diagram on how I parallelize the algorithm in Case 4):

/*
                                +------------------------+    +------------------------+
                                |Barrier->Update->Barrier|    |Barrier->Update->Barrier|
                                +----------+-------------+    +------------------------+
                                           |                           |
               +----------+   +----------+ v +----------+              v +----------+
               | Number_0 +-->+ Number_1 +-+-> Number_2 +--->  ...... +->+ Number_n |
               +----+-----+   +----+-----+   +----+-----+                +----+-----+
                    |              |              |                           |
                    v              v              v                           v
+----------+   +----+-----+   +----+-----+   +----+-----+                +----+-----+
| Thread_0 +-->+Interval00+-->+Interval10+-->+Interval20+--->  ...... +->+Intervaln0|
+----------+   +----------+   +----------+   +----------+                +----------+
+----------+   +----------+   +----------+   +----------+                +----------+
| Thread_1 +-->+Interval01+-->+Interval11+-->+Interval21+--->  ...... +->+Intervaln1|
+----------+   +----------+   +----------+   +----------+                +----------+
+----------+   +----------+   +----------+   +----------+                +----------+
| Thread_2 +-->+Interval02+-->+Interval12+-->+Interval22+--->  ...... +->+Intervaln2|
+----------+   +----------+   +----------+   +----------+                +----------+
     .              .              .              .                           .
     .              .              .              .                           .
+----------+   +----------+   +----------+   +----------+                +----------+
| Thread_t +-->+Interval0t+-+>+Interval1t+-->+Interval2t+-+->  ...... +->+Intervalnt|
+----------+   +----------+ ^ +----------+   +----------+ ^              +----------+
                            |                             |
                  +---------+--------------+   +----------+-------------+
                  |Barrier->Update->Barrier|   |Barrier->Update->Barrier|
                  +------------------------+   +------------------------+

*/

// Also notice that, for better performance, I tune the order
// of accessing memory in atomic operations, the atomic operation I use
// std::memory_order_relaxed
// is because I only need the atomicity is guaranteed without
// synchronization with other threads in this operation; when I use
// std::memory_order_consume is because I need the current atomic operation
// to be done before any following atomic operations.

// Include header files here
#include <atomic>
#include <cmath>
#include <iostream>
#include <pthread.h>
#include <string>
#include <vector>

// Define all type aliases here
using ll = long long;
using Interval =
    std::pair<ll, ll>; // a pair will store [lower_bound, upper_bound)

// The definition of a custom data structure that stores extra data for
// each thread
struct Thread_Data {

  // The vector that stores all numbers to process
  std::vector<ll> numbers_to_process;

  // The vector that stores an array of intervals such that each interval is
  // mapping to each number being processed by this thread in the vector above
  // NOTICE: Each thread will process all numbers, and for every number,
  // each thread only process
  std::vector<Interval> interval_list;

  int thread_id;

  // The index of number in 'numbers_to_process'
  // which should be processed at this round for this thread
  size_t index = 0;

  // Sum of smallest factors
  ll sum_of_smallest_factors = 0;
};

// Define all constants here
constexpr int MAXIMAL_NUMBER_OF_INPUTS = 10000;
constexpr int MINIMAL_THREADS = 1;
constexpr int MAXIMAL_THREADS = 256;
const Interval DUMMY_INTERVAL(-1, -1);
const char USAGE[] =
    "Usage: ./sumFactors <number of threads>\n\n"
    "Notice: Data must given from standard input. If the number of"
    "threads are not given, then only one thread is used.";
// The maximal number of valid arguments from command line
constexpr int NUMBER_OF_ARGUMENTS = 2;

// Define the atomic variable stores the smallest non-trivial divisor
// of a number in one round of test
std::atomic<ll> current_smallest_factor(0);

// Define the thread barrier instance
pthread_barrier_t barrier_instance;

/**
 * A function that takes an atomic of generic type and a value with a specified
 * order to access the memory. The function will update the atomic new_value to
 * the minimal value between its original value and the new value by repeatedly
 * checking and CAS.
 *
 * @tparam T            The type of atomic value
 * @param atomic_value  The atomic value
 * @param new_value     The new value that needs to be compared with the atomic
 *                      value
 * @param m             The memory order, it specifies how memory accesses,
 *                      including regular, non-atomic memory accesses, are to be
 *                      ordered around an atomic operation.
 */
template <typename T>
inline static void update_to_minimum(std::atomic<T> &atomic_value,
                                     const T &new_value,
                                     const std::memory_order &m) {
  T atomic_value_copy(atomic_value);
  while (atomic_value_copy > new_value &&
         !atomic_value.compare_exchange_weak(atomic_value_copy, new_value, m))
    ;
}

/**
 * The function checks if a string given is a valid integer under the context of
 * this problem, return false if the given string contains any character other
 * than digits or contains redundant leading zeros.
 *
 * @param str       The string to test
 * @return          True if a string is a valid integer under the context of
 *                  this problem, false otherwise
 */
inline static bool is_a_valid_integer(const std::string &str, int &threads) {
  // Check if str contains any characters other than digits
  for (const char &ch : str) {
    if (!isdigit(ch)) {
      return false;
    }
  }
  // Check if 'str' contains leading zeroes
  if (str.size() > 1 && str.front() == '0') {
    return false;
  }
  // Check if 'str' is an integer i such that 1 <= i <= 256
  try {
    // Update the global variable
    threads = std::stoi(str);
    if (threads < MINIMAL_THREADS || threads > MAXIMAL_THREADS) {
      return false;
    }
  } catch (...) {
    // Return false if any exception happens, such as a number out of
    // range of INT is given
    return false;
  }
  return true;
}

/**
 * The function splits the range [lower_bound, upper_bound) into numerous
 * intervals base on the threads given and let each interval can be as equal as
 * possible and it will a vector that stores all of these intervals.
 *
 * @param lower_bound   The lower bound of the test range
 * @param upper_bound   The upper bound of the test range
 * @param threads       The number of threads
 * @return              A vector that stores all intervals that represents
 *                      the range.
 */
inline static std::vector<Interval>
make_intervals(ll lower_bound, ll upper_bound, const int &threads) {
  // Initialize the vector to store the result
  std::vector<Interval> result;
  // Case 1: If only 1 thread is given, just use one interval to
  // cover the whole range
  if (threads == MINIMAL_THREADS) {
    result.emplace_back(Interval(lower_bound, upper_bound));
    return result;
  }

  // Case 2: If the number of threads assigned by the user is larger than
  // all length of the range, we assign each element in the range to a
  // thread. For those redundant threads, we assign a dummy element which
  // indicates that thread is useless when we process the number

  // Calculate the elements in the range
  ll length = upper_bound - lower_bound + 1;
  if (length <= threads) {
    for (; lower_bound <= upper_bound - 1; ++lower_bound) {
      result.emplace_back(Interval(lower_bound, lower_bound + 1));
    }
    while (static_cast<int>(result.size()) < threads) {
      result.emplace_back(DUMMY_INTERVAL);
    }
    return result;
  }

  // Case 3: Each thread can handle an interval and each interval has more
  // than 1 element
  ll length_of_intervals =
      static_cast<ll>((static_cast<double>(length) / threads));
  for (int i = 1; i <= threads - 1; ++i) {
    result.emplace_back(
        Interval(lower_bound, lower_bound + length_of_intervals));
    lower_bound += length_of_intervals;
  }
  result.emplace_back(Interval(result.back().second, upper_bound));
  return result;
}

/**
 * The function implements the main logic of each thread on how to test
 * an interval of a number and how to sync the result with other threads
 * that working on different intervals of the same number. The logic to test
 * if it is a non-trivial factor is exact the same as the 'WHILE' loop described
 * in the routine given by the instructor. The only difference is that the
 * thread needs to sync the result atomically which updates by other threads and
 * stops once a smaller non-trivial factor is found, or updates the result
 * atomically if no smaller non-trivial factor is found yet.
 *
 * @param thread_data   void pointer to the corresponding 'Thread_Data' instance
 */
inline void get_smallest_divisor(void *thread_data) {
  // Obtain the pointer to the 'Thread_Data' instance
  auto thread_data_ptr = reinterpret_cast<Thread_Data *>(thread_data);
  // Sub-case 1: The thread will not be used in this round
  if (thread_data_ptr->interval_list.at(thread_data_ptr->index) ==
      DUMMY_INTERVAL) {
    // Stop checking
    return;
  }
  // Sub-case 2: The number being processed is less than 25, we ignore it must
  // be a prime
  if (thread_data_ptr->numbers_to_process.at(thread_data_ptr->index) < 25) {
    // Stop checking
    return;
  }
  // Sub-case 3: The number being processed is larger than or equals to 25
  ll lower_bound =
      5 + 6 * thread_data_ptr->interval_list.at(thread_data_ptr->index).first;
  ll upper_bound =
      5 + 6 * thread_data_ptr->interval_list.at(thread_data_ptr->index).second;
  ll current_smallest_factor_copy;
  // A temporary variable used in CAS
  ll expect_zero(0);

  while (lower_bound < upper_bound) {
    // Sub-sub-case 1: A smaller factor has been found
    current_smallest_factor_copy =
        current_smallest_factor.load(std::memory_order_consume);
    if (current_smallest_factor_copy != 0 &&
        current_smallest_factor_copy < lower_bound) {
      // No need to check anymore
      return;
    }
    // Sub-sub-case 2: A smaller factor is found
    if (thread_data_ptr->numbers_to_process.at(thread_data_ptr->index) %
            lower_bound ==
        0) {
      // Update to the atomic result if the smaller factor is not found yet
      // or the new result is smaller than it atomically by CAS
      if (current_smallest_factor.compare_exchange_strong(
              expect_zero, lower_bound, std::memory_order_relaxed)) {
        return;
      }
      // Otherwise, change the atomic result to the smaller factor found
      // currently
      update_to_minimum<ll>(current_smallest_factor, lower_bound,
                            std::memory_order_relaxed);
      return;
    }
    // Update lower_bound for next sub-sub-case
    lower_bound += 2;
    // Sub-sub-case 3: A smaller factor is found
    if (thread_data_ptr->numbers_to_process.at(thread_data_ptr->index) %
            lower_bound ==
        0) {
      // Update to the atomic result if the smaller factor is not found yet
      // or the new result is smaller than it atomically by CAS
      if (current_smallest_factor.compare_exchange_strong(
              expect_zero, lower_bound, std::memory_order_relaxed)) {
        return;
      }
      update_to_minimum<ll>(current_smallest_factor, lower_bound,
                            std::memory_order_relaxed); // atomic
      return;
    }
    // Update lower_bound for next iteration
    lower_bound += 4;
  }
  // Sub-sub-case 4: No smaller factor is found in this interval, just return
}

/**
 * The function implements the main logic of each thread on how to process
 * non-trivial cases using the thread barrier.
 *
 * @param thread_data   void pointer to the corresponding 'Thread_Data' instance
 */
inline void *thread_main_logic(void *thread_data) {
  // Obtain the pointer to the 'Thread_Data' instance
  auto thread_data_ptr = reinterpret_cast<Thread_Data *>(thread_data);
  // Check every number in an iteration
  while (thread_data_ptr->index != thread_data_ptr->numbers_to_process.size()) {

    // Test the range to calculate the smallest non-trivial divisor
    get_smallest_divisor(thread_data);

    // Synchronize all threads here before updating the result
    pthread_barrier_wait(&barrier_instance);

    /** We allocate the job to the thread whose ID is 0 on
     * loading and resetting the atomic variable since this thread must
     * exist in any situation and one thread is enough to do the work.
     * Meanwhile, when we calculate the sum of smallest non-trivial factors
     * of all numbers after Stage 2, all we need to do is accessing the
     * 'Thread_Data' that maps to thread 0 instead of linear-scanning all
     * 'Thread_Data' instances.
     **/
    // We store the smallest factor calculated by all threads in the
    // thread whose ID is 0, we use this trick to avoid doing too
    // many useless atomic operations, which are expensive.
    if (thread_data_ptr->thread_id == 0) {
      // Update (atomic)
      thread_data_ptr->sum_of_smallest_factors +=
          current_smallest_factor.load(std::memory_order_consume);
      // Reset (atomic)
      current_smallest_factor.store(0, std::memory_order_relaxed);
    }
    // Synchronize all threads here before next round of test
    pthread_barrier_wait(&barrier_instance);
    // Update the index that points to the next number
    ++thread_data_ptr->index;
  }
  // Quit the thread if all intervals have been processed
  pthread_exit(nullptr);
}

int main(int argc, char *argv[]) {

  // For faster input/output, we use std::cin/std::cout and
  // disable the synchronization between standard C++ streams and standard
  // C streams, we also cancel the tie between std::cin and std::cout
  std::ios_base::sync_with_stdio(false);
  std::cin.tie(nullptr);

  // Initialize a variable that stores the number of threads
  int number_of_threads(0);

  // Parse the arguments from the command line
  // Case 1: If too few arguments are given, we set the number of threads
  // with 1 as its default value
  if (argc < NUMBER_OF_ARGUMENTS) {
    number_of_threads = MINIMAL_THREADS;
  }
  // Case 2: If too many arguments are given, report and quit
  else if (argc > NUMBER_OF_ARGUMENTS) {
    std::cout << "Too many arguments provided!\n"
                 "You must provide the number of threads!\n\n";
    std::cout << USAGE << '\n';
    exit(-1);
  }
  // Case 3: If the number of thread given is not in the range [1, 256],
  // report and quit
  else if (!is_a_valid_integer(argv[1], number_of_threads)) {
    std::cout << "Illegal number of threads!\n"
                 "The number of threads should be between 1 and 256!\n";
    std::cout << USAGE << '\n';
    exit(-1);
  }

  // Case 4: The number of arguments and the arguments are valid, start
  // reading numbers from standard input

  // Initialize a variable that stores the sum of all non-trivial
  // smallest factors of all numbers with 0
  ll sum_of_factors(0);

  // Initialize a vector that store all numbers which belong to the non
  // trivial case in the algorithm, we preserve enough slots and initialize
  // a counter of it
  std::vector<ll> numbers_in_the_non_trivial_case(MAXIMAL_NUMBER_OF_INPUTS);
  int counter_of_numbers_in_the_non_trivial_case = 0;

  // Initialize a variable to receive the number from standard input
  ll temp;

  // Initialize a variable counts how many numbers are being read so far
  int number_of_inputs = 0;

  // Start reading numbers from stdin
  while (std::cin >> temp) {

    // Update the counter
    ++number_of_inputs;
    // Report and quit if the number of inputs being read exceeds 10000,
    // because the case is almost impossible according to the constraints
    // given in the assignment, we give the compiler this branch prediction
    // information for better performance
    if (__builtin_expect(number_of_inputs > MAXIMAL_NUMBER_OF_INPUTS, 0)) {
      std::cout << "Too many numbers given!\n"
                   "You must give no more than 10000 numbers!"
                << '\n';
      exit(-1);
    }

    /** Stage 1 **/
    // We enter the Stage 1 to process all trivial cases here once
    // we have the input, and store all numbers that belong to the Case 4)
    // to a vector

    // Case 1: Ignores any numbers that are smaller than 2, ignore 2 and 3
    // as well since 2 and 3 are primes.
    if (temp <= 3) {
      continue;
    }
    // Case 2: For all even numbers except 2, their smallest non-trivial
    // factors are 2.
    else if (temp % 2 == 0) {
      sum_of_factors += 2;
      continue;
    }
    // Case 3: For all numbers that are multiples of 3 except 3, their
    // smallest non-trivial are 3.
    else if (temp % 3 == 0) {
      sum_of_factors += 3;
      continue;
    }
    // Case 4:
    numbers_in_the_non_trivial_case.at(
        counter_of_numbers_in_the_non_trivial_case) = temp;
    ++counter_of_numbers_in_the_non_trivial_case;
  }
  // Resize 'counter_of_numbers_in_the_non_trivial_case' to reduce useless
  // entries
  numbers_in_the_non_trivial_case.resize(
      counter_of_numbers_in_the_non_trivial_case);

  // Display the number of threads
  if (number_of_threads == MINIMAL_THREADS) {
    std::cout << "Using " << MINIMAL_THREADS << " thread." << '\n';
  } else {
    std::cout << "Using " << number_of_threads << " threads." << '\n';
  }

  // Corner case: If there are no non-trivial numbers, then there is no need
  // to proceed the stage 2, and we just directly output the result and
  // quit the routine
  if (numbers_in_the_non_trivial_case.empty()) {
    std::cout << "Sum of divisors = " << sum_of_factors << '\n';
    return 0;
  }

  /** Stage 2 **/
  // Initialize the maximal number of loops to check for a number according
  // to the routine description
  ll maximal_loops_to_check;
  // Initialize the lower bound and the upper bound(exclusive) of 't'
  // in the routine description
  ll lower_bound;
  ll upper_bound;
  // Initialize a vector of 'Thread_Data' instances where each instance
  // is mapping to a unique thread
  std::vector<Thread_Data> thread_data_lists(number_of_threads);
  // Number the thread ID in the list from 0
  for (size_t i = 0; i != thread_data_lists.size(); ++i) {
    thread_data_lists.at(i).thread_id = static_cast<int>(i);
  }
  // Pre-calculate intervals for all numbers and store them in a 2D array,
  // each row represents intervals for a number
  std::vector<std::vector<Interval>> interval_list_for_each_number(
      numbers_in_the_non_trivial_case.size());

  // For each number, make its intervals for multi-threads and store them
  // to the corresponding 'Thread_Data' instance.
  auto iter = interval_list_for_each_number.begin();
  for (const auto &n : numbers_in_the_non_trivial_case) {
    maximal_loops_to_check = sqrt(n);
    lower_bound = 0;
    upper_bound = (maximal_loops_to_check - 5) / 6 + 1;
    // Update this row
    *iter = make_intervals(lower_bound, upper_bound, number_of_threads);
    // Advance the iterator to the next row
    std::advance(iter, 1);
  }
  // Obtain the maximal number of columns in the 2D array
  size_t maximal_number_of_columns = 0;
  for (const auto &i : interval_list_for_each_number) {
    maximal_number_of_columns = std::max(maximal_number_of_columns, i.size());
  }
  // Copy interval lists to each thread data
  for (size_t i = 0; i != interval_list_for_each_number.front().size(); ++i) {
    for (size_t j = 0; j != interval_list_for_each_number.size(); ++j) {
      thread_data_lists.at(i).interval_list.emplace_back(
          interval_list_for_each_number.at(j).at(i));
    }
  }
  // Add numbers to each thread as well since we need to use them later
  for (auto &i : thread_data_lists) {
    i.numbers_to_process = numbers_in_the_non_trivial_case;
  }
  // Initialize the list of threads
  std::vector<pthread_t> threads_list(number_of_threads);
  // Initialize the thread barrier, report and quit if the barrier fails to
  // initialize
  if (pthread_barrier_init(&barrier_instance, nullptr, number_of_threads)) {
    std::cout << "Failed to initialize the thread barrier." << '\n';
    exit(-1);
  }
  // Spawn threads and pass 'Thread_Data' instances to corresponding threads
  auto thread_data_iter = thread_data_lists.begin();
  for (auto &i : threads_list) {
    // If any thread fails to create, report and quit
    if (pthread_create(&i, nullptr, thread_main_logic,
                       static_cast<void *>(&*thread_data_iter))) {
      std::cout << "Failed to create threads." << '\n';
      exit(-1);
    }
    // Update the iterator that points to the next 'Thread_Data' instance
    std::advance(thread_data_iter, 1);
  }
  // Waits for all threads finish their jobs
  // Report and quit if pthread_join() fails to join
  for (auto &i : threads_list) {
    if (pthread_join(i, nullptr)) {
      std::cout << "Failed to join threads." << '\n';
      exit(-1);
    }
  }
  // Destroy the thread barrier, report and quit if the barrier fails to
  // destroy
  if (pthread_barrier_destroy(&barrier_instance)) {
    std::cout << "Failed to destroy the thread barrier." << '\n';
    exit(-1);
  }
  // Sum the factors from the thread whose ID is 0
  sum_of_factors += thread_data_lists.at(0).sum_of_smallest_factors;
  // Output the result
  std::cout << "Sum of divisors = " << sum_of_factors << '\n';
  return 0;
}
