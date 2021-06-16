package com.snksynthesis.voxelgame.gfx;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryUtil;
import static org.lwjgl.opengl.GL33.*;


public class Mesh {

    private int vaoId; // Vertex Array Object ID
    private int vboId; // Vertex Buffer Object ID
    private float[] vertices;

    /**
     * {@link Mesh} is for initializing and drawing meshes
     * 
     * @param vertices must be in format
     * 
     *                 <pre>
     *    {posX, posY, posZ, texCoordX, texCoordY, faceIndex, ...}
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
        int positionSize = 3;
        int textureSize = 2;
        int vertexSizeBytes = (positionSize + textureSize) * Float.BYTES;
        // Positions
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);
        // Texture Coordinates
        glVertexAttribPointer(1, textureSize, GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);

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
}
