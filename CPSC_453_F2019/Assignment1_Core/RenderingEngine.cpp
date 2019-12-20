/**
 * The file is the implementation of RenderingEngine.cpp.
 */

#include "RenderingEngine.hpp"
#include "Program.hpp"
#include "Constants.hpp"

//#define  __CLION

// Initialization of all global static variables
glm::vec3 RenderingEngine::lightPosition = Constants::DEFAULT_LIGHT_POSITION;
glm::vec3 RenderingEngine::lightColor = Constants::DEFAULT_LIGHT_COLOR;
bool RenderingEngine::useShading = false;
bool RenderingEngine::isOrtho = false;
bool RenderingEngine::zoomMode = false;
bool RenderingEngine::panMode = false;
bool RenderingEngine::lightPanMode = false;
bool RenderingEngine::isLeftButtonPressed = false;
bool RenderingEngine::isRightButtonPressed = false;
bool RenderingEngine::isScrollWheelPressed = false;
bool RenderingEngine::firstMoveAfterSwitch = false;
float RenderingEngine::translateX = Constants::DEFAULT_TRANSLATE_X;
float RenderingEngine::translateY = Constants::DEFAULT_TRANSLATE_Y;
float RenderingEngine::translateZ = Constants::DEFAULT_TRANSLATE_Z;
float RenderingEngine::zoom = Constants::DEFAULT_ZOOM;
float RenderingEngine::angleX = Constants::DEFAULT_ANGLE;
float RenderingEngine::angleY = Constants::DEFAULT_ANGLE;
float RenderingEngine::angleZ = Constants::DEFAULT_ANGLE;

RenderingEngine::RenderingEngine() {

    // Set the locations of all shaders' source code
    const char *vertexShaderOfModel = nullptr;
    const char *vertexShaderOfLight = nullptr;
    const char *fragmentShaderOfModel = nullptr;
    const char *fragmentShaderOfLight = nullptr;

#ifdef __CLION
    if (useShading) {
        vertexShaderOfModel = "/Users/hshen/CLionProject/Assignment1_Core/shaders/vertex_model_shading.glsl";
        vertexShaderOfLight = "/Users/hshen/CLionProject/Assignment1_Core/shaders/vertex_light.glsl";
        fragmentShaderOfModel = "/Users/hshen/CLionProject/Assignment1_Core/shaders/fragment_model_shading.glsl";
        fragmentShaderOfLight = "/Users/hshen/CLionProject/Assignment1_Core/shaders/fragment_light.glsl";
    } else {
        vertexShaderOfModel = "/Users/hshen/CLionProject/Assignment1_Core/shaders/vertex_model.glsl";
        fragmentShaderOfModel = "/Users/hshen/CLionProject/Assignment1_Core/shaders/fragment_model.glsl";
    }
#else
    if (useShading) {
        vertexShaderOfModel = "./shaders/vertex_model_shading.glsl";
        vertexShaderOfLight = "./shaders/vertex_light.glsl";
        fragmentShaderOfModel = "./shaders/fragment_model_shading.glsl";
        fragmentShaderOfLight = "./shaders/fragment_light.glsl";
    } else {
        vertexShaderOfModel = "./shaders/vertex_model.glsl";
        fragmentShaderOfModel = "./shaders/fragment_model.glsl";
    }
#endif

    // Check if all shader programs are successfully initialized
    shaderOfModel = std::make_shared<Shader>(vertexShaderOfModel, fragmentShaderOfModel);
    if (!shaderOfModel || !(shaderOfModel->programId)) {
        fprintf(stderr, "Program could not initialize shaders of model, TERMINATING\n");
        exit(EXIT_FAILURE);
    }
    if (useShading) {
        shaderOfLight = std::make_shared<Shader>(vertexShaderOfLight, fragmentShaderOfLight);
        if (!shaderOfLight || !(shaderOfLight->programId)) {
            fprintf(stderr, "Program could not initialize shaders of light, TERMINATING\n");
            exit(EXIT_FAILURE);
        }
    }
}

RenderingEngine::~RenderingEngine() {
    // Delete the shader of the model
    glDeleteProgram(shaderOfModel->programId);
    shaderOfModel = nullptr;
    // Delete the shader of the light source
    if (useShading) {
        glDeleteProgram(shaderOfLight->programId);
        shaderOfLight = nullptr;
    }
}

void RenderingEngine::setTransformationOfModel() {
    glm::mat4 model = glm::mat4(1.0f);
    shaderOfModel->setter("model", GL_FALSE, &model[0][0]);
}

void RenderingEngine::setTransformationOfLightSource() {
    // Define the location of the cube light source here
    // We define it on the right space which is above of the top of the cube
    glm::mat4 model = glm::mat4(1.0f);
    // Make it as a small cube
    model = glm::scale(model, glm::vec3(0.05f));
    model = glm::translate(model, lightPosition);
    shaderOfLight->setter("model", GL_FALSE, &model[0][0]);
}

void RenderingEngine::RenderScene(const std::vector<Geometry> &objects, const Geometry &lightSource) {

    // Refresh the screen by a white fully-opaque background
    // Set a gray background
    glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    // Set camera here (and we will have projection and view)
    int height = Program::getHeight();
    if (height == 0) {
        height = 1;
    }
    int width = Program::getWidth();
    GLfloat aspect = static_cast<GLfloat> (width) / static_cast<GLfloat> (height);
    GLfloat aspect_reverse = static_cast<GLfloat>(1.0 / aspect);
    GLfloat multiplier = Constants::ORTHO_MULTIPLIER;
    glm::mat4 projection;
    if (isOrtho) {
        if (width <= height) {
            projection = glm::ortho(-multiplier, multiplier, -multiplier * aspect_reverse, multiplier * aspect_reverse,
                                    -10.0f, 100.0f);
        } else {
            projection = glm::ortho(-multiplier * aspect, multiplier * aspect, -multiplier, multiplier, -10.0f, 100.0f);
        }
    } else {
        projection = glm::perspective(glm::radians(Constants::DEFAULT_FOV), aspect, 2.0f, 100.0f);
    }
    glm::vec3 eye(5.0f, 1.0f, 8.0f);
    glm::vec3 target(0.0f, 0.0f, 0.0f);
    glm::vec3 upVector(0.0f, 1.0f, 0.0f);
    glm::mat4 view = glm::lookAt(eye, target, upVector);

    // Zoom in/out
    view = glm::scale(view, glm::vec3(zoom * 2, zoom * 2, zoom * 2));

    // Rotate around y-axis
    view = glm::rotate(view, glm::radians(angleY), glm::vec3(0.0f, 1.0f, 0.0f));

    // Rotate around x-axis
    view = glm::rotate(view, glm::radians(angleX), glm::vec3(1.0f, 0.0f, 0.0f));

    // Rotate around z-axis
    view = glm::rotate(view, glm::radians(angleZ), glm::vec3(0.0f, 0.0f, 1.0f));

    // Shift
    view = glm::translate(view, glm::vec3(translateX, translateY, translateZ));

    // Set uniforms in the shader of model
    shaderOfModel->use();
    shaderOfModel->setter("projection", GL_FALSE, &projection[0][0]);
    shaderOfModel->setter("view", GL_FALSE, &view[0][0]);
    if (useShading) {
        shaderOfModel->setter("lightPosition", &lightPosition[0]);
        shaderOfModel->setter("lightColor", &lightColor[0]);
        glm::vec3 viewPosition = glm::vec3(0.0f, 0.0f, 3.0f);
        shaderOfModel->setter("viewPosition", &viewPosition[0]);
    }
    setTransformationOfModel();

    // Draw the model
    for (const Geometry &g : objects) {
        glBindVertexArray(g.vao);
        glDrawArrays(g.drawMode, 0, static_cast<GLsizei>(g.verts.size()));
        // reset state to default (no shaderOfModel or geometry bound)
        glBindVertexArray(0);
    }

    // Draw the light source
    if (useShading) {
        shaderOfLight->use();
        shaderOfLight->setter("projection", GL_FALSE, &projection[0][0]);
        shaderOfLight->setter("view", GL_FALSE, &view[0][0]);
        shaderOfLight->setter("lightColor", &lightColor[0]);
        setTransformationOfLightSource();

        glBindVertexArray(lightSource.vao);
        glDrawArrays(lightSource.drawMode, 0, static_cast<GLsizei>(lightSource.verts.size()));

        // Reset state to default (no shaderOfModel or geometry bound)
        glBindVertexArray(0);
    }

    // Tell OpenGL no program will be used for rendering
    glUseProgram(0);

    // Check if there is any OpenGL error
    CheckGLErrors();
}

void RenderingEngine::assignBuffers(Geometry &geometry) {

    // Generate VAO for the object
    glGenVertexArrays(1, &geometry.vao);
    glBindVertexArray(geometry.vao);

    // Generate VBOs for the object
    glGenBuffers(1, &geometry.vertexBuffer);
    glBindBuffer(GL_ARRAY_BUFFER, geometry.vertexBuffer);
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, 0, (void *) nullptr);
    glEnableVertexAttribArray(0);

    glGenBuffers(1, &geometry.colorBuffer);
    glBindBuffer(GL_ARRAY_BUFFER, geometry.colorBuffer);
    glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, 0, (void *) nullptr);
    glEnableVertexAttribArray(1);

    glGenBuffers(1, &geometry.normalBuffer);
    glBindBuffer(GL_ARRAY_BUFFER, geometry.normalBuffer);
    glVertexAttribPointer(2, 3, GL_FLOAT, GL_FALSE, 0, (void *) nullptr);
    glEnableVertexAttribArray(2);
}

void RenderingEngine::setBufferData(Geometry &geometry) {

    // Send geometry to the GPU
    // Since it is a setter, we bind buffer again although we did this in RenderingEngine::assignBuffers.
    // Must be called whenever anything is updated about the object

    // glBindBuffer is to bind a named buffer object to a target
    // glBufferData is to allocate the space for a buffer object and copy the data stored in the specific address.

    glBindBuffer(GL_ARRAY_BUFFER, geometry.vertexBuffer);
    glBufferData(GL_ARRAY_BUFFER, sizeof(glm::vec3) * geometry.verts.size(), geometry.verts.data(), GL_STATIC_DRAW);

    glBindBuffer(GL_ARRAY_BUFFER, geometry.colorBuffer);
    glBufferData(GL_ARRAY_BUFFER, sizeof(glm::vec3) * geometry.colors.size(), geometry.colors.data(), GL_STATIC_DRAW);

    glBindBuffer(GL_ARRAY_BUFFER, geometry.normalBuffer);
    glBufferData(GL_ARRAY_BUFFER, sizeof(glm::vec3) * geometry.normals.size(), geometry.normals.data(), GL_STATIC_DRAW);

}

void RenderingEngine::deleteBufferData(Geometry &geometry) {
    glDeleteBuffers(1, &geometry.vertexBuffer);
    glDeleteBuffers(1, &geometry.normalBuffer);
    glDeleteBuffers(1, &geometry.colorBuffer);
    glDeleteVertexArrays(1, &geometry.vao);
}

bool RenderingEngine::CheckGLErrors() {
    bool error = false;
    for (GLenum flag = glGetError(); flag != GL_NO_ERROR; flag = glGetError()) {
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
    return error;
}
