package com.salvai.snake.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.salvai.snake.utils.WorldUtils;

public class Block extends GameObject {
    public Block(Vector2 worldPosition, Texture texture, WorldUtils worldUtils) {
        super(worldPosition, texture, worldUtils);
    }

}
