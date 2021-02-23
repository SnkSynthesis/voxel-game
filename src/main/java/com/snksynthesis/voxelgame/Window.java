package com.snksynthesis.voxelgame;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.system.MemoryUtil;

/**
 * {@link Window} is for creating and managing a window.
 */
public class Window {

    private long window;
    private String title;
    private int width, height;
    private boolean resized;

    public Window(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        resized = false;
    }

    /**
     * Creates window
     */
    public void create() {
        // Print error message to System.err
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) { // Initialize GLFW
            throw new RuntimeException("GLFW unintialized!");
        }

        // Configure GLFW window
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // Create window
        window = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create window!");
        }

        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;
            resized = true;
        });

        // Center window
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);

        // Make current OpenGL context
        glfwMakeContextCurrent(window);

        // Enable v-sync
        glfwSwapInterval(1);

        glfwShowWindow(window);
    }

    /**
     * Swap buffer and poll events
     */
    public void update() {
        glfwPollEvents();
        glfwSwapBuffers(window);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    /**
     * @return Whether window is resized or not
     */
    public boolean isResized() {
        if (resized) {
            resized = false;
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return Raw GLFW window
     */
    public long getRawWindow() {
        return window;
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
