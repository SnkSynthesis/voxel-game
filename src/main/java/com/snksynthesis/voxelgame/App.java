package com.snksynthesis.voxelgame;

import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL33.*;

public class App {

    private Window window;

    public void run() {
        window = new Window("Voxel Game", 500, 500);
        window.create();

        GL.createCapabilities();
        glClearColor(0.57647f, 0.81961f, 0.92941f, 1.0f);

        while (!window.shouldClose()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            window.update();
        }
    }

    public static void main(String[] args) {
        new App().run();
    }
}
