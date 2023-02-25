#version 330 core

in vec4 color;
in vec2 shape_position;
in vec2 shape_size;
in float radius;
in float shade;
in float halo;
in float shape_type;

in vec2 tex_coord;

uniform sampler2D font_atlas;

float roundedRectangle(vec2 center, vec2 size, float radius) {
    return length(max(abs(center) - size + radius, 0.0)) - radius;
}

void main() {
    vec2 center = shape_position + shape_size / 2.;

    if (shape_type == 0) {
        float len = length(center - gl_FragCoord.xy);
        float step = 1. - smoothstep(radius - shade, radius, len);
        gl_FragColor = vec4(color.rgb, step);
    } else if (shape_type == 1) {
        float len = roundedRectangle(gl_FragCoord.xy - shape_position - shape_size / 2., shape_size / 2. - vec2(shade), radius);
        float step = 1. - smoothstep(0., shade, len);
        gl_FragColor = vec4(color.rgb, step);
    } else if (shape_type == 2) {
        float alpha = texture(font_atlas, tex_coord).r;
        gl_FragColor = vec4(1, 1, 1, alpha);
    }
}