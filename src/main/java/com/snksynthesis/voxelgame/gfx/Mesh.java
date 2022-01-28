package com.snksynthesis.voxelgame.gfx;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33.*;

public class Mesh {

    public static int POSITION_SIZE = 3;
    public static int TEXTURE_COORD_SIZE = 2;
    public static int ALPHA_VALUE_SIZE = 1;
    public static int AMBIENT_VALUE_SIZE = 1;
    public static int VERTEX_SIZE = POSITION_SIZE + TEXTURE_COORD_SIZE + ALPHA_VALUE_SIZE + AMBIENT_VALUE_SIZE;

    private final int vaoId; // Vertex Array Object ID
    private final int vboId; // Vertex Buffer Object ID
    private final float[] vertices;

    /**
     * {@link Mesh} is for initializing and drawing meshes
     * 
     * @param vertices must be in format
     * 
     *                 <pre>
     *    {posX, posY, posZ, texCoordX, texCoordY, ...}
     *                 </pre>
     * 
     */
    public Mesh(float[] vertices) {
        this.vertices = vertices;

        // Allocate memory
        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
        verticesBuffer.put(vertices).flip();

        // Create VAO
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Vertices
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

        // Positions
        glVertexAttribPointer(0, POSITION_SIZE, GL_FLOAT, false, VERTEX_SIZE * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // Texture Coordinates
        glVertexAttribPointer(1, TEXTURE_COORD_SIZE, GL_FLOAT, false, VERTEX_SIZE * Float.BYTES,
                getSumInBytes(POSITION_SIZE));
        glEnableVertexAttribArray(1);

        // Alpha Values
        glVertexAttribPointer(2, ALPHA_VALUE_SIZE, GL_FLOAT, false, VERTEX_SIZE * Float.BYTES,
                getSumInBytes(POSITION_SIZE, TEXTURE_COORD_SIZE));
        glEnableVertexAttribArray(2);

        // Ambient Values
        glVertexAttribPointer(3, AMBIENT_VALUE_SIZE, GL_FLOAT, false, VERTEX_SIZE * Float.BYTES,
                getSumInBytes(POSITION_SIZE, TEXTURE_COORD_SIZE, ALPHA_VALUE_SIZE));
        glEnableVertexAttribArray(3);

        // Unbind VBO and VAO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        // Free memory
        if (verticesBuffer != null) {
            MemoryUtil.memFree(verticesBuffer);
        }
    }

    public void draw(int count) {
        // Bind
        glBindVertexArray(vaoId);

        // Draw
        glDrawArrays(GL_TRIANGLES, 0, count);

        // Unbind
        glBindVertexArray(0);
    }

    public void destroy() {
        // Delete VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);
        // Delete VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    public float[] getVertices() {
        return vertices;
    }

    private int getSumInBytes(int... sizes) {
        int sizeSum = 0;
        for (int size : sizes) {
            sizeSum += size * Float.BYTES;
        }
        return sizeSum;
    }
}
