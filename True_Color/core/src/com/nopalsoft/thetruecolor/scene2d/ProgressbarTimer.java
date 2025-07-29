package com.nopalsoft.thetruecolor.scene2d;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.nopalsoft.thetruecolor.Assets;

public class ProgressbarTimer extends Table {
    public static float WIDTH = 450;
    public static float HEIGHT = 30;

    private float totalTime;
    private float actualTime;
    Image progressBarImage;

    Color progressBarColor;

    public boolean isTimeOver;

    public ProgressbarTimer(float x, float y) {
        this.setBounds(x, y, WIDTH, HEIGHT);
        progressBarImage = new Image(Assets.barTimerDrawable);
        addActor(progressBarImage);
    }

    public void initialize(Color color, float totalTime) {
        progressBarColor = color;
        this.totalTime = totalTime;
        actualTime = 0;
        progressBarImage.setSize(0, 30);
        progressBarImage.setColor(progressBarColor);
        isTimeOver = false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (!isTimeOver) {
            actualTime += Gdx.graphics.getRawDeltaTime();
            if (actualTime >= totalTime) {
                isTimeOver = true;
                actualTime = totalTime;
            }
            progressBarImage.setWidth(WIDTH * (actualTime / totalTime));
        }
    }
}
