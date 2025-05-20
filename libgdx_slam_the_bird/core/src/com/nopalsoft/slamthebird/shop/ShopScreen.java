package com.nopalsoft.slamthebird.shop;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.MainSlamBird;
import com.nopalsoft.slamthebird.Settings;
import com.nopalsoft.slamthebird.game.GameScreen;
import com.nopalsoft.slamthebird.screens.Screens;

public class ShopScreen extends Screens {

    Button btPersonajes, btPowerUps, btMonedas, btNoAds, btAtras;

    ScrollPane scroll;
    Table contenedor;

    public ShopScreen(final MainSlamBird game) {
        super(game);
        Image shop = new Image(Assets.shop);
        shop.setSize(135, 50);
        shop.setPosition(3, 747);

        Image separadorH = new Image(Assets.separadorHorizontal);
        separadorH.setSize(SCREEN_WIDTH, 5);
        separadorH.setColor(Color.LIGHT_GRAY);
        separadorH.setPosition(0, 740);

        Image separadorV = new Image(Assets.separadorVertical);
        separadorV.setSize(5, 745);
        separadorV.setColor(Color.LIGHT_GRAY);
        separadorV.setPosition(90, 0);

        initButtons();

        contenedor = new Table();
        // contenedor.debug();
        scroll = new ScrollPane(contenedor, Assets.styleScrollPane);
        scroll.setSize(SCREEN_WIDTH - 95, (SCREEN_HEIGHT - 62));
        scroll.setPosition(95, 0);

        stage.addActor(shop);
        stage.addActor(separadorV);
        stage.addActor(separadorH);
        stage.addActor(btPersonajes);
        stage.addActor(btPowerUps);
        stage.addActor(btMonedas);
        stage.addActor(btNoAds);
        stage.addActor(btAtras);
        stage.addActor(scroll);

        new PersonajesSubMenu(game, contenedor);

        game.reqHandler.hideAdBanner();

        btMonedas.remove();

    }

    private void initButtons() {
        btPersonajes = new Button(new TextureRegionDrawable(
                Assets.personajeShopDefault));
        btPersonajes.setSize(55, 55);
        btPersonajes.setPosition(17, 660);
        addEfectoPress(btPersonajes);
        btPersonajes.addListener(new ClickListener() {
            public void clicked(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y) {
                new PersonajesSubMenu(game, contenedor);
            }

            ;
        });

        btPowerUps = new Button(new TextureRegionDrawable(Assets.boosts));
        btPowerUps.setSize(55, 55);
        btPowerUps.setPosition(17, 570);
        addEfectoPress(btPowerUps);
        btPowerUps.addListener(new ClickListener() {
            public void clicked(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y) {
                new UpgradesSubMenu(game, contenedor);
            }

            ;
        });

        btMonedas = new Button(new TextureRegionDrawable(Assets.moneda));
        btMonedas.setSize(55, 55);
        btMonedas.setPosition(17, 480);
        addEfectoPress(btMonedas);
        btMonedas.addListener(new ClickListener() {
            public void clicked(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y) {
                new GetCoinsSubMenu(game, contenedor);
            }

            ;
        });

        btNoAds = new Button(new TextureRegionDrawable(Assets.btNoAds));
        btNoAds.setSize(55, 55);
        btNoAds.setPosition(17, 390);
        addEfectoPress(btNoAds);
        btNoAds.addListener(new ClickListener() {
            public void clicked(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y) {
                new NoAdsSubMenu(game, contenedor);
            }

            ;
        });

        btAtras = new Button(new TextureRegionDrawable(Assets.btAtras));
        btAtras.setSize(55, 55);
        btAtras.setPosition(17, 10);
        addEfectoPress(btAtras);
        btAtras.addListener(new ClickListener() {
            public void clicked(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y) {
                changeScreenWithFadeOut(GameScreen.class, game);
            }

            ;
        });

    }

    @Override
    public void draw(float delta) {
        oCam.update();
        batcher.setProjectionMatrix(oCam.combined);

        batcher.begin();
        batcher.draw(Assets.moneda, 449, 764, 30, 34);
        drawPuntuacionChicoOrigenDerecha(445, 764, Settings.monedasActuales);
        batcher.end();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public boolean keyDown(int tecleada) {
        if (tecleada == Keys.BACK || tecleada == Keys.ESCAPE) {
            changeScreenWithFadeOut(GameScreen.class, game);
            return true;
        }
        return false;
    }

}
