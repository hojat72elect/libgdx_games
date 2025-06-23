package com.salvai.snake.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.salvai.snake.utils.WorldUtils;


public abstract class GameObject extends Image {

    public Vector2 worldPosition;
    public Vector2 screenPosition;
    public WorldUtils worldUtils;

    public GameObject(Vector2 worldPosition, Texture texture, WorldUtils worldUtils) {
        super(texture);
        this.worldUtils = worldUtils;
        this.worldPosition = worldPosition;
        screenPosition = worldUtils.worldToScreen(worldPosition);
        setBounds(screenPosition.x, screenPosition.y, worldUtils.blockSize, worldUtils.blockSize);
    }

}


