/**
 * File:            assign1a.s
 * Author:          Haohu Shen
 * UCID:            30063099
 * Course Number:   CPSC355
 * Instructor:      Tamer Jarada
 * Date:            2019/05/12
 * Description:
 *
 * Assignment1 Version1: Compute the maximum y of the polynomial where
 *
 *          y = -5x^3 - 31x^2 + 4x + 31
 *
 * within the range of x from -6 to 5 where x is an integer.
 * The algorithm is stepping through the range one by one in a loop and testing.
 * Meanwhile, the values of x, y, and the maximum of y on the current loop will
 * be printed after each iteration.
 *
 * For version1, macros are not allowed, only mul, add, mov are allowed for calculations.
 * Loop test must be at the top of the loop.
 */

fmt:    .string     "If x = %2d, then y = %5d and the current maximum of y is %3d.\n" // Define the output string

        .balign     4                           // Ensure following instructions are properly aligned
        .global     main                        // Set the "main" label be visible to the linker

main:       stp     x29, x30, [sp, -16]!        // Save Frame Pointer and Link Register to the Stack Pointer
            mov     x29, sp                     // Set FP to the address of the Stack Pointer

            mov     x19, -2147483648            // Set the initial value of y as the mininum value for a signed int
                                                // when being compiled by GNU C17 (GCC) version 8.2.1 20181215
                                                // (Red Hat 8.2.1-6) (aarch64-redhat-linux) in register x19

            mov     x20, x19                    // Set the initial value of current maximum y the same as that in x19
            mov     x21, -6                     // Set the initial value of x as -6 in register x21
            mov     x25, -5                     // Set the coefficient of x*x*x as -5 in register x25
            mov     x26, -31                    // Set the coefficient of x*x as -31 in register x26
            mov     x27, 4                      // Set the coefficient of x as 4 in register x27

loop_test:  cmp     x21, 5                      // Compare x in x21 and 5
            b.gt    ret                         // Branch to label 'ret' if x > 5

loop:       mov     x19, 31                     // Set value of y in x19 to 31 at the beginning of every iteration

            /* Compute -5*x*x*x and add to y */
            mul     x22, x21, x21               // Calculate x*x and store in register x22
            mul     x23, x22, x21               // Multiply the value in x22 and x then store in register x23
            mul     x24, x23, x25               // Calculate the value in x23 * (-5) and store in register x24
            add     x19, x19, x24               // Add -5*x*x*x in x24 to y in x19

            /* Compute -31*x*x and add to y */
            mul     x23, x22, x26               // Multiply the value in x22 and -31 in x26 then store in x23
            add     x19, x19, x23               // Add -31*x*x in x23 to y in x19

            /* Compute +4*x and add to y */
            mul     x22, x21, x27               // Multiply the value in x21 and 4 in x27 then store in x22
            add     x19, x19, x22               // Add 4*x in x22 to y in x19

cmp_y:      cmp     x19, x20                    // Compare y in x19 and the current maximum of y in x20
            b.le    output                      // Branch to label 'output' if y <= maximum

update:     mov     x20, x19                    // Update maximum to the current y

output:     adrp    x0, fmt                     // Set fmt as the 1st argument of 'printf'
            add     x0, x0, :lo12:fmt           // Add the low 12 bits to x0
            mov     x1, x21                     // Set the value of x in x21 as the 2nd argument of 'printf'
            mov     x2, x19                     // Set the value of y in x19 as the 3rd argument of 'printf'
            mov     x3, x20                     // Set the value of maximum y in x20 as the 4th argument of 'printf'
            bl      printf                      // Call printf

            add     x21, x21, 1                 // Update x in x21 by increasing 1
            b       loop_test                   // Branch to label 'loop_test' for next iteration

ret:        mov     w0, 0			// Return 0 for 'main' function
	    ldp     x29, x30, [sp], 16          // Restore the state of the FP and LR and post-incrementing SP by +16
            ret                                 // Return control to OS
