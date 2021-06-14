package com.snksynthesis.voxelgame.block;

import com.snksynthesis.voxelgame.gfx.*;
import com.snksynthesis.voxelgame.texture.Texture;

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
    /**
     * Face Index:
     * <ul>
     *  <li>0 - Left</li>
     *  <li>1 - Right</li>
     *  <li>2 - Front</li>
     *  <li>3 - Back</li>
     *  <li>4 - Bottom</li>
     *  <li>5 - Top</li>
     * </ul>
     */
    public static final float[][] CUBE_POSITIONS = {
        {
            -0.5f, -0.5f, -0.5f,
             0.5f, -0.5f, -0.5f,
             0.5f,  0.5f, -0.5f,
             0.5f,  0.5f, -0.5f,
            -0.5f,  0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f
        },

        {
            -0.5f, -0.5f,  0.5f,
             0.5f, -0.5f,  0.5f,
             0.5f,  0.5f,  0.5f,
             0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,
            -0.5f, -0.5f,  0.5f
        },
        
        {
            -0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f
        },
        
        { 
            0.5f,  0.5f,  0.5f,
            0.5f,  0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, -0.5f,  0.5f,
            0.5f,  0.5f,  0.5f
        },
        
        {
            -0.5f, -0.5f, -0.5f,
             0.5f, -0.5f, -0.5f,
             0.5f, -0.5f,  0.5f,
             0.5f, -0.5f,  0.5f,
            -0.5f, -0.5f,  0.5f,
            -0.5f, -0.5f, -0.5f
        },

        {
            -0.5f,  0.5f, -0.5f,
             0.5f,  0.5f, -0.5f,
             0.5f,  0.5f,  0.5f,
             0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f,  0.5f,
            -0.5f,  0.5f, -0.5f
        }
    };
    // @formatter:on

    // @formatter:off
    /**
     * Face Index:
     * <ul>
     *  <li>0 - Left</li>
     *  <li>1 - Right</li>
     *  <li>2 - Front</li>
     *  <li>3 - Back</li>
     *  <li>4 - Bottom</li>
     *  <li>5 - Top</li>
     * </ul>
     */
    public static final float[][] CUBE_NORMALS = {
        {
            0.0f,  0.0f, -1.0f,
            0.0f,  0.0f, -1.0f,
            0.0f,  0.0f, -1.0f,
            0.0f,  0.0f, -1.0f,
            0.0f,  0.0f, -1.0f,
            0.0f,  0.0f, -1.0f,
        },

        {
            0.0f,  0.0f,  1.0f,
            0.0f,  0.0f,  1.0f,
            0.0f,  0.0f,  1.0f,
            0.0f,  0.0f,  1.0f,
            0.0f,  0.0f,  1.0f,
            0.0f,  0.0f,  1.0f,
        },

        {
            -1.0f,  0.0f,  0.0f,
            -1.0f,  0.0f,  0.0f,
            -1.0f,  0.0f,  0.0f,
            -1.0f,  0.0f,  0.0f,
            -1.0f,  0.0f,  0.0f,
            -1.0f,  0.0f,  0.0f
        },

        {
            1.0f,  0.0f,  0.0f,
            1.0f,  0.0f,  0.0f,
            1.0f,  0.0f,  0.0f,
            1.0f,  0.0f,  0.0f,
            1.0f,  0.0f,  0.0f,
            1.0f,  0.0f,  0.0f
        },

        {
            0.0f, -1.0f,  0.0f,
            0.0f, -1.0f,  0.0f,
            0.0f, -1.0f,  0.0f,
            0.0f, -1.0f,  0.0f,
            0.0f, -1.0f,  0.0f,
            0.0f, -1.0f,  0.0f
        },

        {
            0.0f,  1.0f,  0.0f,
            0.0f,  1.0f,  0.0f,
            0.0f,  1.0f,  0.0f,
            0.0f,  1.0f,  0.0f,
            0.0f,  1.0f,  0.0f,
            0.0f,  1.0f,  0.0f
        },
    };
    // @formatter:on

    // @formatter:off
    public final float[] CUBE_VERTICES = {
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
    public static final float[] GRASS_CUBE_VERTICES = {
        // Positions            Texture Coords       Normals 
        -0.5f, -0.5f, -0.5f,    0.5f, 0.5f,     0.0f,  0.0f, -1.0f, // left
         0.5f, -0.5f, -0.5f,    1.0f, 0.5f,     0.0f,  0.0f, -1.0f,
         0.5f,  0.5f, -0.5f,    1.0f, 1.0f,     0.0f,  0.0f, -1.0f,
         0.5f,  0.5f, -0.5f,    1.0f, 1.0f,     0.0f,  0.0f, -1.0f,
        -0.5f,  0.5f, -0.5f,    0.5f, 1.0f,     0.0f,  0.0f, -1.0f,
        -0.5f, -0.5f, -0.5f,    0.5f, 0.5f,     0.0f,  0.0f, -1.0f,

        -0.5f, -0.5f,  0.5f,    0.5f, 0.5f,     0.0f,  0.0f, 1.0f, // right
        0.5f, -0.5f,  0.5f,    1.0f, 0.5f,     0.0f,  0.0f, 1.0f,
        0.5f,  0.5f,  0.5f,    1.0f, 1.0f,     0.0f,  0.0f, 1.0f,
        0.5f,  0.5f,  0.5f,    1.0f, 1.0f,     0.0f,  0.0f, 1.0f,
        -0.5f,  0.5f,  0.5f,    0.5f, 1.0f,     0.0f,  0.0f, 1.0f,
        -0.5f, -0.5f,  0.5f,    0.5f, 0.5f,     0.0f,  0.0f, 1.0f,

        -0.5f,  0.5f,  0.5f,    1.0f, 1.0f,     -1.0f,  0.0f,  0.0f, // front
        -0.5f,  0.5f, -0.5f,    0.5f, 1.0f,     -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f, -0.5f,    0.5f, 0.5f,     -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f, -0.5f,    0.5f, 0.5f,     -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f,  0.5f,    1.0f, 0.5f,     -1.0f,  0.0f,  0.0f,
        -0.5f,  0.5f,  0.5f,    1.0f, 1.0f,     -1.0f,  0.0f,  0.0f,

         0.5f,  0.5f,  0.5f,    1.0f, 1.0f,     1.0f,  0.0f,  0.0f, // back
         0.5f,  0.5f, -0.5f,    0.5f, 1.0f,     1.0f,  0.0f,  0.0f,
         0.5f, -0.5f, -0.5f,    0.5f, 0.5f,     1.0f,  0.0f,  0.0f,
         0.5f, -0.5f, -0.5f,    0.5f, 0.5f,     1.0f,  0.0f,  0.0f,
         0.5f, -0.5f,  0.5f,    1.0f, 0.5f,     1.0f,  0.0f,  0.0f,
         0.5f,  0.5f,  0.5f,    1.0f, 1.0f,     1.0f,  0.0f,  0.0f,

        -0.5f, -0.5f, -0.5f,    0.0f, 0.5f,     0.0f, -1.0f,  0.0f, // bottom
         0.5f, -0.5f, -0.5f,    0.5f, 0.5f,     0.0f, -1.0f,  0.0f,
         0.5f, -0.5f,  0.5f,    0.5f, 0.0f,     0.0f, -1.0f,  0.0f,
         0.5f, -0.5f,  0.5f,    0.5f, 0.0f,     0.0f, -1.0f,  0.0f,
        -0.5f, -0.5f,  0.5f,    0.0f, 0.0f,     0.0f, -1.0f,  0.0f,
        -0.5f, -0.5f, -0.5f,    0.0f, 0.5f,     0.0f, -1.0f,  0.0f,

        -0.5f,  0.5f, -0.5f,    0.0f, 1.0f,     0.0f,  1.0f,  0.0f, // top
         0.5f,  0.5f, -0.5f,    0.5f, 1.0f,     0.0f,  1.0f,  0.0f,
         0.5f,  0.5f,  0.5f,    0.5f, 0.5f,     0.0f,  1.0f,  0.0f,
         0.5f,  0.5f,  0.5f,    0.5f, 0.5f,     0.0f,  1.0f,  0.0f,
        -0.5f,  0.5f,  0.5f,    0.0f, 0.5f,     0.0f,  1.0f,  0.0f,
        -0.5f,  0.5f, -0.5f,    0.0f, 1.0f,     0.0f,  1.0f,  0.0f
    };
    // @formatter:on

    // @formatter:off
    public static final float[] SOIL_CUBE_VERTICES = {
        // Positions          Texture Coords          Normals
        -0.5f, -0.5f, -0.5f,    0.0f, 0.0f,     0.0f,  0.0f, -1.0f, // left
         0.5f, -0.5f, -0.5f,    0.5f, 0.0f,     0.0f,  0.0f, -1.0f,
         0.5f,  0.5f, -0.5f,    0.5f, 0.5f,     0.0f,  0.0f, -1.0f,
         0.5f,  0.5f, -0.5f,    0.5f, 0.5f,     0.0f,  0.0f, -1.0f,
        -0.5f,  0.5f, -0.5f,    0.0f, 0.5f,     0.0f,  0.0f, -1.0f,
        -0.5f, -0.5f, -0.5f,    0.0f, 0.0f,     0.0f,  0.0f, -1.0f,

        -0.5f, -0.5f,  0.5f,    0.0f, 0.0f,     0.0f,  0.0f, 1.0f, // right
         0.5f, -0.5f,  0.5f,    0.5f, 0.0f,     0.0f,  0.0f, 1.0f,
         0.5f,  0.5f,  0.5f,    0.5f, 0.5f,     0.0f,  0.0f, 1.0f,
         0.5f,  0.5f,  0.5f,    0.5f, 0.5f,     0.0f,  0.0f, 1.0f,
        -0.5f,  0.5f,  0.5f,    0.0f, 0.5f,     0.0f,  0.0f, 1.0f,
        -0.5f, -0.5f,  0.5f,    0.0f, 0.0f,     0.0f,  0.0f, 1.0f,

        -0.5f,  0.5f,  0.5f,    0.5f, 0.0f,     -1.0f,  0.0f,  0.0f, // front
        -0.5f,  0.5f, -0.5f,    0.5f, 0.5f,     -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f, -0.5f,    0.0f, 0.5f,     -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f, -0.5f,    0.0f, 0.5f,     -1.0f,  0.0f,  0.0f,
        -0.5f, -0.5f,  0.5f,    0.0f, 0.0f,     -1.0f,  0.0f,  0.0f,
        -0.5f,  0.5f,  0.5f,    0.5f, 0.0f,     -1.0f,  0.0f,  0.0f,

         0.5f,  0.5f,  0.5f,    0.5f, 0.0f,     1.0f,  0.0f,  0.0f, // back
         0.5f,  0.5f, -0.5f,    0.5f, 0.5f,     1.0f,  0.0f,  0.0f,
         0.5f, -0.5f, -0.5f,    0.0f, 0.5f,     1.0f,  0.0f,  0.0f,
         0.5f, -0.5f, -0.5f,    0.0f, 0.5f,     1.0f,  0.0f,  0.0f,
         0.5f, -0.5f,  0.5f,    0.0f, 0.0f,     1.0f,  0.0f,  0.0f,
         0.5f,  0.5f,  0.5f,    0.5f, 0.0f,     1.0f,  0.0f,  0.0f,

        -0.5f, -0.5f, -0.5f,    0.0f, 0.5f,     0.0f, -1.0f,  0.0f, // bottom
         0.5f, -0.5f, -0.5f,    0.5f, 0.5f,     0.0f, -1.0f,  0.0f,
         0.5f, -0.5f,  0.5f,    0.5f, 0.0f,     0.0f, -1.0f,  0.0f,
         0.5f, -0.5f,  0.5f,    0.5f, 0.0f,     0.0f, -1.0f,  0.0f,
        -0.5f, -0.5f,  0.5f,    0.0f, 0.0f,     0.0f, -1.0f,  0.0f,
        -0.5f, -0.5f, -0.5f,    0.0f, 0.5f,     0.0f, -1.0f,  0.0f,

        -0.5f,  0.5f, -0.5f,    0.0f, 0.5f,     0.0f,  1.0f,  0.0f, // top
         0.5f,  0.5f, -0.5f,    0.5f, 0.5f,     0.0f,  1.0f,  0.0f,
         0.5f,  0.5f,  0.5f,    0.5f, 0.0f,     0.0f,  1.0f,  0.0f,
         0.5f,  0.5f,  0.5f,    0.5f, 0.0f,     0.0f,  1.0f,  0.0f,
        -0.5f,  0.5f,  0.5f,    0.0f, 0.0f,     0.0f,  1.0f,  0.0f,
        -0.5f,  0.5f, -0.5f,    0.0f, 0.5f,     0.0f,  1.0f,  0.0f
    };
    // @formatter:on

    // @formatter:off
    public static final float[] STONE_CUBE_VERTICES = {
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
    public final float[] LIGHT_CUBE_VERTICES = {
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
    public final float[] COLORED_CUBE_VERTICES = {
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
