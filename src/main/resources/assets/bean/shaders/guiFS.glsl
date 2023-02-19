#version 330 core

in vec4 color;
in vec2 shape_position;
in vec2 shape_size;
in float radius;
in float shade;
in float halo;

void main() {
    vec2 center = shape_position + shape_size / 2.;
    float len = length(center - gl_FragCoord.xy);
    float step = smoothstep(radius, radius + shade, len);

    gl_FragColor = vec4(vec3(step), 1);
}