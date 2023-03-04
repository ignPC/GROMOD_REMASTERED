#version 330 core

in vec4 outColor;
in vec2 outShapePosition;
in vec2 outShapeSize;
in float outRadius;
in float outShade;
in float outHalo;
in float outShapeType;
in vec2 outTexCoord;

uniform sampler2D font_atlas;

float roundedRectangle(vec2 center, vec2 size, float radius) {
    return length(max(abs(center) - size + radius, 0.0)) - radius;
}

void main() {
    vec2 center = outShapePosition + outShapeSize / 2.;

    if (outShapeType == 0) {
        float len = length(center - gl_FragCoord.xy);
        float step = 1. - smoothstep(outRadius - outShade, outRadius, len);
        gl_FragColor = vec4(outColor.rgb, step);
    } else if (outShapeType == 1) {
        float len = roundedRectangle(gl_FragCoord.xy - outShapePosition - outShapeSize / 2., outShapeSize / 2. - vec2(outShade), outRadius);
        float step = 1. - smoothstep(0., outShade, len);
        gl_FragColor = vec4(outColor.rgb, step);
    } else if (outShapeType == 2) {
        float alpha = texture(font_atlas, outTexCoord).r;
        gl_FragColor = vec4(outColor.rgb, alpha * outColor.a);
    }
}