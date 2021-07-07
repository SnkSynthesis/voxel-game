package com.snksynthesis.voxelgame.chunk;

import java.util.ArrayList;
import java.util.List;

import com.snksynthesis.voxelgame.Entity;
import com.snksynthesis.voxelgame.gfx.Shader;
import com.snksynthesis.voxelgame.gfx.Window;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

public class ChunkManager implements Entity {
    private List<Chunk> chunks;
    private Vector3f camPos;

    public ChunkManager() {
        chunks = new ArrayList<>();
        camPos = new Vector3f();
        chunks.add(new Chunk(0f, 0f));
    }

    @Override
    public void draw(Shader shader, MemoryStack stack) {
        for (Chunk chunk : chunks) {
            chunk.draw(shader, stack);
        }
    }

    @Override
    public void update(Window window) {
        for (Chunk chunk : chunks) {
            if ((camPos.x < chunk.getStartPos().x || camPos.z < chunk.getStartPos().z)
                    || (camPos.x > chunk.getEndPos().x || camPos.z > chunk.getEndPos().z)) {
                System.out.println("Outside of chunk boundaries");
            }
            chunk.update(window);
        }
    }

    @Override
    public void destroy() {
        for (Chunk chunk : chunks) {
            chunk.destroy();
        }
    }

    public void setCamPos(Vector3f camPos) {
        this.camPos = camPos;
    }
}
