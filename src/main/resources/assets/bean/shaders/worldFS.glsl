#version 330 core

uniform sampler2D minecraft_atlas;

in vec4 outColor;
in vec2 outTexCoord;

void main() {
    gl_FragColor = texture(minecraft_atlas, outTexCoord) * outColor;
}