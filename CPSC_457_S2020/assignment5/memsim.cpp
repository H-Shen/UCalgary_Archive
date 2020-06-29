/*************************************************
 * Last Name:   Shen
 * First Name:  Haohu
 * Student ID:  30063099
 * Course:      CPSC 457
 * Tutorial:    T01
 * Assignment:  5
 * Question:    1
 * Date:        2020.06.23
 *
 * File name: memsim.cpp
 * Compile by: g++ -Wall -O2 memsim.cpp -o memsim
 * Compiled using SSH on linux.cpsc.ucalgary.ca
 ************************************************/

// Include header files here
#include <cstdio>
#include <list>
#include <set>
#include <string>
#include <vector>

// Define the type alias for long long
using ll = long long;

// Define all constants here
constexpr int NUMBER_OF_ARGUMENTS = 2;
constexpr ll MINIMAL_PAGE_SIZE = 1;
constexpr ll MAXIMAL_PAGE_SIZE = 1000000;
constexpr int MAXIMAL_NUMBER_IN_A_TAG = 1000;
constexpr int FAKE_TAG_FOR_A_FREE_BLOCK = -1;
constexpr int OFFSET =
    50; // An offset is the flexibility we give to the size of containers used
// in problem based on the limit of input in order to avoid hard-to-find
// out-of-range issues
const char USAGE[] = "Usage: ./memsim [page size]\n\n";

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
  // If x is a negative number, print '-'
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

/**
 * The function that checks if a given string can be converted to a valid page
 * size.
 *
 * @param str           the string to convert
 * @param page_size_ref   the reference that stores the page size
 * @return              true if the conversion is successful, otherwise return
 *                      false
 */
inline static bool is_a_valid_page_size(const std::string &str,
                                        ll &page_size_ref) {
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
  // Check if 'str' is an integer i such that MINIMAL_PAGE_SIZE < i <
  // MAXIMAL_PAGE_SIZE
  try {
    // Update the reference
    page_size_ref = std::stoll(str);
    if (page_size_ref < MINIMAL_PAGE_SIZE ||
        page_size_ref > MAXIMAL_PAGE_SIZE) {
      return false;
    }
  } catch (...) {
    // Return false if any exception happens, such as a number out of
    // range of INT is given
    return false;
  }
  return true;
}

// Define the class that represents a chunk
struct Chunk {
  // Constructors with arguments
  Chunk(int tag, ll size, ll address)
      : tag(tag), size(size), address(address) {}
  Chunk(int tag, ll size) : Chunk(tag, size, -1) {}
  // Data members
  int tag;
  ll size;
  ll address;
};

// Define the type alias for an std::list<Chunk>::iterator
using Chunk_Reference = std::list<Chunk>::iterator;

// Define a custom comparator for the std::list<Chunk>::iterator for our
// red black tree
struct custom_comparator {
  // Pick the chunk with smallest size, if there is a tie, pick the one with
  // the largest address, since it is close to the end of the list
  bool operator()(const Chunk_Reference &lhs,
                  const Chunk_Reference &rhs) const {
    if (lhs->size == rhs->size) {
      return (lhs->address < rhs->address);
    }
    return (lhs->size < rhs->size);
  }
};

// Define all global variables here
// Initialize a variable to store the page size
ll page_size = 0;
// Initialize a variable to store the size to allocate of each request of
// allocation
ll size_to_allocate = 0;
// Initialize a variable to store the largest free block at the end of the
// simulation
ll largest_free_block = 0;
// Initialize a variable to store total pages requested
ll total_pages_requested = 0;
// Initialize a doubly-linked-list to store chunks
std::list<Chunk> chunks;
// Initialize a doubly-linked-list to a single fake chunk with helps us find
// the lower bound in the red black tree
std::list<Chunk> dummy_chunk_list{Chunk(FAKE_TAG_FOR_A_FREE_BLOCK, -1)};
// Initialize an array of std::vector<Chunk_Reference> such that
// tagged_blocks[tag] stores all allocated chunks with this tag
std::vector<Chunk_Reference> tagged_blocks[MAXIMAL_NUMBER_IN_A_TAG + OFFSET];
// Initialize a red black tree that store all free blocks and order by the
// criterion defined in custom_comparator
std::set<Chunk_Reference, custom_comparator> free_blocks;

int main(int argc, char *argv[]) {

  // Report and quit if the number of arguments is invalid
  if (argc != NUMBER_OF_ARGUMENTS) {
    fputs_unlocked("No page size is given!\n", stdout);
    fputs_unlocked(USAGE, stdout);
    exit(-1);
  }
  // Report and quit if the page size is invalid
  else if (!is_a_valid_page_size(argv[1], page_size)) {
    fputs_unlocked("The page size is invalid!\n", stdout);
    exit(-1);
  }

  int tag;
  ll pages_requested;
  ll new_address;
  std::list<Chunk>::iterator temp_iter;
  // Read the request till EOF
  while (IO::read<int>(tag)) {
    // Case 1: The request of de-allocation
    if (tag < 0) {
      tag = -tag;
      // Check all chunks with the same tags, after processing each tag, we
      // mark it as a free block, insert it to the red black tree and remove
      // it from the vector
      while (!tagged_blocks[tag].empty()) {
        auto i = tagged_blocks[tag].back();
        i->tag = FAKE_TAG_FOR_A_FREE_BLOCK;
        // Merge any left adjacent free chunks
        if (i != chunks.begin()) {
          temp_iter = prev(i, 1);
          if (temp_iter->tag == FAKE_TAG_FOR_A_FREE_BLOCK) {
            i->size += temp_iter->size;
            free_blocks.erase(temp_iter);
            chunks.erase(temp_iter);
          }
        }
        // Merge any right adjacent free chunks
        temp_iter = next(i, 1);
        if (temp_iter != chunks.end()) {
          if (temp_iter->tag == FAKE_TAG_FOR_A_FREE_BLOCK) {
            i->size += temp_iter->size;
            free_blocks.erase(temp_iter);
            chunks.erase(temp_iter);
          }
        }
        // Re-address the chunk being processed
        if (i == chunks.begin()) {
          i->address = 0;
        } else {
          temp_iter = prev(i, 1);
          i->address = temp_iter->address + temp_iter->size;
        }
        // Store it to the free blocks and remove it from the tagged blocks
        free_blocks.insert(i);
        tagged_blocks[tag].pop_back();
      }
    }
    // Case 2: The request of allocation
    else {
      // Obtain the size to allocate
      IO::read<ll>(size_to_allocate);
      // Try to find the lower bound of free block
      dummy_chunk_list.begin()->size = size_to_allocate;
      auto best_fit_chunk_reference =
          free_blocks.lower_bound(dummy_chunk_list.begin());
      // Sub-case 1: Cannot find any free block since no free blocks or every
      // free block's space is too small to allocate
      if (free_blocks.empty() ||
          best_fit_chunk_reference == free_blocks.end()) {
        // Sub-sub-case 1: Chunk is empty, query pages from the OS
        if (chunks.empty()) {
          pages_requested = size_to_allocate / page_size;
          while (pages_requested * page_size < size_to_allocate) {
            ++pages_requested;
          }
          total_pages_requested += pages_requested;
          chunks.emplace_back(Chunk(tag, size_to_allocate));
          auto chunk_ref_to_a_new_allocated_one = chunks.begin();
          chunk_ref_to_a_new_allocated_one->address = 0;
          tagged_blocks[tag].emplace_back(chunk_ref_to_a_new_allocated_one);
          if (pages_requested * page_size > size_to_allocate) {
            new_address = chunk_ref_to_a_new_allocated_one->address +
                          chunk_ref_to_a_new_allocated_one->size;
            chunks.emplace_back(Chunk(
                FAKE_TAG_FOR_A_FREE_BLOCK,
                pages_requested * page_size - size_to_allocate, new_address));
            // add it to the red black tree as well
            free_blocks.insert(--chunks.end());
          }
        }
        // Sub-sub-case 2: Chunk is not empty and the last one is a free chunk,
        // try to split it
        else if (chunks.back().tag == FAKE_TAG_FOR_A_FREE_BLOCK) {
          auto chunk_ref_to_back = --chunks.end();
          pages_requested =
              (size_to_allocate - chunk_ref_to_back->size) / page_size;
          while (pages_requested * page_size + chunk_ref_to_back->size <
                 size_to_allocate) {
            ++pages_requested;
          }
          total_pages_requested += pages_requested;
          // Remove the old free block
          free_blocks.erase(chunk_ref_to_back);
          // Sub-sub-sub-case 1: the free chunk will be completely allocated
          if (chunk_ref_to_back->size + pages_requested * page_size ==
              size_to_allocate) {
            chunk_ref_to_back->size = size_to_allocate;
            chunk_ref_to_back->tag = tag;
            tagged_blocks[tag].emplace_back(chunk_ref_to_back);
          }
          // Sub-sub-sub case 2: the free chunk will have some left after
          // allocation
          else {
            ll residual = chunk_ref_to_back->size +
                          pages_requested * page_size - size_to_allocate;
            chunk_ref_to_back->size = size_to_allocate;
            chunk_ref_to_back->tag = tag;
            tagged_blocks[tag].emplace_back(chunk_ref_to_back);
            chunks.emplace_back(Chunk(FAKE_TAG_FOR_A_FREE_BLOCK, residual));
            auto chunk_ref_to_new_free_chunk_at_the_back = --chunks.end();
            chunk_ref_to_new_free_chunk_at_the_back->address =
                chunk_ref_to_back->address + chunk_ref_to_back->size;
            free_blocks.insert(chunk_ref_to_new_free_chunk_at_the_back);
          }
        }
        // Sub-case 3: chunk is not empty and the last one is an allocated chunk
        else {
          pages_requested = size_to_allocate / page_size;
          while (pages_requested * page_size < size_to_allocate) {
            ++pages_requested;
          }
          new_address = chunks.back().address + chunks.back().size;
          total_pages_requested += pages_requested;
          chunks.emplace_back(Chunk(tag, size_to_allocate, new_address));
          auto chunk_ref_to_a_new_allocated_chunk_at_the_back = --chunks.end();
          tagged_blocks[tag].emplace_back(
              chunk_ref_to_a_new_allocated_chunk_at_the_back);
          ll residual = pages_requested * page_size - size_to_allocate;
          if (residual != 0) {
            new_address =
                chunk_ref_to_a_new_allocated_chunk_at_the_back->address +
                chunk_ref_to_a_new_allocated_chunk_at_the_back->size;
            chunks.emplace_back(
                Chunk(FAKE_TAG_FOR_A_FREE_BLOCK, residual, new_address));
            free_blocks.insert(--chunks.end());
          }
        }
      }
      // Sub-case 2: a best fit chunk has been found
      else {
        if ((*best_fit_chunk_reference)->size == size_to_allocate) {
          (*best_fit_chunk_reference)->tag = tag;
          tagged_blocks[tag].emplace_back(*best_fit_chunk_reference);
          free_blocks.erase(best_fit_chunk_reference);
        } else {
          auto copy_of_best_fit_chunk_reference = *best_fit_chunk_reference;
          free_blocks.erase(best_fit_chunk_reference);
          copy_of_best_fit_chunk_reference->size -= size_to_allocate;
          new_address = 0;
          if (copy_of_best_fit_chunk_reference != chunks.begin()) {
            auto chunk_before_best_fit_chunk_reference =
                prev(copy_of_best_fit_chunk_reference, 1);
            new_address = chunk_before_best_fit_chunk_reference->address +
                          chunk_before_best_fit_chunk_reference->size;
          }
          auto chunk_ref_to_the_new_allocated_one =
              chunks.emplace(copy_of_best_fit_chunk_reference,
                             Chunk(tag, size_to_allocate, new_address));
          copy_of_best_fit_chunk_reference->address =
              chunk_ref_to_the_new_allocated_one->address +
              chunk_ref_to_the_new_allocated_one->size;
          tagged_blocks[tag].emplace_back(chunk_ref_to_the_new_allocated_one);
          free_blocks.insert(copy_of_best_fit_chunk_reference);
        }
      }
    }
  }
  // Get the largest free chunk at the end of the simulation
  if (!free_blocks.empty()) {
    largest_free_block = (*free_blocks.rbegin())->size;
  }
  // Output the result
  fputs_unlocked("pages requested: ", stdout);
  IO::writeln<ll>(total_pages_requested);
  fputs_unlocked("largest free chunk: ", stdout);
  IO::writeln<ll>(largest_free_block);
  return 0;
}