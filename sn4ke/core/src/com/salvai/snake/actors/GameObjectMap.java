package com.salvai.snake.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.salvai.snake.actors.snake.Snake;
import com.salvai.snake.actors.snake.SnakeBody;
import com.salvai.snake.utils.Constants;
import com.salvai.snake.utils.WorldUtils;

public class GameObjectMap {

    private final int[][] map;
    private final Array<Vector2> freePositions;

    public GameObjectMap(Array<Block> blocks, WorldUtils worldUtils) {
        //because world starts from 0 to screenwidth/height
        map = new int[worldUtils.worldWidth - Constants.PLAY_HEIGHT_FACTOR_X][worldUtils.playableWorldHeigth - 1];
        freePositions = new Array<Vector2>();
        for (Block block : blocks)
            map[(int) block.worldPosition.x - 1][(int) block.worldPosition.y - 1] = 1;
    }

    public Vector2 getFreePositions(Snake snake, Apple apple) {
        map[(int) snake.snakeHead.worldPosition.x - 1][(int) snake.snakeHead.worldPosition.y - 1] = 2;

        for (SnakeBody snakeBody : snake.snakeBodies)
            map[(int) snakeBody.worldPosition.x - 1][(int) snakeBody.worldPosition.y - 1] = 2;

        if (apple != null)
            map[(int) apple.worldPosition.x - 1][(int) apple.worldPosition.y - 1] = 2;
        return getFreePosition();
    }

    private Vector2 getFreePosition() {
        freePositions.clear();
        for (int i = 0; i < map.length; i++)
            for (int j = 0; j < map[i].length; j++)
                if (map[i][j] == 0)
                    freePositions.add(new Vector2(i + 1, j + 1)); //+1 because of border
                else if (map[i][j] == 2)
                    map[i][j] = 0;
        return freePositions.random();
    }
}
