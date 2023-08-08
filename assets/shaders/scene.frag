#version 440

in vec3 v_Color;
in vec2 v_TexCoords;

uniform sampler2D u_Sampler;
uniform int u_Textured;

out vec4 o_Color;

void main() {
    if(u_Textured == 0)
        o_Color = vec4(v_Color.rgb, 1.0);
    else if(u_Textured == 1)
        o_Color = texture(u_Sampler, v_TexCoords) * vec4(v_Color.rgb, 1.0);
}