package com.snksynthesis.voxelgame.block;

import com.snksynthesis.voxelgame.gfx.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;
import static org.lwjgl.opengl.GL33.*;

public class Block {
    
    private Mesh mesh;
    private Texture tex;
    private Vector3f pos;
    private BlockType type;

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
        this.pos = new Vector3f();
        this.mesh = new Mesh(Block.CUBE_VERTICES);
    }

    public void draw(Shader shader, MemoryStack stack) {
        Matrix4f model = new Matrix4f();
        model.translate(pos);
        int modelLoc = glGetUniformLocation(shader.getProgramId(), "model");
        glUniformMatrix4fv(modelLoc, false, model.get(stack.mallocFloat(16)));
        tex.bind();
        mesh.draw();
        tex.unbind();
    }

    
    public void destroy() {
        mesh.destroy();
    }
    
    public BlockType getType() {
        return type;
    }
    
    public void setPos(Vector3f pos) {
        this.pos = pos;
    }

    public Vector3f getPos() {
        return pos;
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
