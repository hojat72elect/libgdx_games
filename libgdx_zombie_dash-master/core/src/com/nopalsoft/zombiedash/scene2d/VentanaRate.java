package com.nopalsoft.zombiedash.scene2d;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.zombiedash.Assets;
import com.nopalsoft.zombiedash.screens.Screens;

public class VentanaRate extends Ventana {

	TextButton btYes, btLater;

	public VentanaRate(Screens currentScreen) {
		super(currentScreen, 400, 310, 100, Assets.backgroundSmallWindow);

		Label lbShop = new Label(idiomas.get("support_this_game"), Assets.labelStyleGrande);
		lbShop.setFontScale(1.2f);
		lbShop.setAlignment(Align.center);
		lbShop.setPosition(getWidth() / 2f - lbShop.getWidth() / 2f, 230);
		addActor(lbShop);

		Label lbContent = new Label(idiomas.get("support_description"), Assets.labelStyleChico);
		lbContent.setWrap(true);
		lbContent.setWidth(getWidth() - 60);
		lbContent.setPosition(35, 150);
		addActor(lbContent);

		initButtons();

		Table content = new Table();
		content.setSize(375, 90);
		content.setPosition(getWidth() / 2f - content.getWidth() / 2f, 40);
		// content.debug();

		content.defaults().expandX().uniform();

		content.add(btYes);
		content.add(btLater);

		addActor(content);

	}

	private void initButtons() {
		btYes = new TextButton(idiomas.get("OK"), Assets.styleTextButtonPurchased);
		screen.addEfectoPress(btYes);
		btYes.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				hide();
				game.reqHandler.showRater();
			};
		});

		btLater = new TextButton(idiomas.get("not_now"), Assets.styleTextButtonPurchased);
		screen.addEfectoPress(btLater);
		btLater.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				hide();
			};
		});

	}

	@Override
	public void show(Stage stage) {
		super.show(stage);
		game.reqHandler.showAdBanner();
	}

}
