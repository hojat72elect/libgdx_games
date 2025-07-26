package com.salvai.snake.levels;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.salvai.snake.actors.Block;
import com.salvai.snake.utils.Constants;
import com.salvai.snake.utils.WorldUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Level {

    public int snakeStartX;
    public int snakeStartY;
    private int[][] blocksPositions;
    private boolean toReverse;

    public Level() {
        snakeStartX = -1;
        snakeStartY = -1;
        toReverse = true;
    }

    //1 block, 2 snake 0 nothing

    public Array<Block> getBlocks(Texture blockTexture, WorldUtils worldUtils, int colorIndex) {
        Array<Block> blocks = new Array<Block>();

        //reverse array other wise blocks are mirrored
        if (toReverse) {
            List<int[]> blockList = Arrays.asList(blocksPositions);
            Collections.reverse(blockList);
            blocksPositions = blockList.toArray(new int[blockList.size()][]);
            toReverse = false;
        }

        for (int i = 0; i < blocksPositions.length; i++)
            for (int j = 0; j < blocksPositions[0].length; j++)
                if (blocksPositions[i][j] == Constants.BLOCK_CELL)
                    blocks.add(new Block(new Vector2(j + 1, i + 1), blockTexture, worldUtils)); // +1 beause of border
                else if (blocksPositions[i][j] == Constants.SNAKE_HEAD_CELL) {
                    snakeStartX = j + 1;
                    snakeStartY = i + 1;
                }
        return blocks;
    }
}

