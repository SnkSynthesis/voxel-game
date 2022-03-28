package com.snksynthesis.voxelgame.texture;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.stb.STBImage.*;

/**
 * {@link Texture} is for loading and using textures
 */
public class Texture {

    private final int texture;

    private Texture(String path, boolean rgba) {
        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        // Flips image vertically so that it is not seen upside down
        stbi_set_flip_vertically_on_load(true);

        ByteBuffer data;
        try (
            InputStream texInput = getClass().getClassLoader().getResourceAsStream(path);
            ReadableByteChannel rbc = Channels.newChannel(texInput);
        ) {

            data = ByteBuffer.allocateDirect((int) (TextureAtlas.IMG_WIDTH_PX * TextureAtlas.IMG_WIDTH_PX));
            data.order(ByteOrder.nativeOrder());
            int bytes = 0;
            while (bytes != -1) {
                bytes = rbc.read(data);
            }
            data.flip();
            data = MemoryUtil.memSlice(data);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read texture");
        }
        
        ByteBuffer image = stbi_load_from_memory(data, width, height, channels, 0);
        if (image != null) {
            if (rgba) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE,
                        image);
            } else {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            }
            glGenerateMipmap(GL_TEXTURE_2D);
        } else {
            throw new RuntimeException("Couldn't load texture!");
        }
        stbi_image_free(image);
    }

    /**
     * Loads an RGB image.
     * 
     * @throws RuntimeException if the texture wasn't able to be loaded
     */
    public static Texture loadRGB(String path) {
        Texture tex = new Texture(path, false);
        return tex;
    }

    /**
     * Loads an RGBA image
     * 
     * @throws RuntimeException if the texture wasn't able to be loaded
     */
    public static Texture loadRGBA(String path) {
        Texture tex = new Texture(path, true);
        return tex;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getTexture() {
        return texture;
    }
}