#version 330 core

in vec3 tex_coords;

out vec4 FragColor;

uniform samplerCube skybox;

void main() {
    FragColor = texture(skybox, tex_coords);
}