package brokenswing.objects;

import brokenswing.utils.IResource;
import brokenswing.utils.ResourcesManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class VertexArrayObject implements IResource {

    private final int id;

    public VertexArrayObject() {
        id = GL30.glGenVertexArrays();
        bind();
        ResourcesManager.instance().addResource(this);
    }

    @Override
    public void unbind() {
        GL30.glBindVertexArray(0);
    }

    public void bind() {
        GL30.glBindVertexArray(id);
    }

    @Override
    public void delete() {
        GL30.glDeleteVertexArrays(id);
    }

    public void createDataBuffer(float[] datas) {
        new VertexBufferObject().setDatas(datas);
    }

    public void createAtttribPointer(int attribute, int size) {
        createAtttribPointer(attribute, size, Float.BYTES * size, 0);
    }

    public void createAtttribPointer(int attribute, int size, int stride, int offset) {
        GL20.glVertexAttribPointer(attribute, size, GL11.GL_FLOAT, false, stride, offset);
        GL20.glEnableVertexAttribArray(attribute);
    }

    public void setTexture(int id, Texture texture) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + id);
        texture.bind();
    }

    public void setElements(int[] elements) {
        ElementBufferObject ebo = new ElementBufferObject();
        ebo.setElements(elements);
    }
}
