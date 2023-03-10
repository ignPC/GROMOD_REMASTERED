#version 330 core

layout (location = 0) in vec3 pos;
layout (location = 1) in vec2 texturePos;
layout (location = 2) in vec3 offset;
layout (location = 3) in vec4 color;
layout (location = 4) in vec4 cubeInfo;
layout (location = 5) in vec4 topTexture;
layout (location = 6) in vec4 middleTexture;
layout (location = 7) in vec4 bottomTexture;

uniform mat4 projection;

out float outVertexId;
out vec2 outTexturePos;
out vec4 outColor;
out vec4 outTopTexture;
out vec4 outMiddleTexture;
out vec4 outBottomTexture;

void main() {
    gl_Position = projection * vec4(pos * cubeInfo.xyz + offset, 1);
    outVertexId = gl_VertexID;
    outTexturePos = texturePos;
    outColor = color;
    outTopTexture = topTexture;
    outMiddleTexture = middleTexture;
    outBottomTexture = bottomTexture;
    if (cubeInfo.w == 4) {
        outVertexId = 25;
    }
}