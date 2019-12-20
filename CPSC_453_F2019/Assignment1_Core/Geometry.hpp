#ifndef GEOMETRY_HPP
#define GEOMETRY_HPP

#include <vector>

#include <glm/glm.hpp>
#include <glm/gtc/matrix_transform.hpp>
#include <glm/gtc/type_ptr.hpp>

#include "glad/glad.h"
#include <GLFW/glfw3.h>

class Geometry {
public:
    Geometry();

    virtual ~Geometry();

    // Data structures for storing vectors of vertices, normals and colors
    std::vector<glm::vec3> verts;
    std::vector<glm::vec3> normals;
    std::vector<glm::vec3> colors;

    // ID numbers of the VAO and VBOs associated with the geometry
    GLuint vao;
    GLuint vertexBuffer;
    GLuint normalBuffer;
    GLuint colorBuffer;

    // Draw mode for how OpenGL interprets primitives
    GLuint drawMode{};
};

#endif /* GEOMETRY_HPP */
