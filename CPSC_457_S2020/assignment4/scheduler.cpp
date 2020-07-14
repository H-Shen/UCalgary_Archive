/******************************************************
 * Last Name:   Shen
 * First Name:  Haohu
 * Student ID:  30063099
 * Course:      CPSC 457
 * Tutorial:    T01
 * Assignment:  4
 * Question:    2
 * Date:        2020.06.15
 *
 * File name: scheduler.cpp
 * Compile by: g++ -Wall -O2 scheduler.cpp -o scheduler
 * Compiled using SSH on linux.cpsc.ucalgary.ca
 *****************************************************/

// Include header files here
#include <deque>
#include <algorithm>
#include <fstream>
#include <iostream>
#include <queue>
#include <string>
#include <vector>

// Define all type aliases here
using ll = long long;

// The reason why I use int128 is because in the
// extreme case, the numbers during the calculation may overflow int64
using int128 = __int128;

// Define all constants here
const char USAGE[] = "Usage:\n"
                     "scheduler [CONFIG_FILE] [SJF|sjf]\n"
                     "scheduler [CONFIG_FILE] [RR|rr] [TIME_SLICE]";
const std::string ROUND_ROBIN = "RR";
const std::string SHORTEST_JOB_FIRST = "SJF";
const std::string CPU_IDLE = "-";
constexpr ll MINIMAL_TIMESLICE = 1;
constexpr ll MAXIMAL_TIMESLICE = 4611686018427387904; // which is 2^62
constexpr ll MINIMAL_CPU_BURST = 1;
constexpr ll MAXIMAL_CPU_BURST = 4611686018427387904;
constexpr ll MINIMAL_ARRIVAL_TIME = 0;
constexpr ll MAXIMAL_ARRIVAL_TIME = 4611686018427387904;
constexpr int MAXIMAL_NUMBER_OF_PROCESSES = 30;
constexpr size_t MAXIMAL_NUMBER_OF_ENTRIES = 100;

// The definition of a custom data structure that stores all information of a
// process
struct Process {
  explicit Process(ll arrivalTime, ll burstLength, int id)
      : arrival_time(arrivalTime), burst_length(burstLength), id(id),
        to_str('P' + std::to_string(id)) {}
  int128 arrival_time;
  int128 burst_length;
  int id;
  std::string to_str;
};

// The function checks if the given string 'str' can be converted into an
// integer x such that lower_bound <= x <= upper_bound, if it is the case then
// store x to reference 'number' and return true, otherwise return false.
inline bool is_a_valid_number(const std::string &str, ll &number,
                              const ll &lower_bound, const ll &upper_bound) {
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
  // Check if 'str' is an integer i such that 1 <= i <= 2^62
  try {
    // Update the reference
    number = std::stoll(str);
    // Return false if it is out of range
    if (number < lower_bound || number > upper_bound) {
      return false;
    }
  } catch (...) {
    // Return false if any exception happens, such as a number out of
    // range of LONG LONG is given
    return false;
  }
  return true;
}

// The function checks if the size of sequence is out of range, report and quit
// if it is the case.
inline void check_sequence_size(const std::vector<std::string> &sequence) {
  if (sequence.size() > MAXIMAL_NUMBER_OF_ENTRIES) {
    std::cout << "The number of entries in the sequence exceeds 100!" << '\n';
    exit(-1);
  }
}

// The function checks if 's' is already in the back of the sequence, quit if it
// is the case, otherwise add it to the sequence's back.
inline void check_sequence_and_add(std::vector<std::string> &sequence,
                                   const std::string &s) {
  if (!sequence.empty() && sequence.back() == s) {
    return;
  }
  sequence.emplace_back(s);
}

// The function implements the main logic of SJF scheduling algorithm by taking
// a list of 'Process' instances and the compressed execution sequence of all
// processes will be stored to 'sequence'
inline void shortest_job_first(std::deque<Process> &process_list,
                               std::vector<std::string> &sequence) {
  // Sort the process_list before further processing: the process with earlier
  // arrival time comes first, if ties then the process with shorter burst
  // length comes first, if ties the process with smaller id comes first
  sort(process_list.begin(), process_list.end(),
       [](const Process &lhs, const Process &rhs) {
         if (lhs.arrival_time == rhs.arrival_time) {
           if (lhs.burst_length == rhs.burst_length) {
             return (lhs.id < rhs.id);
           } else {
             return (lhs.burst_length < rhs.burst_length);
           }
         }
         return (lhs.arrival_time < rhs.arrival_time);
       });
  // Initialize a variable to store the current time
  int128 current_time = 0;
  // Initialize a priority queue that takes a list of processes, the process
  // with shorter burst length has higher priority, if ties the process with
  // earlier arrival time has higher priority, if ties the process with smaller
  // id has higher priority
  auto cmp = [](const Process &lhs, const Process &rhs) {
    if (lhs.burst_length == rhs.burst_length) {
      if (lhs.arrival_time == rhs.arrival_time) {
        return (lhs.id > rhs.id);
      }
      return (lhs.arrival_time > rhs.arrival_time);
    }
    return (lhs.burst_length > rhs.burst_length);
  };
  std::priority_queue<Process, std::vector<Process>, decltype(cmp)> pq(cmp);

  while (true) {
    if (pq.empty()) {
      // Case 1: No processes anymore, break the loop
      if (process_list.empty()) {
        break;
      }
      // Case 2: No tasks being processed currently, the CPU is in idle state
      else if (current_time < process_list.front().arrival_time) {
        current_time = process_list.front().arrival_time;
        check_sequence_and_add(sequence, CPU_IDLE);
        check_sequence_size(sequence);
      }
      // Case 3: Otherwise pop all its processes such that their arrival time <=
      // current time
      else {
        while (!process_list.empty()) {
          if (process_list.front().arrival_time <= current_time) {
            pq.push(process_list.front());
            process_list.pop_front();
          } else {
            break;
          }
        }
      }
    } else {
      // Handle the process in the front of pq one at a time
      Process current_process = pq.top();
      check_sequence_and_add(sequence, current_process.to_str);
      check_sequence_size(sequence);
      current_time += current_process.burst_length;
      pq.pop();
      // Add all processes that have arrived at or before the current time
      // to the priority queue
      while (!process_list.empty() &&
             process_list.front().arrival_time <= current_time) {
        pq.push(process_list.front());
        process_list.pop_front();
      }
    }
  }
}

// The function implements the main logic of RR scheduling algorithm by taking a
// quantum, a list of 'Process' instances and the compressed execution sequence
// of all processes will be stored to 'sequence'
inline void round_robin(const ll &time_slice,
                        std::deque<Process> &process_list,
                        std::vector<std::string> &sequence) {

  // Initialize a variable to store the current time
  int128 current_time = 0;
  // Initialize a variable to store the time slice using int128
  int128 time_slice_128 = time_slice;
  // Copy all processes from process_list to the ready queue
  std::queue<Process> ready_queue;

  // Define a lambda function that checks and pops all processes in the
  // waiting queue whose arrival time equals to current time to the ready queue
  auto update_process_list_at_current_time = [&]() {
    while (!process_list.empty()) {
      if (process_list.front().arrival_time == current_time) {
        ready_queue.push(process_list.front());
        process_list.pop_front();
      } else {
        break;
      }
    }
  };
  // Define a lambda function that checks and pops all processes in the
  // waiting queue whose arrival time is < current time to the ready queue
  auto update_process_list_before_current_time = [&]() {
    while (!process_list.empty()) {
      if (process_list.front().arrival_time < current_time) {
        ready_queue.push(process_list.front());
        process_list.pop_front();
      } else {
        break;
      }
    }
  };

  while (true) {
    // Case 1: the ready queue is empty
    if (ready_queue.empty()) {
      // sub-case 1: the waiting queue is empty
      if (process_list.empty()) {
        break;
      }
      // sub-case 2: the waiting queue is not empty and also currently no
      // processes in the ready queue
      else if (current_time < process_list.front().arrival_time) {
        current_time = process_list.front().arrival_time;
        check_sequence_and_add(sequence, CPU_IDLE);
        check_sequence_size(sequence);
      }
      // sub-case 3: the waiting queue is not empty and the current time is
      // updated, thus we need to check if any processes have arrived before or
      // just at current time
      else {
        update_process_list_before_current_time();
        update_process_list_at_current_time();
      }
    }
    // Case 2: the ready queue is not empty
    else {
      // Sub-case 1: Only one task in the ready queue and no processes in the
      // waiting queue anymore, just get it done
      if (ready_queue.size() == 1 && process_list.empty()) {
        check_sequence_and_add(sequence, ready_queue.front().to_str);
        ready_queue.pop();
        check_sequence_size(sequence);
      }
      // Sub-sub-case 2: Only one task in the ready queue and after the time
      // slice the process in the ready queue is still not finished but it is
      // already on the back of the sequence and still no processes arrives,
      // then we can directly jump the repeated scenario by division to obtain
      // N, such that:
      //
      // 1. current_time + time_slice * N < process_list.front().arrival_time
      // 2. time_slice * N < ready_queue.front().burst_length
      // 3. N is as large as possible
      //
      // Special case for this situation:
      // 100000000000 200000000000
      // 300000000000 400000000000
      // ./scheduler config_file rr 3
      else if (ready_queue.size() == 1 && !process_list.empty() &&
               ready_queue.front().burst_length > time_slice_128 &&
               current_time + time_slice_128 < process_list.front().arrival_time &&
               (!sequence.empty() &&
                sequence.back() == ready_queue.front().to_str)) {
        int128 N = std::min(ready_queue.front().burst_length / time_slice_128,
                            (process_list.front().arrival_time - current_time) /
                                    time_slice_128);
        while (current_time + time_slice_128 * N <
                   process_list.front().arrival_time &&
               time_slice_128 * N < ready_queue.front().burst_length) {
          ++N;
        }
        --N;
        current_time += time_slice_128 * N;
        ready_queue.front().burst_length -= time_slice_128 * N;
      }
      // Sub-case 2: Move the process on the front of queue to the CPU and it
      // will be preempted since its burst time > time slice, thus when it is
      // finished it will append to the back of the ready queue
      else if (ready_queue.front().burst_length > time_slice_128) {
        // Update the compressed sequence
        check_sequence_and_add(sequence, ready_queue.front().to_str);
        check_sequence_size(sequence);
        ready_queue.front().burst_length -= time_slice_128;
        current_time += time_slice_128;
        // Add all processes that have arrived before the process in the CPU is
        // preempted
        update_process_list_before_current_time();
        // Move the front process to the tail of the ready queue
        ready_queue.push(ready_queue.front());
        ready_queue.pop();
        // Add all processes that have arrived at the current time to the ready
        // queue
        update_process_list_at_current_time();
      }
      // Sub-case 3: Move the process on the front of queue to the CPU and it
      // will be done in the CPU since its burst time <= time slice, thus when
      // it is finished it will remove from the ready queue
      else {
        // Update the compressed sequence
        check_sequence_and_add(sequence, ready_queue.front().to_str);
        check_sequence_size(sequence);
        current_time += ready_queue.front().burst_length;
        // Add all processes that have arrived before the process in the CPU is
        // done
        update_process_list_before_current_time();
        // Remove the front process since it is done
        ready_queue.pop();
        // Add all processes that have arrived
        update_process_list_at_current_time();
      }
    }
  }
}

int main(int argc, char *argv[]) {

  // For faster input/output, we use std::cin/std::cout and
  // disable the synchronization between standard C++ streams and standard
  // C streams, we also cancel the tie between std::cin and std::cout
  std::ios_base::sync_with_stdio(false);
  std::cin.tie(nullptr);

  // Initialize the variable that stores the time slice for RR
  ll time_slice;
  // Initialize the vector to store the compressed sequence
  std::vector<std::string> sequence;
  // Initialize a dequeue to simulate the waiting queue
  std::deque<Process> process_list;
  // Check if the number of arguments is valid, report and quit if it is not the
  // case
  if (argc < 3 || argc > 4) {
    std::cout << "Invalid number of arguments!\n" << USAGE << '\n';
    exit(-1);
  }
  // Check the schedule type
  std::string schedule_type = argv[2];
  // Convert the schedule type to uppercase
  transform(schedule_type.begin(), schedule_type.end(), schedule_type.begin(),
            ::toupper);
  // Report and quit if the schedule type is not valid
  if (schedule_type != ROUND_ROBIN && schedule_type != SHORTEST_JOB_FIRST) {
    std::cout << "Invalid schedule type!\n" << USAGE << '\n';
    exit(-1);
  }
  // Report and quit if a time slice is assigned to a SJF schedule
  if (schedule_type == SHORTEST_JOB_FIRST && argc == 4) {
    std::cout << "SJF does not accept timeslice." << '\n';
    exit(-1);
  }
  // Report and quit if no time slice is assigned to a RR schedule or an invalid
  // time slice is assigned to a RR schedule
  else if (schedule_type == ROUND_ROBIN) {
    if (argc != 4) {
      std::cout << "You must assign a timeslice for RR schedule!\n"
                << USAGE << '\n';
      exit(-1);
    } else if (!is_a_valid_number(argv[3], time_slice, MINIMAL_TIMESLICE,
                                  MAXIMAL_TIMESLICE)) {
      std::cout << "The timeslice of RR is invalid!\n" << USAGE << '\n';
      exit(-1);
    }
  }
  // Parse the configuration file
  std::ifstream config_file_stream;
  config_file_stream.open(argv[1]);
  if (config_file_stream.is_open()) {
    std::string arrival_time_str;
    std::string cpu_burst_length_str;
    ll arrival_time;
    ll cpu_burst_length;
    int process_id = 0; // Initialize a process id from 0
    // Read data from stdin till EOF
    while (config_file_stream >> arrival_time_str >> cpu_burst_length_str) {
      // Report and quit if any arrival time is invalid
      if (!is_a_valid_number(arrival_time_str, arrival_time,
                             MINIMAL_ARRIVAL_TIME, MAXIMAL_ARRIVAL_TIME)) {
        std::cout << arrival_time_str << " <- The arrival time is invalid!"
                  << '\n';
        exit(-1);
      }
      // Report and quit if any burst time is invalid
      if (!is_a_valid_number(cpu_burst_length_str, cpu_burst_length,
                             MINIMAL_CPU_BURST, MAXIMAL_CPU_BURST)) {
        std::cout << cpu_burst_length_str
                  << " <- The cpu burst length is invalid!" << '\n';
        exit(-1);
      }
      process_list.emplace_back(
          Process(arrival_time, cpu_burst_length, process_id));
      ++process_id;
      // Report and quit if processes given exceed the limitation
      if (__builtin_expect(process_id > MAXIMAL_NUMBER_OF_PROCESSES, 0)) {
        std::cout << "Too many processes given!\n"
                     "You must give no more than 30 processes!"
                  << '\n';
        exit(-1);
      }
    }
  }
  // Report and quit if the config file is invalid
  else {
    std::cout << "Cannot open the configuration file!" << '\n';
    exit(-1);
  }
  // Handle a corner case
  if (process_list.empty()) {
    std::cout << "Seq:" << '\n';
    return 0;
  }
  // Initialize the simulation of schedule
  if (schedule_type == SHORTEST_JOB_FIRST) {
    shortest_job_first(process_list, sequence);
  } else {
    round_robin(time_slice, process_list, sequence);
  }
  // Output
  std::cout << "Seq: ";
  bool first_item = true;
  for (const auto &i : sequence) {
    if (first_item) {
      first_item = false;
    } else {
      std::cout << ',';
    }
    std::cout << i;
  }
  std::cout << '\n';

  return 0;
}
