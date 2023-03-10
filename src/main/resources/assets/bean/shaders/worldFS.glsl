#version 330 core

uniform sampler2D minecraft_atlas;

in float outVertexId;
in vec2 outTexturePos;
in vec4 outColor;
in vec2 outCubeInfo;
in vec4 outTopTexture;
in vec4 outMiddleTexture;
in vec4 outBottomTexture;

void main() {
    if (outVertexId < 16) {
        gl_FragColor = texture(minecraft_atlas, vec2(outTexturePos.x, 1. - outTexturePos.y) * outMiddleTexture.zw + outMiddleTexture.xy) * outColor;
    } else if (outVertexId < 20) {
        gl_FragColor = texture(minecraft_atlas, vec2(outTexturePos.x, 1. - outTexturePos.y) * outTopTexture.zw + outTopTexture.xy) * outColor;
    } else if (outVertexId < 24) {
        gl_FragColor = texture(minecraft_atlas, vec2(outTexturePos.x, 1. - outTexturePos.y) * outBottomTexture.zw + outBottomTexture.xy) * outColor;
    } else {
        gl_FragColor = outColor;
    }
}