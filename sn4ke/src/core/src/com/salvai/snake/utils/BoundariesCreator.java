package com.salvai.snake.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.salvai.snake.actors.Block;

public class BoundariesCreator {

    private final Texture texture;
    private final WorldUtils worldUtils;

    public BoundariesCreator(Texture texture, WorldUtils worldUtils) {
        this.texture = texture;
        this.worldUtils = worldUtils;
    }

    public Array<Block> fullBoundaries() {
        Array<Block> blocks = new Array<>();
        //horizontal
        for (int i = 0; i <= worldUtils.worldWidth; i++) {
            blocks.add(new Block(new Vector2(i, 0), texture, worldUtils));
            blocks.add(new Block(new Vector2(i, worldUtils.playableWorldHeigth), texture, worldUtils));
        }
        //vertical
        for (int i = 1; i < worldUtils.playableWorldHeigth; i++) {
            blocks.add(new Block(new Vector2(0, i), texture, worldUtils));
            blocks.add(new Block(new Vector2(worldUtils.worldWidth, i), texture, worldUtils));
        }
        return blocks;
    }
}
