/**
 * CPSC359 Assignment 4:
 * This program uses the Raspberry to present a maze on the HDMI monitor.
 *  It uses a dark color for the walls of the maze, a lighter color for the
 *  pathways through the maze, and red for the player that moves through the
 *  maze on the pathways with an SNES controller. When the player reaches the
 *  exit of the maze, it will redisplay the player in green to indicate that
 *  the maze has been solved and the game is over. The program has been tested
 *  in Windows10 x64 1909.
 *
 * Notice before grading:
 * 1. I referred some code comments and code format
 *    from the code in D2L and tutorials, in order to improve the quality and
 *    readability.
 *
 * 2. Since I adjusted and added some code in original framebuffer.c and
 *    framebuffer.h, make sure not to replace the header or source files I
 *    uploaded.
 *
 * Author: Haohu Shen
 * UCID: 30063099
 */

 // Included header files
#include "uart.h"
#include "gpio.h"
#include "framebuffer.h"
#include "systimer.h"
#include "mailbox.h"
#include "snes_uart.h"

/** Constants **/
// Boolean value
#define TRUE 1
#define FALSE 0

// Size
#define WIDTH  16
#define HEIGHT 12
#define SIZE_OF_SQUARE 64

// Space type
#define WALL 1
#define PATH 0
#define EXIT 3

// Original position
#define START_I 2
#define START_J 0

// Macros of RGB color codes
#define WHITE     0x00FFFFFF
#define RED       0x00FF0000
#define GRAY      0x00808080
#define GREEN     0x00008000

// Mapping SNES buttons to hex numbers
#define START     0x00000008
#define UP        0x00000010
#define DOWN      0x00000020
#define LEFT      0x00000040
#define RIGHT     0x00000080
#define NO_INPUT  0x00000000

/** Global variables **/
int MAZE[HEIGHT][WIDTH] = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1},
        {2, 0, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 1, 0, 1},
        {1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1},
        {1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1},
        {1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1},
        {1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0, 1},
        {1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1},
        {1, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 3},
        {1, 1, 0, 1, 0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 1},
        {1, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
};
int current_maze[HEIGHT][WIDTH];

// The player's position
int player_i;
int player_j;

// The status indicates if the player will be shown on the screen
int show_player;

// The status of the game
int game_end;
int game_start;

// SNES Controller
unsigned short current_state;



////////////////////////////////////////////////////////////////////////////////
//
//  Function:       set_GPIO11
//
//  Arguments:      none
//
//  Returns:        void
//
//  Description:    This function sets the GPIO output pin 11
//                  to a 1 (high) level.
//
////////////////////////////////////////////////////////////////////////////////

void set_GPIO11()
{
    register unsigned int r;

    // Put a 1 into the SET11 field of the GPIO Pin Output Set Register 0
    r = (0x1 << 11);
    *GPSET0 = r;
}

////////////////////////////////////////////////////////////////////////////////
//
//  Function:       clear_GPIO9
//
//  Arguments:      none
//
//  Returns:        void
//
//  Description:    This function clears the GPIO output pin 9
//                  to a 0 (low) level.
//
////////////////////////////////////////////////////////////////////////////////

void clear_GPIO9()
{
    register unsigned int r;
    // Put a 1 into the CLR9 field of the GPIO Pin Output Clear Register 0
    r = (0x1 << 9);
    *GPCLR0 = r;
}

////////////////////////////////////////////////////////////////////////////////
//
//  Function:       init_GPIO9_to_output
//
//  Arguments:      none
//
//  Returns:        void
//
//  Description:    This function sets GPIO pin 9 to an output pin without
//                  any pull-up or pull-down resistors.
//
////////////////////////////////////////////////////////////////////////////////

void init_GPIO9_to_output()
{
    register unsigned int r;

    // Get the current contents of the GPIO Function Select Register 0
    r = *GPFSEL0;

    // Clear bits 27 - 29. This is the field FSEL9, which maps to GPIO pin 9.
    // We clear the bits by ANDing with a 000 bit pattern in the field.
    r &= ~(0x7 << 27);

    // Set the field FSEL9 to 001, which sets pin 9 to an output pin.
    // We do so by ORing the bit pattern 001 into the field.
    r |= (0x1 << 27);

    // Write the modified bit pattern back to the
    // GPIO Function Select Register 0
    *GPFSEL0 = r;

    // Disable the pull-up/pull-down control line for GPIO pin 9. We follow the
    // procedure outlined on page 101 of the BCM2837 ARM Peripherals manual. The
    // internal pull-up and pull-down resistor isn't needed for an output pin.

    // Disable pull-up/pull-down by setting bits 0:1
    // to 00 in the GPIO Pull-Up/Down Register
    *GPPUD = 0x0;

    // Wait 150 cycles to provide the required set-up time
    // for the control signal
    r = 150;
    while (r--) {
        asm volatile("nop");
    }

    // Write to the GPIO Pull-Up/Down Clock Register 0, using a 1 on bit 9 to
    // clock in the control signal for GPIO pin 9. Note that all other pins
    // will retain their previous state.
    *GPPUDCLK0 = (0x1 << 9);

    // Wait 150 cycles to provide the required hold time
    // for the control signal
    r = 150;
    while (r--) {
        asm volatile("nop");
    }

    // Clear all bits in the GPIO Pull-Up/Down Clock Register 0
    // in order to remove the clock
    *GPPUDCLK0 = 0;
}

////////////////////////////////////////////////////////////////////////////////
//
//  Function:       init_GPIO11_to_output
//
//  Arguments:      none
//
//  Returns:        void
//
//  Description:    This function sets GPIO pin 11 to an output pin without
//                  any pull-up or pull-down resistors.
//
////////////////////////////////////////////////////////////////////////////////

void init_GPIO11_to_output()
{
    register unsigned int r;


    // Get the current contents of the GPIO Function Select Register 1
    r = *GPFSEL1;

    // Clear bits 3 - 5. This is the field FSEL11, which maps to GPIO pin 11.
    // We clear the bits by ANDing with a 000 bit pattern in the field.
    r &= ~(0x7 << 3);

    // Set the field FSEL11 to 001, which sets pin 9 to an output pin.
    // We do so by ORing the bit pattern 001 into the field.
    r |= (0x1 << 3);

    // Write the modified bit pattern back to the
    // GPIO Function Select Register 1
    *GPFSEL1 = r;

    // Disable the pull-up/pull-down control line for GPIO pin 11. We follow the
    // procedure outlined on page 101 of the BCM2837 ARM Peripherals manual. The
    // internal pull-up and pull-down resistor isn't needed for an output pin.

    // Disable pull-up/pull-down by setting bits 0:1
    // to 00 in the GPIO Pull-Up/Down Register
    *GPPUD = 0x0;

    // Wait 150 cycles to provide the required set-up time
    // for the control signal
    r = 150;
    while (r--) {
        asm volatile("nop");
    }

    // Write to the GPIO Pull-Up/Down Clock Register 0, using a 1 on bit 11 to
    // clock in the control signal for GPIO pin 11. Note that all other pins
    // will retain their previous state.
    *GPPUDCLK0 = (0x1 << 11);

    // Wait 150 cycles to provide the required hold time
    // for the control signal
    r = 150;
    while (r--) {
        asm volatile("nop");
    }

    // Clear all bits in the GPIO Pull-Up/Down Clock Register 0
    // in order to remove the clock
    *GPPUDCLK0 = 0;
}

////////////////////////////////////////////////////////////////////////////////
//
//  Function:       init_GPIO10_to_input
//
//  Arguments:      none
//
//  Returns:        void
//
//  Description:    This function sets GPIO pin 10 to an input pin without
//                  any internal pull-up or pull-down resistors. Note that
//                  a pull-down (or pull-up) resistor must be used externally
//                  on the bread board circuit connected to the pin. Be sure
//                  that the pin high level is 3.3V (definitely NOT 5V).
//
////////////////////////////////////////////////////////////////////////////////

void init_GPIO10_to_input()
{
    register unsigned int r;


    // Get the current contents of the GPIO Function Select Register 1
    r = *GPFSEL1;

    // Clear bits 0 - 2. This is the field FSEL10, which maps to GPIO pin 10.
    // We clear the bits by ANDing with a 000 bit pattern in the field. This
    // sets the pin to be an input pin.
    r &= ~(0x7 << 0);

    // Write the modified bit pattern back to the
    // GPIO Function Select Register 1
    *GPFSEL1 = r;

    // Disable the pull-up/pull-down control line for GPIO pin 10. We follow the
    // procedure outlined on page 101 of the BCM2837 ARM Peripherals manual. We
    // will pull down the pin using an external resistor connected to ground.

    // Disable internal pull-up/pull-down by setting bits 0:1
    // to 00 in the GPIO Pull-Up/Down Register
    *GPPUD = 0x0;

    // Wait 150 cycles to provide the required set-up time
    // for the control signal
    r = 150;
    while (r--) {
        asm volatile("nop");
    }

    // Write to the GPIO Pull-Up/Down Clock Register 0, using a 1 on bit 10 to
    // clock in the control signal for GPIO pin 10. Note that all other pins
    // will retain their previous state.
    *GPPUDCLK0 = (0x1 << 10);

    // Wait 150 cycles to provide the required hold time
    // for the control signal
    r = 150;
    while (r--) {
        asm volatile("nop");
    }

    // Clear all bits in the GPIO Pull-Up/Down Clock Register 0
    // in order to remove the clock
    *GPPUDCLK0 = 0;
}

////////////////////////////////////////////////////////////////////////////////
//
//  Function:       reset_game
//
//  Arguments:      none
//
//  Returns:        void
//
//  Description:    This function resets the game to the original status.
//
////////////////////////////////////////////////////////////////////////////////

void reset_game() {
    uart_puts("Congratulations! You reached the exit! Now you can press H to restart the game...\n");
    while (1) {
        current_state = get_SNES();
        if (current_state == START) {
            // Reset the game status
            player_i = START_I;
            player_j = START_J;
            game_start = FALSE;
            game_end = FALSE;
            show_player = TRUE;
            // Reset the current maze
            for (int i = 0; i < HEIGHT; ++i) {
                for (int j = 0; j < WIDTH; ++j) {
                    current_maze[i][j] = MAZE[i][j];
                }
            }
            displayFrameBufferForMaze(current_maze, show_player, game_end, player_i, player_j);
            break;
        }
    }
}

////////////////////////////////////////////////////////////////////////////////
//
//  Function:       valid_move
//
//  Arguments:      input: An input given from the user
//
//  Returns:        void
//
//  Description:    This function returns 1 if the user gives a W/A/S/D, otherwise
//                  returns 0.
//
////////////////////////////////////////////////////////////////////////////////

int valid_move(unsigned short input)
{
    if (input == UP || input == DOWN || input == LEFT || input == RIGHT) {
        return TRUE;
    }
    return FALSE;
}

////////////////////////////////////////////////////////////////////////////////
//
//  Function:       main
//
//  Arguments:      none
//
//  Returns:        void
//
//  Description:    This function initializes the UART terminal and initializes
//                  a frame buffer for a 1024 x 768 display. Each pixel in the
//                  frame buffer is 32 bits in size, which encodes an RGB value
//                  (plus an 8-bit alpha channel that is not used). The program
//                  then draws and displays an 16 x 12 checker board pattern.
//
////////////////////////////////////////////////////////////////////////////////

void main()
{
    // Initialized global variables
    game_end = FALSE;
    game_start = TRUE;
    show_player = FALSE;

    // Set up the UART serial port
    uart_init();

    // Set up GPIO pin #9 for output (LATCH output)
    init_GPIO9_to_output();

    // Set up GPIO pin #11 for output (CLOCK output)
    init_GPIO11_to_output();

    // Set up GPIO pin #10 for input (DATA input)
    init_GPIO10_to_input();

    // Clear the LATCH line (GPIO 9) to low
    clear_GPIO9();

    // Set CLOCK line (GPIO 11) to high
    set_GPIO11();

    // Initialize the Frame Buffer
    initFrameBuffer();

    // Display the original maze
    displayFrameBufferForMaze(MAZE, show_player, game_end, player_i, player_j);

    // Output to indicate the game is running
    uart_puts("CPSC359 Assignment 4\n\n");
    uart_puts("Press H to start...\n\n");

    // Loop to check if H is pressed
    while (1) {
        // Update the current condition of SNES Controller
        current_state = get_SNES();
        if (current_state == START && game_start) {
            // If the START button is pressed and the game is not starting yet,
            // draw the maze.
            player_i = START_I;
            player_j = START_J;
            game_start = FALSE;
            game_end = FALSE;
            show_player = TRUE;
            // Reset the current maze
            for (int i = 0; i < HEIGHT; ++i) {
                for (int j = 0; j < WIDTH; ++j) {
                    current_maze[i][j] = MAZE[i][j];
                }
            }
            displayFrameBufferForMaze(current_maze, show_player, game_end, player_i, player_j);
            break;
        }
    }

    uart_puts("Operations in the game:\n");
    uart_puts("Press W to move UP\n");
    uart_puts("Press S to move DOWN\n");
    uart_puts("Press A to move LEFT\n");
    uart_puts("Press D to move RIGHT\n");

    while (1) {

        // Update the current condition of SNES Controller
        current_state = get_SNES();
        if (!valid_move(current_state)) {
            continue;
        }
        if (current_maze[player_i][player_j] != EXIT) {
            // Check if the player goes up
            if (current_state == UP) {
                if (player_i > 0 &&
                    current_maze[player_i - 1][player_j] != WALL) {
                    --player_i;
                    // Change the game status if the player reaches the exit
                    if (current_maze[player_i][player_j] == EXIT) {
                        game_start = TRUE;
                        game_end = TRUE;
                    }
                    // Redraw the maze
                    displayFrameBufferForMaze(MAZE, show_player, game_end, player_i, player_j);
                    // Reset the game if the player reaches the exit
                    if (current_maze[player_i][player_j] == EXIT) {
                        reset_game();
                    }
                }
            }
            // Check if the player goes down
            else if (current_state == DOWN) {
                if (player_i < HEIGHT - 1 &&
                    current_maze[player_i + 1][player_j] != WALL) {
                    ++player_i;
                    // Change the game status if the player reaches the exit
                    if (current_maze[player_i][player_j] == EXIT) {
                        game_start = TRUE;
                        game_end = TRUE;
                    }
                    // Redraw the maze
                    displayFrameBufferForMaze(MAZE, show_player, game_end, player_i, player_j);
                    // Reset the game if the player reaches the exit
                    if (current_maze[player_i][player_j] == EXIT) {
                        reset_game();
                    }
                }
            }
            // Check if the player goes left
            else if (current_state == LEFT) {
                if (player_j > 0 &&
                    current_maze[player_i][player_j - 1] != WALL) {
                    --player_j;
                    // Change the game status if the player reaches the exit
                    if (current_maze[player_i][player_j] == EXIT) {
                        game_start = TRUE;
                        game_end = TRUE;
                    }
                    // Redraw the maze
                    displayFrameBufferForMaze(MAZE, show_player, game_end, player_i, player_j);
                    // Reset the game if the player reaches the exit
                    if (current_maze[player_i][player_j] == EXIT) {
                        reset_game();
                    }
                }
            }
            // Check if the player goes right
            else if (current_state == RIGHT) {
                if (player_j < WIDTH - 1 &&
                    current_maze[player_i][player_j + 1] != WALL) {
                    ++player_j;
                    // Change the game status if the player reaches the exit
                    if (current_maze[player_i][player_j] == EXIT) {
                        game_start = TRUE;
                        game_end = TRUE;
                    }
                    // Redraw the maze
                    displayFrameBufferForMaze(MAZE, show_player, game_end, player_i, player_j);
                    // Reset the game if the player reaches the exit
                    if (current_maze[player_i][player_j] == EXIT) {
                        reset_game();
                    }
                }
            }
        }
    }
}
