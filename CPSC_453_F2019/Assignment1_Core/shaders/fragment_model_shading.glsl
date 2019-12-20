#version 410 core

in vec3 Color;
in vec3 FragmentPosition;
in vec3 Normal;

out vec4 FragmentColor;

uniform vec3 lightPosition;
uniform vec3 lightColor;
uniform vec3 viewPosition;

void main() {

    // define the ambient
    float ambientIntensity = 0.1;
    vec3 ambient = ambientIntensity * lightColor;

    // define the diffuse
    // Step 1. Calculate the vector of direction of the lightPosition and the FragmentPosition
    //         We normalize the vector since we do not care its length.
    vec3 norm = normalize(Normal);
    vec3 lightDirection = normalize(lightPosition - FragmentPosition);
    // Step 2. Calculate the dot product of norm and lightDirection to obtain the effect of light source to the
    //         fragment. And then make it times the lightColor to obtain the vector of diffuse
    //         If the angle between norm and lightDirection is > 90 degree, we define the result as 0.0
    float dotProduct = max(dot(norm, lightDirection), 0.0);
    vec3 diffuse = dotProduct * lightColor;

    // define the specular
    float specularIntensity = 0.3;
    vec3 viewDirection = normalize(viewPosition - FragmentPosition);
    vec3 reflectionDirection = reflect(-lightDirection, norm);

    int shininess = 64;
    float multiplier = pow(max(dot(viewDirection, reflectionDirection), 0.0), shininess);
    vec3 specular = specularIntensity * multiplier * lightColor;

    // combine the effect of the ambient, the diffuse and the color of the object itself
    vec3 result = (ambient + diffuse + specular) * Color;
    FragmentColor = vec4(result, 1.0);
}
