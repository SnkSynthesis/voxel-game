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
    private Texture tex;
    private Camera cam;
    private Shader shader;
    private List<Mesh> meshes;
    private List<Vector3f> positions;

    private void draw(MemoryStack stack) {
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

    private void init() {
        window = new Window("Voxel Game", 800, 600);
        window.create();

        GL.createCapabilities();

        shader = new Shader();
        try {
            shader.link();
        } catch (Exception e) {
            e.printStackTrace();
        }

        meshes = new ArrayList<>();
        positions = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                meshes.add(new Mesh(Vertices.CUBE_VERTICES));
                positions.add(new Vector3f(i, 0.0f, j));
            }
        }

        tex = new Texture("res/textures/soil.png");
        cam = new Camera();

        cam.addMouseCallback(window);

        glEnable(GL_DEPTH_TEST);
        glClearColor(0.1607843137254902f, 0.6235294117647059f, 1.0f, 1.0f);
    }

    private void destroy() {
        if (shader != null) {
            shader.destroy();
        }

        for (Mesh mesh : meshes) {
            mesh.destroy();
        }
    }

    private void update() {
        cam.procInput(window);
    }

    private void run() {
        init();
        while (!window.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            update();

            shader.bind();
            try (MemoryStack stack = MemoryStack.stackPush()) {
                draw(stack);
            }
            shader.unbind();

            window.update();
            if (window.isResized()) {
                // Updates relative normalized device coordinates (0-1)
                glViewport(0, 0, window.getWidth(), window.getHeight());
            }
        }
        destroy();
    }

    public static void main(String[] args) {
        new App().run();
    }
}
