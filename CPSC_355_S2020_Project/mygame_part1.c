// Computing Machinery I Project Part 1
// (all features are implemented except extra graphical interfaces)
// To compile: gcc -O2 -Wall mygame_part1.c -o mygame
// To run: ./mygame [player's name] [size of the board]

// Include all header files here!
#include <assert.h>
#include <ctype.h>
#include <fcntl.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>

// Define all constants here!
#define MINIMAL_N 5
#define MAXIMAL_N 20
#define SIZE_OF_MATRIX 25
#define MAXIMAL_LENGTH_OF_NAME 30
#define VISITED 1
#define OFFSET 2
#define UNVISITED 0
#define MAX_HISTORY_ITEMS 100
#define BUFFER_SIZE 30000
#define EPS 0.001
// Define the name to store the history of the game
const char history_file_path[] = "game_history.log";

// Define a structure to store an item of a player's history
struct Player {
  char name[MAXIMAL_LENGTH_OF_NAME + OFFSET]; // including '\0'
  long game_dimension;
  long duration;
  double score;
};

// Return 1 if x is positive, return 0 if x is 0, return -1 if x is negative
int sgn(double x) {
  if (x > EPS) {
    return 1;
  }
  if (x > -EPS) {
    return 0;
  }
  return -1;
}

// Define a comparator for qsort()
// Score bigger then ranks better, if ties, duration longer then ranks better,
// if ties, game dimension bigger ranks better, if ties, name alphabetically
// smaller than ranks better
int comparator(const void *left, const void *right) {
  struct Player *player_left = (struct Player *)left;
  struct Player *player_right = (struct Player *)right;
  // Compare score
  int temp = sgn(player_left->score - player_right->score);
  if (temp > 0) {
    return -1;
  } else if (temp < 0) {
    return 1;
  }
  // Compare duration
  if (player_left->duration > player_right->duration) {
    return -1;
  } else if (player_left->duration < player_right->duration) {
    return 1;
  }
  // Compare game dimension
  if (player_left->game_dimension > player_right->game_dimension) {
    return -1;
  } else if (player_left->game_dimension < player_right->game_dimension) {
    return 1;
  }
  // Compare name
  return strcmp(player_left->name, player_right->name);
}

// Define all global variables here!
char game_board[SIZE_OF_MATRIX][SIZE_OF_MATRIX];
double score_board[SIZE_OF_MATRIX][SIZE_OF_MATRIX];
char surprise_packs[SIZE_OF_MATRIX][SIZE_OF_MATRIX];
int vis[SIZE_OF_MATRIX][SIZE_OF_MATRIX]; // 0: unvisited 1: visited
char input_buffer[BUFFER_SIZE];
char output_buffer[BUFFER_SIZE];
struct Player
    player_history[MAX_HISTORY_ITEMS + 1]; // 1 offset for the new record

int unvisited_cell_counter;
int N;
char *player_name;
double score;

time_t start_time;
time_t last_time;
time_t current_time;
int time_elapsed;
int remaining_time;
int length_of_history = 0;
char *buffer_pointer;

// Generate a random number between lower_bound and upper_bound, the generated
// number is negative if is_negative is true
double randomNum(int lower_bound, int upper_bound, bool is_negative) {
  int integer_part;
  double float_part;
  double result;
  if (is_negative == 0) {
    integer_part = rand() & (upper_bound - 1);
    float_part = (rand() & (100 - 1)) * 0.01;
    result = integer_part + float_part;
    return result;
  }
  integer_part = rand() & (-lower_bound - 1);
  float_part = (rand() & (100 - 1)) * 0.01;
  result = -integer_part - float_part;
  return result;
}

// Generate a number in [lower_bound, upper_bound]
int generateRandomInt(int lower_bound, int upper_bound) {
  assert(lower_bound <= upper_bound);
  int range = upper_bound - lower_bound + 1;
  return rand() % range + lower_bound;
}

// Show the game board and some other conditions
void displayGame(char board[][SIZE_OF_MATRIX]) {
  // Show the game board
  for (int i = 0; i < N; ++i) {
    for (int j = 0; j < N; ++j) {
      printf("\t%c", board[i][j]);
    }
    printf("\n");
  }
  // Show the score
  if (sgn(score) == 0) {
    printf("Score: 0\n");
  } else {
    printf("Score: %.2f\n", score);
  }
  // Show the current time
  printf("Time: %d\n", remaining_time);
}

// Initialize the game board with 'X'
void initializeGameBoard(char board[][SIZE_OF_MATRIX]) {
  /** Initialize the game board **/
  for (int i = 0; i < N; ++i) {
    for (int j = 0; j < N; ++j) {
      board[i][j] = 'X';
    }
  }
}

// Initialize the vis array
void initializeIsVisited() {
  /** Initialize vis **/
  for (int i = 0; i < N; ++i) {
    for (int j = 0; j < N; ++j) {
      vis[i][j] = UNVISITED;
    }
  }
}

// Initialize the score board
void initializeScoreboard() {
  /** Initialize the score board **/
  // Initialize N^2 random number between (-15.0, 15.0) where no more
  // than 0.2 of N^2 are negative numbers
  // Initialize N^2 random number between (-15.0, 15.0) where no more
  // than 0.2 of N^2 are negative numbers
  int number_of_negative_numbers = generateRandomInt(0, N * N / 5);
  int counter_of_negative_numbers = 0;
  int temp;
  for (int i = 0; i < N; ++i) {
    for (int j = 0; j < N; ++j) {
      temp = generateRandomInt(0, 1);
      // -
      if (temp == 0 &&
          counter_of_negative_numbers < number_of_negative_numbers) {
        score_board[i][j] = randomNum(-15, 0, true);
        ++counter_of_negative_numbers;
      }
      // +
      else {
        score_board[i][j] = randomNum(0, 15, false);
      }
    }
  }
}

// Initialize the traps in the board
void initializeTheTraps() {
  /** Initialize the + - traps **/
  for (int j = 0; j < N; ++j) {
    for (int k = 0; k < N; ++k) {
      if (sgn(score_board[j][k]) < 0) {
        surprise_packs[j][k] = '-';
      } else {
        surprise_packs[j][k] = '+';
      }
    }
  }
}

// Initialize the surprise packs
void initializeSurprisePacks() {
  /** Randomly overwrite 10% surprise packs **/
  int number_of_surprise_packs = N * N / 10;
  int surprise_pack_type;
  int col;
  int row;
  for (int j = 0; j < number_of_surprise_packs; ++j) {
    surprise_pack_type = generateRandomInt(0, 2);
    col = generateRandomInt(0, N - 1);
    row = generateRandomInt(0, N - 1);
    if (surprise_pack_type == 0) {
      surprise_packs[row][col] = '$';
    } else if (surprise_pack_type == 1) {
      surprise_packs[row][col] = '!';
    } else {
      surprise_packs[row][col] = '@';
    }
  }
}

// Initialize the whole game
void initializeGame(char board[][SIZE_OF_MATRIX]) {
  initializeGameBoard(board);
  initializeIsVisited();
  initializeScoreboard();
  /** Initialize the score **/
  score = 0;
  /** Initialize unvisitedCellsCounter **/
  unvisited_cell_counter = N * N;
  /** Initialize the remaining time **/
  remaining_time =
      60 +
      (N - 5) * 15; // N increases 1, the total time given increases 15 seconds
  initializeTheTraps();
  initializeSurprisePacks();
}

// Return the length of the string passed
int myStrlen(const char *s) {
  int length = 0;
  for (int i = 0; s[i] != 0; ++i) {
    ++length;
  }
  return length;
}

// Check if the character is a digit
int myIsdigit(char ch) {
  if (ch >= '0' && ch <= '9') {
    return 1;
  }
  return 0;
}

// Check if the string passed is a number
int isANumber(char *s) {
  // Check if any character in the string is a digit
  for (int i = 0; s[i] != 0; ++i) {
    if (myIsdigit(s[i]) == 0) {
      return 0;
    }
  }
  // Check if there is a leading zero
  if (myStrlen(s) > 1 && s[0] == '0') {
    return 0;
  }
  return 1;
}

// Read and parse the history log from the external device
void readLog() {

  // Open current log file, create it if not exists
  int fd = openat(AT_FDCWD, history_file_path, O_CREAT | O_RDONLY,
                  S_IRUSR | S_IWUSR);
  // Quit if it fails
  if (fd == -1) {
    printf("Failed on opening the log file! Program abort!\n");
    exit(0);
  }
  long nRead = read(fd, input_buffer, BUFFER_SIZE);
  buffer_pointer = input_buffer;
  if (nRead == -1) {
    printf("Failed on reading the log file! Program abort!\n");
    exit(0);
  }
  // Close fd
  int status = close(fd);
  if (status != 0) {
    printf("Failed on closing the log file! Program abort!\n");
  }

  if (nRead > 0) {
    int offset;
    length_of_history = 0;
    // name, game_dimension, duration, score
    while (1) {
      // Only read 100 items
      if (length_of_history >= MAX_HISTORY_ITEMS) {
        break;
      }
      int temp = sscanf(buffer_pointer, "%s %ld %ld %lf%n",
                        player_history[length_of_history].name,
                        &player_history[length_of_history].game_dimension,
                        &player_history[length_of_history].duration,
                        &player_history[length_of_history].score, &offset);
      if (temp == 4) { // currently read
        ++length_of_history;
        buffer_pointer += offset;
      } else if (temp == EOF) {
        break;
      } else {
        printf("Failed on parsing the history log! Log file broken! Program "
               "abort!\n");
        exit(0);
      }
    }
  }
}

// Display the top n scores
void displayTopScores(int n) {
  // read files
  readLog();
  // adjust n if n is so larger
  if (n > length_of_history) {
    printf("There are only %d items in the history!\n", length_of_history);
    n = length_of_history;
  }
  // sort
  qsort(player_history, length_of_history, sizeof(struct Player), comparator);
  // output
  printf("Top %d Scores:\n", n);
  for (int i = 0; i < n; ++i) {
    printf("Name: %32s Dimension: %5ld Duration: %5ld Score: %.2lf\n",
           player_history[i].name, player_history[i].game_dimension,
           player_history[i].duration, player_history[i].score);
  }
}

void checkTopScores() {
  int n;
  while (1) {
    printf("Enter the N to check top N scores: ");
    scanf("%d", &n);
    if (n <= 0 || n > MAX_HISTORY_ITEMS) {
      printf("N must be at least 1 but not larger than 100!\n");
    } else {
      displayTopScores(n);
      break;
    }
  }
}

void writeToLog() {
  // sort the history
  qsort(player_history, length_of_history, sizeof(struct Player), comparator);
  // write to buffer
  if (length_of_history > MAX_HISTORY_ITEMS) {
    length_of_history = MAX_HISTORY_ITEMS; // ignore the redundant one
  }
  buffer_pointer = output_buffer;
  // name, game_dimension, duration, score
  for (int i = 0; i < length_of_history; ++i) {
    buffer_pointer +=
        sprintf(buffer_pointer, "%s %ld %ld %.2lf", player_history[i].name,
                player_history[i].game_dimension, player_history[i].duration,
                player_history[i].score);
    *buffer_pointer = ' ';
    ++buffer_pointer;
  }
  *buffer_pointer = '\0';
  // Open current log file, create it if not exists
  int fd = openat(AT_FDCWD, history_file_path, O_CREAT | O_RDWR | O_TRUNC,
                  S_IRUSR | S_IWUSR);
  // Quit if it fails
  if (fd == -1) {
    printf("Failed on opening the log file! Program abort!\n");
    exit(0);
  }
  // write to file
  write(fd, output_buffer, BUFFER_SIZE);
  // Close fd
  int status = close(fd);
  if (status != 0) {
    printf("Failed on closing the log file! Program abort!\n");
  }
}

// Name: %s Size: %d Duration: %d Score: %.2lf
void logScore() {
  time(&current_time);
  int duration = (int)difftime(current_time, start_time);
  // read the log
  readLog();
  // load to log
  for (int i = 0; player_name[i] != 0; ++i) {
    player_history[length_of_history].name[i] = player_name[i];
  }
  player_history[length_of_history].score = score;
  player_history[length_of_history].game_dimension = N;
  player_history[length_of_history].duration = duration;
  ++length_of_history;
  // write to log
  writeToLog();
  printf("Your score has been stored to the history!\n");
}

void exitGame() { printf("Have a nice day! %s\n", player_name); }

void calculateScore(int i, int j) {
  // Check the score/surprise pack and update the score and print the message
  // and update the game board
  if (surprise_packs[i][j] == '-') {
    score += score_board[i][j];
    game_board[i][j] = '-';
    printf("Bang!! You lost %.2lf points\n", score_board[i][j]);
  } else if (surprise_packs[i][j] == '+') {
    score += score_board[i][j];
    game_board[i][j] = '+';
    printf("Uncovered a reward of %.2lf points\n", score_board[i][j]);
  } else if (surprise_packs[i][j] == '@') {
    remaining_time += 30;
    game_board[i][j] = '@';
    printf("Congratulations! You got 30 extra seconds!\n");
  } else if (surprise_packs[i][j] == '!') {
    score = (((int)(score * 100)) >> 1) * 0.01;
    game_board[i][j] = '!';
    printf("Bang!! You lost half of the score\n");
  } else if (surprise_packs[i][j] == '$') {
    score = (((int)(score * 100)) << 1) * 0.01;
    game_board[i][j] = '$';
    printf("Congratulations! You got the score doubled!\n");
  }
}

int main(int argc, char *argv[]) {

  // Check arguments
  if (argc != 3) {
    printf("Invalid number of arguments!\n");
    return 0;
  }
  if (myStrlen(argv[1]) > MAXIMAL_LENGTH_OF_NAME) {
    printf("Player name given is TOO long!\n");
    return 0;
  }
  if (isANumber(argv[2]) == 0) {
    printf("Invalid number of size!\n");
    return 0;
  }
  // Obtain and check the dimension of the game board
  N = atoi(argv[2]);
  if (N < MINIMAL_N || N > MAXIMAL_N) {
    printf("Invalid size of the game board, the size of board should be "
           "between 5 and 20!\n");
    return 0;
  }

  // Obtain the player's name
  player_name = argv[1];
  // Show the game menu
  printf("Hello! %s\nWelcome to the game!\n\n", player_name);

  // Menu before the game
  int select;
  while (1) {
    printf("Game Menu:\n1. Start the game\n2. Check the top scores\n3. "
           "Exit\nSelect: ");
    scanf("%d", &select);
    if (select == 1) {
      break;
    } else if (select == 2) {
      checkTopScores();
    } else if (select == 3) {
      exitGame();
      return 0;
    } else {
      printf("Invalid option! Please select again!\n");
    }
  }

  int i, j;

  // Initialize the seed
  srand(time(NULL));

  // Initialize the game
  initializeGame(game_board); // Pass by pointer

  // Display the game
  displayGame(game_board); // Pass by pointer

  while (1) {

    // Game end 1: the score becomes <= 0 after the initial move
    if (sgn(score) <= 0 && unvisited_cell_counter < N * N - 1) {
      printf("\nSorry! You score is less than or equal to 0!\n"
             "You final score is: %.2lf\n",
             score);
      logScore();
      printf("Returning to the game menu...");
      break;
    }

    // Game end 2: all cells covered
    if (unvisited_cell_counter == 0) {
      printf("\nCongratulations! All cells cleared!\n"
             "You final score is: %.2lf\n",
             score);
      logScore();
      printf("Returning to the game menu...");
      break;
    }

    // Ask the player for the next move
    printf("Enter your move (x, y), or enter (-1, -1) to quit the game: ");
    scanf("%d %d", &i, &j);
    if (i == -1 && j == -1) {
      exitGame();
      return 0;
    }
    // Check if the position is valid
    if (i < 0 || i >= N || j < 0 || j >= N) {
      printf("Invalid move! Please enter again!\n");
      continue;
    }
    // Check if the position is visited
    if (vis[i][j] == VISITED) {
      printf("The position is visited, please enter again!\n");
      continue;
    }
    // Check and update the remaining time here, if no remaining time, report,
    // confirm and return to the main menu Case 1: if the player just made the
    // first move, start counting the time
    if (unvisited_cell_counter == N * N) {
      time(&start_time);
      last_time = start_time;
    }
    // Case 2: update the time cost after the first move
    else {
      time(&current_time);
      time_elapsed = (int)difftime(current_time, last_time);
      last_time = current_time;
      remaining_time -= time_elapsed;
    }
    // Game end 3: Out of time
    if (remaining_time <= 0) {
      printf("\nSorry! You are out of time!\n"
             "You final score is: %.2lf\n",
             score);
      logScore();
      printf("Returning to the game menu...");
      break;
    }
    // Calculate the score
    calculateScore(i, j);
    // update the vis array
    vis[i][j] = VISITED;
    --unvisited_cell_counter;
    // Show the game board
    displayGame(game_board);
  }

  // Menu after the game
  while (true) {
    printf("Game Menu:\n1. Check the top scores\n2. Exit\nSelect: ");
    scanf("%d", &select);
    if (select == 1) {
      checkTopScores();
    } else if (select == 2) {
      exitGame();
      break;
    } else {
      printf("Invalid option! Please select again!\n");
    }
  }
  return 0;
}