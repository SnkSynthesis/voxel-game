package com.snksynthesis.voxelgame;

import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.glfw.GLFW.glfwGetTime;

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
            // Positions            // Texture coords 
            0.0f,  0.5f, 0.0f,      0.0f, 0.0f,
           -0.5f, -0.5f, 0.0f,      1.0f, 0.0f,
            0.5f, -0.5f, 0.0f,      0.5f, 1.0f,
        };
        // @formatter:on  

        Mesh mesh = new Mesh(vertices);
        Texture tex = new Texture("res/textures/soil.png");

        float deltaTime = 0f;
        float lastFrame = 0f;

        glClearColor(0.57647f, 0.81961f, 0.92941f, 1.0f);
        while (!window.shouldClose()) {

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            float currentFrame = (float) glfwGetTime();
            deltaTime = currentFrame - lastFrame;
            lastFrame = currentFrame;
            deltaTime = deltaTime + 0; // TEMPORARY

            shader.bind();
            tex.bind();
            mesh.draw();
            tex.unbind();
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
