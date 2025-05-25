package com.nopalsoft.sharkadventure.scene2d;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.sharkadventure.Assets;
import com.nopalsoft.sharkadventure.game.GameScreen;
import com.nopalsoft.sharkadventure.game.WorldGame;
import com.nopalsoft.sharkadventure.objetos.Tiburon;
import com.nopalsoft.sharkadventure.screens.Screens;

public class GameUI extends Group {
	public static final float ANIMATION_TIME = .35f;

	GameScreen gameScreen;
	WorldGame oWorld;

	public int accelX;
	public boolean didSwimUp;
	public boolean didFire;

	public ProgressBarUI lifeBar;
	public ProgressBarUI energyBar;

	Table tbHeader;
	Label lbPuntuacion;
	Button btIzq, btDer, btSwimUp, btFire, btPause;

	public GameUI(final GameScreen gameScreen, WorldGame oWorld) {
		setBounds(0, 0, Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT);
		this.gameScreen = gameScreen;
		this.oWorld = oWorld;

		init();

		lifeBar = new ProgressBarUI(Assets.redBar, Assets.corazon, Tiburon.MAX_LIFE, -ProgressBarUI.WIDTH, 440);
		energyBar = new ProgressBarUI(Assets.energyBar, Assets.blast, Tiburon.MAX_ENERGY, -ProgressBarUI.WIDTH, 395);

		addActor(lifeBar);
		addActor(energyBar);

	}

	private void init() {

		btSwimUp = new Button(Assets.btUp, Assets.btUpPress);
		btSwimUp.setSize(105, 105);
		btSwimUp.setPosition(692, -105);
		btSwimUp.getColor().a = .35f;

		btFire = new Button(Assets.btFire, Assets.btFirePress);
		btFire.setSize(105, 105);
		btFire.setPosition(579, -105);
		btFire.getColor().a = .35f;

		btDer = new Button(Assets.btDer, Assets.btDerPress, Assets.btDerPress);
		btDer.setSize(120, 120);
		btDer.setPosition(130, -120);
		btDer.getColor().a = .35f;

		btIzq = new Button(Assets.btIzq, Assets.btIzqPress, Assets.btIzqPress);
		btIzq.setSize(120, 120);
		btIzq.setPosition(5, -120);
		btIzq.getColor().a = .35f;

		btPause = new Button(Assets.btPausa, Assets.btPausaPress);
		btPause.setSize(45, 45);
		btPause.setPosition(845, 430);
		btPause.getColor().a = .5f;

		tbHeader = new Table();
		tbHeader.setSize(Screens.SCREEN_WIDTH, 50);
		tbHeader.setPosition(0, Screens.SCREEN_HEIGHT - tbHeader.getHeight());

		lbPuntuacion = new Label("0", Assets.lblStyle);
		tbHeader.add(lbPuntuacion).fill();

		addActor(tbHeader);
		btDer.addListener(new ClickListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				accelX = 1;
				btDer.setChecked(true);

			};

			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				accelX = 0;
				btDer.setChecked(false);
			};
		});
		btIzq.addListener(new ClickListener() {
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				accelX = -1;
				btIzq.setChecked(true);
			};

			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				accelX = 0;
				btIzq.setChecked(false);
			};
		});

		btSwimUp.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				didSwimUp = true;
			}
		});

		btFire.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				didFire = true;
			}
		});
		btPause.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				gameScreen.setPaused();
			}
		});

		if (Gdx.app.getType() == ApplicationType.Android || Gdx.app.getType() == ApplicationType.iOS) {
			addActor(btDer);
			addActor(btIzq);
			addActor(btSwimUp);
			addActor(btFire);
		}

		addActor(btPause);
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		lbPuntuacion.setText(gameScreen.puntuacion + " m");
	}

	private void addInActions() {
		btSwimUp.addAction(Actions.moveTo(692, 10, ANIMATION_TIME));
		btFire.addAction(Actions.moveTo(579, 10, ANIMATION_TIME));
		btDer.addAction(Actions.moveTo(130, 5, ANIMATION_TIME));
		btIzq.addAction(Actions.moveTo(5, 5, ANIMATION_TIME));
		btPause.addAction(Actions.moveTo(750, 430, ANIMATION_TIME));
		lifeBar.addAction(Actions.moveTo(20, 440, ANIMATION_TIME));
		energyBar.addAction(Actions.moveTo(20, 395, ANIMATION_TIME));

	}

	private void addOutActions() {
		btSwimUp.addAction(Actions.moveTo(692, -105, ANIMATION_TIME));
		btFire.addAction(Actions.moveTo(579, -105, ANIMATION_TIME));
		btDer.addAction(Actions.moveTo(130, -120, ANIMATION_TIME));
		btIzq.addAction(Actions.moveTo(5, -120, ANIMATION_TIME));
		btPause.addAction(Actions.moveTo(845, 430, ANIMATION_TIME));
		lifeBar.addAction(Actions.moveTo(-ProgressBarUI.WIDTH, 440, ANIMATION_TIME));
		energyBar.addAction(Actions.moveTo(-ProgressBarUI.WIDTH, 395, ANIMATION_TIME));
	}

	public void show(Stage stage) {
		addInActions();
		stage.addActor(this);
	}

	public void removeWithAnimations() {
		addOutActions();
		addAction(Actions.sequence(Actions.delay(ANIMATION_TIME), Actions.removeActor()));
	}

}
