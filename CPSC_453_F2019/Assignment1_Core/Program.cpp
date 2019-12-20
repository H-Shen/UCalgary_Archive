/**
 * The file is the implementation of Program.hpp.
 */

#include <string>

#include "Program.hpp"
#include "RenderingEngine.hpp"
#include "Scene.hpp"
#include "ColorOutput.hpp"
#include "Miscellaneous.hpp"
#include "Constants.hpp"

#include "glad/glad.h"
#include <GLFW/glfw3.h>

// Initialization of static members in Program
int Program::width = 0;
int Program::height = 0;
bool Program::refreshMenu = true;

int Program::getWidth() {
    return width;
}

int Program::getHeight() {
    return height;
}

Program::Program() {
    setupRC();
}

Program::~Program() {
    glfwDestroyWindow(window);
    glfwTerminate();
}

void Program::start() {

    // Initialize the rendering engine and our shaders will be compiled
    renderingEngine = std::make_shared<RenderingEngine>();

    // Initialize the scene for drawing objects
    scene = std::make_shared<Scene>(renderingEngine);

    // Enter the main render loop
    while (!glfwWindowShouldClose(window)) {

        // Render objects
        scene->displayScene();

        // Show menu for the user interaction and only refresh when the user inputs
        if (refreshMenu) {
            refreshMenu = false;
            showMenu();
        }

        glfwSwapBuffers(window);
        glfwPollEvents();
    }
}

void Program::setupRC() {

    // Set the custom error callback function before initialization
    // Errors will be printed to the console
    glfwSetErrorCallback(errorCallback);

    // Initialize the GLFW windowing system, exit if it fails to initialize
    if (!glfwInit()) {
        fprintf(stderr, "ERROR: GLFW failed to initialize, TERMINATING\n");
        exit(EXIT_FAILURE);
    }

    // Create a window with an OpenGL minimum version 4.1 core profile context
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
#ifdef __APPLE__
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
#endif

    // Use 4xMSAA
    glfwWindowHint(GLFW_SAMPLES, 4);

    // Make the window resizable
    glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);

    // We get the maximum width and the maximum height of the monitor before the creation of the window
    // And we create a window only half size of that width and that height
    // Then we restrict the aspect ratio by using GLFW
    // Thus the aspect ratio will not change when the user re-sizes the window,
    // even though the user maximizes it.
    const auto vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
    width = vidmode->width;
    height = vidmode->height;
    window = glfwCreateWindow(width, height, u8"CPSC453 Fall2019 Assignment1 (Core OpenGL)", nullptr, nullptr);
    if (!window) {
        fprintf(stderr, "Program failed to create GLFW window, TERMINATING\n");
        glfwTerminate();
        exit(EXIT_FAILURE);
    }

    // Set the size of the window as 1/4 of the screen
    glfwSetWindowSize(window, width / 2, height / 2);

    // Set the window at the center of the screen
    glfwSetWindowPos(window, width / 4, height / 4);

    // Set the custom function that tracks key presses
    glfwSetKeyCallback(window, keyCallback);

    // Set the callback executed when the window is closed
    glfwSetWindowCloseCallback(window, windowCloseCallback);

    // Set the callback executed when
    // 1. the cursor of the mouse is moved
    // 2. meanwhile a mouse button is pressed
    glfwSetCursorPosCallback(window, mouseCursorCallback);

    // Set the call executed when the user presses the mouse's button
    glfwSetMouseButtonCallback(window, mouseButtonCallback);

    // Set the framebuffer resize callback for the specified window.
    glfwSetFramebufferSizeCallback(window, frameBufferSizeCallback);

    // Restrict to the aspect ratio
    glfwSetWindowAspectRatio(window, vidmode->width, vidmode->height);

    // Set the current OpenGL context
    glfwMakeContextCurrent(window);

    // Initialize GLAD (finds appropriate OpenGL configuration for your system)
    if (!gladLoadGL()) {
        fprintf(stderr, "GLAD failed to initialize\n");
        exit(EXIT_FAILURE);
    }

    // Enable v-sync
    glfwSwapInterval(1);

    // Define the viewport dimensions
    glViewport(0, 0, width, height);

    // Do not display in wireframe Mode
    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

    // Enable line antialiasing
    glEnable(GL_LINE_SMOOTH);

    // Make sure multi-sample is enabled
    glEnable(GL_MULTISAMPLE);

    // Enable Z-Buffer
    glEnable(GL_DEPTH_TEST);

    // Accept fragment if it is closer to the camera than the former one
    glDepthFunc(GL_LESS);
}

void Program::queryGLVersion() {

    const char *version = reinterpret_cast<const char *>(glGetString(GL_VERSION));
    const char *glsl_version = reinterpret_cast<const char *>(glGetString(GL_SHADING_LANGUAGE_VERSION));
    const char *renderer = reinterpret_cast<const char *>(glGetString(GL_RENDERER));

    fprintf(stdout, "\n");
    ColorOutput::print(ColorOutput::WHITE_BOLD, "OpenGL version: ");
    fprintf(stdout, "%s\n", version);

    ColorOutput::print(ColorOutput::WHITE_BOLD, "GLSL version: ");
    fprintf(stdout, "%s\n", glsl_version);

    ColorOutput::print(ColorOutput::WHITE_BOLD, "Render engine info: ");
    fprintf(stdout, "%s\n", renderer);

    ColorOutput::print(ColorOutput::WHITE_BOLD, "GLFW version:");
    int major, minor, rev;
    glfwGetVersion(&major, &minor, &rev);
    if (major) {
        fprintf(stdout, " Major: %d", major);
    }
    if (minor) {
        fprintf(stdout, " Minor: %d", minor);
    }
    if (rev) {
        fprintf(stdout, " Revision: %d\n", rev);
    }
}

void Program::printWindowSize(GLFWwindow *window) {

    int tempWidth;
    int tempHeight;
    glfwGetWindowSize(window, &tempWidth, &tempHeight);

    fprintf(stdout, "\n");
    ColorOutput::print(ColorOutput::WHITE_BOLD, "Current width  : ");
    fprintf(stdout, "%d\n", tempWidth);
    ColorOutput::print(ColorOutput::WHITE_BOLD, "Current height : ");
    fprintf(stdout, "%d\n", tempHeight);
}

void Program::queryHelpInfo() {

    ColorOutput::print(ColorOutput::WHITE_BOLD, "\nMOUSE CONTROL\n\n");
    fprintf(stdout, "* In ROTATION mode (default)\n"
                    "  1) Drag mouse-left-button left/right to rotate the model along y-axis.\n"
                    "  2) Drag mouse-right-button forward/backward to rotate the model along x-axis.\n"
                    "  3) Drag mouse-scroll-wheel(press it, not scroll it) to rotate the model along z-axis\n\n");
    fprintf(stdout, "* In ZOOM mode: Drag mouse-left-button forward/backward to zoom in/out.\n\n");
    fprintf(stdout, "* In PAN mode:\n"
                    "  1) Drag mouse-left-button left/right to move the camera along x-axis left/right.\n"
                    "  2) Drag mouse-right-button forward/backward to move the camera along y-axis up/down.\n"
                    "  3) Drag mouse-scroll-wheel(press it, not scroll it) forward/backward to move the camera along z-axis forward/backward.\n\n");

    if (RenderingEngine::useShading) {
        fprintf(stdout, "* In LIGHT PAN mode:\n"
                        "  1) Drag mouse-left-button left/right to move the cube light source along x-axis left/right.\n"
                        "  2) Drag mouse-right-button forward/backward to move the cube light source along y-axis up/down.\n"
                        "  3) Drag mouse-scroll-wheel(press it, not scroll it) forward/backward to move the cube light source along z-axis forward/backward.\n\n");
    }

    ColorOutput::print(ColorOutput::WHITE_BOLD, "Switch between different modes:\n\n");
    fprintf(stdout, "  1) When switching from ZOOM/ROTATION to PAN, press 'P'.\n");
    if (RenderingEngine::useShading) {
        fprintf(stdout,
                "  2) When switching from PAN to ZOOM/ROTATION/LIGHT-PAN, quit PAN first by pressing 'P' again, \n"
                "     and you will be in ROTATION.\n");
        fprintf(stdout, "  3) When switching from ZOOM/ROTATION to LIGHT-PAN, press 'L'.\n");
        fprintf(stdout,
                "  4) When switching from LIGHT-PAN to ZOOM/ROTATION/PAN, quit LIGHT-PAN first by pressing 'L' again, \n"
                "     and you will be in ROTATION.\n");
    } else {
        fprintf(stdout, "  2) When switching from PAN to ZOOM/ROTATION, quit PAN first by pressing 'P' again, \n"
                        "     and you will be in ROTATION.\n");
    }
    fprintf(stdout, "  3) When switching from ROTATION/ZOOM to ZOOM/ROTATION, press 'Z'\n");
    fprintf(stdout, "  4) You can switch to default setting by pressing 'R' anytime.\n");
}

void errorCallback(int error, const char *description) {
    fprintf(stderr, "GLFW ERROR Code %d:\n%s\n", error, description);
}

void keyCallback(GLFWwindow *window, int key, int scancode, int action, int mods) {

    if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
        fprintf(stdout, "Exit the program...\n");
        glfwSetWindowShouldClose(window, GL_TRUE);
    } else if (key == GLFW_KEY_H && action == GLFW_PRESS) {
        fprintf(stdout, "%s", Constants::DELIMITER);
        Program::queryHelpInfo();
        fprintf(stdout, "%s", Constants::DELIMITER);
        Program::refreshMenu = true;
    } else if (key == GLFW_KEY_S && action == GLFW_PRESS) {
        fprintf(stdout, "%s", Constants::DELIMITER);
        Program::printWindowSize(window);
        fprintf(stdout, "%s", Constants::DELIMITER);
        Program::refreshMenu = true;
    } else if (key == GLFW_KEY_V && action == GLFW_PRESS) {
        fprintf(stdout, "%s", Constants::DELIMITER);
        Program::queryGLVersion();
        fprintf(stdout, "%s", Constants::DELIMITER);
        Program::refreshMenu = true;
    } else if (key == GLFW_KEY_0 && action == GLFW_PRESS) {
        Scene::currentMengerSpongeStage = 0;
    } else if (key == GLFW_KEY_1 && action == GLFW_PRESS) {
        Scene::currentMengerSpongeStage = 1;
    } else if (key == GLFW_KEY_2 && action == GLFW_PRESS) {
        Scene::currentMengerSpongeStage = 2;
    } else if (key == GLFW_KEY_3 && action == GLFW_PRESS) {
        Scene::currentMengerSpongeStage = 3;
    }
#ifdef __linux__
        else if (key == GLFW_KEY_4 && action == GLFW_PRESS) {
            Scene::currentMengerSpongeStage = 4;
        }
#endif
    else if (key == GLFW_KEY_R && action == GLFW_PRESS) {   // Press 'R' to reset all states

        RenderingEngine::zoomMode = false;
        RenderingEngine::panMode = false;
        RenderingEngine::lightPanMode = false;
        RenderingEngine::isOrtho = false;

        RenderingEngine::isLeftButtonPressed = false;
        RenderingEngine::isRightButtonPressed = false;
        RenderingEngine::isScrollWheelPressed = false;
        RenderingEngine::firstMoveAfterSwitch = true;

        RenderingEngine::angleX = Constants::DEFAULT_ANGLE;
        RenderingEngine::angleY = Constants::DEFAULT_ANGLE;
        RenderingEngine::angleZ = Constants::DEFAULT_ANGLE;

        RenderingEngine::translateX = Constants::DEFAULT_TRANSLATE_X;
        RenderingEngine::translateY = Constants::DEFAULT_TRANSLATE_Y;
        RenderingEngine::translateZ = Constants::DEFAULT_TRANSLATE_Z;
        RenderingEngine::zoom = Constants::DEFAULT_ZOOM;

        RenderingEngine::lightPosition = Constants::DEFAULT_LIGHT_POSITION;

        // update the info
        Program::refreshMenu = true;
    } else if (key == GLFW_KEY_O &&
               action == GLFW_PRESS) {           // Press 'O' to switch between parallel and perspective projection
        RenderingEngine::firstMoveAfterSwitch = true;
        RenderingEngine::isOrtho = !RenderingEngine::isOrtho;
        // update the info
        Program::refreshMenu = true;
    } else if (key == GLFW_KEY_Z && action == GLFW_PRESS) {           // Press 'Z' to switch between zoom/rotation mode
        if (!RenderingEngine::panMode && !RenderingEngine::lightPanMode) {
            RenderingEngine::isLeftButtonPressed = false;
            RenderingEngine::isRightButtonPressed = false;
            RenderingEngine::isScrollWheelPressed = false;
            RenderingEngine::firstMoveAfterSwitch = true;
            RenderingEngine::zoomMode = !RenderingEngine::zoomMode;
            // update the info
            Program::refreshMenu = true;
        }
    } else if (key == GLFW_KEY_P && action == GLFW_PRESS) {         // Press 'P' to switch between PAN/non PAN mode
        if (RenderingEngine::lightPanMode) {
            return;
        }
        RenderingEngine::firstMoveAfterSwitch = true;
        RenderingEngine::zoomMode = false;
        RenderingEngine::isLeftButtonPressed = false;
        RenderingEngine::isRightButtonPressed = false;
        RenderingEngine::isScrollWheelPressed = false;
        RenderingEngine::panMode = !RenderingEngine::panMode;
        // update the info
        Program::refreshMenu = true;
    } else if (key == GLFW_KEY_L &&
               action == GLFW_PRESS) {         // Press 'L' to switch between LIGHT-PAN/non LIGHT-PAN mode
        if (!RenderingEngine::useShading) {
            return;
        }
        if (RenderingEngine::panMode) {
            return;
        }
        RenderingEngine::firstMoveAfterSwitch = true;
        RenderingEngine::zoomMode = false;
        RenderingEngine::isLeftButtonPressed = false;
        RenderingEngine::isRightButtonPressed = false;
        RenderingEngine::isScrollWheelPressed = false;
        RenderingEngine::lightPanMode = !RenderingEngine::lightPanMode;
        // update the info
        Program::refreshMenu = true;
    }
}

void windowCloseCallback(GLFWwindow *window) {
    fprintf(stdout, "Exit the program...\n");
}

void mouseCursorCallback(GLFWwindow *window, double x, double y) {

    double offset_x = 0.0;
    double offset_y = 0.0;
    static double last_x = 0.0;
    static double last_y = 0.0;

    if (RenderingEngine::firstMoveAfterSwitch) {
        RenderingEngine::firstMoveAfterSwitch = false;
    } else {
        offset_x = x - last_x;
        offset_y = y - last_y;
    }

    last_x = x;
    last_y = y;

    Miscellaneous::clamp(offset_x, -Constants::MAXIMAL_OFFSET, Constants::MAXIMAL_OFFSET);
    Miscellaneous::clamp(offset_y, -Constants::MAXIMAL_OFFSET, Constants::MAXIMAL_OFFSET);

    if (RenderingEngine::panMode) {                     // Movement for pan mode
        if (RenderingEngine::isLeftButtonPressed) {
            RenderingEngine::translateX += 0.05f * static_cast<float>(offset_x);
        } else if (RenderingEngine::isRightButtonPressed) {
            RenderingEngine::translateY -= 0.05f * static_cast<float>(offset_y);
        } else if (RenderingEngine::isScrollWheelPressed) {
            RenderingEngine::translateZ -= 0.05f * static_cast<float>(offset_y);
        }
    } else if (RenderingEngine::lightPanMode) {         // Movement for light pan mode
        if (RenderingEngine::isLeftButtonPressed) {
            RenderingEngine::lightPosition.x += 0.1f * static_cast<float>(offset_x);
        } else if (RenderingEngine::isRightButtonPressed) {
            RenderingEngine::lightPosition.y -= 0.1f * static_cast<float>(offset_y);
        } else if (RenderingEngine::isScrollWheelPressed) {
            RenderingEngine::lightPosition.z += 0.1f * static_cast<float>(offset_y);
        }
    } else {                                            // Movement for rotation/zoom mode
        // Movement for rotation mode
        if (!RenderingEngine::zoomMode) {
            // Rotation along y-axis
            if (RenderingEngine::isLeftButtonPressed) {
                RenderingEngine::angleY += 0.5f * static_cast<float>(offset_x);
            }
            // Rotation along x-axis
            if (RenderingEngine::isRightButtonPressed) {
                RenderingEngine::angleX += 0.5f * static_cast<float>(offset_y);
            }
            // Rotation along z-axis
            if (RenderingEngine::isScrollWheelPressed) {
                RenderingEngine::angleZ += 0.5f * static_cast<float>(offset_y);
            }
        } else if (RenderingEngine::isLeftButtonPressed) {
            RenderingEngine::zoom -= 0.01f * static_cast<float>(offset_y);
            // Set a lower bound for zoom make sure the model is always visible in the viewport
            if (RenderingEngine::zoom < Constants::MINIMAL_ZOOM) {
                RenderingEngine::zoom = Constants::MINIMAL_ZOOM;
            }
        }
    }
}

void mouseButtonCallback(GLFWwindow *window, int key, int action, int mods) {

    if (key == GLFW_MOUSE_BUTTON_LEFT) {
        if (action == GLFW_PRESS) {
            RenderingEngine::isLeftButtonPressed = !RenderingEngine::isLeftButtonPressed;
        } else if (action == GLFW_RELEASE) {
            RenderingEngine::isLeftButtonPressed = false;
        }
    } else if (key == GLFW_MOUSE_BUTTON_RIGHT) {
        if (action == GLFW_PRESS) {
            RenderingEngine::isRightButtonPressed = !RenderingEngine::isRightButtonPressed;
        } else if (action == GLFW_RELEASE) {
            RenderingEngine::isRightButtonPressed = false;
        }
    } else {
        if (action == GLFW_PRESS) {
            RenderingEngine::isScrollWheelPressed = !RenderingEngine::isScrollWheelPressed;
        } else if (action == GLFW_RELEASE) {
            RenderingEngine::isScrollWheelPressed = false;
        }
    }

}

void frameBufferSizeCallback(GLFWwindow *window, int width, int height) {
    glViewport(0, 0, width, height);
}

void Program::showMenu() {

    ColorOutput::print(ColorOutput::YELLOW, "\nCONTROL MENU\n\n");

    // Print all draw options
    ColorOutput::print(ColorOutput::WHITE_BOLD, "  DRAW OPTIONS\n");
    fprintf(stdout, "* Draw an ordinary cube          (0)\n");
    fprintf(stdout, "* Draw the stage-1 Menger sponge (1)\n");
    fprintf(stdout, "* Draw the stage-2 Menger sponge (2)\n");
    fprintf(stdout, "* Draw the stage-3 Menger sponge (3)\n");
    fprintf(stdout, "* Draw the stage-4 Menger sponge (4)\n");
    fprintf(stdout, "\n");

    // Print all display options
    ColorOutput::print(ColorOutput::WHITE_BOLD, "  DISPLAY OPTIONS\n");
    fprintf(stdout, "* Perspective/Parallel projection  (O)\n");
    fprintf(stdout, "* Print the current size of window (S)\n");
    fprintf(stdout, "\n");

    // Print all camera options
    ColorOutput::print(ColorOutput::WHITE_BOLD, "  CAMERA OPTIONS\n");
    fprintf(stdout, "* Enter/Quit Pan-Mode               (P)\n");
    fprintf(stdout, "* Enter/Quit Zoom-Mode              (Z)\n");
    fprintf(stdout, "* Reset the location and projection (R)\n");
    fprintf(stdout, "\n");

    // Print all other others
    ColorOutput::print(ColorOutput::WHITE_BOLD, "  OTHERS\n");
    fprintf(stdout, "* Version (V)\n");
    fprintf(stdout, "* Help    (H)\n");
    fprintf(stdout, "* Exit  (Esc)\n");
    fprintf(stdout, "\n");

    // Print all current status
    ColorOutput::print(ColorOutput::WHITE_BOLD, "CURRENT STATUS\n\n");
    fprintf(stdout, "Projection  : ");
    if (RenderingEngine::isOrtho) {
        ColorOutput::print(ColorOutput::LIGHT_CRAY, "PARALLEL\n");
    } else {
        ColorOutput::print(ColorOutput::LIGHT_BLUE, "PERSPECTIVE\n");
    }
    fprintf(stdout, "Current mode: ");
    if (RenderingEngine::panMode) {
        ColorOutput::print(ColorOutput::CRAY, "PAN\n");
    } else if (RenderingEngine::lightPanMode) {
        ColorOutput::print(ColorOutput::CRAY, "LIGHT-PAN\n");
    } else if (RenderingEngine::zoomMode) {
        ColorOutput::print(ColorOutput::GREEN, "ZOOM\n");
    } else {
        ColorOutput::print(ColorOutput::LIGHT_GREEN, "ROTATION\n");
    }
}
