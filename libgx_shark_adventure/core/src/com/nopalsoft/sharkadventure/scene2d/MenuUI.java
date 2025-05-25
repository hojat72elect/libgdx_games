package com.nopalsoft.sharkadventure.scene2d;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
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
import com.nopalsoft.sharkadventure.Settings;
import com.nopalsoft.sharkadventure.game.GameScreen;
import com.nopalsoft.sharkadventure.game.WorldGame;
import com.nopalsoft.sharkadventure.screens.Screens;

public class MenuUI extends Group {
	public static final float ANIMATION_TIME = .35f;

	GameScreen gameScreen;
	WorldGame oWorld;
	Image titulo;
	Image gameOver;

	Table tbMenu;
	Table tbGameOver;

	Label lbBestScore;
	Label lbScore;

	Button btPlay, btLeaderboard, btAchievements, btFacebook, btTwitter;
	Button btMusica, btSonido;

	boolean showMainMenu;

	public MenuUI(final GameScreen gameScreen, WorldGame oWorld) {
		setBounds(0, 0, Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT);
		this.gameScreen = gameScreen;
		this.oWorld = oWorld;

		init();

		tbGameOver = new Table();
		tbGameOver.setSize(350, 200);
		tbGameOver.setBackground(Assets.backgroundVentana);
		tbGameOver.setPosition(getWidth() / 2f - tbGameOver.getWidth() / 2f, 110);

		lbBestScore = new Label("0", Assets.lblStyle);
		lbScore = new Label("0", Assets.lblStyle);

		lbScore.setFontScale(.8f);
		lbBestScore.setFontScale(.8f);

		tbGameOver.pad(15).padTop(30).padBottom(50);
		tbGameOver.defaults().expand();

		tbGameOver.add(new Label("Score", Assets.lblStyle)).left();
		tbGameOver.add(lbScore).expandX().right();

		tbGameOver.row();
		tbGameOver.add(new Label("Best score", Assets.lblStyle)).left();
		tbGameOver.add(lbBestScore).expandX().right();

	}

	private void init() {
		titulo = new Image(Assets.titulo);
		titulo.setScale(1f);
		titulo.setPosition(getWidth() / 2f - titulo.getWidth() * titulo.getScaleX() / 2f, Screens.SCREEN_HEIGHT + titulo.getHeight());

		gameOver = new Image(Assets.gameOver);
		gameOver.setScale(1.25f);
		gameOver.setPosition(getWidth() / 2f - gameOver.getWidth() * gameOver.getScaleX() / 2f, Screens.SCREEN_HEIGHT + gameOver.getHeight());

		btFacebook = new Button(Assets.btFacebook, Assets.btFacebookPress);
		btFacebook.setSize(60, 60);
		btFacebook.setPosition(Screens.SCREEN_WIDTH + btFacebook.getWidth(), 410);

		btTwitter = new Button(Assets.btTwitter, Assets.btTwitterPress);
		btTwitter.setSize(60, 60);
		btTwitter.setPosition(Screens.SCREEN_WIDTH + btTwitter.getWidth(), 410);

		btMusica = new Button(Assets.btMusicOff, Assets.btMusicOn, Assets.btMusicOn);
		btMusica.setSize(60, 60);
		btMusica.setPosition(-btMusica.getWidth(), 410);

		btSonido = new Button(Assets.btSoundOff, Assets.btSoundOn, Assets.btSoundOn);
		btSonido.setSize(60, 60);
		btSonido.setPosition(-btSonido.getWidth(), 325);

		tbMenu = new Table();
		tbMenu.setBackground(Assets.backgroundMenu);

		btPlay = new Button(Assets.btDer, Assets.btDerPress);
		btLeaderboard = new Button(Assets.btLeaderboard, Assets.btLeaderboardPress);
		btAchievements = new Button(Assets.btAchievements, Assets.btAchievementsPress);

		tbMenu.defaults().size(90).padBottom(20).padLeft(10).padRight(10);
		if (Gdx.app.getType() != ApplicationType.WebGL) {
			tbMenu.setSize(385, 85);
			tbMenu.add(btPlay);
			tbMenu.add(btLeaderboard);
			tbMenu.add(btAchievements);
		}
		else {
			tbMenu.setSize(120, 85);
			tbMenu.add(btPlay);
		}
		tbMenu.setPosition(Screens.SCREEN_WIDTH / 2f - tbMenu.getWidth() / 2f, -tbMenu.getHeight());

		btFacebook.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gameScreen.game.facebookHandler.showFacebook();
			}
		});

		btTwitter.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gameScreen.game.reqHandler.shareOnTwitter("");

			}
		});

		btLeaderboard.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (gameScreen.game.gameServiceHandler.isSignedIn()) {
					gameScreen.game.gameServiceHandler.getLeaderboard();
				}
				else {
					gameScreen.game.gameServiceHandler.signIn();
				}
			}
		});

		btAchievements.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (gameScreen.game.gameServiceHandler.isSignedIn()) {
					gameScreen.game.gameServiceHandler.getAchievements();
				}
				else {
					gameScreen.game.gameServiceHandler.signIn();
				}
			}
		});

		btPlay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gameScreen.game.reqHandler.hideAdBanner();
				if (showMainMenu)
					gameScreen.setRunning(true);
				else {
					gameScreen.game.setScreen(new GameScreen(gameScreen.game, false));
				}
			}
		});

		btMusica.setChecked(Settings.isMusicOn);
		btMusica.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Settings.isMusicOn = !Settings.isMusicOn;
				btMusica.setChecked(Settings.isMusicOn);
				if (Settings.isMusicOn)
					Assets.musica.play();
				else
					Assets.musica.pause();
			}
		});

		btSonido.setChecked(Settings.isSoundOn);
		btSonido.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Settings.isSoundOn = !Settings.isSoundOn;
				btSonido.setChecked(Settings.isSoundOn);
			}
		});

		addActor(tbMenu);
		addActor(btFacebook);
		addActor(btTwitter);
		addActor(btMusica);
		addActor(btSonido);

	}

	private void addInActions() {
		titulo.addAction(Actions.moveTo(getWidth() / 2f - titulo.getWidth() * titulo.getScaleX() / 2f, 300, ANIMATION_TIME));
		gameOver.addAction(Actions.moveTo(getWidth() / 2f - gameOver.getWidth() * gameOver.getScaleX() / 2f, 320, ANIMATION_TIME));

		tbMenu.addAction(Actions.moveTo(Screens.SCREEN_WIDTH / 2f - tbMenu.getWidth() / 2f, 0, ANIMATION_TIME));

		btFacebook.addAction(Actions.moveTo(735, 410, ANIMATION_TIME));
		btTwitter.addAction(Actions.moveTo(735, 325, ANIMATION_TIME));
		btMusica.addAction(Actions.moveTo(5, 410, ANIMATION_TIME));
		btSonido.addAction(Actions.moveTo(5, 325, ANIMATION_TIME));

	}

	private void addOutActions() {
		titulo.addAction(Actions.moveTo(getWidth() / 2f - titulo.getWidth() * titulo.getScaleX() / 2f, Screens.SCREEN_HEIGHT + titulo.getHeight(),
				ANIMATION_TIME));
		gameOver.addAction(Actions.moveTo(getWidth() / 2f - gameOver.getWidth() * gameOver.getScaleX() / 2f,
				Screens.SCREEN_HEIGHT + gameOver.getHeight(), ANIMATION_TIME));

		tbMenu.addAction(Actions.moveTo(Screens.SCREEN_WIDTH / 2f - tbMenu.getWidth() / 2f, -tbMenu.getHeight(), ANIMATION_TIME));

		btFacebook.addAction(Actions.moveTo(Screens.SCREEN_WIDTH + btFacebook.getWidth(), 410, ANIMATION_TIME));
		btTwitter.addAction(Actions.moveTo(Screens.SCREEN_WIDTH + btTwitter.getWidth(), 325, ANIMATION_TIME));
		btMusica.addAction(Actions.moveTo(-btMusica.getWidth(), 410, ANIMATION_TIME));
		btSonido.addAction(Actions.moveTo(-btSonido.getWidth(), 325, ANIMATION_TIME));
	}

	public void show(Stage stage, final boolean showMainMenu) {
		addInActions();
		stage.addActor(this);

		titulo.remove();
		gameOver.remove();
		tbGameOver.remove();

		if (showMainMenu) {
			addActor(titulo);
		}
		else {
			lbBestScore.setText(Settings.bestScore + " m");
			lbScore.setText(gameScreen.puntuacion + " m");

			addActor(gameOver);
			addActor(tbGameOver);
		}

		this.showMainMenu = showMainMenu;

	}

	public void removeWithAnimations() {
		addOutActions();
		addAction(Actions.sequence(Actions.delay(ANIMATION_TIME), Actions.removeActor()));
	}

}
