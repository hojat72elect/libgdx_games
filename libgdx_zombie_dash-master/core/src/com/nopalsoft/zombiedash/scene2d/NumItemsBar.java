package com.nopalsoft.zombiedash.scene2d;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.zombiedash.Assets;

public class NumItemsBar extends Table {
	float WIDTH = 100;
	float HEIGHT = 30;

	Label lbNum;
	public int actualNum;

	public NumItemsBar(AtlasRegion icon, float x, float y) {
		// this.debug();
		this.setBounds(x, y, WIDTH, HEIGHT);
		setBackground(Assets.backgroundProgressBar);
		setIcon(icon);
		lbNum = new Label("0", Assets.labelStyleChico);
		lbNum.setPosition(50, 3.5f);
		lbNum.setAlignment(Align.center);
		addActor(lbNum);

	}

	private void setIcon(AtlasRegion icon) {
		Image imgIcon = new Image(icon);
		// Both height because i want it to be a square
		imgIcon.scaleBy(-.3f);
		imgIcon.setPosition(-15, getHeight() / 2f - (imgIcon.getPrefHeight() * imgIcon.getScaleY() / 2f));
		addActor(imgIcon);

	}

	public void updateNumGems(int gems) {
		this.actualNum = gems;
		lbNum.setText(actualNum + "");
	}

}
