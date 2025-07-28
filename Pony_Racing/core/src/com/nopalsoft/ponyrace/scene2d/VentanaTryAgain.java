package com.nopalsoft.ponyrace.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.nopalsoft.ponyrace.game.GameScreen;
import com.nopalsoft.ponyrace.game.TileMapHandler;
import com.nopalsoft.ponyrace.screens.BaseScreen;

public class VentanaTryAgain extends Ventana {

    GameScreen gameScreen;
    TileMapHandler oWorld;

    Label lbCoinsNum, lbTimeLeftNum;

    public VentanaTryAgain(BaseScreen currentScreen) {
        super(currentScreen);
        setSize(460, 320);
        setY(60);
        setBackGround();

        gameScreen = (GameScreen) currentScreen;
        oWorld = gameScreen.world;

        Image medalla = null;
        if (oWorld.oPony.lugarEnLaCarrera == 2)
            medalla = new Image(oAssetsHandler.medallaSegundoLugar);
        else if (oWorld.oPony.lugarEnLaCarrera == 3)
            medalla = new Image(oAssetsHandler.medallaTercerLugar);

        Table content = new Table();
        content.setSize(320, 180);
        if (medalla != null) {
            content.setPosition(140, 70);
            medalla.setScale(.9f);
            medalla.setPosition(15, getHeight() / 2f - medalla.getHeight() / 2f);
            addActor(medalla);
        } else {
            Image youLose = new Image(oAssetsHandler.youLose);
            youLose.setPosition(getWidth() / 2f - youLose.getWidth() / 2f, 250);
            addActor(youLose);

            content.setPosition(getWidth() / 2f - content.getWidth() / 2f, 50);
        }

        // content.debug();

        Label lbLapTime = new Label("Lap time", new LabelStyle(
                oAssetsHandler.fontChco, Color.WHITE));

        Label lbLapTimeNum = new Label(gameScreen.lapTime, new LabelStyle(
                oAssetsHandler.fontChco, Color.WHITE));

        Label lbTimeLeft = new Label("Time left", new LabelStyle(
                oAssetsHandler.fontChco, Color.WHITE));

        lbTimeLeftNum = new Label("", new LabelStyle(oAssetsHandler.fontChco,
                Color.WHITE));

        Label lbCoins = new Label("Coins", new LabelStyle(oAssetsHandler.fontChco,
                Color.WHITE));

        lbCoinsNum = new Label("",
                new LabelStyle(oAssetsHandler.fontChco, Color.WHITE));

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
}
