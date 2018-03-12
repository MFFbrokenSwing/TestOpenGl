package brokenswing.window;

import brokenswing.objects.VertexArrayObject;
import brokenswing.shader.ShaderProgram;
import brokenswing.utils.ResourcesManager;
import org.joml.Math;
import org.joml.Matrix4f;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public abstract class Window {

    private long window;
    protected int width;
    protected int height;
    protected final String title;

    private double lastFrame = 0.0f;
    private double deltaTime = 0f;

    private double lastMouseX = -1;
    private double lastMouseY = -1;
    private double deltaMouseX = 0;
    private double deltaMouseY = 0;

    protected Matrix4f projectionMatrix;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.lastMouseX = width / 2;
        this.lastMouseY = height / 2;
    }

    public void open() {
        init();
        loop();

        Callbacks.glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW.");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(window == NULL) throw new RuntimeException("Unable to create the window.");

        glfwSetKeyCallback(window, (window, key, scancode, actions, mods) -> this.onKeyInput(key, scancode, actions, mods));
        glfwSetCursorPos(window, width / 2, height / 2);
        glfwSetCursorPosCallback(window, (window1, xPos, yPos) -> this.onMouseMovement(xPos, yPos));

        centerWindow();
        makeProjection();

        glfwMakeContextCurrent(window);

        glfwSetFramebufferSizeCallback(window, (win, w, h) -> {
            if(w != width || h != height) {
                this.width = w;
                this.height = h;
                glViewport(0, 0, this.width, this.height);
                makeProjection();
            }
        });

        //glfwSwapInterval(1);
        glfwShowWindow(window);

        GL.createCapabilities();

        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    private void centerWindow() {
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            glfwSetWindowPos(window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }
    }

    private void makeProjection() {
        projectionMatrix =  new Matrix4f().perspective((float) Math.toRadians(45), this.width / this.height, 0.1f, 100.0f);
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    protected abstract void drawScene();

    protected abstract void initScene();

    protected abstract void update();

    private void loop() {

        initScene();

        while(!glfwWindowShouldClose(window)) {
            glClear(GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);

            double currentFrame = glfwGetTime();
            deltaTime = currentFrame - lastFrame;
            lastFrame = currentFrame;

            update();
            drawScene();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        ResourcesManager.instance().clearResources();
    }

    public long getWindow() {
        return window;
    }

    public float getDeltaTime() {
        return (float) deltaTime;
    }

    public abstract void onKeyInput(int key, int scancode, int action, int mods);

    private boolean firstMouseMovement = true;

    public void onMouseMovement(double mouseX, double mouseY) {
        if(firstMouseMovement) {
            firstMouseMovement = false;
            lastMouseX = mouseX;
            lastMouseY = mouseY;
            return;
        }
        deltaMouseX = mouseX - lastMouseX;
        deltaMouseY = mouseY - lastMouseY;
        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }

    public double getDeltaMouseX() {
        return deltaMouseX;
    }

    public double getDeltaMouseY() {
        return deltaMouseY;
    }
}
