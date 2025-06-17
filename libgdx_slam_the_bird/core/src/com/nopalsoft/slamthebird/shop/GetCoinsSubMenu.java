package com.nopalsoft.slamthebird.shop;

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
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.Settings;
import com.nopalsoft.slamthebird.SlamTheBirdGame;

public class GetCoinsSubMenu {

    int facebookLikeCoinBonus = 1500;

    TextButton buttonLikeFacebook;
    TextButton buttonBuy5MCoins, buttonBuy15MCoins, buttonBuy30MCoins,
            buttonBuy50MCoins;

    Table tableContainer;
    SlamTheBirdGame game;

    public GetCoinsSubMenu(final SlamTheBirdGame game, Table tableContainer) {
        this.game = game;
        this.tableContainer = tableContainer;
        tableContainer.clear();

        buttonLikeFacebook = new TextButton("Like us", Assets.styleTextButtonBuy);
        if (Settings.didLikeFacebook)
            buttonLikeFacebook = new TextButton("Visit Us",
                    Assets.styleTextButtonSelected);
        addEfectoPress(buttonLikeFacebook);
        buttonLikeFacebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!Settings.didLikeFacebook) {

                    Settings.didLikeFacebook = true;
                    game.stage.addAction(Actions.sequence(Actions.delay(1),
                            Actions.run(() -> {
                                Settings.currentCoins += facebookLikeCoinBonus;
                                buttonLikeFacebook.setText("Visit us");
                                buttonLikeFacebook
                                        .setStyle(Assets.styleTextButtonSelected);
                            })));
                }
            }
        });

        buttonBuy5MCoins = new TextButton("Buy", Assets.styleTextButtonBuy);
        addEfectoPress(buttonBuy5MCoins);

        buttonBuy15MCoins = new TextButton("Buy", Assets.styleTextButtonBuy);
        addEfectoPress(buttonBuy15MCoins);

        buttonBuy30MCoins = new TextButton("Buy", Assets.styleTextButtonBuy);
        addEfectoPress(buttonBuy30MCoins);

        buttonBuy50MCoins = new TextButton("Buy", Assets.styleTextButtonBuy);
        addEfectoPress(buttonBuy50MCoins);

        // Facebook Like
        tableContainer.add(new Image(Assets.separadorHorizontal)).expandX().fill()
                .height(5);
        tableContainer.row();
        tableContainer
                .add(agregarPersonajeTabla(facebookLikeCoinBonus,
                        Assets.btFacebook, "Like us on facebook and get "
                                + facebookLikeCoinBonus + " coins",
                        buttonLikeFacebook)).expandX().fill();
        tableContainer.row();

        TextureRegionDrawable moneda = new TextureRegionDrawable(Assets.moneda);
        // Venta de monedas


        // Comprar 5mil
        tableContainer
                .add(agregarPersonajeTabla(
                        5000,
                        moneda,
                        "Coin simple pack. A quick way to buy simple upgrades",
                        buttonBuy5MCoins)).expandX().fill();
        tableContainer.row();

        // Comprar 15mil
        tableContainer
                .add(agregarPersonajeTabla(
                        15000,
                        moneda,
                        "Coin super pack. Get some cash for upgrades and characters",
                        buttonBuy15MCoins)).expandX().fill();
        tableContainer.row();

        tableContainer
                .add(agregarPersonajeTabla(
                        30000,
                        moneda,
                        "Coin mega pack. You can buy a lot of characters and upgrades",
                        buttonBuy30MCoins)).expandX().fill();
        tableContainer.row();

        tableContainer
                .add(agregarPersonajeTabla(
                        50000,
                        moneda,
                        "Coin super mega pack. Get this pack and you will be slamming in cash",
                        buttonBuy50MCoins)).expandX().fill();
        tableContainer.row();
    }

    private Table agregarPersonajeTabla(int numMonedasToGet,
                                        TextureRegionDrawable imagen, String descripcion, TextButton boton) {

        Image moneda = new Image(Assets.moneda);
        Image imgPersonaje = new Image(imagen);

        Table tbBarraTitulo = new Table();
        tbBarraTitulo
                .add(new Label("Get " + numMonedasToGet, Assets.styleLabelChico))
                .left().padLeft(5);
        tbBarraTitulo.add(moneda).left().expandX().padLeft(5);

        Table tbDescrip = new Table();
        tbDescrip.add(imgPersonaje).left().pad(10).size(55, 45);
        Label lblDescripcion = new Label(descripcion, Assets.styleLabelChico);
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
