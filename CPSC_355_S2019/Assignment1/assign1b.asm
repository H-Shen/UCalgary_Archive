/**
 * File:            assign1b.asm
 * Author:          Haohu Shen
 * UCID:            30063099
 * Course Number:   CPSC355
 * Instructor:      Tamer Jarada
 * Date:            2019/05/12
 * Description:
 *
 * Assignment1 Version2: Compute the maximum y of the polynomial where
 *
 *          y = -5x^3 - 31x^2 + 4x + 31
 *
 * within the range of x from -6 to 5 where x is an integer.
 * The algorithm is stepping through the range one by one in a loop and testing.
 * Meanwhile, the values of x, y, and the maximum of y on the current loop will
 * be printed after each iteration.
 *
 * For version2, macros are used to improve code readability, especially for for heavily used registers,
 * madd is used for code optimization and loop test must be at the bottom of the pre-test loop.
 */

define(INT_MIN, -2147483648)    // Minimum value as a signed int, which is -2147483648 when being compiled by GNU C17
                                // (GCC) version 8.2.1 20181215 (Red Hat 8.2.1-6) (aarch64-redhat-linux)

define(LOWER, -6)               // Lowerbound of x
define(UPPER, 5)                // Upperbound of x
define(CO0, x25)                // Use CO0 to represent register x25, which stores the coefficient of x*x*x
define(CO1, x26)                // Use CO1 to represent register x26, which stores the coefficient of x*x
define(CO2, x27)                // Use CO2 to represent register x27, which stores the coefficient of x
define(CO3, 31)                 // Constant of the polynomial
define(y_r, x19)                // Use y_r to represent register x19
define(max_y, x20)              // Use max_y to represent register x20
define(x_r, x21)                // Use x_r to represent register x21
define(x_2, x22)                // Use x_2 to represent register x22, which store the value of x*x
define(x_3, x23)                // Use x_3 to represent register x23, which store the value of x*x*x
define(fp, x29)                 // Frame Pointer
define(lr, x30)                 // Link Register

fmt:        .string "If x = %2d, then y = %5d and the current maximum of y is %3d.\n" // Define the output string

            .balign 4                           // Ensure following instructions are properly aligned
            .global main                        // Set the "main" label be visible to the linker

main:       stp     x29, x30, [sp, -16]!        // Save Frame Pointer and Link Register to the Stack Pointer
            mov     x29, sp                     // Set FP to the address of the Stack Pointer

            mov     y_r, INT_MIN                // Set the initial value of y in y_r as INT_MIN
            mov     max_y, y_r                  // Set the value of current maximum y in max_y the same as that in y_r
            mov     x_r, LOWER                  // Set the initial value of x as LOWER
            mov     CO0, -5                     // Set the coefficient of x*x*x as -5 in register CO0
            mov     CO1, -31                    // Set the coefficient of x*x as -31 in register CO1
            mov     CO2, 4                      // Set the coefficient of x as 4 in register CO2

            b       loop_test                   // Branch to the pre-loop test

loop_top:   mov     y_r, CO3                    // Set value of y in y_r to CO3 at the beginning of every iteration

            mul     x_2, x_r, x_r               // Calculate x*x and store in x_2
            mul     x_3, x_2, x_r               // Multiply x*x in x_2 and x then store in register x_3
            madd    y_r, x_3, CO0, y_r          // Multiply x*x*x in x_3 and -5 in CO0 then add to y in y_r
                                                // which actually computes -5*x*x*x and add to y

            madd    y_r, x_2, CO1, y_r          // Multiply x*x in x_2 and -31 in CO1 then add to y in y_r
                                                // which actually computes -31*x*x and add to y

            madd    y_r, x_r, CO2, y_r          // Multiply x in x_r and 4 in CO2 then add to y in y_r
                                                // which actually computes +4*x and add to y

cmp_y:      cmp     y_r, max_y                  // Compare y in y_r and the current maximum of y in max_y
            b.le    output                      // Branch to label 'output' if y <= the current maximum of y

update:     mov     max_y, y_r                  // Update maximum to the current y

output:     adrp    x0, fmt                     // Set fmt as the 1st argument of 'printf'
            add     x0, x0, :lo12:fmt           // Add the low 12 bits to x0
            mov     x1, x_r                     // Set the value of x in x_r as the 2nd argument of 'printf'
            mov     x2, y_r                     // Set the value of y in y_r as the 3rd argument of 'printf'
            mov     x3, max_y                   // Set the value of maximum y in max_y as the 4th argument of 'printf'
            bl      printf                      // Call printf
            add     x_r, x_r, 1                 // Update x in x_r by increasing 1

loop_test:  cmp     x_r, UPPER                  // Compare x in x_r and the upperbound of x, which is 5
            b.le    loop_top                    // Branch to label 'loop_top' if x <= 5

ret:        mov     w0, 0			// Return 0 for 'main' function
	    ldp     x29, x30, [sp], 16          // Restore the state of the FP and LR and post-incrementing SP by +16
            ret                                 // Return control to OS
