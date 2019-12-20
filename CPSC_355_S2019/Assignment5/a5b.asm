/**
 * File:            a5b.asm
 * Author:          Haohu Shen
 * UCID:            30063099
 * Course Number:   CPSC355
 * Instructor:      Tamer Jarada
 * Date:            2019/06/11
 * Description:
 *
 * Assignment5: Part B: External Pointer Arrays and Command-Line Arguments
 *
 * The program translates 2 declarations of external arrays base on the C Code given into ARMv8 assembly code and accepts
 * 2 strings as command-line arguments from standard input. The name of month, the day with the appropriate suffix and
 * the season for the date will be printed if the format of command-line arguments are valid, otherwise the program will
 * print an error message and terminate. M4 macros and register equates are used to improve the code readability.
 * Moreover, we suppose the year is 2019 when the user uses the program, since it is not a leap year, its February
 * has 28 days. Three closed subroutines 'month_check', 'day_check' and 'season_find' will help parsing the input.
 */

define(TRUE, 1)     // A constant integer represents a #TRUE# status whose value is 1
define(FALSE, 0)    // A constant integer represents a #FALSE# status whose value is 0

argc_r              .req    w20                         // A register which temporarily stores the value of argc, which is the number of command-line arguments
argv_r              .req    x21                         // A register which stores the base address of argv[]
argv1_r             .req    w22                         // A register which temporarily stores the value of the 2nd command-line argument, as an integer
argv2_r             .req    w23                         // A register which temporarily stores the value of the 3rd command-line argument, as an integer
i_r                 .req    w24                         // A register which temporarily stores the value of the index of argv[] currently accessed
season_index        .req    w25                         // A register which temporarily stores the value of the index of current season in the array 'season'
suffix_r            .req    x26                         // A register which temporarily stores the content of the correct suffix for the day input
month_r             .req    x27                         // A register which stores the base address of month[]
season_r            .req    x28                         // A register which stores the base address of season[]
fp                  .req    x29                         // Frame Pointer
lr                  .req    x30                         // Link Register

                    .text                               // The text section goes here
Jan:                .asciz     "January"                // Set a string represents January
Feb:                .asciz     "February"               // Set a string represents February
Mar:                .asciz     "March"                  // Set a string represents March
Apr:                .asciz     "April"                  // Set a string represents April
May:                .asciz     "May"                    // Set a string represents May
Jun:                .asciz     "June"                   // Set a string represents June
Jul:                .asciz     "July"                   // Set a string represents July
Aug:                .asciz     "August"                 // Set a string represents August
Sept:               .asciz     "September"              // Set a string represents September
Oct:                .asciz     "October"                // Set a string represents October
Nov:                .asciz     "November"               // Set a string represents November
Dec:                .asciz     "December"               // Set a string represents December

Winter:             .asciz     "Winter"                 // Set a string represents Winter
Spring:             .asciz     "Spring"                 // Set a string represents Spring
Summer:             .asciz     "Summer"                 // Set a string represents Summer
Fall:               .asciz     "Fall"                   // Set a string represents Fall

suffix0:            .asciz     "st"                     // Set a string represents the 'st' suffix for an ordinal number
suffix1:            .asciz     "nd"                     // Set a string represents the 'nd' suffix for an ordinal number
suffix2:            .asciz     "rd"                     // Set a string represents the 'rd' suffix for an ordinal number
suffix3:            .asciz     "th"                     // Set a string represents the 'th' suffix for an ordinal number

error_message:      .asciz     "usage: a5b mm dd\n"     // Set a string represents the error message displayed if any invalid input exists
output_message:     .asciz     "%s %d%s is %s\n"        // Set a string represents the format of output of the date

                    .data                               // The data section goes here
                    .balign 8                           // Ensure following instructions are properly aligned by 8 bytes
                    .global month
                    .global season
month:              .dword      Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sept, Oct, Nov, Dec     // The definition of the 'month' array
season:             .dword      Winter, Spring, Summer, Fall                                    // The definition of the 'season' array

                    .text                               // Return to the text section
                    .balign 4                           // Ensure following instructions are properly aligned by 4 bytes
                    .global main                        // Set the "main" label be visible to the linker


/**
 * The closed subroutine 'month_check' takes the argument of the current month as an integer and check if it is an
 * integer between 1 and 12 (all inclusive), the subroutine returns TRUE if it is valid, otherwise returns FALSE.
 */
month_check:            stp     fp, lr, [sp, -16]!                      // Allocate 16 bytes on the stack and store the contents of fp and lr in the reserved block
                        mov     fp, sp                                  // Set FP to the address of the Stack Pointer

                        cmp     w0, 1                                   // Compare the value in w0 and 1
                        b.lt    month_check_next0                       // Branch to the label 'month_check_next0' if the value in w0 is less than 1
                        cmp     w0, 12                                  // Compare the value in w0 and 12
                        b.gt    month_check_next0                       // Branch to the label 'month_check_next0' if the value in w0 is greater than 1
                        mov     w0, TRUE                                // Set the value in w0 as TRUE
                        b       month_check_ret                         // Branch to the label 'month_check_ret'

month_check_next0:      mov     w0, FALSE                               // Set the value in w0 as FALSE

month_check_ret:        ldp     fp, lr, [sp], 16                        // Restore the state of the FP and LR and post-incrementing SP by +16
                        ret                                             // Return control to the caller


/**
 * The closed subroutine 'day_check' takes the arguments of the current month as an integer and the current day as an
 * integer then check if the day in that month is correct, the subroutine returns TRUE if the day is valid, otherwise
 * returns FALSE.
 */
day_check:              stp     fp, lr, [sp, -16]!                      // Allocate 16 bytes on the stack and store the contents of fp and lr in the reserved block
                        mov     fp, sp                                  // Set FP to the address of the Stack Pointer

                        cmp     w0, 1                                   // Compare the value in w0 and 1
                        b.eq    day_check_next0                         // Branch to the label 'day_check_next0' if the value in w0 equals to 1
                        cmp     w0, 3                                   // Compare the value in w0 and 3
                        b.eq    day_check_next0                         // Branch to the label 'day_check_next0' if the value in w0 equals to 3
                        cmp     w0, 5                                   // Compare the value in w0 and 5
                        b.eq    day_check_next0                         // Branch to the label 'day_check_next0' if the value in w0 equals to 5
                        cmp     w0, 7                                   // Compare the value in w0 and 7
                        b.eq    day_check_next0                         // Branch to the label 'day_check_next0' if the value in w0 equals to 7
                        cmp     w0, 8                                   // Compare the value in w0 and 8
                        b.eq    day_check_next0                         // Branch to the label 'day_check_next0' if the value in w0 equals to 8
                        cmp     w0, 10                                  // Compare the value in w0 and 10
                        b.eq    day_check_next0                         // Branch to the label 'day_check_next0' if the value in w0 equals to 10
                        cmp     w0, 12                                  // Compare the value in w0 and 12
                        b.eq    day_check_next0                         // Branch to the label 'day_check_next0' if the value in w0 equals to 12

                        b       day_check_next1                         // Branch to the label 'day_check_next1'

day_check_next0:        cmp     w1, 1                                   // Compare the value in w1 and 1
                        b.lt    day_check_false                         // Branch to the label 'day_check_false' if the value in w1 is less than 1
                        cmp     w1, 31                                  // Compare the value in w1 and 31
                        b.gt    day_check_false                         // Branch to the label 'day_check_false' if the value in w1 is greater than 31
                        b       day_check_true                          // Branch to the label 'day_check_true'

day_check_next1:        cmp     w0, 2                                   // Compare the value in w0 and 2
                        b.eq    day_check_next2                         // Branch to the label 'day_check_next2' if the value in w0 equals to 2
                        b       day_check_next3                         // Branch to the label 'day_check_next3'

day_check_next2:        cmp     w1, 1                                   // Compare the value in w1 and 1
                        b.lt    day_check_false                         // Branch to the label 'day_check_false' if the value in w1 is less than 1
                        cmp     w1, 28                                  // Compare the value in w1 and 28
                        b.gt    day_check_false                         // Branch to the label 'day_check_false' if the value in w1 is greater than 28
                        b       day_check_true                          // Branch to the label 'day_check_true'

day_check_next3:        cmp     w1, 1                                   // Compare the value in w1 and 1
                        b.lt    day_check_false                         // Branch to the label 'day_check_false' if the value in w1 is less than 1
                        cmp     w1, 30                                  // Compare the value in w1 and 30
                        b.gt    day_check_false                         // Branch to the label 'day_check_false' if the value in w1 is greater than 30
                        b       day_check_true                          // Branch to the label 'day_check_true'

day_check_false:        mov     w0, FALSE                               // Set the value in w0 as FALSE
                        b       day_check_ret                           // Branch to the label 'day_check_ret'

day_check_true:         mov     w0, TRUE                                // Set the value in w0 as TRUE

day_check_ret:          ldp     fp, lr, [sp], 16                        // Restore the state of the FP and LR and post-incrementing SP by +16
                        ret                                             // Return control to the caller


/**
 * The closed subroutine 'season_find' takes the arguments of the current month as an integer and the current day as an
 * integer then return the corresponding index of entry in the 'season' array.
 */
season_find:            stp     fp, lr, [sp, -16]!                      // Allocate 16 bytes on the stack and store the contents of fp and lr in the reserved block
                        mov     fp, sp                                  // Set FP to the address of the Stack Pointer

                        cmp     w0, 12                                  // Compare the value in w0 and 12
                        b.eq    season_find_next0                       // Branch to the label 'season_find_next0' if the value in w0 equals to 12
                        b       season_find_next1                       // Branch to the label 'season_find_next1'
season_find_next0:      cmp     w1, 21                                  // Compare the value in w1 and 21
                        b.ge    season_find_ret_0                       // Branch to the label 'season_find_next0' if the value in w1 is greater than or equal to 21

season_find_next1:      cmp     w0, 1                                   // Compare the value in w0 and 1
                        b.eq    season_find_ret_0                       // Branch to the label 'season_find_next0' if the value in w0 equals to 1
                        cmp     w0, 2                                   // Compare the value in w0 and 2
                        b.eq    season_find_ret_0                       // Branch to the label 'season_find_ret_0' if the value in w0 equals to 2
                        cmp     w0, 3                                   // Compare the value in w0 and 3
                        b.eq    season_find_next2                       // Branch to the label 'season_find_next2' if the value in w0 equals to 3
                        b       season_find_next3                       // Branch to the label 'season_find_next3'

season_find_next2:      cmp     w1, 20                                  // Compare the value in w1 and 20
                        b.le    season_find_ret_0                       // Branch to the label 'season_find_ret_0' if the value in w1 is less than or equal to 20
                        b       season_find_ret_1                       // Branch to the label 'season_find_ret_1'

season_find_next3:      cmp     w0, 4                                   // Compare the value in w0 and 4
                        b.eq    season_find_ret_1                       // Branch to the label 'season_find_ret_1' if the value in w0 equals to 4
                        cmp     w0, 5                                   // Compare the value in w0 and 5
                        b.eq    season_find_ret_1                       // Branch to the label 'season_find_ret_1' if the value in w0 equals to 5
                        cmp     w0, 6                                   // Compare the value in w0 and 6
                        b.eq    season_find_next4                       // Branch to the label 'season_find_next4' if the value in w0 equals to 6
                        b       season_find_next5                       // Branch to the label 'season_find_next5'

season_find_next4:      cmp     w1, 20                                  // Compare the value in w1 and 20
                        b.le    season_find_ret_1                       // Branch to the label 'season_find_ret_1' if the value in w1 is less than or equal to 20
                        b       season_find_ret_2                       // Branch to the label 'season_find_ret_2'

season_find_next5:      cmp     w0, 7                                   // Compare the value in w0 and 7
                        b.eq    season_find_ret_2                       // Branch to the label 'season_find_ret_2' if the value in w0 equals to 7
                        cmp     w0, 8                                   // Compare the value in w0 and 8
                        b.eq    season_find_ret_2                       // Branch to the label 'season_find_ret_2' if the value in w0 equals to 8
                        cmp     w0, 9                                   // Compare the value in w0 and 9
                        b.eq    season_find_next6                       // Branch to the label 'season_find_next6' if the value in w0 equals to 9
                        b       season_find_ret_3                       // Branch to the label 'season_find_ret_3'

season_find_next6:      cmp     w1, 20                                  // Compare the value in w1 and 20
                        b.le    season_find_ret_2                       // Branch to the label 'season_find_ret_2' if the value in w1 is less than or equal to 20
                        b       season_find_ret_3                       // Branch to the label 'season_find_ret_3'

season_find_ret_0:      mov     w0, 0                                   // Set the value in w0 as 0
                        b       season_find_ret                         // Branch to the label 'season_find_ret'

season_find_ret_1:      mov     w0, 1                                   // Set the value in w0 as 1
                        b       season_find_ret                         // Branch to the label 'season_find_ret'

season_find_ret_2:      mov     w0, 2                                   // Set the value in w0 as 2
                        b       season_find_ret                         // Branch to the label 'season_find_ret'

season_find_ret_3:      mov     w0, 3                                   // Set the value in w0 as 3

season_find_ret:        ldp     fp, lr, [sp], 16                        // Restore the state of the FP and LR and post-incrementing SP by +16
                        ret                                             // Return control to the caller


main:                   stp     fp, lr, [sp, -16]!                      // Allocate 16 bytes on the stack and store the contents of fp and lr in the reserved block
                        mov     fp, sp                                  // Set FP to the address of the Stack Pointer

                        mov     argc_r, w0                              // Copy the value from w0 to argc_r
                        mov     argv_r, x1                              // Copy the value from x1 to argv_r

                        // Validate the number of arguments
                        cmp     argc_r, 3                               // Compare the value in argc_r and 3
                        b.eq    next0                                   // Branch to the label 'next0' if the value in argc_r equals to 3
                        ldr     x0, =error_message                      // Set error_message as the 1st argument of 'printf'
                        bl      printf                                  // Call 'printf'
                        b       main_ret                                // Branch to the label 'main_ret'

next0:                  // Convert argv[1] to an integer
                        mov     i_r, 1                                  // Set the value in i_r as 1
                        ldr     x0, [argv_r, i_r, sxtw 3]               // Load the value in (argv_r+(i_r<<3)) to x0
                        bl      atoi                                    // Call 'atoi'
                        mov     argv1_r, w0                             // Copy the value from w0 to argv1_r, which is the return value of 'atoi'

                        // Range check for month
                        mov     w0, argv1_r                             // Copy the value from argv1_r to w0
                        bl      month_check                             // Call 'month_check'
                        cmp     w0, FALSE                               // Compare the return value of 'month_check' in w0 and FALSE
                        b.ne    next1                                   // Branch to the label 'next1' if the value in w0 does not equal to FALSE
                        ldr     x0, =error_message                      // Set error_message as the 1st argument of 'printf'
                        bl      printf                                  // Call 'printf'
                        b       main_ret                                // Branch to the label 'main_ret'

next1:                  // Convert argv[2] to an integer
                        mov     i_r, 2                                  // Set the value in i_r as 2
                        ldr     x0, [argv_r, i_r, sxtw 3]               // Load the value in (argv_r+(i_r<<3)) to x0
                        bl      atoi                                    // Call 'atoi'
                        mov     argv2_r, w0                             // Copy the value from w0 to argv2_r, which is the return value of 'atoi'

                        // Range check for day
                        mov     w0, argv1_r                             // Copy the value from argv1_r to w0
                        mov     w1, argv2_r                             // Copy the value from argv2_r to w1
                        bl      day_check                               // Call 'day_check'
                        cmp     w0, FALSE                               // Compare the value in w0 and FALSE
                        b.ne    next2                                   // Branch to the label 'next2' if the value in w0 does not equal to FALSE
                        ldr     x0, =error_message                      // Set error_message as the 1st argument of 'printf'
                        bl      printf                                  // Call 'printf'
                        b       main_ret                                // Branch to the label 'main_ret'

next2:                  // Get the index of current season in the 'season' array
                        mov     w0, argv1_r                             // Copy the value from argv1_r to w0
                        mov     w1, argv2_r                             // Copy the value from argv2_r to w1
                        bl      season_find                             // Call 'season_find'
                        mov     season_index, w0                        // Copy the value from w0 to season_index

                        // Get the proper suffix for the day
                        cmp     argv2_r, 1                              // Compare the value in argv2_r and 1
                        b.eq    suffix_st                               // Branch to the label 'suffix_st' if the value in argv2_r equals to 1
                        cmp     argv2_r, 21                             // Compare the value in argv2_r and 21
                        b.eq    suffix_st                               // Branch to the label 'suffix_st' if the value in argv2_r equals to 21
                        cmp     argv2_r, 31                             // Compare the value in argv2_r and 31
                        b.eq    suffix_st                               // Branch to the label 'suffix_st' if the value in argv2_r equals to 31
                        cmp     argv2_r, 2                              // Compare the value in argv2_r and 2
                        b.eq    suffix_nd                               // Branch to the label 'suffix_nd' if the value in argv2_r equals to 2
                        cmp     argv2_r, 22                             // Compare the value in argv2_r and 22
                        b.eq    suffix_nd                               // Branch to the label 'suffix_nd' if the value in argv2_r equals to 22
                        cmp     argv2_r, 3                              // Compare the value in argv2_r and 3
                        b.eq    suffix_rd                               // Branch to the label 'suffix_rd' if the value in argv2_r equals to 3
                        b       suffix_th                               // Branch to the label 'suffix_th'

suffix_st:              ldr     suffix_r, =suffix0                      // Load the address of the label 'suffix0' to suffix_r
                        b       next3                                   // Branch to the label 'next3'
suffix_nd:              ldr     suffix_r, =suffix1                      // Load the address of the label 'suffix1' to suffix_r
                        b       next3                                   // Branch to the label 'next3'
suffix_rd:              ldr     suffix_r, =suffix2                      // Load the address of the label 'suffix2' to suffix_r
                        b       next3                                   // Branch to the label 'next3'
suffix_th:              ldr     suffix_r, =suffix3                      // Load the address of the label 'suffix3' to suffix_r

next3:                  // Output
                        ldr     x0, =output_message                     // Set output_message as the 1st argument of 'printf'

                        sub     argv1_r, argv1_r, 1                     // Update the value in argv1_r by increasing 1
                        ldr     month_r, =month                         // Load the base address of the 'month' array to month_r
                        ldr     x1, [month_r, argv1_r, sxtw 3]          // Load the value in (month_r+(argv1_r<<3)) to x1
                        mov     w2, argv2_r                             // Copy the value from argv2_r to w2
                        mov     x3, suffix_r                            // Copy the value from suffix_r to x3

                        ldr     season_r, =season                       // Load the base address of the 'season' array to season_r
                        ldr     x4, [season_r, season_index, sxtw 3]    // Load the value in (season_r+(season_index<<3)) to x4

                        bl      printf                                  // Call 'printf'

main_ret:               mov     w0, 0                                   // Set the value in w0 as 0
                        ldp     fp, lr, [sp], 16                        // Restore the state of the FP and LR and post-incrementing SP by +16
                        ret                                             // Return control to the OS
