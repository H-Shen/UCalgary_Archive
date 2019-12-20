/**
 * The file contains all miscellaneous functions we need for the assignment.
 */

#ifndef MISCELLANEOUS_HPP
#define MISCELLANEOUS_HPP

#include <experimental/filesystem>
#include <string>
#include <tuple>

namespace Miscellaneous {

    // Check if filename is a regular file
    bool isRegularFile(const std::string &filename);

    // Check if filename has .glsl suffix
    bool isGlslFile(const std::string &filename);

    // Check if the user has the permission to read the file.
    bool hasReadPermission(const std::experimental::filesystem::path &filename);

    // Convert RGB unsigned integers to the corresponding float between 0.0f-1.0f
    std::tuple<float, float, float> rgbUB2Float(const unsigned int &r, const unsigned int &g, const unsigned int &b);

    // Return min(max(n, lowerBound), upperBound) for n using the lowerBound and the upperBound
    void clamp(double &n, const double &lowerBound, const double &upperBound);

    // Check if a string can be converted to a valid integer between 0-255(inclusive) by using regular expression
    bool isValidRGBValue(const std::string &s);
}

#endif //MISCELLANEOUS_HPP
