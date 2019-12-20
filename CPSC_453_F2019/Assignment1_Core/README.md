## CPSC 453 Fall 2019 Assignment 1 (Core OpenGL)

### Introduction

This program demonstrates how the menger sponge with different stage can be generated recursively from a normal cube. Meanwhile, it also provides the user a camera that is able to rotate the model, zoom in/out at the center of the model, move through three axes in the world space, the user can also choose using fixed color or shading to render the model. The program has been tested in Linux's system of the UofC Lab.

### Install

*   Initiate in a Linux terminal and change the current working directory to the root of ***Assignment1_Core***

*   Execute the following instructions to compile

```sh
cmake .
make
```

### Arguments

*   The user can switch the type of rendering by providing arguments from standard input.

*   To show the usage, please run by

```sh
./assignment1_core_openGL.out --help
```

*   For rendering using fixed-color, please run by

```sh
./assignment1_core_openGL.out
```

*   For rendering using shading, please run by

```sh
./assignment1_core_openGL.out --shading
```

*   For rendering using shading with custom color of the light source, please run by

```sh
./assignment1_core_openGL.out --shading red_value green_value blue_value
# ./assignment1_core_openGL.out --shading 205 89 75
```

*   A usage will be shown if any invalid arguments provided.

### Uninstall

You may clean the temporary output files by running

```sh
rm -rf CMakeCache.txt
rm -rf ./CMakeFiles/
rm -rf Makefile
rm -rf assignment1_core_openGL.out
rm -rf cmake_install.cmake
```

### Usage

*   To draw the Menger sponge with different stage, you can

    *   Draw an ordinary cube by pressing **0**.
    *   Draw the stage-1 Menger sponge by pressing **1**.
    *   Draw the stage-2 Menger sponge by pressing **2**.
    *   Draw the stage-3 Menger sponge by pressing **3**.
    *   Draw the stage-4 Menger sponge by pressing **4**.
    *   You can redraw by switching the stage anytime.

*   Press **O** to switch the projection between perspective/parallel.

*   Press **S** to print the size of the current window.

*   Press **V** to print the version information.

*   Press **H** to print the help information.

*   Press **Esc** to quit the program.

*   The camera has three modes: **ROTATION**, **ZOOM**, **PAN**. Another mode **LIGHT-PAN** is provided if shading is used.

    *   The default mode is **ROTATION**.
    *   Press **Z** to switch mode between **ZOOM** and **ROTATION**.
    *   Press **P** to enter **PAN** or quit **PAN** and move to **ROTATION**.
    *   Press **L** to enter **LIGHT-PAN** or quit **LIGHT-PAN** and move to **ROTATION**.
    *   To switch from **PAN** to **ROTATION**/**ZOOM**/**LIGHT-PAN**, you must press **P** to quit **PAN** mode at first.
    *   To switch from **LIGHT-PAN** to **ROTATION**/**ZOOM**/**PAN**, you must press **L** to quit **LIGHT-PAN** mode at first.
    *   You can press **R** to restore the default location of the camera and the light source and the default type of projection **anytime**.

*   Mouse control has different effects on three modes. (Four modes if shading is used)

    *   In **ROTATION**:
        
        *   Drag mouse-left-button left/right to rotate the model along y-axis.
        *   Drag mouse-right-button forward/backward to rotate the model along x-axis.
        *   Drag mouse-scroll-wheel(press it, not scroll it) to rotate the model along z-axis.
    
    *   In **ZOOM**:
    
        *   Drag mouse-left-button forward/backward to zoom in/out.
    
    *   In **PAN**:
    
        *   Drag mouse-left-button left/right to move the camera along x-axis left/right.
        *   Drag mouse-right-button forward/backward to move the camera along y-axis up/down.
        *   Drag mouse-scroll-wheel(press it, not scroll it) forward/backward to move the camera along z-axis forward/backward.

    *   In **LIGHT-PAN**:

        *   Drag mouse-left-button left/right to move the cube light source along x-axis left/right.
        
        *   Drag mouse-right-button forward/backward to move the cube light source along y-axis up/down.

        *   Drag mouse-scroll-wheel(press it, not scroll it) forward/backward to move the cube light source along z-axis forward/backward.

### Notice

*   The aspect ratio is kept while resizing the window.

*   When using fixed-color, faces in different direction of the menger sponge have different colour, the gradient colour is changing from RGBA(106, 220, 151, 1.0).

*   When the shading is used, all faces of the menger sponge itself have the same color RGBA(204, 120, 50, 1.0) and the default color of the light source is RGBA(255, 255, 255, 0)

*   If near clipping happens, the best way is to reset by pressing **R**.

*   A menu with the current status of the camera will be shown in the standard output where you can interact with the program.