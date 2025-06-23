package com.nopalsoft.zombiedash.scene2d;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.zombiedash.Assets;
import com.nopalsoft.zombiedash.Settings;
import com.nopalsoft.zombiedash.game.GameScreen;
import com.nopalsoft.zombiedash.screens.Screens;

public class VentanaRevive extends Ventana {

	TextButton btOK, btNo;
	NumItemsBar totalGems;

	GameScreen gameScreen;

	int buttonSize = 55;
	int priceRevive;

	public VentanaRevive(Screens currentScreen, int priceRevive) {
		super(currentScreen, 250, 250, 100, Assets.backgroundSmallWindow);
		gameScreen = (GameScreen) currentScreen;

		this.priceRevive = priceRevive;

		totalGems = new NumItemsBar(Assets.itemGem, 150, 220);
		totalGems.updateNumGems(Settings.gemsTotal);
		addActor(totalGems);

		Label lbShop = new Label(idiomas.get("revive"), Assets.labelStyleGrande);
		lbShop.setFontScale(1f);
		lbShop.setAlignment(Align.center);
		lbShop.setPosition(getWidth() / 2f - lbShop.getWidth() / 2f, 170);
		addActor(lbShop);

		Table tbGemsPrice = new Table();
		tbGemsPrice.add(new Image(Assets.itemGem)).size(25);
		tbGemsPrice.add(new Label("x" + priceRevive, Assets.labelStyleChico)).pad(5).center();

		tbGemsPrice.pack();
		tbGemsPrice.setPosition(getWidth() / 2f - tbGemsPrice.getWidth() / 2f, 120);
		addActor(tbGemsPrice);

		initButtons();

		Table content = new Table();
		content.setSize(getWidth() - 50, 90);
		content.setPosition(getWidth() / 2f - content.getWidth() / 2f, 30);
		// content.debug();

		content.defaults().expandX().uniform().pad(5, 5, 5, 5).fill();

		content.add(btOK).height(50);
		content.add(btNo).height(50);

		addActor(content);

	}

	private void initButtons() {
		btOK = new TextButton(idiomas.get("OK"), Assets.styleTextButtonBuy);
		screen.addEfectoPress(btOK);
		btOK.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				hide();
				gameScreen.setRevive();
				// Ya revise antes de poner el dialogo que si se pudiera hacer esta resta
				Settings.gemsTotal -= priceRevive;

			};
		});

		btNo = new TextButton(idiomas.get("no"), Assets.styleTextButtonBuy);
		screen.addEfectoPress(btNo);
		btNo.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				hide();
				gameScreen.setGameover();

			};
		});

	}

	@Override
	public void show(Stage stage) {
		super.show(stage);
		game.reqHandler.showAdBanner();
	}

}
