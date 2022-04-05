package com.snksynthesis.voxelgame.gfx;

import java.io.InputStream;
import java.util.Scanner;

import static org.lwjgl.opengl.GL33.*;

/**
 * {@link Shader} handles shaders
 */
public class Shader {

    private int programId;
    private int vertexId;
    private int fragId;
    private final InputStream vertexPath;
    private final InputStream fragPath;
    private boolean linked = false;

    /**
     * Initializes a Shader object
     * @param vertexPath path to vertex shader
     * @param fragPath path to fragment shader
     */
    public Shader(String vertexPath, String fragPath) {
        this.vertexPath = getClass().getClassLoader().getResourceAsStream(vertexPath);
        this.fragPath = getClass().getClassLoader().getResourceAsStream(fragPath);
    }

    /**
     * Link shaders
     * 
     * @throws Exception when there is failure in compiling, linking, etc. of
     *                   shaders
     */
    public void link() throws Exception {
        linked = true;
        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Unable to create shader program!");
        }

        vertexId = createShader(vertexPath, GL_VERTEX_SHADER);
        fragId = createShader(fragPath, GL_FRAGMENT_SHADER);

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

    private int createShader(InputStream input, int shaderType) throws Exception {
        String code = readFile(input);
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

    private String readFile(InputStream input) {
        StringBuilder sb = new StringBuilder();
        try (Scanner sc = new Scanner(input)) {
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine());
                sb.append(System.lineSeparator());
            }
        }

        return sb.toString();
    }

    public boolean isLinked() {
        return linked;
    }

    public int getProgramId() {
        return programId;
    }

    public int getLocation(String name) {
        return glGetUniformLocation(programId, name);
    }
}
