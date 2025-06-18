package com.nopalsoft.slamthebird.shop;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.Settings;
import com.nopalsoft.slamthebird.SlamTheBirdGame;

public class NoAdsSubMenu {

    int priceNoAds = 35000;

    TextButton buttonNoAds;
    Label labelNoAds;

    Table tableContainer;
    SlamTheBirdGame game;

    public NoAdsSubMenu(final SlamTheBirdGame game, Table tableContainer) {
        this.game = game;
        this.tableContainer = tableContainer;
        tableContainer.clear();

        if (!Settings.didBuyNoAds)
            labelNoAds = new Label(priceNoAds + "", Assets.smallLabelStyle);

        buttonNoAds = new TextButton("Buy", Assets.styleTextButtonBuy);
        if (Settings.didBuyNoAds)
            buttonNoAds.setVisible(false);
        addEfectoPress(buttonNoAds);
        buttonNoAds.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.currentCoins >= priceNoAds) {
                    Settings.currentCoins -= priceNoAds;
                    Settings.didBuyNoAds = true;
                    labelNoAds.setVisible(false);
                    buttonNoAds.setVisible(false);
                }
            }
        });

        tableContainer.add(new Image(Assets.horizontalSeparator)).expandX().fill().height(5);
        tableContainer.row();
        tableContainer
                .add(agregarPersonajeTabla(labelNoAds, Assets.buttonNoAds,
                        buttonNoAds)).expandX().fill();
        tableContainer.row();
    }

    private Table agregarPersonajeTabla(Label lblPrecio, TextureRegionDrawable imagen,
                                        TextButton boton) {

        Image moneda = new Image(Assets.coinsRegion);
        Image imgPersonaje = new Image(imagen);

        if (lblPrecio == null)
            moneda.setVisible(false);

        Table tbBarraTitulo = new Table();
        tbBarraTitulo.add(new Label("No more ads", Assets.smallLabelStyle)).expandX().left().padLeft(5);
        tbBarraTitulo.add(moneda).right();
        tbBarraTitulo.add(lblPrecio).right().padRight(10);

        Table tbDescrip = new Table();
        tbDescrip.add(imgPersonaje).left().pad(10).size(55, 45);
        Label lblDescripcion = new Label("Buy it and no more ads will apper in the app", Assets.smallLabelStyle);
        lblDescripcion.setWrap(true);
        tbDescrip.add(lblDescripcion).expand().fill().padLeft(5);

        Table tbContent = new Table();
        tbContent.add(tbBarraTitulo).expandX().fill().colspan(2).padTop(8);
        tbContent.row().colspan(2);
        tbContent.add(tbDescrip).expandX().fill();
        tbContent.row().colspan(2);

        tbContent.add(boton).right().padRight(10).size(120, 45);

        tbContent.row().colspan(2);
        tbContent.add(new Image(Assets.horizontalSeparator)).expandX().fill().height(5).padTop(15);

        return tbContent;
    }

    protected void addEfectoPress(final Actor actor) {
        actor.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                actor.setPosition(actor.getX(), actor.getY() - 3);
                event.stop();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                actor.setPosition(actor.getX(), actor.getY() + 3);
            }
        });
    }
}
