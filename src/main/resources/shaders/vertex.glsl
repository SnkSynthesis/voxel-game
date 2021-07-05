#version 330 core

layout(location = 0) in vec3 pos;
layout(location = 1) in vec2 inTexCoord;
layout(location = 2) in float inAlphaValue;

out vec2 texCoord;
out float alphaValue;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    texCoord = inTexCoord;
    alphaValue = inAlphaValue;
    
    vec3 fragPos = vec3(model * vec4(pos, 1.0));
    gl_Position = projection * view * vec4(fragPos, 1.0);
}