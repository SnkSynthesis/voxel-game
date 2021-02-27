package com.snksynthesis.voxelgame;

import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL33.*;

import org.joml.Matrix4f;

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
            // positionX, positionY, positionZ, texCoordX, texCoordY 
            -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
            0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,

            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
            -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,

            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
            0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,

            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
            0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
            -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
            -0.5f,  0.5f, -0.5f,  0.0f, 1.0f
        };
        // @formatter:on    

        Mesh mesh = new Mesh(vertices);
        Texture tex = new Texture("res/textures/soil.png");

        float deltaTime = 0f;
        float lastFrame = 0f;

        float inc = 0f;

        glEnable(GL_DEPTH_TEST);
        glClearColor(0.1607843137254902f, 0.6235294117647059f, 1.0f, 1.0f);
        while (!window.shouldClose()) {

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            float currentFrame = (float) glfwGetTime();
            deltaTime = currentFrame - lastFrame;
            lastFrame = currentFrame;

            inc += 2.0f * deltaTime;

            
            shader.bind();

            try (MemoryStack stack = MemoryStack.stackPush()) {

                Matrix4f model = new Matrix4f();
                model.rotateXYZ(inc, inc, inc);
                Matrix4f view = new Matrix4f();

                float aspectRatio = window.getWidth() / window.getHeight();
                Matrix4f projection = new Matrix4f().setPerspective((float) Math.toRadians(60.0f), aspectRatio, 0.01f, 1000.0f);
                view.translate(0.0f, 0.0f, -3.0f);

                int modelLoc = glGetUniformLocation(shader.getProgramId(), "model");
                glUniformMatrix4fv(modelLoc, false, model.get(stack.mallocFloat(16)));
                int viewLoc = glGetUniformLocation(shader.getProgramId(), "view");
                glUniformMatrix4fv(viewLoc, false, view.get(stack.mallocFloat(16)));
                int projectionLoc = glGetUniformLocation(shader.getProgramId(), "projection");
                glUniformMatrix4fv(projectionLoc, false, projection.get(stack.mallocFloat(16)));
            }

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
