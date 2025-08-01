package com.salvai.snake.utils;

import com.badlogic.gdx.math.Vector2;
import com.salvai.snake.enums.BlockRatio;

public class WorldUtils {

    public int blockSize;
    public BlockRatio blockRatio;

    public int worldWidth;
    public int worldHeight;
    public int worldWidthCenter;
    public int worldHeightCenter;
    public int playableWorldWidth;
    public int playableWorldHeigth;

    public WorldUtils(BlockRatio blockRatio) {
        this.blockRatio = blockRatio;
        setBlockSize(blockRatio);
    }

    public void setBlockSize(BlockRatio blockRatio) {
        this.blockRatio = blockRatio;
        switch (blockRatio) {
            case SMALL:
                blockSize = Constants.SCREEN_BLOCK_SIZE_SMALL;
                break;
            case MEDIUM:
                blockSize = Constants.SCREEN_BLOCK_SIZE_MEDIUM;
                break;
            case BIG:
                blockSize = Constants.SCREEN_BLOCK_SIZE_BIG;
                break;
        }

        worldWidth = Constants.SCREEN_WIDTH / blockSize - 1; //screen width / blocksize - 1
        worldHeight = Constants.SCREEN_HEIGHT / blockSize - 1;//screen height / blocksize - 1
        worldWidthCenter = worldWidth / 2;
        worldHeightCenter = worldHeight / 2;
        playableWorldWidth = worldWidth - (Constants.PLAY_HEIGHT_FACTOR_X * 2);

        switch (blockRatio) {
            case SMALL:
                playableWorldHeigth = worldHeight - Constants.PLAY_HEIGHT_FACTOR_Y_SMALL + 1;
                break;
            case MEDIUM:
                playableWorldHeigth = worldHeight - Constants.PLAY_HEIGHT_FACTOR_Y_MEDIUM + 1;
                break;
            case BIG:
                playableWorldHeigth = worldHeight - Constants.PLAY_HEIGHT_FACTOR_Y_BIG + 1;
                break;
        }
    }

    public Vector2 worldToScreen(Vector2 worldPosition) {
        return new Vector2(worldPosition.x * blockSize, worldPosition.y * blockSize);
    }
}
