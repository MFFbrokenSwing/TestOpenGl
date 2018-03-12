package brokenswing.utils;

import brokenswing.objects.CubeMap;
import brokenswing.objects.Texture;
import de.javagl.obj.Obj;
import de.javagl.obj.ObjReader;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.StringJoiner;

public class FileUtils {

    public static String getResourceContents(String resourcePath) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(FileUtils.class.getClassLoader()
                        .getResourceAsStream(resourcePath)))) {
            StringJoiner joiner = new StringJoiner("\n");
            reader.lines().forEachOrdered(joiner::add);
            return joiner.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Texture loadTexture(String texturePath) {
        STBImage.stbi_set_flip_vertically_on_load(true);
        String fileName = FileUtils.class.getClassLoader().getResource(texturePath).getPath().substring(1);
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channelsCount = BufferUtils.createIntBuffer(1);
        ByteBuffer pixels = STBImage.stbi_load(fileName, width, height, channelsCount, 0);
        if(pixels == null)
            System.err.println("Couldn't read texture");
        Texture texture = new Texture(pixels, width.get(0), height.get(0));
        STBImage.stbi_image_free(pixels);
        return texture;
    }

    public static Obj loadModel(String modelName) {
        try {
            return ObjReader.read(FileUtils.class.getClassLoader().getResourceAsStream("models/" + modelName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static CubeMap loadCubeMap(String[] sides) {
        ByteBuffer[] buffers = new ByteBuffer[sides.length];
        int[] width = new int[sides.length];
        int[] height = new int[sides.length];
        for(int i = 0; i < buffers.length; i++) {
            String fileName = FileUtils.class.getClassLoader()
                    .getResource("textures/cubemap/" + sides[i])
                    .getPath().substring(1);
            int[] w = new int[1];
            int[] h = new int[1];
            STBImage.stbi_set_flip_vertically_on_load(false);
            buffers[i] = STBImage.stbi_load(fileName, w, h, new int[1], 0);
            width[i] = w[0];
            height[i] = h[0];
        }
        CubeMap cube = new CubeMap(buffers, width, height);
        for(int i = 0; i < buffers.length; i++) {
            STBImage.stbi_image_free(buffers[i]);
        }
        return cube;
    }

}
