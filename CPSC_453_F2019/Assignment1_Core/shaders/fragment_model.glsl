#version 410 core

in vec3 Colour;

out vec4 FragmentColour;

void main() {
    FragmentColour = vec4(Colour, 1.0);
}
