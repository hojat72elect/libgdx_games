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

    public void drawLargeNumberCenteredX(Batch batcher, float x, float y, int puntuacion) {
        String score = String.valueOf(puntuacion);

        int len = score.length();
        float charWidth = 42;
        float textWidth = len * charWidth;
        for (int i = 0; i < len; i++) {
            AtlasRegion keyFrame;

            char character = score.charAt(i);

            if (character == '0') {
                keyFrame = Assets.num0Grande;
            } else if (character == '1') {
                keyFrame = Assets.num1Grande;
            } else if (character == '2') {
                keyFrame = Assets.num2Grande;
            } else if (character == '3') {
                keyFrame = Assets.num3Grande;
            } else if (character == '4') {
                keyFrame = Assets.num4Grande;
            } else if (character == '5') {
                keyFrame = Assets.num5Grande;
            } else if (character == '6') {
                keyFrame = Assets.num6Grande;
            } else if (character == '7') {
                keyFrame = Assets.num7Grande;
            } else if (character == '8') {
                keyFrame = Assets.num8Grande;
            } else {
                keyFrame = Assets.num9Grande;
            }
            batcher.draw(keyFrame, x + ((charWidth - 1f) * i) - textWidth / 2f, y, charWidth, 64);
        }
    }
}
