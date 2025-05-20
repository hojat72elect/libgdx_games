package com.nopalsoft.superjumper.scene2d;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.I18NBundle;
import com.nopalsoft.superjumper.MainSuperJumper;
import com.nopalsoft.superjumper.screens.Screens;

public class Ventana extends Group {
	public static final float DURACION_ANIMATION = .3f;
	protected Screens screen;
	protected I18NBundle idiomas;
	protected MainSuperJumper game;

	private boolean isVisible = false;

	public Ventana(Screens currentScreen, float width, float height, float positionY) {
		screen = currentScreen;
		game = currentScreen.game;
		idiomas = game.idiomas;
		setSize(width, height);
		setY(positionY);

	}

	public void show(Stage stage) {

		setOrigin(getWidth() / 2f, getHeight() / 2f);
		setX(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f);

		setScale(.5f);
		addAction(Actions.sequence(Actions.scaleTo(1, 1, DURACION_ANIMATION), Actions.run(new Runnable() {

			@Override
			public void run() {
				endResize();
			}

		})));

		isVisible = true;
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
