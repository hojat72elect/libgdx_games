package com.nopalsoft.zombiekiller.scene2d;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.nopalsoft.zombiekiller.Assets;
import com.nopalsoft.zombiekiller.MainZombie;
import com.nopalsoft.zombiekiller.screens.Screens;

public class Ventana extends Group {
	public static final float DURACION_ANIMATION = .3f;
	protected Screens screen;
	protected I18NBundle idiomas;
	protected MainZombie game;

	private boolean isVisible = false;

	public Ventana(Screens currentScreen, float width, float height, float positionY, TextureRegionDrawable imageBackgroun) {
		screen = currentScreen;
		game = currentScreen.game;
		idiomas = game.idiomas;
		setSize(width, height);
		setY(positionY);

		setBackGround(imageBackgroun);
	}

	protected void setCloseButton(float positionX, float positionY, float size) {
		Button btClose = new Button(Assets.btClose);
		btClose.setSize(size, size);
		btClose.setPosition(positionX, positionY);
		screen.addEfectoPress(btClose);
		btClose.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hide();
			}
		});
		addActor(btClose);

	}

	private void setBackGround(TextureRegionDrawable imageBackground) {
		Image img = new Image(imageBackground);
		img.setSize(getWidth(), getHeight());
		addActor(img);

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
