package com.snksynthesis.voxelgame;

import org.joml.SimplexNoise;

public class Noise {
    public static float getNoiseHeight(float x, float z) {
        float nx = x / 200 + 0.5f;
        float nz = z / 200 + 0.5f;
        return ridgeNoise(nx * 4.77f, nz * 3.77f) + ridgeNoise(nx * 2.77f, nz * 1.77f)
                + ridgeNoise(nx * 0.5f, nz * 1.3f);
    }

    public static float ridgeNoise(float nx, float nz) {
        return 2f * (0.5f - (float) Math.abs(0.5 - SimplexNoise.noise(nx, nz)));
    }
}
