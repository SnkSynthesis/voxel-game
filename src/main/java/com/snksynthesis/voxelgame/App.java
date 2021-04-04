package com.snksynthesis.voxelgame;

import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL33.*;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.snksynthesis.voxelgame.block.Block;
import com.snksynthesis.voxelgame.block.BlockType;
import com.snksynthesis.voxelgame.gfx.*;

public class App {

    private Window window;
    private Camera cam;
    private Shader shader;
    private List<Block> blocks;

    private void draw(MemoryStack stack) {
        float aspectRatio = window.getWidth() / window.getHeight();
        Matrix4f projection = new Matrix4f().setPerspective((float) Math.toRadians(80.0f), aspectRatio, 0.1f, 100.0f);

        Matrix4f view = cam.getViewMat();
        int viewLoc = glGetUniformLocation(shader.getProgramId(), "view");
        glUniformMatrix4fv(viewLoc, false, view.get(stack.mallocFloat(16)));

        int projectionLoc = glGetUniformLocation(shader.getProgramId(), "projection");
        glUniformMatrix4fv(projectionLoc, false, projection.get(stack.mallocFloat(16)));

        for (int i = 0; i < blocks.size(); i++) {
            blocks.get(i).draw(shader, stack);
        }
    }

    private void init() {
        window = new Window("Voxel Game", 800, 600);
        window.create();

        shader = new Shader();
        try {
            shader.link();
        } catch (Exception e) {
            e.printStackTrace();
        }

        blocks = new ArrayList<>();

        long field = 10;

        for (long i = 0; i < field; i++) {
            for (long j = 0; j < field; j++) {
                Block block = new Block(BlockType.GRASS);
                block.setPos(new Vector3f(i, 0.0f, j));
                blocks.add(block);
            }
        }
        for (long i = 0; i < field; i++) {
            for (long j = 0; j < field; j++) {
                Block block = new Block(BlockType.SOIL);
                block.setPos(new Vector3f(i, -1.0f, j));
                blocks.add(block);
            }
        }
        for (long i = 0; i < field; i++) {
            for (long j = 0; j < field; j++) {
                Block block = new Block(BlockType.STONE);
                block.setPos(new Vector3f(i, -2.0f, j));
                blocks.add(block);
            }
        }

        cam = new Camera();

        cam.addMouseCallback(window);

        glEnable(GL_DEPTH_TEST);
        glClearColor(0.1607843137254902f, 0.6235294117647059f, 1.0f, 1.0f);
    }

    private void destroy() {
        if (shader != null) {
            shader.destroy();
        }

        for (Block block : blocks) {
            block.destroy();
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
        }
        destroy();
    }

    public static void main(String[] args) {
        new App().run();
    }
}
