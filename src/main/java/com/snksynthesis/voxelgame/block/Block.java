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
                tex = Texture.loadRGBA("textures/soil+grass+stone.png");
                this.mesh = new Mesh(GRASS_CUBE_VERTICES);
                break;
            case STONE:
                tex = Texture.loadRGBA("textures/soil+grass+stone.png");
                this.mesh = new Mesh(STONE_CUBE_VERTICES);
                break;
            case SOIL:
                tex = Texture.loadRGBA("textures/soil+grass+stone.png");
                this.mesh = new Mesh(SOIL_CUBE_VERTICES);
                break;
            case LIGHT:
                tex = Texture.loadRGBA("textures/light.png");
                this.mesh = new Mesh(LIGHT_CUBE_VERTICES, "LIGHT_SOURCE");
                break;
        }
        this.type = type;
        this.model = new Matrix4f();
        allocatedMem = MemoryUtil.memAllocFloat(16);
    }

    /**
     * Copy constructor
     * 
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
    protected final float[] CUBE_VERTICES = {
        // Positions            Texture Coords       Normals
        -0.5f, -0.5f, -0.5f,    0.0f, 0.0f,     0.0f,  0.0f, -1.0f,
         0.5f, -0.5f, -0.5f,    1.0f, 0.0f,     0.0f,  0.0f, -1.0f,
         0.5f,  0.5f, -0.5f,    1.0f, 1.0f,     0.0f,  0.0f, -1.0f,
         0.5f,  0.5f, -0.5f,    1.0f, 1.0f,     0.0f,  0.0f, -1.0f,
        -0.5f,  0.5f, -0.5f,    0.0f, 1.0f,     0.0f,  0.0f, -1.0f,
        -0.5f, -0.5f, -0.5f,    0.0f, 0.0f,     0.0f,  0.0f, -1.0f,

        -0.5f, -0.5f,  0.5f,    0.0f, 0.0f,     0.0f,  0.0f, 1.0f,
         0.5f, -0.5f,  0.5f,    1.0f, 0.0f,     0.0f,  0.0f, 1.0f,
         0.5f,  0.5f,  0.5f,    1.0f, 1.0f,     0.0f,  0.0f, 1.0f,
         0.5f,  0.5f,  0.5f,    1.0f, 1.0f,     0.0f,  0.0f, 1.0f,
        -0.5f,  0.5f,  0.5f,    0.0f, 1.0f,     0.0f,  0.0f, 1.0f,
        -0.5f, -0.5f,  0.5f,    0.0f, 0.0f,     0.0f,  0.0f, 1.0f,

        -0.5f,  0.5f,  0.5f,    1.0f, 0.0f,     -1.0f,  0.0f,  0.0f,
        -0.5f,  0.5f, -0.5f,    1.0f, 1.0f,     -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f, -0.5f,    0.0f, 1.0f,     -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f, -0.5f,    0.0f, 1.0f,     -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f,  0.5f,    0.0f, 0.0f,     -1.0f,  0.0f,  0.0f,
        -0.5f,  0.5f,  0.5f,    1.0f, 0.0f,     -1.0f,  0.0f,  0.0f,

         0.5f,  0.5f,  0.5f,    1.0f, 0.0f,     1.0f,  0.0f,  0.0f,
         0.5f,  0.5f, -0.5f,    1.0f, 1.0f,     1.0f,  0.0f,  0.0f,
         0.5f, -0.5f, -0.5f,    0.0f, 1.0f,     1.0f,  0.0f,  0.0f,
         0.5f, -0.5f, -0.5f,    0.0f, 1.0f,     1.0f,  0.0f,  0.0f,
         0.5f, -0.5f,  0.5f,    0.0f, 0.0f,     1.0f,  0.0f,  0.0f,
         0.5f,  0.5f,  0.5f,    1.0f, 0.0f,     1.0f,  0.0f,  0.0f,

        -0.5f, -0.5f, -0.5f,    0.0f, 1.0f,     0.0f, -1.0f,  0.0f,
         0.5f, -0.5f, -0.5f,    1.0f, 1.0f,     0.0f, -1.0f,  0.0f,
         0.5f, -0.5f,  0.5f,    1.0f, 0.0f,     0.0f, -1.0f,  0.0f,
         0.5f, -0.5f,  0.5f,    1.0f, 0.0f,     0.0f, -1.0f,  0.0f,
        -0.5f, -0.5f,  0.5f,    0.0f, 0.0f,     0.0f, -1.0f,  0.0f,
        -0.5f, -0.5f, -0.5f,    0.0f, 1.0f,     0.0f, -1.0f,  0.0f,

        -0.5f,  0.5f, -0.5f,    0.0f, 1.0f,     0.0f,  1.0f,  0.0f,
         0.5f,  0.5f, -0.5f,    1.0f, 1.0f,     0.0f,  1.0f,  0.0f,
         0.5f,  0.5f,  0.5f,    1.0f, 0.0f,     0.0f,  1.0f,  0.0f,
         0.5f,  0.5f,  0.5f,    1.0f, 0.0f,     0.0f,  1.0f,  0.0f,
        -0.5f,  0.5f,  0.5f,    0.0f, 0.0f,     0.0f,  1.0f,  0.0f,
        -0.5f,  0.5f, -0.5f,    0.0f, 1.0f,     0.0f,  1.0f,  0.0f
    };
    // @formatter:on

    // @formatter:off
    protected final float[] GRASS_CUBE_VERTICES = {
        // Positions            Texture Coords       Normals 
        -0.5f, -0.5f, -0.5f,    0.5f, 0.5f,     0.0f,  0.0f, -1.0f,
         0.5f, -0.5f, -0.5f,    1.0f, 0.5f,     0.0f,  0.0f, -1.0f,
         0.5f,  0.5f, -0.5f,    1.0f, 1.0f,     0.0f,  0.0f, -1.0f,
         0.5f,  0.5f, -0.5f,    1.0f, 1.0f,     0.0f,  0.0f, -1.0f,
        -0.5f,  0.5f, -0.5f,    0.5f, 1.0f,     0.0f,  0.0f, -1.0f,
        -0.5f, -0.5f, -0.5f,    0.5f, 0.5f,     0.0f,  0.0f, -1.0f,

        -0.5f, -0.5f,  0.5f,    0.5f, 0.5f,     0.0f,  0.0f, 1.0f, 
         0.5f, -0.5f,  0.5f,    1.0f, 0.5f,     0.0f,  0.0f, 1.0f,
         0.5f,  0.5f,  0.5f,    1.0f, 1.0f,     0.0f,  0.0f, 1.0f,
         0.5f,  0.5f,  0.5f,    1.0f, 1.0f,     0.0f,  0.0f, 1.0f,
        -0.5f,  0.5f,  0.5f,    0.5f, 1.0f,     0.0f,  0.0f, 1.0f,
        -0.5f, -0.5f,  0.5f,    0.5f, 0.5f,     0.0f,  0.0f, 1.0f,

        -0.5f,  0.5f,  0.5f,    1.0f, 1.0f,     -1.0f,  0.0f,  0.0f,
        -0.5f,  0.5f, -0.5f,    0.5f, 1.0f,     -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f, -0.5f,    0.5f, 0.5f,     -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f, -0.5f,    0.5f, 0.5f,     -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f,  0.5f,    1.0f, 0.5f,     -1.0f,  0.0f,  0.0f,
        -0.5f,  0.5f,  0.5f,    1.0f, 1.0f,     -1.0f,  0.0f,  0.0f,

         0.5f,  0.5f,  0.5f,    1.0f, 1.0f,     1.0f,  0.0f,  0.0f,
         0.5f,  0.5f, -0.5f,    0.5f, 1.0f,     1.0f,  0.0f,  0.0f,
         0.5f, -0.5f, -0.5f,    0.5f, 0.5f,     1.0f,  0.0f,  0.0f,
         0.5f, -0.5f, -0.5f,    0.5f, 0.5f,     1.0f,  0.0f,  0.0f,
         0.5f, -0.5f,  0.5f,    1.0f, 0.5f,     1.0f,  0.0f,  0.0f,
         0.5f,  0.5f,  0.5f,    1.0f, 1.0f,     1.0f,  0.0f,  0.0f,

        -0.5f, -0.5f, -0.5f,    0.0f, 0.5f,     0.0f, -1.0f,  0.0f,
         0.5f, -0.5f, -0.5f,    0.5f, 0.5f,     0.0f, -1.0f,  0.0f,
         0.5f, -0.5f,  0.5f,    0.5f, 0.0f,     0.0f, -1.0f,  0.0f,
         0.5f, -0.5f,  0.5f,    0.5f, 0.0f,     0.0f, -1.0f,  0.0f,
        -0.5f, -0.5f,  0.5f,    0.0f, 0.0f,     0.0f, -1.0f,  0.0f,
        -0.5f, -0.5f, -0.5f,    0.0f, 0.5f,     0.0f, -1.0f,  0.0f,

        -0.5f,  0.5f, -0.5f,    0.0f, 1.0f,     0.0f,  1.0f,  0.0f,
         0.5f,  0.5f, -0.5f,    0.5f, 1.0f,     0.0f,  1.0f,  0.0f,
         0.5f,  0.5f,  0.5f,    0.5f, 0.5f,     0.0f,  1.0f,  0.0f,
         0.5f,  0.5f,  0.5f,    0.5f, 0.5f,     0.0f,  1.0f,  0.0f,
        -0.5f,  0.5f,  0.5f,    0.0f, 0.5f,     0.0f,  1.0f,  0.0f,
        -0.5f,  0.5f, -0.5f,    0.0f, 1.0f,     0.0f,  1.0f,  0.0f
    };
    // @formatter:on

    // @formatter:off
    protected final float[] SOIL_CUBE_VERTICES = {
        // Positions          Texture Coords          Normals
        -0.5f, -0.5f, -0.5f,    0.0f, 0.0f,     0.0f,  0.0f, -1.0f,
         0.5f, -0.5f, -0.5f,    0.5f, 0.0f,     0.0f,  0.0f, -1.0f,
         0.5f,  0.5f, -0.5f,    0.5f, 0.5f,     0.0f,  0.0f, -1.0f,
         0.5f,  0.5f, -0.5f,    0.5f, 0.5f,     0.0f,  0.0f, -1.0f,
        -0.5f,  0.5f, -0.5f,    0.0f, 0.5f,     0.0f,  0.0f, -1.0f,
        -0.5f, -0.5f, -0.5f,    0.0f, 0.0f,     0.0f,  0.0f, -1.0f,

        -0.5f, -0.5f,  0.5f,    0.0f, 0.0f,     0.0f,  0.0f, 1.0f,
         0.5f, -0.5f,  0.5f,    0.5f, 0.0f,     0.0f,  0.0f, 1.0f,
         0.5f,  0.5f,  0.5f,    0.5f, 0.5f,     0.0f,  0.0f, 1.0f,
         0.5f,  0.5f,  0.5f,    0.5f, 0.5f,     0.0f,  0.0f, 1.0f,
        -0.5f,  0.5f,  0.5f,    0.0f, 0.5f,     0.0f,  0.0f, 1.0f,
        -0.5f, -0.5f,  0.5f,    0.0f, 0.0f,     0.0f,  0.0f, 1.0f,

        -0.5f,  0.5f,  0.5f,    0.5f, 0.0f,     -1.0f,  0.0f,  0.0f,
        -0.5f,  0.5f, -0.5f,    0.5f, 0.5f,     -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f, -0.5f,    0.0f, 0.5f,     -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f, -0.5f,    0.0f, 0.5f,     -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f,  0.5f,    0.0f, 0.0f,     -1.0f,  0.0f,  0.0f,
        -0.5f,  0.5f,  0.5f,    0.5f, 0.0f,     -1.0f,  0.0f,  0.0f,

         0.5f,  0.5f,  0.5f,    0.5f, 0.0f,     1.0f,  0.0f,  0.0f,
         0.5f,  0.5f, -0.5f,    0.5f, 0.5f,     1.0f,  0.0f,  0.0f,
         0.5f, -0.5f, -0.5f,    0.0f, 0.5f,     1.0f,  0.0f,  0.0f,
         0.5f, -0.5f, -0.5f,    0.0f, 0.5f,     1.0f,  0.0f,  0.0f,
         0.5f, -0.5f,  0.5f,    0.0f, 0.0f,     1.0f,  0.0f,  0.0f,
         0.5f,  0.5f,  0.5f,    0.5f, 0.0f,     1.0f,  0.0f,  0.0f,

        -0.5f, -0.5f, -0.5f,    0.0f, 0.5f,     0.0f, -1.0f,  0.0f,
         0.5f, -0.5f, -0.5f,    0.5f, 0.5f,     0.0f, -1.0f,  0.0f,
         0.5f, -0.5f,  0.5f,    0.5f, 0.0f,     0.0f, -1.0f,  0.0f,
         0.5f, -0.5f,  0.5f,    0.5f, 0.0f,     0.0f, -1.0f,  0.0f,
        -0.5f, -0.5f,  0.5f,    0.0f, 0.0f,     0.0f, -1.0f,  0.0f,
        -0.5f, -0.5f, -0.5f,    0.0f, 0.5f,     0.0f, -1.0f,  0.0f,

        -0.5f,  0.5f, -0.5f,    0.0f, 0.5f,     0.0f,  1.0f,  0.0f,
         0.5f,  0.5f, -0.5f,    0.5f, 0.5f,     0.0f,  1.0f,  0.0f,
         0.5f,  0.5f,  0.5f,    0.5f, 0.0f,     0.0f,  1.0f,  0.0f,
         0.5f,  0.5f,  0.5f,    0.5f, 0.0f,     0.0f,  1.0f,  0.0f,
        -0.5f,  0.5f,  0.5f,    0.0f, 0.0f,     0.0f,  1.0f,  0.0f,
        -0.5f,  0.5f, -0.5f,    0.0f, 0.5f,     0.0f,  1.0f,  0.0f
    };
    // @formatter:on

    // @formatter:off
    protected final float[] STONE_CUBE_VERTICES = {
        // Positions            Texture Coords         Normals 
        -0.5f, -0.5f, -0.5f,    0.5f, 0.0f,      0.0f,  0.0f, -1.0f,
         0.5f, -0.5f, -0.5f,    1.0f, 0.0f,      0.0f,  0.0f, -1.0f,
         0.5f,  0.5f, -0.5f,    1.0f, 0.5f,      0.0f,  0.0f, -1.0f,
         0.5f,  0.5f, -0.5f,    1.0f, 0.5f,      0.0f,  0.0f, -1.0f,
        -0.5f,  0.5f, -0.5f,    0.5f, 0.5f,      0.0f,  0.0f, -1.0f,
        -0.5f, -0.5f, -0.5f,    0.5f, 0.0f,      0.0f,  0.0f, -1.0f,

        -0.5f, -0.5f,  0.5f,    0.5f, 0.0f,      0.0f,  0.0f, 1.0f,
         0.5f, -0.5f,  0.5f,    1.0f, 0.0f,      0.0f,  0.0f, 1.0f,
         0.5f,  0.5f,  0.5f,    1.0f, 0.5f,      0.0f,  0.0f, 1.0f,
         0.5f,  0.5f,  0.5f,    1.0f, 0.5f,      0.0f,  0.0f, 1.0f,
        -0.5f,  0.5f,  0.5f,    0.5f, 0.5f,      0.0f,  0.0f, 1.0f,
        -0.5f, -0.5f,  0.5f,    0.5f, 0.0f,      0.0f,  0.0f, 1.0f,

        -0.5f,  0.5f,  0.5f,    0.5f, 0.0f,      -1.0f,  0.0f,  0.0f,
        -0.5f,  0.5f, -0.5f,    1.0f, 0.0f,      -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f, -0.5f,    1.0f, 0.5f,      -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f, -0.5f,    1.0f, 0.5f,      -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f,  0.5f,    0.5f, 0.5f,      -1.0f,  0.0f,  0.0f,
        -0.5f,  0.5f,  0.5f,    0.5f, 0.0f,      -1.0f,  0.0f,  0.0f,

         0.5f,  0.5f,  0.5f,    0.5f, 0.0f,      1.0f,  0.0f,  0.0f,
         0.5f,  0.5f, -0.5f,    1.0f, 0.0f,      1.0f,  0.0f,  0.0f,
         0.5f, -0.5f, -0.5f,    1.0f, 0.5f,      1.0f,  0.0f,  0.0f,
         0.5f, -0.5f, -0.5f,    1.0f, 0.5f,      1.0f,  0.0f,  0.0f,
         0.5f, -0.5f,  0.5f,    0.5f, 0.5f,      1.0f,  0.0f,  0.0f,
         0.5f,  0.5f,  0.5f,    0.5f, 0.0f,      1.0f,  0.0f,  0.0f,

        -0.5f, -0.5f, -0.5f,    0.5f, 0.0f,      0.0f, -1.0f,  0.0f,
         0.5f, -0.5f, -0.5f,    1.0f, 0.0f,      0.0f, -1.0f,  0.0f,
         0.5f, -0.5f,  0.5f,    1.0f, 0.5f,      0.0f, -1.0f,  0.0f,
         0.5f, -0.5f,  0.5f,    1.0f, 0.5f,      0.0f, -1.0f,  0.0f,
        -0.5f, -0.5f,  0.5f,    0.5f, 0.5f,      0.0f, -1.0f,  0.0f,
        -0.5f, -0.5f, -0.5f,    0.5f, 0.0f,      0.0f, -1.0f,  0.0f,

        -0.5f,  0.5f, -0.5f,    0.5f, 0.0f,      0.0f,  1.0f,  0.0f,
         0.5f,  0.5f, -0.5f,    1.0f, 0.0f,      0.0f,  1.0f,  0.0f,
         0.5f,  0.5f,  0.5f,    1.0f, 0.5f,      0.0f,  1.0f,  0.0f,
         0.5f,  0.5f,  0.5f,    1.0f, 0.5f,      0.0f,  1.0f,  0.0f,
        -0.5f,  0.5f,  0.5f,    0.5f, 0.5f,      0.0f,  1.0f,  0.0f,
        -0.5f,  0.5f, -0.5f,    0.5f, 0.0f,      0.0f,  1.0f,  0.0f
    };
    // @formatter:on

    // @formatter:off
    protected final float[] LIGHT_CUBE_VERTICES = {
        // Positions            Texture Coords
        -0.5f, -0.5f, -0.5f,    0.0f, 0.0f,
         0.5f, -0.5f, -0.5f,    1.0f, 0.0f,
         0.5f,  0.5f, -0.5f,    1.0f, 1.0f,
         0.5f,  0.5f, -0.5f,    1.0f, 1.0f,
        -0.5f,  0.5f, -0.5f,    0.0f, 1.0f,
        -0.5f, -0.5f, -0.5f,    0.0f, 0.0f,

        -0.5f, -0.5f,  0.5f,    0.0f, 0.0f,
         0.5f, -0.5f,  0.5f,    1.0f, 0.0f,
         0.5f,  0.5f,  0.5f,    1.0f, 1.0f,
         0.5f,  0.5f,  0.5f,    1.0f, 1.0f,
        -0.5f,  0.5f,  0.5f,    0.0f, 1.0f,
        -0.5f, -0.5f,  0.5f,    0.0f, 0.0f,

        -0.5f,  0.5f,  0.5f,    1.0f, 0.0f,
        -0.5f,  0.5f, -0.5f,    1.0f, 1.0f,
        -0.5f, -0.5f, -0.5f,    0.0f, 1.0f,
        -0.5f, -0.5f, -0.5f,    0.0f, 1.0f,
        -0.5f, -0.5f,  0.5f,    0.0f, 0.0f,
        -0.5f,  0.5f,  0.5f,    1.0f, 0.0f,

         0.5f,  0.5f,  0.5f,    1.0f, 0.0f,
         0.5f,  0.5f, -0.5f,    1.0f, 1.0f,
         0.5f, -0.5f, -0.5f,    0.0f, 1.0f,
         0.5f, -0.5f, -0.5f,    0.0f, 1.0f,
         0.5f, -0.5f,  0.5f,    0.0f, 0.0f,
         0.5f,  0.5f,  0.5f,    1.0f, 0.0f,

        -0.5f, -0.5f, -0.5f,    0.0f, 1.0f,
         0.5f, -0.5f, -0.5f,    1.0f, 1.0f,
         0.5f, -0.5f,  0.5f,    1.0f, 0.0f,
         0.5f, -0.5f,  0.5f,    1.0f, 0.0f,
        -0.5f, -0.5f,  0.5f,    0.0f, 0.0f,
        -0.5f, -0.5f, -0.5f,    0.0f, 1.0f,

        -0.5f,  0.5f, -0.5f,    0.0f, 1.0f,
         0.5f,  0.5f, -0.5f,    1.0f, 1.0f,
         0.5f,  0.5f,  0.5f,    1.0f, 0.0f,
         0.5f,  0.5f,  0.5f,    1.0f, 0.0f,
        -0.5f,  0.5f,  0.5f,    0.0f, 0.0f,
        -0.5f,  0.5f, -0.5f,    0.0f, 1.0f
    };
    // @formatter:on

    // @formatter:off
    protected final float[] COLORED_CUBE_VERTICES = {
        -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f,
        0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f, 
        0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f, 
        0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f, 
        -0.5f,  0.5f, -0.5f,  0.0f,  0.0f, -1.0f, 
        -0.5f, -0.5f, -0.5f,  0.0f,  0.0f, -1.0f, 

        -0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
        0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
        0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
        0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
        -0.5f,  0.5f,  0.5f,  0.0f,  0.0f, 1.0f,
        -0.5f, -0.5f,  0.5f,  0.0f,  0.0f, 1.0f,

        -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,
        -0.5f,  0.5f, -0.5f, -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f, -0.5f, -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f,  0.5f, -1.0f,  0.0f,  0.0f,
        -0.5f,  0.5f,  0.5f, -1.0f,  0.0f,  0.0f,

        0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,
        0.5f,  0.5f, -0.5f,  1.0f,  0.0f,  0.0f,
        0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,
        0.5f, -0.5f, -0.5f,  1.0f,  0.0f,  0.0f,
        0.5f, -0.5f,  0.5f,  1.0f,  0.0f,  0.0f,
        0.5f,  0.5f,  0.5f,  1.0f,  0.0f,  0.0f,

        -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,
        0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,
        0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
        0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
        -0.5f, -0.5f,  0.5f,  0.0f, -1.0f,  0.0f,
        -0.5f, -0.5f, -0.5f,  0.0f, -1.0f,  0.0f,

        -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,
        0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f,
        0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
        0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
        -0.5f,  0.5f,  0.5f,  0.0f,  1.0f,  0.0f,
        -0.5f,  0.5f, -0.5f,  0.0f,  1.0f,  0.0f
    };
    // @formatter:on
}
