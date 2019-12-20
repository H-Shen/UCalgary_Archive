cmake_minimum_required(VERSION 3.14)
project(CPSC453_Assignment1_Core)

IF (NOT "${CMAKE_CXX_COMPILER_ID}" STREQUAL "GNU")
    MESSAGE(STATUS "You are using : ")
    MESSAGE(${CMAKE_CXX_COMPILER_ID})
    MESSAGE(FATAL_ERROR "Please use GCC instead of other compilers!")
ENDIF ()

set(CMAKE_CXX_STANDARD 17)

IF (WIN32)

    MESSAGE(STATUS "The system is Windows. The boilerplate is not for this platform. Exit without doing anything.")

ELSEIF (APPLE)

    # find OpenGL
    find_package(OpenGL REQUIRED)
    if (OpenGL_FOUND)
        message(STATUS "OpenGL found!")
        message(OPENGL_INCLUDE_DIR = ${OPENGL_INCLUDE_DIR})
        message(OPENGL_LIBRARIES = ${OPENGL_LIBRARIES})
        link_directories(${OpenGL_LIBRARY_DIRS})
        add_definitions(${OpenGL_DEFINITIONS})
    else ()
        message(STATUS "error: OpenGL not found!")
    endif ()

    # find GLFW3
    find_package(glfw3 3.3 REQUIRED)

    # find GLM
    include_directories(${CMAKE_CURRENT_LIST_DIR}/middleware/glm-0.9.9.6)

    # find GLAD
    include_directories(${CMAKE_CURRENT_LIST_DIR}/middleware/glad/include)

    add_executable(assignment1_core_openGL.out
            main.cpp
            glad.c
            Geometry.cpp
            Geometry.hpp
            Program.cpp
            Program.hpp
            RenderingEngine.cpp
            RenderingEngine.hpp
            Scene.cpp
            Scene.hpp
            Miscellaneous.cpp
            Miscellaneous.hpp
            DrawModels.hpp
            DrawModels.cpp
            Shader.cpp
            Shader.hpp
            ColorOutput.hpp Constants.hpp)

    target_link_libraries(assignment1_core_openGL.out
            glfw
            stdc++fs)

    set(CMAKE_CXX_FLAGS "-Wall -Ofast")

ELSEIF (UNIX)

    # find OpenGL
    find_package(OpenGL REQUIRED)
    if (OpenGL_FOUND)
        message(STATUS "OpenGL found!")
        message(OPENGL_INCLUDE_DIR = ${OPENGL_INCLUDE_DIR})
        message(OPENGL_LIBRARIES = ${OPENGL_LIBRARIES})
        link_directories(${OpenGL_LIBRARY_DIRS})
        add_definitions(${OpenGL_DEFINITIONS})
    else ()
        message(STATUS "error: OpenGL not found!")
    endif ()

    # find GLFW3
    find_package(glfw3 3.2.1 REQUIRED)

    # find GLM
    include_directories(./middleware/glm-0.9.9.6)

    # find GLAD
    include_directories(./middleware/glad/include)

    set(CMAKE_CXX_FLAGS "-Wall -Ofast")

    add_executable(assignment1_core_openGL.out
            main.cpp
            glad.c
            Geometry.cpp
            Geometry.hpp
            Program.cpp
            Program.hpp
            RenderingEngine.cpp
            RenderingEngine.hpp
            Scene.cpp
            Scene.hpp
            Miscellaneous.cpp
            Miscellaneous.hpp
            DrawModels.hpp
            DrawModels.cpp
            Shader.cpp
            Shader.hpp
            ColorOutput.hpp Constants.hpp)

    target_include_directories(assignment1_core_openGL.out
            PUBLIC
            $<BUILD_INTERFACE:${CMAKE_CURRENT_LIST_DIR}/include>
            $<INSTALL_INTERFACE:include>)

    target_link_libraries(assignment1_core_openGL.out
            PUBLIC glfw OpenGL::GL ${CMAKE_DL_LIBS} stdc++fs)

ENDIF ()
