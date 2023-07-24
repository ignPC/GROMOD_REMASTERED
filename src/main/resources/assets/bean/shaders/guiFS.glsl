#version 330 core

in vec2 outPos;
in vec2 outOffset;
in vec4 outColor;
in vec2 outSize;
in vec4 outShapeInfo;
in vec4 outTextureInfo;

uniform sampler2D font_atlas;
uniform sampler2D image;

float roundedRectangle(vec2 center, vec2 size, float radius) {
    return length(max(abs(center) - size + radius, 0.0)) - radius;
}

void main() {
    float radius = outShapeInfo.x;
    float shade = outShapeInfo.y;
    float halo = outShapeInfo.z;
    float shapeType = outShapeInfo.w;
    vec2 center = outOffset + outSize / 2.;

    if (shapeType == 0) {
        float len = length(center - gl_FragCoord.xy);
        float step = 1. - smoothstep(radius - shade, radius, len);
        gl_FragColor = vec4(outColor.rgb, step * outColor.a);
    } else if (shapeType == 1) {
        float len = roundedRectangle(gl_FragCoord.xy - outOffset - outSize / 2., outSize / 2. - vec2(shade), radius);
        float step = 1. - smoothstep(0., shade, len);
        gl_FragColor = vec4(outColor.rgb, step * outColor.a);
    } else if (shapeType == 2) {
        float alpha = texture(font_atlas, vec2(outPos.x, 1. - outPos.y) * outTextureInfo.zw + outTextureInfo.xy).r;
        gl_FragColor = vec4(outColor.rgb, alpha * outColor.a);
    } else if (shapeType == 3) {
        gl_FragColor = outColor;
    } else if (shapeType == 4) {
        gl_FragColor = texture(image, vec2(outPos.x, 1. - outPos.y) * outTextureInfo.zw + outTextureInfo.xy);
    }
}