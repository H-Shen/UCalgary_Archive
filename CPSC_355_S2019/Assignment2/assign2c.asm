/**
 * File:            assign2c.asm
 * Author:          Haohu Shen
 * UCID:            30063099
 * Course Number:   CPSC355
 * Instructor:      Tamer Jarada
 * Date:            2019/05/22
 * Description:
 *
 * Assignment2 Version3: Integer Multiplication using Add and Shift Operations
 *
 * Implements the C Code given for integer multiplication in ARMv8 assembly code.
 * 32-bit registers must be used for variables declared using 'int', while 64-bit
 * registers for 'long int', m4 macros will also be used to make the code more
 * readable. In version3, the initial value of multiplicand is -252645136, the initial
 * value of multiplier is -256.
 */

fmt0:       .string "multiplier = 0x%08x (%d) multiplicand = 0x%08x (%d)\n\n"   // The string for printing out initial values of variables
fmt1:       .string "product = 0x%08x multiplier = 0x%08x\n"                    // The string for printing out product and multiplier
fmt2:       .string "64-bit result = 0x%016lx (%ld)\n"                          // The string for printing out 64-bit result

            .balign 4                                                           // Ensure following instructions are properly aligned
            .global main                                                        // Set the "main" label be visible to the linker

define(FALSE, 0)                                                                // A constant represents the status of FALSE, whose value is 0
define(TRUE, 1)                                                                 // A constant represents the status of TRUE, whose value is 1
define(LOWER, 0)                                                                // A constant represents the lowerbound of the for-loop counter, whose value is 0
define(UPPER, 32)                                                               // A constant represents the upperbound of the for-loop counter, whose value is 32

define(multiplier_r, w19)                                                       // Use multiplier_r to represent register w19
define(multiplicand_r, w20)                                                     // Use multiplicand_r to represent register w20
define(product_r, w21)                                                          // Use product_r to represent register w21
define(i_r, w22)                                                                // Use i_r to represent register w22
define(negative_r, w23)                                                         // Use negative_r to represent register w23
define(result_r, x19)                                                           // Use result_r to represent register x19
define(temp1_r, x20)                                                            // Use temp1_r to represent register x20
define(temp2_r, x21)                                                            // Use temp2_r to represent register x21
define(fp, x29)                                                                 // Frame Pointer
define(lr, x30)                                                                 // Link Register

main:       stp     fp, lr, [sp, -16]!                                          // Save Frame Pointer and Link Register to the Stack Pointer
            mov     fp, sp                                                      // Set FP to the address of the Stack Pointer

            // Initialize variables
            mov     multiplicand_r, -252645136                                  // Set the value in multiplicand_r as -252645136
            mov     multiplier_r, -256                                          // Set the value in multiplier_r as -256
            mov     product_r, 0                                                // Set the value in product_r as 0

output0:    // Print out initial values of variables
            ldr     x0, =fmt0                                                   // Set fmt0 as the 1st argument of 'printf'
            mov     w1, multiplier_r                                            // Set the value in multiplier_r as the 2nd argument of 'printf'
            mov     w2, multiplier_r                                            // Set the value in multiplier_r as the 3rd argument of 'printf'
            mov     w3, multiplicand_r                                          // Set the value in multiplicand_r as the 4th argument of 'printf'
            mov     w4, multiplicand_r                                          // Set the value in multiplicand_r as the 5th argument of 'printf'
            bl      printf                                                      // Call printf

            // Determine if multiplier is negative
            mov     negative_r, TRUE                                            // Set the value in negative_r as TRUE
            cmp     multiplier_r, 0                                             // Compare the value in multiplier_r and 0
            b.lt    next0                                                       // Branch to label 'next0' if the value in multiplier_r < 0

neg_false:  mov     negative_r, FALSE                                           // Set the value in negative_r as FALSE

next0:      // Do repeated add and shift
            mov     i_r, LOWER                                                  // Set the value in i_r as LOWER
            b       loop_test                                                   // Branch to label 'loop_test' for the pre-loop test

loop:       tst     multiplier_r, 0x1                                           // Bitwise AND of the value in multiplier_r and 0x1
                                                                                // also setting the condition flags

            b.eq    next1                                                       // Branch to label 'next1' if (multiplier & 0x1) is false
            add     product_r, product_r, multiplicand_r                        // Add the value in multiplicand_r to product_r

next1:      // Arithmetic shift right the combined product and multiplier
            asr     multiplier_r, multiplier_r, 1                               // Arithmetic shift right on the value in multiplier_r by 1
            tst     product_r, 0x1                                              // Bitwise AND of the value in product_r and 0x1
                                                                                // also setting the condition flags

            b.eq    next2                                                       // Branch to label 'next2' if (product & 0x1) is false
            orr     multiplier_r, multiplier_r, 0x80000000                      // Bitwise OR the value in multiplier_r and 0x80000000
                                                                                // then result in multiplier_r

            b       next3                                                       // Branch to label 'next3'

next2:      and     multiplier_r, multiplier_r, 0x7FFFFFFF                      // Bitwise AND of the value in multiplier_r and 0x7FFFFFFF
                                                                                // then result in multiplier_r

next3:      asr     product_r, product_r, 1                                     // Arithmetic shift right on the value in product_r by 1
            add     i_r, i_r, 1                                                 // Update the value in i_r by increasing 1

loop_test:  cmp     i_r, UPPER                                                  // Compare the value in i_r and UPPER, which is 32
            b.lt    loop                                                        // Branch to the label 'loop' if the value in i_r is less than 32

next4:      // Adjust product register if multiplier is negative
            cmp     negative_r, TRUE                                            // Compare the value in negative_r and TRUE, which is 1
            b.ne    output1                                                     // Branch to label 'output1' if the value in negative_r is not 1
            sub     product_r, product_r, multiplicand_r                        // Subtract the value in multiplicand_r from product_r

output1:    // Print out product and multiplier
            ldr     x0, =fmt1                                                   // Set fmt1 as the 1st argument of 'printf'
            mov     w1, product_r                                               // Set the value in product_r as the 2nd argument of 'printf'
            mov     w2, multiplier_r                                            // Set the value in multiplier_r as the 3rd argument of 'printf'
            bl      printf                                                      // Call printf

combine:    // Combine product and multiplier together
            sxtw    temp1_r, product_r                                          // Sign extend product_r and put it in temp1_r
            and     temp1_r, temp1_r, 0xFFFFFFFF                                // Bitwise AND of the value in temp1_r and 0xFFFFFFFF
                                                                                // then result in temp1_r

            lsl     temp1_r, temp1_r, 32                                        // Logical shift the value in temp1_r over left by 32 bits
            sxtw    temp2_r, multiplier_r                                       // Sign extend multiplier_r and put it in temp2_r
            and     temp2_r, temp2_r, 0xFFFFFFFF                                // Bitwise AND of the value in temp2_r and 0xFFFFFFFF
                                                                                // then result in temp2_r

            add     result_r, temp1_r, temp2_r                                  // Set the value in result_r be the sum of the value in
                                                                                // temp1_r and the value in temp2_r

output2:    // Print out 64-bit result
            ldr     x0, =fmt2                                                   // Set fmt2 as the 1st argument of 'printf'
            mov     x1, result_r                                                // Set the value in result_r as the 2nd argument of 'printf'
            mov     x2, result_r                                                // Set the value in result_r as the 3rd argument of 'printf'
            bl      printf                                                      // Call printf

ret:        mov     w0, 0                                                       // Return 0 in the 'main' function
            ldp     fp, lr, [sp], 16                                            // Restore the state of the FP and LR and post-incrementing SP by +16
            ret                                                                 // Return control to OS
