package com.snksynthesis.voxelgame;

import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.glfw.GLFW.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.snksynthesis.voxelgame.block.Block;
import com.snksynthesis.voxelgame.block.BlockType;
import com.snksynthesis.voxelgame.chunk.Chunk;
import com.snksynthesis.voxelgame.gfx.*;

public class App {

    private Window window;
    private Camera cam;
    private Shader shader;
    private Shader lightShader;
    private Chunk chunk;
    private Block light;
    private Vector3f lightPos;
    private boolean toggleWireframe;
    private int frames;
    private double lastTime;

    private void draw(MemoryStack stack) {
        // Bind shader
        shader.bind();

        // Set light color and position
        glUniform3fv(shader.getLocation("lightPos"), lightPos.get(stack.mallocFloat(3)));
        glUniform3f(shader.getLocation("lightColor"), 1.0f, 1.0f, 1.0f);

        // Projection Matrix
        var aspectRatio = (float) window.getWidth() / window.getHeight();
        var projection = new Matrix4f().setPerspective((float) Math.toRadians(100.0f), aspectRatio, 0.1f, 100.0f);
        glUniformMatrix4fv(shader.getLocation("projection"), false, projection.get(stack.mallocFloat(16)));

        // View Matrix
        var view = cam.getViewMat();
        glUniformMatrix4fv(shader.getLocation("view"), false, view.get(stack.mallocFloat(16)));

        chunk.draw(shader, stack);

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

        shader = new Shader("shaders/vertex.glsl", "shaders/fragment.glsl");
        try {
            shader.link();
        } catch (Exception e) {
            e.printStackTrace();
        }

        lightShader = new Shader("shaders/light_vertex.glsl", "shaders/light_fragment.glsl");
        try {
            lightShader.link();
        } catch (Exception e) {
            e.printStackTrace();
        }

        chunk = new Chunk();

        cam = new Camera();
        cam.setPos(chunk.WIDTH / 2f, 10f, chunk.WIDTH / 2f);
        cam.addMouseCallback(window);

        lightPos = new Vector3f(chunk.WIDTH / 2f, 20.5f, chunk.WIDTH / 2f);
        light = new Block(BlockType.LIGHT);
        light.getModel().translate(lightPos);

        toggleWireframe = false;
        glfwSetKeyCallback(window.getRawWindow(), (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_T && action == GLFW_PRESS) {
                toggleWireframe = !toggleWireframe;
                if (toggleWireframe) {
                    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                } else {
                    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                }
            }
        });

        frames = 0;
        lastTime = glfwGetTime();

        glEnable(GL_DEPTH_TEST);
        glClearColor(0.1607843137254902f, 0.6235294117647059f, 1.0f, 1.0f);
    }

    private void destroy() {
        shader.destroy();
        shader.destroy();
        chunk.destroy();
    }

    private void update() {
        cam.procInput(window);
        chunk.genWorld();
        calcFps();
    }

    private void run() {
        init();

        while (!window.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            update();

            MemoryStack stack = MemoryStack.stackPush();
            try {
                draw(stack);
            } finally {
                stack.close();
            }

            window.update();
        }
        destroy();
    }

    private void calcFps() {
        frames++;
        if (glfwGetTime() - lastTime > 1.0) {
            window.setTitle("Voxel Game | FPS: " + frames);
            frames = 0;
            lastTime = glfwGetTime();
        }
    }

    public static void main(String[] args) {
        new App().run();
    }
}
