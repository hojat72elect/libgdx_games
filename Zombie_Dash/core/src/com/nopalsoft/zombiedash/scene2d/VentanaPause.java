package com.nopalsoft.zombiedash.scene2d;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.zombiedash.Assets;
import com.nopalsoft.zombiedash.game.GameScreen;
import com.nopalsoft.zombiedash.screens.MainMenuScreen;
import com.nopalsoft.zombiedash.screens.Screens;

public class VentanaPause extends Ventana {

	GameScreen gameScreen;

	Button btMenu, btTryAgain;

	int buttonSize = 55;

	public VentanaPause(Screens currentScreen) {
		super(currentScreen, 350, 310, 100, Assets.backgroundSmallWindow);
		setCloseButton(305, 265, 45);

		gameScreen = (GameScreen) currentScreen;

		Label lbShop = new Label(idiomas.get("pause"), Assets.labelStyleGrande);
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

		btTryAgain = new Button(Assets.btTryAgain);
		btTryAgain.setSize(buttonSize, buttonSize);

		screen.addEfectoPress(btTryAgain);
		btTryAgain.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				hide();
				screen.changeScreenWithFadeOut(GameScreen.class, game);
			};
		});

	}

	@Override
	public void show(Stage stage) {
		super.show(stage);
		game.reqHandler.showAdBanner();
	}

	@Override
	public void hide() {
		gameScreen.setRunning();
		super.hide();
	}

}
