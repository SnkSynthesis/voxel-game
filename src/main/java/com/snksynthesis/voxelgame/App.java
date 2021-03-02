package com.snksynthesis.voxelgame;

import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL33.*;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.snksynthesis.voxelgame.gfx.*;

public class App {

    private Window window;

    public void run() {
        window = new Window("Voxel Game", 800, 600);
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

        List<Mesh> meshes = new ArrayList<>();
        List<Vector3f> positions = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                meshes.add(new Mesh(vertices));
                positions.add(new Vector3f(i, 0.0f, j));
            }
        }

        Texture tex = new Texture("res/textures/soil.png");
        Camera cam = new Camera();

        cam.addMouseCallback(window);

        glEnable(GL_DEPTH_TEST);
        glClearColor(0.1607843137254902f, 0.6235294117647059f, 1.0f, 1.0f);
        while (!window.shouldClose()) {

            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            cam.procInput(window);

            shader.bind();

            try (MemoryStack stack = MemoryStack.stackPush()) {

                Matrix4f view = cam.getViewMat();
                int viewLoc = glGetUniformLocation(shader.getProgramId(), "view");
                glUniformMatrix4fv(viewLoc, false, view.get(stack.mallocFloat(16)));

                float aspectRatio = window.getWidth() / window.getHeight();
                Matrix4f projection = new Matrix4f().setPerspective((float) Math.toRadians(60.0f), aspectRatio, 0.01f,
                        1000.0f);
                int projectionLoc = glGetUniformLocation(shader.getProgramId(), "projection");
                glUniformMatrix4fv(projectionLoc, false, projection.get(stack.mallocFloat(16)));

                for (int i = 0; i < meshes.size(); i++) {
                    Matrix4f model = new Matrix4f();

                    model.translate(positions.get(i));

                    int modelLoc = glGetUniformLocation(shader.getProgramId(), "model");
                    glUniformMatrix4fv(modelLoc, false, model.get(stack.mallocFloat(16)));

                    tex.bind();
                    meshes.get(i).draw();
                    tex.unbind();
                }

            }

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

        for (Mesh mesh : meshes) {
            mesh.destroy();
        }
    }

    public static void main(String[] args) {
        new App().run();
    }
}
