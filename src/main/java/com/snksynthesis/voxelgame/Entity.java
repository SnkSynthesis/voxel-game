package com.snksynthesis.voxelgame;

import com.snksynthesis.voxelgame.gfx.Shader;
import com.snksynthesis.voxelgame.gfx.Window;

import org.lwjgl.system.MemoryStack;

public interface Entity {
    void draw(Shader shader, MemoryStack stack);
    void update(Window window);
    void destroy();
}
