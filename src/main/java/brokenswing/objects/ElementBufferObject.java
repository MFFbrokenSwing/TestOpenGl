package brokenswing.objects;

import brokenswing.utils.IResource;
import brokenswing.utils.ResourcesManager;
import org.lwjgl.opengl.GL15;

public class ElementBufferObject implements IResource {

    private final int id;

    public ElementBufferObject() {
        id = GL15.glGenBuffers();
        bind();
        ResourcesManager.instance().addResource(this);
    }

    public void setElements(int[] elements) {
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, elements, GL15.GL_STATIC_DRAW);
    }

    public void bind() {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id);
    }

    @Override
    public void delete() {
        GL15.glDeleteBuffers(id);
    }

    @Override
    public void unbind() {
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }
}
