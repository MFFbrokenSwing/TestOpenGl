package brokenswing.window;

import brokenswing.entity.Camera;
import brokenswing.entity.Light;
import brokenswing.objects.CubeMap;
import brokenswing.objects.Material;
import brokenswing.objects.Texture;
import brokenswing.objects.VertexArrayObject;
import brokenswing.shader.ShaderProgram;
import brokenswing.utils.Constants;
import brokenswing.utils.FileUtils;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjData;
import de.javagl.obj.ObjUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class GameWindow extends Window {

    private VertexArrayObject vao;
    private VertexArrayObject vaoNorm;
    private VertexArrayObject vaoSky;
    private ShaderProgram program;
    private ShaderProgram normalsProgram;
    private ShaderProgram skyProgram;
    private Camera camera;
    private int toDraw;

    public GameWindow(int width, int height, String title) {
        super(width, height, title);
    }

    @Override
    protected void initScene() {
        glClearColor(0f, 0f, 0f, 1f);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);

        Obj cube = FileUtils.loadModel("cube.obj");
        cube = ObjUtils.convertToRenderable(cube);
        toDraw = ObjData.getFaceVertexIndices(cube).capacity();
        Texture tex = FileUtils.loadTexture("textures/container.jpg");
        Texture tex2 = FileUtils.loadTexture("textures/awesomeface.jpg");
        Light light = new Light()
                .position(new Vector3f(4, 5, 0))
                .ambient(new Vector3f(0.3f))
                .diffuse(new Vector3f(0.8f))
                .specular(new Vector3f(1f));

        camera = new Camera();

        CubeMap cubeMap = FileUtils.loadCubeMap(new String[]{"front.tga", "back.tga", "up.tga",
        "down.tga", "right.tga", "left.tga"});

        program = new ShaderProgram("basic", "basic");
        program.setUniform("model_matrix", new Matrix4f().translate(0f, 0f, -5f));
        program.setUniform("projection_matrix", getProjectionMatrix());
        light.storeInShader(program, "light");
        Material.RUBY.storeToShader(program, "material");

        skyProgram = new ShaderProgram("skybox/skybox", "skybox/skybox");
        skyProgram.setUniform("projection", getProjectionMatrix());
        skyProgram.unbind();

        vao = new VertexArrayObject();
        vao.createDataBuffer(ObjData.getVerticesArray(cube));
        vao.createAtttribPointer(0, 3, Float.BYTES * 3, 0);
        vao.createDataBuffer(ObjData.getTexCoordsArray(cube, 2));
        vao.createAtttribPointer(1, 2, Float.BYTES * 2, 0);
        vao.createDataBuffer(ObjData.getNormalsArray(cube));
        vao.createAtttribPointer(2, 3, Float.BYTES * 3, 0);
        vao.setElements(ObjData.getFaceVertexIndicesArray(cube));
        vao.unbind();

        normalsProgram = new ShaderProgram("normals", "normals", "normals");
        normalsProgram.setUniform("model", new Matrix4f().translate(0, 0, -5));
        normalsProgram.setUniform("projection", getProjectionMatrix());

        vaoNorm = new VertexArrayObject();
        vao.createDataBuffer(ObjData.getVerticesArray(cube));
        vao.createAtttribPointer(0, 3, Float.BYTES * 3, 0);
        vao.createDataBuffer(ObjData.getNormalsArray(cube));
        vao.createAtttribPointer(1, 3, Float.BYTES * 3, 0);
        vao.setElements(ObjData.getFaceVertexIndicesArray(cube));
        vao.unbind();

        vaoSky = new VertexArrayObject();
        vaoSky.createDataBuffer(Constants.SKYBOX_VERTICES);
        vaoSky.createAtttribPointer(0, 3);
        cubeMap.bind();
        vaoSky.unbind();

    }

    @Override
    protected void drawScene() {

        glDepthMask(false);
        skyProgram.bind();
        skyProgram.setUniform("view", camera.getViewMatrix());
        vaoSky.bind();
        glDrawArrays(GL_TRIANGLES, 0, Constants.SKYBOX_VERTICES.length / 3);
        glDepthMask(true);

        program.bind();
        program.setUniform("view_matrix", camera.getViewMatrix());
        program.setUniform("view_pos", camera.getPosition());

        vao.bind();
        glDrawElements(GL_TRIANGLES, toDraw, GL_UNSIGNED_INT, 0);

        normalsProgram.bind();

        normalsProgram.setUniform("view", camera.getViewMatrix());

        //vaoNorm.bind();
        //glDrawElements(GL_TRIANGLES, toDraw, GL_UNSIGNED_INT, 0);
        //glDrawArrays(GL_TRIANGLES, 0, 36);
    }

    @Override
    protected void update() {
        camera.processKeyInput(this);
    }

    private boolean isWireFrame = false;

    @Override
    public void onKeyInput(int key, int scancode, int action, int mods) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
            glfwSetWindowShouldClose(this.getWindow(), true);
        } else if (key == GLFW_KEY_Z && action == GLFW_RELEASE) {
            isWireFrame = !isWireFrame;
            glPolygonMode(GL_FRONT_AND_BACK, isWireFrame ? GL_LINE : GL_FILL);
        }
    }

    @Override
    public void onMouseMovement(double mouseX, double mouseY) {
        super.onMouseMovement(mouseX, mouseY);
        camera.processMouseMovement(this);
    }
}
