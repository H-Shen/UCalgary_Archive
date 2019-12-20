## CPSC 453 Fall 2019 Assignment 1 (Legacy OpenGL)

### Introduction

This program demonstrates how the menger sponge with different stage can be generated recursively from a normal cube. Meanwhile, it also provides the user a camera that is able to rotate the model, zoom in/out at the center of the model, move through three axes in the world space. The program has been tested in Linux's system of the UofC Lab.

### Install

Run the script shell in a Linux's system on the lab by bash

```sh
bash ./compile_and_run.sh
```

### Uninstall

Since there is only one output file, you can just remove it by

```sh
rm -rf ./assignment1_legacy_openGL.out
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

*   The camera has three modes: **ROTATION**, **ZOOM**, **PAN**.

    *   The default mode is **ROTATION**.
    *   Press **Z** to switch mode between **ZOOM** and **ROTATION**.
    *   Press **P** to enter **PAN** or quit **PAN** and move to **ROTATION**
    *   To switch from **PAN** to **ROTATION**/**ZOOM**, you must press **P** to quit **PAN** mode at first.
    *   You can press **R** to restore the default location of the camera and the default type of projection **anytime**.

*   Mouse control has different effects on three modes.

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

### Notice

*   The aspect ratio is kept while resizing the window.

*   Faces in different direction of the menger sponge have different colour, the gradient colour is changing from RGBA(226, 156, 94, 1.0).

*   If near clipping happens, the best way is to reset by pressing **R**.

*   A menu with current status of the camera will be shown from the standard output  where you can interact with the program.