package com.snksynthesis.voxelgame.texture;

import com.snksynthesis.voxelgame.block.BlockFace;
import com.snksynthesis.voxelgame.block.BlockType;

import org.joml.Vector2f;

public class TextureAtlas {

    private static final float IMG_WIDTH_PX = 128.0f;
    private static final float TEX_WIDTH_PX = 32.0f;
    private static final float TEX_WIDTH = TEX_WIDTH_PX / IMG_WIDTH_PX;

    /**
     * @param type the {@link BlockType}
     * @param face which face of the block {@link BlockFace}
     * @return the texture coordinates based on <code>type</code> and
     *         <code>face</code> parameters
     */
    public static float[] getTexCoords(BlockType type, BlockFace face) {
        switch (type) {
            case GRASS:
                switch (face) {
                    case TOP:
                        return TextureAtlas.getTexCoordsByRowCol(face, 1, 0);

                    case BOTTOM:
                        return TextureAtlas.getTexCoordsByRowCol(face, 0, 0);

                    case LEFT:
                    case RIGHT:
                    case FRONT:
                    case BACK:
                    default:
                        return TextureAtlas.getTexCoordsByRowCol(face, 1, 1);
                }
            case STONE:
                return TextureAtlas.getTexCoordsByRowCol(face, 0, 1);
            
            case SAND:
                return TextureAtlas.getTexCoordsByRowCol(face, 2, 0);

            case WATER:
                return TextureAtlas.getTexCoordsByRowCol(face, 2, 1);

            case SOIL:
            default:
                return TextureAtlas.getTexCoordsByRowCol(face, 0, 0);
        }
    }

    /**
     * @param row 0-based starts from bottom right corner of image
     * @param col 0-based starts from bottom right corner of image 
     */
    private static float[] getTexCoordsByRowCol(BlockFace face, int row, int col) {
        float offsetX = TEX_WIDTH * col;
        float offsetY = TEX_WIDTH * row;
        return getTexCoordsRaw(face, new Vector2f(0.0f + offsetX, TEX_WIDTH + offsetY),
                new Vector2f(TEX_WIDTH + offsetX, TEX_WIDTH + offsetY), new Vector2f(0.0f + offsetX, 0.0f + offsetY),
                new Vector2f(TEX_WIDTH + offsetX, 0.0f + offsetY));
    }

    private static float[] getTexCoordsRaw(BlockFace face, Vector2f topLeft, Vector2f topRight, Vector2f bottomLeft,
            Vector2f bottomRight) {
        switch (face) {
            case TOP:
            case BOTTOM:
                return new float[] { topLeft.x, topLeft.y, topRight.x, topRight.y, bottomRight.x, bottomRight.y,
                        bottomRight.x, bottomRight.y, bottomLeft.x, bottomLeft.y, topLeft.x, topLeft.y };
            case LEFT:
            case RIGHT:
                return new float[] { bottomLeft.x, bottomLeft.y, bottomRight.x, bottomRight.y, topRight.x, topRight.y,
                        topRight.x, topRight.y, topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y, };
            case FRONT:
            case BACK:
                return new float[] { topRight.x, topRight.y, topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y,
                        bottomLeft.x, bottomLeft.y, bottomRight.x, bottomRight.y, topRight.x, topRight.y, };
            default:
                return new float[] {};
        }
    }
}
