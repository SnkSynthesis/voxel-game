package com.snksynthesis.voxelgame;

import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL33.*;

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

        Mesh mesh = new Mesh(vertices);

        glClearColor(0.57647f, 0.81961f, 0.92941f, 1.0f);
        while (!window.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            shader.bind();
            mesh.draw();
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

        mesh.destroy();
    }

    public static void main(String[] args) {
        new App().run();
    }
}
