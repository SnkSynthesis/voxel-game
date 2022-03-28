package com.snksynthesis.voxelgame.block;

public enum BlockFace {
    LEFT(0), RIGHT(1), FRONT(2), BACK(3), BOTTOM(4), TOP(5);
    
    private final int index;

    private BlockFace(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
