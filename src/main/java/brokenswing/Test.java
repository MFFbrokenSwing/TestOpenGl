package brokenswing;

import brokenswing.window.GameWindow;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Test {

    public static void main(String[] args) {
        new GameWindow(1200, 900, "My window").open();
    }
}
