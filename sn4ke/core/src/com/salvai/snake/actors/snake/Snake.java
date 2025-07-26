package com.salvai.snake.actors.snake;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.salvai.snake.actors.Apple;
import com.salvai.snake.actors.Block;
import com.salvai.snake.enums.MovingDirection;
import com.salvai.snake.utils.Colors;
import com.salvai.snake.utils.WorldUtils;

public class Snake {

    public SnakeHead snakeHead;
    public Array<com.salvai.snake.actors.snake.SnakeBody> snakeBodies;
    public boolean addBody;
    public Texture texture;
    public Color bodyColor;
    private final Color headColor;
    private final WorldUtils worldUtils;

    public Snake(Texture texture, int styleIndex, WorldUtils worldUtils) {
        this.texture = texture;
        this.worldUtils = worldUtils;

        headColor = Colors.getHeadColor(styleIndex);
        bodyColor = Colors.getBodyColor(styleIndex);

        snakeHead = new SnakeHead(texture, headColor, worldUtils);
        snakeBodies = new Array<SnakeBody>();
        addBody = false;
        setDirection(MovingDirection.NONE);
    }

    public Snake(Texture texture, int startX, int startY, int styleIndex, WorldUtils worldUtils) {
        this.texture = texture;
        this.worldUtils = worldUtils;

        headColor = Colors.getHeadColor(styleIndex);
        bodyColor = Colors.getBodyColor(styleIndex);

        snakeHead = new SnakeHead(texture, startX, startY, headColor, worldUtils);
        snakeBodies = new Array<SnakeBody>();
        addBody = false;
        setDirection(MovingDirection.NONE);
    }

    public boolean checkGameOver(Array<Block> blocks) {
        for (int i = 3; i < snakeBodies.size; i++)
            if (snakeHead.worldPosition.equals(snakeBodies.get(i).worldPosition))
                return true;
        if (snakeHead.worldPosition.x < 1 || snakeHead.worldPosition.y < 1 || snakeHead.worldPosition.x > snakeHead.worldUtils.worldWidth - 1 || snakeHead.worldPosition.y > snakeHead.worldUtils.playableWorldHeigth - 1)
            return true;
        for (Block block : blocks)
            if (snakeHead.worldPosition.equals(block.worldPosition))
                return true;
        return false;
    }


    public void setDirection(MovingDirection direction) {
        snakeHead.setDirection(direction);
    }

    public void updateBodyAndTailDirections() {
        for (int i = snakeBodies.size - 2; i >= 0; i--)
            snakeBodies.get(i + 1).setDirectionWhitoutCheck(snakeBodies.get(i).direction);
        if (snakeBodies.size > 0)
            snakeBodies.first().setDirectionWhitoutCheck(snakeHead.direction);
    }

    public void move(int screenBlockSize) {
        snakeHead.move(screenBlockSize);
        for (SnakeBody snakeBody : snakeBodies)
            snakeBody.move(screenBlockSize);
    }

    public void moveWorldPosition() {
        snakeHead.moveWorldPosition();
        for (SnakeBody snakeBody : snakeBodies)
            snakeBody.moveWorldPosition();
    }


    public SnakeBody addBody() {
        SnakeBody newBody;
        if (snakeBodies.size > 0) {
            newBody = new SnakeBody(new Vector2(snakeBodies.peek().worldPosition), texture, snakeBodies.peek().direction, bodyColor, worldUtils);
        } else
            newBody = new SnakeBody(new Vector2(snakeHead.worldPosition), texture, snakeHead.direction, bodyColor, worldUtils);
        snakeBodies.add(newBody);
        addBody = false;
        return newBody;
    }

    public boolean eats(Apple apple) {
        if (snakeHead.worldPosition.equals(apple.worldPosition)) {
            addBody = true;
            return true;
        }
        return false;
    }

    public void hide() {
        snakeHead.setVisible(false);
        for (SnakeBody snakeBody : snakeBodies)
            snakeBody.setVisible(false);
    }

    public void show() {
        snakeHead.setVisible(true);
        for (SnakeBody snakeBody : snakeBodies)
            snakeBody.setVisible(true);
    }
}


