/*
 * RenderingEngine.h
 *
 *  Created on: Sep 10, 2018
 *      Author: John Hall
 */

#ifndef RENDERINGENGINE_HPP
#define RENDERINGENGINE_HPP

#include "Geometry.hpp"
#include "Shader.hpp"
#include "glad/glad.h"
#include <GLFW/glfw3.h>
#include <memory>

//Forward declaration
struct GLFWwindow;

class RenderingEngine {
public:
    // Constructor
    explicit RenderingEngine();

    // Destructor
    virtual ~RenderingEngine();

    // Render each object
    void RenderScene(const std::vector<Geometry> &objects, const Geometry &lightSource);

    // Create VAO and VBOs for objects
    static void assignBuffers(Geometry &geometry);

    // Bind and copy data from a Geometry instance to buffers
    static void setBufferData(Geometry &geometry);

    // Delete buffers in a Geometry instance
    static void deleteBufferData(Geometry &geometry);

    // Ensure that VAO and VBOs are set up properly, there will be a message if any error occurs
    static bool CheckGLErrors();

    // Define the transformation matrix of the model and assign to the shader
    void setTransformationOfModel();

    // Define the transformation matrix of the light source and assign to the shader
    void setTransformationOfLightSource();

    // Global variables for the light
    static glm::vec3 lightPosition;
    static glm::vec3 lightColor;
    static bool lightPanMode;

    // A boolean controls if shading is used instead of fixed color
    static bool useShading;

    // Global variables for the camera
    static bool isOrtho;
    static bool zoomMode;
    static bool panMode;

    // Global variables for the mouse
    static bool isLeftButtonPressed;
    static bool isRightButtonPressed;
    static bool isScrollWheelPressed;
    static bool firstMoveAfterSwitch;
    static float translateX;
    static float translateY;
    static float translateZ;
    static float zoom;
    static float angleY;
    static float angleX;
    static float angleZ;

private:
    std::shared_ptr<Shader> shaderOfModel;
    std::shared_ptr<Shader> shaderOfLight;
};

#endif /* RENDERINGENGINE_HPP */
