package com.nopalsoft.zombiedash.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.TextInputListener;
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
import com.badlogic.gdx.utils.I18NBundle;
import com.nopalsoft.zombiedash.Assets;
import com.nopalsoft.zombiedash.MainZombieDash;
import com.nopalsoft.zombiedash.Settings;

public class GetGemsSubMenu {

    int monedasLikeFacebook = 1500;
    // Comun
    TextButton btLikeFacebook;
    TextButton btInviteFacebook;

    // iOS
    TextButton btBuy5milCoins, btBuy15MilCoins, btBuy30MilCoins, btBuy50MilCoins;

    Table contenedor;
    MainZombieDash game;
    I18NBundle idiomas;
    String textBuy;

    public GetGemsSubMenu(final MainZombieDash game, Table contenedor) {
        this.game = game;
        this.contenedor = contenedor;
        idiomas = game.idiomas;
        contenedor.clear();

        textBuy = idiomas.get("buy");

        btLikeFacebook = new TextButton(idiomas.get("like_us"), Assets.styleTextButtonBuy);
        if (Settings.didLikeFacebook)
            btLikeFacebook = new TextButton(idiomas.get("visit_us"), Assets.styleTextButtonPurchased);
        addEfectoPress(btLikeFacebook);
        btLikeFacebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!Settings.didLikeFacebook) {

                    Settings.didLikeFacebook = true;
                    game.stage.addAction(Actions.sequence(Actions.delay(1), Actions.run(new Runnable() {

                        @Override
                        public void run() {
                            Settings.gemsTotal += monedasLikeFacebook;
                            btLikeFacebook.setText(idiomas.get("visit_us"));
                            btLikeFacebook.setStyle(Assets.styleTextButtonBuy);
                        }
                    })));
                }
                game.facebookHandler.showFacebook();
            }
        });

        btInviteFacebook = new TextButton(idiomas.get("invite"), Assets.styleTextButtonBuy);
        addEfectoPress(btInviteFacebook);
        btInviteFacebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.facebookHandler.facebookIsSignedIn()) {
                    Gdx.input.getTextInput(new TextInputListener() {
                        @Override
                        public void input(String text) {
                            game.facebookHandler.facebookInviteFriends(text);
                        }

                        @Override
                        public void canceled() {

                        }
                    }, idiomas.get("type_your_message"), "", "");

                } else
                    game.facebookHandler.facebookSignIn();
            }
        });

        btBuy5milCoins = new TextButton(textBuy, Assets.styleTextButtonBuy);
        addEfectoPress(btBuy5milCoins);
        btBuy5milCoins.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.reqHandler.buy5milCoins();
            }
        });

        btBuy15MilCoins = new TextButton(textBuy, Assets.styleTextButtonBuy);
        addEfectoPress(btBuy15MilCoins);
        btBuy15MilCoins.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.reqHandler.buy15milCoins();
            }
        });

        btBuy30MilCoins = new TextButton(textBuy, Assets.styleTextButtonBuy);
        addEfectoPress(btBuy30MilCoins);
        btBuy30MilCoins.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.reqHandler.buy30milCoins();
            }
        });

        btBuy50MilCoins = new TextButton(textBuy, Assets.styleTextButtonBuy);
        addEfectoPress(btBuy50MilCoins);
        btBuy50MilCoins.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.reqHandler.buy50milCoins();
            }
        });

        // Facebook Like
        String faceLikeDescription = idiomas.format("facebook_like_description", monedasLikeFacebook);
        String faceInviteDescription = idiomas.format("facebook_invite_description", Settings.NUM_GEMS_INVITE_FACEBOOK);


        // contenedor.row();
        contenedor.add(agregarPersonajeTabla(monedasLikeFacebook, Assets.btFacebook, faceLikeDescription, btLikeFacebook)).expandX().fill();
        contenedor.row();

        contenedor.add(agregarPersonajeTabla(Settings.NUM_GEMS_INVITE_FACEBOOK, Assets.btFacebook, faceInviteDescription, btInviteFacebook))
                .expandX().fill();
        contenedor.row();


        TextureRegionDrawable moneda = new TextureRegionDrawable(Assets.itemGem);
        // Venta de monedas

        // Comprar 5mil
        contenedor.add(agregarPersonajeTabla(5000, moneda, idiomas.get("gem_simple_pack"), btBuy5milCoins)).expandX().fill();
        contenedor.row();

        // Comprar 15mil
        contenedor.add(agregarPersonajeTabla(15000, moneda, idiomas.get("gem_super_pack"), btBuy15MilCoins)).expandX().fill();
        contenedor.row();

        contenedor.add(agregarPersonajeTabla(30000, moneda, idiomas.get("gem_mega_pack"), btBuy30MilCoins)).expandX().fill();
        contenedor.row();

        contenedor.add(agregarPersonajeTabla(50000, moneda, idiomas.get("gem_super_mega_pack"), btBuy50MilCoins)).expandX().fill();
        contenedor.row();
    }

    private Table agregarPersonajeTabla(int numMonedasToGet, TextureRegionDrawable imagen, String descripcion, TextButton boton) {

        Image moneda = new Image(Assets.itemGem);
        Image imgPersonaje = new Image(imagen);

        Table tbBarraTitulo = new Table();
        tbBarraTitulo.add(new Label(idiomas.format("get_num", numMonedasToGet), Assets.labelStyleChico)).left().padLeft(5);
        tbBarraTitulo.add(moneda).left().expandX().padLeft(5).size(20);

        Table tbDescrip = new Table();
        tbDescrip.add(imgPersonaje).left().pad(10).size(55, 45);
        Label lblDescripcion = new Label(descripcion, Assets.labelStyleChico);
        lblDescripcion.setWrap(true);
        lblDescripcion.setFontScale(.9f);
        tbDescrip.add(lblDescripcion).expand().fill().padLeft(5);

        Table tbContent = new Table();
        tbContent.defaults().padLeft(20).padRight(20);
        tbContent.setBackground(Assets.storeTableBackground);
        tbContent.add(tbBarraTitulo).expandX().fill().colspan(2).padTop(20);
        tbContent.row().colspan(2);
        tbContent.add(tbDescrip).expandX().fill();
        tbContent.row().colspan(2);

        tbContent.add(boton).right().padBottom(20).size(120, 45);

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
