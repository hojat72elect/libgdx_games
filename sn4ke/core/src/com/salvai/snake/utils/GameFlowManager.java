package com.salvai.snake.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.salvai.snake.actors.Apple;
import com.salvai.snake.actors.Block;
import com.salvai.snake.actors.GameObjectMap;
import com.salvai.snake.actors.snake.Snake;
import com.salvai.snake.actors.snake.SnakeBody;
import com.salvai.snake.enums.MovingDirection;
import com.salvai.snake.screens.GameScreen;

public class GameFlowManager {
    private final GameScreen gameScreen;
    private final GameObjectMap gameObjectMap;
    public Snake snake;
    public Array<Block> baseBlocks;
    public Array<Block> blocks;
    public Apple apple;


    public GameFlowManager(GameScreen screen) {
        this.gameScreen = screen;
        this.gameScreen.game.savePreferences();

        Texture blockTexture = gameScreen.game.assetsManager.manager.get(Constants.BLOCK_IMAGE_NAME, Texture.class);

        BoundariesCreator boundariesCreator = new BoundariesCreator(blockTexture, gameScreen.game.worldUtils);

        baseBlocks = boundariesCreator.fullBoundaries();
        blocks = new Array<>();

        //load blocks
        blocks = this.gameScreen.game.getCurrentLevel().getBlocks(blockTexture, gameScreen.game.worldUtils);

        //Load blocks first other wise start X is aways -1. See getBocks method
        if (gameScreen.game.firstTimeOpen)
            snake = new Snake(blockTexture, 8, 19, gameScreen.game.selectedColor, gameScreen.game.worldUtils);
        else if (gameScreen.game.getCurrentLevel().snakeStartX > 0)
            snake = new Snake(blockTexture, gameScreen.game.getCurrentLevel().snakeStartX, gameScreen.game.getCurrentLevel().snakeStartY, gameScreen.game.selectedColor, gameScreen.game.worldUtils);
        else
            snake = new Snake(blockTexture, gameScreen.game.selectedColor, gameScreen.game.worldUtils);

        gameObjectMap = new GameObjectMap(blocks, gameScreen.game.worldUtils);


        if (gameScreen.game.firstTimeOpen)
            apple = new Apple(new Vector2(11, 19), blockTexture, gameScreen.game.worldUtils, gameScreen.game.selectedColor);
        else
            apple = new Apple(gameObjectMap.getFreePositions(snake, null), blockTexture, gameScreen.game.worldUtils, gameScreen.game.selectedColor);
    }

    public SnakeBody update(MovingDirection userDirection) {
        SnakeBody newSnakeBody = null;

        if (userDirection != null && userDirection != snake.snakeHead.direction) {
            snake.setDirection(userDirection);
        }

        snake.moveWorldPosition();

        if (snake.checkGameOver(blocks)) {
            gameScreen.gameOver = true;
            if (gameScreen.game.vibrationOn) {
                Gdx.input.vibrate(Constants.VIBRATION_DURATION_GAME_OVER);
            }
            assignHighscore();
        } else {
            snake.move(gameScreen.game.worldUtils.blockSize);
            checkScore();
            newSnakeBody = addBody();
            snake.updateBodyAndTailDirections();
        }
        return newSnakeBody;
    }

    private SnakeBody addBody() {
        SnakeBody newSnakeBody = null;
        if (snake.addBody) {
            if (gameScreen.game.vibrationOn)
                Gdx.input.vibrate(Constants.VIBRATION_DURATION);
            newSnakeBody = snake.addBody();
        }
        return newSnakeBody;
    }

    private void checkScore() {
        if (snake.eats(apple)) {
            gameScreen.game.score += Constants.POINT;
            gameScreen.updateScoreLabel();

            apple.reset(gameObjectMap.getFreePositions(snake, apple));
        }
    }

    private void assignHighscore() {
        if (gameScreen.game.highScores[gameScreen.game.level] < gameScreen.game.score) {
            gameScreen.game.highScores[gameScreen.game.level] = gameScreen.game.score;
            gameScreen.newHighscore = true;
        }
        gameScreen.game.savePreferences();
    }
}
