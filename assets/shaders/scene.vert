#version 440 core

layout(location=0) in vec3 a_Position;
layout(location=1) in vec3 a_Color;
layout(location=2) in vec2 a_TexCoords;

uniform mat4 u_MVPmatrix;

out vec3 v_Color;
out vec2 v_TexCoords;

void main() {
    gl_Position = u_MVPmatrix * vec4(a_Position.xyz, 1.0);

    v_Color = a_Color;
    v_TexCoords = a_TexCoords;
}