package com.nopalsoft.ninjarunner.scene2d;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.game.GameScreen;
import com.nopalsoft.ninjarunner.game.WorldGame;
import com.nopalsoft.ninjarunner.screens.Screens;

import javax.swing.plaf.ProgressBarUI;

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

	Button btJump, btSlide;
	public boolean didJump, didSlide, didDash;

	public GameUI(final GameScreen gameScreen, WorldGame oWorld) {
		setBounds(0, 0, Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT);
		this.gameScreen = gameScreen;
		this.oWorld = oWorld;

		init();

		// lifeBar = new ProgressBarUI(Assets.redBar, Assets.corazon, Tiburon.MAX_LIFE, -ProgressBarUI.WIDTH, 440);
		// energyBar = new ProgressBarUI(Assets.energyBar, Assets.blast, Tiburon.MAX_ENERGY, -ProgressBarUI.WIDTH, 395);
		//
		// addActor(lifeBar);
		// addActor(energyBar);

	}

	private void init() {

		btJump = new Button(new ButtonStyle(null, null, null));
		btJump.setSize(getWidth() / 2f, getHeight());
		btJump.setPosition(0, 0);
		btJump.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				didJump = true;
				return false;

			}
		});

		btSlide = new Button(new ButtonStyle(null, null, null));
		btSlide.setSize(getWidth() / 2f, getHeight());
		btSlide.setPosition(getWidth() / 2f + 1, 0);
		btSlide.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				didSlide = true;
				return true;

			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				didSlide = false;
			}
		});

		tbHeader = new Table();
		tbHeader.setSize(Screens.SCREEN_WIDTH, 50);
		tbHeader.setPosition(0, Screens.SCREEN_HEIGHT - tbHeader.getHeight());

		lbPuntuacion = new Label("0", Assets.labelStyleChico);
		tbHeader.add(lbPuntuacion).fill();

		addActor(tbHeader);

		addActor(btJump);
		addActor(btSlide);

		// addActor(btPause);
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		// lbPuntuacion.setText(gameScreen.puntuacion + " m");
	}

	private void addInActions() {
		// btSwimUp.addAction(Actions.moveTo(692, 10, ANIMATION_TIME));
		// btFire.addAction(Actions.moveTo(579, 10, ANIMATION_TIME));
		// btDer.addAction(Actions.moveTo(130, 5, ANIMATION_TIME));
		// btIzq.addAction(Actions.moveTo(5, 5, ANIMATION_TIME));
		// btPause.addAction(Actions.moveTo(750, 430, ANIMATION_TIME));
		// lifeBar.addAction(Actions.moveTo(20, 440, ANIMATION_TIME));
		// energyBar.addAction(Actions.moveTo(20, 395, ANIMATION_TIME));

	}

	private void addOutActions() {
		// btSwimUp.addAction(Actions.moveTo(692, -105, ANIMATION_TIME));
		// btFire.addAction(Actions.moveTo(579, -105, ANIMATION_TIME));
		// btDer.addAction(Actions.moveTo(130, -120, ANIMATION_TIME));
		// btIzq.addAction(Actions.moveTo(5, -120, ANIMATION_TIME));
		// btPause.addAction(Actions.moveTo(845, 430, ANIMATION_TIME));
		// lifeBar.addAction(Actions.moveTo(-ProgressBarUI.WIDTH, 440, ANIMATION_TIME));
		// energyBar.addAction(Actions.moveTo(-ProgressBarUI.WIDTH, 395, ANIMATION_TIME));
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
