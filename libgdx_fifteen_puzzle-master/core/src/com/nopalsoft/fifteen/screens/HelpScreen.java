package com.nopalsoft.fifteen.screens;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.fifteen.Assets;
import com.nopalsoft.fifteen.MainFifteen;

public class HelpScreen extends Screens {

	Label lbTextHelp;
	Image imgPuzzle;

	Button btBack;

	public HelpScreen(final MainFifteen game) {
		super(game);
		lbTextHelp = new Label(
				"The object of the puzzle is to place the tiles in order (see diagram below) by making sliding moves that use the empty space.",
				Assets.labelStyleChico);
		lbTextHelp.setWrap(true);
		lbTextHelp.setWidth(SCREEN_WIDTH - 20);
		lbTextHelp.setPosition(SCREEN_WIDTH / 2f - lbTextHelp.getWidth() / 2f,
				660);
		lbTextHelp.setScale(1.1f);

		imgPuzzle = new Image(Assets.puzzleSolved);
		imgPuzzle.setSize(350, 350);
		imgPuzzle
				.setPosition(SCREEN_WIDTH / 2 - imgPuzzle.getWidth() / 2f, 200);

		btBack = new Button(Assets.btAtras);
		btBack.setSize(60, 60);
		btBack.setPosition(SCREEN_WIDTH / 2 - 30, 115);
		addEfectoPress(btBack);
		btBack.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				changeScreenWithFadeOut(MainMenuScreen.class, game);
			}
		});
		stage.addActor(lbTextHelp);
		stage.addActor(btBack);
		stage.addActor(imgPuzzle);
	}

	@Override
	public void draw(float delta) {
		batcher.begin();
		batcher.draw(Assets.fondo, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		batcher.end();
	}

	@Override
	public void update(float delta) {

	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {

			changeScreenWithFadeOut(MainMenuScreen.class, game);
		}
		return super.keyDown(keycode);
	}

}
