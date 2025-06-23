package com.nopalsoft.zombiekiller.scene2d;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.zombiekiller.Assets;
import com.nopalsoft.zombiekiller.Settings;
import com.nopalsoft.zombiekiller.game.GameScreen;
import com.nopalsoft.zombiekiller.screens.Screens;

public class VentanaSelectLevel extends Ventana {

	Table contenedor;
	int totalSkulls;// Cada nivel necesita (nivel * 2) skulls para ser accecible.

	public VentanaSelectLevel(Screens currentScreen) {
		super(currentScreen, 650, 450, 20, Assets.backgroundBigWindow);
		setCloseButton(570, 320, 65);

		Label lbShop = new Label(idiomas.get("select_a_level"), Assets.labelStyleGrande);
		lbShop.setPosition(getWidth() / 2f - lbShop.getWidth() / 2f, 355);
		lbShop.setAlignment(Align.center);
		lbShop.setFontScale(1.2f);
		addActor(lbShop);

		contenedor = new Table();
		// contenedor.debug();
		ScrollPane scroll = new ScrollPane(contenedor, Assets.styleScrollPane);
		scroll.setFadeScrollBars(false);
		scroll.setSize(465, 280);
		scroll.setPosition(91, 53);
		scroll.setVariableSizeKnobs(false);

		for (int i = 0; i < Settings.arrSkullsMundo.length; i++) {
			totalSkulls += Settings.arrSkullsMundo[i];
		}

		if (Settings.isTest)
			totalSkulls += Settings.arrSkullsMundo.length * 3;

		int level = 0;
		contenedor.defaults().pad(7, 5, 7, 5);
		for (int col = 0; col < Settings.NUM_MAPS; col++) {
			contenedor.add(getLevelButton(level)).size(93, 113);
			level++;

			if (level % 4 == 0)
				contenedor.row();
		}

		addActor(scroll);
	}

	public Button getLevelButton(final int level) {
		final TextButton button;

		final int skullsToNextLevel = (int) (level * 2.5f);

		if (!(totalSkulls >= skullsToNextLevel)) {
			button = new TextButton("" + (level + 1), Assets.styleTextButtontLevelLocked);
			button.setDisabled(true);
		}
		else {
			button = new TextButton("" + (level + 1), Assets.styleTextButtontLevel);

			int stars = Settings.arrSkullsMundo[level];

			Table starTable = new Table();
			starTable.defaults().pad(1);
			if (stars >= 0) {
				for (int star = 0; star < 3; star++) {
					if (stars > star) {
						starTable.add(new Image(Assets.itemSkull)).width(20).height(20);
					}
					else {
						starTable.add(new Image(Assets.upgradeOff)).width(20).height(20);
					}
				}
			}

			button.row();
			button.add(starTable).height(30).padBottom(10);
		}

		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (button.isDisabled()) {
					new VentanaWarning(screen, idiomas.format("warning_level_window", skullsToNextLevel)).show(screen.stage);
				}
				else {
					hide();
					screen.changeScreenWithFadeOut(GameScreen.class, level, game);
				}
			}
		});

		screen.addEfectoPress(button);

		return button;

	}

	@Override
	public void show(Stage stage) {
		super.show(stage);
		game.reqHandler.showAdBanner();
	}
}
