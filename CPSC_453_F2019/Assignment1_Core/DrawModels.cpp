/**
 * This file is the implementation of DrawModels.hpp.
 */

#include "DrawModels.hpp"
#include "Miscellaneous.hpp"
#include "Constants.hpp"

Geometry DrawModels::createCube(GLfloat x, GLfloat y, GLfloat z, GLfloat a, GLfloat r, GLfloat g, GLfloat b) {

    static float gap = 0.0f;
    if (!RenderingEngine::useShading) {
        gap = Constants::DEFAULT_GAP;
    }

    Geometry cube;
    a = static_cast<GLfloat>(fabs(a));

    // Since a cube is consist of 12 triangles, we defined all vertices for these triangles
    glm::vec3 normal;

    // Front face
    normal = glm::vec3(0.0f, 0.0f, a);
    // triangle
    cube.verts.emplace_back(x - a, y + a, z + a);
    cube.verts.emplace_back(x + a, y + a, z + a);
    cube.verts.emplace_back(x - a, y - a, z + a);
    // adjacent triangle
    cube.verts.emplace_back(x + a, y + a, z + a);
    cube.verts.emplace_back(x + a, y - a, z + a);
    cube.verts.emplace_back(x - a, y - a, z + a);
    // add normal vectors for all 6 vertices if shading is used
    if (RenderingEngine::useShading) {
        for (int i = 0; i < 6; ++i) {
            cube.normals.emplace_back(normal);
        }
    }

    // Top face
    normal = glm::vec3(0.0f, a, 0.0f);
    // triangle
    cube.verts.emplace_back(x - a, y + a, z - a);
    cube.verts.emplace_back(x - a, y + a, z + a);
    cube.verts.emplace_back(x + a, y + a, z - a);
    // adjacent triangle
    cube.verts.emplace_back(x - a, y + a, z + a);
    cube.verts.emplace_back(x + a, y + a, z - a);
    cube.verts.emplace_back(x + a, y + a, z + a);
    // add normal vectors for all 6 vertices if shading is used
    if (RenderingEngine::useShading) {
        for (int i = 0; i < 6; ++i) {
            cube.normals.emplace_back(normal);
        }
    }

    // Left face
    normal = glm::vec3(-a, 0.0f, 0.0f);
    // triangle
    cube.verts.emplace_back(x - a, y + a, z - a);
    cube.verts.emplace_back(x - a, y + a, z + a);
    cube.verts.emplace_back(x - a, y - a, z - a);
    // adjacent triangle
    cube.verts.emplace_back(x - a, y - a, z + a);
    cube.verts.emplace_back(x - a, y + a, z + a);
    cube.verts.emplace_back(x - a, y - a, z - a);
    // add normal vectors for all 6 vertices if shading is used
    if (RenderingEngine::useShading) {
        for (int i = 0; i < 6; ++i) {
            cube.normals.emplace_back(normal);
        }
    }

    // Right face
    normal = glm::vec3(a, 0.0f, 0.0f);
    // triangle
    cube.verts.emplace_back(x + a, y + a, z - a);
    cube.verts.emplace_back(x + a, y - a, z - a);
    cube.verts.emplace_back(x + a, y - a, z + a);
    // adjacent triangle
    cube.verts.emplace_back(x + a, y + a, z - a);
    cube.verts.emplace_back(x + a, y + a, z + a);
    cube.verts.emplace_back(x + a, y - a, z + a);
    // add normal vectors for all 6 vertices if shading is used
    if (RenderingEngine::useShading) {
        for (int i = 0; i < 6; ++i) {
            cube.normals.emplace_back(normal);
        }
    }

    // Back face
    normal = glm::vec3(0.0f, 0.0f, -a);
    // triangle
    cube.verts.emplace_back(x - a, y + a, z - a);
    cube.verts.emplace_back(x - a, y - a, z - a);
    cube.verts.emplace_back(x + a, y - a, z - a);
    // adjacent triangle
    cube.verts.emplace_back(x - a, y + a, z - a);
    cube.verts.emplace_back(x + a, y + a, z - a);
    cube.verts.emplace_back(x + a, y - a, z - a);
    // add normal vectors for all 6 vertices if shading is used
    if (RenderingEngine::useShading) {
        for (int i = 0; i < 6; ++i) {
            cube.normals.emplace_back(normal);
        }
    }

    // Bottom face
    normal = glm::vec3(0.0f, -a, 0.0f);
    // triangle
    cube.verts.emplace_back(x - a, y - a, z + a);
    cube.verts.emplace_back(x + a, y - a, z + a);
    cube.verts.emplace_back(x + a, y - a, z - a);
    // adjacent triangle
    cube.verts.emplace_back(x - a, y - a, z + a);
    cube.verts.emplace_back(x + a, y - a, z - a);
    cube.verts.emplace_back(x - a, y - a, z - a);
    // add normal vectors for all 6 vertices
    // add normal vectors for all 6 vertices if shading is used
    if (RenderingEngine::useShading) {
        for (int i = 0; i < 6; ++i) {
            cube.normals.emplace_back(normal);
        }
    }

    // Color all faces
    for (int i = 0; i < 6; ++i) {
        // triangle (i + 1)
        cube.colors.emplace_back(r, g, b);
        cube.colors.emplace_back(r, g, b);
        cube.colors.emplace_back(r, g, b);
        // triangle (i + 1)'s adjacent
        cube.colors.emplace_back(r, g, b);
        cube.colors.emplace_back(r, g, b);
        cube.colors.emplace_back(r, g, b);
        // update the color of different faces
        r -= gap;
        g -= gap;
        b -= gap;
    }
    cube.drawMode = GL_TRIANGLES;
    return cube;
}

void
DrawModels::createMengerSponge(std::vector<Geometry> &output, GLfloat x, GLfloat y, GLfloat z, GLfloat a, GLfloat r,
                               GLfloat g, GLfloat b) {

    // A Menger-Sponge is consist of 20 cubes
    float halfLength = a / 6.0f;
    float length = halfLength * 2;

    // middle
    auto cube1 = DrawModels::createCube(x + length, y, z + length, halfLength, r, g, b);
    auto cube2 = DrawModels::createCube(x - length, y, z + length, halfLength, r, g, b);
    auto cube3 = DrawModels::createCube(x - length, y, z - length, halfLength, r, g, b);
    auto cube4 = DrawModels::createCube(x + length, y, z - length, halfLength, r, g, b);

    // up
    auto cube5 = DrawModels::createCube(x + length, y + length, z + length, halfLength, r, g, b);
    auto cube6 = DrawModels::createCube(x + length, y + length, z - length, halfLength, r, g, b);
    auto cube7 = DrawModels::createCube(x, y + length, z + length, halfLength, r, g, b);
    auto cube8 = DrawModels::createCube(x, y + length, z - length, halfLength, r, g, b);
    auto cube9 = DrawModels::createCube(x + length, y + length, z, halfLength, r, g, b);
    auto cube10 = DrawModels::createCube(x - length, y + length, z, halfLength, r, g, b);
    auto cube11 = DrawModels::createCube(x - length, y + length, z + length, halfLength, r, g, b);
    auto cube12 = DrawModels::createCube(x - length, y + length, z - length, halfLength, r, g, b);

    // down
    auto cube13 = DrawModels::createCube(x + length, y - length, z + length, halfLength, r, g, b);
    auto cube14 = DrawModels::createCube(x + length, y - length, z - length, halfLength, r, g, b);
    auto cube15 = DrawModels::createCube(x, y - length, z + length, halfLength, r, g, b);
    auto cube16 = DrawModels::createCube(x, y - length, z - length, halfLength, r, g, b);
    auto cube17 = DrawModels::createCube(x + length, y - length, z, halfLength, r, g, b);
    auto cube18 = DrawModels::createCube(x - length, y - length, z, halfLength, r, g, b);
    auto cube19 = DrawModels::createCube(x - length, y - length, z + length, halfLength, r, g, b);
    auto cube20 = DrawModels::createCube(x - length, y - length, z - length, halfLength, r, g, b);

    output.emplace_back(cube1);
    output.emplace_back(cube2);
    output.emplace_back(cube3);
    output.emplace_back(cube4);
    output.emplace_back(cube5);
    output.emplace_back(cube6);
    output.emplace_back(cube7);
    output.emplace_back(cube8);
    output.emplace_back(cube9);
    output.emplace_back(cube10);
    output.emplace_back(cube11);
    output.emplace_back(cube12);
    output.emplace_back(cube13);
    output.emplace_back(cube14);
    output.emplace_back(cube15);
    output.emplace_back(cube16);
    output.emplace_back(cube17);
    output.emplace_back(cube18);
    output.emplace_back(cube19);
    output.emplace_back(cube20);
}

void
DrawModels::mengerSpongeRecursion(std::vector<Geometry> &output, int stage, GLfloat x, GLfloat y, GLfloat z, GLfloat a,
                                  GLfloat r, GLfloat g, GLfloat b) {

    // Base case 1
    // When stage is 0, we construct the model as an ordinary cube.
    if (stage == 0) {
        output.emplace_back(createCube(x, y, z, a / 2, r, g, b));
        return;
    }
    // Base case 2
    if (stage == 1) {
        createMengerSponge(output, x, y, z, a, r, g, b);
        return;
    }

    --stage;
    float new_a = a / 3;

    // middle
    mengerSpongeRecursion(output, stage, x + new_a, y, z + new_a, new_a, r, g, b);
    mengerSpongeRecursion(output, stage, x - new_a, y, z + new_a, new_a, r, g, b);
    mengerSpongeRecursion(output, stage, x - new_a, y, z - new_a, new_a, r, g, b);
    mengerSpongeRecursion(output, stage, x + new_a, y, z - new_a, new_a, r, g, b);

    // up
    mengerSpongeRecursion(output, stage, x + new_a, y + new_a, z + new_a, new_a, r, g, b);
    mengerSpongeRecursion(output, stage, x + new_a, y + new_a, z - new_a, new_a, r, g, b);
    mengerSpongeRecursion(output, stage, x, y + new_a, z + new_a, new_a, r, g, b);
    mengerSpongeRecursion(output, stage, x, y + new_a, z - new_a, new_a, r, g, b);
    mengerSpongeRecursion(output, stage, x + new_a, y + new_a, z, new_a, r, g, b);
    mengerSpongeRecursion(output, stage, x - new_a, y + new_a, z, new_a, r, g, b);
    mengerSpongeRecursion(output, stage, x - new_a, y + new_a, z + new_a, new_a, r, g, b);
    mengerSpongeRecursion(output, stage, x - new_a, y + new_a, z - new_a, new_a, r, g, b);

    // down
    mengerSpongeRecursion(output, stage, x + new_a, y - new_a, z + new_a, new_a, r, g, b);
    mengerSpongeRecursion(output, stage, x + new_a, y - new_a, z - new_a, new_a, r, g, b);
    mengerSpongeRecursion(output, stage, x, y - new_a, z + new_a, new_a, r, g, b);
    mengerSpongeRecursion(output, stage, x, y - new_a, z - new_a, new_a, r, g, b);
    mengerSpongeRecursion(output, stage, x + new_a, y - new_a, z, new_a, r, g, b);
    mengerSpongeRecursion(output, stage, x - new_a, y - new_a, z, new_a, r, g, b);
    mengerSpongeRecursion(output, stage, x - new_a, y - new_a, z + new_a, new_a, r, g, b);
    mengerSpongeRecursion(output, stage, x - new_a, y - new_a, z - new_a, new_a, r, g, b);
}