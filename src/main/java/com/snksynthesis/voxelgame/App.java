package com.snksynthesis.voxelgame;

import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL33.*;

import java.nio.FloatBuffer;

public class App {

    private Window window;

    public void run() {
        window = new Window("Voxel Game", 500, 500);
        window.create();

        GL.createCapabilities();

        Shader shader = new Shader();
        try {
            shader.link();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // @formatter:off
        float[] vertices = {
            // positions            // colors
            0.0f,  0.5f, 0.0f,      0.0f, 1.0f, 0.0f,
           -0.5f, -0.5f, 0.0f,      0.5f, 0.0f, 1.0f,
            0.5f, -0.5f, 0.0f,      1.0f, 0.5f, 0.0f,
        };
        // @formatter:on

        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
        verticesBuffer.put(vertices).flip();

        int vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);
        int vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        int positionSize = 3;
        int colorSize = 3;
        int vertexSizeBytes = (positionSize + colorSize) * Float.BYTES;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);

        // Unbind VBO and VAO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        MemoryUtil.memFree(verticesBuffer);

        while (!window.shouldClose()) {
            glClearColor(0.57647f, 0.81961f, 0.92941f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            shader.bind();

            // Bind
            glBindVertexArray(vaoId);
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);

            // Draw
            glDrawArrays(GL_TRIANGLES, 0, 3);

            // Unbind
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glBindVertexArray(0);
            shader.unbind();

            window.update();
            if (window.isResized()) {
                // Updates relative normalized device coordinates (0-1)
                glViewport(0, 0, window.getWidth(), window.getHeight());
            }
        }

        if (shader != null) {
            shader.destroy();
        }

        // Delete VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);
        // Delete VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vboId);
    }

    public static void main(String[] args) {
        new App().run();
    }
}
