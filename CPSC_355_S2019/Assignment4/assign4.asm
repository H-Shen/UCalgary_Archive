/**
 * File:            assign4.asm
 * Author:          Haohu Shen
 * UCID:            30063099
 * Course Number:   CPSC355
 * Instructor:      Tamer Jarada
 * Date:            2019/06/09
 * Description:
 *
 * Assignment4: Structures and Subroutines
 *
 * Implements all the subroutines as unoptimized closed subroutines base on the C Code given
 * in ARMv8 assembly code. All local variables must be created on the stack.
 * M4 macros and register equates are used to improve the code readability.
 */

define(SIZE, 50)                                                            // A constant represents an integer whose value is 50
define(FALSE, 0)                                                            // A constant represents an integer whose value is 0
define(TRUE, 1)                                                             // A constant represents an integer whose value is 1

fp          .req    x29                                                     // Frame Pointer
lr          .req    x30                                                     // Link Register
result_r    .req    w19                                                     // A register that stores the value of the variable 'result'

// Equates for the size of the frame record
fr                      =   16                                              // Set 'fr' as the size of the frame record, which is 16

// Equates for point
x_offset                =   0                                               // Set the address offset of the integer variable 'x' for the struct 'point' from base
y_offset                =   4                                               // Set the address offset of the integer variable 'y' for the struct 'point' from base
struct_point_size       =   8                                               // Set the size of an instance of the struct 'point', which is 8

// Equates for dimension
width_offset            =   0                                               // Set the address offset of the integer variable 'width' for the struct 'dimension' from base
height_offset           =   4                                               // Set the address offset of the integer variable 'height' for the struct 'dimension' from base
struct_dimension_size   =   8                                               // Set the size of an instance of the struct 'dimension', which is 8

// Equates for box
origin_offset           =   0                                               // Set the address offset of the struct variable 'origin' of type 'point' for the struct 'box' from base
dimension_offset        =   8                                               // Set the address offset of the struct variable 'size' of type 'dimension' for the struct 'box' from base
area_offset             =   16                                              // Set the address offset of the integer variable 'area' for the struct 'box' from base
struct_box_size         =   20                                              // Set the size of an instance of the struct 'box', which is 20

title0:     .asciz      "Initial box values:\n"                                         // A string for printing the title after the initialization of instances of 'box'
title1:     .asciz      "\nChanged box values:\n"                                       // A string for printing the title after modifying the instances of 'box'
fmt1:       .asciz      "Box %s origin = (%d, %d) width = %d height = %d area = %d\n"   // A string for printing the detail of an instance of 'box'
fmt2:       .asciz      "first"                                                         // A printing format of the string "first"
fmt3:       .asciz      "second"                                                        // A printing format of the string "second"

            .balign     4                                                   // Ensure following instructions are properly aligned
            .global     main                                                // Set the "main" label be visible to the linker

alloc                   =   -(fr + struct_box_size) & -16                   // Set the total pre-increment value of Stack Pointer for the subroutine 'newBox'
                                                                            // also bitwise AND operator here is used for quad-word alignment

dealloc                 =   -alloc                                          // Set the total post-increment value of Stack Pointer for the subroutine 'newBox'
                                                                            // by the allocation of bytes in the stack

newBox:         stp     fp, lr, [sp, alloc]!                                // Allocate alloc*(-1) bytes on the stack and store the contents of fp and lr in the reserved block
                mov     fp, sp                                              // Set FP to the address of the Stack Pointer

                str     wzr, [fp, fr + origin_offset + x_offset]            // Store 0 from wzr to b.origin.x in (fp+fr+origin_offset+x_offset)
                str     wzr, [fp, fr + origin_offset + y_offset]            // Store 0 from wzr to b.origin.y in (fp+fr+origin_offset+y_offset)
                mov     w9, 1                                               // Set the value in the register w9 as 1
                str     w9, [fp, fr + dimension_offset + width_offset]      // Store 1 from w9 to b.size.width in (fp+fr+dimension_offset+width_offset)
                str     w9, [fp, fr + dimension_offset + height_offset]     // Store 1 from w9 to b.size.height in (fp+fr+dimension_offset+height_offset)

                ldr     w9, [fp, fr + dimension_offset + width_offset]      // Load the value of b.size.width in (fp+fr+dimension_offset+width_offset) to w9
                ldr     w10, [fp, fr + dimension_offset + height_offset]    // Load the value of b.size.height in (fp+fr+dimension_offset+width_offset) to w10
                mul     w9, w9, w10                                         // Multiply the value of w9 and w10 then store in the register w9
                str     w9, [fp, fr + area_offset]                          // Store the value from w9 of b.size.width*b.size.height to b.area in (fp+fr+area_offset)

                ldr     w9, [fp, fr + origin_offset + x_offset]             // Load the value of b.origin.x in (fp+fr+origin_offset+x_offset) to w9
                str     w9, [x8, origin_offset + x_offset]                  // Store the value of b.origin.x from w9 to the address (x8+origin_offset+x_offset)
                ldr     w9, [fp, fr + origin_offset + y_offset]             // Load the value of b.origin.y in (fp+fr+origin_offset+y_offset) to w9
                str     w9, [x8, origin_offset + y_offset]                  // Store the value of b.origin.y from w9 to the address (x8+origin_offset+y_offset)
                ldr     w9, [fp, fr + dimension_offset + width_offset]      // Load the value of b.size.width in (fp+fr+dimension_offset+width_offset) to w9
                str     w9, [x8, dimension_offset + width_offset]           // Store the value of b.size.width from w9 to the address (x8+dimension_offset+width_offset)
                ldr     w9, [fp, fr + dimension_offset + height_offset]     // Load the value of b.size.height in (fp+fr+dimension_offset+height_offset) to w9
                str     w9, [x8, dimension_offset + height_offset]          // Store the value of b.size.height from w9 to the address (x8+dimension_offset+height_offset)
                ldr     w9, [fp, fr + area_offset]                          // Load the value of b.area in (fp+fr+area_offset) to w9
                str     w9, [x8, area_offset]                               // Store the value of b.area from w9 to the address (x8+area_offset)

                ldp     fp, lr, [sp], dealloc                               // Restore the state of the FP and LR and post-incrementing SP by +dealloc
                ret                                                         // Return control to the caller


alloc                   =   -fr                                             // Set the total pre-increment value of Stack Pointer for the subroutine 'move'
                                                                            // also bitwise AND operator here is used for quad-word alignment

dealloc                 =   -alloc                                          // Set the total post-increment value of Stack Pointer for the subroutine 'move'
                                                                            // by the allocation of bytes in the stack

move:           stp     fp, lr, [sp, alloc]!                                // Allocate alloc*(-1) bytes on the stack and store the contents of fp and lr in the reserved block
                mov     fp, sp                                              // Set FP to the address of the Stack Pointer

                ldr     w9, [x0, origin_offset + x_offset]                  // Load the value of b->origin.x in (x0+origin_offset+x_offset) to w9
                add     w9, w9, w1                                          // Update the value in w9 by adding the value in w1
                str     w9, [x0, origin_offset + x_offset]                  // Store the value from w9 to the address (x0+origin_offset+x_offset)

                ldr     w9, [x0, origin_offset + y_offset]                  // Load the value of b->origin.y in (x0+origin_offset+y_offset) to w9
                add     w9, w9, w2                                          // Update the value in w9 by adding the value in w2
                str     w9, [x0, origin_offset + y_offset]                  // Store the value from w9 to the address (x0+origin_offset+y_offset)

                ldp     fp, lr, [sp], dealloc                               // Restore the state of the FP and LR and post-incrementing SP by +dealloc
                ret                                                         // Return control to the caller


alloc                   =   -fr                                             // Set the total pre-increment value of Stack Pointer for the subroutine 'expand'
                                                                            // also bitwise AND operator here is used for quad-word alignment

dealloc                 =   -alloc                                          // Set the total post-increment value of Stack Pointer for the subroutine 'expand'
                                                                            // by the allocation of bytes in the stack

expand:         stp     fp, lr, [sp, alloc]!                                // Allocate alloc*(-1) bytes on the stack and store the contents of fp and lr in the reserved block
                mov     fp, sp                                              // Set FP to the address of the Stack Pointer

                ldr     w9, [x0, dimension_offset + width_offset]           // Load the value of b->size.width in (x0+dimension_offset+width_offset) to w9
                mul     w9, w9, w1                                          // Multiply the value of w9 and w1 then store in the register w9
                str     w9, [x0, dimension_offset + width_offset]           // Store the value of updated b->size.width from w9 to the address (x0+dimension_offset+width_offset)

                ldr     w9, [x0, dimension_offset + height_offset]          // Load the value of b->size.height in (x0+dimension_offset+height_offset) to w9
                mul     w9, w9, w1                                          // Multiply the value of w9 and w1 then store in the register w9
                str     w9, [x0, dimension_offset + height_offset]          // Store the value of updated b->size.height from w9 to the address (x0+dimension_offset+height_offset)

                ldr     w9, [x0, dimension_offset + width_offset]           // Load the value of b->size.width in (x0+dimension_offset+width_offset) to w9
                ldr     w10, [x0, dimension_offset + height_offset]         // Load the value of b->size.height in (x0+dimension_offset+height_offset) to w10
                mul     w9, w9, w10                                         // Multiply the value of w9 and w10 then store in the register w9
                str     w9, [x0, area_offset]                               // Store the value of b->area from w9 to the address (x0+area_offset)

                ldp     fp, lr, [sp], dealloc                               // Restore the state of the FP and LR and post-incrementing SP by +dealloc
                ret                                                         // Return control to the caller


alloc                   =   -fr                                             // Set the total pre-increment value of Stack Pointer for the subroutine 'equal'
                                                                            // also bitwise AND operator here is used for quad-word alignment

dealloc                 =   -alloc                                          // Set the total post-increment value of Stack Pointer for the subroutine 'equal'
                                                                            // by the allocation of bytes in the stack

equal:          stp     fp, lr, [sp, alloc]!                                // Allocate alloc*(-1) bytes on the stack and store the contents of fp and lr in the reserved block
                mov     fp, sp                                              // Set FP to the address of the Stack Pointer

                mov     result_r, FALSE                                     // Set the value in result_r as FALSE, which is 0

                ldr     w9, [x0, origin_offset + x_offset]                  // Load the value of b1->origin.x in (x0+origin_offset+x_offset) to w9
                ldr     w10, [x1, origin_offset + x_offset]                 // Load the value of b2->origin.x in (x1+origin_offset+x_offset) to w10
                cmp     w9, w10                                             // Compare the value in w9 and the value in w10
                b.ne    end_of_equal                                        // Branch to label 'end_of_equal' if the value in w9 does not equal to the value in w10

                ldr     w9, [x0, origin_offset + y_offset]                  // Load the value of b1->origin.y in (x0+origin_offset+y_offset) to w9
                ldr     w10, [x1, origin_offset + y_offset]                 // Load the value of b2->origin.y in (x1+origin_offset+y_offset) to w10
                cmp     w9, w10                                             // Compare the value in w9 and the value in w10
                b.ne    end_of_equal                                        // Branch to label 'end_of_equal' if the value in w9 does not equal to the value in w10

                ldr     w9, [x0, dimension_offset + width_offset]           // Load the value of b1->size.width in (x0+dimension_offset+width_offset) to w9
                ldr     w10, [x1, dimension_offset + width_offset]          // Load the value of b2->size.width in (x1+dimension_offset+width_offset) to w10
                cmp     w9, w10                                             // Compare the value in w9 and the value in w10
                b.ne    end_of_equal                                        // Branch to label 'end_of_equal' if the value in w9 does not equal to the value in w10

                ldr     w9, [x0, dimension_offset + height_offset]          // Load the value of b1->size.height in (x0+dimension_offset+height_offset) to w9
                ldr     w10, [x1, dimension_offset + height_offset]         // Load the value of b2->size.height in (x1+dimension_offset+height_offset) to w10
                cmp     w9, w10                                             // Compare the value in w9 and the value in w10
                b.ne    end_of_equal                                        // Branch to label 'end_of_equal' if the value in w9 does not equal to the value in w10

                mov     result_r, TRUE                                      // Set the value in result_r as TRUE, which is 1

end_of_equal:   mov     w0, result_r                                        // Set the return value as the same as that in result_r for 'equal' subroutine
                ldp     fp, lr, [sp], dealloc                               // Restore the state of the FP and LR and post-incrementing SP by +dealloc
                ret                                                         // Return control to the caller


alloc                   =   -fr                                             // Set the total pre-increment value of Stack Pointer for the subroutine 'printBox'
                                                                            // also bitwise AND operator here is used for quad-word alignment

dealloc                 =   -alloc                                          // Set the total post-increment value of Stack Pointer for the subroutine 'printBox'
                                                                            // by the allocation of bytes in the stack

printBox:       stp     fp, lr, [sp, alloc]!                                // Allocate alloc*(-1) bytes on the stack and store the contents of fp and lr in the reserved block
                mov     fp, sp                                              // Set FP to the address of the Stack Pointer

                ldr     w2, [x1, x_offset]                                  // Set the value in (x1+x_offset) as the 3rd argument of 'printf'
                ldr     w3, [x1, y_offset]                                  // Set the value in (x1+y_offset) as the 4th argument of 'printf'
                ldr     w4, [x1, dimension_offset + width_offset]           // Set the value in (x1+dimension_offset+width_offset) as the 5th argument of 'printf'
                ldr     w5, [x1, dimension_offset + height_offset]          // Set the value in (x1+dimension_offset+height_offset) as the 6th argument of 'printf'
                ldr     w6, [x1, area_offset]                               // Set the value in (x1+area_offset) as the 7th argument of 'printf'
                mov     x1, x0                                              // Set the 2nd argument of 'printf' as the the value of the 1st argument of the subroutine
                ldr     x0, =fmt1                                           // Set fmt1 as the 1st argument of 'printf'
                bl      printf                                              // Call printf

                ldp     fp, lr, [sp], dealloc                               // Restore the state of the FP and LR and post-incrementing SP by +dealloc
                ret                                                         // Return control to the caller



alloc                   =   -(fr + 2*struct_box_size) & -16                 // Set the total pre-increment value of Stack Pointer
                                                                            // also bitwise AND operator here is used for quad-word alignment

dealloc                 =   -alloc                                          // Set the total post-increment value for Stack Pointer
                                                                            // by the allocation of bytes in the stack

first_box_offset        =   fr                                              // Set the address offset in the stack for the local variable 'first' of type 'box' as 16
second_box_offset       =   first_box_offset + struct_box_size              // Set the address offset in the stack for the local variable 'second' of type 'box' as 16+20

main:           stp     fp, lr, [sp, alloc]!                                // Allocate alloc*(-1) bytes on the stack and store the contents of fp and lr in the reserved block
                mov     fp, sp                                              // Set FP to the address of the Stack Pointer

                add     x8, fp, first_box_offset                            // Put the address (fp+first_box_offset) where the variable 'first' is stored into x8
                bl      newBox                                              // Call newBox
                add     x8, fp, second_box_offset                           // Put the address (fp+second_box_offset) where the variable 'second' is stored into x8
                bl      newBox                                              // Call newBox

output0:        ldr     x0, =title0                                         // Set title0 as the 1st argument of 'printf'
                bl      printf                                              // Call printf

                ldr     x0, =fmt2                                           // Set fmt2 as the 1st argument of the subroutine 'printBox'
                add     x1, fp, first_box_offset                            // Set the address (fp+first_box_offset) where the variable 'first' is stored
                                                                            // as the 2nd argument of the subroutine 'printBox'

                bl      printBox                                            // Call printBox

                ldr     x0, =fmt3                                           // Set fmt3 as the 1st argument of the subroutine 'printBox'
                add     x1, fp, second_box_offset                           // Set the address (fp+second_box_offset) where the variable 'second' is stored
                                                                            // as the 3rd argument of the subroutine 'printBox'

                bl      printBox                                            // Call printBox

                add     x0, fp, first_box_offset                            // Set the address (fp+first_box_offset) where the variable 'first' is stored
                                                                            // as the 1st argument of the subroutine 'equal'

                add     x1, fp, second_box_offset                           // Set the address (fp+second_box_offset) where the variable 'second' is stored
                                                                            // as the 2nd argument of the subroutine 'equal'

                bl      equal                                               // Call equal
                cmp     w0, TRUE                                            // Compare the value in w0 which stores the return value from the subroutine 'equal' and TRUE
                b.ne    output1                                             // Branch to label 'output' if the value in w0 does not equal to TRUE

                add     x0, fp, first_box_offset                            // Set the address (fp+first_box_offset) where the variable 'first' is stored
                                                                            // as the 1st argument of the subroutine 'move'

                mov     w1, -5                                              // Set the value in the register w1 as -5
                mov     w2, 7                                               // Set the value in the register w2 as 7
                bl      move                                                // Call move

                add     x0, fp, second_box_offset                           // Set the address (fp+second_box_offset) where the variable 'second' is stored
                                                                            // as the 1st argument of the subroutine 'expand'

                mov     w1, 3                                               // Set the value in the register w1 as 3
                bl      expand                                              // Call expand

output1:        ldr     x0, =title1                                         // Set title1 as the 1st argument of 'printf'
                bl      printf                                              // Call printf

                ldr     x0, =fmt2                                           // Set fmt2 as the 1st argument of the subroutine 'printBox'
                add     x1, fp, first_box_offset                            // Set the address (fp+first_box_offset) where the variable 'first' is stored
                                                                            // as the 2nd argument of the subroutine 'printBox'

                bl      printBox                                            // Call printBox

                ldr     x0, =fmt3                                           // Set fmt3 as the 1st argument of the subroutine 'printBox'
                add     x1, fp, second_box_offset                           // Set the address (fp+second_box_offset) where the variable 'second' is stored
                                                                            // as the 2nd argument of the subroutine 'printBox'

                bl      printBox                                            // Call printBox

ret:            mov     w0, 0                                               // Return 0 for 'main' function
                ldp     fp, lr, [sp], dealloc                               // Restore the state of the FP and LR and post-incrementing SP by +16
                ret                                                         // Return control to OS
