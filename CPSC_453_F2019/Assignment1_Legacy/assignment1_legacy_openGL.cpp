/**
 * This program demonstrates how the menger sponge with different stage can be generated recursively from a normal cube.
 * Meanwhile, it also provides the user a camera that is able to rotate the model, zoom in/out at the center of the
 * model, move through three axes in the world space. The program has been tested in Linux's system of the UofC Lab.
 *
 * Author: Haohu Shen (UCID: 30063099)
 * Date:   2019.10.2
 */

#define GL_SILENCE_DEPRECATION

#include <bits/stdc++.h>
#include <GL/glew.h>

#ifdef __APPLE__
#include <GL/glut.h>
#else
#include <GL/freeglut.h>
#include <GL/freeglut_ext.h>
#endif

struct Vec3f {
    float x, y, z;
    Vec3f(float x, float y, float z) : x(x), y(y), z(z) {}
    Vec3f() : Vec3f(0.0f, 0.0f, 0.0f) {}
};

/**
 * A function that converts value of RGB(0-255) to RGB(0.0f-1.0f)
 * @param r the value of r
 * @param g the value of g
 * @param b the value of b
 * @return a tuple with three floats indicate the corresponding value of r, g, b respectively
 */
inline static
std::tuple<float, float, float> rgbUB2Float(const unsigned int &r, const unsigned int &g, const unsigned int &b) {
    return std::make_tuple<float, float, float>(
            static_cast<float>(r / 255.0 * 1.0),
            static_cast<float>(g / 255.0 * 1.0),
            static_cast<float>(b / 255.0 * 1.0)
    );
}

inline static
void clamp(int &n, const int &lowerBound, const int &upperBound) {
    n = std::min(std::max(n, lowerBound), upperBound);
}

// Parameters for text
const char* delimiter = "\n-----------------------\n";

// Parameters for the model
inline const auto COLOR_TUPLE = rgbUB2Float(226, 156, 94);
constexpr GLfloat GAP = 0.12f;
constexpr GLfloat LENGTH = 4.2f;
constexpr int MINIMAL_STAGE = 0;
constexpr int MAXIMAL_STAGE = 4;

// States for the mouse
inline bool isLeftButtonPressed = false;
inline bool isRightButtonPressed = false;
inline bool isScrollWheelPressed = false;
inline bool firstMoveAfterSwitch = true;
inline int last_x;
inline int last_y;

// States for the camera
constexpr GLfloat DEFAULT_ANGLE = 0.0f;
constexpr GLfloat DEFAULT_TRANSLATE_X = 0.0f;
constexpr GLfloat DEFAULT_TRANSLATE_Y = 0.0f;
constexpr GLfloat DEFAULT_TRANSLATE_Z = 0.0f;
constexpr GLfloat DEFAULT_ZOOM = 0.5f;
constexpr GLfloat MINIMAL_ZOOM = 0.03f;
constexpr GLfloat DEFAULT_FOV = 45.0f;
constexpr int MAXIMAL_OFFSET = 20;

inline GLfloat fov = DEFAULT_FOV;
inline bool isOrtho = false;
inline bool zoomMode = false;
inline bool panMode = false;

// for rotation
inline GLfloat angle_x = DEFAULT_ANGLE;
inline GLfloat angle_y = DEFAULT_ANGLE;
inline GLfloat angle_z = DEFAULT_ANGLE;

// for shift
inline GLfloat translate_x = DEFAULT_TRANSLATE_X;
inline GLfloat translate_y = DEFAULT_TRANSLATE_Y;
inline GLfloat translate_z = DEFAULT_TRANSLATE_Z;

// for zoom
inline GLfloat zoom = DEFAULT_ZOOM;

/**
 * Define all variables and functions for colored stdout.
 */
namespace ColorOutput {

    inline const std::string GREEN("\033[0;32m");
    inline const std::string LIGHT_GREEN("\033[1;92m");
    inline const std::string CRAY("\033[0;36m");
    inline const std::string LIGHT_BLUE("\033[1;94m");
    inline const std::string LIGHT_CRAY("\033[1;96m");
    inline const std::string YELLOW("\033[1;33m");
    inline const std::string DEFAULT("\033[0m");
    inline const std::string WHITE_BOLD("\033[1;37m");

    inline static
    void print(const std::string &colorType, const std::string &output) {
        std::cout << colorType << output << DEFAULT;
    }
}

/**
 * Define a cube.
 */
struct Cube {

    Cube(GLfloat x, GLfloat y, GLfloat z, GLfloat length) {

        length = abs(length);
        GLfloat r = std::get<0>(COLOR_TUPLE);
        GLfloat g = std::get<1>(COLOR_TUPLE);
        GLfloat b = std::get<2>(COLOR_TUPLE);
        GLfloat gap = GAP;

        // Define vertices in counter-clockwise (CCW) order with normal pointing out
        // Front face
        colors.emplace_back(r, g, b);
        vertices.emplace_back(x + length, y + length, z + length);
        vertices.emplace_back(x - length, y + length, z + length);
        vertices.emplace_back(x - length, y - length, z + length);
        vertices.emplace_back(x + length, y - length, z + length);

        r -= gap;
        g -= gap;
        b -= gap;

        // Top face
        colors.emplace_back(r, g, b);
        vertices.emplace_back(x + length, y + length, z - length);
        vertices.emplace_back(x - length, y + length, z - length);
        vertices.emplace_back(x - length, y + length, z + length);
        vertices.emplace_back(x + length, y + length, z + length);

        r -= gap;
        g -= gap;
        b -= gap;

        // Bottom face
        colors.emplace_back(r, g, b);
        vertices.emplace_back(x + length, y - length, z + length);
        vertices.emplace_back(x - length, y - length, z + length);
        vertices.emplace_back(x - length, y - length, z - length);
        vertices.emplace_back(x + length, y - length, z - length);

        r -= gap;
        g -= gap;
        b -= gap;

        // Back face
        colors.emplace_back(r, g, b);
        vertices.emplace_back(x + length, y - length, z - length);
        vertices.emplace_back(x - length, y - length, z - length);
        vertices.emplace_back(x - length, y + length, z - length);
        vertices.emplace_back(x + length, y + length, z - length);

        r -= gap;
        g -= gap;
        b -= gap;

        // Left face
        colors.emplace_back(r, g, b);
        vertices.emplace_back(x - length, y + length, z + length);
        vertices.emplace_back(x - length, y + length, z - length);
        vertices.emplace_back(x - length, y - length, z - length);
        vertices.emplace_back(x - length, y - length, z + length);

        r -= gap;
        g -= gap;
        b -= gap;

        // Right face
        colors.emplace_back(r, g, b);
        vertices.emplace_back(x + length, y + length, z - length);
        vertices.emplace_back(x + length, y + length, z + length);
        vertices.emplace_back(x + length, y - length, z + length);
        vertices.emplace_back(x + length, y - length, z - length);

        // End of setting a color-cube
    }

    //Data structures for storing vertices, colors
    std::vector<Vec3f> vertices;
    std::vector<Vec3f> colors;
};

/**
 * A function that shows help information.
 */
inline static
void queryHelpInfo() {
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
    ColorOutput::print(ColorOutput::WHITE_BOLD, "Switch between different modes:\n\n");
    fprintf(stdout, "  1) When switching from ZOOM/ROTATION to PAN, press 'P'.\n");
    fprintf(stdout, "  2) When switching from PAN to ZOOM/ROTATION, quit PAN first by pressing 'P' again, \n"
                    "     and you will be in ROTATION.\n");
    fprintf(stdout, "  3) When switching from ROTATION/ZOOM to ZOOM/ROTATION, press 'Z'\n");
    fprintf(stdout, "  4) You can switch to default setting by pressing 'R' anytime.\n");

}

/**
 * A function that shows the menu to the stdout.
 */
inline static
void showMenu() {

    ColorOutput::print(ColorOutput::YELLOW, "\nCONTROL MENU\n\n");

    // print all draw options
    ColorOutput::print(ColorOutput::WHITE_BOLD, "  DRAW OPTIONS\n");
    fprintf(stdout, "* Draw an ordinary cube          (0)\n");
    fprintf(stdout, "* Draw the stage-1 Menger sponge (1)\n");
    fprintf(stdout, "* Draw the stage-2 Menger sponge (2)\n");
    fprintf(stdout, "* Draw the stage-3 Menger sponge (3)\n");
    fprintf(stdout, "* Draw the stage-4 Menger sponge (4)\n");
    fprintf(stdout, "\n");

    // print all display options
    ColorOutput::print(ColorOutput::WHITE_BOLD, "  DISPLAY OPTIONS\n");
    fprintf(stdout, "* Perspective/Parallel projection  (O)\n");
    fprintf(stdout, "* Print the current size of window (S)\n");
    fprintf(stdout, "\n");

    // print all camera options
    ColorOutput::print(ColorOutput::WHITE_BOLD, "  CAMERA OPTIONS\n");
    fprintf(stdout, "* Enter/Quit Pan-Mode               (P)\n");
    fprintf(stdout, "* Enter/Quit Zoom-Mode              (Z)\n");
    fprintf(stdout, "* Reset the location and projection (R)\n");
    fprintf(stdout, "\n");

    // print all other others
    ColorOutput::print(ColorOutput::WHITE_BOLD, "  OTHERS\n");
    fprintf(stdout, "* Version (V)\n");
    fprintf(stdout, "* Help    (H)\n");
    fprintf(stdout, "* Exit  (Esc)\n");
    fprintf(stdout, "\n");

    // print all currect status
    ColorOutput::print(ColorOutput::WHITE_BOLD, "CURRENT STATUS\n\n");

    fprintf(stdout, "Projection  : ");
    if (isOrtho) {
        ColorOutput::print(ColorOutput::LIGHT_CRAY, "PARALLEL\n");
    } else {
        ColorOutput::print(ColorOutput::LIGHT_BLUE, "PERSPECTIVE\n");
    }

    fprintf(stdout, "Current mode: ");
    if (panMode) {
        ColorOutput::print(ColorOutput::CRAY, "PAN\n");
    } else if (zoomMode) {
        ColorOutput::print(ColorOutput::GREEN, "ZOOM\n");
    } else {
        ColorOutput::print(ColorOutput::LIGHT_GREEN, "ROTATION\n");
    }
}

inline std::vector<std::vector<Cube> > history;
inline int mengerSpongeStage = 0;

/**
 * A function to draw a cube
 * @param cube
 */
inline static
void drawCube(const Cube &cube) {

    auto colors_iter = cube.colors.cbegin();
    auto vertices_iter = cube.vertices.cbegin();

    while (vertices_iter != cube.vertices.cend()) {
        glBegin(GL_QUADS);
            glColor3f(colors_iter->x, colors_iter->y, colors_iter->z);
            for (int i = 0; i < 4; ++i, ++vertices_iter) {
                glVertex3f(vertices_iter->x, vertices_iter->y, vertices_iter->z);
            }
        glEnd();
        ++colors_iter;
    }
}

/**
 * A function to draw a stage-1 menger-sponge
 * @param output A vector stores all 20 cubes of a stage-1 menger-sponge
 * @param x the value of coord-x of the stage-1 menger-sponge's center
 * @param y the value of coord-y of the stage-1 menger-sponge's center
 * @param z the value of coord-z of the stage-1 menger-sponge's center
 * @param a the value of length of the stage-1 menger-sponge's side
 */
inline static
void createMengerSponge(std::vector<Cube> &output, GLfloat x, GLfloat y, GLfloat z, GLfloat a) {

    // A mengerSponge is consist of 20 cubes
    float halfLength = a / 6.0f;
    float length = halfLength * 2;

    // middle
    output.emplace_back(Cube(x + length, y, z + length, halfLength));
    output.emplace_back(Cube(x - length, y, z + length, halfLength));
    output.emplace_back(Cube(x - length, y, z - length, halfLength));
    output.emplace_back(Cube(x + length, y, z - length, halfLength));

    // up
    output.emplace_back(Cube(x + length, y + length, z + length, halfLength));
    output.emplace_back(Cube(x + length, y + length, z - length, halfLength));
    output.emplace_back(Cube(x, y + length, z + length, halfLength));
    output.emplace_back(Cube(x, y + length, z - length, halfLength));
    output.emplace_back(Cube(x + length, y + length, z, halfLength));
    output.emplace_back(Cube(x - length, y + length, z, halfLength));
    output.emplace_back(Cube(x - length, y + length, z + length, halfLength));
    output.emplace_back(Cube(x - length, y + length, z - length, halfLength));

    // down
    output.emplace_back(Cube(x + length, y - length, z + length, halfLength));
    output.emplace_back(Cube(x + length, y - length, z - length, halfLength));
    output.emplace_back(Cube(x, y - length, z + length, halfLength));
    output.emplace_back(Cube(x, y - length, z - length, halfLength));
    output.emplace_back(Cube(x + length, y - length, z, halfLength));
    output.emplace_back(Cube(x - length, y - length, z, halfLength));
    output.emplace_back(Cube(x - length, y - length, z + length, halfLength));
    output.emplace_back(Cube(x - length, y - length, z - length, halfLength));
}

/**
 * A function that generates menger sponge with specific stage recursively.
 * @param output A vector stores all component cubes of the menger-sponge
 * @param stage the value of the stage
 * @param x the value of coord-x of the menger-sponge's center
 * @param y the value of coord-y of the menger-sponge's center
 * @param z the value of coord-z of the menger-sponge's center
 * @param a the value of length of the menger-sponge's side
 */
inline static
void mengerSpongeRecursion(std::vector<Cube> &output, int stage, GLfloat x, GLfloat y, GLfloat z, GLfloat a) {

    // Base case 1
    // When stage is 0, we construct the model as an ordinary cube.
    if (stage == 0) {
        output.emplace_back(Cube(x, y, z, a / 2));
        return;
    }
    // Base case 2
    if (stage == 1) {
        createMengerSponge(output, x, y, z, a);
        return;
    }

    --stage;
    float new_a = a / 3;

    // middle
    mengerSpongeRecursion(output, stage, x + new_a, y, z + new_a, new_a);
    mengerSpongeRecursion(output, stage, x - new_a, y, z + new_a, new_a);
    mengerSpongeRecursion(output, stage, x - new_a, y, z - new_a, new_a);
    mengerSpongeRecursion(output, stage, x + new_a, y, z - new_a, new_a);

    // up
    mengerSpongeRecursion(output, stage, x + new_a, y + new_a, z + new_a, new_a);
    mengerSpongeRecursion(output, stage, x + new_a, y + new_a, z - new_a, new_a);
    mengerSpongeRecursion(output, stage, x, y + new_a, z + new_a, new_a);
    mengerSpongeRecursion(output, stage, x, y + new_a, z - new_a, new_a);
    mengerSpongeRecursion(output, stage, x + new_a, y + new_a, z, new_a);
    mengerSpongeRecursion(output, stage, x - new_a, y + new_a, z, new_a);
    mengerSpongeRecursion(output, stage, x - new_a, y + new_a, z + new_a, new_a);
    mengerSpongeRecursion(output, stage, x - new_a, y + new_a, z - new_a, new_a);

    // down
    mengerSpongeRecursion(output, stage, x + new_a, y - new_a, z + new_a, new_a);
    mengerSpongeRecursion(output, stage, x + new_a, y - new_a, z - new_a, new_a);
    mengerSpongeRecursion(output, stage, x, y - new_a, z + new_a, new_a);
    mengerSpongeRecursion(output, stage, x, y - new_a, z - new_a, new_a);
    mengerSpongeRecursion(output, stage, x + new_a, y - new_a, z, new_a);
    mengerSpongeRecursion(output, stage, x - new_a, y - new_a, z, new_a);
    mengerSpongeRecursion(output, stage, x - new_a, y - new_a, z + new_a, new_a);
    mengerSpongeRecursion(output, stage, x - new_a, y - new_a, z - new_a, new_a);
}

/**
 * A function that pre-generates all data for menger-sponges from stage 1-4.
 */
inline static
void initializeObjectData() {

    // Parameters of the menger-sponge.
    GLfloat x = 0.0f;
    GLfloat y = 0.0f;
    GLfloat z = 0.0f;
    GLfloat length = LENGTH;

    for (int i = MINIMAL_STAGE; i <= MAXIMAL_STAGE; ++i) {
        std::vector<Cube> output;
        // Recursively generate stage-n menger-sponges
        mengerSpongeRecursion(output, i, x, y, z, length);
        history.emplace_back(output);
    }
}

/**
 * A function that querys opengl version and renderer information.
 */
inline static
void queryGLVersion() {

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
    ColorOutput::print(ColorOutput::WHITE_BOLD, "GLEW version: ");
    fprintf(stdout, "%s\n", glewGetString(GLEW_VERSION));
}

inline static
void display() {

    // Clear color and depth buffers
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    // Store current state
    glPushMatrix();
    {
        glLoadIdentity();

        // Setting camera
        Vec3f eye(5.0f, 1.0f, 8.0f);
        Vec3f target(0.0f, 0.0f, 0.0f);
        Vec3f upVector(0.0f, 1.0f, 0.0f);

        // Both ortho and perspective use this policy
        gluLookAt(eye.x, eye.y, eye.z, target.x, target.y, target.z, upVector.x, upVector.y, upVector.z);
        glTranslatef(translate_x, translate_y, translate_z);

        // Rotate around y-axis
        glRotatef(angle_y, 0.0f, 1.0f, 0.0f);

        // Rotate around x-axis
        glRotatef(angle_x, 1.0f, 0.0f, 0.0f);

        // Rotate around z-axis
        glRotatef(angle_z, 0.0f, 0.0f, 1.0f);

        glScalef(zoom * 2, zoom * 2, zoom * 2);

        // Draw objects
        for (const auto &i : history.at(static_cast<size_t>(mengerSpongeStage))) {
            drawCube(i);
        }

    }
    // Restore the previous state
    glPopMatrix();

    // Start rendering
    glutSwapBuffers();
}

inline static
void mouseCallback(int button, int state, int x, int y) {

    switch (button) {
        // Policy for mouse-left-button
        case GLUT_LEFT_BUTTON:
            if (state == GLUT_DOWN) {
                isLeftButtonPressed = !isLeftButtonPressed;
            } else if (state == GLUT_UP) {
                isLeftButtonPressed = false;
            }
            break;
        // Policy for mouse-right-button
        case GLUT_RIGHT_BUTTON:
            if (state == GLUT_DOWN) {
                isRightButtonPressed = !isRightButtonPressed;
            } else if (state == GLUT_UP) {
                isRightButtonPressed = false;
            }
            break;
        // Policy for mouse-scroll-wheel
        default:
            if (state == GLUT_DOWN) {
                isScrollWheelPressed = !isScrollWheelPressed;
            } else if (state == GLUT_UP) {
                isScrollWheelPressed = false;
            }
            /*if (panMode) {
                if (state == GLUT_DOWN) {
                    isScrollWheelPressed = !isScrollWheelPressed;
                } else if (state == GLUT_UP) {
                    isScrollWheelPressed = false;
                }
            }*/
            break;
    }
}

inline static
void mouseMotionCallback(int x, int y) {

    int offset_x = 0;
    int offset_y = 0;

    if (firstMoveAfterSwitch) {
        firstMoveAfterSwitch = false;
    } else {
        offset_x = x - last_x;
        offset_y = y - last_y;
    }

    last_x = x;
    last_y = y;

    // Set a lower-bound and an upper-bound for offset_x and offset_y respectively
    clamp(offset_x, -MAXIMAL_OFFSET, MAXIMAL_OFFSET);
    clamp(offset_y, -MAXIMAL_OFFSET, MAXIMAL_OFFSET);

    // Movement for pan mode
    if (panMode) {
        if (isLeftButtonPressed) {
            translate_x += 0.05f * static_cast<float>(offset_x);
            glutPostRedisplay();
        } else if (isRightButtonPressed) {
            translate_y -= 0.05f * static_cast<float>(offset_y);
            glutPostRedisplay();
        } else if (isScrollWheelPressed) {
            translate_z -= 0.05f * static_cast<float>(offset_y);
            glutPostRedisplay();
        }
    }

    // Movement for non-pan mode
    else {
        // Movement for rotation mode
        if (!zoomMode) {
            // Rotation along y-axis
            if (isLeftButtonPressed) {
                angle_y += 0.5f * static_cast<float>(offset_x);
                glutPostRedisplay();
            }
            // Rotation along x-axis
            if (isRightButtonPressed) {
                angle_x += 0.5f * static_cast<float>(offset_y);
                glutPostRedisplay();
            }
            // Rotation along z-axis
            if (isScrollWheelPressed) {
                angle_z += 0.5f * static_cast<float>(offset_y);
                glutPostRedisplay();
            }
        }
        else if (isLeftButtonPressed) {
            zoom -= 0.01f * static_cast<float>(offset_y);
            // set a lower bound for zoom make sure the model is always visible in the viewport
            if (zoom < MINIMAL_ZOOM) {
                zoom = MINIMAL_ZOOM;
            }
            glutPostRedisplay();
        }
    }
}

// Reshape to keep the aspect ratio
inline static
void reshape(GLsizei width, GLsizei height) {

    // Compute aspect ratio of the new window
    if (height == 0) {
        height = 1;                // To prevent divide by 0
    }
    GLfloat aspect = static_cast<GLfloat> (width) / static_cast<GLfloat> (height);
    GLfloat aspect_reverse = 1.0f / aspect;

    // Set the viewport to cover the new window
    glViewport(0, 0, width, height);

    glMatrixMode(GL_PROJECTION);                        // Select The Projection Matrix
    glLoadIdentity();                           // Reset The Projection Matrix
    // Calculate The Aspect Ratio Of The Window

    float multiplier = 3.0f;
    if (isOrtho) {
        if (width <= height) {
            glOrtho(-multiplier, multiplier, -multiplier * aspect_reverse, multiplier * aspect_reverse, -10.0f, 100.0f);
        } else {
            glOrtho(-multiplier * aspect, multiplier * aspect, -multiplier, multiplier, -10.0f, 100.0f);
        }
    } else {
        gluPerspective(fov, aspect, 2.0f, 100.0f);
    }

    glMatrixMode(GL_MODELVIEW);                     // Select The Modelview Matrix
    glLoadIdentity();                           // Reset The Modelview Matrix
}

/**
 * A function that sets up the render context.
 */
inline static
void setupRC() {

    // Enable smooth shading
    glShadeModel(GL_SMOOTH);

    // Set background color to black and opaque
    glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

    // Depth Buffer Setup
    glClearDepth(1.0f);

    // Enables Depth Testing and define the test type
    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LEQUAL);

    // Use really nice perspective calculations
    glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);

#ifdef __linux__
    // Enables 8xMSAA
    glutSetOption(GLUT_MULTISAMPLE, 8);
#endif
}

inline static
void printWindowSize() {
    fprintf(stdout, "\n");
    ColorOutput::print(ColorOutput::WHITE_BOLD, "Current width  : ");
    fprintf(stdout, "%d\n", glutGet(GLUT_WINDOW_WIDTH));
    ColorOutput::print(ColorOutput::WHITE_BOLD, "Current height : ");
    fprintf(stdout, "%d\n", glutGet(GLUT_WINDOW_HEIGHT));
}

inline static
void keyboardCallback(unsigned char key, int x, int y) {

    switch (key) {
        case '0':
            mengerSpongeStage = 0;
            glutPostRedisplay();
            break;
        case '1':
            mengerSpongeStage = 1;
            glutPostRedisplay();
            break;
        case '2':
            mengerSpongeStage = 2;
            glutPostRedisplay();
            break;
        case '3':
            mengerSpongeStage = 3;
            glutPostRedisplay();
            break;
        case '4':
            mengerSpongeStage = 4;
            glutPostRedisplay();
            break;
        case 'z':
            // Make sure there is no conflict between multiple modes
            if (!panMode) {
                isLeftButtonPressed = false;
                isRightButtonPressed = false;
                isScrollWheelPressed = false;
                firstMoveAfterSwitch = true;
                zoomMode = !zoomMode;
                showMenu();
            }
            break;
        // Switch between ortho and perspective
        case 'o':
            firstMoveAfterSwitch = true;
            isOrtho = !isOrtho;
            reshape(glutGet(GLUT_WINDOW_WIDTH), glutGet(GLUT_WINDOW_HEIGHT));
            showMenu();
            glutPostRedisplay();
            break;
        // Switch between pan mode and non-pan mode
        case 'p':
            firstMoveAfterSwitch = true;
            zoomMode = false;
            isLeftButtonPressed = false;
            isRightButtonPressed = false;
            isScrollWheelPressed = false;
            panMode = !panMode;
            showMenu();
            glutPostRedisplay();
            break;
            // Reset to original state
        case 'r':
            // Reset all states
            zoomMode = false;
            isLeftButtonPressed = false;
            isRightButtonPressed = false;
            isScrollWheelPressed = false;
            firstMoveAfterSwitch = true;
            panMode = false;
            isOrtho = false;

            angle_x = DEFAULT_ANGLE;
            angle_y = DEFAULT_ANGLE;
            angle_z = DEFAULT_ANGLE;
            translate_x = DEFAULT_TRANSLATE_X;
            translate_y = DEFAULT_TRANSLATE_Y;
            translate_z = DEFAULT_TRANSLATE_Z;
            zoom = DEFAULT_ZOOM;

            // Redraw
            reshape(glutGet(GLUT_WINDOW_WIDTH), glutGet(GLUT_WINDOW_HEIGHT));
            showMenu();
            glutPostRedisplay();

            break;
        case 's':
            fprintf(stdout, "%s", delimiter);
            printWindowSize();
            fprintf(stdout, "%s", delimiter);
            showMenu();
            break;
        case 'h':
            fprintf(stdout, "%s", delimiter);
            queryHelpInfo();
            fprintf(stdout, "%s", delimiter);
            showMenu();
            break;
        // Press 'v' to query version info
        case 'v':
            fprintf(stdout, "%s", delimiter);
            queryGLVersion();
            fprintf(stdout, "%s", delimiter);
            showMenu();
            break;
        // Press 'Esc' to exit
        case 27:
            fprintf(stdout, "Exit the program...\n");
            exit(EXIT_SUCCESS);
        default:
            break;
    }
}

int main(int argc, char *argv[]) {

    glutInit(&argc, argv);

#ifdef __linux__
    // Specify the version of GL
    glutInitContextVersion(2, 0);
#endif

    glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH | GLUT_MULTISAMPLE);

    // The default size of the window will be decided depending on the size of the screen
    int screen_width = glutGet(GLUT_SCREEN_WIDTH);
    int screen_height = glutGet(GLUT_SCREEN_HEIGHT);
    glutInitWindowSize(screen_width / 2, screen_height / 2);

    // Set the window at the center of the screen
    glutInitWindowPosition(screen_width / 4, screen_height / 4);

    // It must be done before the initialization of glew
    glutCreateWindow("CPSC453 Fall2019 Assignment1 (Legacy OpenGL)");

    // Initialization of GLEW
    GLenum error;
    if ((error = glewInit()) != GLEW_OK) {
        // Notice the user and quit if glew failed to initialize
        fprintf(stderr, "GLEW failed to initialized: %s\n", glewGetErrorString(error));
        exit(EXIT_FAILURE);
    }

    // Initialization of all data of vertices and color before rendering. It is a trade-off since
    // when the user changes the stage of menger-sponges, the renderer can directly obtain the data of color
    // and position of each vertex instead of re-generating them.
    initializeObjectData();

    // Initialize the render context
    setupRC();

    // Register all callbacks here
    glutKeyboardFunc(keyboardCallback);
    glutDisplayFunc(display);
    glutReshapeFunc(reshape);
    glutMouseFunc(mouseCallback);
    glutMotionFunc(mouseMotionCallback);

    // Display the menu
    showMenu();

    // Start the loop
    glutMainLoop();

    return 0;
}
