package com.snksynthesis.voxelgame.block;

import com.snksynthesis.voxelgame.gfx.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL33.*;

import java.nio.FloatBuffer;

public class Block {
    
    private Mesh mesh;
    private Texture tex;
    private BlockType type;
    private FloatBuffer allocatedMem;
    private Matrix4f model;

    public Block(BlockType type) {
        switch (type) {
            case GRASS:
                tex = new Texture("res/textures/grass.png");
                break;
            case STONE:
                tex = new Texture("res/textures/stone.png");
                break;
            case SOIL:
                tex = new Texture("res/textures/soil.png");
                break;
        }
        this.type = type;
        this.model = new Matrix4f();
        this.mesh = new Mesh(Block.CUBE_VERTICES);
        allocatedMem = MemoryUtil.memAllocFloat(16);
    }

    /**
     * Copy constructor
     * @param block - Block to be copied
     */
    public Block(Block block) {
        this(block.getType());
    }

    public void draw(Shader shader, MemoryStack stack) {
        int modelLoc = glGetUniformLocation(shader.getProgramId(), "model");
        glUniformMatrix4fv(modelLoc, false, model.get(allocatedMem));
        tex.bind();
        mesh.draw();
        tex.unbind();
    }
    
    public void destroy() {
        mesh.destroy();
        MemoryUtil.memFree(allocatedMem);
    }
    
    public BlockType getType() {
        return type;
    }
    
    public void setPos(Vector3f pos) {
        model.translate(pos);
    }

    public Matrix4f getModel() {
        return model;
    }

    // @formatter:off
    protected static final float[] CUBE_VERTICES = {
        // positionX, positionY, positionZ, texCoordX, texCoordY 
        -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
        0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
        0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
        0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
        -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
        -0.5f, -0.5f, -0.5f,  0.0f, 0.0f,

        -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
        0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
        0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
        0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
        -0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
        -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,

        -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
        -0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
        -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
        -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
        -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
        -0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

        0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
        0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
        0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
        0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
        0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
        0.5f,  0.5f,  0.5f,  1.0f, 0.0f,

        -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
        0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
        0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
        0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
        -0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
        -0.5f, -0.5f, -0.5f,  0.0f, 1.0f,

        -0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
        0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
        0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
        0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
        -0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
        -0.5f,  0.5f, -0.5f,  0.0f, 1.0f
    };
    // @formatter:on
}
