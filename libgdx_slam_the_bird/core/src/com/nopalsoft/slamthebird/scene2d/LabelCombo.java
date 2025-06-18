package com.nopalsoft.slamthebird.scene2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.game.WorldGame;

public class LabelCombo extends Actor {
    int actualCombo;

    public LabelCombo(float x, float y, int actualCombo) {
        this.actualCombo = actualCombo;
        this.setPosition(x, y);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.draw(Assets.combo, getX(), getY(), 65, 27);
        if (actualCombo >= WorldGame.COMBO_TO_START_GETTING_COINS) {
            batch.draw(Assets.coinsRegion, getX() + 20, getY() + 35, 23, 26);
        }
        drawSmallScoreLeftOrigin(batch, this.getX() + 70, this.getY() + 2,
                actualCombo);
    }

    public void drawSmallScoreLeftOrigin(Batch batch, float x, float y,
                                         int comboActual) {
        String score = String.valueOf(comboActual);

        int length = score.length();
        float charWidth;
        float textWidth = 0;
        for (int i = 0; i < length; i++) {
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
            } else {// 9
                keyFrame = Assets.smallNum9;
            }

            batch.draw(keyFrame, x + textWidth, y, charWidth, 22);
            textWidth += charWidth;
        }
    }
}
