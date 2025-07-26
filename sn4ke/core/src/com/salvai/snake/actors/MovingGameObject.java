package com.salvai.snake.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.salvai.snake.enums.MovingDirection;
import com.salvai.snake.utils.Constants;
import com.salvai.snake.utils.WorldUtils;

public abstract class MovingGameObject extends GameObject {

    public MovingDirection direction;
    private boolean move; // to wait before first move

    public MovingGameObject(Vector2 worldPosition, Texture texture, MovingDirection direction, WorldUtils worldUtils, boolean move) {
        super(worldPosition, texture, worldUtils);
        this.direction = direction;
        this.move = move;
    }


    public void setDirection(MovingDirection direction) {
        switch (direction) {
            case UP:
                if (this.direction != MovingDirection.DOWN)
                    this.direction = direction;
                break;
            case DOWN:
                if (this.direction != MovingDirection.UP)
                    this.direction = direction;
                break;
            case LEFT:
                if (this.direction != MovingDirection.RIGHT)
                    this.direction = direction;
                break;
            case RIGHT:
                if (this.direction != MovingDirection.LEFT)
                    this.direction = direction;
                break;
            default:
                this.direction = direction;
                break;
        }
    }

    public void setDirectionWhitoutCheck(MovingDirection direction) {
        this.direction = direction;
    }


    public void move(int screenBlockSize) {
        if (move)
            switch (direction) {
                case UP:
                    moveUp(screenBlockSize);
                    break;
                case DOWN:
                    moveDown(screenBlockSize);
                    break;
                case LEFT:
                    moveLeft(screenBlockSize);
                    break;
                case RIGHT:
                    moveRight(screenBlockSize);
                    break;
                default:
                    break;
            }
        else
            move = true;
        setScreenPosition();
    }


    private void moveUp(int screenBlockSize) {
        addAction(Actions.moveBy(0, screenBlockSize, Constants.SNAKE_DURATION, Constants.SNAKE_INTERPOLATION));
    }

    private void moveDown(int screenBlockSize) {
        addAction(Actions.moveBy(0, -screenBlockSize, Constants.SNAKE_DURATION, Constants.SNAKE_INTERPOLATION));
    }

    private void moveLeft(int screenBlockSize) {
        addAction(Actions.moveBy(-screenBlockSize, 0, Constants.SNAKE_DURATION, Constants.SNAKE_INTERPOLATION));
    }

    private void moveRight(int screenBlockSize) {
        addAction(Actions.moveBy(screenBlockSize, 0, Constants.SNAKE_DURATION, Constants.SNAKE_INTERPOLATION));
    }

    public void moveWorldPosition() {
        if (move)
            switch (direction) {
                case UP:
                    moveUpWorldPosition();
                    break;
                case DOWN:
                    moveDownWorldPosition();
                    break;
                case LEFT:
                    moveLeftWorldPosition();
                    break;
                case RIGHT:
                    moveRightWorldPosition();
                    break;
                default:
                    break;
            }
        setScreenPosition();
    }


    private void moveUpWorldPosition() {
        worldPosition.y += Constants.WORLD_BLOCK_SIZE;
    }

    private void moveDownWorldPosition() {
        worldPosition.y -= Constants.WORLD_BLOCK_SIZE;
    }

    private void moveLeftWorldPosition() {
        worldPosition.x -= Constants.WORLD_BLOCK_SIZE;
    }

    private void moveRightWorldPosition() {
        worldPosition.x += Constants.WORLD_BLOCK_SIZE;
    }


    public void setScreenPosition() {
        screenPosition = worldUtils.worldToScreen(worldPosition);
    }
}
