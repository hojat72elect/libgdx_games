package com.nopalsoft.ponyrace.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.nopalsoft.ponyrace.game.GameScreenTileds;
import com.nopalsoft.ponyrace.game.WorldTiled;
import com.nopalsoft.ponyrace.screens.Screens;

public class VentanaTryAgain extends Ventana {

	GameScreenTileds gameScreen;
	WorldTiled oWorld;

	Label lbCoinsNum, lbTimeLeftNum;

	public VentanaTryAgain(Screens currentScreen) {
		super(currentScreen);
		setSize(460, 320);
		setY(60);
		setBackGround();

		gameScreen = (GameScreenTileds) currentScreen;
		oWorld = gameScreen.oWorld;

		Image medalla = null;
		if (oWorld.oPony.lugarEnLaCarrera == 2)
			medalla = new Image(oAssets.medallaSegundoLugar);
		else if (oWorld.oPony.lugarEnLaCarrera == 3)
			medalla = new Image(oAssets.medallaTercerLugar);

		Table content = new Table();
		content.setSize(320, 180);
		if (medalla != null) {
			content.setPosition(140, 70);
			medalla.setScale(.9f);
			medalla.setPosition(15, getHeight() / 2f - medalla.getHeight() / 2f);
			addActor(medalla);
		}
		else {
			Image youLose = new Image(oAssets.youLose);
			youLose.setPosition(getWidth() / 2f - youLose.getWidth() / 2f, 250);
			addActor(youLose);

			content.setPosition(getWidth() / 2f - content.getWidth() / 2f, 50);

		}

		// content.debug();

		Label lbLapTime = new Label("Lap time", new LabelStyle(
				oAssets.fontChco, Color.WHITE));

		Label lbLapTimeNum = new Label(gameScreen.lapTime, new LabelStyle(
				oAssets.fontChco, Color.WHITE));

		Label lbTimeLeft = new Label("Time left", new LabelStyle(
				oAssets.fontChco, Color.WHITE));

		lbTimeLeftNum = new Label("", new LabelStyle(oAssets.fontChco,
				Color.WHITE));

		Label lbCoins = new Label("Coins", new LabelStyle(oAssets.fontChco,
				Color.WHITE));

		lbCoinsNum = new Label("",
				new LabelStyle(oAssets.fontChco, Color.WHITE));

		content.row();
		content.add(lbLapTime).left();
		content.add(lbLapTimeNum).expand();

		content.row();
		content.add(lbTimeLeft).left();
		content.add(lbTimeLeftNum).expand();

		content.row();
		content.add(lbCoins).left();
		content.add(lbCoinsNum).expand();

		addActor(content);

	}

	@Override
	public void act(float delta) {
		lbCoinsNum.setText(gameScreen.stringMonedasRecolectadas);
		lbTimeLeftNum.setText(gameScreen.stringTiempoLeft);
		super.act(delta);
	}

	@Override
	protected void endResize() {

	}
}
