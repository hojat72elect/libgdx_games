package com.nopalsoft.fifteen.scene2d;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.fifteen.Assets;
import com.nopalsoft.fifteen.game.GameScreen;
import com.nopalsoft.fifteen.screens.MainMenuScreen;
import com.nopalsoft.fifteen.screens.Screens;

public class MarcoPaused extends Group {

	Screens screen;

	public MarcoPaused(final Screens screen) {
		this.screen = screen;
		setSize(420, 300);
		setOrigin(getWidth() / 2f, getHeight() / 2f);
		setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 260);
		setScale(.5f);

		Image background = new Image(Assets.fondoPuntuaciones);
		background.setSize(getWidth(), getHeight());
		addActor(background);

		Label lbPaused = new Label("Pause", Assets.labelStyleGrande);
		lbPaused.setAlignment(Align.center);
		lbPaused.setFontScale(.85f);
		lbPaused.setPosition(getWidth() / 2f - lbPaused.getWidth() / 2f, 230);
		addActor(lbPaused);

		final Label lbResume = new Label("Resume", Assets.labelStyleChico);
		lbResume.setWrap(true);
		lbResume.setAlignment(Align.center);
		lbResume.setPosition(getWidth() / 2f - lbResume.getWidth() / 2f, 155);
		screen.addEfectoPress(lbResume);
		lbResume.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				GameScreen oGame = (GameScreen) screen;
				remove();
				oGame.setRunning();

			}
		});

		final Label lbMainMenu = new Label("Main Menu", Assets.labelStyleChico);
		lbMainMenu.setWrap(true);
		lbMainMenu.setAlignment(Align.center);
		lbMainMenu
				.setPosition(getWidth() / 2f - lbMainMenu.getWidth() / 2f, 65);
		screen.addEfectoPress(lbMainMenu);
		lbMainMenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				screen.changeScreenWithFadeOut(MainMenuScreen.class,
						screen.game);

			}
		});

		addAction(Actions.sequence(Actions.scaleTo(1, 1, .2f),
				Actions.run(new Runnable() {

					@Override
					public void run() {
						addActor(lbMainMenu);
						addActor(lbResume);

					}
				})));

	}
}
