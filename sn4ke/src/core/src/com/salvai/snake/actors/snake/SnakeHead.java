package com.salvai.snake.actors.snake;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.salvai.snake.actors.MovingGameObject;
import com.salvai.snake.enums.MovingDirection;
import com.salvai.snake.utils.WorldUtils;

public class SnakeHead extends MovingGameObject {

    SnakeHead(Texture baseTexture, Color color, WorldUtils worldUtils) {
        super(new Vector2(worldUtils.worldWidthCenter, worldUtils.worldHeightCenter), baseTexture, MovingDirection.NONE, worldUtils, true); //using baseTexture as normal one
        setColor(color);
    }

    SnakeHead(Texture baseTexture, int startX, int startY, Color color, WorldUtils worldUtils) {
        super(new Vector2(startX, startY), baseTexture, MovingDirection.NONE, worldUtils, true); //using baseTexture as normal one
        setColor(color);
    }

}
