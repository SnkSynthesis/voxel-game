package com.snksynthesis.voxelgame.chunk;

import com.snksynthesis.voxelgame.Entity;
import com.snksynthesis.voxelgame.gfx.Shader;
import com.snksynthesis.voxelgame.gfx.Window;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class ChunkManager implements Entity {
    private final Map<String, Chunk> chunkMap;
    private final Map<Float, Chunk> visibleChunks;
    private final Vector2i camPos; // relative to chunkMap
    private final Vector3f actualCamPos;
    private final ThreadPoolExecutor executor;
    private final List<Future<Chunk>> genFutures;

    private final int CHUNK_RENDER_DIST = 5;

    public ChunkManager() {
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        genFutures = new ArrayList<>();
        camPos = new Vector2i();
        actualCamPos = new Vector3f();
        visibleChunks = new TreeMap<>();
        chunkMap = new HashMap<>();
    }

    @Override
    public void draw(Shader shader, MemoryStack stack) {
        for (Chunk chunk : visibleChunks.values()) {
            chunk.draw(shader, stack);
        }
    }

    @Override
    public void update(Window window) {
        for (Chunk chunk : visibleChunks.values()) {
            Future<Chunk> genFuture = executor.submit(() -> {
                chunk.update(window);
                return chunk;
            });
            genFutures.add(genFuture);
        }
        for (int i = 0; i < genFutures.size(); i++) {
            if (genFutures.get(i).isDone()) {
                try {
                    Chunk chunk;
                    chunk = genFutures.get(i).get();
                    chunk.genMesh();
                    genFutures.remove(genFutures.get(i));
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void destroy() {
        executor.shutdown();
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
        for (int y = camPos.y - CHUNK_RENDER_DIST; y < camPos.y + CHUNK_RENDER_DIST; y++) {
            for (int x = camPos.x - CHUNK_RENDER_DIST; x < camPos.x + CHUNK_RENDER_DIST; x++) {
                if (getChunk(x, y) == null) {
                    putChunk(x, y, new Chunk(x * Chunk.WIDTH, y * Chunk.WIDTH));
                }
                // Make distance negative to reverse sorting
                visibleChunks.put(-actualCamPos.distance(getChunk(x, y).getPos()), getChunk(x, y));
            }
        }
    }

    public void setCamPos(Vector3f camPos) {
        actualCamPos.set(camPos);
        // camPos.z is this.camPos.y as this is 3d -> 2d
        this.camPos.set((int) (camPos.x / Chunk.WIDTH), (int) (camPos.z / Chunk.WIDTH));
        setVisibleChunks();
    }
}
