#version 330 core

out vec4 color;

in vec2 texCoord;
in float alphaValue;
in float ambientValue;

uniform sampler2D tex;

void main() {
    vec3 lightColor = vec3(1.0, 1.0, 1.0);
    vec4 ambient = vec4(lightColor * ambientValue, 1.0);
    color = vec4(texture(tex, texCoord).xyz, alphaValue) * ambient;
}