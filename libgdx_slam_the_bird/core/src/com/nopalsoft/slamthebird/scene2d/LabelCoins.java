package com.nopalsoft.slamthebird.scene2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nopalsoft.slamthebird.Assets;

public class LabelCoins extends Actor {
    int numCoins;

    public LabelCoins(float x, float y, int numCoins) {
        this.numCoins = numCoins;
        this.setPosition(x, y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawScoreRightAligned(batch, this.getX(), this.getY(), numCoins);
    }

    public void drawScoreRightAligned(Batch batch, float x, float y, int numCoins) {
        String score = String.valueOf(numCoins);

        int length = score.length();
        float charWidth;
        float textWidth = 0;
        for (int i = length - 1; i >= 0; i--) {
            AtlasRegion keyFrame;

            charWidth = 22;
            char character = score.charAt(i);

            if (character == '0') {
                keyFrame = Assets.smallNum0;
            } else if (character == '1') {
                keyFrame = Assets.smallNum1;
                charWidth = 11f;
            } else if (character == '2') {
                keyFrame = Assets.smallNum2;
            } else if (character == '3') {
                keyFrame = Assets.smallNum3;
            } else if (character == '4') {
                keyFrame = Assets.smallNum4;
            } else if (character == '5') {
                keyFrame = Assets.smallNum5;
            } else if (character == '6') {
                keyFrame = Assets.smallNum6;
            } else if (character == '7') {
                keyFrame = Assets.smallNum7;
            } else if (character == '8') {
                keyFrame = Assets.smallNum8;
            } else {
                keyFrame = Assets.smallNum9;
            }
            textWidth += charWidth;
            batch.draw(keyFrame, x - textWidth, y, charWidth, 32);
        }
    }
}
