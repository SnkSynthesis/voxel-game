#version 330 core

out vec4 color;

in vec2 texCoord;
in float alphaValue;

uniform sampler2D tex;

void main() {
    color = vec4(texture(tex, texCoord).xyz, alphaValue);
}