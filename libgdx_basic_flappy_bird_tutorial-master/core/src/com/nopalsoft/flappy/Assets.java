package com.nopalsoft.flappy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class Assets {

    public static BitmapFont font;
    private static final GlyphLayout glyphLayout = new GlyphLayout();

    public static Animation<AtlasRegion> bird;

    public static TextureRegion background;
    public static TextureRegion gameOver;
    public static TextureRegion getReady;
    public static TextureRegion tap;
    public static TextureRegion downPipe;
    public static TextureRegion upPipe;

    public static void load() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("data/atlasMap.txt"));

        background = atlas.findRegion("fondo");
        gameOver = atlas.findRegion("gameOver");
        getReady = atlas.findRegion("getReady");
        tap = atlas.findRegion("tap");
        downPipe = atlas.findRegion("tuberiaDown");
        upPipe = atlas.findRegion("tuberiaUp");

        bird = new Animation<>(.3f,
                atlas.findRegion("bird1"),
                atlas.findRegion("bird2"),
                atlas.findRegion("bird3"));

        // Use default libGDX font
        font = new BitmapFont();
        font.getData().scale(7f);
    }

    /**
     * Get the text width in order to center in the screen
     */
    public static float getTextWidth(String text) {
        glyphLayout.setText(font, text);
        return glyphLayout.width;
    }
}
