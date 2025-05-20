package com.nopalsoft.sokoban.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.sokoban.Assets;
import com.nopalsoft.sokoban.Settings;
import com.nopalsoft.sokoban.game.GameScreen;
import com.nopalsoft.sokoban.screens.MainMenuScreen;
import com.nopalsoft.sokoban.screens.Screens;

public class VentanaPause extends Ventana {

	Button btHome, btRefresh;
	Table tbAnimations;

	public VentanaPause(Screens currentScreen) {
		super(currentScreen, 350, 300, 100);

		setCloseButton(290, 250, 60);
		setTitle(180, 50, "Paused", 1);

		Table tbMenu = new Table();
		tbMenu.setFillParent(true);

		btHome = new Button(Assets.btHome, Assets.btHomePress);
		btHome.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				screen.changeScreenWithFadeOut(MainMenuScreen.class, screen.game);
			}
		});

		btRefresh = new Button(Assets.btRefresh, Assets.btRefreshPress);
		btRefresh.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				screen.changeScreenWithFadeOut(GameScreen.class, ((GameScreen) screen).level, screen.game);
			}
		});

		final Button btAnimations = new Button(Assets.btOff, Assets.btOn, Assets.btOn);
		btAnimations.setChecked(Settings.animationWalkIsON);

		tbAnimations = new Table();
		tbAnimations.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Settings.animationWalkIsON = !Settings.animationWalkIsON;
				btAnimations.setChecked(Settings.animationWalkIsON);
				Settings.save();
			}
		});

		tbMenu.defaults().expandX();

		tbMenu.pad(30).padTop(55);
		tbMenu.add(btHome);
		tbMenu.add(btRefresh);
		tbMenu.row();

		Label lbAnimatons = new Label("Animations", new LabelStyle(Assets.fontRed, Color.WHITE));
		tbAnimations.add(lbAnimatons);
		tbAnimations.add(btAnimations).padLeft(15);

		tbMenu.add(tbAnimations).colspan(2).padTop(10);

		addActor(tbMenu);

	}

	@Override
	public void hideCompleted() {
		((GameScreen) screen).setRunning();

	}

}
