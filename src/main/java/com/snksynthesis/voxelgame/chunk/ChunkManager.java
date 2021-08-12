package com.snksynthesis.voxelgame.chunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.snksynthesis.voxelgame.Entity;
import com.snksynthesis.voxelgame.gfx.Shader;
import com.snksynthesis.voxelgame.gfx.Window;

import org.joml.Vector3f;
import org.joml.Vector2i;
import org.lwjgl.system.MemoryStack;

public class ChunkManager implements Entity {
    private Map<String, Chunk> chunkMap;
    private ArrayList<Chunk> visibleChunks;
    private Vector2i camPos;

    private final int CHUNK_RENDER_DIST = 5;

    public ChunkManager() {
        camPos = new Vector2i();
        visibleChunks = new ArrayList<>();
        chunkMap = new HashMap<>();
    }

    @Override
    public void draw(Shader shader, MemoryStack stack) {
        for (Chunk chunk : visibleChunks) {
            chunk.draw(shader, stack);
        }
    }

    @Override
    public void update(Window window) {
        for (Chunk chunk : visibleChunks) {
            chunk.update(window);
        }
    }

    @Override
    public void destroy() {
        for (Chunk chunk : chunkMap.values()) {
            chunk.destroy();
        }
    }

    private Chunk getChunk(int x, int y) {
        return chunkMap.get(String.format("%d; %d", x, y));
    }

    private void putChunk(int x, int y, Chunk chunk) {
        chunkMap.put(String.format("%d; %d", x, y), chunk);
    }

    private void setVisibleChunks() {
        visibleChunks.clear();
        for (int y = camPos.y - 1; y < camPos.y + CHUNK_RENDER_DIST; y++) {
            for (int x = camPos.x - 1; x < camPos.x + CHUNK_RENDER_DIST; x++) {
                if (getChunk(x, y) == null) {
                    putChunk(x, y, new Chunk(x * Chunk.WIDTH, y * Chunk.WIDTH));
                }
                visibleChunks.add(getChunk(x, y));
            }
        }
    }

    public void setCamPos(Vector3f camPos) {
        // camPos.z is this.camPos.y as this is 3d -> 2d
        this.camPos.set((int) (camPos.x / Chunk.WIDTH), (int) (camPos.z / Chunk.WIDTH));
        setVisibleChunks();
    }
}
