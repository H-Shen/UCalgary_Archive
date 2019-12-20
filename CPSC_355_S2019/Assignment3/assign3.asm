/**
 * File:            assign3.asm
 * Author:          Haohu Shen
 * UCID:            30063099
 * Course Number:   CPSC355
 * Instructor:      Tamer Jarada
 * Date:            2019/05/28
 * Description:
 *
 * Assignment3: Sorting One-Dimensional Arrays
 *
 * Implements the C Code given for sorting a one-dimensional array by the algorithm of insertion-sort
 * in ARMv8 assembly code. All local variables must be created on the stack and assembler equates are used
 * for all stack variable offsets. Whenever a local variable is used or changed, it must be accessed by
 * reading or writing on the corresponding memory address with the help of a temporary register, instead of
 * using a register to store its value during the whole runtime. M4 macros are also used to improve the code readability.
 */

define(SIZE, 50)                                                    // A constant represents the length of the array, whose value is 50

alloc           =   -(16+SIZE*4+4+4+4) & -16                        // Set the total pre-increment value for Stack Pointer, also
                                                                    // bitwise AND operator here is used for quad-word alignment

dealloc         =   -alloc                                          // Set the total post-increment value for Stack Pointer by the allocation of bytes in the stack
base_offset     =   16                                              // Set the address offset in the stack of the array base as 16
i_offset        =   16+SIZE*4                                       // Set the address offset in the stack of the local variable 'i' as (16+SIZE*4)
j_offset        =   16+SIZE*4+4                                     // Set the address offset in the stack of the local variable 'j' as (16+SIZE*4+4)
temp_offset     =   16+SIZE*4+4+4                                   // Set the address offset in the stack of the local variable 'temp' as (16+SIZE*4+4+4)

i_r                 .req    w19                                     // A register which temporarily stores the value of i
vi_r                .req    w20                                     // A register which temporarily stores the value of v[i]
j_r                 .req    w21                                     // A register which temporarily stores the value of j
vj_r                .req    w22                                     // A register which temporarily stores the value of v[j]
temp_r              .req    w23                                     // A register which temporarily stores the value of temp
base_address        .req    x24                                     // A register which stores the base address of the array
fp                  .req    x29                                     // Frame Pointer
lr                  .req    x30                                     // Link Register

fmt1:               .asciz  "v[%d]: %d\n"                           // A string for printing for every entry of the array
fmt2:               .asciz  "\nSorted array:\n"                     // A string for printing the title before printing the sorted array
                    .balign 4                                       // Ensure following instructions are properly aligned
                    .global main                                    // Set the "main" label be visible to the linker

main:               stp     fp, lr, [sp, alloc]!                    // Allocate alloc*(-1) bytes on the stack and store the contents of fp and lr in the reserved block.
                    mov     fp, sp                                  // Set FP to the address of the Stack Pointer
                    add     base_address, fp, base_offset           // Store the base address of the array in base_address

                    /*  Initialize array to random positive integers, mod 256  */
                    mov     i_r, 0                                  // Set the value of i_r as 0
                    str     i_r, [fp, i_offset]                     // Store the value from i_r to the address (fp+i_offset)
                    b       loop1_test                              // Branch to 'loop1_test' for the pre-loop test

loop1:              bl      rand                                    // Call rand, for returning a pseudo-random number in the range of 0 to RAND_MAX
                                                                    // which is library-dependent but is guaranteed to be at least 32767 on any
                                                                    // standard library implementation

                    and     vi_r, w0, 0xFF                          // Bitwise AND with the value in w0 and 0xFF then store the result in vi_r
                    ldr     i_r, [fp, i_offset]                     // Load the current value of i to i_r
                    str     vi_r, [base_address, i_r, sxtw 2]       // Store the value from vi_r to v[i] whose memory address is (base_address+(i_r<<2))

                    ldr     x0, =fmt1                               // Set fmt1 as the 1st argument of 'printf'
                    ldr     w1, [fp, i_offset]                      // Load the value whose address is (fp+i_offset) to w1 as the 2nd argument of 'printf'
                    ldr     w2, [base_address, i_r, sxtw 2]         // Load the value whose address is (base_address+(i_r<<2)) to w2 as the 3rd argument of 'printf'
                    bl      printf                                  // Call printf

                    add     i_r, i_r, 1                             // Update the value in i_r by increasing 1
                    str     i_r, [fp, i_offset]                     // Store the value from i_r to the address (fp+i_offset)

loop1_test:         ldr     i_r, [fp, i_offset]                     // Load the index from (fp+i_offset) to i_r
                    cmp     i_r, SIZE                               // Compare the value in i_r and SIZE
                    b.lt    loop1                                   // Branch to the label 'loop1' if the value in i_r is less than SIZE

                    /* Sort the array using an insertion sort */
                    mov     i_r, 1                                  // Set the value of i_r as 1
                    str     i_r, [fp, i_offset]                     // Store the value from i_r to the address (fp+i_offset)
                    b       loop2_test                              // Branch to label 'loop2_test'

loop2:              ldr     i_r, [fp, i_offset]                     // Load the current value of i in (fp+i_offset) to i_r
                    ldr     temp_r, [base_address, i_r, sxtw 2]     // Load the current value of ith entry of the array in (base_address+(i_r<<2)) to temp_r
                    str     temp_r, [fp, temp_offset]               // Store the value from temp_r to the address (fp+temp_offset)
                    ldr     j_r, [fp, i_offset]                     // Load the current value of i in (fp+i_offset) to j_r
                    str     j_r, [fp, j_offset]                     // Store the value from j_r to the address (fp+j_offset)
                    b       inner_loop_test                         // Branch to label 'inner_loop_test'

inner_loop:         ldr     j_r, [fp, j_offset]                     // Load the current value of j in (fp+j_offset) to j_r
                    sub     w25, j_r, 1                             // Subtract the value in j_r by 1 and store to w25
                    ldr     w26, [base_address, w25, sxtw 2]        // Load the current value of (j-1)th entry of the array in (base_address+((j_r-1)<<2)) to w26
                    str     w26, [base_address, j_r, sxtw 2]        // Store the value from w26 to the address (base_address+(j_r<<2))
                    str     w25, [fp, j_offset]                     // Store the value from w25 to the address (fp+j_offset), that is, increasing the local variable 'j' by 1

inner_loop_test:    ldr     j_r, [fp, j_offset]                     // Load the current value of j in (fp+j_offset) to j_r
                    cmp     j_r, 0                                  // Compare the value in j_r and 0
                    b.le    loop2_body_end                          // Branch to label 'loop2_body_end' if the value in j_r is less than or equal to 0

                    sub     j_r, j_r, 1                             // Update the value in j_r by decreasing 1
                    ldr     w27, [base_address, j_r, sxtw 2]        // Load the current value of jth entry of the array in (base_address+(j_r<<2)) to w27
                    ldr     temp_r, [fp, temp_offset]               // Load the current value of temp in (fp+temp_offset) to temp_r
                    cmp     temp_r, w27                             // Compare the value in temp_r and the value in w27
                    b.ge    loop2_body_end                          // Branch to label 'loop2_body_end' if the value in temp_r is greater than or equal to the value in w27
                    b       inner_loop                              // Branch to label 'inner_loop'

loop2_body_end:     ldr     temp_r, [fp, temp_offset]               // Load the current value of temp in (fp+temp_offset) to temp_r
                    ldr     j_r, [fp, j_offset]                     // Load the current value of the variable 'j' in (fp+j_offset) to j_r
                    str     temp_r, [base_address, j_r, sxtw 2]     // Store the value in temp_r to the address (base_address+(j_r<<2))

                    ldr     i_r, [fp, i_offset]                     // Load the current value of i in (fp+i_offset) to i_r
                    add     i_r, i_r , 1                            // Update the value in i_r by increasing 1
                    str     i_r, [fp, i_offset]                     // Store the value from i_r to the address (fp+i_offset)

loop2_test:         cmp     i_r, SIZE                               // Compare the value in i_r and SIZE
                    b.lt    loop2                                   // Branch to label 'loop2' if the value in i_r is less than SIZE

                    /*  Print out the sorted array  */
                    ldr     x0, =fmt2                               // Set fmt2 as the 1st argument of 'printf'
                    bl      printf                                  // Call printf

                    mov     i_r, 0                                  // Set the value of i_r as 0
                    str     i_r, [fp, i_offset]                     // Store the value from i_r to the address fp+i_offset
                    b       loop3_test                              // Branch to 'loop_test3' for the pre-loop test

loop3:              ldr     x0, =fmt1                               // Set fmt1 as the 1st argument of 'printf'
                    ldr     w1, [fp, i_offset]                      // Load the value whose address is (fp+i_offset) to w1 as the 2nd argument of 'printf'
                    ldr     w2, [base_address, i_r, sxtw 2]         // Load the value whose address is (base_address+(i_r<<2)) to w2 as the 3rd argument of 'printf'
                    bl      printf                                  // Call printf

                    ldr     i_r, [fp, i_offset]                     // Load the current value of i in (fp+i_offset) to i_r
                    add     i_r, i_r, 1                             // Update the value in i_r by increasing 1
                    str     i_r, [fp, i_offset]                     // Store the value from i_r to the address (fp+i_offset)

loop3_test:         ldr     i_r, [fp, i_offset]                     // Load the index from (fp+i_offset) to i_r
                    cmp     i_r, SIZE                               // Compare the value in i_r and SIZE
                    b.lt    loop3                                   // Branch to the label 'loop3' if the value in i_r is less than SIZE

ret:                mov     w0, 0                                   // Return 0 for 'main' function
                    ldp     fp, lr, [sp], dealloc                   // Restore the state of the FP and LR and post-incrementing SP by +dealloc
                    ret                                             // Return control to OS

