package com.nopalsoft.slamthebird.scene2d;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.screens.Screens;

public class VentanaRate extends Ventana {

	public VentanaRate(Screens currentScreen) {
		super(currentScreen);
		setSize(390, 260);
		setY(300);
		setBackGround();

		Label lbTitle = new Label("Support this game", Assets.styleLabelChico);
		lbTitle.setPosition(getWidth() / 2f - lbTitle.getWidth() / 2f, 210);

		Label lbContenido = new Label(
				"Hello, thank you for playing Slam the Bird.\nHelp us to support this game. Just rate us at the app store.",
				Assets.styleLabelChico);
		lbContenido.setSize(getWidth() - 20, 170);
		lbContenido.setPosition(getWidth() / 2f - lbContenido.getWidth() / 2f,
				50);
		lbContenido.setWrap(true);

		TextButton btRate = new TextButton("Rate",
				Assets.styleTextButtonPurchased);
		screen.addEfectoPress(btRate);
		btRate.getLabel().setWrap(true);
		btRate.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hide();
				game.reqHandler.showRater();

			}
		});

		TextButton btNotNow = new TextButton("Not now",
				Assets.styleTextButtonSelected);
		screen.addEfectoPress(btNotNow);
		btNotNow.getLabel().setWrap(true);
		btNotNow.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hide();

			}
		});

		Table tbBotones = new Table();
		tbBotones.setSize(getWidth() - 20, 60);
		tbBotones.setPosition(getWidth() / 2f - tbBotones.getWidth() / 2f, 10);

		tbBotones.defaults().uniform().expand().center().fill().pad(10);
		tbBotones.add(btRate);
		tbBotones.add(btNotNow);

		addActor(lbContenido);
		addActor(tbBotones);
		addActor(lbTitle);

	}

	@Override
	protected void endResize() {

	}
}
