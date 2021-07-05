package com.snksynthesis.voxelgame.chunk;

import java.util.List;

import com.snksynthesis.voxelgame.Entity;
import com.snksynthesis.voxelgame.Noise;
import com.snksynthesis.voxelgame.block.Block;
import com.snksynthesis.voxelgame.block.BlockFace;
import com.snksynthesis.voxelgame.block.BlockType;
import com.snksynthesis.voxelgame.gfx.Mesh;
import com.snksynthesis.voxelgame.gfx.Shader;
import com.snksynthesis.voxelgame.gfx.Window;
import com.snksynthesis.voxelgame.texture.Texture;
import com.snksynthesis.voxelgame.texture.TextureAtlas;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import static org.lwjgl.opengl.GL33.*;

public class Chunk implements Entity {

    public final int WIDTH = 200;
    public final int HEIGHT = 256;

    private float x, startX, z, startZ;

    private Mesh mesh;
    private Matrix4f model;
    private FloatBuffer allocatedMem;
    private Texture tex;
    private List<Float> vertices;
    private BlockType[][][] blocks;
    private int blockCount = 0;

    public Chunk(float startX, float startZ) {
        model = new Matrix4f();
        allocatedMem = MemoryUtil.memAllocFloat(16);
        tex = Texture.loadRGBA("textures/atlas.png");
        vertices = new ArrayList<>();
        blocks = new BlockType[WIDTH][HEIGHT][WIDTH];

        this.startX = startX;
        this.startZ = startZ;

        for (int x = 0; x < WIDTH; x++) {
            for (int z = 0; z < WIDTH; z++) {
                for (int y = 1; y <= 4; y++) {
                    blocks[x][0][z] = BlockType.SAND;
                    blocks[x][y][z] = BlockType.WATER;
                }
            }
        }
    }

    @Override
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

    @Override
    public void update(Window window) {
        genWorld();
    }

    @Override
    public void destroy() {
        MemoryUtil.memFree(allocatedMem);
        if (mesh != null) {
            mesh.destroy();
        }
    }

    private void genPillar(float x, float z, float height) {
        // `int y = 0; ...` makes everything stay in integers and not floats
        for (int y = 0; y < height; y++) {
            BlockType type;
            while (true) {
                if (height < 6 && y < height) {
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
        boolean addedVertices = false;
        do {
            // Positions
            if (type == BlockType.WATER) {
                vertices.add(Block.CUBE_POSITIONS[face.getIndex()][i + 0] + x);
                vertices.add(Block.CUBE_POSITIONS[face.getIndex()][i + 1] * 0.4f + y);
                vertices.add(Block.CUBE_POSITIONS[face.getIndex()][i + 2] + z);
                addedVertices = true;
            }

            if (addedVertices == false) {
                vertices.add(Block.CUBE_POSITIONS[face.getIndex()][i + 0] + x);
                vertices.add(Block.CUBE_POSITIONS[face.getIndex()][i + 1] + y);
                vertices.add(Block.CUBE_POSITIONS[face.getIndex()][i + 2] + z);
            }

            // Texture Coordinates
            vertices.add(texCoords[j + 0]);
            vertices.add(texCoords[j + 1]);

            i += 3;
            j += 2;

        } while (i < Block.CUBE_POSITIONS[face.getIndex()].length);
    }

    public void addBlock(float x, float y, float z, BlockType type) {
        blockCount++;
        blocks[(int) x][(int) y][(int) z] = type;
    }

    private boolean isVisibleFrom(int x, int y, int z, BlockType type) {
        try {
            if (x > blocks.length || y > blocks[x].length || z > blocks[x][y].length) {
                return true;
            } else if (x < 0 || y < 0 || z < 0) {
                return true;
            } else if (blocks[x][y][z] == BlockType.WATER && type != BlockType.WATER) {
                return true;
            }
            return blocks[x][y][z] == null;
        } catch (ArrayIndexOutOfBoundsException e) {
            return true;
        }
    }

    private List<BlockFace> getVisibleFaces(int x, int y, int z, BlockType type) {
        var faces = new ArrayList<BlockFace>();

        if (isVisibleFrom(x + 1, y, z, type)) {
            faces.add(BlockFace.BACK);
        }
        if (isVisibleFrom(x - 1, y, z, type)) {
            faces.add(BlockFace.FRONT);
        }
        if (isVisibleFrom(x, y + 1, z, type)) {
            faces.add(BlockFace.TOP);
            if (blocks[x][y][z] == BlockType.SOIL) {
                blocks[x][y][z] = BlockType.GRASS;
            }
        } else {
            if (blocks[x][y + 1][z] == BlockType.GRASS) {
                blocks[x][y][z] = BlockType.SOIL;
            }
        }
        if (isVisibleFrom(x, y - 1, z, type)) {
            faces.add(BlockFace.BOTTOM);
        }
        if (isVisibleFrom(x, y, z + 1, type)) {
            faces.add(BlockFace.RIGHT);
        }
        if (isVisibleFrom(x, y, z - 1, type)) {
            faces.add(BlockFace.LEFT);
        }

        return faces;
    }

    public void genWorld() {
        if (z < WIDTH) {
            while (x < WIDTH) {
                float height = Noise.getNoiseHeight(x, z);
                height = (float) Math.pow(height, 2.01);
                height += 0.5;
                height *= 5;
                height += 2;
                genPillar(x, z, height);
                x++;
            }
            x = 0;
            z++;
        } else {
            if (mesh == null) {
                for (int y = 0; y < HEIGHT; y++) {
                    for (int x = 0; x < WIDTH; x++) {
                        for (int z = 0; z < WIDTH; z++) {
                            if (blocks[x][y][z] != null) {
                                var visibleFaces = getVisibleFaces(x, y, z, blocks[x][y][z]);
                                for (BlockFace face : visibleFaces) {
                                    addFace(face, x + startX, y, z + startZ, blocks[x][y][z]);
                                }
                            }
                        }
                    }
                }

                var verticesArr = new float[vertices.size()];
                for (int i = 0; i < vertices.size(); i++) {
                    verticesArr[i] = vertices.get(i).floatValue();
                }

                mesh = new Mesh(verticesArr);
            }
        }
    }

    public Vector3f getStartPos() {
        return new Vector3f(startX, 0f, startZ);
    }

    public Vector3f getEndPos() {
        return new Vector3f(startX + WIDTH, 0f, startZ + WIDTH);
    }
}
