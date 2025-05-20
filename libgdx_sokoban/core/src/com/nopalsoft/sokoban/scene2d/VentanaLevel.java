package com.nopalsoft.sokoban.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.sokoban.Assets;
import com.nopalsoft.sokoban.game.GameScreen;
import com.nopalsoft.sokoban.screens.Screens;

public class VentanaLevel extends Ventana {

	Button btPlay;
	Label lbBestMoves, lbBestTime;

	public VentanaLevel(Screens currentScreen) {
		super(currentScreen, 350, 300, 100);

		setCloseButton(290, 250, 60);
		setTitle(180, 50, "Puntuaciones", .75f);

		Table tbMenu = new Table();
		tbMenu.setFillParent(true);

		btPlay = new Button(Assets.btPlay, Assets.btPlayPress);

		Image imgClock = new Image(Assets.clock);
		Image imgMoves = new Image(Assets.personajeStand);

		lbBestMoves = new Label("0", new LabelStyle(Assets.fontRed, Color.WHITE));
		lbBestTime = new Label("0", new LabelStyle(Assets.fontRed, Color.WHITE));

		tbMenu.defaults().expandX();

		tbMenu.padLeft(30).padRight(30).padBottom(20).padTop(50);
		tbMenu.add(imgMoves).size(45);
		tbMenu.add(lbBestMoves);

		tbMenu.row().padTop(10);
		tbMenu.add(imgClock).size(45);
		tbMenu.add(lbBestTime);

		tbMenu.row().padTop(10);
		tbMenu.add(btPlay).colspan(2).size(60);

		addActor(tbMenu);

	}

	public void show(Stage stage, final int level, int bestMoves, int bestTime) {
		lbBestMoves.setText(bestMoves + "");
		lbBestTime.setText(bestTime + "");

		btPlay.clear();
		btPlay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				screen.changeScreenWithFadeOut(GameScreen.class, level, screen.game);
			}
		});

		super.show(stage);
	}

}
