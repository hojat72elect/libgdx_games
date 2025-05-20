package com.nopalsoft.slamthebird.scene2d;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.MainSlamBird;
import com.nopalsoft.slamthebird.screens.Screens;

public class Ventana extends Group {
	public static final float DURACION_ANIMATION = .3f;
	Screens screen;
	MainSlamBird game;

	private boolean isVisible = false;

	public Ventana(Screens currentScreen) {
		screen = currentScreen;
		game = currentScreen.game;
	}

	public void setBackGround() {
		Image img = new Image(Assets.fondoPuntuaciones);
		img.setSize(getWidth(), getHeight());
		addActor(img);

	}

	public void show(Stage stage) {

		setOrigin(getWidth() / 2f, getHeight() / 2f);
		setX(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f);

		setScale(.5f);
		addAction(Actions.sequence(Actions.scaleTo(1, 1, DURACION_ANIMATION),
				Actions.run(new Runnable() {

					@Override
					public void run() {
						endResize();
					}

				})));

		isVisible = true;
		game.reqHandler.showAdBanner();
		stage.addActor(this);

	}

	public boolean isVisible() {
		return isVisible;
	}

	public void hide() {
		isVisible = false;
		game.reqHandler.hideAdBanner();
		remove();
	}

	protected void endResize() {

	}

}
