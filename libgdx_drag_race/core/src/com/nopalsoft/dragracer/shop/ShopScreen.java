package com.nopalsoft.dragracer.shop;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.dragracer.Assets;
import com.nopalsoft.dragracer.MainStreet;
import com.nopalsoft.dragracer.Settings;
import com.nopalsoft.dragracer.screens.MainMenuScreen;
import com.nopalsoft.dragracer.screens.Screens;

public class ShopScreen extends Screens {

    Button btPersonajes, btPowerUps, btMonedas, btNoAds, btAtras;

    Label lbCoin;

    ScrollPane scroll;
    Table contenedor;

    public ShopScreen(final MainStreet game) {
        super(game);

        Label lbShop = new Label("Shop", Assets.labelStyleLarge);
        lbShop.setSize(135, 50);
        lbShop.setPosition(3, 747);

        Image imgCoin = new Image(Assets.coinFront);

        lbCoin = new Label("0", Assets.labelStyleLarge);
        lbCoin.setFontScale(.8f);

        Table tbScores = new Table();
        tbScores.setWidth(SCREEN_WIDTH);
        tbScores.setPosition(0, SCREEN_HEIGHT - lbCoin.getHeight() / 2);
        tbScores.padLeft(5).padRight(5);

        tbScores.add(lbCoin).right().expand().padRight(5);
        tbScores.add(imgCoin).right();

        Image separadorH = new Image(Assets.horizontalSeparatorDrawable);
        separadorH.setSize(SCREEN_WIDTH, 5);
        separadorH.setColor(Color.LIGHT_GRAY);
        separadorH.setPosition(0, 740);

        Image separadorV = new Image(Assets.verticalSeparatorDrawable);
        separadorV.setSize(5, 745);
        separadorV.setColor(Color.LIGHT_GRAY);
        separadorV.setPosition(90, 0);

        initButtons();

        contenedor = new Table();
        // contenedor.debug();
        scroll = new ScrollPane(contenedor, Assets.styleScrollPane);
        scroll.setSize(SCREEN_WIDTH - 95, (SCREEN_HEIGHT - 62));
        scroll.setPosition(95, 0);

        stage.addActor(tbScores);
        stage.addActor(lbShop);
        stage.addActor(separadorV);
        stage.addActor(separadorH);
        stage.addActor(btPersonajes);
        // stage.addActor(btPowerUps);
        stage.addActor(btMonedas);
        stage.addActor(btNoAds);
        stage.addActor(btAtras);
        stage.addActor(scroll);

        new PlayerSubMenu(game, contenedor);
    }

    private void initButtons() {
        btPersonajes = new Button(
                new TextureRegionDrawable(Assets.carTornado));
        btPersonajes.setSize(45, 65);
        btPersonajes.setPosition(23, 660);
        addPressEffect(btPersonajes);
        btPersonajes.addListener(new ClickListener() {
            public void clicked(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y) {
                new PlayerSubMenu(game, contenedor);
            }
        });

        btPowerUps = new Button(new TextureRegionDrawable(Assets.carTornado));
        btPowerUps.setSize(55, 55);
        btPowerUps.setPosition(17, 570);
        addPressEffect(btPowerUps);
        btPowerUps.addListener(new ClickListener() {
            public void clicked(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y) {
                // Settings.deleteAll();
                // new UpgradesSubMenu(game, contenedor);
            }
        });

        btMonedas = new Button(new TextureRegionDrawable(Assets.coinFront));
        btMonedas.setSize(55, 55);
        btMonedas.setPosition(17, 480);
        addPressEffect(btMonedas);
        btMonedas.addListener(new ClickListener() {
            public void clicked(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y) {
                new GetCoinsSubMenu(game, contenedor);
            }
        });

        btNoAds = new Button(new TextureRegionDrawable(Assets.buttonNoAds));
        btNoAds.setSize(55, 55);
        btNoAds.setPosition(17, 390);
        addPressEffect(btNoAds);
        btNoAds.addListener(new ClickListener() {
            public void clicked(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y) {
                new NoAdsSubMenu(game, contenedor);
            }
        });

        btAtras = new Button(new TextureRegionDrawable(Assets.buttonBack));
        btAtras.setSize(55, 55);
        btAtras.setPosition(17, 10);
        addPressEffect(btAtras);
        btAtras.addListener(new ClickListener() {
            public void clicked(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y) {
                changeScreenWithFadeOut(MainMenuScreen.class, game);
            }
        });
    }

    @Override
    public void draw(float delta) {

    }

    @Override
    public void update(float delta) {
        lbCoin.setText(Settings.coinsTotal + "");
    }

    @Override
    public boolean keyDown(int tecleada) {
        if (tecleada == Keys.BACK || tecleada == Keys.ESCAPE) {
            changeScreenWithFadeOut(MainMenuScreen.class, game);
            return true;
        }
        return false;
    }
}
