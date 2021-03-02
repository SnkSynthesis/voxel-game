package com.snksynthesis.voxelgame.gfx;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryUtil;
import static org.lwjgl.opengl.GL33.*;

/**
 * {@link Mesh} is for initializing and drawing meshes
 */
public class Mesh {

    private int vaoId;
    private int vboId;

    /**
     * @param vertices must be in format
     * 
     *                 <pre>
     *  {posX, posY, posZ, texCoordX, texCoordY, ...}
     *                 </pre>
     */
    public Mesh(float[] vertices) {

        FloatBuffer verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);
        verticesBuffer.put(vertices).flip();

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
        glEnableVertexAttribArray(0);
        int positionSize = 3;
        int textureSize = 2;
        int vertexSizeBytes = (positionSize + textureSize) * Float.BYTES;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glVertexAttribPointer(1, textureSize, GL_FLOAT, false, vertexSizeBytes, positionSize * Float.BYTES);

        // Unbind VBO and VAO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);

        // Free MemoryUtil
        MemoryUtil.memFree(verticesBuffer);
    }

    public void draw() {
        // Bind
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw
        glDrawArrays(GL_TRIANGLES, 0, 36);

        // Unbind
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
    }

    public void destroy() {
        // Delete VBO
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);
        // Delete VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vboId);
    }
}
