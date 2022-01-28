package com.snksynthesis.voxelgame;

import com.snksynthesis.voxelgame.chunk.ChunkManager;
import com.snksynthesis.voxelgame.gfx.Shader;
import com.snksynthesis.voxelgame.gfx.Window;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;

public class App {

    private Window window;
    private Camera cam;
    private Shader shader;
    private ChunkManager chunkManager;
    private boolean toggleWireframe;
    private int frames, fps;
    private double lastTime;

    private void draw(MemoryStack stack) {
        // Bind shader
        shader.bind();

        // Projection Matrix
        var aspectRatio = (float) window.getWidth() / window.getHeight();
        var projection = new Matrix4f().setPerspective((float) Math.toRadians(100.0f), aspectRatio, 0.1f, 200.0f);
        glUniformMatrix4fv(shader.getLocation("projection"), false, projection.get(stack.mallocFloat(16)));

        // View Matrix
        var view = cam.getViewMat();
        glUniformMatrix4fv(shader.getLocation("view"), false, view.get(stack.mallocFloat(16)));

        chunkManager.draw(shader, stack);

        // Unbind shader
        shader.unbind();
    }

    private void init() {
        window = new Window("Voxel Game", 1920, 1200);
        window.create();

        shader = new Shader("shaders/vertex.glsl", "shaders/fragment.glsl");
        try {
            shader.link();
        } catch (Exception e) {
            e.printStackTrace();
        }

        chunkManager = new ChunkManager();

        cam = new Camera();
        cam.setPos(0f, 10f, 0f);
        cam.addMouseCallback(window);

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
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glClearColor(0.8f, 0.9372549019607843f, 1.0f, 1.0f);
    }

    private void destroy() {
        shader.destroy();
        chunkManager.destroy();
    }

    private void update() {
        var camPos = cam.getPos();
        window.setTitle(String.format("Voxel Game | FPS: %d | Pos: %dx %dy %dz", fps, (int) camPos.x, (int) camPos.y, (int) camPos.z));
        cam.update(window);
        
        chunkManager.setCamPos(camPos);
        chunkManager.update(window);
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
            fps = frames;
            frames = 0;
            lastTime = glfwGetTime();
        }
    }

    public static void main(String[] args) {
        new App().run();
    }
}
