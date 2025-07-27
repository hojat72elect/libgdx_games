package com.nopalsoft.ponyrace.scene2d;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.ponyrace.Settings;
import com.nopalsoft.ponyrace.game.GameScreenTileds;
import com.nopalsoft.ponyrace.screens.LoadingScreen;
import com.nopalsoft.ponyrace.screens.Screens;
import com.nopalsoft.ponyrace.screens.WorldMapTiledScreen;

public class VentanaNextLevel extends Ventana {

    GameScreenTileds gameScreen;

    Label lbCoinsNum, lbTimeLeftNum;

    public VentanaNextLevel(Screens currentScreen) {
        super(currentScreen);
        setSize(600, 370);
        setY(50);
        setBackGround();

        gameScreen = (GameScreenTileds) currentScreen;

        Image trofeo = new Image(oAssets.medallaPrimerLugar);
        trofeo.setScale(.9f);
        trofeo.setPosition(0, getHeight() / 2f - trofeo.getHeight() / 2f);
        addActor(trofeo);

        Image congratulations = new Image(oAssets.congratulations);
        congratulations.setPosition(getWidth() / 2f - congratulations.getWidth() / 2f, 300);
        addActor(congratulations);

        Table content = new Table();
        content.setSize(320, 200);
        content.setPosition(280, 70);
        // content.debug();

        Label lbLapTime = new Label("Lap time", new LabelStyle(oAssets.fontChco, Color.WHITE));

        Label lbLapTimeNum = new Label(gameScreen.lapTime, new LabelStyle(oAssets.fontChco, Color.WHITE));

        Label lbTimeLeft = new Label("Time left", new LabelStyle(oAssets.fontChco, Color.WHITE));

        lbTimeLeftNum = new Label("", new LabelStyle(oAssets.fontChco, Color.WHITE));

        Label lbCoins = new Label("Coins", new LabelStyle(oAssets.fontChco, Color.WHITE));

        lbCoinsNum = new Label("", new LabelStyle(oAssets.fontChco, Color.WHITE));

        Label lbShare = new Label("Share your time and get " + Settings.MONEDAS_REGALO_SHARE_FACEBOOK + " coins", new LabelStyle(oAssets.fontChco,
                Color.WHITE));
        lbShare.setAlignment(Align.center);
        lbShare.setWrap(true);

        TextButton btShare = new TextButton("Share", new TextButtonStyle(oAssets.btShareFacebookUp, oAssets.btShareFacebookDown, null,
                oAssets.fontChco));
        btShare.setPosition(390, 15);
        btShare.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                game.setScreen(new LoadingScreen(game, WorldMapTiledScreen.class));
                screen.dispose();
            }
        });

        content.row();
        content.add(lbLapTime).left();
        content.add(lbLapTimeNum).expand();

        content.row();
        content.add(lbTimeLeft).left();
        content.add(lbTimeLeftNum).expand();

        content.row();
        content.add(lbCoins).left();
        content.add(lbCoinsNum).expand();

        content.row().colspan(2);
        content.add(lbShare).expand().fill().center();

        addActor(content);
        addActor(btShare);

        if (Gdx.app.getType() == ApplicationType.iOS) {
            lbShare.setVisible(false);
            btShare.setVisible(false);
        }
    }

    @Override
    public void act(float delta) {
        lbCoinsNum.setText(gameScreen.stringMonedasRecolectadas);
        lbTimeLeftNum.setText(gameScreen.stringTiempoLeft);
        super.act(delta);
    }
}
