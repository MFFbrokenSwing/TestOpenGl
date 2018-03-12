package brokenswing.entity;

import brokenswing.shader.ShaderProgram;
import org.joml.Vector3f;

public class Light {

    private Vector3f position;
    private Vector3f ambient;
    private Vector3f diffuse;
    private Vector3f specular;

    public Light() {
        this(1, 1, 1, 0, 0, 0);
    }

    public Light(float r, float g, float b, float x, float y, float z) {
        this(new Vector3f(r, g, b), new Vector3f(x, y, z));
    }

    public Light(Vector3f color, Vector3f position) {
        this.ambient = color;
        this.diffuse = color;
        this.specular = color;
        this.position = position;
    }

    public Light specular(Vector3f color) {
        this.specular = color;
        return this;
    }
    public Light ambient(Vector3f color) {
        this.ambient = color;
        return this;
    }
    public Light diffuse(Vector3f color) {
        this.diffuse = color;
        return this;
    }

    public void storeInShader(ShaderProgram program, String name) {
        program.setUniform(name + ".position", this.position);
        program.setUniform(name + ".ambient", this.ambient);
        program.setUniform(name + ".diffuse", this.diffuse);
        program.setUniform(name + ".specular", this.specular);
    }

    public Light position(Vector3f position) {
        this.position = position;
        return this;
    }
}
