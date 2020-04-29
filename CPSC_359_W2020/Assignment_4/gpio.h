// The addresses of the GPIO registers.
//
// These were originally defined on page 90 - 91 of the Broadcom BCM2837
// ARM Peripherals Manual, which is for the Pi 3 model. The mapping is
// the same for BCM2711 (the Pi 4), except that the base address has been
// changed from 0x3F000000 to 0xFE00000. When we write code to access these
// registers, we specify the ARM physical addresses of the peripherals,
// which now have the address range 0xFE000000 to 0xFEFFFFFF for the BCM2711.
// These addresses are mapped by the VideoCore Memory Management Unit (MMU)
// onto the bus addresses in the range 0x7E000000 to 0x7EFFFFFF.
//
// Note that we are still waiting for official documentation for the
// BCM2711 from the Raspberry Pi organization.

// BCM2711 (Pi 4) Memory Mapped I/O base address
//#define MMIO_BASE       0xFE000000
#define MMIO_BASE       0x3F000000 		//Reverted back to Pi 3 for emulation

// GPIO register addresses
#define GPFSEL0         ((volatile unsigned int *)(MMIO_BASE + 0x00200000))
#define GPFSEL1         ((volatile unsigned int *)(MMIO_BASE + 0x00200004))
#define GPFSEL2         ((volatile unsigned int *)(MMIO_BASE + 0x00200008))
#define GPFSEL3         ((volatile unsigned int *)(MMIO_BASE + 0x0020000C))
#define GPFSEL4         ((volatile unsigned int *)(MMIO_BASE + 0x00200010))
#define GPFSEL5         ((volatile unsigned int *)(MMIO_BASE + 0x00200014))
#define GPSET0          ((volatile unsigned int *)(MMIO_BASE + 0x0020001C))
#define GPSET1          ((volatile unsigned int *)(MMIO_BASE + 0x00200020))
#define GPCLR0          ((volatile unsigned int *)(MMIO_BASE + 0x00200028))
#define GPCLR1          ((volatile unsigned int *)(MMIO_BASE + 0x0020002C))
#define GPLEV0          ((volatile unsigned int *)(MMIO_BASE + 0x00200034))
#define GPLEV1          ((volatile unsigned int *)(MMIO_BASE + 0x00200038))
#define GPEDS0          ((volatile unsigned int *)(MMIO_BASE + 0x00200040))
#define GPEDS1          ((volatile unsigned int *)(MMIO_BASE + 0x00200044))
#define GPREN0          ((volatile unsigned int *)(MMIO_BASE + 0x0020004C))
#define GPREN1          ((volatile unsigned int *)(MMIO_BASE + 0x00200050))
#define GPFEN0          ((volatile unsigned int *)(MMIO_BASE + 0x00200058))
#define GPFEN1          ((volatile unsigned int *)(MMIO_BASE + 0x0020005C))
#define GPHEN0          ((volatile unsigned int *)(MMIO_BASE + 0x00200064))
#define GPHEN1          ((volatile unsigned int *)(MMIO_BASE + 0x00200068))
#define GPLEN0          ((volatile unsigned int *)(MMIO_BASE + 0x00200070))
#define GPLEN1          ((volatile unsigned int *)(MMIO_BASE + 0x00200074))
#define GPAREN0         ((volatile unsigned int *)(MMIO_BASE + 0x0020007C))
#define GPAREN1         ((volatile unsigned int *)(MMIO_BASE + 0x00200080))
#define GPAFEN0         ((volatile unsigned int *)(MMIO_BASE + 0x00200088))
#define GPAFEN1         ((volatile unsigned int *)(MMIO_BASE + 0x0020008C))
#define GPPUD           ((volatile unsigned int *)(MMIO_BASE + 0x00200094))
#define GPPUDCLK0       ((volatile unsigned int *)(MMIO_BASE + 0x00200098))
#define GPPUDCLK1       ((volatile unsigned int *)(MMIO_BASE + 0x0020009C))

// New registers on the BCM2711
// A new mechanism is used to enable or disable pull-ups and pull-downs
// for GPIO pins. Each register is mapped to the following pins:
// GPPUPPDN0: pins 15:0
// GPPUPPDN1: pins 31:16
// GPPUPPDN2: pins 47:32
// GPPUPPDN3: pins 57:48
#define GPPUPPDN0       ((volatile unsigned int *)(MMIO_BASE + 0x002000E4))
#define GPPUPPDN1       ((volatile unsigned int *)(MMIO_BASE + 0x002000E8))
#define GPPUPPDN2       ((volatile unsigned int *)(MMIO_BASE + 0x002000EC))
#define GPPUPPDN3       ((volatile unsigned int *)(MMIO_BASE + 0x002000F0))
