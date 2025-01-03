package com.nopalsoft.zombiekiller.scene2d;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.zombiekiller.Assets;
import com.nopalsoft.zombiekiller.screens.Screens;

public class VentanaWarning extends Ventana {

	public VentanaWarning(Screens currentScreen, String text) {
		super(currentScreen, 350, 200, 150, Assets.backgroundSmallWindow);
		setCloseButton(305, 155, 45);

		Label lbShop = new Label(text, Assets.labelStyleChico);
		lbShop.setFontScale(1);
		lbShop.setWrap(true);
		lbShop.setAlignment(Align.center);
		lbShop.setWidth(getWidth() - 20);

		lbShop.setPosition(getWidth() / 2f - lbShop.getWidth() / 2f, 90);
		addActor(lbShop);

	}

}
