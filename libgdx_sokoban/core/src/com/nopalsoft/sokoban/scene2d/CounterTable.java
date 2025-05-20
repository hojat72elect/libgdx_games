package com.nopalsoft.sokoban.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.sokoban.Assets;

public class CounterTable extends Table {
    float WIDTH = 125;
    float HEIGHT = 42;

    Label displayLabel;

    public CounterTable(TextureRegionDrawable fondo, float x, float y) {

        this.setBounds(x, y, WIDTH, HEIGHT);
        setBackground(fondo);

        displayLabel = new Label("", new LabelStyle(Assets.fontRed, Color.WHITE));
        displayLabel.setFontScale(.8f);
        add(displayLabel);

        center();
        padLeft(25);
        padBottom(5);
    }

    public void updateDisplayedNumber(int newNumber) {
        displayLabel.setText(newNumber + "");
    }
}
