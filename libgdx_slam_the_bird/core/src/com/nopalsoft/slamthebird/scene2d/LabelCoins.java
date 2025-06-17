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
                keyFrame = Assets.num0Chico;
            } else if (character == '1') {
                keyFrame = Assets.num1Chico;
                charWidth = 11f;
            } else if (character == '2') {
                keyFrame = Assets.num2Chico;
            } else if (character == '3') {
                keyFrame = Assets.num3Chico;
            } else if (character == '4') {
                keyFrame = Assets.num4Chico;
            } else if (character == '5') {
                keyFrame = Assets.num5Chico;
            } else if (character == '6') {
                keyFrame = Assets.num6Chico;
            } else if (character == '7') {
                keyFrame = Assets.num7Chico;
            } else if (character == '8') {
                keyFrame = Assets.num8Chico;
            } else {// 9
                keyFrame = Assets.num9Chico;
            }
            textWidth += charWidth;
            batch.draw(keyFrame, x - textWidth, y, charWidth, 32);
        }
    }
}
