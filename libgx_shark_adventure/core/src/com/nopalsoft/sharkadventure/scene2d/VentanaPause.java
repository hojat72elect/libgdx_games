package com.nopalsoft.sharkadventure.scene2d;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.sharkadventure.Assets;
import com.nopalsoft.sharkadventure.MainShark;
import com.nopalsoft.sharkadventure.game.GameScreen;
import com.nopalsoft.sharkadventure.screens.Screens;

public class VentanaPause extends Group {
	public static final float DURACION_ANIMATION = .3f;

	protected GameScreen screen;
	protected MainShark game;

	private boolean isVisible = false;

	Button btPlay, btRefresh, btHome;

	public VentanaPause(GameScreen currentScreen) {
		setSize(300, 300);
		setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 80);
		screen = currentScreen;
		game = currentScreen.game;
		setBackGround();

		Table tbTitulo = new Table();
		tbTitulo.setSize(getWidth() - 80, 50);
		tbTitulo.setPosition(getWidth() / 2f - tbTitulo.getWidth() / 2f, getHeight() - 30);
		tbTitulo.setBackground(Assets.backgroundTitulo);

		Label lbTitulo = new Label("Paused", Assets.lblStyle);

		tbTitulo.add(lbTitulo).fill().padBottom(10);
		addActor(tbTitulo);

		btPlay = new Button(Assets.btDer, Assets.btDerPress);
		btPlay.setSize(70, 70);
		btPlay.setPosition(getWidth() / 2f - btPlay.getWidth() / 2f, 170);
		btPlay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hide();
				screen.setRunning(false);

			}
		});

		btRefresh = new Button(Assets.btRefresh, Assets.btRefreshPress);
		btRefresh.setSize(70, 70);
		btRefresh.setPosition(getWidth() / 2f + 25, 80);
		btRefresh.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hide();
				screen.game.setScreen(new GameScreen(game, false));
			}
		});

		btHome = new Button(Assets.btHome, Assets.btHomePress);
		btHome.setSize(70, 70);
		btHome.setPosition(getWidth() / 2f - btHome.getWidth() - 25, 80);
		btHome.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hide();
				screen.game.setScreen(new GameScreen(game, true));
			}
		});

		addActor(btPlay);
		addActor(btRefresh);
		addActor(btHome);
	}

	private void setBackGround() {
		Image img = new Image(Assets.backgroundVentana);
		img.setSize(getWidth(), getHeight());
		addActor(img);

	}

	public void show(Stage stage) {

		setOrigin(getWidth() / 2f, getHeight() / 2f);
		setX(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f);

		setScale(.5f);
		addAction(Actions.sequence(Actions.scaleTo(1, 1, DURACION_ANIMATION)));

		isVisible = true;
		stage.addActor(this);

		game.reqHandler.showAdBanner();

	}

	public boolean isVisible() {
		return isVisible;
	}

	public void hide() {
		isVisible = false;
		game.reqHandler.hideAdBanner();
		remove();
	}

}
