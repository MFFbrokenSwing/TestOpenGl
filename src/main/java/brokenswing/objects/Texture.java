package brokenswing.objects;

import brokenswing.utils.IResource;
import brokenswing.utils.ResourcesManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import sun.net.ResourceManager;

import javax.annotation.Resource;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_LINEAR;

public class Texture implements IResource {

    private final int width, height;
    private final int id;

    public Texture(ByteBuffer pixels, int width, int height) {
        this.width = width;
        this.height = height;
        this.id = GL11.glGenTextures();
        bind();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        GL11.glTexImage2D(GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, pixels);
        GL30.glGenerateMipmap(GL_TEXTURE_2D);
        ResourcesManager.instance().addResource(this);
    }

    @Override
    public void delete() {
        GL11.glDeleteTextures(this.id);
    }

    @Override
    public void unbind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
    }
}
