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

    int coinsForFacebookLike = 250;

    
    TextButton btLikeFacebook;
    TextButton btBuy50MilCoins;

    Table tableContainer;
    MainStreet game;

    public GetCoinsSubMenu(final MainStreet game, Table tableContainer) {
        this.game = game;
        this.tableContainer = tableContainer;
        tableContainer.clear();

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
                                    Settings.coinsTotal += coinsForFacebookLike;
                                    btLikeFacebook.setText("Visit us");
                                    btLikeFacebook
                                            .setStyle(Assets.styleTextButtonSelected);
                                }
                            })));
                }
            }
        });

        btBuy50MilCoins = new TextButton("Buy", Assets.styleTextButtonBuy);
        addEfectoPress(btBuy50MilCoins);


        // Facebook Like
        tableContainer.add(new Image(Assets.separadorHorizontal)).expandX().fill()
                .height(5);
        tableContainer.row();
        tableContainer
                .add(addPlayerTable(coinsForFacebookLike,
                        Assets.btFacebook, "Like us on facebook and get "
                                + coinsForFacebookLike + " coins",
                        btLikeFacebook)).expandX().fill();
        tableContainer.row();

        TextureRegionDrawable coinDrawable = new TextureRegionDrawable(
                Assets.coinFrente);

        tableContainer
                .add(addPlayerTable(
                        50000,
                        coinDrawable,
                        "Coin super mega pack. Get this pack and you will be racing in cash",
                        btBuy50MilCoins)).expandX().fill();
        tableContainer.row();
    }

    private Table addPlayerTable(int numCoinsToGet,
                                 TextureRegionDrawable imageDrawable, String description, TextButton button) {

        Image moneda = new Image(Assets.coinFrente);
        Image imgPersonaje = new Image(imageDrawable);

        Table tbBarraTitulo = new Table();
        tbBarraTitulo
                .add(new Label("Get " + numCoinsToGet, Assets.labelStyleChico))
                .left().padLeft(5);
        tbBarraTitulo.add(moneda).left().expandX().padLeft(5);

        Table tbDescrip = new Table();
        tbDescrip.add(imgPersonaje).left().pad(10).size(55, 55);
        Label lblDescripcion = new Label(description, Assets.labelStyleChico);
        lblDescripcion.setWrap(true);
        tbDescrip.add(lblDescripcion).expand().fill().padLeft(5);

        Table tbContent = new Table();
        tbContent.add(tbBarraTitulo).expandX().fill().colspan(2).padTop(8);
        tbContent.row().colspan(2);
        tbContent.add(tbDescrip).expandX().fill();
        tbContent.row().colspan(2);

        tbContent.add(button).right().padRight(10).size(120, 45);

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
