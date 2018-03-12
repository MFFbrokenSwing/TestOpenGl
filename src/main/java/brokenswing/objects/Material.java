package brokenswing.objects;

import brokenswing.shader.ShaderProgram;
import org.joml.Vector3f;

/**
 * Some materials can be found here <a href="http://devernay.free.fr/cours/opengl/materials.html">.
 */
public class Material {

    public static final Material EMERALD = create()
            .ambient(0.0215f, 0.1745f, 0.0215f)
            .diffuse(0.07568f, 0.61424f, 0.07568f)
            .specular(0.633f, 0.727811f, 0.633f)
            .shininess(0.6f);

    public static final Material OBSIDIAN = create()
            .ambient(0.05375f, 0.05f, 0.06625f)
            .diffuse(0.18275f, 0.17f, 0.22525f)
            .specular(0.332741f, 0.328634f, 0.346435f)
            .shininess(0.3f);

    public static final Material RUBY = create()
            .ambient(0.1745f, 0.01175f, 0.01175f)
            .diffuse(0.61424f, 0.04136f, 0.04136f)
            .specular(0.727811f, 0.626959f, 0.626959f)
            .shininess(0.6f);

    private Vector3f ambient;
    private Vector3f diffuse;
    private Vector3f specular;
    private float shininess;

    private static Material create() {
        return new Material();
    }

    public Material ambient(float r, float g, float b) {
        this.ambient = new Vector3f(r, g, b);
        return this;
    }

    public Material diffuse(float r, float g, float b) {
        this.diffuse = new Vector3f(r, g, b);
        return this;
    }

    public Material specular(float r, float g, float b) {
        this.specular = new Vector3f(r, g, b);
        return this;
    }

    public Material shininess(float s) {
        this.shininess = s;
        return this;
    }

    public void storeToShader(ShaderProgram program, String name) {
        program.setUniform(name + ".ambient", this.ambient);
        program.setUniform(name + ".diffuse", this.diffuse);
        program.setUniform(name + ".specular", this.specular);
        program.setUniform(name + ".shininess", this.shininess);
    }
}
