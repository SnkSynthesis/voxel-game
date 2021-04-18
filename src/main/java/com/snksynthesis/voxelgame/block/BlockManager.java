package com.snksynthesis.voxelgame.block;

import java.util.List;

import com.snksynthesis.voxelgame.gfx.Shader;

import java.util.ArrayList;
import org.joml.SimplexNoise;
import org.lwjgl.system.MemoryStack;

public class BlockManager {

    private List<Block> blocks;

    private final int WIDTH = 30;
    private final int LENGTH = 30;

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
        for (float i = 0f; i < height; i++) {
            var block = new Block(BlockType.GRASS);
            block.getModel().translate(x, i, z);
            blocks.add(block);
        }
    }

    public void genWorld() {
        if (z < WIDTH) {
            if (x < LENGTH) {
                float nx = x / WIDTH + 0.5f;
                float nz = z / LENGTH + 0.5f;
                float height = SimplexNoise.noise(nx * 1.77f, nz * 1.77f);
                height += 1;
                height *= 5;
                genPillar(x, z, height);
                x++;
            } else {
                x = 0;
                z++;
            }
        }
    }
}
