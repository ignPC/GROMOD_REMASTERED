#version 330 core

layout (location = 0) in vec2 pos2D;
layout (location = 1) in vec4 color;
layout (location = 2) in vec2 shapePosition;
layout (location = 3) in vec2 shapeSize;
layout (location = 4) in float radius;
layout (location = 5) in float shade;
layout (location = 6) in float halo;
layout (location = 7) in float shapeType;
layout (location = 8) in vec2 texCoord;

uniform mat4 projection;

out vec4 outColor;
out vec2 outShapePosition;
out vec2 outShapeSize;
out float outRadius;
out float outShade;
out float outHalo;
out float outShapeType;
out vec2 outTexCoord;

void main() {
    gl_Position = projection * vec4(pos2D, 0, 1);
    outColor = color;
    outShapePosition = shapePosition;
    outShapeSize = shapeSize;
    outRadius = radius;
    outShade = shade;
    outHalo = halo;
    outShapeType = shapeType;
    outTexCoord = texCoord;
}