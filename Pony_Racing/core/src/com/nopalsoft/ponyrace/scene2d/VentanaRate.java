package com.nopalsoft.ponyrace.scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.ponyrace.screens.Screens;

public class VentanaRate extends Ventana {

    public VentanaRate(Screens currentScreen) {
        super(currentScreen);
        setSize(450, 280);
        setY(90);
        setBackGround();

        Label lbTitle = new Label("Support this game", new LabelStyle(
                oAssets.fontGde, Color.WHITE));
        lbTitle.setPosition(getWidth() / 2f - lbTitle.getWidth() / 2f, 235);

        Label lbContenido = new Label(
                "Hello, thank you for playing Pony racing.\nHelp us to support this game. Just rate us at the app store.",
                game.oAssets.skin);
        lbContenido.setSize(getWidth() - 20, 170);
        lbContenido.setPosition(getWidth() / 2f - lbContenido.getWidth() / 2f,
                70);
        lbContenido.setWrap(true);

        TextButton btRate = new TextButton("Rate", game.oAssets.skin);
        btRate.getLabel().setWrap(true);
        btRate.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                game.reqHandler.showRater();
            }
        });

        TextButton btNotNow = new TextButton("Not now", game.oAssets.skin);
        btNotNow.getLabel().setWrap(true);
        btNotNow.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });

        Table tbBotones = new Table();
        tbBotones.setSize(getWidth() - 20, 40);
        tbBotones.setPosition(getWidth() / 2f - tbBotones.getWidth() / 2f, 10);

        tbBotones.defaults().uniform().expandX().center().fill().pad(10);
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
