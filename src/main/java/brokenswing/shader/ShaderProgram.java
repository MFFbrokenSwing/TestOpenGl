package brokenswing.shader;

import brokenswing.objects.Texture;
import brokenswing.utils.FileUtils;
import brokenswing.utils.IResource;
import brokenswing.utils.ResourcesManager;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;

import javax.imageio.event.IIOReadProgressListener;
import java.nio.FloatBuffer;

public class ShaderProgram implements IResource {

    private final int programId;

    public ShaderProgram(String vertexShader, String fragmentShader) {
        this(vertexShader, fragmentShader, null);
    }

    public ShaderProgram(String vertexShader, String fragmentShader, String geometryShader) {
        programId = GL20.glCreateProgram();
        int[] shaders = loadShaders(vertexShader, fragmentShader);

        GL20.glAttachShader(programId, shaders[0]);
        GL20.glAttachShader(programId, shaders[1]);
        int gId = 0;
        if(geometryShader != null) {
            gId = loadShader(geometryShader);
            GL20.glAttachShader(programId, gId);
        }

        GL20.glLinkProgram(programId);
        if(GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            System.err.print(GL20.glGetProgramInfoLog(programId));
        }

        GL20.glDeleteShader(shaders[0]);
        GL20.glDeleteShader(shaders[1]);
        if(geometryShader != null)
            GL20.glDeleteShader(gId);

        bind();
        ResourcesManager.instance().addResource(this);
    }

    public void bind() {
        GL20.glUseProgram(this.programId);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    @Override
    public void delete() {
        GL20.glDeleteProgram(this.programId);
    }

    private int loadShader(String geometryShader) {
        int gId = GL20.glCreateShader(GL32.GL_GEOMETRY_SHADER);
        GL20.glShaderSource(gId, FileUtils.getResourceContents("shaders/" + geometryShader + ".geometry.glsl"));

        GL20.glCompileShader(gId);
        if(GL20.glGetShaderi(gId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println(GL20.glGetShaderInfoLog(gId));
        }

        return gId;
    }

    private int[] loadShaders(String vertexShader, String fragmentShader) {
        int vId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vId, FileUtils.getResourceContents("shaders/" + vertexShader + ".vertex.glsl"));

        GL20.glCompileShader(vId);
        if(GL20.glGetShaderi(vId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println(GL20.glGetShaderInfoLog(vId));
        }


        int fId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fId, FileUtils.getResourceContents("shaders/" + fragmentShader + ".fragment.glsl"));

        GL20.glCompileShader(fId);
        if(GL20.glGetShaderi(fId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println(GL20.glGetShaderInfoLog(fId));
        }
        return new int[]{vId, fId};
    }

    protected int getUniformLocation(String name) {
        return GL20.glGetUniformLocation(this.programId, name);
    }

    public void setUniform(String name, float value) {
        GL20.glUniform1f(getUniformLocation(name), value);
    }

    public void setUniform(String name, int value) {
        GL20.glUniform1i(getUniformLocation(name), value);
    }

    public void setUniform(String name, Matrix4f mat) {
        GL20.glUniformMatrix4fv(getUniformLocation(name), false, mat.get(BufferUtils.createFloatBuffer(16)));
    }

    public void setUniform(String name, Vector3f vec) {
        GL20.glUniform3f(getUniformLocation(name), vec.x, vec.y, vec.z);
    }

}
