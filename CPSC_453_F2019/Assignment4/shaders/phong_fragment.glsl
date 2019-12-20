#version 410 core

out vec4 FragmentColour;

in vec3 Colour;
in vec3 FragmentPosition;
in vec3 Normal;
in vec2 TextureCoords;

// Material
uniform vec3 materialAmbient;
uniform vec3 materialDiffuse;
uniform vec3 materialSpecular;
uniform float shininess;

// Directional Light
uniform vec3 lightDirection;
uniform vec3 lightAmbient;
uniform vec3 lightDiffuse;
uniform vec3 lightSpecular;

// Texture
uniform sampler2D texture0;
uniform int useTexture;

void main() {

    // Phone shading
    // ambient
    vec3 ambient = lightAmbient * materialAmbient;

    // diffuse
    vec3 norm = normalize(Normal);
    vec3 lightDirectionNorm = normalize(-lightDirection);
    vec3 diffuse = lightDiffuse * (max(dot(norm, lightDirectionNorm), 0.0) * materialDiffuse);

    // specular
    vec3 viewPosition = vec3(0.0, 0.0, 35.0);
    vec3 viewDirection = normalize(viewPosition - FragmentPosition);
    vec3 reflectDirection = reflect(-lightDirectionNorm, norm);
    float spec = pow(max(dot(viewDirection, reflectDirection), 0.0), shininess);
    vec3 specular = lightSpecular * spec * materialSpecular;

    vec3 lightResult = diffuse;
    lightResult = specular + ambient + lightResult;

    if (useTexture == 1) {
        FragmentColour = vec4(lightResult * texture(texture0, TextureCoords).rgb, 1.0);
    } else {
        FragmentColour = vec4(lightResult, 1.0);
    }
}
