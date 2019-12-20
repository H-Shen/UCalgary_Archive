/**
 * This program demonstrates how the menger sponge with different stage can be generated recursively from a normal cube
 * in modern core OpenGL. Meanwhile, it also provides the user a camera that is able to rotate the model, zoom in/out at
 * the center of the model, move through three axes in the world space. The program has been tested in Linux's system of
 * the UofC Lab.
 *
 * Author: Haohu Shen (UCID: 30063099)
 * Date:   2019.10.8
 */

#include <cstring>
#include "Miscellaneous.hpp"
#include "Program.hpp"
#include "RenderingEngine.hpp"

/**
 * Print the usage if the arguments provided are not valid.
 */
inline static
void printUsage() {
    fprintf(stdout, "\nUsage:\n"
                    "./assignment1_core_openGL.out                                            (use fixed color)\n"
                    "./assignment1_core_openGL.out --shading                                  (use shading with default light color)\n"
                    "./assignment1_core_openGL.out --shading red_value green_value blue_value (use shading with custom light color)\n"
                    "\n"
                    "Example:\n"
                    "./assignment1_core_openGL.out --shading 229 110 101\n");
}

/**
 * Validate all arguments provided, prompt the usage and exit the program if any invalid argument detected.
 * @param argc the number of arguments
 * @param argv the string array of arguments
 */
inline static
void argumentsValidation(int argc, char *argv[]) {
    // Case 1: No shading
    if (argc == 1) {
        return;
    }
    // Case 2: Show the usage
    if (argc == 2 && strcmp(argv[1], "--help") == 0) {
        printUsage();
        exit(EXIT_SUCCESS);
    }
    // Case 3: Shading with default light color
    if (argc == 2 && strcmp(argv[1], "--shading") == 0) {
        RenderingEngine::useShading = true;
        return;
    }
    // Case 4: Shading with custom light color
    if (argc == 5 && strcmp(argv[1], "--shading") == 0) {
        if (Miscellaneous::isValidRGBValue(argv[2]) &&
            Miscellaneous::isValidRGBValue(argv[3]) &&
            Miscellaneous::isValidRGBValue(argv[4])) {

            RenderingEngine::useShading = true;

            // Get integers
            auto rValue = std::stoul(argv[2]);
            auto gValue = std::stoul(argv[3]);
            auto bValue = std::stoul(argv[4]);

            // Turn all integers to floats
            auto tempTuple = Miscellaneous::rgbUB2Float(rValue, gValue, bValue);

            // Update the light color
            RenderingEngine::lightColor.x = std::get<0>(tempTuple);
            RenderingEngine::lightColor.y = std::get<1>(tempTuple);
            RenderingEngine::lightColor.z = std::get<2>(tempTuple);

            return;
        }
    }
    // Case 4: Invalid arguments
    fprintf(stderr, "Invalid arguments, program exits.\n");
    printUsage();
    exit(EXIT_FAILURE);
}

int main(int argc, char *argv[]) {
    argumentsValidation(argc, argv);
    Program p;
    p.start();
    return 0;
}
