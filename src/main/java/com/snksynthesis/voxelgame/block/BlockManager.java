package com.snksynthesis.voxelgame.block;

import java.util.List;

import com.snksynthesis.voxelgame.gfx.Shader;

import java.util.ArrayList;
import org.joml.SimplexNoise;
import org.lwjgl.system.MemoryStack;

public class BlockManager {

    private List<Block> blocks;

    private final int WIDTH = 50;
    private final int LENGTH = 50;

    private float x;
    private float z;

    public BlockManager() {
        blocks = new ArrayList<>();
    }

    public void draw(Shader shader, MemoryStack stack) {
        for (Block block : blocks) {
            block.draw(shader, stack);
        }
    }

    public void destroy() {
        for (Block block : blocks) {
            block.destroy();
        }
    }

    private void genPillar(float x, float z, float height) {
        for (int i = 0; i < height; i++) {
            Block block = null;
            if (i < 3) {
                block = new Block(BlockType.STONE);
            } else if (i < 5) {
                block = new Block(BlockType.SOIL);
            } else {
                block = new Block(BlockType.GRASS);
            }
            block.getModel().translate(x, i, z);
            blocks.add(block);
        }
        for (int i = 0; i > -height; i--) {
            Block block = null;
            if (i < 3) {
                block = new Block(BlockType.STONE);
            } else if (i < 5) {
                block = new Block(BlockType.SOIL);
            } else {
                block = new Block(BlockType.GRASS);
            }
            block.getModel().translate(x, i, z);
            blocks.add(block);
        }
    }

    public void genWorld() {
        if (z < WIDTH) {
            while (x < LENGTH) {
                float nx = x / WIDTH + 0.5f;
                float nz = z / LENGTH + 0.5f;
                float height = SimplexNoise.noise(nx * 2f, nz * 2f) + 0.25f * SimplexNoise.noise(nx * 3f, nz * 3f)
                        + 0.5f * SimplexNoise.noise(nx * 4f, nz * 4f);
                height += 0.1f;
                height *= 10;
                genPillar(x, z, height);
                x++;
            }
            x = 0;
            z++;
        }
    }
}
