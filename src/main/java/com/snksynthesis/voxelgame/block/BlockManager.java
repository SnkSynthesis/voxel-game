package com.snksynthesis.voxelgame.block;

import java.util.List;

import com.snksynthesis.voxelgame.gfx.Shader;

import java.util.ArrayList;
import org.joml.SimplexNoise;
import org.lwjgl.system.MemoryStack;

public class BlockManager {

    private List<Block> blocks;

    public final int WIDTH = 50;
    public final int LENGTH = 50;

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
        for (var y = (int) height; y > height - 3; y--) {
            Block block = null;
            if (y < 3) {
                block = new Block(BlockType.STONE);
            } else if (y < 5) {
                block = new Block(BlockType.SOIL);
            } else {
                block = new Block(BlockType.GRASS);
            }
            block.getModel().translate(x, y, z);
            blocks.add(block);
        }
    }

    public void addBlock(float x, float y, float z, BlockType type) {
        var block = new Block(type);
        block.getModel().translate(x, y, z);
        blocks.add(block);
    }

    public void genWorld() {
        if (z < WIDTH) {
            while (x < LENGTH) {
                float nx = x / WIDTH + 0.5f;
                float nz = z / LENGTH + 0.5f;
                float height = SimplexNoise.noise(nx * 1.77f, nz * 1.77f);
                height += 0.5f;
                height *= 10;
                genPillar(x, z, height);
                x++;
            }
            x = 0;
            z++;
        }
    }
}
