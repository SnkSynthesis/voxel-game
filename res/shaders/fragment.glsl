#version 330 core

out vec4 color;
in vec3 aColor;

void main() {
    color = vec4(aColor, 1.0);
}