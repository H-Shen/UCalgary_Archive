/**
 * 1. The project has been tested in UCalgary's arm linux server using Secure Shell.
 * 2. The time bonus and the top score sorting are all implemented.
 * 3. To compile and run, please run these commands in a Secure Shell:
 *    m4 mygame.asm > mygame.s
 *    gcc -Wall -O2 mygame.s -o mygame
 *    ./mygame [player name] [the dimension of the game board]
 *
 * Project: mygame(Part 2)
 */

/* Notice: The structure 'Player' is defined as below, and player_history stores MAX_HISTORY_ITEMS
'Player' instances

struct Player {
    char name[MAXIMAL_LENGTH_OF_NAME + 2];  // add 2 to include '\0'
    long game_dimension;
    long duration;
    double score;
};
*/

// Define all constants here
define(EOF, -1)                         // Define a constant which indicates the value of end-of-file
define(MINIMAL_N, 5)                    // Define a constant which indicates the minimal value of dimension
define(MAXIMAL_N, 20)                   // Define a constant which indicates the maximal value of dimension
define(SIZE_OF_MATRIX, 25)              // Define a constant which indicates the preserved value of dimension
define(MAXIMAL_LENGTH_OF_NAME, 30)      // Define a constant which indicates the maximal length of the name
define(VISITED, 1)                      // Define a constant which indicates the cell is visited
define(UNVISITED, 0)                    // Define a constant which indicates the cell is not visited yet
define(MAX_HISTORY_ITEMS, 100)          // Define a constant which indicates the maximal number of player history items
define(DATA_MEMBER_OF_STRUCT, 4)        // Define a constant which indicates the number of data members of a 'Player' instance
define(SIZEOF_STRUCT_PLAYER, 56)        // Define a constant which indicates the size of a 'Player' instance after alignment
define(NULL_TERMINATOR, 0)              // Define a constant which indicates the zero null terminator
define(BUFFER_SIZE, 30000)              // Define a constant which indicates size of buffer for read/write
define(RAND_MAX, 2147483647)            // Define a constant which indicates the largest number when call rand()
define(AT_FDCWD, -100)                  // Define a constant which indicates the value of AT_FDCWD in the armv8 linux machine
define(O_FLAG, 64)                      // Define a constant which indicates the value of O_CREAT | O_RDONLY in the armv8 linux machine
define(O_FLAG_2, 578)                   // Define a constant which indicates the value of O_CREAT | O_RDWR | O_TRUNC in the armv8 linux machine
define(S_FLAG, 384)                     // Define a constant which indicates the value of S_IRUSR | S_IWUSR in the armv8 linux machine

// Value of register x8 mapping to the service request
define(OPENAT, 56)      // system call: openat
define(CLOSE, 57)       // system call: close
define(READ, 63)        // system call: read
define(WRITE, 64)       // system call: write

// Define all macro preprocessors
fp      .req    x29     // Frame Pointer
lr      .req    x30     // Link Register

        .bss   // The bss section starts here

game_board:     .skip SIZE_OF_MATRIX * SIZE_OF_MATRIX * 1       // Preserved space in bss for char game_board[SIZE_OF_MATRIX][SIZE_OF_MATRIX]
score_board:    .skip SIZE_OF_MATRIX * SIZE_OF_MATRIX * 8       // Preserved space in bss for double score_board[SIZE_OF_MATRIX][SIZE_OF_MATRIX]
surprise_packs: .skip SIZE_OF_MATRIX * SIZE_OF_MATRIX * 1       // Preserved space in bss for char surprise_packs[SIZE_OF_MATRIX][SIZE_OF_MATRIX]
is_visited:     .skip SIZE_OF_MATRIX * SIZE_OF_MATRIX * 4       // Preserved space in bss for int is_visited[SIZE_OF_MATRIX][SIZE_OF_MATRIX]
input_buffer:   .skip BUFFER_SIZE * 1                           // Preserved space in bss for char input_buffer[BUFFER_SIZE]
output_buffer:  .skip BUFFER_SIZE * 1                           // Preserved space in bss for char output_buffer[BUFFER_SIZE]
player_history: .skip MAX_HISTORY_ITEMS * 56                    // Preserved space in bss for struct Player player_history[MAX_HISTORY_ITEMS]

        .data   // The data section starts here

unvisited_cell_counter: .word 0         // Initialize the global variable int unvisited_cell_counter = 0
N:              .dword  0               // Initialize the global variable long N = 0
player_name:    .dword  0               // Initialize the global variable char* player_name = NULL
score:          .double 0r0.0           // Initialize the global variable double score = 0.0
start_time:     .dword  0               // Initialize the global variable time_t start_time = 0
last_time:      .dword  0               // Initialize the global variable time_t last_time = 0
current_time:   .dword  0               // Initialize the global variable time_t current_time = 0
time_elapsed:   .word   0               // Initialize the global variable int time_elapsed = 0
remaining_time: .word   0               // Initialize the global variable int remaining_time = 0
length_of_history:      .word 0         // Initialize the global variable int length_of_history = 0
buffer_pointer:  .dword  0              // Initialize the global variable char* buffer_pointer = NULL
eps:             .double 0r0.001        // Initialize the global variable double eps = 0.001
one_over_a_hundred: .double 0r0.01      // Initialize the global variable double one_over_a_hundred = 0.01
negative_eps:    .double 0r-0.001       // Initialize the global variable double negative_eps = -0.001
one_hundred_in_double: .double 0r100.0  // Initialize the global variable double one_hundred_in_double = 100.0

        .text   // The text section starts here

history_file_path:      .asciz "game_history.log"                                                       // Initialize a string represents the file path of the game history
tab_and_character: .asciz "\t%c"                                                                        // Initialize a string represents the tab and character placeholder
new_line:               .asciz "\n"                                                                     // Initialize a string represents the new line character
score_zero:             .asciz "Score: 0\n"                                                             // Initialize a string represents the score is 0
score_double:           .asciz "Score: %.2f\n"                                                          // Initialize a string represents the actual score
current_time_info:      .asciz "Time: %d\n"                                                             // Initialize a string represents the current time
failed_write_log_file:  .asciz "Failed on writing the log file! Program abort!\n"                       // Initialize a string represents the log file cannot be written
failed_open_log_file:   .asciz "Failed on opening the log file! Program abort!\n"                       // Initialize a string represents the log file cannot be opened
failed_close_log_file:  .asciz "Failed on closing the log file! Program abort!\n"                       // Initialize a string represents the log file cannot be closed
failed_read_log_file:   .asciz "Failed on reading the log file! Program abort!\n"                       // Initialize a string represents the log file cannot be read
failed_parse_log_file:  .asciz "Failed on parsing the history log! Log file broken! Program abort!\n"   // Initialize a string represents the log file cannot be parsed
sscanf_format:          .asciz "%s %ld %ld %lf%n"                                                       // Initialize a string represents the format for sscanf()
only_n_items_in_history:.asciz "There are only %d items in the history!\n"                              // Initialize a string represents the number of items in the history
top_n_scores_title:     .asciz "Top %d Scores:\n"                                                       // Initialize a string represents the title of topscores
format_of_each_item:    .asciz "Name: %32s Dimension: %5ld Duration: %5ld Score: %.2lf\n"               // Initialize a string represents the format of each player's history item
ask_for_n:              .asciz "Enter the N to check top N scores: "                                    // Initialize a string represents the prompt that asks the player for N
scanf_one_number:       .asciz "%d"                                                                     // Initialize a string represents one number for scanf
n_is_out_of_range:      .asciz "N must be at least 1 but not larger than 100!\n"                        // Initialize a string represents n is out of range
sprintf_format:         .asciz "%s %ld %ld %.2lf"                                                       // Initialize a string represents the format for sprintf()
score_saved:            .asciz "Your score has been stored to the history!\n"                           // Initialize a string represents the score is saved to the history
have_a_nice_day:        .asciz "Have a nice day! %s\n"                                                  // Initialize a string represents a message to say goodbye to the player
lost_points:            .asciz "Bang!! You lost %.2lf points\n"                                         // Initialize a string represents a message that the player lost points
uncovered_reward:       .asciz "Uncovered a reward of %.2lf points\n"                                   // Initialize a string represents a message that the player earned points
got_extra_seconds:      .asciz "Congratulations! You got 30 extra seconds!\n"                           // Initialize a string represents a message that the player earned 30 seconds
lose_half_points:       .asciz "Bang!! You lost half of the score\n"                                    // Initialize a string represents a message that the player lost half of the points
got_score_doubled:      .asciz "Congratulations! You got the score doubled!\n"                          // Initialize a string represents a message that the player doubled the points
invalid_arguments:      .asciz "Invalid number of arguments!\n"                                         // Initialize a string represents a message that the number of argument given is invalid
name_too_long:          .asciz "Player name given is TOO long!\n"                                       // Initialize a string represents a message that the player's name is too long
size_of_board_invalid:  .asciz "Invalid number of size!\n"                                              // Initialize a string represents a message that the game dimension is not an integer
size_of_board_out_of_range: .asciz "Invalid size of the game board, the size of board should be between 5 and 20!\n"    // Initialize a string represents a message that the game dimension is out of range
welcome_message:        .asciz "Hello! %s\nWelcome to the game!\n\n"                                                    // Initialize a string represents a message that welcomes the player
menu_before_game:       .asciz "Game Menu:\n1. Start the game\n2. Check the top scores\n3. Exit\nSelect: "              // Initialize a string represents the game menu before the game
invalid_option:         .asciz "Invalid option! Please select again!\n"                                                 // Initialize a string represents the option is invalid
game_end_1:             .asciz "\nSorry! You score is less than or equal to 0!\nYou final score is: %.2lf\n"            // Initialize a string represents the score is <= 0
return_to_game_menu:    .asciz "Returning to the game menu...\n"                                                        // Initialize a string represents we are returning to the game menu
game_end_2:             .asciz "\nCongratulations! All cells cleared!\nYou final score is: %.2lf\n"                     // Initialize a string represents the map is cleared by the user
ask_for_a_move:         .asciz "Enter your move (x, y), or enter (-1, -1) to quit the game: "                           // Initialize a string represents the prompt that asks the player for the coordinate of next move
scanf_two_numbers:      .asciz "%d %d"                                                                                  // Initialize a string represents two numbers for scanf
invalid_move:           .asciz "Invalid move! Please enter again!\n"                                                    // Initialize a string represents the current coordinate is invalid
position_visited:       .asciz "The position is visited, please enter again!\n"                                         // Initialize a string represents the current coordinate is visited
game_end_4:             .asciz "\nSorry! You are out of time!\nYou final score is: %.2lf\n"                             // Initialize a string represents the player has run out of time
menu_after_game:        .asciz "Game Menu:\n1. Check the top scores\n2. Exit\nSelect: "                                 // Initialize a string represents the game menu after the game

        .balign 4       // Make sure that following instructions are aligned by 4 bytes
        .global main    // Make the "main" label be visible to the linker

// int signOfAFloat(double x)
// Description: if x > 0.001, return 1; if x > -0.001, return 0; otherwise return -1
signOfAFloat:   stp     fp, lr, [sp, -16]!      // Allocate 16 bytes on the stack, save the contents of frame pointer register and procedure link register in the reserved block
                mov     fp, sp                  // Set frame pointer register to the address of the stack pointer
                // load eps
                ldr     x9,=eps
                // load to d9
                ldr     d9, [x9]
                // compare d0 and d9
                fcmp d0, d9
                // branch to the label if the greater than relationship is hold
                b.gt    signOfAFloat_positive
                // load negative_eps
                ldr     x9,=negative_eps
                // load value in x9 to d9
                ldr     d9, [x9]
                // compare d0 and d9
                fcmp    d0, d9
                // branch to the label if the greater than relationship is hold
                b.gt    signOfAFloat_zero
                // store -1 to w0
                mov     w0, -1
                b signOfAFloat_end      // Jump to the branch
signOfAFloat_positive:
                // store 1 to w0
                mov     w0, 1
                b signOfAFloat_end      // Jump to the branch
signOfAFloat_zero:
                // store 0 to w0
                mov     w0, 0
                b signOfAFloat_end      // Jump to the branch
signOfAFloat_end:
                ldp     fp, lr, [sp], 16        // Restore the state of the frame pointer register and procedure link register by post-incrementing 16 on stack pointer
                ret     // Give the control back to the caller

// int comparator(const void *left, const void *right)
// If the player score is bigger then he ranks better, if ties, then if the duration is longer then ranks better,
// if the duration is the same, during the player with bigger N ranks better, if two players' N tied, then the player
// with smaller alphabetical order wins
comparator:
                stp     fp, lr, [sp, -64]!      // Allocate 64 bytes on the stack, save the contents of frame pointer register and procedure link register in the reserved block
                mov     fp, sp                  // Set frame pointer register to the address of the stack pointer
	        str	x0, [sp, 24]	// store the the address of 'left' to sp+24
	        str	x1, [sp, 16]	// store the the address of 'right' to sp+16
                // struct Player *player_left = (struct Player *) left
	        ldr	x0, [sp, 24]	// load the value in sp+24, which is the address of 'left'
	        str	x0, [sp, 56]	// store it in sp+56
                // struct Player *player_right = (struct Player *) right
	        ldr	x0, [sp, 16]	// load the value in sp+16, which is the address of 'right'
	        str	x0, [sp, 48]	// store it in sp+48
                // get player_left->score
	        ldr	x0, [sp, 56]
	        ldr	d1, [x0, 48]
                // get player_right->score
	        ldr	x0, [sp, 48]
	        ldr	d0, [x0, 48]
                // calculate player_left->score - player_right->score and send to signOfAFloat()
	        fsub	d0, d1, d0      // d0 = d1 - d0
	        bl	signOfAFloat    // call subroutine signOfAFloat
                // use a local variable 'temp' to store signOfAFloat(player_left->score - player_right->score);
	        str	w0, [sp, 44]
                // check if temp > 0
                cmp	w0, 0
	        b.le	comparator_label0	// return -1 if temp > 0, otherwise jump to the label
	        mov	w0, -1
	        b	comparator_end
comparator_label0:
                // check if temp < 0
                ldr	w0, [sp, 44]
                cmp	w0, 0
	        b.ge	comparator_label1    // return 1 if temp < 0, otherwise jump to the label
	        mov	w0, 1
	        b	comparator_end
comparator_label1:
	        ldr	x0, [sp, 56]	// get player_left
	        ldr	x1, [x0, 40]	// get player_left->duration
	        ldr	x0, [sp, 48]	// get player_right
	        ldr	x0, [x0, 40]	// get player_right->duration
                // check if player_left->duration > player_right->duration
	        cmp	x1, x0
	        b.le	comparator_label2    // return -1 if player_left->duration > player_right->duration, otherwise jump to the label
	        mov	w0, -1
	        b	comparator_end
comparator_label2:
                ldr	x0, [sp, 56]	// get player_left
                ldr	x1, [x0, 40]	// get player_left->duration
                ldr	x0, [sp, 48]	// get player_right
                ldr	x0, [x0, 40]	// get player_right->duration
	        cmp	x1, x0
	        b.ge	comparator_label3    // return 1 if player_left->duration < player_right->duration, otherwise jump to the label
	        mov	w0, 1
	        b	comparator_end
comparator_label3:
                ldr	x0, [sp, 56]	// get player_left
                ldr	x1, [x0, 32]	// get player_left->game_dimension
	        ldr	x0, [sp, 48]	// get player_right
	        ldr	x0, [x0, 32]	// get player_right->game_dimension
                // check if player_left->game_dimension > player_right->game_dimension
	        cmp	x1, x0
	        b.le	comparator_label4    // return -1 if player_left->game_dimension > player_right->game_dimension, otherwise jump to the label
	        mov	w0, -1
	        b	comparator_end
comparator_label4:
                ldr	x0, [sp, 56]	// get player_left
                ldr	x1, [x0, 32]	// get player_left->game_dimension
	        ldr	x0, [sp, 48]	// get player_right
	        ldr	x0, [x0, 32]	// get player_right->game_dimension
                // check if player_left->game_dimension < player_right->game_dimension
                cmp	x1, x0
                b.ge	comparator_label5    // return 1 if player_left->game_dimension < player_right->game_dimension, otherwise jump to the label
                mov	w0, 1
                b	comparator_end
comparator_label5:
	        ldr	x0, [sp, 56]	// get player_left->name
	        ldr	x1, [sp, 48]	// get player_right->name
	        bl	strcmp	        // pass these two pointers to strcmp to compare its name
comparator_end:
                ldp	fp, lr, [sp], 64        // Restore the state of the frame pointer register and procedure link register by post-incrementing 64 on stack pointer
                ret     // Give the control back to the caller

// double randomNum(int lower_bound, int upper_bound, int is_negative)
// generate a double which is between the lower_bound and upper_bound, if is_negative is true, then it must return a negative number,
// otherwise, return a positive number.
randomNum:
                stp     fp, lr, [sp, -64]!      // Allocate 64 bytes on the stack, save the contents of frame pointer register and procedure link register in the reserved block
                mov     fp, sp                  // Set frame pointer register to the address of the stack pointer
                str     w0, [sp, 28]        // store lower_bound
                str     w1, [sp, 24]        // store upper_bound
                str     w2, [sp, 20]        // store is_negative
                ldr     w0, [sp, 20]        // get is_negative
                cmp     w0, 0               // compare is_negative and 0
                b.ne randomNum_negative     // branch if is_negative
                bl      rand            // call rand()
                mov	w1, w0          // store rand() to w1
                ldr	w0, [sp, 24]    // get upperbound
                sub	w0, w0, 1       // upperbound - 1
                and	w0, w1, w0      // rand() & (upper_bound - 1)
                str	w0, [sp, 60]    // integer_part = rand() & (upper_bound - 1)
                bl	rand            // call rand()
                mov	w1, w0          // store the result of rand() to w1
                mov	w0, 99          // store 100 - 1
                and	w0, w1, w0      // get rand() & (100 - 1)
                scvtf	d0, w0          // convert to double
                ldr     x0, =one_over_a_hundred // get 0.01
                ldr     d1, [x0]
                fmul    d0, d0, d1              // (double)(rand() & (100 - 1)) * 0.01
                str	d0, [sp, 48]            // float_part = (double)(rand() & (100 - 1)) * 0.01
                ldr	w0, [sp, 60]    // get integer_part
                scvtf	d0, w0          // (double)integer_part
                ldr	d1, [sp, 48]    // get float_part
                fadd	d0, d1, d0      // float_part += (double)integer_part
                str	d0, [sp, 40]    // result = float_part
                ldr     x0, [sp, 40]    // get result
                b       randomNum_end   // jump to the branch
randomNum_negative:
                bl      rand            // call rand()
                mov     w1, w0          // store the result to w1
                ldr	w0, [sp, 28]    // get lower_bound
                mvn	w0, w0          // get -lower_bound-1
                and	w0, w1, w0      // rand() & (-lower_bound-1)
                str	w0, [sp, 60]    // integer_part = rand() & (-lower_bound-1)
                bl      rand            // call rand()
                mov	w1, w0          // store rand() to w1
                mov	w0, 99          // store 100 - 1
                and	w0, w1, w0      // rand() & (100 - 1)
                scvtf	d0, w0          // (double)(rand() & (100 - 1))
                ldr     x0, =one_over_a_hundred // get 0.01
                ldr	d1, [x0]        // load 0.01 to d1
                fmul	d0, d0, d1      // (double)(rand() & (100 - 1)) * 0.01
                str	d0, [sp, 48]    // float_part = (double)(rand() & (100 - 1)) * 0.01
                ldr	w0, [sp, 60]    // get integer_part
                neg     w0, w0          // -integer_part
                scvtf	d1, w0          // (double)(-integer_part)
                ldr	d0, [sp, 48]    // get float_part
                fsub	d0, d1, d0      // (double)(-integer_part) - float_part
                str	d0, [sp, 40]    // store to result
                ldr	x0, [sp, 40]    // get result
randomNum_end:  fmov	d0, x0          // move result to d0 and return
                ldp	fp, lr, [sp], 64        // Restore the state of the frame pointer register and procedure link register by post-incrementing 64 on stack pointer
                ret     // Give the control back to the caller

// int generateRandomInt(int lowerBound, int upperBound)
// generate an integer in the range of [lowerBound, upperBound]
generateRandomInt:
                stp     fp, lr, [sp, -32]!  // Allocate 32 bytes on the stack, save the contents of frame pointer register and procedure link register in the reserved block
                mov     fp, sp              // Set frame pointer register to the address of the stack pointer

                str     w0, [sp, 28]        // lowerBound
                str     w1, [sp, 24]        // upperBound
                bl      rand                // Call rand()
                mov     w1, w0              // save the result of rand() to w1

                ldr     w2, [sp, 24]	// load upperBound
                ldr     w0, [sp, 28]	// load lowerBound

                sub     w0, w2, w0	// range = upperBound - lowerBound
                add     w0, w0, 1       // range = range + 1

                sdiv w2, w1, w0         // w1 / range
                mul w0, w2, w0          // (w1 / range) * range
                sub w1, w1, w0          // w1 -  (w1 / range) * range
                ldr w0, [sp, 28]        // w0 = lowerBound
                add w0, w1, w0          // w0 = w1 + w0

                ldp fp, lr, [sp], 32        // Restore the state of the frame pointer register and procedure link register by post-incrementing 32 on stack pointer
                ret     // Give the control back to the caller


// void displayGame(char board[][SIZE_OF_MATRIX])
// implement the logic of displaying board
displayGame:
                stp     fp, lr, [sp, -48]!      // Allocate 48 bytes on the stack, save the contents of frame pointer register and procedure link register in the reserved block
                mov     fp, sp                  // Set frame pointer register to the address of the stack pointer
	        str	x0, [sp, 24]	// store the address of 'board' to sp+24
	        str	wzr, [sp, 44]	// initialize the local variable outer loop counter 'i' to 0
	        b	displayGame_outer_loop_test      // jump to the branch
displayGame_inner_loop_start:
	        str	wzr, [sp, 40]	// initialize the local variable inner loop counter 'j' to 0
	        b	displayGame_inner_loop_test      // jump to the branch
displayGame_inner_loop_main_part:
                // Get the address of board + i + j
                // get the local variable outer loop counter 'i'
	        ldrsw	x1, [sp, 44]
	        mov	x0, x1
	        lsl	x0, x0, 1
	        add	x0, x0, x1
	        lsl	x0, x0, 3
	        add	x0, x0, x1
	        ldr	x1, [sp, 24]
	        add	x1, x1, x0
	        ldrsw	x0, [sp, 40]
	        ldrb	w0, [x1, x0]	// w0 = *(board + i + j)
                // Print the character on board[i][j]
                mov	w1, w0	        // w1 = *(board + i + j)
	        adrp	x0, tab_and_character	// set the adrp address
	        add	x0, x0, :lo12:tab_and_character // add only the lowest 12 bits of the label to the adrp address
	        bl	printf  // Call printf
                // Increase the local variable inner loop counter 'j' by 1
	        ldr	w0, [sp, 40]
	        add	w0, w0, 1
	        str	w0, [sp, 40]
displayGame_inner_loop_test:
                // Compare the local variable inner loop counter 'j' with 'N'
                // get N
	        adrp	x0, N	// set the adrp address
	        add	x0, x0, :lo12:N // add only the lowest 12 bits of the label to the adrp address
	        ldr	w0, [x0]
                // get j
                ldr	w1, [sp, 40]
                cmp	w1, w0
                b.lt	displayGame_inner_loop_main_part        // Branch to the label if the less than relationship is hold (j < N)
	        adrp	x0, new_line // otherwise, print the new line, update 'i' and goto next outer loop
	        add	x0, x0, :lo12:new_line  // add only the lowest 12 bits of the label to the adrp address
                bl      printf  // Call printf
                // Increase the local variable outer loop counter 'i' by 1
	        ldr	w0, [sp, 44]
	        add	w0, w0, 1
	        str	w0, [sp, 44]
displayGame_outer_loop_test:
                // Compare the local variable outer loop counter 'i' with 'N'
                // get N
                adrp	x0, N	// set the adrp address
                add	x0, x0, :lo12:N // add only the lowest 12 bits of the label to the adrp address
                ldr	w0, [x0]
                // get i
                ldr	w1, [sp, 44]
                cmp	w1, w0
                b.lt	displayGame_inner_loop_start        // Branch to the label if the less than relationship is hold (i < N)
                // Check if (signOfAFloat(score) == 0)
	        adrp	x0, score	// set the adrp address
	        add	x0, x0, :lo12:score     // add only the lowest 12 bits of the label to the adrp address
	        ldr	d0, [x0]        // store the value in x0 to d0
	        bl	signOfAFloat    // call subroutine signOfAFloat
                // if (signOfAFloat(score) == 0), just print 0 without decimal
	        cmp	w0, 0
                // otherwise, jump to the label
	        b.ne	displayGame_show_actual_score
                adrp	x0, score_zero	// set the adrp address
                add	x0, x0, :lo12:score_zero        // add only the lowest 12 bits of the label to the adrp address
                bl	printf  // Call printf
                b	displayGame_show_remaining_time      // Jump to the branch
displayGame_show_actual_score:
                // if (signOfAFloat(score) != 0), print the actual score
	        adrp	x0, score	// set the adrp address
	        add	x0, x0, :lo12:score// add only the lowest 12 bits of the label to the adrp address
	        ldr	d0, [x0]        // store the value in x0 to d0
	        adrp	x0, score_double	// set the adrp address
	        add	x0, x0, :lo12:score_double// add only the lowest 12 bits of the label to the adrp address
	        bl	printf  // Call printf
displayGame_show_remaining_time:
                // Show the remaining time as well
	        adrp	x0, remaining_time	// set the adrp address
	        add	x0, x0, :lo12:remaining_time// add only the lowest 12 bits of the label to the adrp address
	        ldr	w0, [x0]
	        mov	w1, w0
	        adrp	x0, current_time_info	// set the adrp address
	        add	x0, x0, :lo12:current_time_info// add only the lowest 12 bits of the label to the adrp address
	        bl	printf  // Call printf
displayGame_end:
                ldp fp, lr, [sp], 48        // Restore the state of the frame pointer register and procedure link register by post-incrementing 48 on stack pointer
                ret     // Give the control back to the caller

// void initializeGameBoard(char board[][SIZE_OF_MATRIX])
// Initialize the game board as 'X'
initializeGameBoard:
	        sub	sp, sp, 32      // allocate 32 bytes locally
	        str	x0, [sp, 8]	// store the address of board on sp+8
	        str	wzr, [sp, 28]	// initialize the outer loop local variable 'i' as 0
	        b	initializeGameBoard_outer_loop_test      // Jump to the branch
initializeGameBoard_outer_loop:
	        str	wzr, [sp, 24]	// initialize the inner loop local variable 'j' as 0
	        b	initializeGameBoard_inner_loop_test      // Jump to the branch
initializeGameBoard_inner_loop:
                // get i
	        ldrsw	x1, [sp, 28]
                // calculate the adress of board+i
                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                ldr	x1, [sp, 8]
                add	x1, x1, x0
                // get j
	        ldrsw	x0, [sp, 24]
	        mov	w2, 'X'
                // set board[i][j] to 'X'
	        strb	w2, [x1, x0]
                // increase j by 1
                ldr	w0, [sp, 24]
                add	w0, w0, 1
                str	w0, [sp, 24]
initializeGameBoard_inner_loop_test:
                // get N
                adrp	x0, N	// set the adrp address
                add	x0, x0, :lo12:N// add only the lowest 12 bits of the label to the adrp address
                ldr	w0, [x0]
                // get j
	        ldr	w1, [sp, 24]
	        cmp	w1, w0
	        b.lt	initializeGameBoard_inner_loop        // Branch to the label if the less than relationship is hold (j < N)
                // increase i by 1
                ldr	w0, [sp, 28]
                add	w0, w0, 1
                str	w0, [sp, 28]
initializeGameBoard_outer_loop_test:
                // get N
                adrp	x0, N	// set the adrp address
                add	x0, x0, :lo12:N// add only the lowest 12 bits of the label to the adrp address
                ldr	w0, [x0]
                // get i
                ldr	w1, [sp, 28]
                cmp	w1, w0
                b.lt	initializeGameBoard_outer_loop        // Branch to the label if the less than relationship is hold (i < N)
	        add	sp, sp, 32  // Free 32 bytes being allocated before
	        ret     // Give the control back to the caller
// initializeIsVisited()
// Initialize the 2D array of is_visited
initializeIsVisited:
	        sub	sp, sp, 16	// allocate 16 bytes locally
	        str	wzr, [sp, 12]	// initialize the outer loop local variable 'i' as 0
	        b	initializeIsVisited_outer_loop_test      // Jump to the branch
initializeIsVisited_outer_loop:
	        str	wzr, [sp, 8]	// initialize the inner loop local variable 'j' as 0
	        b	initializeIsVisited_inner_loop_test      // Jump to the branch
initializeIsVisited_inner_loop:
                // get the address of is_visited
                adrp	x0, is_visited	// set the adrp address
                add	x2, x0, :lo12:is_visited// add only the lowest 12 bits of the label to the adrp address
	        ldrsw	x1, [sp, 12]	// get i
	        ldrsw	x3, [sp, 8]	// get j
                // calculate the address of is_visited+i+j
	        mov	x0, x1
	        lsl	x0, x0, 1
	        add	x0, x0, x1
	        lsl	x0, x0, 3
	        add	x0, x0, x1
	        add	x0, x0, x3
	        str	wzr, [x2, x0, lsl 2]	// Set is_visited[i][j] to be UNVISITED
                // increase j by 1
                ldr	w0, [sp, 8]
                add	w0, w0, 1
                str	w0, [sp, 8]
initializeIsVisited_inner_loop_test:
                // get N
                adrp	x0, N	// set the adrp address
                add	x0, x0, :lo12:N// add only the lowest 12 bits of the label to the adrp address
                ldr	w0, [x0]
                // get j
	        ldr	w1, [sp, 8]
	        cmp	w1, w0
	        b.lt	initializeIsVisited_inner_loop        // Branch to the label if the less than relationship is hold (j < N)
                // increase i by 1
                ldr	w0, [sp, 12]
                add	w0, w0, 1
                str	w0, [sp, 12]
initializeIsVisited_outer_loop_test:
                // get N
                adrp	x0, N	// set the adrp address
                add	x0, x0, :lo12:N// add only the lowest 12 bits of the label to the adrp address
                ldr	w0, [x0]
                // get i
                ldr	w1, [sp, 12]
                cmp	w1, w0
                b.lt	initializeIsVisited_outer_loop        // Branch to the label if the less than relationship is hold (i < N)
                add	sp, sp, 16	// Free 16 bytes being allocated before
                ret     // Give the control back to the caller

// void initializeScoreboard()
initializeScoreboard:
	        stp	fp, lr, [sp, -48]!      // Allocate 48 bytes on the stack, save the contents of frame pointer register and procedure link register in the reserved block
	        mov	fp, sp	// Set frame pointer register to the address of the stack pointer
                // get N
                adrp	x0, N	// set the adrp address
                add	x0, x0, :lo12:N // add only the lowest 12 bits of the label to the adrp address
                ldr	w1, [x0]        // store the value in x0 to w1
                // get N * N
	        mul	w0, w1, w1
                mov     w1, w0
                // get N * N / 5
                mov     w0, 5
                sdiv    w1, w1, w0
                mov     w0, 0
	        bl	generateRandomInt // generateRandomInt(0, N * N / 5)
	        str	w0, [sp, 32]	// number_of_negative_numbers = generateRandomInt(0, N * N / 5)
	        str	wzr, [sp, 44]	// initialize a local variable 'counter_of_negative_numbers' to 0
	        str	wzr, [sp, 40]	// initialize a local variable outer loop counter 'i' to 0
	        b	initializeScoreboard_outer_loop_test      // Jump to the branch
initializeScoreboard_outer_loop:
	        str	wzr, [sp, 36]	// initialize a local variable outer loop counter 'j' to 0
	        b	initializeScoreboard_inner_loop_test      // Jump to the branch
initializeScoreboard_inner_loop:
	        mov	w1, 1
	        mov	w0, 0
	        bl	generateRandomInt // generateRandomInt(0, 1)
	        str	w0, [sp, 28]	// temp = generateRandomInt(0, 1)
	        // Get temp
                ldr	w0, [sp, 28]
	        cmp	w0, 0
	        b.ne	initializeScoreboard_inner_loop_label0		// jump to the label if temp != 0
	        ldr	w1, [sp, 44]	// load the value of counter_of_negative_numbers
	        ldr	w0, [sp, 32]	// load the value of number_of_negative_numbers
	        cmp	w1, w0	// compare counter_of_negative_numbers and number_of_negative_numbers
                b.ge	initializeScoreboard_inner_loop_label0    // jump to the label if counter_of_negative_numbers >= number_of_negative_numbers
                mov	w0, -15 // set the first argument of randomNum() to -15
                mov	w1, 0   // set the second argument of randomNum() to 0
                mov	w2, 1   // set the third argument of randomNum() to 1
                bl	randomNum // Call randomNum(-15, 0, 1) to generate a negative number in [-15, 0)
                // Get the address of score_board
                adrp	x0, score_board	// set the adrp address
                add	x2, x0, :lo12:score_board// add only the lowest 12 bits of the label to the adrp address
                ldrsw	x1, [sp, 40]	// load the value of 'i'
                ldrsw	x3, [sp, 36]	// load the value of 'j'
                // get the address of score_board+i+j
                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x0, x3
	        str	d0, [x2, x0, lsl 3]	// store the result of randomNum() to score[i][j]
                // increase counter_of_negative_numbers by 1
                ldr	w0, [sp, 44]
                add	w0, w0, 1
                str	w0, [sp, 44]
	        b	initializeScoreboard_inner_loop_label1      // Jump to the branch
initializeScoreboard_inner_loop_label0:
                mov	w0, 0           // Set the first argument of randomNum() to 0
                mov	w1, 15          // Set the second argument of randomNum() to 15
                mov	w2, 0           // Set the third argument of randomNum() to 0
                bl	randomNum        // Call randomNum(0, 15, 0) to generate a positive number in (0, 15]
                // get the address of score_board
                adrp	x0, score_board	// set the adrp address
                add	x2, x0, :lo12:score_board// add only the lowest 12 bits of the label to the adrp address
                ldrsw	x1, [sp, 40]	// load the value of 'i'
                ldrsw	x3, [sp, 36]	// load the value of 'j'
                // get the address of score_board+i+j
                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x0, x3
                // store the result of randomNum() to score[i][j]
	        str	d0, [x2, x0, lsl 3]
initializeScoreboard_inner_loop_label1:
                // increase j by 1
                ldr	w0, [sp, 36]
                add	w0, w0, 1
                str	w0, [sp, 36]
initializeScoreboard_inner_loop_test:
                // get N
                adrp	x0, N	// set the adrp address
                add	x0, x0, :lo12:N // add only the lowest 12 bits of the label to the adrp address
                ldr	w0, [x0]
                // get j
                ldr	w1, [sp, 36]
                cmp	w1, w0
                b.lt	initializeScoreboard_inner_loop        // Branch to the label if the less than relationship is hold (j < N)
                // increase i by 1
                ldr	w0, [sp, 40]
                add	w0, w0, 1
                str	w0, [sp, 40]
initializeScoreboard_outer_loop_test:
                // get N
                adrp	x0, N	// set the adrp address
                add	x0, x0, :lo12:N // add only the lowest 12 bits of the label to the adrp address
                ldr	w0, [x0]
                // get i
                ldr	w1, [sp, 40]
                cmp	w1, w0
                b.lt	initializeScoreboard_outer_loop        // Branch to the label if the less than relationship is hold (i < N)
                ldp	fp, lr, [sp], 48        // Restore the state of the frame pointer register and procedure link register by post-incrementing 48 on stack pointer
                ret     // Give the control back to the caller

// void initializeTheTraps()
// the function initialize all + - traps in the map according to the value of score_board in the same place
initializeTheTraps:
                stp	fp, lr, [sp, -32]!      // Allocate 32 bytes on the stack, save the contents of frame pointer register and procedure link register in the reserved block
                mov	fp, sp  // Set frame pointer register to the address of the stack pointer
	        str	wzr, [sp, 28] // initialize the local variable outer loop counter 'j' to 0
	        b	initializeTheTraps_outer_loop_test      // Jump to the branch
initializeTheTraps_outer_loop:
	        str	wzr, [sp, 24] // initialize the local variable inner loop counter 'k' to 0
	        b	initializeTheTraps_inner_loop_test      // Jump to the branch
initializeTheTraps_inner_loop:
                // get the address of score_board
                adrp	x0, score_board	// set the adrp address
                add	x2, x0, :lo12:score_board       // add only the lowest 12 bits of the label to the adrp address
                ldrsw	x1, [sp, 28]	// load the value of 'j'
                ldrsw	x3, [sp, 24]	// load the value of 'k'
                // get the address of score_board+j+k
                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x0, x3
                ldr	d0, [x2, x0, lsl 3]	// store score_board[j][k] in d0
                bl	signOfAFloat    // call subroutine signOfAFloat
                cmp	w0, 0   // compare the result of signOfAFloat with 0
                b.ge	initializeTheTraps_inner_loop_label0   // branch if signOfAFloat(score_board[j][k]) >= 0
                // get the address of surprise_packs
                adrp	x0, surprise_packs	// set the adrp address
                add	x3, x0, :lo12:surprise_packs// add only the lowest 12 bits of the label to the adrp address
                ldrsw	x2, [sp, 24]	// load the value of 'k'
                ldrsw	x1, [sp, 28]	// load the value of 'j'
                // get the address of surprise_packs+j+k
                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x3, x0
                add	x0, x0, x2
	        mov	w1, '-'
	        strb	w1, [x0]	// write '-' back to surprise_packs[j][k]
	        b	initializeTheTraps_inner_loop_label1      // Jump to the branch
initializeTheTraps_inner_loop_label0:
                // get the address of surprise_packs
                adrp	x0, surprise_packs	// set the adrp address
                add	x3, x0, :lo12:surprise_packs// add only the lowest 12 bits of the label to the adrp address
                ldrsw	x1, [sp, 28]	// get the value of j
                ldrsw	x2, [sp, 24]	// get the value of k
                // get the address of surprise_packs+j+k
                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x3, x0
                add	x0, x0, x2

                mov	w1, '+'
                strb	w1, [x0]	// write '+' back to surprise_packs[j][k]
initializeTheTraps_inner_loop_label1:
                // increase k by 1
                ldr	w0, [sp, 24]
                add	w0, w0, 1
                str	w0, [sp, 24]
initializeTheTraps_inner_loop_test:
                // get N
                adrp	x0, N	// set the adrp address
                add	x0, x0, :lo12:N// add only the lowest 12 bits of the label to the adrp address
                ldr	w0, [x0]
                // get k
                ldr	w1, [sp, 24]
                cmp	w1, w0
                b.lt	initializeTheTraps_inner_loop        // Branch to the label if the less than relationship is hold (k < N)
                // increase j by 1
                ldr	w0, [sp, 28]
                add	w0, w0, 1
                str	w0, [sp, 28]
initializeTheTraps_outer_loop_test:
                // get N
                adrp	x0, N	// set the adrp address
                add	x0, x0, :lo12:N// add only the lowest 12 bits of the label to the adrp address
                ldr	w0, [x0]
                // get j
                ldr	w1, [sp, 28]
                cmp	w1, w0
                b.lt	initializeTheTraps_outer_loop        // Branch to the label if the less than relationship is hold (j < N)
                ldp	fp, lr, [sp], 32        // Restore the state of the frame pointer register and procedure link register by post-incrementing 32 on stack pointer
                ret     // Give the control back to the caller

// void initializeSurprisePacks()
// the function initialize all surprise packs in the map by overwritting the traps
initializeSurprisePacks:
	        stp	fp, lr, [sp, -48]!      // Allocate 48 bytes on the stack, save the contents of frame pointer register and procedure link register in the reserved block
	        mov	fp, sp  // Set frame pointer register to the address of the stack pointer
                // get N
                adrp	x0, N	// set the adrp address
                add	x0, x0, :lo12:N // add only the lowest 12 bits of the label to the adrp address
                ldr	w1, [x0]
                // get N * N
                mul	w0, w1, w1
                mov     w1, 10
                sdiv    w0, w0, w1
                str	w0, [sp, 40]	// number_of_surprise_packs = N * N / 10

	        str	wzr, [sp, 44]	// initialize the local variable outer loop counter 'j' to 0
	        b	initializeSurprisePacks_loop_test      // Jump to the branch
initializeSurprisePacks_loop:
                mov	w0, 0   // Set the first argument of generateRandomInt() to 0
                mov	w1, 2   // Set the second argument of generateRandomInt() to 2
                bl	generateRandomInt       // Call generateRandomInt(0, 2)
                str	w0, [sp, 36]	// surprise_pack_type = generateRandomInt(0, 2)
                // get N
                adrp	x0, N	// set the adrp address
                add	x0, x0, :lo12:N
                ldr	w0, [x0]
                // get N - 1
                sub	w0, w0, 1
                mov	w1, w0
                mov	w0, 0
                bl	generateRandomInt       // call generateRandomInt(0, N - 1)
                str	w0, [sp, 32]	//col = generateRandomInt(0, N - 1)
                // get N
                adrp	x0, N	// set the adrp address
                add	x0, x0, :lo12:N	// add only the lowest 12 bits of the label to the adrp address
                // get N - 1
                ldr	w0, [x0]	// get N
                sub	w0, w0, 1	// N - 1
                mov	w1, w0	// store to w1
                mov	w0, 0	// set the first argument of generateRandomInt to 0
                bl	generateRandomInt   // call generateRandomInt(0, N - 1)
                str	w0, [sp, 28]	// store the result to the address sp+28
                // Check if surprise_pack_type == 0
                ldr	w0, [sp, 36]	// get surprise_pack_type
                cmp	w0, 0	// compare to 0
                b.ne	initializeSurprisePacks_loop_label0     //  branch if surprise_pack_type != 0
                // Set surprise_packs[row][col] = '$';
                adrp	x0, surprise_packs	// set the adrp address
                add	x3, x0, :lo12:surprise_packs	// add only the lowest 12 bits of the label to the adrp address

                ldrsw	x1, [sp, 28]	// get the value of row
                ldrsw	x2, [sp, 32]	// get the value of col

                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x3, x0
                add	x0, x0, x2

                mov	w1, '$'
                strb	w1, [x0]

                b	initializeSurprisePacks_loop_label1      // Jump to the branch

initializeSurprisePacks_loop_label0:

                // Check if surprise_pack_type == 1
                ldr	w0, [sp, 36]
                cmp	w0, 1
                b.ne	initializeSurprisePacks_loop_label2     // branch if surprise_pack_type != 1

                // Set surprise_packs[row][col] = '!';
                adrp	x0, surprise_packs	// set the adrp address
                add	x3, x0, :lo12:surprise_packs // add only the lowest 12 bits of the label to the adrp address

                ldrsw	x2, [sp, 32]	// col
                ldrsw	x1, [sp, 28]	// row

                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x3, x0
                add	x0, x0, x2

                mov	w1, '!'
                strb	w1, [x0]	// Write '!' back to surprise_packs[row][col]
                b	initializeSurprisePacks_loop_label1      // Jump to the branch

initializeSurprisePacks_loop_label2:

                // Set surprise_packs[row][col] = '@'
                adrp	x0, surprise_packs	// set the adrp address
                add	x3, x0, :lo12:surprise_packs	// add only the lowest 12 bits of the label to the adrp address

                ldrsw	x1, [sp, 28]	// row
                ldrsw	x2, [sp, 32]	// col

                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x3, x0
                add	x0, x0, x2

                mov	w1, '@'
                strb	w1, [x0]	// Write '@' back to surprise_packs[row][col]

initializeSurprisePacks_loop_label1:

                // increase j by 1
                ldr	w0, [sp, 44]
                add	w0, w0, 1
                str	w0, [sp, 44]
initializeSurprisePacks_loop_test:
                // compare j and number_of_surprise_packs
                ldr	w1, [sp, 44]	// get j
                ldr	w0, [sp, 40]	// get number_of_surprise_packs
                cmp	w1, w0
                b.lt	initializeSurprisePacks_loop        // Branch to the label if the less than relationship is hold (j < number_of_surprise_packs)
initializeSurprisePacks_end:
                ldp	fp, lr, [sp], 48        // Restore the state of the frame pointer register and procedure link register by post-incrementing 48 on stack pointer
                ret     // Give the control back to the caller

// void initializeGame(char board[][SIZE_OF_MATRIX])
// Initialize the game by the execution of initializeGameBoard(board), initializeIsVisited(), initializeScoreboard(), score, unvisited_cell_counter, remaining_time, initializeTheTraps(), initializeSurprisePacks()
initializeGame:

	        stp	fp, lr, [sp, -32]!	// Allocate 32 bytes on the stack, save the contents of frame pointer register and procedure link register in the reserved block
	        mov	fp, sp	// Set frame pointer register to the address of the stack pointer
	        str	x0, [sp, 24]	// get board
                ldr	x0, [sp, 24]
                bl	initializeGameBoard		// Call initializeGameBoard()
                bl	initializeIsVisited		// Call initializeIsVisited()
                bl	initializeScoreboard		// Call initializeScoreboard()
                // Set score = 0
                adrp	x0, score	// set the adrp address
                add	x0, x0, :lo12:score	// add only the lowest 12 bits of the label to the adrp address
                str	xzr, [x0]	// initialize the value in x0 as 0, that is , set score = 0
                // Set unvisited_cell_counter = N * N
                adrp	x0, N	// set the adrp address
                add	x0, x0, :lo12:N	// add only the lowest 12 bits of the label to the adrp address
                ldr	w1, [x0]	// store the value in x0 to w1, that is, N
                // get N * N
                mul     w1, w1, w1

                adrp	x0, unvisited_cell_counter	// set the adrp address
                add	x0, x0, :lo12:unvisited_cell_counter	// add only the lowest 12 bits of the label to the adrp address
                str	w1, [x0]	// // Set unvisited_cell_counter = N * N by writing back

                // Set remaining_time = 60 + (N - 5) * 15
                adrp	x0, N	// set the adrp address
                add	x0, x0, :lo12:N	// add only the lowest 12 bits of the label to the adrp address
                ldr	w0, [x0]
                sub	w1, w0, 5
                mov	w0, w1
                lsl	w0, w0, 4
                sub	w0, w0, w1
                add	w1, w0, 60
                adrp	x0, remaining_time	// set the adrp address
                add	x0, x0, :lo12:remaining_time	// add only the lowest 12 bits of the label to the adrp address
                str	w1, [x0]

                bl	initializeTheTraps		// Call initializeTheTraps()

                bl	initializeSurprisePacks		// Call initializeSurprisePacks()

                ldp	fp, lr, [sp], 32        // Restore the state of the frame pointer register and procedure link register by post-incrementing 32 on stack pointer
                ret     // Give the control back to the caller

// int myStrlen(char *s)
// Calculate and return the length of the string 's'
myStrlen:       sub     sp, sp, 32  // allocate 32 bytes locally
                str     x0, [sp, 8] // store s
                str     wzr, [sp, 28] // initialize length to be 0
                str     wzr, [sp, 24] // initialize i to be 0
                b       myStrlen_loop2      // Jump to the branch
myStrlen_loop1:
                ldr     w0, [sp, 28] // get length
                add     w0, w0, 1 // ++length
                str     w0, [sp, 28] // store length

                ldr     w0, [sp, 24] // get i
                add     w0, w0, 1 // ++i
                str     w0, [sp, 24] // store i
myStrlen_loop2:
                // get *(s + i) and compare to null terminator
                ldrsw   x0, [sp, 24]
                ldr     x1, [sp, 8]
                add     x0, x1, x0
                ldrb    w0, [x0]
                cmp     w0, NULL_TERMINATOR
                b.ne    myStrlen_loop1

                ldr     w0, [sp, 28] // return length
                add     sp, sp, 32  // Free 32 bytes being allocated before
                ret     // Give the control back to the caller

// int myIsdigit(char ch)
// Check if '0' <= 'ch' <= '9', return 1 if it is the case, otherwise return 0
myIsdigit:
                stp     fp, lr, [sp, -16]!      // Allocate 16 bytes on the stack, save the contents of frame pointer register and procedure link register in the reserved block
                mov     fp, sp  // Set frame pointer register to the address of the stack pointer
                cmp     w0, '0' // Compare w0 with '0'
                b.lt    myIsdigit_test_failed        // Branch to the label if the less than relationship is hold (ch < '0')
myIsdigit_test:
                cmp     w0, '9' // Compare w0 with '9'
                b.gt    myIsdigit_test_failed   // Branch to the label if the greater than relationship is hold
                mov     w0, 1              // Return 1
                b       myIsdigit_end      // Jump to the branch

myIsdigit_test_failed:

                mov     w0, 0               // Return 0

myIsdigit_end:
                ldp     fp, lr, [sp], 16        // Restore the state of the frame pointer register and procedure link register by post-incrementing 16 on stack pointer
                ret     // Give the control back to the caller

// int isANumber(char *s)
// Check if 's' is a valid number, that is, no characters other than digits, also no redundant leading zeroes, return 1 if it is the case, otherwise return 0.
isANumber:
                stp fp, lr, [sp, -48]!  // Allocate 48 bytes on the stack, save the contents of frame pointer register and procedure link register in the reserved block
                mov fp, sp      // Set frame pointer register to the address of the stack pointer
                str x0, [sp, 24]        // get the address of s
                str wzr, [sp, 44]       // initialize i with 0
                b isANumber_loop3      // Jump to the branch
isANumber_loop1:
                ldrsw	x0, [sp, 44]    // load i to x0
                ldr	x1, [sp, 24]    // load s to x1
                add	x0, x1, x0      // s + i
                ldrb    w0, [x0]        // *(s + i) pass to myIsdigit
                bl      myIsdigit      // Jump to the branch

                cmp     w0, NULL_TERMINATOR
                b.ne    isANumber_loop2
                mov	w0, 0
                b       isANumber_end      // Jump to the branch

isANumber_loop2:
                // update i by 1
                ldr	w0, [sp, 44]
                add	w0, w0, 1
                str	w0, [sp, 44]
isANumber_loop3:
                // load w0 to s + i
                ldrsw	x0, [sp, 44]
                ldr	x1, [sp, 24]
                add	x0, x1, x0
                ldrb	w0, [x0]
                // check if *(s + i) == 0
                cmp	w0, 0
                b.ne    isANumber_loop1
                //  check if myStrlen(s) > 1
                ldr	x0, [sp, 24]
                bl	myStrlen
                cmp	w0, 1
                b.le    isANumber_next
                ldr	x0, [sp, 24]
                ldrb	w0, [x0]
                // check if *s == '0'
                cmp	w0, '0'
                b.ne    isANumber_next

                mov     w0, 0
                b isANumber_end      // Jump to the branch

isANumber_next: mov w0, 1       // return 1

isANumber_end:  ldp fp, lr, [sp], 48        // Restore the state of the frame pointer register and procedure link register by post-incrementing 48 on stack pointer
                ret     // Give the control back to the caller

// void exitGame()
// Print a goodbye message and quit the game
exitGame:       stp     fp, lr, [sp, -16]!      // Allocate 16 bytes on the stack, save the contents of frame pointer register and procedure link register in the reserved block
                mov     fp, sp                  // Set frame pointer register to the address of the stack pointer

	        adrp	x0, have_a_nice_day	// set the adrp address
	        add	x0, x0, :lo12:have_a_nice_day

                ldr     x9, =player_name    // get player's name's address
                ldr     x1, [x9]
                bl      printf  // Call printf

                ldp     fp, lr, [sp], 16        // Restore the state of the frame pointer register and procedure link register by post-incrementing 16 on stack pointer
                ret     // Give the control back to the caller

// void readLog()
// Read the game history log into the array struct Player player_history[MAX_HISTORY_ITEMS]
readLog:
	        stp	fp, lr, [sp, -48]!      // Allocate 48 bytes on the stack, save the contents of frame pointer register and procedure link register in the reserved block
	        mov	fp, sp                  // Set frame pointer register to the address of the stack pointer

                //     int fd = openat(AT_FDCWD, history_file_path, O_CREAT | O_RDONLY, S_IRUSR | S_IWUSR);

                //      load history_file_path as the second argument of openat()
                adrp	x0, history_file_path	// set the adrp address
                add	x1, x0, :lo12:history_file_path // add only the lowest 12 bits of the label to the adrp address
                mov	w0, AT_FDCWD	// load AT_FDCWD as the first argument of openat()
                mov	w2, O_FLAG      // load O_FLAG as the third argument of openat()
                mov	w3, S_FLAG      // load S_FLAG as the fourth argument of openat()
                mov     x8, OPENAT  // openat I/O request
                svc     0       // invoke syscall
	        str	w0, [sp, 44]	// store the result to fd

                // check if fd == -1
	        ldr	w0, [sp, 44]	// get the value of fd
	        cmp	w0, 0	        // compare with 0
	        b.ge	readLog_label0	// if not, openat successfully executed, branch to the label
                // otherwise, report and quit
                adrp	x0, failed_open_log_file	// set the adrp address
                add	x0, x0, :lo12:failed_open_log_file // add only the lowest 12 bits of the label to the adrp address
                bl	printf  // Call printf
	        mov	w0, 0
	        bl	exit    // Exit the program directly
readLog_label0:
                // long nRead = read(fd, input_buffer, BUFFER_SIZE);
	        adrp	x0, input_buffer	// set the adrp address
	        add	x1, x0, :lo12:input_buffer // add only the lowest 12 bits of the label to the adrp address
	        ldr	w0, [sp, 44]	// get the value of fd
                mov	x2, BUFFER_SIZE
                mov     x8, READ        // read I/O request
                svc     0       // invoke syscall

	        str	x0, [sp, 32]	// store the result to nRead

                // buffer_pointer = input_buffer;
                adrp	x0, buffer_pointer
                add	x0, x0, :lo12:buffer_pointer
                adrp	x1, input_buffer
                add	x1, x1, :lo12:input_buffer
                str	x1, [x0]
                // Check if nRead >= 0
                ldr	x0, [sp, 32]
                cmp	x0, 0
	        b.ge	readLog_label1
                // Report and quit if nRead < 0
                adrp	x0, failed_read_log_file	// set the adrp address
                add	x0, x0, :lo12:failed_read_log_file // add only the lowest 12 bits of the label to the adrp address
                bl	printf  // Call printf

                mov	w0, 0
                bl	exit    // Exit the program directly
readLog_label1:
                // int status = close(fd);
	        ldr	w0, [sp, 44]
                mov     x8, CLOSE
                svc     0       // invoke syscall
	        str	w0, [sp, 28]
                // Check if (status != 0) {
                ldr	w0, [sp, 28]
                cmp	w0, 0
                b.eq	readLog_label2
                // Report and quit if status != 0
                adrp	x0, failed_close_log_file	// set the adrp address
                add	x0, x0, :lo12:failed_close_log_file// add only the lowest 12 bits of the label to the adrp address
                bl	printf  // Call printf
readLog_label2:
                // Check if nRead > 0
	        ldr	x0, [sp, 32]
	        cmp	x0, 0
	        b.le	readLog_end
                // Set length_of_history = 0
                adrp	x0, length_of_history	// set the adrp address
                add	x0, x0, :lo12:length_of_history// add only the lowest 12 bits of the label to the adrp address
                str	wzr, [x0]
readLog_loop_label0:
                // Check if length_of_history > MAX_HISTORY_ITEMS - 1
                adrp	x0, length_of_history	// set the adrp address
                add	x0, x0, :lo12:length_of_history	// add only the lowest 12 bits of the label to the adrp address
                ldr	w0, [x0]
	        cmp	w0, 99
	        b.gt	readLog_end   // Branch to the label if the greater than relationship is hold

                // get buffer_pointer
                adrp	x0, buffer_pointer	// set the adrp address
                add	x0, x0, :lo12:buffer_pointer // add only the lowest 12 bits of the label to the adrp address
                ldr	x7, [x0]

                // get player_history[length_of_history].name
                adrp	x0, length_of_history	// set the adrp address
                add	x0, x0, :lo12:length_of_history
	        ldr	w0, [x0]

                sxtw	x1, w0
                mov	x0, x1
                lsl	x0, x0, 3
                sub	x0, x0, x1
                lsl	x0, x0, 3

                adrp	x1, player_history	// set the adrp address
                add	x1, x1, :lo12:player_history	// add only the lowest 12 bits of the label to the adrp address
	        add	x2, x0, x1

                // get the address of player_history[length_of_history].game_dimension
                adrp	x0, length_of_history	// set the adrp address
                add	x0, x0, :lo12:length_of_history
                ldr	w0, [x0]

                sxtw	x1, w0	// length_of_history
                mov	x0, x1
                lsl	x0, x0, 3
                sub	x0, x0, x1
                lsl	x0, x0, 3
                add	x1, x0, 32

                adrp	x0, player_history
                add	x0, x0, :lo12:player_history

	        add	x3, x1, x0

                // get the address of player_history[length_of_history].duration
                adrp	x0, length_of_history	// set the adrp address
                add	x0, x0, :lo12:length_of_history // add only the lowest 12 bits of the label to the adrp address
                ldr	w0, [x0]

                sxtw	x1, w0
                mov	x0, x1
                lsl	x0, x0, 3
                sub	x0, x0, x1
                lsl	x0, x0, 3
                add	x1, x0, 32

                adrp	x0, player_history	// set the adrp address
                add	x0, x0, :lo12:player_history // add only the lowest 12 bits of the label to the adrp address
                add	x0, x1, x0
                add	x4, x0, 8

                // Get &player_history[length_of_history].score
                adrp	x0, length_of_history	// set the adrp address
                add	x0, x0, :lo12:length_of_history	 // add only the lowest 12 bits of the label to the adrp address
                ldr	w0, [x0]

                sxtw	x1, w0
                mov	x0, x1
                lsl	x0, x0, 3
                sub	x0, x0, x1
                lsl	x0, x0, 3
                add	x1, x0, 48

                adrp	x0, player_history	// set the adrp address
                add	x0, x0, :lo12:player_history	// add only the lowest 12 bits of the label to the adrp address
                add	x0, x1, x0

                add	x1, sp, 20
                mov	x6, x1
                mov	x5, x0

                adrp	x0, sscanf_format       // set the adrp address
                add	x1, x0, :lo12:sscanf_format     // add only the lowest 12 bits of the label to the adrp address
                mov	x0, x7
                bl	sscanf  // Call sscanf
                str	w0, [sp, 24]

                // Check if temp == 4
                ldr	w0, [sp, 24]
                cmp	w0, DATA_MEMBER_OF_STRUCT
                b.ne	readLog_loop_label1

                // length_of_history = length_of_history + 1
                adrp	x0, length_of_history	// set the adrp address
                add	x0, x0, :lo12:length_of_history	// add only the lowest 12 bits of the label to the adrp address
                ldr	w0, [x0]
	        add	w1, w0, 1
                adrp	x0, length_of_history	// set the adrp address
                add	x0, x0, :lo12:length_of_history	// add only the lowest 12 bits of the label to the adrp address
                str	w1, [x0]

                // buffer_pointer = offset + buffer_pointer;
                adrp	x0, buffer_pointer      // set the adrp address
                add	x0, x0, :lo12:buffer_pointer	// add only the lowest 12 bits of the label to the adrp address

                ldr	x1, [x0]
                ldr	w0, [sp, 20]
                sxtw	x0, w0
                add	x1, x1, x0

                adrp	x0, buffer_pointer	// set the adrp address
                add	x0, x0, :lo12:buffer_pointer	// add only the lowest 12 bits of the label to the adrp address

                str	x1, [x0]
                b	readLog_loop_label0      // Jump to the branch
readLog_loop_label1:
                // Check if temp == EOF
                ldr	w0, [sp, 24]
                cmp	w0, EOF
                b.eq	readLog_end
                // Report and quit if failed on parsing the log file
	        adrp	x0, failed_parse_log_file	// set the adrp address
	        add	x0, x0, :lo12:failed_parse_log_file  // add only the lowest 12 bits of the label to the adrp address
	        bl	printf  // Call printf
                mov	w0, 0
                bl	exit    // Exit the program directly
readLog_end:
                ldp	fp, lr, [sp], 48        // Restore the state of the frame pointer register and procedure link register by post-incrementing 48 on stack pointer
                ret     // Give the control back to the caller

// void displayTopScores(int n)
// display the top n scores
displayTopScores:
	        stp	fp, lr, [sp, -48]!	// Allocate 48 bytes on the stack, save the contents of frame pointer register and procedure link register in the reserved block
	        mov	fp, sp	// Set frame pointer register to the address of the stack pointer
	        str	w0, [sp, 28]	// store n
	        bl	readLog		// Call readLog()
                // Check if n > length_of_history
                adrp	x0, length_of_history	// set the adrp address
                add	x0, x0, :lo12:length_of_history	// Set frame pointer register to the address of the stack pointer
                ldr	w0, [x0]
                ldr	w1, [sp, 28]
                cmp	w1, w0
                b.le	displayTopScores_label0

                adrp	x0, length_of_history	// set the adrp address
                add	x0, x0, :lo12:length_of_history	// Set frame pointer register to the address of the stack pointer
                ldr	w0, [x0]

                mov	w1, w0
                adrp	x0, only_n_items_in_history	// set the adrp address
                add	x0, x0, :lo12:only_n_items_in_history	// Set frame pointer register to the address of the stack pointer
                bl	printf  // Call printf

                adrp	x0, length_of_history	// set the adrp address
                add	x0, x0, :lo12:length_of_history	// Set frame pointer register to the address of the stack pointer

                ldr	w0, [x0]
                str	w0, [sp, 28]

displayTopScores_label0:
                // qsort(player_history, length_of_history, sizeof(struct Player), comparator)
                adrp	x0, length_of_history	// set the adrp address
                add	x0, x0, :lo12:length_of_history	// Set frame pointer register to the address of the stack pointer
                ldr	w0, [x0]
                sxtw	x1, w0

                adrp	x0, comparator	// set the adrp address
                add	x3, x0, :lo12:comparator	// add only the lowest 12 bits of the label to the adrp address

	        mov	x2, SIZEOF_STRUCT_PLAYER	// get sizeof(struct Player)
                adrp	x0, player_history	// set the adrp address
                add	x0, x0, :lo12:player_history	// Set frame pointer register to the address of the stack pointer

	        bl	qsort		//      Call qsort

                ldr	w1, [sp, 28]
                adrp	x0, top_n_scores_title  // set the adrp address
                add	x0, x0, :lo12:top_n_scores_title        // add only the lowest 12 bits of the label to the adrp address
	        bl	printf  // Call printf

	        str	wzr, [sp, 44]	// initialize the local loop counter as 0

	        b	displayTopScores_loop_test

displayTopScores_loop:

                // get player_history[i].name and player_history[i].game_dimension
                ldrsw	x1, [sp, 44]
                mov	x0, x1
                lsl	x0, x0, 3
                sub	x0, x0, x1
                lsl	x0, x0, 3

                adrp	x1, player_history	// set the adrp address
                add	x1, x1, :lo12:player_history	// add only the lowest 12 bits of the label to the adrp address
                add	x4, x0, x1

                adrp	x0, player_history	// set the adrp address
                add	x2, x0, :lo12:player_history	// add only the lowest 12 bits of the label to the adrp address

                ldrsw	x1, [sp, 44]
                mov	x0, x1
                lsl	x0, x0, 3
                sub	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x2, x0
                ldr	x5, [x0, 32]

                adrp	x0, player_history	// set the adrp address
                add	x2, x0, :lo12:player_history	// add only the lowest 12 bits of the label to the adrp address

                ldrsw	x1, [sp, 44]
                mov	x0, x1
                lsl	x0, x0, 3
                sub	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x2, x0
                ldr	x3, [x0, 40]

                adrp	x0, player_history	// set the adrp address
                add	x2, x0, :lo12:player_history	// add only the lowest 12 bits of the label to the adrp address

                ldrsw	x1, [sp, 44]
                mov	x0, x1
                lsl	x0, x0, 3
                sub	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x2, x0
                ldr	d0, [x0, 48]

                mov	x2, x5
                mov	x1, x4

                adrp	x0, format_of_each_item	// set the adrp address
                add	x0, x0, :lo12:format_of_each_item	// add only the lowest 12 bits of the label to the adrp address
                bl	printf  // Call printf
                // increase the loop counter 'i' by 1
                ldr	w0, [sp, 44]
                add	w0, w0, 1
                str	w0, [sp, 44]
displayTopScores_loop_test:
                // Compare 'i' and 'n'
                ldr	w1, [sp, 44]
                ldr	w0, [sp, 28]
                cmp	w1, w0
                b.lt	displayTopScores_loop        // Branch to the label if the less than relationship is hold (i < n)
displayTopScores_end:
                ldp	fp, lr, [sp], 48        // Restore the state of the frame pointer register and procedure link register by post-incrementing 48 on stack pointer
                ret     // Give the control back to the caller

// void checkTopScores()
// the function that implements the process of checking top scores
checkTopScores:
	        stp	fp, lr, [sp, -32]!	// Allocate 32 bytes on the stack, save the contents of frame pointer register and procedure link register in the reserved block
	        mov	fp, sp	// Set frame pointer register to the address of the stack pointer
checkTopScores_loop:
                adrp	x0, ask_for_n	// set the adrp address
                add	x0, x0, :lo12:ask_for_n // add only the lowest 12 bits of the label to the adrp address
                bl	printf  // Call printf
                // scanf("%d", &n)
                add	x0, sp, 28
                mov	x1, x0
                adrp	x0, scanf_one_number	// set the adrp address
                add	x0, x0, :lo12:scanf_one_number  // add only the lowest 12 bits of the label to the adrp address
	        bl	scanf  // Call scanf
                // Check if (n <= 0 || n > MAX_HISTORY_ITEMS)
	        ldr	w0, [sp, 28]
	        cmp	w0, 0	// n.12_1,
	        b.le	checkTopScores_loop_label0
	        ldr	w0, [sp, 28]
	        cmp	w0, MAX_HISTORY_ITEMS
	        b.le	checkTopScores_loop_label1
checkTopScores_loop_label0:
                adrp	x0, n_is_out_of_range	// set the adrp address
                add	x0, x0, :lo12:n_is_out_of_range // add only the lowest 12 bits of the label to the adrp address
                bl	printf  // Call printf
	        b	checkTopScores_loop
checkTopScores_loop_label1:
                // Pass n to displayTopScores()
                ldr	w0, [sp, 28]
                bl	displayTopScores
                ldp	fp, lr, [sp], 32        // Restore the state of the frame pointer register and procedure link register by post-incrementing 32 on stack pointer
                ret     // Give the control back to the caller

// void writeToLog()
// the function implements the logic of writing the history log to the external file
writeToLog:
                stp	fp, lr, [sp, -32]!      // Allocate 32 bytes on the stack, save the contents of frame pointer register and procedure link register in the reserved block
                mov	fp, sp  // Set frame pointer register to the address of the stack pointer

                // qsort(player_history, length_of_history, sizeof(struct Player), comparator);
                adrp	x0, length_of_history	// set the adrp address
                add	x0, x0, :lo12:length_of_history	// add only the lowest 12 bits of the label to the adrp address
                ldr	w0, [x0]
	        sxtw	x1, w0

                adrp	x0, comparator	// set the adrp address
                add	x3, x0, :lo12:comparator	// add only the lowest 12 bits of the label to the adrp address

	        mov	x2, SIZEOF_STRUCT_PLAYER

                adrp	x0, player_history	// set the adrp address
                add	x0, x0, :lo12:player_history	// add only the lowest 12 bits of the label to the adrp address
	        bl	qsort		// Call qsort

                // Check if (length_of_history > MAX_HISTORY_ITEMS)
                adrp	x0, length_of_history	// set the adrp address
                add	x0, x0, :lo12:length_of_history	// add only the lowest 12 bits of the label to the adrp address
                ldr	w0, [x0]
                cmp	w0, MAX_HISTORY_ITEMS
                b.le	writeToLog_label0

                // Set length_of_history = MAX_HISTORY_ITEMS
                adrp	x0, length_of_history
                add	x0, x0, :lo12:length_of_history
                mov	w1, MAX_HISTORY_ITEMS
                str	w1, [x0]
writeToLog_label0:
                // Set buffer_pointer = output_buffer
                adrp	x0, buffer_pointer      // set the adrp address
                add	x0, x0, :lo12:buffer_pointer    // add only the lowest 12 bits of the label to the adrp address

                adrp	x1, output_buffer       // set the adrp address
                add	x1, x1, :lo12:output_buffer     // add only the lowest 12 bits of the label to the adrp address
                str	x1, [x0]

	        str	wzr, [sp, 28]	// initialize the loop counter 'i' as 0
	        b	writeToLog_loop_test
writeToLog_loop:
                // buffer_pointer += sprintf(buffer_pointer, "%s %ld %ld %.2lf",player_history[i].name,player_history[i].game_dimension, player_history[i].duration,player_history[i].score);
                adrp	x0, buffer_pointer
                add	x0, x0, :lo12:buffer_pointer
                ldr	x5, [x0]
                // get player_history[i].name,
                ldrsw	x1, [sp, 28]
                mov	x0, x1
                lsl	x0, x0, 3
                sub	x0, x0, x1
                lsl	x0, x0, 3

                adrp	x1, player_history	// set the adrp address
                add	x1, x1, :lo12:player_history	// add only the lowest 12 bits of the label to the adrp address
                add	x6, x0, x1

                adrp	x0, player_history	// set the adrp address
                add	x2, x0, :lo12:player_history	// add only the lowest 12 bits of the label to the adrp address

                ldrsw	x1, [sp, 28]
                mov	x0, x1
                lsl	x0, x0, 3
                sub	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x2, x0
                ldr	x3, [x0, 32]

                adrp	x0, player_history      	// set the adrp address
                add	x2, x0, :lo12:player_history    // add only the lowest 12 bits of the label to the adrp address

                ldrsw	x1, [sp, 28]
                mov	x0, x1
                lsl	x0, x0, 3
                sub	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x2, x0
                ldr	x4, [x0, 40]

                adrp	x0, player_history	// set the adrp address
                add	x2, x0, :lo12:player_history	// add only the lowest 12 bits of the label to the adrp address

                // Calculate the address
                ldrsw	x1, [sp, 28]
                mov	x0, x1
                lsl	x0, x0, 3
                sub	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x2, x0
                ldr	d0, [x0, 48]
                mov	x2, x6

                adrp	x0, sprintf_format	// set the adrp address
                add	x1, x0, :lo12:sprintf_format   // add only the lowest 12 bits of the label to the adrp address
                mov	x0, x5
                bl	sprintf		// Call sprintf()
                mov	w2, w0

                // buffer_pointer += sprintf(buffer_pointer, "%s %ld %ld %.2lf",player_history[i].name,player_history[i].game_dimension,player_history[i].duration,player_history[i].score);
                adrp	x0, buffer_pointer	// set the adrp address
                add	x0, x0, :lo12:buffer_pointer	// Set frame pointer register to the address of the stack pointer
                ldr	x1, [x0]

	        sxtw	x0, w2
                add	x1, x1, x0
                adrp	x0, buffer_pointer	// set the adrp address
                add	x0, x0, :lo12:buffer_pointer	// add only the lowest 12 bits of the label to the adrp address
                str	x1, [x0]

                // Set *buffer_pointer = ' '
                adrp	x0, buffer_pointer
                add	x0, x0, :lo12:buffer_pointer
                ldr	x0, [x0]
                mov	w1, ' '
                strb	w1, [x0]
                // ++buffer_pointer
                adrp	x0, buffer_pointer
                add	x0, x0, :lo12:buffer_pointer
                ldr	x0, [x0]
                add	x1, x0, 1
                adrp	x0, buffer_pointer
                add	x0, x0, :lo12:buffer_pointer
                str	x1, [x0]
                // increase the loop counter 'i' by 1
                ldr	w0, [sp, 28]
                add	w0, w0, 1
                str	w0, [sp, 28]
writeToLog_loop_test:
                adrp	x0, length_of_history	// set the adrp address
                add	x0, x0, :lo12:length_of_history	// add only the lowest 12 bits of the label to the adrp address
                ldr	w0, [x0]
                ldr	w1, [sp, 28]	// get 'i'
                cmp	w1, w0	// Compare 'i' with length_of_history
                b.lt	writeToLog_loop        // Branch to the label if the less than relationship is hold (i < length_of_history)

                // Set *buffer_pointer = '\0';
                adrp	x0, buffer_pointer
                add	x0, x0, :lo12:buffer_pointer
                ldr	x0, [x0]
	        strb	wzr, [x0]

                // int fd = openat(AT_FDCWD, history_file_path, O_CREAT | O_RDWR | O_TRUNC, S_IRUSR | S_IWUSR)
                // get history_file_path as the second argument of openat()
	        adrp	x0, history_file_path	// set the adrp address
	        add	x1, x0, :lo12:history_file_path	// add only the lowest 12 bits of the label to the adrp address
	        mov	w0, AT_FDCWD	// Set the first argument of openat()
                mov	w2, O_FLAG_2	// Set the third argument of openat()
        	mov	w3, S_FLAG	// Set the fourth argument of openat()

                mov     x8, OPENAT      // openat I/O request
                svc     0               // invoke syscall

	        str	w0, [sp, 24]	// get the value of fd
                // Check if fd < 0
	        ldr	w0, [sp, 24]
	        cmp	w0, 0
	        b.ge	writeToLog_label0_after_loop
                adrp	x0, failed_open_log_file	// set the adrp address
                add	x0, x0, :lo12:failed_open_log_file	// add only the lowest 12 bits of the label to the adrp address
                bl	printf  // Call printf

                mov	w0, 0
                bl	exit    // Exit the program directly
writeToLog_label0_after_loop:
                // write(fd, output_buffer, BUFFER_SIZE);
	        adrp	x0, output_buffer	// set the adrp address
	        add	x1, x0, :lo12:output_buffer	// add only the lowest 12 bits of the label to the adrp address
                ldr	w0, [sp, 24]	// get the value of fd as the 1st argument
                mov	x2, BUFFER_SIZE	// set buffer size as the 3rd argument
                mov     x8, WRITE       // Call write I/O request
                svc     0               // invoke syscall

writeToLog_label1_after_loop:
                // int status = close(fd);
	        ldr	w0, [sp, 24]	// get the value of fd
                mov     x8, CLOSE       // Call close I/O request
                svc     0               // invoke syscall
	        str	w0, [sp, 20]	// status
                // Check if status == 0
                ldr	w0, [sp, 20]
                cmp	w0, 0
                b.eq	writeToLog_end
                adrp	x0, failed_close_log_file	// set the adrp address
                add	x0, x0, :lo12:failed_close_log_file	// add only the lowest 12 bits of the label to the adrp address
                bl	printf  // Call printf
writeToLog_end:
                ldp	fp, lr, [sp], 32        // Restore the state of the frame pointer register and procedure link register by post-incrementing 32 on stack pointer
                ret     // Give the control back to the caller

// void logScore()
// the function logs the current player's score and write to the history stored in the external device
logScore:
	        stp	fp, lr, [sp, -32]!      // Allocate 32 bytes on the stack, save the contents of frame pointer register and procedure link register in the reserved block
	        mov	fp, sp                  // Set frame pointer register to the address of the stack pointer
                // obtain time(&current_time);
                adrp	x0, current_time	// set the adrp address
                add	x0, x0, :lo12:current_time	// add only the lowest 12 bits of the label to the adrp address
                bl	time		// Call time()
                // obtain difftime(current_time, start_time)
                adrp	x0, current_time	// set the adrp address
                add	x0, x0, :lo12:current_time	// add only the lowest 12 bits of the label to the adrp address
                ldr	x2, [x0]	// get current_time
                adrp	x0, start_time	// set the adrp address
                add	x0, x0, :lo12:start_time	// add only the lowest 12 bits of the label to the adrp address
                ldr	x0, [x0]
                mov	x1, x0
                mov	x0, x2
                bl	difftime        // Call difftime()
                // Obtain (int) difftime(current_time, start_time);
                fcvtzs	w0, d0	// Double to integer conversion
                str	w0, [sp, 24]
	        bl	readLog		// Call the subroutine readLog
	        str	wzr, [sp, 28]	// Initialize the loop counter as 0
	        b	logScore_loop_test
logScore_loop:
                // player_history[length_of_history].name[i] = player_name[i]
                adrp	x0, player_name	// set the adrp address
                add	x0, x0, :lo12:player_name	// add only the lowest 12 bits of the label to the adrp address
                ldr	x1, [x0]
                ldrsw	x0, [sp, 28]	// get i
                add	x0, x1, x0
                adrp	x1, length_of_history	// set the adrp address
                add	x1, x1, :lo12:length_of_history	// add only the lowest 12 bits of the label to the adrp address
                ldr	w1, [x1]
                ldrb	w4, [x0]
                adrp	x0, player_history	// set the adrp address
                add	x3, x0, :lo12:player_history	// add only the lowest 12 bits of the label to the adrp address
                // calculate the address
                ldrsw	x2, [sp, 28]
                sxtw	x1, w1
                mov	x0, x1
                lsl	x0, x0, 3
                sub	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x3, x0
                add	x0, x0, x2
                mov	w1, w4
                // store player_name[i] to player_history[length_of_history].name[i]
                strb	w1, [x0]
                // increase loop counter by 1
                ldr	w0, [sp, 28]
                add	w0, w0, 1
                str	w0, [sp, 28]
logScore_loop_test:
                adrp	x0, player_name	// set the adrp address
                add	x0, x0, :lo12:player_name	// add only the lowest 12 bits of the label to the adrp address
                ldr	x1, [x0]
                ldrsw	x0, [sp, 28]
                add	x0, x1, x0
                ldrb	w0, [x0]
                cmp	w0, NULL_TERMINATOR	// Comapre player_name[i] with 0
                b.ne	logScore_loop

                // Set player_history[length_of_history].score = score
                adrp	x0, length_of_history	// set the adrp address
                add	x0, x0, :lo12:length_of_history	// add only the lowest 12 bits of the label to the adrp address
                ldr	w1, [x0]	// length_of_history.28_13, length_of_history

                adrp	x0, score	// set the adrp address
                add	x0, x0, :lo12:score	// add only the lowest 12 bits of the label to the adrp address
                ldr	x2, [x0]

                adrp	x0, player_history	// set the adrp address
                add	x3, x0, :lo12:player_history	// add only the lowest 12 bits of the label to the adrp address

                // calculate the address
                sxtw	x1, w1
                mov	x0, x1
                lsl	x0, x0, 3
                sub	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x3, x0

                str	x2, [x0, 48]

                // Set player_history[length_of_history].game_dimension = N
                // get N
                adrp	x0, N	// set the adrp address
                add	x0, x0, :lo12:N	// add only the lowest 12 bits of the label to the adrp address
                ldr	w2, [x0]

                // get length_of_history
                adrp	x0, length_of_history	// set the adrp address
                add	x0, x0, :lo12:length_of_history	// add only the lowest 12 bits of the label to the adrp address
                ldr	w1, [x0]

                sxtw	x2, w2
                adrp	x0, player_history	// set the adrp address
                add	x3, x0, :lo12:player_history	// add only the lowest 12 bits of the label to the adrp address

                // calculate the address
                sxtw	x1, w1
                mov	x0, x1
                lsl	x0, x0, 3
                sub	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x3, x0
                str	x2, [x0, 32]

                // player_history[length_of_history].duration = duration;
                adrp	x0, length_of_history	// set the adrp address
                add	x0, x0, :lo12:length_of_history	// add only the lowest 12 bits of the label to the adrp address
                ldr	w1, [x0]

                ldrsw	x2, [sp, 24]
                adrp	x0, player_history	// set the adrp address
                add	x3, x0, :lo12:player_history	// add only the lowest 12 bits of the label to the adrp address
                sxtw	x1, w1
                mov	x0, x1
                lsl	x0, x0, 3
                sub	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x3, x0
                str	x2, [x0, 40]

                // ++length_of_history
                adrp	x0, length_of_history	// set the adrp address
                add	x0, x0, :lo12:length_of_history	// add only the lowest 12 bits of the label to the adrp address
                ldr	w0, [x0]
                add	w1, w0, 1
                adrp	x0, length_of_history	// set the adrp address
                add	x0, x0, :lo12:length_of_history	// add only the lowest 12 bits of the label to the adrp address
                str	w1, [x0]

	        bl	writeToLog		// Call writeToLog

                adrp	x0, score_saved	// set the adrp address
                add	x0, x0, :lo12:score_saved	// add only the lowest 12 bits of the label to the adrp address
                bl	printf  // Call printf

logScore_end:
                ldp	fp, lr, [sp], 32        // Restore the state of the frame pointer register and procedure link register by post-incrementing 32 on stack pointer
                ret     // Give the control back to the caller

// void calculateScore(int i, int j)
// the function update the score, remaining_time or game_board by given the new coordinate (i, j) and then print a corresponding message to notify the player
calculateScore:

                stp     fp, lr, [sp, -32]!      // Allocate 32 bytes on the stack, save the contents of frame pointer register and procedure link register in the reserved block
                mov     fp, sp                  // Set frame pointer register to the address of the stack pointer

                str	w0, [sp, 28]	// store the variable passed in i to sp+28
	        str	w1, [sp, 24]	// store the variable passed in j to sp+24

                // Check if surprise_packs[i][j] == '-'
                adrp	x0, surprise_packs	// set the adrp address
                add	x3, x0, :lo12:surprise_packs    // add only the lowest 12 bits of the label to the adrp address

                ldrsw	x1, [sp, 28]	// get the value in sp+28, which is i
                ldrsw	x2, [sp, 24]	// get the value in sp+24, which is j

                // calculate the address of surprise_packs+i+j
                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x3, x0
                add	x0, x0, x2

                // get the value of surprise_packs[i][j]
                ldrb	w0, [x0]
                cmp	w0, '-'                 // compare its value with surprise_packs[i][j]
	        b.ne	calculateScore_label0

                // get the value in score_board[i][j]
                adrp	x0, score_board	// set the adrp address
                add	x2, x0, :lo12:score_board       // add only the lowest 12 bits of the label to the adrp address

                ldrsw	x1, [sp, 28]	// get the value in sp+28, which is i
                ldrsw	x3, [sp, 24]	// get the value in sp+24, which is j

                // calculate the address of score_board[i][j]
                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x0, x3

                // get the value of score_board[i][j]
                ldr	d1, [x2, x0, lsl 3]

                // get score
                adrp	x0, score	// set the adrp address
	        add	x0, x0, :lo12:score     // add only the lowest 12 bits of the label to the adrp address
	        ldr	d0, [x0]	// get the score
	        fadd	d0, d1, d0	// add score_board[i][j] to the score

	        adrp	x0, score	// set the adrp address
	        add	x0, x0, :lo12:score     // add only the lowest 12 bits of the label to the adrp address
	        str	d0, [x0]	// write back to the score

                // get the value in game_board[i][j]
                adrp	x0, game_board	// set the adrp address
                add	x3, x0, :lo12:game_board        // add only the lowest 12 bits of the label to the adrp address

                ldrsw	x1, [sp, 28]	// get the value in sp+28, which is i
                ldrsw	x2, [sp, 24]	// get the value in sp+24, which is j

                // calculate the address of game_board[i][j]
                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x3, x0
                add	x0, x0, x2
                mov	w1, '-'
                strb	w1, [x0]	// write '-' back to the game_board[i][j]

                // get the value in score_board[i][j]
                adrp	x0, score_board	// set the adrp address
                add	x2, x0, :lo12:score_board       // add only the lowest 12 bits of the label to the adrp address

                ldrsw	x1, [sp, 28]	// get the value in sp+28, which is i
                ldrsw	x3, [sp, 24]	// get the value in sp+24, which is j

                // calculate the address of score_board[i][j]
                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x0, x3

                // Obtain the value of score_board[i][j]
                ldr	d0, [x2, x0, lsl 3]

                // Print the information that the player lose the points, including how many points lose
                adrp	x0, lost_points	// set the adrp address
                add	x0, x0, :lo12:lost_points       // add only the lowest 12 bits of the label to the adrp address
                bl	printf  // Call printf
                b	calculateScore_end

calculateScore_label0:

                // Check if surprise_packs[i][j] == '+'
                adrp	x0, surprise_packs	// set the adrp address
                add	x3, x0, :lo12:surprise_packs    // add only the lowest 12 bits of the label to the adrp address

                ldrsw	x1, [sp, 28]	// get the value in sp+28, which is i
                ldrsw	x2, [sp, 24]	// get the value in sp+24, which is j

                // Get the value of surprise_packs[i][j]
                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x3, x0
                add	x0, x0, x2
                ldrb	w0, [x0]

                cmp	w0, '+' // Compare the value of surprise_packs[i][j] and '+'
                b.ne	calculateScore_label1

                // get the value of score_board[i][j]
                adrp	x0, score_board	// set the adrp address
                add	x2, x0, :lo12:score_board       // add only the lowest 12 bits of the label to the adrp address

                ldrsw	x1, [sp, 28]	// get the value in sp+28, which is i
                ldrsw	x3, [sp, 24]	// get the value in sp+24, which is j

                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x0, x3

                ldr	d1, [x2, x0, lsl 3]

                // get score
                adrp	x0, score	// set the adrp address
	        add	x0, x0, :lo12:score   // add only the lowest 12 bits of the label to the adrp address

	        ldr	d0, [x0]	// get the score
	        fadd	d0, d1, d0	// add score_board[i][j] to the score
	        adrp	x0, score	// set the adrp address
	        add	x0, x0, :lo12:score   // add only the lowest 12 bits of the label to the adrp address
	        str	d0, [x0]        // write back to the score

                // get the value in game_board[i][j]
                adrp	x0, game_board	// set the adrp address
                add	x3, x0, :lo12:game_board // add only the lowest 12 bits of the label to the adrp address

                ldrsw	x1, [sp, 28]	// get the value in sp+28, which is i
                ldrsw	x2, [sp, 24]	// get the value in sp+24, which is j

                // Write '+' back to the game_board[i][j]
                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x3, x0
                add	x0, x0, x2
                mov	w1, '+'
                strb	w1, [x0]

                // Get the value of score_board[i][j]
                adrp	x0, score_board	// set the adrp address
	        add	x2, x0, :lo12:score_board // add only the lowest 12 bits of the label to the adrp address

                ldrsw	x1, [sp, 28]	// get the value in sp+28, which is i
                ldrsw	x3, [sp, 24]	// get the value in sp+24, which is j

                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x0, x3
                ldr	d0, [x2, x0, lsl 3]

                // Print the information that the player uncovered a reward, including how many points are rewarded
                adrp	x0, uncovered_reward	// set the adrp address
                add	x0, x0, :lo12:uncovered_reward // add only the lowest 12 bits of the label to the adrp address
                bl	printf  // Call printf

                b       calculateScore_end

calculateScore_label1:
                // Check if surprise_packs[i][j] == '@'
                // obtain the value of surprise_packs[i][j]
                adrp	x0, surprise_packs	// set the adrp address
                add	x3, x0, :lo12:surprise_packs // add only the lowest 12 bits of the label to the adrp address

                ldrsw	x1, [sp, 28]	// get the value in sp+28, which is i
                ldrsw	x2, [sp, 24]	// get the value in sp+24, which is j

                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x3, x0
                add	x0, x0, x2

                ldrb	w0, [x0]
                cmp     w0, '@'         // Compare the surprise_packs[i][j] and '@'
                b.ne    calculateScore_label2

                // get the remaining time
                adrp	x0, remaining_time	// set the adrp address
                add	x0, x0, :lo12:remaining_time // add only the lowest 12 bits of the label to the adrp address
                ldr	w0, [x0]
                add	w1, w0, 30	// add 30 seconds on it

                adrp	x0, remaining_time	// set the adrp address
                add	x0, x0, :lo12:remaining_time // add only the lowest 12 bits of the label to the adrp address
                str	w1, [x0]	// write back to the remaining time

                // Get the value of game_board[i][j] and write '@' back to it
                adrp	x0, game_board	// set the adrp address
                add	x3, x0, :lo12:game_board

                ldrsw	x1, [sp, 28]	// get the value in sp+28, which is i
                ldrsw	x2, [sp, 24]	// get the value in sp+24, which is j

                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x3, x0
                add	x0, x0, x2
                mov	w1, '@'
                strb	w1, [x0]

                // Print the information that the player has a 30 seconds extension
                adrp	x0, got_extra_seconds	// set the adrp address
	        add	x0, x0, :lo12:got_extra_seconds  // add only the lowest 12 bits of the label to the adrp address
	        bl	printf  // Call printf

                b	calculateScore_end

calculateScore_label2:
                // Check if surprise_packs[i][j] == '!'
                // get the address pf surprise_packs
                adrp	x0, surprise_packs	// set the adrp address
                add	x3, x0, :lo12:surprise_packs

                ldrsw	x1, [sp, 28]	// get the value in sp+28, which is i
                ldrsw	x2, [sp, 24]	// get the value in sp+24, which is j

                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x3, x0
                add	x0, x0, x2

                // Get the value of surprise_packs[i][j]
                ldrb	w0, [x0]

                cmp	w0, '!'         // Write '!' back to surprise_packs[i][j]
                b.ne	calculateScore_label3

                // Get 100.0
                adrp	x0, one_hundred_in_double	// set the adrp address
                add	x0, x0, :lo12:one_hundred_in_double  // add only the lowest 12 bits of the label to the adrp address
                ldr	d1, [x0]

                // Get the score
                adrp	x0, score	// set the adrp address
                add	x0, x0, :lo12:score  // add only the lowest 12 bits of the label to the adrp address
                ldr	d0, [x0]

                fmul	d0, d0, d1	// d0 = d0 * 100.0
                fcvtzs	w0, d0          // convert to integer
                asr	w0, w0, 1       // divide by 2 using arithmetic right-shift

                scvtf	d0, w0          // convert integer back to double
                ldr     x0, =one_over_a_hundred // get 0.01
                ldr     d1, [x0]
                fmul	d0, d0, d1      // d0 = d0 * 0.01

                // get score
                adrp	x0, score	// set the adrp address
                add	x0, x0, :lo12:score
                str	d0, [x0]	// update the score with value in d0

                // Get the value in game_board[i][j]
                adrp	x0, game_board	// set the adrp address
                add	x3, x0, :lo12:game_board

                ldrsw	x1, [sp, 28]	// get the value in sp+28, which is i
                ldrsw	x2, [sp, 24]	// get the value in sp+24, which is j

                // Obtain the value of game_board[i][j] and write '!' back game_board[i][j]
                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x3, x0
                add	x0, x0, x2
                mov	w1, '!'
                strb	w1, [x0]

                // Print the information that the player hits the bad surprise pack and lose half of the score
                adrp	x0, lose_half_points	// set the adrp address
                add	x0, x0, :lo12:lose_half_points  // add only the lowest 12 bits of the label to the adrp address
                bl      printf  // Call printf

                b       calculateScore_end

calculateScore_label3:

                // Check if surprise_packs[i][j] == '$'

                // get the value of surprise_packs[i][j]
                adrp	x0, surprise_packs	// set the adrp address
	        add	x3, x0, :lo12:surprise_packs  // add only the lowest 12 bits of the label to the adrp address

                ldrsw	x1, [sp, 28]	// get the value in sp+28, which is i
                ldrsw	x2, [sp, 24]	// get the value in sp+24, which is j

                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x3, x0
                add	x0, x0, x2

                ldrb	w0, [x0]

                cmp     w0, '$'                 // Write '$' back to surprise_packs[i][j]
                b.ne    calculateScore_end

                // get 100.0
                adrp	x0, one_hundred_in_double	// set the adrp address
                add	x0, x0, :lo12:one_hundred_in_double  // add only the lowest 12 bits of the label to the adrp address
                ldr	d1, [x0]

                // Get the score
                adrp	x0, score	// set the adrp address
                add	x0, x0, :lo12:score  // add only the lowest 12 bits of the label to the adrp address
                ldr	d0, [x0]

                fmul	d0, d0, d1	// d0 = d0 * 100.0
                fcvtzs	w0, d0          // convert to integer
                lsl	w0, w0, 1       // mutiply 2 by using left-shift

                scvtf	d0, w0          // convert integer back to double
                ldr     x0, =one_over_a_hundred // get 0.01
                ldr     d1, [x0]
                fmul	d0, d0, d1      // d0 = d0 * 0.01

                // get score
                adrp	x0, score	// set the adrp address
	        add	x0, x0, :lo12:score
	        str	d0, [x0]	// update the score with value in d0

                // Get the value of game_board[i][j]
                adrp	x0, game_board	// set the adrp address
                add	x3, x0, :lo12:game_board  // add only the lowest 12 bits of the label to the adrp address

                ldrsw	x1, [sp, 28]	// get the value in sp+28, which is i
                ldrsw	x2, [sp, 24]	// get the value in sp+24, which is j

                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x3, x0
                add	x0, x0, x2
                mov	w1, '$'
                strb	w1, [x0]	// Write '$' back to the game_board[i][j]

                // Print the information that the player hits the good surprise pack and got the score doubled
                adrp	x0, got_score_doubled	// set the adrp address
	        add	x0, x0, :lo12:got_score_doubled  // add only the lowest 12 bits of the label to the adrp address

                bl      printf  // Call printf

calculateScore_end:

                ldp     fp, lr, [sp], 32        // Restore the state of the frame pointer register and procedure link register by post-incrementing 32 on stack pointer
                ret     // Give the control back to the caller

// int main(int argc, char *argv[])
// the implementation of main function
main:
                stp     fp, lr, [sp, -48]!      // Allocate 48 bytes on the stack, save the contents of frame pointer register and procedure link register in the reserved block
                mov     fp, sp                  // Set frame pointer register to the address of the stack pointer

                str	w0, [sp, 28]    // store argc to sp+28
                str	x1, [sp, 16]    // store argv to sp+16

                // Check if the number of arguments is 3
                ldr     w0, [sp, 28]
                cmp     w0, 3
                b.eq    main_next0

                // Report and quit if the number of arguments is not 3
                adrp	x0, invalid_arguments	// set the adrp address
                add	x0, x0, :lo12:invalid_arguments  // add only the lowest 12 bits of the label to the adrp address
                bl      printf  // Call printf
                mov     w0, 0
                b       main_end

main_next0:
                // Check if player's name is too long
                ldr     x0, [sp, 16]    // get argv
                add     x0, x0, 8       // argv + 8
                ldr     x0, [x0]        // *(argv + 8)
                bl      myStrlen        // call myStrlen
                cmp     w0, MAXIMAL_LENGTH_OF_NAME      // compare the length of the string with MAXIMAL_LENGTH_OF_NAME
                b.le    main_next1

                // Report and quit if the length of name exceeds MAXIMAL_LENGTH_OF_NAME
                adrp	x0, name_too_long	// set the adrp address
                add	x0, x0, :lo12:name_too_long  // add only the lowest 12 bits of the label to the adrp address
                bl      printf  // Call printf
                mov     w0, 0
                b       main_end

main_next1:
                // Check if the dimension of the game board is valid
                ldr     x0, [sp, 16]    // get argv
                add     x0, x0, 16      // argv + 16
                ldr     x0, [x0]        // *(argv + 16)
                bl      isANumber       // Call isANumber
                cmp     w0, 0
                b.ne    main_next2

                // Report and quit if the size of board is not an integer
                adrp	x0, size_of_board_invalid	// set the adrp address
                add	x0, x0, :lo12:size_of_board_invalid  // add only the lowest 12 bits of the label to the adrp address
                bl      printf  // Call printf
                mov     w0, 0
                b       main_end
main_next2:
                // Convert and dimension and store to N
                ldr     x0, [sp, 16]    // get argv
                add     x0, x0, 16      // argv + 16
                ldr     x0, [x0]        // *(argv + 16)
                bl      atoi            // atoi(*(argv + 16))
                mov     w1, w0          // store the result in w1

                // Get N
                adrp	x0, N	// set the adrp address
                add	x0, x0, :lo12:N  // add only the lowest 12 bits of the label to the adrp address
                str     w1, [x0]        // store the value of w1 to N

                cmp     w1, MINIMAL_N   // Compare N and 5
                b.lt    main_next3      // Branch to the label if the less than relationship is hold (N < 5)
                cmp     w1, MAXIMAL_N   // check if N <= 20
                b.le    main_next4

main_next3:
                // Report the size of board is out of range and quit the game
                adrp	x0, size_of_board_out_of_range	// set the adrp address
                add	x0, x0, :lo12:size_of_board_out_of_range   // add only the lowest 12 bits of the label to the adrp address
                bl      printf  // Call printf
                mov     w0, 0
                b       main_end
main_next4:

                // Store the player's name to player_name
                ldr     x0, [sp, 16]    // get argv
                ldr     x1, [x0, 8]     // argv + 8
                adrp	x0, player_name	// set the adrp address
                add	x0, x0, :lo12:player_name         // add only the lowest 12 bits of the label to the adrp address
                str     x1, [x0]
                // Print welcome statements with the player's name
                adrp	x0, player_name	// set the adrp address
                add	x0, x0, :lo12:player_name         // add only the lowest 12 bits of the label to the adrp address
                ldr     x0, [x0]
                mov     x1, x0
                adrp	x0, welcome_message	// set the adrp address
                add	x0, x0, :lo12:welcome_message     // add only the lowest 12 bits of the label to the adrp address
                bl      printf  // Call printf

main_menu_before_game:

                // Show the menu before game
                adrp	x0, menu_before_game	// set the adrp address
                add	x0, x0, :lo12:menu_before_game    // add only the lowest 12 bits of the label to the adrp address
                bl      printf  // Call printf

                // Locate the local variable 'select'
                add     x0, sp, 44
                mov     x1, x0
                adrp	x0, scanf_one_number	// set the adrp address
                add	x0, x0, :lo12:scanf_one_number    // add only the lowest 12 bits of the label to the adrp address
                bl      scanf  // Call scanf

                // Check the value of 'select'
                ldr	w0, [sp, 44]    // get select

                // Check if select == 1
                cmp     w0, 1
                b.eq    main_game_loop

                // Check if select == 2, check top scores and then return to the menu
                cmp     w0, 2
                b.ne    main_game_before_loop0
                bl      checkTopScores    // call subroutine checkTopScores
                b       main_menu_before_game

main_game_before_loop0:

                // Check if select == 3, exit the game
                cmp     w0, 3
                b.ne    main_game_before_loop1
                bl      exitGame    // call subroutine exitGame()
                mov     w0, 0
                b       main_end

main_game_before_loop1:

                adrp	x0, invalid_option	// set the adrp address
                add	x0, x0, :lo12:invalid_option      // add only the lowest 12 bits of the label to the adrp address
                bl      printf  // Call printf
                b       main_menu_before_game

main_game_loop:

                // srand(time(NULL))
                mov     x0, 0
                bl      time    // Call time()
                bl      srand   // Call srand()

                // Call initializeGame(game_board) and pass by pointer
                adrp	x0, game_board	// set the adrp address
                add	x0, x0, :lo12:game_board
                bl      initializeGame
                // Call displayGame(game_board) and pass by pointer
                adrp	x0, game_board	// set the adrp address
                add	x0, x0, :lo12:game_board
                bl      displayGame

main_game_loop_label0:

                // Check if signOfAFloat(score) <= 0
                adrp	x0, score	// set the adrp address
                add	x0, x0, :lo12:score  // add only the lowest 12 bits of the label to the adrp address
                ldr     d0, [x0]
                bl      signOfAFloat    // call subroutine signOfAFloat
                cmp     w0, 0
                b.gt    main_game_loop_label1   // Branch to the label if the greater than relationship is hold

                // get N
                adrp	x0, N	// set the adrp address
                add	x0, x0, :lo12:N   // add only the lowest 12 bits of the label to the adrp address
                ldr     w1, [x0]
                mul     w0, w1, w1      // calculate N * N
                sub	w1, w0, 1	// calculate N * N - 1

                // Check if N * N - 1 <= unvisited_cell_counter
                adrp	x0, unvisited_cell_counter	// set the adrp address
                add	x0, x0, :lo12:unvisited_cell_counter      // add only the lowest 12 bits of the label to the adrp address
                ldr     w0, [x0]
                cmp     w1, w0
                b.le    main_game_loop_label1

                // Print score if score <= 0, save score and return to the menu after game
                adrp	x0, score	// set the adrp address
                add	x0, x0, :lo12:score       // add only the lowest 12 bits of the label to the adrp address
                ldr     d0, [x0]

                adrp	x0, game_end_1	// set the adrp address
                add	x0, x0, :lo12:game_end_1 // add only the lowest 12 bits of the label to the adrp address
                bl      printf  // Call printf

                bl      logScore    // call subroutine logScore

                adrp	x0, return_to_game_menu	// set the adrp address
                add	x0, x0, :lo12:return_to_game_menu         // add only the lowest 12 bits of the label to the adrp address
                bl      printf  // Call printf
                b       main_menu_after_game    // Turn to the menu after game

main_game_loop_label1:

                // Check if unvisited_cell_counter == 0
                adrp	x0, unvisited_cell_counter	// set the adrp address
                add	x0, x0, :lo12:unvisited_cell_counter      // add only the lowest 12 bits of the label to the adrp address
                ldr     w0, [x0]
                cmp     w0, 0
                b.ne    main_game_loop_label2  // jump to the label if unvisited_cell_counter != 0

                // Otherwise, print score, save score and return to the menu after game
                adrp	x0, score	// set the adrp address
                add	x0, x0, :lo12:score       // add only the lowest 12 bits of the label to the adrp address
                ldr     d0, [x0]

                adrp	x0, game_end_2	// set the adrp address
                add	x0, x0, :lo12:game_end_2          // add only the lowest 12 bits of the label to the adrp address
                bl      printf  // Call printf

                bl      logScore    // call subroutine logScore

                adrp	x0, return_to_game_menu	// set the adrp address
                add	x0, x0, :lo12:return_to_game_menu       // add only the lowest 12 bits of the label to the adrp address
                bl      printf  // Call printf
                b       main_menu_after_game

main_game_loop_label2:

                // Otherwise, ask the position for the next move
                adrp	x0, ask_for_a_move	// set the adrp address
                add	x0, x0, :lo12:ask_for_a_move    // add only the lowest 12 bits of the label to the adrp address

                bl      printf  // Call printf
                add     x0, sp, 40      // locate the local variable i
                add     x1, sp, 36      // locate the local variable j
                mov     x2, x1
                mov	x1, x0

                adrp	x0, scanf_two_numbers	// set the adrp address
                add	x0, x0, :lo12:scanf_two_numbers    // add only the lowest 12 bits of the label to the adrp address
                bl      scanf  // Call scanf

                // Check if i == -1
                ldr     w0, [sp, 40]    // get the local variable i
                cmp     w0, -1
                b.ne    main_game_loop_label3
                ldr     w0, [sp, 36]    // get the local variable j
                // Check if j == -1
                cmp     w0, -1
                b.ne    main_game_loop_label3
                bl      exitGame    // call subroutine exitGame
                mov     w0, 0
                b       main_end

main_game_loop_label3:

                ldr	w0, [sp, 40]    // get the local variable i
                cmp	w0, 0           // check if i < 0
                b.lt	main_game_loop_label4        // Branch to the label if the less than relationship is hold (i < 0)

                ldr	w1, [sp, 40]    // get the local variable i
                adrp	x0, N	// set the adrp address
                add	x0, x0, :lo12:N
                ldr     w0, [x0]
                cmp	w1, w0          // check if i >= N
                b.ge    main_game_loop_label4

                // Check if j < 0
                ldr	w0, [sp, 36]
                cmp	w0, 0
                b.lt	main_game_loop_label4        // Branch to the label if the less than relationship is hold (j < 0)

                // Check if j < N
                ldr	w1, [sp, 36]
                ldr     x0,=N
                ldr	w0, [x0]
                cmp	w1, w0
                b.lt	main_game_loop_label5        // Branch to the label if the less than relationship is hold (j < N)

main_game_loop_label4:

                adrp	x0, invalid_move        // set the adrp address
                add	x0, x0, :lo12:invalid_move      // add only the lowest 12 bits of the label to the adrp address
                bl      printf  // Call printf
                b       main_game_loop_label10

main_game_loop_label5:

                // Check if is_visited[i][j] == VISITED
                ldr	w1, [sp, 40]	// get i
                ldr	w3, [sp, 36]	// get j

                adrp	x0, is_visited	// set the adrp address
                add	x2, x0, :lo12:is_visited      // add only the lowest 12 bits of the label to the adrp address

                sxtw	x1, w1	// extend i
                sxtw	x3, w3	// extend j

                // Obtain the value is_visited[i][j]
                mov	x0, x1
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x0, x3
                ldr	w0, [x2, x0, lsl 2]

                cmp	w0, VISITED
                b.ne	main_game_loop_label6

                adrp	x0, position_visited	// set the adrp address
                add	x0, x0, :lo12:position_visited  // add only the lowest 12 bits of the label to the adrp address
                bl      printf  // Call printf

                b	main_game_loop_label10

main_game_loop_label6:

                // Check if unvisited_cell_counter == N * N
                adrp	x0, N	// set the adrp address
                add	x0, x0, :lo12:N
                ldr	w1, [x0]
                mul	w1, w1, w1

                // get unvisited_cell_counter
                adrp	x0, unvisited_cell_counter	// set the adrp address
                add	x0, x0, :lo12:unvisited_cell_counter      // add only the lowest 12 bits of the label to the adrp address
                ldr	w0, [x0]

                cmp	w1, w0
                b.ne	main_game_loop_label7

                // Get start_time and pass to the time()
                adrp	x0, start_time	// set the adrp address
                add	x0, x0, :lo12:start_time
                bl	time

                adrp	x0, start_time	// set the adrp address
                add	x0, x0, :lo12:start_time
                ldr	x1, [x0]        // add only the lowest 12 bits of the label to the adrp address

                // Get last time
                adrp	x0, last_time	// set the adrp address
                add	x0, x0, :lo12:last_time // add only the lowest 12 bits of the label to the adrp address
                str	x1, [x0]	// last_time = start_time
                b	main_game_loop_label8

main_game_loop_label7:

                // Get current_time by passing to time() by pointer
                adrp	x0, current_time	// set the adrp address
                add	x0, x0, :lo12:current_time      // add only the lowest 12 bits of the label to the adrp address
                bl	time

                // Get the updated current_time
                adrp	x0, current_time	// set the adrp address
                add	x0, x0, :lo12:current_time      // add only the lowest 12 bits of the label to the adrp address
                ldr	x2, [x0]

                // Get last_time
                adrp	x0, last_time	// set the adrp address
                add	x0, x0, :lo12:last_time // add only the lowest 12 bits of the label to the adrp address
                ldr	x0, [x0]

                // Pass the value of current_time and last_time to difftime
                mov	x1, x0
                mov	x0, x2
                bl	difftime

                // Convert the result of difftime from double to int
                fcvtzs	w1, d0

                // Store the result of difftime to time_elapsed
                adrp	x0, time_elapsed	// set the adrp address
                add	x0, x0, :lo12:time_elapsed      // add only the lowest 12 bits of the label to the adrp address
                str	w1, [x0]

                // Get current_time
                adrp	x0, current_time	// set the adrp address
                add	x0, x0, :lo12:current_time      // add only the lowest 12 bits of the label to the adrp address
                ldr	x1, [x0]

                // Get last_time
                adrp	x0, last_time	// set the adrp address
                add	x0, x0, :lo12:last_time // add only the lowest 12 bits of the label to the adrp address
                // store the value of current_time to last_time
                str	x1, [x0]

                // get remaining_time
                adrp	x0, remaining_time	// set the adrp address
                add	x0, x0, :lo12:remaining_time    // add only the lowest 12 bits of the label to the adrp address
                ldr	w1, [x0]
                // get time_elapsed
                adrp	x0, time_elapsed	// set the adrp address
                add	x0, x0, :lo12:time_elapsed      // add only the lowest 12 bits of the label to the adrp address
                // calculate remaining_time - time_elapsed
                ldr	w0, [x0]
                sub	w1, w1, w0
                // store the result to remaining_time
                adrp	x0, remaining_time	// set the adrp address
                add	x0, x0, :lo12:remaining_time    // add only the lowest 12 bits of the label to the adrp address
                str	w1, [x0]

main_game_loop_label8:
                // Get remaining_time
                adrp	x0, remaining_time	// set the adrp address
                add	x0, x0, :lo12:remaining_time    // add only the lowest 12 bits of the label to the adrp address
                ldr	w0, [x0]
                // Check if remaining_time > 0, otherwise, game ends
                cmp	w0, 0
                b.gt	main_game_loop_label9   // Branch to the label if the greater than relationship is hold
                // Get and print score
                adrp	x0, score	// set the adrp address
                add	x0, x0, :lo12:score     // add only the lowest 12 bits of the label to the adrp address
                ldr	d0, [x0]
                adrp	x0, game_end_4	// set the adrp address
                add	x0, x0, :lo12:game_end_4        // add only the lowest 12 bits of the label to the adrp address
                bl	printf  // Call printf

                bl	logScore    // call subroutine logScore

                // Return to the game menu
                adrp	x0, return_to_game_menu	// set the adrp address
                add	x0, x0, :lo12:return_to_game_menu       // add only the lowest 12 bits of the label to the adrp address
                bl	printf  // Call printf
                b	main_menu_after_game
main_game_loop_label9:
                // Get local variables i, j and pass them to calculateScore()
                ldr	w0, [sp, 40]	// i
                ldr	w1, [sp, 36]	// j
                bl	calculateScore
                // Get the value of is_visited[i][j]
                ldr	w1, [sp, 40]	// i
                ldr	w3, [sp, 36]	// j
                adrp	x0, is_visited	// set the adrp address
                add	x2, x0, :lo12:is_visited
                sxtw	x3, w3	// extend j
                sxtw	x1, w1	// extend i
                mov	x0, x1	// calculate the offset
                lsl	x0, x0, 1
                add	x0, x0, x1
                lsl	x0, x0, 3
                add	x0, x0, x1
                add	x0, x0, x3
                mov	w1, VISITED
                str	w1, [x2, x0, lsl 2]	// set is_visited[i][j] to VISITED
                adrp	x0, unvisited_cell_counter	// set the adrp address
                add	x0, x0, :lo12:unvisited_cell_counter
                ldr	w0, [x0]        // get the value of unvisited_cell_counter
                sub	w1, w0, 1	// unvisited_cell_counter - 1
                adrp	x0, unvisited_cell_counter	// set the adrp address
                add	x0, x0, :lo12:unvisited_cell_counter
                str	w1, [x0]	// copy the new value to the address of unvisited_cell_counter
                adrp	x0, game_board	// set the adrp address
                add	x0, x0, :lo12:game_board
                bl	displayGame // pass to displayGame() by pointer
main_game_loop_label10:
                b	main_game_loop_label0   // jump to main_game_loop_label0
main_menu_after_game:
                // Print the menu after game
                adrp	x0, menu_after_game	// set the adrp address
                add	x0, x0, :lo12:menu_after_game   // add only the lowest 12 bits of the label to the adrp address
                bl	printf  // Call printf
                // Get the local variable 'select' and pass to scanf()
                add	x0, sp, 44
                mov	x1, x0
                adrp	x0, scanf_one_number	// set the adrp address
                add	x0, x0, :lo12:scanf_one_number  // add only the lowest 12 bits of the label to the adrp address
                bl	scanf  // Call scanf
                // Check if the value of 'select' == 1
                ldr	w0, [sp, 44]
                cmp	w0, 1
                b.ne	main_menu_after_game_label0    // if select != 1, jump to the label
                bl	checkTopScores    // call subroutine checkTopScores
                b	main_menu_after_game		// return to the menu after checking top scores
main_menu_after_game_label0:
                // Check if the value of 'select' == 2
                ldr	w0, [sp, 44]
                cmp	w0, 2
                b.ne	main_menu_after_game_label1		// if select != 2, jump to the label
                bl	exitGame    // call subroutine exitGame
                mov	w0, 0
                b	main_end
main_menu_after_game_label1:
                // Report if the player enters an invalid option
                adrp	x0, invalid_option	// set the adrp address
                add	x0, x0, :lo12:invalid_option    // add only the lowest 12 bits of the label to the adrp address
                bl	printf  // Call printf
                b	main_menu_after_game		// jump back to the menu after game
main_end:
                ldp     fp, lr, [sp], 48        // Restore the state of the frame pointer register and procedure link register by post-incrementing 48 on stack pointer
                ret     // Give the control back to the caller
