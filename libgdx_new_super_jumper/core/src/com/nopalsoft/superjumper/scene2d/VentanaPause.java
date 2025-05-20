package com.nopalsoft.superjumper.scene2d;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.superjumper.Assets;
import com.nopalsoft.superjumper.game.GameScreen;
import com.nopalsoft.superjumper.game.WorldGame;
import com.nopalsoft.superjumper.screens.MainMenuScreen;

public class VentanaPause extends Ventana {

	TextButton btMenu, btResume;
	WorldGame oWorld;

	int buttonSize = 55;

	public VentanaPause(final GameScreen currentScreen) {
		super(currentScreen, 350, 280, 300);
		oWorld = currentScreen.oWorld;

		Label lbShop = new Label("Pause", Assets.labelStyleGrande);
		lbShop.setFontScale(1.5f);
		lbShop.setAlignment(Align.center);
		lbShop.setPosition(getWidth() / 2f - lbShop.getWidth() / 2f, 230);
		addActor(lbShop);

		initButtons();

		Table content = new Table();

		content.defaults().expandX().uniform().fill();

		content.add(btResume);
		content.row().padTop(20);
		content.add(btMenu);

		content.pack();
		content.setPosition(getWidth() / 2f - content.getWidth() / 2f, 50);

		addActor(content);

	}

	private void initButtons() {
		btMenu = new TextButton("Menu", Assets.textButtonStyleGrande);
		btMenu.pad(15);

		screen.addEfectoPress(btMenu);
		btMenu.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				hide();
				screen.changeScreenWithFadeOut(MainMenuScreen.class, game);
			};
		});

		btResume = new TextButton("Resume", Assets.textButtonStyleGrande);
		btResume.pad(15);

		screen.addEfectoPress(btResume);
		btResume.addListener(new ClickListener() {
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

	@Override
	public void hide() {
		((GameScreen) screen).setRunning();
		super.hide();
	}

}
