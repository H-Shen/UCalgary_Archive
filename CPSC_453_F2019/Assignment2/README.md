## CPSC 453 Fall 2019 Assignment 2

### IMPORTANT NOTICE BEFORE GRADING

*  In the lecture slide we call smooth shading as Gouraud shading (Page31, http://algorithmicbotany.org/courses/Fall2019/Lecture11/ShadingAndHiddenSurfaces_2019F_smaller.pdf) and it is different from diffuse-shading described in the textbook (Page233, Fundamentals of Computer Graphics, 4th Edition). Thus, I implemented both shadings in order to eliminate the ambiguity.

*    The 4th specification of the assignment says **The user can change light direction**, thus from my understanding it refers the type of light is **directional light** and not **point light source** since 'direction' is **NOT** an attribute of a 'point light source' but 'position'. Although in the assignment the reference about 'Render settings' mentions a 'light position', I still think satisfying the requirement of 'let user be able to change light direction' has higher priority since it is **mandatory**.

### Introduction

This program implements a viewer of 3D objects specified as polygon meshes and the mesh data are given in the OBJ format. It simulates a directional light to the model, the user can

*   Change the direction of the directional light by pressing the keyboard.
*   Switch the rendering mode between
    *   Wireframe
    *   Flat-shading
    *   Diffuse-shading
    *   Gouraud-shading(smooth-shading)
    *   Phong-shading
*   Rotate/Translate/Scale the model with a mouse.
*   Switch the projection between perspective/parallel projection.
* All four samples have been tested in the program. And the program has been tested in Linux's system of the UofC Lab.

### Install

*   Initiate in a Linux terminal and change the current working directory to the root of ***Assignment2***
*   Execute the following instructions to compile

```sh
cmake .
make
```

### Arguments

*   The user can switch the type of rendering by providing arguments from standard input.
*   To show the usage, please run by

```sh
./assignment2.out --help
```

*   For rendering different model, please run by

```sh
./assignment2.out modelNumber
```

*   A usage will be shown if any invalid arguments provided.

### Uninstall

You may clean the temporary output files by running

```sh
rm -rf CMakeCache.txt
rm -rf ./CMakeFiles/
rm -rf Makefile
rm -rf assignment2.out
rm -rf cmake_install.cmake
```

### Usage

*   To switch between different rendering mode in a model, you can

    *   Render in wireframe by pressing **1**.
    *   Render in flat-shading by pressing **2**.
    *   Render in diffuse-shading by pressing **3**.
    *   Render in Gouraud-shading(smooth-shading) by pressing **4**.
    *   Render in Phong-shading by pressing **5**.
    *   You can re-render by switching the stage anytime.

*   Press **O** to switch the projection between perspective/parallel.
*   Press **S** to print the size of the current window.
*   Press **V** to print the version information.
*   Press **Q** to print the help information.
*   Press **Esc** to quit the program.

*   To control the light direction of the directional light, you can

    *   Press **T**/**G** to control the direction left/right along the x-axis.
    *   Press **Y**/**G** to control the direction up/down along the y-axis.
    *   Press **U**/**J** to control the direction forward/backward along the z-axis.

*   The camera has three modes: **ROTATION**, **ZOOM**, **PAN**.

    *   The default mode is **ROTATION**.
    *   Press **Z** to switch mode between **ZOOM** and **ROTATION**.
    *   Press **P** to enter **PAN** or quit **PAN** and move to **ROTATION**.
    *   To switch from **PAN** to **ROTATION**/**ZOOM**, you must press **P** to quit **PAN** mode at first.
    *   You can press **R** to restore the default location of the camera, the default rendering-mode, the default value of the light direction and the default type of projection **anytime**.

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