package com.snksynthesis.voxelgame.chunk;

import java.util.List;

import com.snksynthesis.voxelgame.block.Block;
import com.snksynthesis.voxelgame.block.BlockFace;
import com.snksynthesis.voxelgame.block.BlockType;
import com.snksynthesis.voxelgame.gfx.Mesh;
import com.snksynthesis.voxelgame.gfx.Shader;
import com.snksynthesis.voxelgame.texture.Texture;
import com.snksynthesis.voxelgame.texture.TextureAtlas;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.SimplexNoise;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import java.util.Random;

import static org.lwjgl.opengl.GL33.*;

public class Chunk {

    public final int WIDTH = 200;

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
        tex = Texture.loadRGBA("textures/atlas.png");
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
            while (true) {
                if (height < 3 && y < height) {
                    type = BlockType.SAND;
                    break;
                } else if (y < height * 0.5) {
                    type = BlockType.STONE;
                    break;
                } else if (y < height * 0.95) {
                    type = BlockType.SOIL;
                    break;
                } else {
                    type = BlockType.GRASS;
                    break;
                }
            }
            addBlock(x, y, z, type);
        }
    }

    private void addFace(BlockFace face, float x, float y, float z, BlockType type) {
        float[] texCoords = TextureAtlas.getTexCoords(type, face);

        int i = 0;
        int j = 0;
        do {
            // Positions
            vertices.add(Block.CUBE_POSITIONS[face.getIndex()][i + 0] + x);
            vertices.add(Block.CUBE_POSITIONS[face.getIndex()][i + 1] + y);
            vertices.add(Block.CUBE_POSITIONS[face.getIndex()][i + 2] + z);

            // Texture Coordinates
            vertices.add(texCoords[j + 0]);
            vertices.add(texCoords[j + 1]);

            i += 3;
            j += 2;

        } while (i < Block.CUBE_POSITIONS[face.getIndex()].length);
    }

    public void addBlock(float x, float y, float z, BlockType type) {
        blockCount++;
        addFace(BlockFace.TOP, x, y, z, type);
        addFace(BlockFace.BOTTOM, x, y, z, type);
        addFace(BlockFace.LEFT, x, y, z, type);
        addFace(BlockFace.RIGHT, x, y, z, type);
        addFace(BlockFace.FRONT, x, y, z, type);
        addFace(BlockFace.BACK, x, y, z, type);
    }

    private float ridgeNoise(float nx, float nz) {
        return 2f * (0.5f - (float) Math.abs(0.5 - SimplexNoise.noise(nx, nz)));
    }

    public void genWorld() {
        if (z < WIDTH) {
            while (x < WIDTH) {
                float nx = x / 300 + 0.5f;
                float nz = z / 300 + 0.5f;

                float height = ridgeNoise(nx * 4.77f, nz * 3.77f) + ridgeNoise(nx * 2.77f, nz * 1.77f)
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