package com.nopalsoft.sokoban.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.sokoban.Assets;

public class ContadorBar extends Table {
	float WIDTH = 125;
	float HEIGHT = 42;

	Label lblDisplay;

	public ContadorBar(TextureRegionDrawable fondo, float x, float y) {

		this.setBounds(x, y, WIDTH, HEIGHT);
		setBackground(fondo);

		lblDisplay = new Label("", new LabelStyle(Assets.fontRed, Color.WHITE));
		lblDisplay.setFontScale(.8f);
		add(lblDisplay);

		center();
		padLeft(25);
		padBottom(5);
	}

	public void updateActualNum(int actualNum) {
		lblDisplay.setText(actualNum + "");
	}

}
