package com.nopalsoft.donttap.dialogs;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.donttap.Assets;
import com.nopalsoft.donttap.screens.Screens;

public class VentanaHelp extends Group {
	static public float fadeDuration = 0.25f;
	Screens screen;

	public VentanaHelp(Screens screen) {
		this.screen = screen;

		setSize(430, 460);
		setOrigin(getWidth() / 2f, getHeight() / 2f);
		setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, 200);

		Image background = new Image(Assets.fondoPuntuaciones);
		background.setSize(getWidth(), getHeight());
		addActor(background);
		getColor().a = 0;

		Table tbHelp = new Table();
		tbHelp.setFillParent(true);
		// tbHelp.debug();

		ScrollPaneStyle scrollStyle = new ScrollPaneStyle(null, null, null,
				null, Assets.fondoPuntuaciones);
		final ScrollPane scroll = new ScrollPane(tbHelp, scrollStyle);
		scroll.setSize(getWidth(), getHeight() - 50);
		scroll.setPosition(0, 50);

		Label lbClassic = new Label("Classic:", Assets.labelStyleBlack);
		lbClassic.setWrap(true);

		Label lbTime = new Label("Time trial:", Assets.labelStyleBlack);
		lbTime.setWrap(true);

		Label lbEndless = new Label("Endless:", Assets.labelStyleBlack);
		lbEndless.setWrap(true);

		Label lbHelpClassic = new Label("Tap 100 tiles as fast as you can",
				Assets.labelStyleChico);
		lbHelpClassic.setWrap(true);

		Label lbHelpTime = new Label("Tap as fast as you can for 1 minute",
				Assets.labelStyleChico);
		lbHelpTime.setWrap(true);

		Label lbHelpEndless = new Label("How many tiles can you tap?",
				Assets.labelStyleChico);
		lbHelpEndless.setWrap(true);

		tbHelp.add(lbClassic).left().padLeft(5).width(135);
		tbHelp.add(lbHelpClassic).left().expandX().fill();

		tbHelp.row().padTop(15);
		tbHelp.add(lbTime).left().padLeft(5).width(135);
		tbHelp.add(lbHelpTime).left().expandX().fill();

		tbHelp.row().padTop(15);
		tbHelp.add(lbEndless).left().padLeft(5).width(135);
		tbHelp.add(lbHelpEndless).left().expandX().fill();

		final TextButton btOk = new TextButton("OK",
				Assets.textButtonStyleChico);
		btOk.setPosition(getWidth() / 2f - btOk.getWidth() / 2f, 5);
		screen.addEfectoPress(btOk);
		btOk.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				hide();
			}
		});

		addAction(Actions.sequence(Actions.alpha(1f, fadeDuration),
				Actions.run(new Runnable() {

					@Override
					public void run() {
						addActor(scroll);
						addActor(btOk);
					}
				})));

	}

	Image dim;

	public void show(Stage stage) {

		dim = new Image(Assets.pixelNegro);
		dim.setFillParent(true);
		dim.getColor().a = 0;
		dim.addAction(Actions.alpha(.7f, fadeDuration - .5f));

		stage.addActor(dim);

		stage.addActor(this);
	}

	void hide() {

		dim.addAction(Actions.sequence(Actions.alpha(0, fadeDuration - .5f),
				Actions.removeActor()));
		addAction(Actions.sequence(Actions.alpha(0, fadeDuration),
				Actions.removeActor()));

	}
}
