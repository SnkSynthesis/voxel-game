package com.snksynthesis.voxelgame.block;

import java.util.List;

import com.snksynthesis.voxelgame.gfx.Shader;

import java.util.ArrayList;
import org.joml.SimplexNoise;
import org.lwjgl.system.MemoryStack;

public class BlockManager {

    private List<Block> blocks;

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

    public void genWorld() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                float height = SimplexNoise.noise(i, j);
                height *= 32;
                var block = new Block(BlockType.GRASS)
                
            }
        }
    }
}
