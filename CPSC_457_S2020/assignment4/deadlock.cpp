/*****************************************************
 * Last Name:   Shen
 * First Name:  Haohu
 * Student ID:  30063099
 * Course:      CPSC 457
 * Tutorial:    T01
 * Assignment:  4
 * Question:    1
 * Date:        2020.06.15
 *
 * File name: deadlock.cpp
 * Compile by: g++ -Wall -O2 deadlock.cpp -o deadlock
 * Compiled using SSH on linux.cpsc.ucalgary.ca
 ****************************************************/

// Include header files here
#include <algorithm>
#include <bitset>
#include <chrono>
#include <cstring>
#include <iostream>
#include <numeric>
#include <stack>
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <vector>

// Define all constants here
const std::string LEFT_ARROW = "<-";
const std::string RIGHT_ARROW = "->";
constexpr int NUMBER_OF_ARGUMENTS = 1;
constexpr int MAXIMAL_NUMBER_OF_EDGES = 10000;
constexpr int MAX_NODES = 20005; // Since the number of edges is no more than
                                 // 10000, the number of nodes won't be more
                                 // than 20000, we give it 5 offset just in case

// A class that overwrites std::hash<uint64_t> which improves the performance of
// a function and safeguards the hash map from collision attacks in a way
// by non-deterministic hashing via high-precision clock
// Reference: https://codeforces.com/blog/entry/62393
struct Uint64Hash {
  static uint64_t splitmix64(uint64_t x) {
    // http://xorshift.di.unimi.it/splitmix64.c
    x += 0x9e3779b97f4a7c15;
    x = (x ^ (x >> 30)) * 0xbf58476d1ce4e5b9;
    x = (x ^ (x >> 27)) * 0x94d049bb133111eb;
    return x ^ (x >> 31);
  }
  // The hash function of a uint64_t
  size_t operator()(uint64_t x) const {
    static const uint64_t FIXED_RANDOM =
        std::chrono::steady_clock::now().time_since_epoch().count();
    return splitmix64(x + FIXED_RANDOM);
  }
};

// Define the adjacency list of the graph as a global variable
std::unordered_map<int, std::unordered_set<int, Uint64Hash>, Uint64Hash> G;

// A collection of containers and procedures that implements Tarjan's strongly
// connected components algorithm. Assume that the node id starts from 1 and the
// index of a strongly connected component (SCC) also starts from 1 Reference:
// https://en.wikipedia.org/wiki/Tarjan%27s_strongly_connected_components_algorithm#The_algorithm_in_pseudocode
namespace StronglyConnectedComponent {

int number_of_nodes;   // number of nodes in the graph
int number_of_scc;     // number of strongly connected components in the graph
int current_timestamp; // current timestamp
std::stack<int> s;     // A stack is used to store all nodes that may form a
                       // strongly connected component
std::vector<bool>
    visited; // visited.at(id) flags if the node id is in the stack
std::vector<int> dfs_rank; // dfs_rank.at(id) numbers the nodes consecutively
                           // in the order in which they are discovered by DFS
std::vector<int> low_link; // low_link.at(id) represents the smallest node id
                           // of any node known to be reachable from id through
                           // id's DFS subtree, including id itself
std::vector<int> scc;      // scc.at(id) is the index of the strongly connected
                           // component that the node id belongs to
std::vector<int> size_of_scc; // size_of_scc.at(id) is the size_of_tree of the
                              // strongly connected component whose index is id

// Initialize all global variables in the namespace with n nodes given
inline void init(int n) {
  number_of_nodes = n;
  number_of_scc = 0;
  current_timestamp = 0;
  // Give some flexibility of size_of_tree of our containers since
  // the node id/SCC id may not strictly start from 1, since the
  // number of nodes in the assignment will not exceed 10001,
  // the space complexity will not be a bottle-neck
  int offset = 5;
  visited.resize(number_of_nodes + offset, false);
  dfs_rank.resize(number_of_nodes + offset, 0);
  low_link.resize(number_of_nodes + offset, 0);
  scc.resize(number_of_nodes + offset, 0);
  size_of_scc.resize(number_of_nodes + offset, 0);
}

// Implementation of the main logic of Tarjan's algorithm
inline void Tarjan(int u) { // u: the node id being processed
  dfs_rank.at(u) = current_timestamp;
  low_link.at(u) = current_timestamp;
  ++current_timestamp; // update the current timestamp
  s.push(u);
  visited.at(u) = true;
  for (const auto &v : G[u]) {
    if (!dfs_rank[v]) {
      Tarjan(v);
      low_link.at(u) = std::min(low_link.at(u), low_link.at(v));
    } else if (visited.at(v)) {
      low_link.at(u) = std::min(low_link.at(u), dfs_rank.at(v));
    }
  }
  if (low_link.at(u) == dfs_rank.at(u)) {
    ++number_of_scc;
    while (s.top() != u) {
      int top_id = s.top();
      // Paint top_id
      s.pop();
      scc.at(top_id) = number_of_scc;
      ++size_of_scc.at(number_of_scc);
      visited.at(top_id) = false;
    }
    // Paint u
    s.pop();
    scc.at(u) = number_of_scc;
    ++size_of_scc.at(number_of_scc);
    visited.at(u) = false;
  }
}
} // namespace StronglyConnectedComponent

// A collection of containers and procedures that implements a Disjoint-set
// data structure optimized with path compression and union by size_of_tree.
// Reference: https://cp-algorithms.com/data_structures/disjoint_set_union.html
namespace DisjointSet {

// father[x]: father[x] is the father node of x
// size_of_tree[x]:  size_of_tree[x] is the size_of_tree of the tree whose root
// is x
std::vector<int> father;
std::vector<int> size_of_tree;

// Initialize the the father array and the size_of_tree array
inline void init() {
  father.resize(MAX_NODES);
  std::iota(father.begin(), father.end(), 0);
  size_of_tree.resize(MAX_NODES, 1);
}

// Find and return the ancestor of x
inline int find(int x) {
  if (x != father[x]) {
    // Path compression
    father[x] = find(father[x]);
  }
  return father[x];
}

// Check if x and y have the same ancestor, merge x and y if it is not the case
inline void merge(int x, int y) {
  x = find(x);
  y = find(y);
  // Case 1: x and y have the same ancestor
  if (x == y) {
    return;
  }
  // Case 2: x and y do not have the same ancestor
  if (size_of_tree[x] >
      size_of_tree[y]) { // Union by size_of_tree, make sure the tree with less
                         // nodes combines to the tree with more nodes
    std::swap(x, y);
  }
  father[x] = y;
  size_of_tree[y] += size_of_tree[x];
}

// Check if x and y have the same ancestor, return true if it is the case
inline bool is_same_group(int i, int j) { return find(i) == find(j); }
} // namespace DisjointSet

// A collection of containers and procedures that implements the test of
// reachability of two nodes in a directed graph using depth-first search
// without backtrace
namespace ReachabilityTest {

std::bitset<MAX_NODES> visited; // mark if a node is visited during DFS
int source_node_id;             // store the node id where the DFS starts
int destination_node_id;        // store the node id where the DFS stops

// Implementation of the main logic of DFS that starts from 'id'
inline void dfs(int id) {
  visited[id] = true;
  // Case 1: Reach the destination, stop DFS
  if (id == destination_node_id) {
    return;
  }
  // Case 2: Current node is not the destination, DFS all its connected edges
  for (const auto &v_id : G[id]) {
    // Skip all nodes that are visited
    if (!visited[v_id]) {
      dfs(v_id);
    }
  }
}

// Check if there is a directed path from v to u
inline bool is_reachable(int v, int u) {
  // Initialize all global variables in this namespace
  visited.reset();
  source_node_id = v;
  destination_node_id = u;
  // Initialize DFS
  dfs(source_node_id);
  // Return if node u is visited
  return visited[destination_node_id];
}
} // namespace ReachabilityTest

int main(int argc, char *argv[]) {

  // For faster input/output, we use std::cin/std::cout and
  // disable the synchronization between standard C++ streams and standard
  std::ios_base::sync_with_stdio(false);
  std::cin.tie(nullptr);

  // Report and quit if the number of arguments is invalid
  if (argc != NUMBER_OF_ARGUMENTS) {
    std::cout << "The program should not take any arguments!" << '\n';
    exit(-1);
  }

  // Initialize a variable that counts the node id from 1
  int node_id_counter = 1;
  // Initialize a hash-map that maps each process to a unique node id
  std::unordered_map<std::string, int> process_to_node_id;
  // Initialize a hash-map that maps each request to a unique node id
  std::unordered_map<std::string, int> request_to_node_id;
  // Initialize a hash-map that maps each node id to its corresponding process
  std::unordered_map<int, std::string, Uint64Hash> node_id_to_process;
  // Initialize a hash-map that maps each node id to its corresponding request
  std::unordered_map<int, std::string, Uint64Hash> node_id_to_request;
  // Initialize a variable to read the name of a process
  std::string process;
  // Initialize a variable to read the arrow of an edge
  std::string directed_edge;
  // Initialize a variable to read the name of a process
  std::string request;
  // Initialize two temporary variables to store two node ids
  int u;
  int v;
  // Initialize a container to store all deadlocked processes
  std::vector<std::string> deadlocked_processes;
  // Initialize the Disjoint Set
  DisjointSet::init();

  // Initialize a variable counts how many edges are being read so far
  int number_of_edges = 0;

  // Read the edges and nodes from stdin until EOF
  while (std::cin >> process >> directed_edge >> request) {
    // Add the process node if it does not exist in the graph
    if (process_to_node_id.find(process) == process_to_node_id.end()) {
      process_to_node_id[process] = node_id_counter;
      node_id_to_process[node_id_counter] = process;
      ++node_id_counter;
    }
    // Add the request node if it does not exist in the graph
    if (request_to_node_id.find(request) == request_to_node_id.end()) {
      request_to_node_id[request] = node_id_counter;
      node_id_to_request[node_id_counter] = request;
      ++node_id_counter;
    }
    // Add the new edge from u to v to the adjacency list
    // process(v) <- request(u)
    if (directed_edge == LEFT_ARROW) {
      u = request_to_node_id[request];
      v = process_to_node_id[process];
    }
    // process(u) -> request(v)
    else if (directed_edge == RIGHT_ARROW) {
      v = request_to_node_id[request];
      u = process_to_node_id[process];
    }
    // Invalid arrow sign, report and quit
    else {
      std::cout << "The directed edge is invalid!\n"
                   "It must be <- or ->!"
                << '\n';
      exit(-1);
    }
    // Update the number of edges
    ++number_of_edges;
    // Report and quit if the number of edges being read exceeds 10000,
    // because the case is almost impossible according to the constraints
    // given in the assignment, we give the compiler this branch prediction
    // information for better performance
    if (__builtin_expect(number_of_edges > MAXIMAL_NUMBER_OF_EDGES, 0)) {
      std::cout << "Too many edges given!\n"
                   "You must give no more than 10000 edges!"
                << '\n';
      exit(-1);
    }

    // Skip if the edge (u, v) already exists in the graph
    if (G.find(u) != G.end() && G[u].find(v) != G[u].end()) {
      continue;
    }

    // Before insertion, we check if there is a directed path from v to u,
    // if it is true then when the edge (u, v) is added, there must be a
    // cycle and we should check the cycle and break the loop. Otherwise,
    // add the edge (u, v) to the graph and update the disjoint set

    // Case 1: u and v are not in the same component, thus there is no
    // directed path from v to u of course
    if (!DisjointSet::is_same_group(u, v)) {
      G[u].insert(v);
      // Update the disjoint set since u and v are in the same component now
      DisjointSet::merge(u, v);
    }
    // Case 2: u and v are in the same component but there is no directed
    // path from v to u
    else if (!ReachabilityTest::is_reachable(v, u)) {
      G[u].insert(v);
    }
    // Case 3: there is a directed path from v to u, thus a cycle will exist
    // after we add the edge (u, v) and u and v are of course nodes in the
    // cycle, also the edge (u, v) is where the deadlock occurs
    else {
      G[u].insert(v);
      // Obtain all nodes in this component by checking the disjoint set
      std::unordered_set<int, Uint64Hash> nodes;
      int father_node_id = DisjointSet::find(u);
      for (int i = 1; i < node_id_counter; ++i) {
        if (DisjointSet::find(i) == father_node_id) {
          nodes.insert(i);
        }
      }
      // Run Tarjan's SCC algorithm to obtain the SCC in this component since
      // the cycle itself must be in the SCC
      StronglyConnectedComponent::init(node_id_counter);
      for (const auto &i : nodes) {
        // Process node i if the node i has not been visited in the SCC
        // algorithm
        if (!StronglyConnectedComponent::dfs_rank.at(i)) {
          StronglyConnectedComponent::Tarjan(i);
        }
      }
      // Obtain the id of the SCC that the node 'u' belongs to
      int scc_id = StronglyConnectedComponent::scc.at(u);
      // Check all nodes in this SCC and add to the process list if it is a
      // process
      for (const auto &i : nodes) {
        if (StronglyConnectedComponent::scc.at(i) == scc_id &&
            node_id_to_process.find(i) != node_id_to_process.end()) {
          deadlocked_processes.emplace_back(node_id_to_process[i]);
        }
      }
      // Since a deadlock is detected, we can stop adding new edges
      break;
    }
  }
  // Output
  if (deadlocked_processes.empty()) {
    std::cout << "No deadlock." << '\n';
  } else {
    // Output the edge (u, v) since it is where the deadlock occurs
    std::cout << "Deadlock on edge: ";
    // u -> v
    if (node_id_to_process.find(u) != node_id_to_process.end()) {
      std::cout << node_id_to_process[u] << " -> " << node_id_to_request[v]
                << '\n';
    }
    // v <- u
    else {
      std::cout << node_id_to_process[v] << " <- " << node_id_to_request[u]
                << '\n';
    }
    // Sort and output all deadlock processes
    std::sort(deadlocked_processes.begin(), deadlocked_processes.end());
    std::cout << "Deadlocked processes: " << deadlocked_processes.front();
    for (size_t i = 1; i != deadlocked_processes.size(); ++i) {
      std::cout << ", " << deadlocked_processes.at(i);
    }
    std::cout << '\n';
  }

  return 0;
}
