#version 410 core

layout(location = 0) in vec3 VertexPosition;
layout(location = 1) in vec3 VertexColour;
layout(location = 2) in vec2 VertexTextureCoords;
layout (location = 3) in vec3 VertexNormal;

out vec3 Colour;
out vec3 Normal;
out vec3 FragmentPosition;
out vec2 TextureCoords;
flat out vec3 resultForFlatingShading;
out vec3 result;

// Directional Light
uniform vec3 lightDirection;
uniform vec3 lightAmbient;
uniform vec3 lightDiffuse;
uniform vec3 lightSpecular;

// Texture
uniform sampler2D texture0;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

// Rendering mode
uniform int renderingMode;

void main() {

    // Phong shading and wireframe and diffuse-shading
    if (renderingMode == 1 || renderingMode == 3 || renderingMode == 5) {
        gl_Position = projection * view * model * vec4(VertexPosition, 1.0);
        FragmentPosition = vec3(model * vec4(VertexPosition, 1.0));
        Colour = VertexColour;
        Normal = mat3(transpose(inverse(model))) * VertexNormal;
        TextureCoords = VertexTextureCoords;
    }
    // Flat-shading
    else if (renderingMode == 2) {

        gl_Position = projection * view * model * vec4(VertexPosition, 1.0);
        TextureCoords = VertexTextureCoords;

        vec3 FragmentPosition = vec3(model * vec4(VertexPosition, 1.0));
        vec3 Normal = mat3(transpose(inverse(model))) * VertexNormal;

        // ambient
        vec3 ambient = lightAmbient;

        // diffuse
        vec3 norm = normalize(Normal);
        vec3 lightDirectionNorm = normalize(-lightDirection);
        vec3 diffuse = lightDiffuse * (max(dot(norm, lightDirectionNorm), 0.0));

        // specular
        vec3 viewPosition = vec3(0.0, 0.0, 35.0);
        vec3 viewDirection = normalize(viewPosition - FragmentPosition);
        vec3 reflectDirection = reflect(-lightDirectionNorm, norm);
        float spec = pow(max(dot(viewDirection, reflectDirection), 0.0), 32.0);
        vec3 specular = lightSpecular * spec;

        // combine all factors
        resultForFlatingShading = ambient + diffuse + specular;
    }
    // Smooth-shading
    else if (renderingMode == 4) {

        gl_Position = projection * view * model * vec4(VertexPosition, 1.0);
        TextureCoords = VertexTextureCoords;

        vec3 FragmentPosition = vec3(model * vec4(VertexPosition, 1.0));
        vec3 Normal = mat3(transpose(inverse(model))) * VertexNormal;

        // ambient
        vec3 ambient = lightAmbient;

        // diffuse
        vec3 norm = normalize(Normal);
        vec3 lightDirectionNorm = normalize(-lightDirection);
        vec3 diffuse = lightDiffuse * (max(dot(norm, lightDirectionNorm), 0.0));

        // specular
        vec3 viewPosition = vec3(0.0, 0.0, 35.0);
        vec3 viewDirection = normalize(viewPosition - FragmentPosition);
        vec3 reflectDirection = reflect(-lightDirectionNorm, norm);
        float spec = pow(max(dot(viewDirection, reflectDirection), 0.0), 32.0);
        vec3 specular = lightSpecular * spec;

        // combine all factors
        result = ambient + diffuse + specular;
    }
}