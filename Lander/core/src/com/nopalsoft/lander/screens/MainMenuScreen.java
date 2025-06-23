package com.nopalsoft.lander.screens;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.lander.Assets;
import com.nopalsoft.lander.MainLander;
import com.nopalsoft.lander.dialogs.VentanaShop;

public class MainMenuScreen extends Screens {

	TextButton btPlay, btSettings, btMore;

	VentanaShop dialogShop;

	public MainMenuScreen(MainLander game) {
		super(game);

		initButtons();

		MoveToAction action = Actions.action(MoveToAction.class);
		action.setInterpolation(Interpolation.linear);
		action.setPosition(5f, 540);
		action.setDuration(.75f);
		ScaleToAction scAction = Actions.action(ScaleToAction.class);
		scAction.setInterpolation(Interpolation.fade);
		scAction.setDuration(1f);
		scAction.setScale(1);
		Image titulo = new Image(Assets.titulo);
		titulo.setSize(447, 225);
		titulo.setPosition(5f, 1000);
		titulo.setScale(.3f);
		titulo.addAction(Actions.parallel(action, scAction));

		stage.addActor(btPlay);
		stage.addActor(btSettings);
		stage.addActor(btMore);
		stage.addActor(titulo);

		dialogShop = new VentanaShop(game);

	}

	private void initButtons() {

		float botonWidth = 300;
		float botonX = SCREEN_WIDTH / 2f - botonWidth / 2f;

		btPlay = new TextButton("Play", Assets.styleTextButtonMenu);
		btPlay.setSize(botonWidth, 100);
		btPlay.setPosition(botonX, -10);
		btPlay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				changeScreen(LevelScreen.class);
			}
		});

		btSettings = new TextButton("Settings", Assets.styleTextButtonMenu);
		btSettings.setSize(botonWidth, 100);
		btSettings.setPosition(botonX, -50);
		btSettings.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dialogShop.show(stage);
			}
		});

		btMore = new TextButton("More", Assets.styleTextButtonMenu);
		btMore.setSize(botonWidth, 100);
		btMore.setPosition(botonX, -90);
		btMore.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

			}
		});

		addActionToButtonEnter(btPlay, botonX, 420);
		addActionToButtonEnter(btSettings, botonX, 300);
		addActionToButtonEnter(btMore, botonX, 180);

	}

	public void addActionToButtonEnter(TextButton bt, float x, float y) {
		MoveToAction action = Actions.action(MoveToAction.class);
		action.setInterpolation(Interpolation.exp10Out);
		action.setPosition(x, y);
		action.setDuration(.75f);
		bt.addAction(action);
	}

	public void changeScreen(final Class<?> screen) {

		addActionToButtonLeave(btPlay, btPlay.getX(), -100);
		addActionToButtonLeave(btSettings, btSettings.getX(), -100);
		addActionToButtonLeave(btMore, btMore.getX(), -100);

		stage.addAction(Actions.sequence(Actions.delay(.75f), Actions.run(new Runnable() {
			@Override
			public void run() {
				if (screen == LevelScreen.class) {
					game.setScreen(new LevelScreen(game));
				}
			}
		})));
	}

	public void addActionToButtonLeave(TextButton bt, float x, float y) {
		MoveToAction action = Actions.action(MoveToAction.class);
		action.setInterpolation(Interpolation.exp10In);
		action.setPosition(x, y);
		action.setDuration(.75f);
		bt.addAction(action);
	}

	@Override
	public void draw(float delta) {
		oCam.update();
		batcher.setProjectionMatrix(oCam.combined);

		batcher.begin();
		batcher.disableBlending();
		batcher.draw(Assets.fondo, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		batcher.end();

	}

	@Override
	public void update(float delta) {

	}

}
