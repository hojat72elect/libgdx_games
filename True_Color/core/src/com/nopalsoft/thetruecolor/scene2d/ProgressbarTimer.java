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
    Image barra;

    Color oColor;

    public boolean timeIsOver;

    public ProgressbarTimer(float x, float y) {
        this.setBounds(x, y, WIDTH, HEIGHT);
        barra = new Image(Assets.barTimer);
        addActor(barra);
    }

    public void init(Color color, float totalTime) {
        oColor = color;
        this.totalTime = totalTime;
        actualTime = 0;
        barra.setSize(0, 30);
        barra.setColor(oColor);
        timeIsOver = false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (!timeIsOver) {
            actualTime += Gdx.graphics.getRawDeltaTime();
            if (actualTime >= totalTime) {
                timeIsOver = true;
                actualTime = totalTime;
            }
            barra.setWidth(WIDTH * (actualTime / totalTime));
        }
    }
}
