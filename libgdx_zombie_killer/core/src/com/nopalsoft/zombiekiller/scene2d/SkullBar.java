package com.nopalsoft.zombiekiller.scene2d;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.zombiekiller.Assets;

public class SkullBar extends Table {
	float WIDTH = 180;
	float HEIGHT = 60;

	public int numOfSkulls;
	Image[] arrSkulls;

	public SkullBar(float x, float y) {
		// this.debug();
		this.setBounds(x - WIDTH / 2f, y, WIDTH, HEIGHT);
		// setBackground(Assets.skullBarBackground, false);
		setBackground(Assets.skullBarBackground);
		arrSkulls = new Image[3];

		for (int i = 0; i < arrSkulls.length; i++) {
			arrSkulls[i] = new Image();
			add(arrSkulls[i]).size(40).pad(6);
		}

	}

	public void tryToUpdateSkulls(int actualNumSkulls) {
		if (numOfSkulls < actualNumSkulls) {
			numOfSkulls = actualNumSkulls;
			for (int i = 0; i < numOfSkulls; i++) {
				arrSkulls[i].setDrawable(new TextureRegionDrawable(Assets.itemSkull));
			}
		}

	}
}
