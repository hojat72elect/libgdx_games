package com.nopalsoft.superjumper.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.superjumper.Assets;
import com.nopalsoft.superjumper.MainSuperJumper;
import com.nopalsoft.superjumper.Settings;
import com.nopalsoft.superjumper.game.GameScreen;

public class MainMenuScreen extends Screens {

	Image titulo;

	TextButton btShop, btPlay, btLeaderboard, btRate;
	Button btMusica;
	Label lbBestScore;

	public MainMenuScreen(final MainSuperJumper game) {
		super(game);

		titulo = new Image(Assets.titulo);
		titulo.setPosition(SCREEN_WIDTH / 2f - titulo.getWidth() / 2f, 800);

		titulo.addAction(Actions.sequence(Actions.moveTo(titulo.getX(), 600, 1, Interpolation.bounceOut), Actions.run(new Runnable() {

			@Override
			public void run() {
				stage.addActor(lbBestScore);

			}
		})));

		lbBestScore = new Label("Best score " + Settings.bestScore, Assets.labelStyleChico);
		lbBestScore.setPosition(SCREEN_WIDTH / 2f - lbBestScore.getWidth() / 2f, 570);
		lbBestScore.getColor().a = 0;
		lbBestScore.addAction(Actions.alpha(1, .25f));

		btPlay = new TextButton("Play", Assets.textButtonStyleGrande);
		btPlay.setPosition(SCREEN_WIDTH / 2f - btPlay.getWidth() / 2f, 440);
		btPlay.pad(10);
		btPlay.pack();
		addEfectoPress(btPlay);
		btPlay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				changeScreenWithFadeOut(GameScreen.class, game);
			}
		});

		btShop = new TextButton("Shop", Assets.textButtonStyleGrande);
		btShop.setPosition(SCREEN_WIDTH / 2f - btShop.getWidth() / 2f, 340);
		btShop.pad(10);
		btShop.pack();
		addEfectoPress(btShop);
		btShop.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

			}
		});

		btRate = new TextButton("Rate", Assets.textButtonStyleGrande);
		btRate.setPosition(SCREEN_WIDTH / 2f - btRate.getWidth() / 2f, 340);
		btRate.pad(10);
		btRate.pack();
		addEfectoPress(btRate);
		btRate.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.reqHandler.showRater();
			}
		});

		btLeaderboard = new TextButton("Leaderboard", Assets.textButtonStyleGrande);
		btLeaderboard.pad(10);
		btLeaderboard.pack();
		btLeaderboard.setPosition(SCREEN_WIDTH / 2f - btLeaderboard.getWidth() / 2f, 240);

		addEfectoPress(btLeaderboard);
		btLeaderboard.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (game.gameServiceHandler.isSignedIn())
					game.gameServiceHandler.getLeaderboard();
				else
					game.gameServiceHandler.signIn();
			}
		});

		stage.addActor(titulo);
		stage.addActor(btPlay);
		stage.addActor(btRate);
		// stage.addActor(btShop);
		stage.addActor(btLeaderboard);

	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void draw(float delta) {
		batcher.begin();
		batcher.draw(Assets.fondo, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		batcher.draw(Assets.plataformaBeigeBroken, 100, 100, 125, 45);
		batcher.draw(Assets.plataformaBlue, 350, 280, 125, 45);
		batcher.draw(Assets.plataformaMulticolor, 25, 430, 125, 45);
		batcher.draw(Assets.personajeJump, 25, 270, 75, 80);
		batcher.draw(Assets.nubeHappy, 350, 500, 95, 60);
		batcher.end();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
			Gdx.app.exit();
		}
		return super.keyDown(keycode);
	}

}
