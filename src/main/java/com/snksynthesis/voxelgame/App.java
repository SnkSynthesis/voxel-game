package com.snksynthesis.voxelgame;

import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL33.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.snksynthesis.voxelgame.block.Block;
import com.snksynthesis.voxelgame.block.BlockManager;
import com.snksynthesis.voxelgame.block.BlockType;
import com.snksynthesis.voxelgame.gfx.*;

public class App {

    private Window window;
    private Camera cam;
    private Shader shader;
    private Shader lightShader;
    private BlockManager blockManager;
    private Block light;
    private Vector3f lightPos;

    private void draw(MemoryStack stack) {
        // Bind shader
        shader.bind();

        // Set light color and position
        glUniform3fv(shader.getLocation("lightPos"), lightPos.get(stack.mallocFloat(3)));
        glUniform3f(shader.getLocation("lightColor"), 1.0f, 1.0f, 1.0f);

        // Projection Matrix
        float aspectRatio = window.getWidth() / window.getHeight();
        Matrix4f projection = new Matrix4f().setPerspective((float) Math.toRadians(100.0f), aspectRatio, 0.1f, 100.0f);
        glUniformMatrix4fv(shader.getLocation("projection"), false, projection.get(stack.mallocFloat(16)));

        // View Matrix
        Matrix4f view = cam.getViewMat();
        glUniformMatrix4fv(shader.getLocation("view"), false, view.get(stack.mallocFloat(16)));

        blockManager.draw(shader, stack);

        // Unbind shader
        shader.unbind();

        // Bind Light Shader
        lightShader.bind();

        // Set projection and view matrices
        glUniformMatrix4fv(lightShader.getLocation("projection"), false, projection.get(stack.mallocFloat(16)));
        glUniformMatrix4fv(lightShader.getLocation("view"), false, view.get(stack.mallocFloat(16)));

        // Draw light source
        light.draw(lightShader, stack);

        // Unbind light shader
        lightShader.unbind();
    }

    private void init() {
        window = new Window("Voxel Game", 650, 650);
        window.create();

        shader = new Shader("res/shaders/vertex.glsl", "res/shaders/fragment.glsl");
        try {
            shader.link();
        } catch (Exception e) {
            e.printStackTrace();
        }

        lightShader = new Shader("res/shaders/light_vertex.glsl", "res/shaders/light_fragment.glsl");
        try {
            lightShader.link();
        } catch (Exception e) {
            e.printStackTrace();
        }

        cam = new Camera();
        cam.addMouseCallback(window);

        blockManager = new BlockManager();

        lightPos = new Vector3f(5.0f, 5.5f, 5.0f);
        light = new Block(BlockType.LIGHT);
        light.getModel().translate(lightPos);

        glEnable(GL_DEPTH_TEST);
        glClearColor(0.1607843137254902f, 0.6235294117647059f, 1.0f, 1.0f);
        // glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
    }

    private void destroy() {
        shader.destroy();
        shader.destroy();
        blockManager.destroy();
    }

    private void update() {
        cam.procInput(window);
        blockManager.genWorld();
        lightPos.add(1.0f * window.getDeltaTime(), 1.0f * window.getDeltaTime(), 1.0f * window.getDeltaTime());
        light.getModel().identity();
        light.getModel().translate(lightPos);
    }

    private void run() {
        init();

        while (!window.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            update();

            try (MemoryStack stack = MemoryStack.stackPush()) {
                draw(stack);
            }

            window.update();
        }
        destroy();
    }

    public static void main(String[] args) {
        new App().run();
    }
}
