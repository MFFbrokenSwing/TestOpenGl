#version 330 core

layout (location = 0) in vec3 pos;

out vec3 tex_coords;

uniform mat4 projection;
uniform mat4 view;

void main() {
    tex_coords = pos;
    gl_Position = projection * mat4(mat3(view)) * vec4(pos, 1.0);
}