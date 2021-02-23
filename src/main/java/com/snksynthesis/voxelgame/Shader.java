package com.snksynthesis.voxelgame;

import static org.lwjgl.opengl.GL33.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * {@link Shader} handles shaders
 */
public class Shader {

    private int programId;
    private int vertexId;
    private int fragId;

    /**
     * Link shaders
     * 
     * @throws Exception when there is failure in compiling, linking, etc. of
     *                   shaders
     */
    public void link() throws Exception {

        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Unable to create shader program!");
        }

        vertexId = createShader("res/shaders/vertex.glsl", GL_VERTEX_SHADER);
        fragId = createShader("res/shaders/fragment.glsl", GL_FRAGMENT_SHADER);

        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking shader: " + glGetProgramInfoLog(programId));
        }

        if (vertexId == 0) {
            glDetachShader(programId, vertexId);
        }
        if (fragId == 0) {
            glDetachShader(programId, vertexId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning: " + glGetProgramInfoLog(programId));
        }
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void destroy() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }

    private int createShader(String path, int shaderType) throws Exception {
        String code = readFile(path);
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader: " + glGetShaderInfoLog(shaderId));
        }

        glShaderSource(shaderId, code);
        glCompileShader(shaderId);
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error creating shader: " + glGetShaderInfoLog(shaderId));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    private String readFile(String path) {
        File file = new File(path);
        StringBuilder sb = new StringBuilder();
        try (Scanner sc = new Scanner(file);) {
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine());
                sb.append(System.lineSeparator());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public int getProgramId() {
        return programId;
    }
}
