#version 330 core

layout (location = 0) in vec2 aPos;
layout (location = 1) in vec4 aColor;
layout (location = 2) in vec2 aShape_position;
layout (location = 3) in vec2 aShape_size;
layout (location = 4) in float aRadius;
layout (location = 5) in float aShade;
layout (location = 6) in float aHalo;

uniform mat4 projection;

out vec4 color;
out vec2 shape_position;
out vec2 shape_size;
out float radius;
out float shade;
out float halo;

void main() {
    gl_Position = projection * vec4(aPos, 0, 1);
    color = aColor;
    shape_position = aShape_position;
    shape_size = aShape_size;
    radius = aRadius;
    shade = aShade;
    halo = aHalo;
}