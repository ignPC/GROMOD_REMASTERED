#version 330 core

layout (location = 0) in vec3 pos;
layout (location = 1) in vec3 offset;
layout (location = 2) in vec4 color;
layout (location = 3) in float scale;
layout (location = 4) in float textureType;
layout (location = 5) in vec2 tntTexCoord;
layout (location = 6) in vec2 sandTexCoord;
layout (location = 7) in vec2 redSandTexCoord;
layout (location = 8) in vec2 gravelTexCoord;

uniform mat4 projection;

out vec4 outColor;
out vec2 outTexCoord;

void main() {
    gl_Position = projection * vec4(pos * scale + offset, 1);
    outColor = color;
    if (textureType == 0) {
        outTexCoord = tntTexCoord;
    } else if (textureType == 1) {
        outTexCoord = sandTexCoord;
    } else if (textureType == 2) {
        outTexCoord = redSandTexCoord;
    } else if (textureType == 3) {
        outTexCoord = gravelTexCoord;
    }
}