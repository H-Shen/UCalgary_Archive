/**
 * The file is the implementation of Miscellaneous.cpp.
 */

#include "Miscellaneous.hpp"
#include <regex>

bool Miscellaneous::isRegularFile(const std::string &filename) {
    return std::experimental::filesystem::is_regular_file(filename);
}

bool Miscellaneous::isGlslFile(const std::string &filename) {
    return std::experimental::filesystem::path(filename).extension() == ".glsl";
}

bool Miscellaneous::hasReadPermission(const std::experimental::filesystem::path &filename) {
    auto perm = std::experimental::filesystem::status(filename).permissions();
    return (perm & std::experimental::filesystem::perms::owner_read) != std::experimental::filesystem::perms::none;
}

std::tuple<float, float, float>
Miscellaneous::rgbUB2Float(const unsigned int &r, const unsigned int &g, const unsigned int &b) {
    return std::make_tuple<float, float, float>(r / 255.0 * 1.0, g / 255.0 * 1.0, b / 255.0 * 1.0);
}

void Miscellaneous::clamp(double &n, const double &lowerBound, const double &upperBound) {
    n = std::min(std::max(n, lowerBound), upperBound);
}

bool Miscellaneous::isValidRGBValue(const std::string &s) {
    const static std::string pattern("^(-?[1-9][0-9]*|0)$");
    const static std::regex r(pattern);
    if (std::regex_match(begin(s), end(s), r) && s.size() <= 3) {
        int tempVal = std::stoi(s);
        return (tempVal >= 0 && tempVal <= 255);
    }
    return false;
}
