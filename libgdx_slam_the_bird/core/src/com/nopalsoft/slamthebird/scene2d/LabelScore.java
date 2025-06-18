package com.nopalsoft.slamthebird.scene2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nopalsoft.slamthebird.Assets;

public class LabelScore extends Actor {
    int score;

    public LabelScore(float x, float y, int score) {
        this.score = score;
        this.setPosition(x, y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawLargeNumberCenteredX(batch, this.getX(), this.getY(), score);
    }

    public void drawLargeNumberCenteredX(Batch batcher, float x, float y, int newScore) {
        String score = String.valueOf(newScore);

        int len = score.length();
        float charWidth = 42;
        float textWidth = len * charWidth;
        for (int i = 0; i < len; i++) {
            AtlasRegion keyFrame;

            char character = score.charAt(i);

            if (character == '0') {
                keyFrame = Assets.largeNum0;
            } else if (character == '1') {
                keyFrame = Assets.largeNum1;
            } else if (character == '2') {
                keyFrame = Assets.largeNum2;
            } else if (character == '3') {
                keyFrame = Assets.largeNum3;
            } else if (character == '4') {
                keyFrame = Assets.largeNum4;
            } else if (character == '5') {
                keyFrame = Assets.largeNum5;
            } else if (character == '6') {
                keyFrame = Assets.largeNum6;
            } else if (character == '7') {
                keyFrame = Assets.largeNum7;
            } else if (character == '8') {
                keyFrame = Assets.largeNum8;
            } else {
                keyFrame = Assets.largeNum9;
            }
            batcher.draw(keyFrame, x + ((charWidth - 1f) * i) - textWidth / 2f, y, charWidth, 64);
        }
    }
}
