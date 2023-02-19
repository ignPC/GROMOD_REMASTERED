#version 330 core

uniform vec2 center;

void main() {
    float radius = 200.;
    float shade = 2.;

    float len = length(center - gl_FragCoord.xy);
    float step = smoothstep(radius, radius + shade, len);
    gl_FragColor = vec4(vec3(step), 1);
}