#version 410 core

out vec4 FragmentColour;

in vec3 Colour;
in vec3 FragmentPosition;
in vec3 Normal;
in vec2 TextureCoords;
flat in vec3 resultForFlatingShading;
in vec3 result;

// Directional Light
uniform vec3 lightDirection;
uniform vec3 lightAmbient;
uniform vec3 lightDiffuse;
uniform vec3 lightSpecular;

// Texture
uniform sampler2D texture0;

// Rendering mode
uniform int renderingMode;

void main() {

    // Phong shading and wireframe and diffuse-shading
    if (renderingMode == 1 || renderingMode == 3 || renderingMode == 5) {

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

        vec3 lightResult = diffuse;
        if (renderingMode != 3) {
            lightResult = specular + ambient + lightResult;
        }
        FragmentColour=vec4(lightResult * texture(texture0, TextureCoords).rgb, 1.0);
    }
    // Flat-shading
    else if (renderingMode == 2) {
        FragmentColour = vec4(resultForFlatingShading * texture(texture0, TextureCoords).rgb, 1.0);
    }
    // Smooth-shading
    else if (renderingMode == 4) {
        FragmentColour = vec4(result * texture(texture0, TextureCoords).rgb, 1.0);
    }
}