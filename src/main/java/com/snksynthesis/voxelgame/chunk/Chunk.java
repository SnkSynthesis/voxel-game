package com.snksynthesis.voxelgame.chunk;

import java.util.List;

import com.snksynthesis.voxelgame.block.Block;
import com.snksynthesis.voxelgame.block.BlockType;
import com.snksynthesis.voxelgame.gfx.Mesh;
import com.snksynthesis.voxelgame.gfx.Shader;
import com.snksynthesis.voxelgame.gfx.Texture;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.SimplexNoise;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import java.util.Random;

import static org.lwjgl.opengl.GL33.*;

public class Chunk {

    public final int WIDTH = 100;

    private float x;
    private float z;

    private Mesh mesh;
    private Matrix4f model;
    private FloatBuffer allocatedMem;
    private Texture tex;
    private List<Float> vertices;
    private int blockCount;
   
    public Chunk() {
        model = new Matrix4f();
        allocatedMem = MemoryUtil.memAllocFloat(16);
        tex = Texture.loadRGBA("textures/soil+grass+stone.png");
        vertices = new ArrayList<>();
        blockCount = 0;
        new Random();
    }

    public void draw(Shader shader, MemoryStack stack) {
        if (mesh != null) {
            int modelLoc = glGetUniformLocation(shader.getProgramId(), "model");
            glUniformMatrix4fv(modelLoc, false, model.get(allocatedMem));
            tex.bind();
            // blockCount * 6 faces * 2 triangles * 8 vertices
            mesh.draw(blockCount * 6 * 2 * 8);
            tex.unbind();
        }
    }

    public void destroy() {
        MemoryUtil.memFree(allocatedMem);
        if (mesh != null) {
            mesh.destroy();
        }
    }

    private void genPillar(float x, float z, float height) {
        for (int y = 0; y < height; y++) {
            BlockType type;
            if (y < height * 0.5) {
                type = BlockType.STONE;
            } else if (y < height * 0.9) {
                type = BlockType.SOIL;
            } else {
                type = BlockType.GRASS;
            } 
            addBlock(x, y, z, type);
        }
    }

    public void addBlock(float x, float y, float z, BlockType type) {
        blockCount++;
        float[] baseVertices;
        switch (type) {
            case GRASS:
                baseVertices = Block.GRASS_CUBE_VERTICES;
                break;
            case SOIL:
                baseVertices = Block.SOIL_CUBE_VERTICES;
                break;
            case STONE:
                baseVertices = Block.STONE_CUBE_VERTICES;
                break;
            default:
                baseVertices = Block.STONE_CUBE_VERTICES;
                break;
        }
        
        for (int i = 0; i < baseVertices.length; i += 8) {
            vertices.add(baseVertices[i + 0] + x);
            vertices.add(baseVertices[i + 1] + y);
            vertices.add(baseVertices[i + 2] + z);
            vertices.add(baseVertices[i + 3]);
            vertices.add(baseVertices[i + 4]);
            vertices.add(baseVertices[i + 5]);
            vertices.add(baseVertices[i + 6]);
            vertices.add(baseVertices[i + 7]);
        }
    }

    private float ridgeNoise(float nx, float nz) {
        return 2f * (0.5f - (float) Math.abs(0.5 - SimplexNoise.noise(nx, nz)));
    }

    public void genWorld() {
        if (z < WIDTH) {
            while (x < WIDTH) {
                float nx = x / 200 + 0.5f;
                float nz = z / 200 + 0.5f;

                float height = ridgeNoise(nx * 4.77f, nz * 3.77f) 
                    + ridgeNoise(nx * 2.77f, nz * 1.77f) 
                    + ridgeNoise(nx * 0.5f, nz * 1.3f);

                height = (float) Math.pow(height, 2.01);
                height += 0.5;
                height *= 5;
                genPillar(x, z, height);
                x++;
            }
            x = 0;
            z++;
        } else {
            if (mesh == null) {
                var verticesArr = new float[vertices.size()];
                for (int i = 0; i < vertices.size(); i++) {
                    verticesArr[i] = vertices.get(i).floatValue();
                }
        
                mesh = new Mesh(verticesArr);
            }
        } 
    }
}
