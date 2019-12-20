/**
 * The file is the implementation of Shader.hpp.
 */

#include "Shader.hpp"
#include "Miscellaneous.hpp"
#include <fstream>

void Shader::LinkProgram(GLuint vertexShader, GLuint geometryShader, GLuint fragmentShader) {

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
        fprintf(stderr, "ERROR linking shaderOfModel program:\n%s\n", info.c_str());
        // Quit if the linking is unsuccessful
        exit(EXIT_FAILURE);
    }
}

GLuint Shader::CompileShader(GLenum shaderType, const std::string &source) {

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
        fprintf(stderr, "ERROR compiling shaderOfModel:\n\n%s\n%s\n", source_ptr, info.c_str());
        // Quit if the shader failed to compile
        exit(EXIT_FAILURE);
    }
    return shaderObject;
}

std::string Shader::LoadSource(const std::string &filename) {

    std::string source;
    if (!Miscellaneous::isRegularFile(filename)) {
        fprintf(stderr, "%s is not a valid file!\n", filename.c_str());
        exit(EXIT_FAILURE);
    }
    if (!Miscellaneous::isGlslFile(filename)) {
        fprintf(stderr, "%s is not a GLSL source file!\n", filename.c_str());
        exit(EXIT_FAILURE);
    }
    if (!Miscellaneous::hasReadPermission(filename)) {
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

Shader::Shader(const char *vertexShaderSourceFilename, const char *fragmentShaderSourceFilename,
               const char *geometryShaderSourceFilename) : programId(0) {

    // Load shader source from files
    std::string vertexSource = LoadSource(vertexShaderSourceFilename);
    std::string fragmentSource = LoadSource(fragmentShaderSourceFilename);
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

void Shader::setter(const std::string &variableName, GLboolean transpose, const GLfloat *newValue) const {
    glUniformMatrix4fv(glGetUniformLocation(programId, variableName.c_str()), 1, transpose, newValue);
}

void Shader::setter(const std::string &variableName, GLfloat *newValue) const {
    glUniform3fv(glGetUniformLocation(programId, variableName.c_str()), 1, newValue);
}

void Shader::use() {
    glUseProgram(programId);
}
