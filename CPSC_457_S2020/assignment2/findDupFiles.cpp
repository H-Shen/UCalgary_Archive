/***********************************************
 * Last Name:   Shen
 * First Name:  Haohu
 * Student ID:  30063099
 * Course:      CPSC 457
 * Tutorial:    T01
 * Assignment:  2
 * Question:    4
 * Date:        2020.05.24
 *
 * File name: findDupFiles.cpp
 * Compile by: g++ -O2 -Wall findDupFiles.cpp -o findDupFiles
 * Compiled using SSH on linux.cpsc.ucalgary.ca
 **********************************************/

// Include header files here
#include <algorithm>
#include <unordered_map>
#include <vector>

// Include all constants here
constexpr int NUMBER_OF_ARGUMENTS = 1;
const char USAGE[] = "Usage: ./findDupFiles\n"
                     "Notice: Data must given from standard input.\n";

/**
 * The function parses the stream from 'fp' and stores the result to 'input'.
 *
 * @param input A reference to the string that stores the result of parsing
 * @param fp    An object that contains information to control a stream
 * @return      True if it encouters a '\n' or EOF when the result is not
 *              empty, false otherwise.
 */
inline static bool my_getline(std::string &input, FILE *fp = stdin) {
  // Clean the previous result that stores in 'input'
  input.clear();
  int ch;
  while (true) {
    // Get a character from the stream in each iteration
    ch = fgetc(fp);
    if (ch == '\n') {
      return true;
    } else if (ch == EOF) {
      return !input.empty();
    }
    input.push_back(ch);
  }
}

/**
 * The function calls an external command to compute and check SHA256 message
 * digest on the file name given.
 *
 * @param file_name The name of the file that needs to be computed
 * @param digest    A reference to a string that stores the result of the
 *                  SHA256 digest.
 * @return          True if the external command can be called on a valid file,
 *                  otherwise false.
 */
inline static bool has_digest(const std::string &file_name,
                              std::string &digest) {
  std::string cmdLine = "sha256sum " + file_name + " 2>/dev/null";
  FILE *fp = popen(cmdLine.c_str(), "r");
  // Return false if popen() returns NULL
  if (!fp) {
    return false;
  }
  std::string result;
  if (!my_getline(result, fp)) {
    return false;
  }
  // Return false if pclose() returns a non-zero value,
  if (pclose(fp)) {
    return false;
  }
  digest.clear();
  // Filter the result to obtain sha256sum
  for (const auto &ch : result) {
    if (isspace(ch)) {
      break;
    }
    digest.push_back(ch);
  }
  // Otherwise return true
  return true;
}

int main(int argc, char *argv[]) {

  // Check if the number of arguments is correct
  // Report and quit if the number of arguments is incorrect
  if (argc != NUMBER_OF_ARGUMENTS) {
    fprintf(stderr, "Illegal number of arguments!\n\n");
    fputs(USAGE, stderr);
    exit(-1);
  }

  // Parse and store all file names given from the standard input
  // and store them into a container
  std::string file_name;
  std::vector<std::string> file_list;
  while (my_getline(file_name)) {
    file_list.emplace_back(file_name);
  }

  // Initialize a string variable to temporarily store our digest of file
  std::string file_digest;
  // Initialize a hash map that maps unique digest to a unique Match Id
  std::unordered_map<std::string, int> digest_to_match_id;
  // Initialize a vector that maps unique Match Id as index to all file
  // names that have the same digest
  std::vector<std::vector<std::string>> match_id_to_file_names;
  // Initialize a vector that stores all files whose digest cannot be computed
  std::vector<std::string> files_cannot_compute_digest;
  // Initialize the Match Id
  int match_id = 0;

  // Sort the file names given lexicographically
  std::sort(file_list.begin(), file_list.end());
  // Put each file into the group with the same digest
  for (const auto &i : file_list) {
    // Case 1: The file's digest can be computed
    if (has_digest(i, file_digest)) {
      // Subcase 1: The file's digest is already in the hash map
      if (digest_to_match_id.find(file_digest) == digest_to_match_id.end()) {
        digest_to_match_id[file_digest] = match_id;
        ++match_id;
        match_id_to_file_names.emplace_back(std::vector<std::string>{i});
      }
      // Subcase 2: The file's digest is not in the hash map yet
      else {
        match_id_to_file_names.at(digest_to_match_id[file_digest])
            .emplace_back(i);
      }
    }
    // Case 2: The file's digest cannot be computed
    else {
      files_cannot_compute_digest.emplace_back(i);
    }
  }

  // Output all files with its corresponding match id
  // Reset the Match Id
  match_id = 1;
  for (size_t i = 0; i != match_id_to_file_names.size(); ++i) {
    // Since we category at least 2 file names that have the same digest
    // with an increasing match id, we ignore those file names with
    // unique digest and reorder the match id
    if (match_id_to_file_names.at(i).size() > 1) {
      fprintf(stdout, "Match %d:\n", match_id);
      for (const auto &j : match_id_to_file_names.at(i)) {
        fprintf(stdout, "  - %s\n", j.c_str());
      }
      ++match_id;
    }
  }
  // Output all files whose digest cannot be computed
  if (!files_cannot_compute_digest.empty()) {
    fputs("Could not compute digests for files:\n", stdout);
    for (const auto &i : files_cannot_compute_digest) {
      fprintf(stdout, "  - %s\n", i.c_str());
    }
  }

  return 0;
}
