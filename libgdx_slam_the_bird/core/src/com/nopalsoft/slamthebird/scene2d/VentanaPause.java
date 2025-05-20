package com.nopalsoft.slamthebird.scene2d;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.game.GameScreen;
import com.nopalsoft.slamthebird.screens.Screens;

public class VentanaPause extends Ventana {

	GameScreen gameScreen;

	public VentanaPause(Screens currentScreen) {
		super(currentScreen);
		setSize(350, 260);
		setY(300);
		setBackGround();

		gameScreen = (GameScreen) currentScreen;

		Label lbTitle = new Label("Paused", Assets.styleLabelChico);
		lbTitle.setPosition(getWidth() / 2f - lbTitle.getWidth() / 2f, 210);

		TextButton btResume = new TextButton("Resume",
				Assets.styleTextButtonPurchased);
		screen.addEfectoPress(btResume);
		btResume.setSize(150, 50);
		btResume.setPosition(getWidth() / 2f - btResume.getWidth() / 2f, 130);
		btResume.getLabel().setWrap(true);
		btResume.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hide();
				gameScreen.setRunningFromPaused();

			}
		});

		TextButton btMainMenu = new TextButton("Menu",
				Assets.styleTextButtonPurchased);
		screen.addEfectoPress(btMainMenu);
		btMainMenu.setSize(150, 50);
		btMainMenu.setPosition(getWidth() / 2f - btResume.getWidth() / 2f, 40);
		btMainMenu.getLabel().setWrap(true);
		btMainMenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hide();
				screen.changeScreenWithFadeOut(GameScreen.class, game);

			}
		});

		addActor(btResume);
		addActor(btMainMenu);
		addActor(lbTitle);

	}

	@Override
	protected void endResize() {

	}
}
