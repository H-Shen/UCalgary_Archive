#include "systimer.h"
#include "gpio.h"
#include "uart.h"
#include "snes_uart.h"

// Taken from uart.c:
// The addresses of the Auxilary Mini UART registers.
//
// These are defined on pages 8 - 9 of the Broadcom BCM2837 ARM Peripherals
// Manual. Note that we specify the ARM physical addresses of the peripherals,
// which have the address range 0xFE000000 to 0xFEFFFFFF. These addresses are
// mapped by the VideoCore Memory Management Unit (MMU) onto the bus addresses
// in the range 0x7E000000 to 0x7EFFFFFF.
#define AUX_IRQ         ((volatile unsigned int *)(MMIO_BASE + 0x00215000))
#define AUX_ENABLE      ((volatile unsigned int *)(MMIO_BASE + 0x00215004))
#define AUX_MU_IO       ((volatile unsigned int *)(MMIO_BASE + 0x00215040))
#define AUX_MU_IER      ((volatile unsigned int *)(MMIO_BASE + 0x00215044))
#define AUX_MU_IIR      ((volatile unsigned int *)(MMIO_BASE + 0x00215048))
#define AUX_MU_LCR      ((volatile unsigned int *)(MMIO_BASE + 0x0021504C))
#define AUX_MU_MCR      ((volatile unsigned int *)(MMIO_BASE + 0x00215050))
#define AUX_MU_LSR      ((volatile unsigned int *)(MMIO_BASE + 0x00215054))
#define AUX_MU_MSR      ((volatile unsigned int *)(MMIO_BASE + 0x00215058))
#define AUX_MU_SCRATCH  ((volatile unsigned int *)(MMIO_BASE + 0x0021505C))
#define AUX_MU_CNTL     ((volatile unsigned int *)(MMIO_BASE + 0x00215060))
#define AUX_MU_STAT     ((volatile unsigned int *)(MMIO_BASE + 0x00215064))
#define AUX_MU_BAUD     ((volatile unsigned int *)(MMIO_BASE + 0x00215068))



////////////////////////////////////////////////////////////////////////////////
//
//  Function:       get_SNES
//
//  Arguments:      none
//
//  Returns:        A short integer with the button presses encoded with 16
//                  bits. 1 means pressed, and 0 means unpressed. Bit 0 is
//                  button B, Bit 1 is button Y, etc. up to Bit 11, which is
//                  button R. Bits 12-15 are always 0.
//
//  Description:    This function mocks up the SNES controller using serial input
//                  Unlike the get_SNES controller, this function blocks until a
//                  character is recieved. 
//  		   
//                  uart_init() must be called before using this function
//
//  Caveats:        Multiple buttons cannot be pressed at once (limitation of serial)
//
// SNES Mapping:
//      __Q__             __E__          
//   __/_____\___________/_____\___
//  /   W                     I    \.
// | A     D     G   H     U     K  |
// |    S    _____________    J     |
//  \_______/             \________/
//
//   Up = W        Down = S        Left = A         Right = D
//   Select = G   
//   Start  = H
//   Y = U         X = I           B = J            A = K
//   Left Shoulder = Q             Right Shoulder = E
//
////////////////////////////////////////////////////////////////////////////////

unsigned short get_SNES()
{
    unsigned short data = 0;
    char *cur;
    unsigned value;
    char c;
    
    // The mapping encodes ASCII characters to bit positions for the SNES
    // controller value, the list goes from bit 0 to bit 11:
    // B,Y,Select,Start,UP,DOWN,LEFT,RIGHT,A,X,L,R
    char mapping[] = "JUGHWSADKIQE"; 
	
    // Delay to emulate the delay required to read the SNES controller
    // 12us latch + 16 bits at 12us each
    microsecond_delay(12+(16*12));

    // Check if there is a character waiting in the UART
    if (*AUX_MU_LSR & 0x1) {
	    // Read in character from UART
	    c = (char)(*AUX_MU_IO);
	
	    // If character is lowercase, then convert to uppercase
	    if (c >= 'a' && c <= 'z')
	        c -= 'a' - 'A';
	
	    // Iterate through our mapping till we either match a character
	    // or there are no characters left to check
	    for (cur = &mapping[0], value = 1; *cur; cur++, value <<= 1) {
	        if (*cur == c) {
		    data |= value;
		    break;
	        }
	    }	
    } 
    // No character waiting = no buttons pressed on SNES controller
	
    // Return the encoded data
    return data;
}