/**
 * File:            a5a.asm
 * Author:          Haohu Shen
 * UCID:            30063099
 * Course Number:   CPSC355
 * Instructor:      Tamer Jarada
 * Date:            2019/06/10
 * Description:
 *
 * Assignment5: Part A: Global Variables and Separate Compilation
 *
 * Translates all functions except main() base on the C Code given into ARMv8 assembly code.
 * All global variables from the C code must be moved here. Whenever a global variable is used or changed,
 * it must be accessed by reading or writing on the corresponding memory address with the help of a temporary register,
 * instead of manipulating, a register to store its value during the whole runtime, although it may occur code repetition.
 * Local variables in the 'dequeue' function and the 'display' function will be stored in registers since they are
 * decorated by the keyword 'register' in the C code given. M4 macros and register equates are used to
 * improve the code readability.
 */

define(QUEUESIZE, 8)                        // A constant integer represents the size of the queue whose value is 8
define(MODMASK, 0x7)                        // A constant integer represents the value to mod, which is 0x7
define(FALSE, 0)                            // A constant integer represents a #FALSE# status whose value is 0
define(TRUE, 1)                             // A constant integer represents a #TRUE# status whose value is 1

head_r              .req    w20             // A register which temporarily stores the value of head
tail_r              .req    w21             // A register which temporarily stores the value of tail
i_r                 .req    w22             // A register which temporarily stores the value of i in the function 'display'
j_r                 .req    w23             // A register which temporarily stores the value of j in the function 'display'
count_r             .req    w24             // A register which temporarily stores the value of count in the function 'display'
base_r              .req    x25             // A register which stores the base address of the queue
value_r             .req    w26             // A register which temporarily stores the value of 'value' in the function 'dequeue'
fp                  .req    x29             // Frame Pointer
lr                  .req    x30             // Link Register

                    .bss                    // The bss section goes here
                    .global queue           // Make the variable 'queue' to be global
queue:              .skip   QUEUESIZE*4     // Allocate QUEUESIZE*4 bytes in the bss section with zero-initialization

                    .data                   // The data section goes here
                    .global head            // Make the variable 'head' to be global
head:               .word   -1              // Allocate 4 bytes in the data section for the word 'head' and initializes with -1
                    .global tail            // Make the variable 'tail' to be global
tail:               .word   -1              // Allocate 4 bytes in the data section for the word 'tail' and initializes with -1

                    .text                                                                   // The text section goes here
overflow_message:   .asciz      "\nQueue overflow! Cannot enqueue into a full queue.\n"     // Set a string of error message in the text section when the queue is overflow
underflow_message:  .asciz      "\nQueue underflow! Cannot dequeue from an empty queue.\n"  // Set a string of error message in the text section when the queue is underflow
empty_message:      .asciz      "\nEmpty queue\n"                                           // Set a string of error message in the text section when the queue is empty
title_of_display:   .asciz      "\nCurrent queue contents:\n"                               // Set a string of title in the text section before printing all entries of the queue
entry_message:      .asciz      " %d"                                                       // Set a string for printing for every entry of the queue in the text section
head_message:       .asciz      " <-- head of queue"                                        // Set a string of message to indicate the head of queue in the text section
tail_message:       .asciz      " <-- tail of queue"                                        // Set a string of message to indicate the tail of queue in the text section
newline:            .asciz      "\n"                                                        // Set a string of new line in the text section

                    .balign     4                                                           // Ensure following instructions are properly aligned by 4 bytes
                    .global     enqueue                                                     // Make the 'enqueue' function be visible to the 'main' function
                    .global     dequeue                                                     // Make the 'dequeue' function be visible to the 'main' function
                    .global     queueEmpty                                                  // Make the 'queueEmpty' function be visible to the 'main' function
                    .global     queueFull                                                   // Make the 'queueFull' function be visible to the 'main' function
                    .global     display                                                     // Make the 'display' function be visible to the 'main' function

enqueue:                stp     fp, lr, [sp, -16]!                  // Allocate 16 bytes on the stack and store the contents of FP and LR in the reserved block
                        mov     fp, sp                              // Set FP to the address of the Stack Pointer

                        mov     w27, w0                             // Save the value in w0, which is the 1st argument passed to the function, to w27 temporarily
                        bl      queueFull                           // Call 'queueFull'
                        cmp     w0, TRUE                            // Compare the result of the 'queueFull' function in w0 and TRUE
                        b.ne    enqueue_next0                       // Branch to the label 'enqueue_next0' if the value in w0 does not equal to TRUE

                        ldr     x0, =overflow_message               // Set overflow_message as the 1st argument of 'printf'
                        bl      printf                              // Call 'printf'
                        b       enqueue_ret                         // Branch to the label 'enqueue_ret'

enqueue_next0:          bl      queueEmpty                          // Call 'queueEmpty'
                        cmp     w0, TRUE                            // Compare the result of the 'queueEmpty' function in w0 and TRUE
                        b.ne    enqueue_next1                       // Branch to the label 'enqueue_next1' if the value in w0 does not equal to TRUE

                        ldr     x19, =tail                          // Load the address of label 'tail' to x19
                        ldr     tail_r, [x19]                       // Load the value stored in the address that stores in x19 to tail_r
                        mov     tail_r, wzr                         // Set the value in tail_r as 0
                        str     tail_r, [x19]                       // Store the value in tail_r to the address that stores in x19

                        mov     head_r, tail_r                      // Set the value in head_r as the same as that in tail_r
                        ldr     x19, =head                          // Load the address of label 'head' to x19
                        str     head_r, [x19]                       // Store the value in head_r to the address that stores in x19

                        b       enqueue_next2                       // Branch to the label 'enqueue_next2'

enqueue_next1:          ldr     x19, =tail                          // Load the address of label 'tail' to x19
                        ldr     tail_r, [x19]                       // Load the value stored in the address that stores in x19 to tail_r
                        add     tail_r, tail_r, 1                   // Update the value in tail_r by increasing 1
                        and     tail_r, tail_r, MODMASK             // Bitwise AND of the value in tail_r and MODMASK then result in tail_r
                        str     tail_r, [x19]                       // Store the value in tail_r to the address that stores in x19

enqueue_next2:          ldr     x19, =tail                          // Load the address of label 'tail' to x19
                        ldr     tail_r, [x19]                       // Load the value stored in the address that stores in x19 to tail_r
                        ldr     base_r, =queue                      // Load the base address of the queue to base_r
                        str     w27, [base_r, tail_r, sxtw 2]       // Store the value in w27 to the address (base_r+(tail_r<<2))

enqueue_ret:            ldp     fp, lr, [sp], 16                    // Restore the state of the FP and LR and post-incrementing SP by +16
                        ret                                         // Return control to the caller

dequeue:                stp     fp, lr, [sp, -16]!                  // Allocate 16 bytes on the stack and store the contents of FP and LR in the reserved block
                        mov     fp, sp                              // Set FP to the address of the Stack Pointer

                        bl      queueEmpty                          // Call 'queueEmpty'
                        cmp     w0, TRUE                            // Compare the result of the 'queueEmpty' function in w0 and TRUE
                        b.ne    dequeue_next0                       // Branch to the label 'enqueue_next0' if the value in w0 does not equal to TRUE

                        ldr     x0, =underflow_message              // Set underflow_message as the 1st argument of 'printf'
                        bl      printf                              // Call 'printf'
                        mov     w0, -1                              // Set the value in w0 as -1
                        b       dequeue_ret                         // Branch to the label 'dequeue_ret'

dequeue_next0:          ldr     x19, =head                          // Load the address of label 'head' to x19
                        ldr     head_r, [x19]                       // Load the value stored in the address that stores in x19 to head_r

                        ldr     base_r, =queue                      // Load the base address of the queue to base_r
                        ldr     value_r, [base_r, head_r, sxtw 2]   // Load the value in (base_r+(head_r<<2)) to value_r

                        ldr     x19, =tail                          // Load the address of label 'tail' to x19
                        ldr     tail_r, [x19]                       // Load the value stored in the address that stores in x19 to tail_r

                        cmp     head_r, tail_r                      // Compare the value in head_r and the value in tail_r
                        b.ne    dequeue_next1                       // Branch to the label 'dequeue_next1' if the value in head_r does not equal to the value in tail_r

                        mov     tail_r, -1                          // Set the value in tail_r as -1
                        ldr     x19, =tail                          // Load the address of label 'tail' to x19
                        str     tail_r, [x19]                       // Store the value in tail_r to the address that stores in x19

                        mov     head_r, tail_r                      // Set the value in head_r as the same as that in tail_r
                        ldr     x19, =head                          // Load the address of label 'head' to x19
                        str     head_r, [x19]                       // Store the value in head_r to the address that stores in x19

                        b       dequeue_next2                       // Branch to the label 'dequeue_next2'

dequeue_next1:          ldr     x19, =head                          // Load the address of label 'head' to x19
                        ldr     head_r, [x19]                       // Load the value stored in the address that stores in x19 to head_r
                        add     head_r, head_r, 1                   // Update the value in head_r by increasing 1
                        and     head_r, head_r, MODMASK             // Bitwise AND of the value in head_r and MODMASK then result in head_r
                        str     head_r, [x19]                       // Store the value in head_r to the address that stores in x19

dequeue_next2:          mov     w0, value_r                         // Set the value in w0 as the same as that in value_r

dequeue_ret:            ldp     fp, lr, [sp], 16                    // Restore the state of the FP and LR and post-incrementing SP by +16
                        ret                                         // Return control to the caller

queueEmpty:             stp     fp, lr, [sp, -16]!                  // Allocate 16 bytes on the stack and store the contents of FP and LR in the reserved block
                        mov     fp, sp                              // Set FP to the address of the Stack Pointer

                        ldr     x19, =head                          // Load the address of label 'head' to x19
                        ldr     head_r, [x19]                       // Store the value in head_r to the address that stores in x19

                        cmp     head_r, -1                          // Set the value in head_r as -1
                        b.ne    queueEmpty_next0                    // Branch to the label 'queueEmpty_next0' if the value in head_r does not equal to -1
                        mov     w0, TRUE                            // Compare the value in w0 and TRUE
                        b       queueEmpty_ret                      // Branch to the label 'queueEmpty_ret'

queueEmpty_next0:       mov     w0, FALSE                           // Set the value in w0 as FALSE

queueEmpty_ret:         ldp     fp, lr, [sp], 16                    // Restore the state of the FP and LR and post-incrementing SP by +16
                        ret                                         // Return control to the caller

queueFull:              stp     fp, lr, [sp, -16]!                  // Allocate 16 bytes on the stack and store the contents of FP and LR in the reserved block
                        mov     fp, sp                              // Set FP to the address of the Stack Pointer

                        ldr     x19, =tail                          // Load the address of label 'tail' to x19
                        ldr     tail_r, [x19]                       // Load the value stored in the address that stores in x19 to tail_r

                        add     tail_r, tail_r, 1                   // Update the value in tail_r by increasing 1
                        and     tail_r, tail_r, MODMASK             // Bitwise AND of the value in tail_r and MODMASK then result in tail_r

                        ldr     x19, =head                          // Load the address of label 'head' to x19
                        ldr     head_r, [x19]                       // Load the value stored in the address that stores in x19 to head_r

                        cmp     head_r, tail_r                      // Compare the value in head_r and the value in tail_r
                        b.ne    queueFull_next0                     // Branch to the label 'queueFull_next0' if the value in head_r does not equal to the value in tail_r
                        mov     w0, TRUE                            // Set the value in w0 as TRUE
                        b       queueFull_ret                       // Branch to the label 'queueFull_ret'

queueFull_next0:        mov     w0, FALSE                           // Set the value in w0 as FALSE

queueFull_ret:          ldp     fp, lr, [sp], 16                    // Restore the state of the FP and LR and post-incrementing SP by +16
                        ret                                         // Return control to the caller

display:                stp     fp, lr, [sp, -16]!                  // Allocate 16 bytes on the stack and store the contents of FP and LR in the reserved block
                        mov     fp, sp                              // Set FP to the address of the Stack Pointer

                        bl      queueEmpty                          // Call 'queueEmpty'
                        cmp     w0, TRUE                            // Compare the result of the 'queueEmpty' function in w0 and TRUE
                        b.ne    display_next0                       // Branch to the label 'display_next0' if the value in w0 does not equal to TRUE

                        ldr     x0, =empty_message                  // Set empty_message as the 1st argument of 'printf'
                        bl      printf                              // Call 'printf'
                        b       display_ret                         // Branch to the label 'display_ret'

display_next0:          ldr     x19, =tail                          // Load the address of label 'tail' to x19
                        ldr     tail_r, [x19]                       // Load the value stored in the address that stores in x19 to tail_r
                        ldr     x19, =head                          // Load the address of label 'head' to x19
                        ldr     head_r, [x19]                       // Load the value stored in the address that stores in x19 to head_r

                        sub     count_r, tail_r, head_r             // Subtract the value in head_r from the value in tail_r and store in count_r
                        add     count_r, count_r, 1                 // Update the value in count_r by increasing 1

                        cmp     count_r, wzr                        // Compare the value in count_r and 0
                        b.gt    display_next1                       // Branch to the label 'display_next1' if the value in count_r is greater than 0
                        add     count_r, count_r, QUEUESIZE         // Update the value in count_r by increasing QUEUESIZE

display_next1:          ldr     x0, =title_of_display               // Set title_of_display as the 1st argument of 'printf'
                        bl      printf                              // Call 'printf'
                        mov     i_r, head_r                         // Set the value in i_r as the same as that in head_r
                        mov     j_r, wzr                            // Set the value in j_r as 0
                        b       loop_test                           // Branch to the label 'loop_test'

loop_body:              ldr     base_r, =queue                      // Load the base address of the queue to base_r
                        ldr     w1, [base_r, i_r, sxtw 2]           // Load the value in (base_r+(i_r<<2)) to w1
                        ldr     x0, =entry_message                  // Set entry_message as the 1st argument of 'printf'
                        bl      printf                              // Call 'printf'

                        ldr     x19, =head                          // Load the address of label 'head' to x19
                        ldr     head_r, [x19]                       // Load the value stored in the address that stores in x19 to head_r

                        cmp     i_r, head_r                         // Compare the value in i_r and the value in head_r
                        b.ne    display_next2                       // Branch to the label 'display_next2' if the value in i_r does not equal to the value in head_r
                        ldr     x0, =head_message                   // Set head_message as the 1st argument of 'printf'
                        bl      printf                              // Call 'printf'

display_next2:          ldr     x19, =tail                          // Load the address of label 'tail' to x19
                        ldr     tail_r, [x19]                       // Load the value stored in the address that stores in x19 to tail_r

                        cmp     i_r, tail_r                         // Compare the value in i_r and the value in tail_r
                        b.ne    display_next3                       // Branch to the label 'display_next3' if the value in i_r does not equal to the value in tail_r
                        ldr     x0, =tail_message                   // Set tail_message as the 1st argument of 'printf'
                        bl      printf                              // Call 'printf'

display_next3:          ldr     x0, =newline                        // Set newline as the 1st argument of 'printf'
                        bl      printf                              // Call 'printf'

                        add     i_r, i_r, 1                         // Update the value in i_r by increasing 1
                        and     i_r, i_r, MODMASK                   // Bitwise AND of the value in i_r and MODMASK then result in i_r
                        add     j_r, j_r, 1                         // Update the value in j_r by increasing 1

loop_test:              cmp     j_r, count_r                        // Compare the value in j_r and the value in count_r
                        b.ge    display_ret                         // Branch to the label 'display_ret' if the value in j_r is greater than the value in count_r
                        b       loop_body                           // Branch back to the label 'loop_body'

display_ret:            ldp     fp, lr, [sp], 16                    // Restore the state of the FP and LR and post-incrementing SP by +16
                        ret                                         // Return control to the caller
