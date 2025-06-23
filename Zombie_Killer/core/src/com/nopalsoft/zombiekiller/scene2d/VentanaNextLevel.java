package com.nopalsoft.zombiekiller.scene2d;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.zombiekiller.Assets;
import com.nopalsoft.zombiekiller.game.GameScreen;
import com.nopalsoft.zombiekiller.game.WorldGame;
import com.nopalsoft.zombiekiller.screens.Screens;
import com.nopalsoft.zombiekiller.shop.VentanaShop;

public class VentanaNextLevel extends Ventana {

	WorldGame oWorld;

	Button btLevels, btShop, btTryAgain;
	int buttonSize = 55;
	VentanaShop ventanaShop;
	VentanaSelectLevel ventanaSelectLevel;

	int skulls;

	public VentanaNextLevel(Screens currentScreen) {
		super(currentScreen, 380, 390, 50, Assets.backgroundSmallWindow);
		oWorld = ((GameScreen) currentScreen).oWorld;
		ventanaShop = new VentanaShop(screen);
		ventanaSelectLevel = new VentanaSelectLevel(screen);

		skulls = oWorld.skulls;

		Label lbShop = new Label(idiomas.get("congratulations"), Assets.labelStyleGrande);
		lbShop.setFontScale(1.3f);
		lbShop.setAlignment(Align.center);
		lbShop.setPosition(getWidth() / 2f - lbShop.getWidth() / 2f, 300);
		addActor(lbShop);

		initButtons();

		Table tbSkulls = new Table();
		tbSkulls.setSize(210, 60);
		tbSkulls.setPosition(getWidth() / 2f - tbSkulls.getWidth() / 2f, 225);
		// tbSkulls.debug();
		tbSkulls.defaults().expandX().uniform();

		for (int i = 1; i <= 3; i++) {
			Image imgSkull = new Image(Assets.upgradeOff);
			if (skulls >= i)
				imgSkull.setDrawable(new TextureRegionDrawable(Assets.itemSkull));
			tbSkulls.add(imgSkull).size(55);
		}

		Table tbStats = new Table();
		tbStats.setSize(getWidth(), 95);
		tbStats.setPosition(0, 130);
		// tbStats.debug();

		Label lbZombiesKilled = new Label(idiomas.get("zombies_killed"), Assets.labelStyleChico);
		Label lbZombiesKilledNum = new Label(oWorld.totalZombiesKilled + "/" + oWorld.TOTAL_ZOMBIES_LEVEL, Assets.labelStyleChico);

		Label lbGemsCollected = new Label(idiomas.get("gems"), Assets.labelStyleChico);
		Label lbGemsCollectedNum = new Label(oWorld.gems + "", Assets.labelStyleChico);

		Label lbBonus = new Label(idiomas.get("bonus_gems"), Assets.labelStyleChico);
		Label lbBonusNum = new Label(oWorld.bonus + "", Assets.labelStyleChico);

		tbStats.defaults().pad(0, 45, 0, 45).expandX();

		tbStats.add(lbZombiesKilled).left();
		tbStats.add(lbZombiesKilledNum).right();

		tbStats.row();
		tbStats.add(lbGemsCollected).left();
		tbStats.add(lbGemsCollectedNum).right();

		tbStats.row();
		tbStats.add(lbBonus).left();
		tbStats.add(lbBonusNum).right();

		// Botones
		Table tbButtons = new Table();
		tbButtons.setSize(250, 90);
		tbButtons.setPosition(getWidth() / 2f - tbButtons.getWidth() / 2f, 40);
		// content.debug();

		tbButtons.defaults().expandX().uniform();

		tbButtons.add(btShop);
		tbButtons.add(btTryAgain);
		tbButtons.add(btLevels);

		addActor(tbButtons);
		addActor(tbSkulls);
		addActor(tbStats);

	}

	private void initButtons() {
		btLevels = new Button(Assets.btRight);
		btLevels.setSize(buttonSize, buttonSize);

		screen.addEfectoPress(btLevels);
		btLevels.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				ventanaSelectLevel.show(screen.stage);
			};
		});

		btShop = new Button(Assets.btShop);
		btShop.setSize(buttonSize, buttonSize);

		screen.addEfectoPress(btShop);
		btShop.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				ventanaShop.show(screen.stage);
			};
		});

		btTryAgain = new Button(Assets.btTryAgain);
		btTryAgain.setSize(buttonSize, buttonSize);

		screen.addEfectoPress(btTryAgain);
		btTryAgain.addListener(new ClickListener() {
			public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
				screen.changeScreenWithFadeOut(GameScreen.class, ((GameScreen) screen).level, game);
			};
		});
	}

	@Override
	public void show(Stage stage) {
		super.show(stage);
		game.reqHandler.showAdBanner();
	}

}
