package brokenswing.entity;

import brokenswing.window.Window;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {

    public static final Vector3f UP_VECTOR = new Vector3f(0f, 1f, 0f);
    public static final float SPEED = 1f;
    public static final float SENSIBILITY = 0.25f;

    protected Vector3f position;
    protected Vector3f front;
    // Pitch, Yaw, Roll
    protected Vector3f rotation;

    public Camera() {
        this(new Vector3f(0f, 0f, 0f), new Vector3f(0f, 0f, -1f));
    }

    public Camera(Vector3f position, Vector3f target) {
        this.position = position;
        this.front = target;
        this.rotation = new Vector3f(0f, 0f, 0f);
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(position, position.add(front, new Vector3f()), UP_VECTOR);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void processKeyInput(Window win) {
        long window = win.getWindow();
        float speed = SPEED * win.getDeltaTime();

        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS)
            this.position.add(new Vector3f(this.front).normalize(speed));
        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS)
            this.position.sub(new Vector3f(this.front).normalize(speed));
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS)
            this.position.sub(this.front.cross(UP_VECTOR, new Vector3f()).normalize(speed));
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS)
            this.position.add(this.front.cross(UP_VECTOR, new Vector3f()).normalize(speed));
        if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS)
            this.position.add(new Vector3f(UP_VECTOR).normalize(speed));
        if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS)
            this.position.sub(new Vector3f(UP_VECTOR).normalize(speed));
    }

    public void processMouseMovement(Window window) {

        this.rotation.x += window.getDeltaMouseY() * SENSIBILITY;
        this.rotation.y += window.getDeltaMouseX() * SENSIBILITY;
        this.rotation.x = Math.max(-89f, Math.min(this.rotation.x, 89f));

        this.front.z = -(float) (Math.cos(Math.toRadians(this.rotation.x)) * Math.cos(Math.toRadians(this.rotation.y)));
        this.front.y = -(float) (Math.sin(Math.toRadians(this.rotation.x)));
        this.front.x = (float) (Math.cos(Math.toRadians(this.rotation.x)) * Math.sin(Math.toRadians(this.rotation.y)));
        this.front.normalize();
    }

}
