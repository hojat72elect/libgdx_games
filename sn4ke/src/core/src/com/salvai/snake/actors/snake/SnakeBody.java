package com.salvai.snake.actors.snake;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.salvai.snake.actors.MovingGameObject;
import com.salvai.snake.enums.MovingDirection;
import com.salvai.snake.utils.WorldUtils;

public class SnakeBody extends MovingGameObject {


    public SnakeBody(Vector2 worldPosition, Texture texture, MovingDirection direction, Color color, WorldUtils worldUtils) {
        super(worldPosition, texture, direction, worldUtils, false);
        setColor(color);
    }


}
