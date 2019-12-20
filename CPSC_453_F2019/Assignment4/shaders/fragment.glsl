#version 410 core

out vec4 FragmentColour;

in vec3 Colour;

void main() {
    FragmentColour = vec4(Colour, 1.0);
}
