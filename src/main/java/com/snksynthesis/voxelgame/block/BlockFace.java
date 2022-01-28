package com.snksynthesis.voxelgame.block;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BlockFace {
    LEFT(0), RIGHT(1), FRONT(2), BACK(3), BOTTOM(4), TOP(5);

    @Getter
    private final int index;
}
