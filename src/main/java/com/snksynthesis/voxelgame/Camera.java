package com.snksynthesis.voxelgame;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {

    private Vector3f up, front, pos;
    private boolean firstMouse;
    private float lastX, lastY, yaw, pitch;

    public Camera() {
        up = new Vector3f(0.0f, 1.0f, 0.0f);
        front = new Vector3f(0.0f, 0.0f, -1.0f);
        pos = new Vector3f(0.0f, 0.0f, 0.0f);
    }

    public void procInput(Window window) {
        // Keyboard input
        final float SPEED = 5.0f * window.getDeltaTime();
        if (glfwGetKey(window.getRawWindow(), GLFW_KEY_W) == GLFW_PRESS) {
            pos.add(new Vector3f(front).mul(SPEED));
        }
        if (glfwGetKey(window.getRawWindow(), GLFW_KEY_S) == GLFW_PRESS) {
            pos.sub(new Vector3f(front).mul(SPEED));
        }
        if (glfwGetKey(window.getRawWindow(), GLFW_KEY_A) == GLFW_PRESS) {
            pos.sub(new Vector3f(front).cross(up).normalize().mul(SPEED));
        }
        if (glfwGetKey(window.getRawWindow(), GLFW_KEY_D) == GLFW_PRESS) {
            pos.add(new Vector3f(front).cross(up).normalize().mul(SPEED));
        }

        // Toggle cursor mode
        if (glfwGetKey(window.getRawWindow(), GLFW_KEY_Q) == GLFW_PRESS) {
            glfwSetInputMode(window.getRawWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);
        }
        if (glfwGetKey(window.getRawWindow(), GLFW_KEY_ESCAPE) == GLFW_PRESS) {
            glfwSetInputMode(window.getRawWindow(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        }
    }

    public void addMouseCallback(Window window) {

        lastX = window.getWidth() / 2;
        lastY = window.getHeight() / 2;
        firstMouse = true;

        glfwSetCursorPos(window.getRawWindow(), lastX, lastY);
        glfwSetInputMode(window.getRawWindow(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        glfwSetCursorPosCallback(window.getRawWindow(), (_window, xpos, ypos) -> {
            if (glfwGetInputMode(window.getRawWindow(), GLFW_CURSOR) == GLFW_CURSOR_DISABLED) {
                if (firstMouse) {
                    lastX = (float) xpos;
                    lastY = (float) ypos;
                    firstMouse = false;
                }
    
                float xOffset = (float) (xpos - lastX);
                float yOffset = (float) (lastY - ypos);
                lastX = (float) xpos;
                lastY = (float) ypos;
    
                final float SENSITIVITY = 0.1f;
    
                xOffset *= SENSITIVITY;
                yOffset *= SENSITIVITY;
    
                yaw += xOffset;
                pitch += yOffset;
    
                if (pitch > 89.0f) {
                    pitch = 89.0f;
                } else if (pitch < -89.0f) {
                    pitch = -89.0f;
                }
    
                Vector3f direction = new Vector3f();
                direction.x = (float) Math.cos(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
                direction.y = (float) Math.sin(Math.toRadians(pitch));
                direction.z = (float) Math.sin(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
                front = direction.normalize();
            }
        });
    }

    public Matrix4f getViewMat() {
        return new Matrix4f().lookAt(pos, new Vector3f(pos).add(front), up);
    }
}
