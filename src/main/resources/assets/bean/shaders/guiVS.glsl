#version 330 core

layout (location = 0) in vec2 pos;
layout (location = 1) in vec2 offset;
layout (location = 2) in vec4 color;
layout (location = 3) in vec2 size;
layout (location = 4) in vec4 shapeInfo;
layout (location = 5) in vec4 textureInfo;

uniform mat4 projection;

out vec2 outPos;
out vec2 outOffset;
out vec4 outColor;
out vec2 outSize;
out vec4 outShapeInfo;
out vec4 outTextureInfo;

void main() {
    gl_Position = projection * vec4(pos * size + offset, 0, 1);
    outPos = pos;
    outOffset = offset;
    outColor = color;
    outSize = size;
    outShapeInfo = shapeInfo;
    outTextureInfo = textureInfo;
}