#version 440 core

in vec3 v_Color;
in vec2 v_TexCoords;

uniform int u_Textured;
uniform sampler2D u_Sampler;

out vec4 o_Color;

void main() {
    vec4 color = vec4(1, 1, 1, 1);

    if(u_Textured == 0)
        color = vec4(v_Color.rgb, 1.0);
    else if(u_Textured == 1)
        color = texture(u_Sampler, v_TexCoords) * vec4(v_Color.rgb, 1.0);

    o_Color = color;
}