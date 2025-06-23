package com.nopalsoft.zombiekiller.scene2d;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.zombiekiller.Assets;
import com.nopalsoft.zombiekiller.game.GameScreen;
import com.nopalsoft.zombiekiller.screens.MainMenuScreen;
import com.nopalsoft.zombiekiller.screens.Screens;
import com.nopalsoft.zombiekiller.shop.VentanaShop;

public class VentanaGameover extends Ventana {

	Button btMenu, btShop, btTryAgain;

	int buttonSize = 55;

	VentanaShop ventanaShop;

	public VentanaGameover(Screens currentScreen) {
		super(currentScreen, 350, 310, 100, Assets.backgroundSmallWindow);

		ventanaShop = new VentanaShop(screen);

		Label lbShop = new Label(idiomas.get("game_over"), Assets.labelStyleGrande);
		lbShop.setFontScale(1.5f);
		lbShop.setAlignment(Align.center);
		lbShop.setPosition(getWidth() / 2f - lbShop.getWidth() / 2f, 210);
		addActor(lbShop);

		initButtons();

		Table content = new Table();
		content.setSize(250, 90);
		content.setPosition(getWidth() / 2f - content.getWidth() / 2f, 80);
		// content.debug();

		content.defaults().expandX().uniform();

		content.add(btMenu);
		content.add(btShop);
		content.add(btTryAgain);

		addActor(content);

	}

	private void initButtons() {
		btMenu = new Button(Assets.btMenu);
		btMenu.setSize(buttonSize, buttonSize);

		screen.addEfectoPress(btMenu);
		btMenu.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				hide();
				screen.changeScreenWithFadeOut(MainMenuScreen.class, game);
			};
		});

		btShop = new Button(Assets.btShop);
		btShop.setSize(buttonSize, buttonSize);
		screen.addEfectoPress(btShop);
		btShop.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				ventanaShop.show(screen.stage);
			};
		});

		btTryAgain = new Button(Assets.btTryAgain);
		btTryAgain.setSize(buttonSize, buttonSize);
		screen.addEfectoPress(btTryAgain);
		btTryAgain.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				hide();
				screen.changeScreenWithFadeOut(GameScreen.class, ((GameScreen) screen).level, game);
			};
		});
	}

	@Override
	public void show(Stage stage) {
		super.show(stage);
		game.reqHandler.showAdBanner();
	}

}
