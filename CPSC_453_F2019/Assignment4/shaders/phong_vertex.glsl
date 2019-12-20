#version 410 core

layout(location = 0) in vec3 VertexPosition;
layout(location = 1) in vec3 VertexColour;
layout(location = 2) in vec2 VertexTextureCoords;
layout (location = 3) in vec3 VertexNormal;

out vec3 Colour;
out vec3 Normal;
out vec3 FragmentPosition;
out vec2 TextureCoords;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;


void main() {
    // Phong shading
    gl_Position = projection * view * model * vec4(VertexPosition, 1.0);
    FragmentPosition = vec3(model * vec4(VertexPosition, 1.0));
    Colour = VertexColour;
    Normal = mat3(transpose(inverse(model))) * VertexNormal;
    TextureCoords = VertexTextureCoords;
}
