package brokenswing.objects;

import brokenswing.utils.IResource;
import brokenswing.utils.ResourcesManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;

import java.nio.ByteBuffer;

public class CubeMap implements IResource {

    private final int id;

    public CubeMap(ByteBuffer[] textures, int[] width, int[] height) {
        id = GL11.glGenTextures();
        bind();
        for(int i = 0; i < textures.length; i++) {
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGB,
                    width[i], height[i], 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, textures[i]);
        }
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_WRAP_R, GL12.GL_CLAMP_TO_EDGE);
        ResourcesManager.instance().addResource(this);
    }

    @Override
    public void delete() {
        GL11.glDeleteTextures(id);
    }

    @Override
    public void unbind() {
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, 0);
    }

    public void bind() {
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, id);
    }

}
