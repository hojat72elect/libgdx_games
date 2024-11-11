package com.nopalsoft.sokoban.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.sokoban.Assets;

/**
 * This counter is either of the numbers we show in the top left of the game screen.
 * One of them shows the number of seconds passed, and the other one shows number of
 * steps that player has taken so far.
 */
public class Counter extends Table {
    float WIDTH = 125;
    float HEIGHT = 42;

    Label labelDisplay;

    public Counter(TextureRegionDrawable background, float x, float y) {

        this.setBounds(x, y, WIDTH, HEIGHT);
        setBackground(background);

        labelDisplay = new Label("", new LabelStyle(Assets.fontRed, Color.WHITE));
        labelDisplay.setFontScale(.8f);
        add(labelDisplay);

        center();
        padLeft(25);
        padBottom(5);
    }

    public void updateActualNum(int actualNum) {
        labelDisplay.setText(actualNum + "");
    }

}
