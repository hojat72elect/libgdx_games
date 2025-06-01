package com.nopalsoft.dragracer.shop;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.dragracer.Assets;
import com.nopalsoft.dragracer.MainStreet;
import com.nopalsoft.dragracer.Settings;

public class GetCoinsSubMenu {

    int monedasLikeFacebook = 250;

    // Comun
    TextButton btLikeFacebook;

    // iOS
    TextButton btBuy50MilCoins;

    Table contenedor;
    MainStreet game;

    public GetCoinsSubMenu(final MainStreet game, Table contenedor) {
        this.game = game;
        this.contenedor = contenedor;
        contenedor.clear();

        btLikeFacebook = new TextButton("Like us", Assets.styleTextButtonBuy);
        if (Settings.didLikeFacebook)
            btLikeFacebook = new TextButton("Visit Us",
                    Assets.styleTextButtonSelected);
        addEfectoPress(btLikeFacebook);
        btLikeFacebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!Settings.didLikeFacebook) {

                    Settings.didLikeFacebook = true;
                    game.stage.addAction(Actions.sequence(Actions.delay(1),
                            Actions.run(new Runnable() {

                                @Override
                                public void run() {
                                    Settings.coinsTotal += monedasLikeFacebook;
                                    btLikeFacebook.setText("Visit us");
                                    btLikeFacebook
                                            .setStyle(Assets.styleTextButtonSelected);
                                }
                            })));
                }
                game.reqHandler.showFacebook();
            }
        });

        btBuy50MilCoins = new TextButton("Buy", Assets.styleTextButtonBuy);
        addEfectoPress(btBuy50MilCoins);
        btBuy50MilCoins.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.reqHandler.buy50milCoins();
            }
        });

        // Facebook Like
        contenedor.add(new Image(Assets.separadorHorizontal)).expandX().fill()
                .height(5);
        contenedor.row();
        contenedor
                .add(agregarPersonajeTabla(monedasLikeFacebook,
                        Assets.btFacebook, "Like us on facebook and get "
                                + monedasLikeFacebook + " coins",
                        btLikeFacebook)).expandX().fill();
        contenedor.row();

        TextureRegionDrawable moneda = new TextureRegionDrawable(
                Assets.coinFrente);

        contenedor
                .add(agregarPersonajeTabla(
                        50000,
                        moneda,
                        "Coin super mega pack. Get this pack and you will be racing in cash",
                        btBuy50MilCoins)).expandX().fill();
        contenedor.row();
    }

    private Table agregarPersonajeTabla(int numMonedasToGet,
                                        TextureRegionDrawable imagen, String descripcion, TextButton boton) {

        Image moneda = new Image(Assets.coinFrente);
        Image imgPersonaje = new Image(imagen);

        Table tbBarraTitulo = new Table();
        tbBarraTitulo
                .add(new Label("Get " + numMonedasToGet, Assets.labelStyleChico))
                .left().padLeft(5);
        tbBarraTitulo.add(moneda).left().expandX().padLeft(5);

        Table tbDescrip = new Table();
        tbDescrip.add(imgPersonaje).left().pad(10).size(55, 55);
        Label lblDescripcion = new Label(descripcion, Assets.labelStyleChico);
        lblDescripcion.setWrap(true);
        tbDescrip.add(lblDescripcion).expand().fill().padLeft(5);

        Table tbContent = new Table();
        tbContent.add(tbBarraTitulo).expandX().fill().colspan(2).padTop(8);
        tbContent.row().colspan(2);
        tbContent.add(tbDescrip).expandX().fill();
        tbContent.row().colspan(2);

        tbContent.add(boton).right().padRight(10).size(120, 45);

        tbContent.row().colspan(2);
        tbContent.add(new Image(Assets.separadorHorizontal)).expandX().fill()
                .height(5).padTop(15);

        return tbContent;

    }

    protected void addEfectoPress(final Actor actor) {
        actor.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                actor.setPosition(actor.getX(), actor.getY() - 3);
                event.stop();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                actor.setPosition(actor.getX(), actor.getY() + 3);
            }
        });
    }
}
