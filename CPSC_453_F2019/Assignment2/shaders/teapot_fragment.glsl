#version 410 core

out vec4 FragmentColour;

in vec3 Colour;
in vec3 FragmentPosition;
in vec3 Normal;
flat in vec3 resultForFlatingShading;
in vec3 result;

// Material
uniform vec3 materialAmbient;
uniform vec3 materialDiffuse;
uniform vec3 materialSpecular;

// Directional Light
uniform vec3 lightDirection;
uniform vec3 lightAmbient;
uniform vec3 lightDiffuse;
uniform vec3 lightSpecular;

// Rendering mode
uniform int renderingMode;

void main() {

    // Phong shading and wireframe and diffuse-shading
    if (renderingMode == 1 || renderingMode == 3 || renderingMode == 5) {

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
        float spec = pow(max(dot(viewDirection, reflectDirection), 0.0), 32.0);
        vec3 specular = lightSpecular * spec * materialSpecular;

        vec3 lightResult = diffuse;
        if (renderingMode != 3) {
            lightResult = specular + ambient + lightResult;
        }
        FragmentColour = vec4(lightResult, 1.0);
    }
    // Flat-shading
    else if (renderingMode == 2) {
        FragmentColour = vec4(resultForFlatingShading, 1.0);
    }
    // Smooth-shading
    else if (renderingMode == 4) {
        // The difference of smooth shading and Phong shading is the final color of each fragment is essentially calculated
        // in the vertex shader and then interpolated before being passed to the fragment shader.
        // Thus in our fragment shader we only need to write the incoming color ('result') to the framebuffer.
        FragmentColour = vec4(result, 1.0);
    }
}
