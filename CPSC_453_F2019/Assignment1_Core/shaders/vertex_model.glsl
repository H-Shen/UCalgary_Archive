#version 410 core

layout(location = 0) in vec3 VertexPosition;
layout(location = 1) in vec3 VertexColour;

out vec3 Colour;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    gl_Position = projection * view * model * vec4(VertexPosition.xyz, 1.0);
    Colour = VertexColour;
}
