#version 330 core

layout (location = 0) in vec3 pos;
layout (location = 1) in vec2 tex;
layout (location = 2) in vec3 normal;

out vec2 passed_tex_coords;
out vec3 passed_normal;
out vec3 frag_pos;

uniform mat4 projection_matrix;
uniform mat4 model_matrix;
uniform mat4 view_matrix;

void main() {
    passed_tex_coords = tex;
    passed_normal = mat3(transpose(inverse(model_matrix))) * normal;
    frag_pos = vec3(model_matrix * vec4(pos, 1.0));
    gl_Position = projection_matrix * view_matrix * model_matrix * vec4(pos, 1.0);
}
