/** This program demonstrates a viewer of 3D objects specified as polygon meshes and the mesh data are given in the OBJ
 * format. It simulates a directional light to the model, the user can
 *
 * 1. Change the direction of the directional light by pressing the keyboard.
 * 2. Switch the rendering mode between wireframe/flat-shading/diffuse-shading/Gouraud-shading(smooth-shading)/Phong-shading
 * 3. Rotate/Translate/Scale the model with a mouse.
 * 4. Switch the projection between perspective/parallel projection.
 *
 * All four samples have been tested in the program. And the program has been tested in Linux's system of
 * the UofC Lab.
 *
 * IMPORTANT NOTICE BEFORE GRADING:
 *
 * 1. In the lecture slide we call smooth shading as Gouraud shading (Page31,
 *    http://algorithmicbotany.org/courses/Fall2019/Lecture11/ShadingAndHiddenSurfaces_2019F_smaller.pdf) and it is
 *    different from diffuse-shading described in the textbook (Page233, Fundamentals of Computer Graphics, 4th Edition).
 *    Thus I implemented both shadings in order to eliminate the ambiguity.
 *
 * 2. The 4th specification of the assignment says 'The user can change light direction', thus from my understanding
 *    it refers the type of light is 'directional light' and not 'point light source' since 'direction' is NOT an
 *    attribute of a 'point light source' but 'position'. Although in the assignment the reference about 'Render settings'
 *    mentions a 'light position', I still think satisfying the requirement of 'let user be able to change light direction'
 *    has higher priority since it is mandatory. So I take the opposite of 'Light position' mentioned as
 *    the default direction of the directional light.
 *
 * Author: Haohu Shen (UCID: 30063099)
 * Date:   2019.10.29
 */

// #define __CLION

#define STB_IMAGE_IMPLEMENTATION

#include "middleware/stb/stb_image.h"

#include "OBJ_Loader.h"
#include <glm/glm.hpp>
#include <glm/ext.hpp>
#include <glm/glm.h>

#include "glad/glad.h"
#include <GLFW/glfw3.h>

#include <bits/stdc++.h>
#include <experimental/filesystem>

/**
 * Definition of all constants here.
 */

// Constants of the light source (the type of light: directional light)

static const glm::vec3 DEFAULT_LIGHT_DIRECTION = glm::vec3(-10.0f, -10.0f, -10.0f);
static const glm::vec3 DEFAULT_LIGHT_DIRECTION_FOR_SPOT = glm::vec3(10.0f, -10.0f, 10.0f);
static const glm::vec3 DEFAULT_LIGHT_COLOR = glm::vec3(1.0f, 1.0f, 1.0f);
static const GLfloat DEFAULT_LIGHT_DIRECTION_INCREMENT = 1.5f;

// Text constants
static const char *DELIMITER = "\n-----------------------\n";
static const std::string GREEN("\033[0;32m");
static const std::string LIGHT_GREEN("\033[1;92m");
static const std::string CRAY("\033[0;36m");
static const std::string LIGHT_BLUE("\033[1;94m");
static const std::string LIGHT_CRAY("\033[1;96m");
static const std::string YELLOW("\033[1;33m");
static const std::string DEFAULT_COLOR("\033[0m");
static const std::string WHITE_BOLD("\033[1;37m");

// Camera constants
static constexpr GLfloat DEFAULT_FOV = 30.0f;
static constexpr GLfloat DEFAULT_ANGLE = 0.0f;
static constexpr GLfloat DEFAULT_TRANSLATE = 0.0f;
static constexpr GLfloat DEFAULT_ZOOM = 0.5f;
static constexpr GLfloat MINIMAL_ZOOM = 0.03f;

static constexpr glm::vec3 DEFAULT_UP_VECTOR = glm::vec3(0.0f, 1.0f, 0.0f);

static constexpr glm::vec3 DEFAULT_EYE_FOR_TEAPOT = glm::vec3(0.0f, 2.0f, 15.0f);
static constexpr glm::vec3 DEFAULT_TARGET_FOR_TEAPOT = glm::vec3(0.0f, 2.0f, 0.0f);

static constexpr glm::vec3 DEFAULT_EYE_FOR_SPOT = glm::vec3(0.0f, 0.3f, -4.5f);
static constexpr glm::vec3 DEFAULT_TARGET_FOR_SPOT = glm::vec3(0.0f, 0.3f, 0.0f);

static constexpr glm::vec3 DEFAULT_EYE_FOR_NEFERTITI = glm::vec3(0.0f, 10.0f, 45.0f);
static constexpr glm::vec3 DEFAULT_TARGET_FOR_NEFERTITI = glm::vec3(0.0f, 10.0f, 0.0f);

// Mouse constants
static constexpr GLfloat MAXIMAL_OFFSET = 20.0f;

// Model constants
enum class MODEL : int {
    TEAPOT, SPOT, NEFERTITI_LOW, NEFERTITI_HIGH
};
// Rendering mode constants
enum class RENDERING_MODE : int {
    WIREFRAME, FLAT_SHADING, DIFFUSE_SHADING, SMOOTH_SHADING, PHONG_SHADING
};

/**
 * Forward declaration of all classes.
 */
class Scene;

class Program;

class Geometry;

class RenderingEngine;

class Shader;

class Texture;

/**
 * Definition of all static variables shared among all classes, namespaces and functions here
 */
// The parameters of the window
static int windowHeight = 0;
static int windowWidth = 0;

// Global variables for the light
static glm::vec3 lightDirection;
static glm::vec3 lightColor = DEFAULT_LIGHT_COLOR;

// The default number of model
static MODEL modelNumber = MODEL::TEAPOT;

// The default number of rendering mode
static RENDERING_MODE renderMode = RENDERING_MODE::PHONG_SHADING;

// Global variables for the camera
static bool isOrtho = false;
static bool zoomMode = false;
static bool panMode = false;

static glm::vec3 eye;
static glm::vec3 target;
static glm::vec3 upVector = DEFAULT_UP_VECTOR;

static float translateX = DEFAULT_TRANSLATE;
static float translateY = DEFAULT_TRANSLATE;
static float translateZ = DEFAULT_TRANSLATE;

static float zoom = DEFAULT_ZOOM;
static float angleY = DEFAULT_ANGLE;
static float angleX = DEFAULT_ANGLE;
static float angleZ = DEFAULT_ANGLE;

// Global variables for the mouse
static bool isLeftButtonPressed = false;
static bool isRightButtonPressed = false;
static bool isScrollWheelPressed = false;
static bool firstMoveAfterSwitch = false;

// Global variables for the output
static bool refreshMenu = true;

// Global variables for shaders
static std::shared_ptr<Shader> shaderOfModel = nullptr;

/**
 * Forward declaration of all functions defined outside namespaces and classes.
 */
inline static
void errorCallback(int error, const char *description);

inline static
void keyCallback(GLFWwindow *window, int key, int scancode, int action, int mods);

inline static
void windowCloseCallback(GLFWwindow *window);

inline static
void mouseCursorCallback(GLFWwindow *window, double x, double y);

inline static
void mouseButtonCallback(GLFWwindow *window, int key, int action, int mods);

inline static
void frameBufferSizeCallback(GLFWwindow *window, int width, int height);

inline static
void colorPrint(const std::string &colorType, const std::string &output);

inline static
void queryHelpInfo();

inline static
void queryGLVersion();

inline static
void printWindowSize(GLFWwindow *window);

inline static
void showMenu();

inline static
void printLightDirection();

inline static
void argumentsValidation(int argc, char *argv[]);

inline static
void printUsage();

/**
 * A user-defined object for modeling.
 */
class Geometry {
public:

    // Constructor
    Geometry() : vao(0), vertexBuffer(0), normalBuffer(0), uvBuffer(0), colorBuffer(0), ebo(0) {}

    // Destructor
    ~Geometry() = default;

    // Data structures for storing vectors of vertices, normals and colors
    std::vector<glm::vec3> vertices;
    std::vector<glm::vec3> normals;
    std::vector<glm::vec3> colors;
    std::vector<glm::vec2> uvs;

    // Data structure for storing indices of vertices, the index has a type of 'unsigned int'
    std::vector<glm::uvec3> indicesOfVertices;

    // ID numbers of the VAO and VBOs associated with the geometry
    GLuint vao;
    GLuint vertexBuffer;
    GLuint normalBuffer;
    GLuint uvBuffer;
    GLuint colorBuffer;

    // ID number of the Element buffer object associated with the geometry
    GLuint ebo;

    // The texture of the object;
    std::shared_ptr<Texture> texture;

    // Draw mode for how OpenGL interprets primitives
    GLuint drawMode{};
};


/**
 * A collection of all miscellaneous functions we need.
 */
namespace Miscellaneous {

    // Check if filename is a regular file
    bool isRegularFile(const std::string &filename) {
        return std::experimental::filesystem::is_regular_file(filename);
    }

    // Check if filename has .glsl suffix
    bool isGlslFile(const std::string &filename) {
        return std::experimental::filesystem::path(filename).extension() == ".glsl";
    }

    // Check if the user has the permission to read the file.
    bool hasReadPermission(const std::experimental::filesystem::path &filename) {
        auto perm = std::experimental::filesystem::status(filename).permissions();
        return (perm & std::experimental::filesystem::perms::owner_read) != std::experimental::filesystem::perms::none;
    }

    // Convert RGB unsigned integers to the corresponding float between 0.0f-1.0f
    std::tuple<float, float, float> rgbUB2Float(const unsigned int &r, const unsigned int &g, const unsigned int &b) {
        return std::make_tuple<float, float, float>(
                static_cast<float>(r) / 255.0f,
                static_cast<float>(g) / 255.0f,
                static_cast<float>(b) / 255.0f);
    }

    // Return min(max(n, lowerBound), upperBound) for n using the lowerBound and the upperBound
    void clamp(double &n, const double &lowerBound, const double &upperBound) {
        n = std::min(std::max(n, lowerBound), upperBound);
    }

    // Check if a string can be converted to a valid integer between 0-255(inclusive) by using regular expression
    bool isValidRGBValue(const std::string &s) {
        const static std::string pattern("^(-?[1-9][0-9]*|0)$");
        const static std::regex r(pattern);
        if (std::regex_match(begin(s), end(s), r) && s.size() <= 3) {
            int tempVal = std::stoi(s);
            return (tempVal >= 0 && tempVal <= 255);
        }
        return false;
    }

    // Obtain the coord of center of a Geometry object.
    glm::vec3 getCenterCoord(const Geometry &object) {
        glm::vec3 result = glm::vec3(0.0f, 0.0f, 0.0f);
        for (const auto &i : object.vertices) {
            result = result + i;
        }
        result.x /= static_cast<float>(object.vertices.size());
        result.y /= static_cast<float>(object.vertices.size());
        result.z /= static_cast<float>(object.vertices.size());
        return result;
    }

    // Obtain the coord of center of a list of Geometry objects.
    glm::vec3 getCenterCoord(const std::vector<Geometry> &objects) {
        glm::vec3 result = glm::vec3(0.0f, 0.0f, 0.0f);
        for (const auto &i : objects) {
            result += getCenterCoord(i);
        }
        result.x /= static_cast<float>(objects.size());
        result.y /= static_cast<float>(objects.size());
        result.z /= static_cast<float>(objects.size());
        return result;
    }

    // Check if there is an OpenGL error, print the error message and abort the program if it is true.
    void CheckGLErrors(const char *errorLocation = nullptr) {
        bool error = false;
        for (GLenum flag = glGetError(); flag != GL_NO_ERROR; flag = glGetError()) {

            // Print the location is the argument is given
            if (errorLocation) {
                fprintf(stderr, "%s: ", errorLocation);
            }

            fprintf(stderr, "OpenGL ERROR:  ");
            switch (flag) {
                case GL_INVALID_ENUM:
                    fprintf(stderr, "GL_INVALID_ENUM\n");
                    break;
                case GL_INVALID_VALUE:
                    fprintf(stderr, "GL_INVALID_VALUE\n");
                    break;
                case GL_INVALID_OPERATION:
                    fprintf(stderr, "GL_INVALID_OPERATION\n");
                    break;
                case GL_INVALID_FRAMEBUFFER_OPERATION:
                    fprintf(stderr, "GL_INVALID_FRAMEBUFFER_OPERATION\n");
                    break;
                case GL_OUT_OF_MEMORY:
                    fprintf(stderr, "GL_OUT_OF_MEMORY\n");
                    break;
                default:
                    fprintf(stderr, "[unknown error code]\n");
            }
            error = true;
        }
        if (error) {
            exit(EXIT_FAILURE);
        }
    }

    // Calculate the normal vector of a Triangle
    glm::vec3
    calculateNormalOfATriangle(const glm::uvec3 &indexOfATriangleFace, const std::vector<glm::vec3> &vertices) {

        glm::vec3 normal;
        unsigned int v1 = indexOfATriangleFace.x;
        unsigned int v2 = indexOfATriangleFace.y;
        unsigned int v3 = indexOfATriangleFace.z;

        glm::vec3 edge1 = vertices.at(v2) - vertices.at(v1);
        glm::vec3 edge2 = vertices.at(v3) - vertices.at(v1);

        normal = glm::normalize(glm::cross(edge1, edge2));
        return normal;
    }

    // Calculate normal vectors for all triangles using 'calculateNormalOfATriangle'
    std::vector<glm::vec3> normalVectorForAllTriangle(const std::vector<glm::uvec3> &indices_of_vertices,
                                                      const std::vector<glm::vec3> &vertices) {
        std::vector<glm::vec3> result;
        for (const auto &i : indices_of_vertices) {
            result.emplace_back(calculateNormalOfATriangle(i, vertices));
        }
        return result;
    }

    // Calculate the triangle IDs that share a single vertex for the vertex and all triangle IDs given
    // Triangle IDs are indices of 'indices_of_vertices_output' in the function 'simpleObjLoader'
    // Return a hash map where each key-value pair is (index of a vertex, all triangle IDs that share this vertex)
    std::unordered_map<unsigned int, std::unordered_set<unsigned int> > getSharedTriangleForEachVertex(
            const std::vector<glm::uvec3> &indices_of_vertices
    ) {
        std::unordered_map<unsigned int, std::unordered_set<unsigned int> > result;
        for (size_t i = 0; i != indices_of_vertices.size(); ++i) {
            result[indices_of_vertices.at(i).x].insert(static_cast<unsigned int &&>(i));
            result[indices_of_vertices.at(i).y].insert(static_cast<unsigned int &&>(i));
            result[indices_of_vertices.at(i).z].insert(static_cast<unsigned int &&>(i));
        }
        return result;
    }

    // Check if filename has .obj suffix
    bool isObjFile(const std::string &filename) {
        return std::experimental::filesystem::path(filename).extension() == ".obj";
    }

    // A helper function to split a string by using whitespaces with unknown length as delimiter,
    // whose original code refers https://stackoverflow.com/questions/2275135/splitting-a-string-by-whitespace-in-c
    std::vector<std::string> splitByWhitespaces(const std::string &s) {
        std::istringstream iss(s);
        return std::vector<std::string>{std::istream_iterator<std::string>(iss),
                                        std::istream_iterator<std::string>()};
    }

    // A helper function to split a string by using a character delimiter.
    std::vector<std::string> splitByChar(std::string s, const char &delimiter) {
        std::vector<std::string> res;
        std::istringstream f(s);
        std::string temp;
        while (std::getline(f, s, delimiter)) {
            res.emplace_back(s);
        }
        return res;
    }

    /**
     * A simple obj loader. (only tested and used in Teapot)
     * 1.   It only handles triangle faces.
     * 2.   It only handles geometric vertices('v'), texture vertices('vt'), face('f')
     * 3.   It uses Element/Index Buffer Objects when no uv coords or normal coords are provided.
     * 4.   If uv coords or normal coords are provided it use Array Objects instead since there will be an issue about
     *      duplications.
     *
     * Author: Haohu Shen
     */
    bool simpleObjLoader(const std::string &filename, Geometry &object) {

        // Check if the file exists and make sure it is not a directory
        if (!isRegularFile(filename)) {
            return false;
        }
        // Check if the file ends with '.obj'
        if (!isObjFile(filename)) {
            return false;
        }
        // Check if the user has the permission to read the file
        if (!hasReadPermission((const std::experimental::filesystem::path &) filename)) {
            return false;
        }

        std::ifstream ifs(filename);
        if (ifs.good()) {

            // Parse all lines
            std::vector<std::vector<std::string> > contentsWithoutFaces;
            std::vector<std::vector<std::string> > faces;
            std::vector<std::string> currentLine;
            std::string tempStr;

            // We ignore all lines not start with 'v ', 'vt ', 'vn ', 'f '
            while (std::getline(ifs, tempStr)) {
                currentLine = splitByWhitespaces(tempStr);
                if (!currentLine.empty()) {
                    if (currentLine.front() == "v" || currentLine.front() == "vt" || currentLine.front() == "vn") {
                        contentsWithoutFaces.emplace_back(currentLine);
                    } else if (currentLine.front() == "f") {
                        faces.emplace_back(currentLine);
                    }
                }
            }

            std::vector<glm::vec3> vertices;
            std::vector<glm::vec2> uvs;
            std::vector<glm::vec3> normals;

            for (const auto &i : contentsWithoutFaces) {
                if (i.front() == "v") {
                    // Example: v -0.375418 -0.216313 0.0359591
                    vertices.emplace_back(
                            std::stof(i.at(1)),
                            std::stof(i.at(2)),
                            std::stof(i.at(3)));
                } else if (i.front() == "vt") {
                    // Example: vt 0.917129 0.645104
                    uvs.emplace_back(
                            std::stof(i.at(1)),
                            std::stof(i.at(2)));
                } else if (i.front() == "vn") {
                    // Example: vn 0.005098 -0.999771 -0.020782
                    normals.emplace_back(
                            std::stof(i.at(1)),
                            std::stof(i.at(2)),
                            std::stof(i.at(3)));
                }
            }

            // For every face in our three sample models, it only supports 3 patterns,
            // Example of pattern 1 : f 2752 2742 2741 (vertex)
            // Example of pattern 2: f 2747/2546 2748/2547 169/3160 (vertex/uv)
            // Example of pattern 3: f 12/24/55 16/29/56 19/30/57 (vertex/uv/normals)
            // We parse all faces by its corresponding pattern.
            int pattern = 0;
            if (normals.empty() && !uvs.empty()) {
                pattern = 2;
            } else if (!normals.empty() && !uvs.empty()) {
                pattern = 3;
            } else {
                pattern = 1;
            }

            // the result that will be passed to a Geometry instance
            std::vector<glm::uvec3> indices_of_vertices_output;
            std::vector<glm::vec3> vertices_output;
            std::vector<glm::vec2> uvs_output;
            std::vector<glm::vec3> normals_output;

            if (pattern == 1) {

                std::copy(vertices.begin(), vertices.end(), back_inserter(vertices_output));

                // f 2733 2743 2744
                for (const auto &i : faces) {

                    std::string vertex_a = i.at(1);
                    std::string vertex_b = i.at(2);
                    std::string vertex_c = i.at(3);

                    auto vertex_a_index_of_vertex = static_cast<unsigned int>(std::stoul(vertex_a) - 1);
                    auto vertex_b_index_of_vertex = static_cast<unsigned int>(std::stoul(vertex_b) - 1);
                    auto vertex_c_index_of_vertex = static_cast<unsigned int>(std::stoul(vertex_c) - 1);

                    // Used for the calculation of normal vectors
                    indices_of_vertices_output.emplace_back(
                            vertex_a_index_of_vertex,
                            vertex_b_index_of_vertex,
                            vertex_c_index_of_vertex);

                    // We use EBO in pattern 1.
                    object.indicesOfVertices.emplace_back(
                            vertex_a_index_of_vertex,
                            vertex_b_index_of_vertex,
                            vertex_c_index_of_vertex);
                }

                // Map every vertex to its directly connected triangles' ID
                auto vertexToTriangles = getSharedTriangleForEachVertex(indices_of_vertices_output);

                // Calculate the normal for each triangle, stored it in an array of glm::vec3
                std::vector<glm::vec3> normalVectorForEachTriangle = normalVectorForAllTriangle(
                        indices_of_vertices_output, vertices_output);

                // Calculate the normal vector for each vertex
                for (size_t i = 0; i != vertices_output.size(); ++i) {
                    std::unordered_set<unsigned int> triangleIDSet = vertexToTriangles[i];
                    glm::vec3 normalVector(0.0f, 0.0f, 0.0f);

                    // Add sum of all normal vectors in triangleIDSet
                    for (const auto &id : triangleIDSet) {
                        normalVector = normalVector + normalVectorForEachTriangle.at(id);
                    }

                    // Normalize the result to get the average normal vector
                    normalVector = glm::normalize(normalVector);

                    // Store back to the 'normals_output'
                    normals_output.emplace_back(normalVector);
                }
            } else if (pattern == 2) {

                // f 2770/2569 1883/2573 2767/2567
                for (const auto &i : faces) {

                    auto vertex_a = splitByChar(i.at(1), '/');
                    auto vertex_b = splitByChar(i.at(2), '/');
                    auto vertex_c = splitByChar(i.at(3), '/');

                    auto vertex_a_index_of_vertex = static_cast<unsigned int>(std::stoul(vertex_a.at(0)) - 1);
                    auto vertex_a_index_of_uv = static_cast<unsigned int>(std::stoul(vertex_a.at(1)) - 1);

                    auto vertex_b_index_of_vertex = static_cast<unsigned int>(std::stoul(vertex_b.at(0)) - 1);
                    auto vertex_b_index_of_uv = static_cast<unsigned int>(std::stoul(vertex_b.at(1)) - 1);

                    auto vertex_c_index_of_vertex = static_cast<unsigned int>(std::stoul(vertex_c.at(0)) - 1);
                    auto vertex_c_index_of_uv = static_cast<unsigned int>(std::stoul(vertex_c.at(1)) - 1);

                    vertices_output.emplace_back(vertices.at(vertex_a_index_of_vertex));
                    uvs_output.emplace_back(uvs.at(vertex_a_index_of_uv));

                    vertices_output.emplace_back(vertices.at(vertex_b_index_of_vertex));
                    uvs_output.emplace_back(uvs.at(vertex_b_index_of_uv));

                    vertices_output.emplace_back(vertices.at(vertex_c_index_of_vertex));
                    uvs_output.emplace_back(uvs.at(vertex_c_index_of_uv));

                    // Used for the calculation of normal vectors
                    indices_of_vertices_output.emplace_back(
                            vertex_a_index_of_vertex,
                            vertex_b_index_of_vertex,
                            vertex_c_index_of_vertex);
                }

                // Map every vertex to its directly connected triangles' ID
                auto vertexToTriangles = getSharedTriangleForEachVertex(indices_of_vertices_output);

                // Calculate the normal for each triangle, stored it in an array of glm::vec3
                std::vector<glm::vec3> normalVectorForEachTriangle = normalVectorForAllTriangle(
                        indices_of_vertices_output, vertices_output);

                // Calculate the normal vector for each vertex
                for (size_t i = 0; i != vertices_output.size(); ++i) {

                    std::unordered_set<unsigned int> triangleIDSet = vertexToTriangles[i];
                    glm::vec3 normalVector(0.0f, 0.0f, 0.0f);

                    // Add sum of all normal vectors in triangleIDSet
                    int counter = 0;
                    for (const auto &id : triangleIDSet) {
                        normalVector = normalVector + normalVectorForEachTriangle.at(id);
                        ++counter;
                    }

                    // Normalize the result (Average all normal vectors of adjacent faces)
                    normalVector = glm::normalize(normalVector);

                    // Store back to the 'normals_output'
                    normals_output.emplace_back(normalVector);
                }
            } else {

                // f 12/24/55 16/29/56 19/30/57
                for (const auto &i : faces) {

                    std::vector<std::string> vertex_a = splitByChar(i.at(1), '/');
                    std::vector<std::string> vertex_b = splitByChar(i.at(2), '/');
                    std::vector<std::string> vertex_c = splitByChar(i.at(3), '/');

                    auto vertex_a_index_of_vertex = static_cast<unsigned int>(std::stoul(vertex_a.at(0)) - 1);
                    auto vertex_a_index_of_uv = static_cast<unsigned int>(std::stoul(vertex_a.at(1)) - 1);
                    auto vertex_a_index_of_normal = static_cast<unsigned int>(std::stoul(vertex_a.at(2)) - 1);

                    auto vertex_b_index_of_vertex = static_cast<unsigned int>(std::stoul(vertex_b.at(0)) - 1);
                    auto vertex_b_index_of_uv = static_cast<unsigned int>(std::stoul(vertex_b.at(1)) - 1);
                    auto vertex_b_index_of_normal = static_cast<unsigned int>(std::stoul(vertex_a.at(2)) - 1);

                    auto vertex_c_index_of_vertex = static_cast<unsigned int>(std::stoul(vertex_c.at(0)) - 1);
                    auto vertex_c_index_of_uv = static_cast<unsigned int>(std::stoul(vertex_c.at(1)) - 1);
                    auto vertex_c_index_of_normal = static_cast<unsigned int>(std::stoul(vertex_c.at(2)) - 1);

                    vertices_output.emplace_back(vertices.at(vertex_a_index_of_vertex));
                    uvs_output.emplace_back(uvs.at(vertex_a_index_of_uv));
                    normals_output.emplace_back(normals.at(vertex_a_index_of_normal));

                    vertices_output.emplace_back(vertices.at(vertex_b_index_of_vertex));
                    uvs_output.emplace_back(uvs.at(vertex_b_index_of_uv));
                    normals_output.emplace_back(normals.at(vertex_b_index_of_normal));

                    vertices_output.emplace_back(vertices.at(vertex_c_index_of_vertex));
                    uvs_output.emplace_back(uvs.at(vertex_c_index_of_uv));
                    normals_output.emplace_back(normals.at(vertex_c_index_of_normal));
                }
            }

            // Copy the result to object
            for (const auto &i : vertices_output) {
                object.vertices.emplace_back(i);
            }
            for (const auto &i : uvs_output) {
                object.uvs.emplace_back(i);
            }
            for (const auto &i : normals_output) {
                object.normals.emplace_back(i);
            }
            return true;
        }
        return false;
    }

    /**
     * A wrapper of the obj loader implemented in OBJ_Loader.h.
     *
     */
    bool objLoader(const std::string &filename, Geometry &g) {

        // Check if the file exists and make sure it is not a directory
        if (!isRegularFile(filename)) {
            return false;
        }
        // Check if the file ends with '.obj'
        if (!isObjFile(filename)) {
            return false;
        }
        // Check if the user has the permission to read the file
        if (!hasReadPermission((const std::experimental::filesystem::path &) filename)) {
            return false;
        }

        // If the .obj File can be successfully loaded
        if (OBJLoader::Loader Loader; Loader.LoadFile(filename)) {
            // Go through each loaded mesh and out its contents
            for (size_t i = 0; i != Loader.LoadedMeshes.size(); i++) {
                // Copy one of the loaded meshes to be our current mesh
                OBJLoader::Mesh currentTriangleMesh = Loader.LoadedMeshes.at(i);
                //  Go through each vertex and print its number,
                //  position, normal, and texture coordinate
                for (size_t j = 0; j != currentTriangleMesh.Vertices.size(); j++) {
                    g.vertices.emplace_back(
                            currentTriangleMesh.Vertices.at(j).Position.X,
                            currentTriangleMesh.Vertices.at(j).Position.Y,
                            currentTriangleMesh.Vertices.at(j).Position.Z);

                    g.normals.emplace_back(
                            currentTriangleMesh.Vertices.at(j).Normal.X,
                            currentTriangleMesh.Vertices.at(j).Normal.Y,
                            currentTriangleMesh.Vertices.at(j).Normal.Z
                    );
                    g.uvs.emplace_back(
                            currentTriangleMesh.Vertices.at(j).TextureCoordinate.X,
                            currentTriangleMesh.Vertices.at(j).TextureCoordinate.Y
                    );
                }
                //  Go through every 3rd index and print the
                //	triangle that these indices represent
                for (size_t j = 0; j < currentTriangleMesh.Indices.size(); j += 3) {
                    g.indicesOfVertices.emplace_back(
                            currentTriangleMesh.Indices.at(j),
                            currentTriangleMesh.Indices.at(j + 1),
                            currentTriangleMesh.Indices.at(j + 2)
                    );
                }
            }
            return true;
        }
        return false;
    }
}

/**
 * 'Texture' wraps all functions to set up OpenGL buffers for storing textures
 */
class Texture {
public:
    GLuint textureID;
    GLuint target;
    int textureWidth;
    int textureHeight;

    // Initialize texture with the file given
    explicit Texture(const char *filename, GLenum target = GL_TEXTURE_2D) :
            textureID(0),
            target(target),
            textureWidth(0),
            textureHeight(0) {
        int numberOfComponents;
        stbi_set_flip_vertically_on_load(true);
        unsigned char *data = stbi_load(filename,
                                        &textureWidth,
                                        &textureHeight,
                                        &numberOfComponents,
                                        0);
        if (data) {
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);        // Set alignment to 1
            glGenTextures(1, &textureID);
            glBindTexture(this->target, textureID);
            GLuint channels;
            switch (numberOfComponents) {
                case 4:
                    channels = GL_RGBA;
                    break;
                case 3:
                    channels = GL_RGB;
                    break;
                case 2:
                    channels = GL_RG;
                    break;
                case 1:
                    channels = GL_RED;
                    break;
                default:
                    fprintf(stderr, "Invalid Texture Format\n");
                    exit(EXIT_FAILURE);
            }

            glTexParameteri(this->target, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(this->target, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexParameteri(this->target, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameteri(this->target, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            glTexImage2D(this->target, 0, channels, textureWidth, textureHeight, 0, channels, GL_UNSIGNED_BYTE, data);
            glGenerateMipmap(this->target);

            // Clean up and reallocate the space
            glBindTexture(this->target, 0);
            glPixelStorei(GL_UNPACK_ALIGNMENT, 4);    // Restore the default alignment
            stbi_image_free(data);

            // Check if there is en error.
            Miscellaneous::CheckGLErrors((std::string("Loading texture: ") + filename).c_str());

        } else {
            // Print an error message and abort the program if the texture file cannot be loaded successfully.
            fprintf(stderr, "Failed to load the texture file!\n");
            exit(EXIT_FAILURE);
        }
    }

    // Destructor
    ~Texture() {
        DestroyTexture();
    }

    // Deallocate texture-related objects
    void DestroyTexture() {
        glBindTexture(target, 0);
        glDeleteTextures(1, &textureID);
    }
};

/**
 * The 'Shader' class defines all functions and members needed to compile shaders, adjust uniforms in a shader file
 * and link the program.
 */
class Shader {
public:
    GLuint programId;

    // Default constructor is not allowed
    Shader() = delete;

    // Constructor
    Shader(const char *vertexShaderSourceFilename, const char *fragmentShaderSourceFilename,
           const char *geometryShaderSourceFilename = nullptr) : programId(0) {

        // Load shader source from files
        std::string vertexSource = LoadSource(vertexShaderSourceFilename);
        std::string fragmentSource = LoadSource(fragmentShaderSourceFilename);

        // Geometry Shader is optional
        std::string geometrySource;
        if (geometryShaderSourceFilename) {
            geometrySource = LoadSource(geometryShaderSourceFilename);
        }

        // If source code is empty, exit the program as a failure
        if (vertexSource.empty()) {
            fprintf(stderr, "Vertex shader source file empty!\n");
            exit(EXIT_FAILURE);
        }
        if (fragmentSource.empty()) {
            fprintf(stderr, "Fragment shader source file empty!\n");
            exit(EXIT_FAILURE);
        }
        if (geometryShaderSourceFilename && geometrySource.empty()) {
            fprintf(stderr, "Fragment shader source file empty!\n");
            exit(EXIT_FAILURE);
        }

        // Compile shader source into shader objects
        GLuint vertex = CompileShader(GL_VERTEX_SHADER, vertexSource);
        GLuint geometry = 0;
        if (geometryShaderSourceFilename) {
            geometry = CompileShader(GL_GEOMETRY_SHADER, geometrySource);
        }
        GLuint fragment = CompileShader(GL_FRAGMENT_SHADER, fragmentSource);

        // Link shaderOfModel program
        LinkProgram(vertex, geometry, fragment);

        // Deallocate resources
        // Detach shaders
        glDetachShader(programId, vertex);
        if (geometry != 0) {
            glDetachShader(programId, geometry);
        }
        glDetachShader(programId, fragment);

        // Delete shaders
        glDeleteShader(vertex);
        if (geometry != 0) {
            glDeleteShader(geometry);
        }
        glDeleteShader(fragment);
    }

    // Use the shader program
    void use() {
        glUseProgram(programId);
    }

    /** A collection of GLSL uniform setters **/
    // GLSL uniform one int setter
    void setter_int(const std::string &variableName, GLint newValue) const {
        glUniform1i(glGetUniformLocation(programId, variableName.c_str()), newValue);
    }

    // GLSL uniform one unsigned int setter
    void setter_uint(const std::string &variableName, GLuint newValue) const {
        glUniform1ui(glGetUniformLocation(programId, variableName.c_str()), newValue);
    }

    // GLSL uniform one float setter
    void setter_float(const std::string &variableName, GLfloat newValue) const {
        glUniform1f(glGetUniformLocation(programId, variableName.c_str()), newValue);
    }

    // GLSL uniform one double setter
    void setter_double(const std::string &variableName, GLdouble newValue) const {
        glUniform1d(glGetUniformLocation(programId, variableName.c_str()), newValue);
    }

    // GLSL uniform mat4x4(float) setter
    void setter_mat4f(const std::string &variableName, GLboolean transpose, const GLfloat *newValue) const {
        glUniformMatrix4fv(glGetUniformLocation(programId, variableName.c_str()), 1, transpose, newValue);
    }

    // GLSL uniform vec3f setter
    void setter_vec3f(const std::string &variableName, GLfloat *newValue) const {
        glUniform3fv(glGetUniformLocation(programId, variableName.c_str()), 1, newValue);
    }

    // GLSL uniform vec3f setter
    void setter_vec3f(const std::string &variableName, const glm::vec3 &newValue) const {
        glUniform3fv(glGetUniformLocation(programId, variableName.c_str()), 1, &newValue[0]);
    }

private:
    static std::string LoadSource(const std::string &filename) {
        std::string source;
        if (!Miscellaneous::isRegularFile(filename)) {
            fprintf(stderr, "%s is not a valid file!\n", filename.c_str());
            exit(EXIT_FAILURE);
        }
        if (!Miscellaneous::isGlslFile(filename)) {
            fprintf(stderr, "%s is not a GLSL source file!\n", filename.c_str());
            exit(EXIT_FAILURE);
        }
        if (!Miscellaneous::hasReadPermission((const std::experimental::filesystem::path &) filename)) {
            fprintf(stderr, "The current user has no READ permission for the file!\n");
            exit(EXIT_FAILURE);
        }

        std::ifstream input(filename.c_str());
        if (input) {
            std::copy(std::istreambuf_iterator<char>(input),
                      std::istreambuf_iterator<char>(),
                      back_inserter(source));
            input.close();
        } else {
            fprintf(stderr, "ERROR: Could not load shader source from file %s\n", filename.c_str());
            exit(EXIT_FAILURE);
        }

        return source;
    }

    static GLuint CompileShader(GLenum shaderType, const std::string &source) {

        // Obtain the ID of the shader which is allocated by OpenGL
        GLuint shaderObject = glCreateShader(shaderType);

        // Compile the source as a shader of the given type
        const GLchar *source_ptr = source.c_str();
        glShaderSource(shaderObject, 1, &source_ptr, nullptr);
        glCompileShader(shaderObject);

        // Check compile status
        GLint status;
        glGetShaderiv(shaderObject, GL_COMPILE_STATUS, &status);
        if (!status) {
            GLint length;
            glGetShaderiv(shaderObject, GL_INFO_LOG_LENGTH, &length);
            std::string info(static_cast<unsigned long>(length), ' ');
            glGetShaderInfoLog(shaderObject, static_cast<GLsizei>(info.length()), &length, &info[0]);
            fprintf(stderr, "ERROR compiling shader:\n\n%s\n%s\n", source_ptr, info.c_str());
            // Quit if the shader failed to compile
            exit(EXIT_FAILURE);
        }
        return shaderObject;
    }

    void LinkProgram(GLuint vertexShader, GLuint geometryShader, GLuint fragmentShader) {

        // Obtain the ID of the program which is allocated by OpenGL
        programId = glCreateProgram();

        // Attach provided shader objects to this program
        if (vertexShader) {
            glAttachShader(programId, vertexShader);
        }
        if (geometryShader) {
            glAttachShader(programId, geometryShader);
        }
        if (fragmentShader) {
            glAttachShader(programId, fragmentShader);
        }

        // Link the program with given attachments
        glLinkProgram(programId);

        // Check if the linking is successful
        GLint status;
        glGetProgramiv(programId, GL_LINK_STATUS, &status);
        if (!status) {
            GLint length;
            glGetProgramiv(programId, GL_INFO_LOG_LENGTH, &length);
            std::string info(static_cast<unsigned long>(length), ' ');
            glGetProgramInfoLog(programId, static_cast<GLsizei>(info.length()), &length, &info[0]);
            fprintf(stderr, "ERROR linking shader program:\n%s\n", info.c_str());
            // Quit if the linking is unsuccessful
            exit(EXIT_FAILURE);
        }
    }
};

/**
 * The 'RenderingEngine' defines all stuff about VAO, VBO, EBO and camera.
 */
class RenderingEngine {
public:
    // Constructor
    explicit RenderingEngine() {
        // Set the locations of all shaders' source code
        const char *vertexShaderOfModel = nullptr;
        const char *geometryShaderOfModel = nullptr;
        const char *fragmentShaderOfModel = nullptr;

        // The shader file we loaded depends on the number of model we chose
#ifdef __CLION
        switch (modelNumber) {
            // Teapot
            case MODEL::TEAPOT : {
                vertexShaderOfModel = "/Users/hshen/CLionProject/Assignment2/shaders/teapot_vertex.glsl";
                fragmentShaderOfModel = "/Users/hshen/CLionProject/Assignment2/shaders/teapot_fragment.glsl";
                break;
            }
                // Spot
            case MODEL::SPOT : {
                vertexShaderOfModel = "/Users/hshen/CLionProject/Assignment2/shaders/spot_vertex.glsl";
                fragmentShaderOfModel = "/Users/hshen/CLionProject/Assignment2/shaders/spot_fragment.glsl";
                break;
            }
                // Nefertiti
            default : {
                vertexShaderOfModel = "/Users/hshen/CLionProject/Assignment2/shaders/nefertiti_vertex.glsl";
                fragmentShaderOfModel = "/Users/hshen/CLionProject/Assignment2/shaders/nefertiti_fragment.glsl";
                break;
            }
        }
#else
        switch (modelNumber) {
            // Teapot
            case MODEL::TEAPOT : {
                vertexShaderOfModel = "./shaders/teapot_vertex.glsl";
                fragmentShaderOfModel = "./shaders/teapot_fragment.glsl";
                break;
            }
            // Spot
            case MODEL::SPOT : {
                vertexShaderOfModel = "./shaders/spot_vertex.glsl";
                fragmentShaderOfModel = "./shaders/spot_fragment.glsl";
                break;
            }
            // Nefertiti
            default : {
                vertexShaderOfModel = "./shaders/nefertiti_vertex.glsl";
                fragmentShaderOfModel = "./shaders/nefertiti_fragment.glsl";
                break;
            }
        }
#endif
        // Check if all shader programs are successfully initialized
        shaderOfModel = std::make_shared<Shader>(vertexShaderOfModel, fragmentShaderOfModel, geometryShaderOfModel);
        if (!shaderOfModel || !(shaderOfModel->programId)) {
            fprintf(stderr, "Program could not initialize shaders of model, TERMINATING\n");
            exit(EXIT_FAILURE);
        }
    }

    // Destructor
    ~RenderingEngine() {
        // Delete the shader of the model
        glDeleteProgram(shaderOfModel->programId);
        shaderOfModel = nullptr;
    }

    // Render each object
    void RenderScene(const std::vector<Geometry> &objects) {

        // Refresh the screen by a black fully-opaque background
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Set camera here (and we will have projection and view)
        int height = windowHeight;
        if (height == 0) {
            height = 1;
        }
        int width = windowWidth;
        GLfloat aspect = static_cast<GLfloat> (width) / static_cast<GLfloat> (height);
        auto aspect_reverse = static_cast<GLfloat>(1.0 / aspect);
        glm::mat4 projection;
        if (isOrtho) {
            GLfloat multiplier;
            if (modelNumber == MODEL::NEFERTITI_LOW || modelNumber == MODEL::NEFERTITI_HIGH) {
                multiplier = 12.0f;
            } else if (modelNumber == MODEL::SPOT) {
                multiplier = 1.2f;
            } else {
                multiplier = 3.0f;
            }
            if (width <= height) {
                projection = glm::ortho(-multiplier, multiplier, -multiplier * aspect_reverse,
                                        multiplier * aspect_reverse,
                                        -10.0f, 100.0f);
            } else {
                projection = glm::ortho(-multiplier * aspect, multiplier * aspect, -multiplier, multiplier, -10.0f,
                                        100.0f);
            }
        } else {
            projection = glm::perspective(glm::radians(DEFAULT_FOV), aspect, 2.0f, 100.0f);
        }

        // Set the lookAt function
        glm::mat4 view = glm::lookAt(eye, target, upVector);

        // Zoom in/out
        view = glm::scale(view, glm::vec3(zoom * 2, zoom * 2, zoom * 2));

        // Set uniforms in the shader of model
        shaderOfModel->use();
        shaderOfModel->setter_mat4f("projection", GL_FALSE, &projection[0][0]);
        shaderOfModel->setter_mat4f("view", GL_FALSE, &view[0][0]);

        // Set transformation of the model
        glm::mat4 model = glm::mat4(1.0f);

        // Rotate around y-axis
        model = glm::rotate(model, glm::radians(angleY), glm::vec3(0.0f, 1.0f, 0.0f));

        // Rotate around x-axis
        model = glm::rotate(model, glm::radians(angleX), glm::vec3(1.0f, 0.0f, 0.0f));

        // Rotate around z-axis
        model = glm::rotate(model, glm::radians(angleZ), glm::vec3(0.0f, 0.0f, 1.0f));

        // Shift
        model = glm::translate(model, glm::vec3(translateX, translateY, translateZ));

        shaderOfModel->setter_mat4f("model", GL_FALSE, &model[0][0]);

        // Draw every Geometry object from the 'objects' array.
        for (const Geometry &g : objects) {

            // Bind VAO
            glBindVertexArray(g.vao);

            // If indicesOfVertices is not empty, it means we are gonna use EBO.
            if (!g.indicesOfVertices.empty()) {
                glDrawElements(g.drawMode, static_cast<GLsizei>(3 * g.indicesOfVertices.size()), GL_UNSIGNED_INT,
                               (void *) nullptr);
            } else {
                glDrawArrays(g.drawMode, 0, static_cast<GLsizei>(g.vertices.size()));
            }
            // Restore the state
            glBindVertexArray(0);
        }

        // Tell OpenGL no program will be used for rendering
        glUseProgram(0);

        // Ensure that VAO and VBOs are set up properly, there will be a message if any error occurs and program will abort.
        Miscellaneous::CheckGLErrors("VAO and VBOs are NOT set up properly");
    }

    // Create VAO and VBOs for objects
    static void assignBuffers(Geometry &geometry) {

        // Generate VAO for the object
        glGenVertexArrays(1, &geometry.vao);
        glBindVertexArray(geometry.vao);

        // Generate VBOs for the object (vertex)
        glGenBuffers(1, &geometry.vertexBuffer);
        glBindBuffer(GL_ARRAY_BUFFER, geometry.vertexBuffer);
        glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 0, (void *) nullptr);
        glEnableVertexAttribArray(0);

        // Generate VBOs for the object (color)
        glGenBuffers(1, &geometry.colorBuffer);
        glBindBuffer(GL_ARRAY_BUFFER, geometry.colorBuffer);
        glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 0, (void *) nullptr);
        glEnableVertexAttribArray(1);

        // Generate VBOs for the object (uv)
        glGenBuffers(1, &geometry.uvBuffer);
        glBindBuffer(GL_ARRAY_BUFFER, geometry.uvBuffer);
        glVertexAttribPointer(2, 2, GL_FLOAT, GL_FALSE, 0, (void *) nullptr);
        glEnableVertexAttribArray(2);

        // Generate VBOs for the object (normal)
        glGenBuffers(1, &geometry.normalBuffer);
        glBindBuffer(GL_ARRAY_BUFFER, geometry.normalBuffer);
        glVertexAttribPointer(3, 3, GL_FLOAT, GL_FALSE, 0, (void *) nullptr);
        glEnableVertexAttribArray(3);

        glGenBuffers(1, &geometry.ebo);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, geometry.ebo);
    }

    // Bind and copy data from a Geometry instance to buffers
    static void setBufferData(Geometry &geometry) {

        // Send geometry to the GPU
        // Since it is a setter_int, we bind buffer again although we did this in RenderingEngine::assignBuffers.
        // Must be called whenever anything is updated about the object

        // glBindBuffer is to bind a named buffer object to a target
        // glBufferData is to allocate the space for a buffer object and copy the data stored in the specific address.

        glBindBuffer(GL_ARRAY_BUFFER, geometry.vertexBuffer);
        glBufferData(GL_ARRAY_BUFFER, sizeof(glm::vec3) * geometry.vertices.size(), geometry.vertices.data(),
                     GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, geometry.colorBuffer);
        glBufferData(GL_ARRAY_BUFFER, sizeof(glm::vec3) * geometry.colors.size(), geometry.colors.data(),
                     GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, geometry.uvBuffer);
        glBufferData(GL_ARRAY_BUFFER, sizeof(glm::vec2) * geometry.uvs.size(), geometry.uvs.data(), GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, geometry.normalBuffer);
        glBufferData(GL_ARRAY_BUFFER, sizeof(glm::vec3) * geometry.normals.size(), geometry.normals.data(),
                     GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, geometry.ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(glm::ivec3) * geometry.indicesOfVertices.size(),
                     geometry.indicesOfVertices.data(), GL_STATIC_DRAW);
    }

    // Delete buffers in a Geometry instance
    static void deleteBufferData(Geometry &geometry) {
        glDeleteBuffers(1, &geometry.vertexBuffer);
        glDeleteBuffers(1, &geometry.normalBuffer);
        glDeleteBuffers(1, &geometry.colorBuffer);
        glDeleteBuffers(1, &geometry.uvBuffer);
        glDeleteBuffers(1, &geometry.ebo);
        glDeleteVertexArrays(1, &geometry.vao);
    }
};

/**
 * The class 'Scene' defines a scene where all the models are placed.
 */
class Scene {
public:

    // Constructor
    explicit Scene(std::shared_ptr<RenderingEngine> renderer) : renderer(std::move(renderer)) {

        // Initialization
        drawScene();

        // Texture binding if the model is not a teapot
        if (modelNumber != MODEL::TEAPOT) {
            glActiveTexture(GL_TEXTURE0);
            glBindTexture(objects.front().texture->target, objects.front().texture->textureID);
            shaderOfModel->use();
            glUniform1i(glGetUniformLocation(shaderOfModel->programId, "texture0"), 0);
        }

        // Set the light direction
        if (modelNumber == MODEL::SPOT) {
            lightDirection = DEFAULT_LIGHT_DIRECTION_FOR_SPOT;
        } else {
            lightDirection = DEFAULT_LIGHT_DIRECTION;
        }

        shaderOfModel->use();
        shaderOfModel->setter_int("renderingMode", 5);
        shaderOfModel->setter_vec3f("lightDirection", lightDirection);

        if (modelNumber == MODEL::TEAPOT) {
            // Light properties
            glm::vec3 lightAmbientStrength = glm::vec3(0.25f);
            glm::vec3 lightAmbient = lightColor * lightAmbientStrength;
            glm::vec3 lightDiffuse = lightColor * glm::vec3(0.5f);
            glm::vec3 lightSpecularStrength = {0.5f, 0.5f, 0.5f};
            shaderOfModel->setter_vec3f("lightAmbient", lightAmbient);
            shaderOfModel->setter_vec3f("lightDiffuse", lightDiffuse);
            shaderOfModel->setter_vec3f("lightSpecular", lightSpecularStrength);
            // Material properties
            glm::vec3 materialAmbient = {1.0f, 0.54f, 0.35f};
            glm::vec3 materialDiffuse = {1.0f, 0.54f, 0.35f};
            glm::vec3 materialSpecular = {1.0f, 1.0f, 1.0f};
            shaderOfModel->setter_vec3f("materialAmbient", materialAmbient);
            shaderOfModel->setter_vec3f("materialDiffuse", materialDiffuse);
            shaderOfModel->setter_vec3f("materialSpecular", materialSpecular);
        } else if (modelNumber == MODEL::SPOT) {
            // Light properties
            glm::vec3 lightAmbientStrength = glm::vec3(0.4f);
            glm::vec3 lightAmbient = lightColor * lightAmbientStrength;
            glm::vec3 lightDiffuse = lightColor * glm::vec3(0.7f);
            glm::vec3 lightSpecularStrength = {0.5f, 0.5f, 0.5f};
            shaderOfModel->setter_vec3f("lightAmbient", lightAmbient);
            shaderOfModel->setter_vec3f("lightDiffuse", lightDiffuse);
            shaderOfModel->setter_vec3f("lightSpecular", lightSpecularStrength);
        } else if (modelNumber == MODEL::NEFERTITI_LOW || modelNumber == MODEL::NEFERTITI_HIGH) {
            // Light properties
            glm::vec3 lightAmbientStrength = glm::vec3(0.4f);
            glm::vec3 lightAmbient = lightColor * lightAmbientStrength;
            glm::vec3 lightDiffuse = lightColor * glm::vec3(0.7f);
            glm::vec3 lightSpecularStrength = {0.5f, 0.5f, 0.5f};
            shaderOfModel->setter_vec3f("lightAmbient", lightAmbient);
            shaderOfModel->setter_vec3f("lightDiffuse", lightDiffuse);
            shaderOfModel->setter_vec3f("lightSpecular", lightSpecularStrength);
        }
    }

    // Destructor
    ~Scene() {
        // Delete buffer data of all objects in scene
        cleanScene();
        // Empty the container
        cleanObjects();
    }

    // Send Geometry instance to the renderer
    void displayScene() {
        renderer->RenderScene(objects);
    }

    // Draw the scene with the stage given
    void drawScene() {

        switch (modelNumber) {
            // Teapot
            case MODEL::TEAPOT : {
                // Load .obj file
#ifdef __CLION
                objFile = "/Users/hshen/CLionProject/Assignment2/samples/teapot/teapot_triangulated.obj";
#else
                objFile = "./samples/teapot/teapot_triangulated.obj";
#endif
                Geometry g;
                if (!Miscellaneous::simpleObjLoader(objFile, g)) {
                    fprintf(stderr, "Failed to load the .obj file.\n");
                    exit(EXIT_FAILURE);
                }
                objects.emplace_back(g);
                // Give a color
                auto colorTuple = Miscellaneous::rgbUB2Float(237, 148, 95);
                for (auto &i : objects) {
                    // Add color to each vertex
                    for (size_t count = 0; count != i.vertices.size(); ++count) {
                        i.colors.emplace_back(std::get<0>(colorTuple), std::get<1>(colorTuple),
                                              std::get<2>(colorTuple));
                    }
                    // Add type of primitive
                    i.drawMode = GL_TRIANGLES;
                    // Generate VAO and EBO
                    RenderingEngine::assignBuffers(i);
                    RenderingEngine::setBufferData(i);
                }
                // Adjust the arguments of the camera
                eye = DEFAULT_EYE_FOR_TEAPOT;
                target = DEFAULT_TARGET_FOR_TEAPOT;
                break;
            }
                // Spot
            case MODEL::SPOT : {
                // Load .obj file
#ifdef __CLION
                objFile = "/Users/hshen/CLionProject/Assignment2/samples/spot/spot_triangulated.obj";
#else
                objFile = "./samples/spot/spot_triangulated.obj";
#endif
                Geometry g;
                auto model_obj = glmReadOBJ(const_cast<char *>(objFile.c_str()));
                // fails
                if (!model_obj) {
                    fprintf(stderr, "Failed to load the .obj file.\n");
                    exit(EXIT_SUCCESS);
                }
                // generate normals
                glmFacetNormals(model_obj);
                glmVertexNormals(model_obj, 90.0f);
                // transfer data
                getData(model_obj, GLM_SMOOTH | GLM_TEXTURE, g.vertices, g.uvs, g.normals);
                glmDelete(model_obj);

                objects.emplace_back(g);
                // Add type of primitive and generate VAO, VBO, EBO
                for (auto &i : objects) {
                    // Add type of primitive
                    i.drawMode = GL_TRIANGLES;
                    // Generate VAO and EBO
                    RenderingEngine::assignBuffers(i);
                    RenderingEngine::setBufferData(i);
                }
                // We load texture to the first Geometry in objects.
#ifdef __CLION
                objects.front().texture = std::make_shared<Texture>(
                        "/Users/hshen/CLionProject/Assignment2/samples/spot/spot_texture.png", GL_TEXTURE_2D);
#else
                objects.front().texture = std::make_shared<Texture>("./samples/spot/spot_texture.png", GL_TEXTURE_2D);
#endif
                // Adjust the arguments of the camera
                eye = DEFAULT_EYE_FOR_SPOT;
                target = DEFAULT_TARGET_FOR_SPOT;
                break;
            }
                // Nefertiti_Low
            case MODEL::NEFERTITI_LOW : {
                // Load .obj file
#ifdef __CLION
                objFile = "/Users/hshen/CLionProject/Assignment2/samples/Nefertiti_Low/Nefertiti_Low.obj";
#else
                objFile = "./samples/Nefertiti_Low/Nefertiti_Low.obj";
#endif
                Geometry g;
                if (!Miscellaneous::objLoader(objFile, g)) {
                    fprintf(stderr, "Failed to load the .obj file.\n");
                    exit(EXIT_FAILURE);
                }
                std::vector<glm::uvec3>().swap(g.indicesOfVertices);
                objects.emplace_back(g);

                // Add type of primitive and generate VAO, VBO, EBO
                for (auto &i : objects) {
                    // Add type of primitive
                    i.drawMode = GL_TRIANGLES;
                    // Generate VAO and EBO
                    RenderingEngine::assignBuffers(i);
                    RenderingEngine::setBufferData(i);
                }
                // We load texture to the first Geometry in objects.
#ifdef __CLION
                objects.front().texture = std::make_shared<Texture>(
                        "/Users/hshen/CLionProject/Assignment2/samples/Nefertiti_Low/COLOR_Low.jpg", GL_TEXTURE_2D);
#else
                objects.front().texture = std::make_shared<Texture>("./samples/Nefertiti_Low/COLOR_Low.jpg", GL_TEXTURE_2D);
#endif
                // Adjust the arguments of the camera
                eye = DEFAULT_EYE_FOR_NEFERTITI;
                target = DEFAULT_TARGET_FOR_NEFERTITI;
                break;
            }
                // Nefertiti_High
            default : {
                // Load .obj file
#ifdef __CLION
                objFile = "/Users/hshen/CLionProject/Assignment2/samples/Nefertiti_High/Nefertiti_High.obj";
#else
                objFile = "./samples/Nefertiti_High/Nefertiti_High.obj";
#endif
                Geometry g;
                if (!Miscellaneous::objLoader(objFile, g)) {
                    fprintf(stderr, "Failed to load the .obj file.\n");
                    exit(EXIT_FAILURE);
                }
                std::vector<glm::uvec3>().swap(g.indicesOfVertices);
                objects.emplace_back(g);

                // Give a color (not used in Fragment Shader)
                auto colorTuple = Miscellaneous::rgbUB2Float(237, 148, 95);
                for (auto &i : objects) {
                    // Add color to each vertex
                    for (size_t count = 0; count != i.vertices.size(); ++count) {
                        i.colors.emplace_back(std::get<0>(colorTuple), std::get<1>(colorTuple),
                                              std::get<2>(colorTuple));
                    }
                    // Add type of primitive
                    i.drawMode = GL_TRIANGLES;
                    // Generate VAO and EBO
                    RenderingEngine::assignBuffers(i);
                    RenderingEngine::setBufferData(i);
                }
                // We load texture to the first Geometry in objects.
#ifdef __CLION
                objects.front().texture = std::make_shared<Texture>(
                        "/Users/hshen/CLionProject/Assignment2/samples/Nefertiti_High/COLOR_High.jpg", GL_TEXTURE_2D);
#else
                objects.front().texture = std::make_shared<Texture>("./samples/Nefertiti_High/COLOR_High.jpg", GL_TEXTURE_2D);
#endif
                // Adjust the arguments of the camera
                eye = DEFAULT_EYE_FOR_NEFERTITI;
                target = DEFAULT_TARGET_FOR_NEFERTITI;
                break;
            }
        }
    }

    // Clean the Scene
    void cleanScene() {
        for (auto &i : objects) {
            RenderingEngine::deleteBufferData(i);
        }
    }

    // Clean objects in the container 'objects'
    void cleanObjects() {
        // Since textures are smart pointers in every Geometry instance, we don't need to explicitly reallocate it
        std::vector<Geometry>().swap(objects);
    }

    // The filepath of .obj
    static std::string objFile;

private:
    std::shared_ptr<RenderingEngine> renderer;
    std::vector<Geometry> objects;
};

// Initialization of static members in Scene
std::string Scene::objFile;

/**
 * The 'Program' class controls the main logic of the program.
 */
class Program {
public:
    // Constructor
    Program() {
        setupRC();
    }

    // Destructor
    ~Program() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    // Creates the rendering engine and the scene and does the main draw loop
    void start() {

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

    // Initializes the render context
    void setupRC() {
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

        // We get the maximum window_width and the maximum window_height of the monitor before the creation of the window
        // And we create a window only half size of that window_width and that window_height
        // Then we restrict the aspect ratio by using GLFW
        // Thus the aspect ratio will not change when the user re-sizes the window,
        // even though the user maximizes it.
        const auto vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        windowWidth = vidmode->width;
        windowHeight = vidmode->height;
        window = glfwCreateWindow(windowWidth, windowHeight, u8"CPSC453 Fall2019 Assignment2", nullptr, nullptr);
        if (!window) {
            fprintf(stderr, "Program failed to create GLFW window, TERMINATING\n");
            glfwTerminate();
            exit(EXIT_FAILURE);
        }

        // Set the size of the window as 1/4 of the screen
        glfwSetWindowSize(window, windowWidth / 2, windowHeight / 2);

        // Set the window at the center of the screen
        glfwSetWindowPos(window, windowWidth / 4, windowHeight / 4);

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
        glViewport(0, 0, windowWidth, windowHeight);

        // Enable line and polygon antialiasing
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glEnable(GL_POLYGON_SMOOTH);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);

        // Make sure multi-sample is enabled
        glEnable(GL_MULTISAMPLE);

        // Enable Z-Buffer
        glEnable(GL_DEPTH_TEST);

        // Accept fragment if it is closer to the camera than the former one
        glDepthFunc(GL_LESS);

        // Make sure Counter-Clockwise is the direction of the front face
        glFrontFace(GL_CCW);
    }

private:
    GLFWwindow *window = nullptr;
    std::shared_ptr<RenderingEngine> renderingEngine;
    std::shared_ptr<Scene> scene;
};

/** The definitions of all functions defined outside namespaces, classes and functions**/

// The callback executed when the size of the frame buffer changes
void frameBufferSizeCallback(GLFWwindow *window, int width, int height) {
    glViewport(0, 0, width, height);
}

// The callback executed when an action is taken on a mouse button
void mouseButtonCallback(GLFWwindow *window, int key, int action, int mods) {
    if (key == GLFW_MOUSE_BUTTON_LEFT) {
        if (action == GLFW_PRESS) {
            isLeftButtonPressed = !isLeftButtonPressed;
        } else if (action == GLFW_RELEASE) {
            isLeftButtonPressed = false;
        }
    } else if (key == GLFW_MOUSE_BUTTON_RIGHT) {
        if (action == GLFW_PRESS) {
            isRightButtonPressed = !isRightButtonPressed;
        } else if (action == GLFW_RELEASE) {
            isRightButtonPressed = false;
        }
    } else {
        if (action == GLFW_PRESS) {
            isScrollWheelPressed = !isScrollWheelPressed;
        } else if (action == GLFW_RELEASE) {
            isScrollWheelPressed = false;
        }
    }
}

// The callback executed when a cursor moves
void mouseCursorCallback(GLFWwindow *window, double x, double y) {

    double offset_x = 0.0;
    double offset_y = 0.0;
    static double last_x = 0.0;
    static double last_y = 0.0;

    if (firstMoveAfterSwitch) {
        firstMoveAfterSwitch = false;
    } else {
        offset_x = x - last_x;
        offset_y = y - last_y;
    }

    last_x = x;
    last_y = y;

    Miscellaneous::clamp(offset_x, -MAXIMAL_OFFSET, MAXIMAL_OFFSET);
    Miscellaneous::clamp(offset_y, -MAXIMAL_OFFSET, MAXIMAL_OFFSET);

    if (panMode) {                     // Movement for pan mode
        if (isLeftButtonPressed) {
            translateX += 0.05f * static_cast<float>(offset_x);
        } else if (isRightButtonPressed) {
            translateY -= 0.05f * static_cast<float>(offset_y);
        } else if (isScrollWheelPressed) {
            translateZ -= 0.05f * static_cast<float>(offset_y);
        }
    } else {                           // Movement for rotation/zoom mode
        // Movement for rotation mode
        if (!zoomMode) {
            // Rotation along y-axis
            if (isLeftButtonPressed) {
                angleY += 0.5f * static_cast<float>(offset_x);
            }
            // Rotation along x-axis
            if (isRightButtonPressed) {
                angleX += 0.5f * static_cast<float>(offset_y);
            }
            // Rotation along z-axis
            if (isScrollWheelPressed) {
                angleZ += 0.5f * static_cast<float>(offset_y);
            }
        } else if (isLeftButtonPressed) {
            zoom -= 0.01f * static_cast<float>(offset_y);
            // Set a lower bound for zoom make sure the model is always visible in the viewport
            if (zoom < MINIMAL_ZOOM) {
                zoom = MINIMAL_ZOOM;
            }
        }
    }
}

// The callback executed when the window closes
void windowCloseCallback(GLFWwindow *window) {
    fprintf(stdout, "Exit the program...\n");
}

// The callback executed when an action is taken on a key
void keyCallback(GLFWwindow *window, int key, int scancode, int action, int mods) {

    if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
        fprintf(stdout, "Exit the program...\n");
        glfwSetWindowShouldClose(window, GL_TRUE);
    } else if (key == GLFW_KEY_Q && action == GLFW_PRESS) {
        fprintf(stdout, "%s", DELIMITER);
        queryHelpInfo();
        fprintf(stdout, "%s", DELIMITER);
        refreshMenu = true;
    } else if (key == GLFW_KEY_S && action == GLFW_PRESS) {
        fprintf(stdout, "%s", DELIMITER);
        printWindowSize(window);
        fprintf(stdout, "%s", DELIMITER);
        refreshMenu = true;
    } else if (key == GLFW_KEY_V && action == GLFW_PRESS) {
        fprintf(stdout, "%s", DELIMITER);
        queryGLVersion();
        fprintf(stdout, "%s", DELIMITER);
        refreshMenu = true;
    } else if (key == GLFW_KEY_R && action == GLFW_PRESS) {   // Press 'R' to reset all states

        zoomMode = false;
        panMode = false;
        isOrtho = false;

        isLeftButtonPressed = false;
        isRightButtonPressed = false;
        isScrollWheelPressed = false;
        firstMoveAfterSwitch = true;

        angleX = DEFAULT_ANGLE;
        angleY = DEFAULT_ANGLE;
        angleZ = DEFAULT_ANGLE;

        translateX = DEFAULT_TRANSLATE;
        translateY = DEFAULT_TRANSLATE;
        translateZ = DEFAULT_TRANSLATE;
        zoom = DEFAULT_ZOOM;

        // Restore the direction of the light
        if (modelNumber == MODEL::SPOT) {
            lightDirection = DEFAULT_LIGHT_DIRECTION_FOR_SPOT;
        } else {
            lightDirection = DEFAULT_LIGHT_DIRECTION;
        }
        // Restore the default rendering mode
        renderMode = RENDERING_MODE::PHONG_SHADING;
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glProvokingVertex(GL_LAST_VERTEX_CONVENTION);
        // update the shader
        shaderOfModel->use();
        shaderOfModel->setter_vec3f("lightDirection", lightDirection);
        shaderOfModel->setter_int("renderingMode", 5);

        // update the info
        refreshMenu = true;

    } else if (key == GLFW_KEY_O &&
               action == GLFW_PRESS) {           // Press 'O' to switch between parallel and perspective projection
        firstMoveAfterSwitch = true;
        isOrtho = !isOrtho;
        // update the info
        refreshMenu = true;
    } else if (key == GLFW_KEY_Z && action == GLFW_PRESS) {           // Press 'Z' to switch between zoom/rotation mode
        if (!panMode) {
            isLeftButtonPressed = false;
            isRightButtonPressed = false;
            isScrollWheelPressed = false;
            firstMoveAfterSwitch = true;
            zoomMode = !zoomMode;
            // update the info
            refreshMenu = true;
        }
    } else if (key == GLFW_KEY_P && action == GLFW_PRESS) {         // Press 'P' to switch between PAN/zoom mode
        firstMoveAfterSwitch = true;
        zoomMode = false;
        isLeftButtonPressed = false;
        isRightButtonPressed = false;
        isScrollWheelPressed = false;
        panMode = !panMode;
        // update the info
        refreshMenu = true;
    }
    /**
     * Adjustment of the light direction
     *
     * T for +lightDirection.x
     * G for -lightDirection.x
     * Y for +lightDirection.y
     * H for -lightDirection.y
     * U for +lightDirection.z
     * J for -lightDirection.z
     * Increment: 0.5f
     * A vector of updated light direction will be printed
     */
    else if (key == GLFW_KEY_T && action == GLFW_PRESS) {
        lightDirection.x += DEFAULT_LIGHT_DIRECTION_INCREMENT;
        // update the uniform
        shaderOfModel->use();
        shaderOfModel->setter_vec3f("lightDirection", lightDirection);
        // print the update info
        printLightDirection();
    } else if (key == GLFW_KEY_G && action == GLFW_PRESS) {
        lightDirection.x -= DEFAULT_LIGHT_DIRECTION_INCREMENT;
        // update the uniform
        shaderOfModel->use();
        shaderOfModel->setter_vec3f("lightDirection", lightDirection);
        // print the update info
        printLightDirection();
    } else if (key == GLFW_KEY_Y && action == GLFW_PRESS) {
        lightDirection.y += DEFAULT_LIGHT_DIRECTION_INCREMENT;
        // update the uniform
        shaderOfModel->use();
        shaderOfModel->setter_vec3f("lightDirection", lightDirection);
        // print the update info
        printLightDirection();
    } else if (key == GLFW_KEY_H && action == GLFW_PRESS) {
        lightDirection.y -= DEFAULT_LIGHT_DIRECTION_INCREMENT;
        // update the uniform
        shaderOfModel->use();
        shaderOfModel->setter_vec3f("lightDirection", lightDirection);
        // print the update info
        printLightDirection();
    } else if (key == GLFW_KEY_U && action == GLFW_PRESS) {
        lightDirection.z += DEFAULT_LIGHT_DIRECTION_INCREMENT;
        // update the uniform
        shaderOfModel->use();
        shaderOfModel->setter_vec3f("lightDirection", lightDirection);
        // print the update info
        printLightDirection();
    } else if (key == GLFW_KEY_J && action == GLFW_PRESS) {
        lightDirection.z -= DEFAULT_LIGHT_DIRECTION_INCREMENT;
        // update the uniform
        shaderOfModel->use();
        shaderOfModel->setter_vec3f("lightDirection", lightDirection);
        // print the update info
        printLightDirection();
    }
        // Switch between between different rendering modes by pressing the number 1-5. (Default rendering-mode is Phong-shading)
    else if (key == GLFW_KEY_1 && action == GLFW_PRESS) {
        // Wireframe
        renderMode = RENDERING_MODE::WIREFRAME;
        glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
        glProvokingVertex(GL_LAST_VERTEX_CONVENTION);
        shaderOfModel->use();
        shaderOfModel->setter_int("renderingMode", 1);
        // update the info
        refreshMenu = true;
    } else if (key == GLFW_KEY_2 && action == GLFW_PRESS) {
        // Flat shading
        renderMode = RENDERING_MODE::FLAT_SHADING;
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glProvokingVertex(GL_FIRST_VERTEX_CONVENTION);
        shaderOfModel->use();
        shaderOfModel->setter_int("renderingMode", 2);
        // update the info
        refreshMenu = true;
    } else if (key == GLFW_KEY_3 && action == GLFW_PRESS) {
        // Diffuse shading
        renderMode = RENDERING_MODE::DIFFUSE_SHADING;
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glProvokingVertex(GL_LAST_VERTEX_CONVENTION);
        shaderOfModel->use();
        shaderOfModel->setter_int("renderingMode", 3);
        // update the info
        refreshMenu = true;
    } else if (key == GLFW_KEY_4 && action == GLFW_PRESS) {
        // Smooth shading(Gouraud Shading)
        renderMode = RENDERING_MODE::SMOOTH_SHADING;
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glProvokingVertex(GL_LAST_VERTEX_CONVENTION);
        shaderOfModel->use();
        shaderOfModel->setter_int("renderingMode", 4);
        // update the info
        refreshMenu = true;
    } else if (key == GLFW_KEY_5 && action == GLFW_PRESS) {
        // Phong shading
        renderMode = RENDERING_MODE::PHONG_SHADING;
        glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        glProvokingVertex(GL_LAST_VERTEX_CONVENTION);
        shaderOfModel->use();
        shaderOfModel->setter_int("renderingMode", 5);
        // update the info
        refreshMenu = true;
    }
}

// The callback executed when any error occurs
void errorCallback(int error, const char *description) {
    fprintf(stderr, "GLFW ERROR Code %d:\n%s\n", error, description);
}

// The print function for colored output
void colorPrint(const std::string &colorType, const std::string &output) {
    std::cout << colorType << output << DEFAULT_COLOR;
}

// The function prints the help information if the user queries it.
void queryHelpInfo() {

    colorPrint(WHITE_BOLD, "\nMOUSE CONTROL\n\n");
    fprintf(stdout, "* In ROTATION mode (default)\n"
                    "  1) Drag mouse-left-button left/right to rotate the model along y-axis.\n"
                    "  2) Drag mouse-right-button forward/backward to rotate the model along x-axis.\n"
                    "  3) Drag mouse-scroll-wheel(press it, not scroll it) to rotate the model along z-axis\n\n");
    fprintf(stdout, "* In ZOOM mode: Drag mouse-left-button forward/backward to zoom in/out.\n\n");
    fprintf(stdout, "* In PAN mode:\n"
                    "  1) Drag mouse-left-button left/right to move the camera along x-axis left/right.\n"
                    "  2) Drag mouse-right-button forward/backward to move the camera along y-axis up/down.\n"
                    "  3) Drag mouse-scroll-wheel(press it, not scroll it) forward/backward to move the camera along z-axis forward/backward.\n\n");

    colorPrint(WHITE_BOLD, "Switch between different modes:\n\n");
    fprintf(stdout, "  1) When switching from ZOOM/ROTATION to PAN, press 'P'.\n");
    fprintf(stdout, "  2) When switching from PAN to ZOOM/ROTATION, quit PAN first by pressing 'P' again, \n"
                    "     and you will be in ROTATION.\n");

    fprintf(stdout, "  3) When switching from ROTATION/ZOOM to ZOOM/ROTATION, press 'Z'\n");
    fprintf(stdout, "  4) You can switch to default setting by pressing 'R' anytime.\n");
    fprintf(stdout,
            "  5) Press 'R' will reset the location of the model, the direction of the light, the rendering mode and the type of projection.\n");
}

// Query OpenGL version and renderer information
void queryGLVersion() {

    const char *opengl_version = reinterpret_cast<const char *>(glGetString(GL_VERSION));
    const char *glsl_version = reinterpret_cast<const char *>(glGetString(GL_SHADING_LANGUAGE_VERSION));
    const char *renderer_version = reinterpret_cast<const char *>(glGetString(GL_RENDERER));

    fprintf(stdout, "\n");
    colorPrint(WHITE_BOLD, "OpenGL version: ");
    fprintf(stdout, "%s\n", opengl_version);

    colorPrint(WHITE_BOLD, "GLSL version: ");
    fprintf(stdout, "%s\n", glsl_version);

    colorPrint(WHITE_BOLD, "Render engine info: ");
    fprintf(stdout, "%s\n", renderer_version);

    colorPrint(WHITE_BOLD, "GLFW version:");
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

// Query the size of the current window
void printWindowSize(GLFWwindow *window) {
    int tempWidth;
    int tempHeight;
    glfwGetWindowSize(window, &tempWidth, &tempHeight);
    fprintf(stdout, "\n");
    colorPrint(WHITE_BOLD, "Current width of window  : ");
    fprintf(stdout, "%d\n", tempWidth);
    colorPrint(WHITE_BOLD, "Current height of window : ");
    fprintf(stdout, "%d\n", tempHeight);
}

// Show the menu
void showMenu() {

    colorPrint(YELLOW, "\nCONTROL MENU\n\n");

    // Print all rendering options
    colorPrint(WHITE_BOLD, "  RENDERING OPTIONS\n");
    fprintf(stdout, "* Wireframe                       (1)\n");
    fprintf(stdout, "* Flat-shading                    (2)\n");
    fprintf(stdout, "* Diffuse-shading                 (3)\n");
    fprintf(stdout, "* Smooth-shading(Gouraud shading) (4)\n");
    fprintf(stdout, "* Phong-shading                   (5)\n");
    fprintf(stdout, "\n");

    // Print all light options
    colorPrint(WHITE_BOLD, "  LIGHT OPTIONS\n");
    fprintf(stdout, "* Light direction up/down along x-axis (T/G)\n");
    fprintf(stdout, "* Light direction up/down along y-axis (Y/H)\n");
    fprintf(stdout, "* Light direction up/down along z-axis (U/J)\n");
    fprintf(stdout, "\n");

    // Print all display options
    colorPrint(WHITE_BOLD, "  DISPLAY OPTIONS\n");
    fprintf(stdout, "* Perspective/Parallel projection  (O)\n");
    fprintf(stdout, "* Print the current size of window (S)\n");
    fprintf(stdout, "\n");

    // Print all camera options
    colorPrint(WHITE_BOLD, "  CAMERA OPTIONS\n");
    fprintf(stdout, "* Enter/Quit Pan-Mode                            (P)\n");
    fprintf(stdout, "* Enter/Quit Zoom-Mode                           (Z)\n");
    fprintf(stdout, "* Reset the location, projection, rendering mode (R)\n");
    fprintf(stdout, "\n");

    // Print all other others
    colorPrint(WHITE_BOLD, "  OTHERS\n");
    fprintf(stdout, "* Version (V)\n");
    fprintf(stdout, "* Help    (Q)\n");
    fprintf(stdout, "* Exit  (Esc)\n");
    fprintf(stdout, "\n");

    // Print all current status
    colorPrint(WHITE_BOLD, "CURRENT STATUS\n\n");
    fprintf(stdout, "Projection  : ");
    if (isOrtho) {
        colorPrint(LIGHT_CRAY, "PARALLEL\n");
    } else {
        colorPrint(LIGHT_BLUE, "PERSPECTIVE\n");
    }
    fprintf(stdout, "Current mode: ");
    if (panMode) {
        colorPrint(CRAY, "PAN\n");
    } else if (zoomMode) {
        colorPrint(GREEN, "ZOOM\n");
    } else {
        colorPrint(LIGHT_GREEN, "ROTATION\n");
    }
    fprintf(stdout, "Current rendering mode: ");
    switch (renderMode) {
        case RENDERING_MODE::WIREFRAME:
            colorPrint(LIGHT_CRAY, "WIREFRAME\n");
            break;
        case RENDERING_MODE::FLAT_SHADING:
            colorPrint(LIGHT_CRAY, "FLAT SHADING\n");
            break;
        case RENDERING_MODE::DIFFUSE_SHADING:
            colorPrint(LIGHT_CRAY, "DIFFUSE SHADING\n");
            break;
        case RENDERING_MODE::SMOOTH_SHADING:
            colorPrint(LIGHT_CRAY, "SMOOTH SHADING(GOURAUD SHADING)\n");
            break;
        default:
            colorPrint(LIGHT_CRAY, "PHONG SHADING\n");
            break;
    }
}

// Print the current light direction if there is an update of it
void printLightDirection() {
    fprintf(stdout, "Current vector of light direction: (%.2f, %.2f, %.2f)\n", -lightDirection.x, -lightDirection.y,
            -lightDirection.z);
}

// Print the usage if the arguments provided are not valid.
void printUsage() {
    colorPrint(WHITE_BOLD, "\nUsage: ");
    fprintf(stdout, "./assignment2.out ModelNumber\n\n"
                    "Model: \n"
                    "1. Teapot\n"
                    "2. Spot\n"
                    "3. Nefertiti_Low_Polygon\n"
                    "4. Nefertiti_High_Polygon\n\n");
}

/**
 * Validate all arguments provided, prompt the usage and exit the program if any invalid argument detected.
 * @param argc the number of arguments
 * @param argv the string array of arguments
 */
void argumentsValidation(int argc, char *argv[]) {

    // Check the number of arguments
    if (argc != 2) {
        printUsage();
        exit(EXIT_SUCCESS);
    }

    // Validate the 2nd argument
    for (int i = 1; i <= 4; ++i) {
        if (std::to_string(i) == argv[1]) {
            modelNumber = static_cast<MODEL>(i - 1);
            return;
        }
    }
    // Print the error message and abort the program if the second argument is not correct.
    printUsage();
    exit(EXIT_SUCCESS);
}

/**
 * the 'main' function.
 * @param argc
 * @param argv
 * @return
 */
int main(int argc, char *argv[]) {
#ifndef __CLION
    argumentsValidation(argc, argv);
#endif
    Program p;
    p.start();
    return 0;
}