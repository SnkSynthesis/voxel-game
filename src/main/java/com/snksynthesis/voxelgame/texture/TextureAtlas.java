package com.snksynthesis.voxelgame.texture;

import com.snksynthesis.voxelgame.block.BlockFace;
import com.snksynthesis.voxelgame.block.BlockType;

import org.joml.Vector2f;

public class TextureAtlas {

    public static float[] getTexCoords(BlockType type, BlockFace face) {
        switch (type) {
            case GRASS:
                switch (face) {
                    case TOP:
                        return TextureAtlas.getTexCoords(face, new Vector2f(0.0f, 1.0f), new Vector2f(0.5f, 1.0f),
                                new Vector2f(0.0f, 0.5f), new Vector2f(0.5f, 0.5f));

                    case BOTTOM:
                        return TextureAtlas.getTexCoords(face, new Vector2f(0.0f, 0.5f), new Vector2f(0.5f, 0.5f),
                                new Vector2f(0.0f, 0.0f), new Vector2f(0.5f, 0.0f));

                    case LEFT:
                    case RIGHT:
                    case FRONT:
                    case BACK:
                    default:
                        return TextureAtlas.getTexCoords(face, new Vector2f(0.5f, 1.0f), new Vector2f(1.0f, 1.0f),
                                new Vector2f(0.5f, 0.5f), new Vector2f(1.0f, 0.5f));
                }
            case STONE:
                return TextureAtlas.getTexCoords(face, new Vector2f(0.5f, 0.5f), new Vector2f(1.0f, 0.5f),
                        new Vector2f(0.5f, 0.0f), new Vector2f(1.0f, 0.0f));
            case SOIL:
            default:
                return TextureAtlas.getTexCoords(face, new Vector2f(0.0f, 0.5f), new Vector2f(0.5f, 0.5f),
                        new Vector2f(0.0f, 0.0f), new Vector2f(0.5f, 0.0f));
        }
    }

    private static float[] getTexCoords(BlockFace face, Vector2f topLeft, Vector2f topRight, Vector2f bottomLeft,
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
