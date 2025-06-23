package com.nopalsoft.zombiedash.scene2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.nopalsoft.zombiedash.Assets;

public class ProgressBarUI extends Table {
    float WIDTH = 180;
    float HEIGHT = 30;
    float BAR_WIDTH = 140;
    float BAR_HEIGHT = 20;

    public float maxNum;
    public float actualNum;
    AtlasRegion bar;

    public ProgressBarUI(AtlasRegion bar, AtlasRegion icon, float maxNum, float actualNum, float x, float y) {
        // this.debug();
        this.setBounds(x, y, WIDTH, HEIGHT);
        this.maxNum = maxNum;
        this.actualNum = actualNum;

        setBackground(Assets.backgroundProgressBar);
        setIcon(icon);
        this.bar = bar;

    }

    public ProgressBarUI(AtlasRegion bar, AtlasRegion icon, float maxNum, float x, float y) {
        // this.debug();
        this.setBounds(x, y, WIDTH, HEIGHT);
        this.maxNum = maxNum;
        this.actualNum = maxNum;
        setBackground(Assets.backgroundProgressBar);
        setIcon(icon);
        this.bar = bar;

    }

    private void setIcon(AtlasRegion icon) {
        Image imgIcon = new Image(icon);
        // Both height because i want it to be a square
        imgIcon.scaleBy(-.3f);
        imgIcon.setPosition(-15, getHeight() / 2f - (imgIcon.getPrefHeight() * imgIcon.getScaleY() / 2f));
        addActor(imgIcon);

    }

    public void updateActualNum(float actualNum) {
        this.actualNum = actualNum;

        if (actualNum > maxNum)
            maxNum = actualNum;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (actualNum > 0)
            batch.draw(bar, this.getX() + 34, this.getY() + 6, BAR_WIDTH * (actualNum / maxNum), BAR_HEIGHT);
    }
}
