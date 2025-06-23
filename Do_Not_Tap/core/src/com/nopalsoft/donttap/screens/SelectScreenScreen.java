package com.nopalsoft.donttap.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.donttap.Assets;
import com.nopalsoft.donttap.MainDoNot;
import com.nopalsoft.donttap.dialogs.VentanaHelp;
import com.nopalsoft.donttap.game.GameScreen;

public class SelectScreenScreen extends Screens {

	Label lbSelectGameMode;
	TextButton btClassic, btEndless, btTime, btHelp;

	Button btBack;

	public SelectScreenScreen(final MainDoNot game) {
		super(game);
		addBackGround();

		lbSelectGameMode = new Label("Select game mode", Assets.labelStyleBlack);
		lbSelectGameMode.setWidth(300);
		lbSelectGameMode.setFontScale(1.3f);
		lbSelectGameMode.setWrap(true);
		lbSelectGameMode.setPosition(
				SCREEN_WIDTH / 2f - lbSelectGameMode.getWidth() / 2f, 650);
		lbSelectGameMode.setAlignment(Align.center);

		Table menu = new Table();
		menu.setSize(200, 540);
		// menu.debug();
		menu.setPosition(SCREEN_WIDTH / 2f - menu.getWidth() / 2f, 60);
		menu.defaults().center().expand();

		btClassic = new TextButton("Classic", Assets.textButtonStyleChico);
		addEfectoPress(btClassic);
		btClassic.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				changeScreenWithFadeOut(GameScreen.class, game,
						GameScreen.MODE_CLASSIC);

			}
		});

		btTime = new TextButton("Time trial", Assets.textButtonStyleChico);
		addEfectoPress(btTime);
		btTime.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				changeScreenWithFadeOut(GameScreen.class, game,
						GameScreen.MODE_TIME);

			}
		});

		btEndless = new TextButton("Endless", Assets.textButtonStyleChico);
		addEfectoPress(btEndless);
		btEndless.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				changeScreenWithFadeOut(GameScreen.class, game,
						GameScreen.MODE_ENDLESS);

			}
		});

		btHelp = new TextButton("?", Assets.textButtonStyleChico);
		addEfectoPress(btHelp);
		btHelp.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				new VentanaHelp(SelectScreenScreen.this).show(stage);
			}
		});

		btBack = new Button(new ButtonStyle(Assets.btAtras, null, null));
		btBack.setSize(55, 55);
		btBack.setPosition(5, 5);
		addEfectoPress(btBack);
		btBack.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				changeScreenWithFadeOut(MainMenuScreen.class, game);
				super.clicked(event, x, y);
			}
		});

		menu.add(btClassic);

		menu.row();
		menu.add(btTime);

		menu.row();
		menu.add(btEndless);

		menu.row();
		menu.add(btHelp).width(45);

		stage.addActor(lbSelectGameMode);
		stage.addActor(menu);
		stage.addActor(btBack);

		game.reqHandler.showAdBanner();

	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ESCAPE || keycode == Keys.BACK)
			changeScreenWithFadeOut(MainMenuScreen.class, game);
		return true;

	}

	@Override
	public void hide() {
		game.reqHandler.hideAdBanner();
		super.hide();
	}

	@Override
	public void draw(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(float delta) {

	}

}
