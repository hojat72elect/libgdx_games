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

    Button buttonPlayers, buttonPowerUps, buttonCoins, buttonNoAds, buttonBack;

    Label labelCoin;

    ScrollPane scroll;
    Table tableContainer;

    public ShopScreen(final MainStreet game) {
        super(game);

        Label labelShop = new Label("Shop", Assets.labelStyleLarge);
        labelShop.setSize(135, 50);
        labelShop.setPosition(3, 747);

        Image coinImage = new Image(Assets.coinFront);

        labelCoin = new Label("0", Assets.labelStyleLarge);
        labelCoin.setFontScale(.8f);

        Table tbScores = new Table();
        tbScores.setWidth(SCREEN_WIDTH);
        tbScores.setPosition(0, SCREEN_HEIGHT - labelCoin.getHeight() / 2);
        tbScores.padLeft(5).padRight(5);

        tbScores.add(labelCoin).right().expand().padRight(5);
        tbScores.add(coinImage).right();

        Image horizontalSeparatorImage = new Image(Assets.horizontalSeparatorDrawable);
        horizontalSeparatorImage.setSize(SCREEN_WIDTH, 5);
        horizontalSeparatorImage.setColor(Color.LIGHT_GRAY);
        horizontalSeparatorImage.setPosition(0, 740);

        Image verticalSeparatorImage = new Image(Assets.verticalSeparatorDrawable);
        verticalSeparatorImage.setSize(5, 745);
        verticalSeparatorImage.setColor(Color.LIGHT_GRAY);
        verticalSeparatorImage.setPosition(90, 0);

        initializeButtons();

        tableContainer = new Table();
        scroll = new ScrollPane(tableContainer, Assets.styleScrollPane);
        scroll.setSize(SCREEN_WIDTH - 95, (SCREEN_HEIGHT - 62));
        scroll.setPosition(95, 0);

        stage.addActor(tbScores);
        stage.addActor(labelShop);
        stage.addActor(verticalSeparatorImage);
        stage.addActor(horizontalSeparatorImage);
        stage.addActor(buttonPlayers);
        // stage.addActor(btPowerUps);
        stage.addActor(buttonCoins);
        stage.addActor(buttonNoAds);
        stage.addActor(buttonBack);
        stage.addActor(scroll);

        new PlayerSubMenu(game, tableContainer);
    }

    private void initializeButtons() {
        buttonPlayers = new Button(
                new TextureRegionDrawable(Assets.carTornado));
        buttonPlayers.setSize(45, 65);
        buttonPlayers.setPosition(23, 660);
        addPressEffect(buttonPlayers);
        buttonPlayers.addListener(new ClickListener() {
            public void clicked(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y) {
                new PlayerSubMenu(game, tableContainer);
            }
        });

        buttonPowerUps = new Button(new TextureRegionDrawable(Assets.carTornado));
        buttonPowerUps.setSize(55, 55);
        buttonPowerUps.setPosition(17, 570);
        addPressEffect(buttonPowerUps);

        buttonCoins = new Button(new TextureRegionDrawable(Assets.coinFront));
        buttonCoins.setSize(55, 55);
        buttonCoins.setPosition(17, 480);
        addPressEffect(buttonCoins);
        buttonCoins.addListener(new ClickListener() {
            public void clicked(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y) {
                new GetCoinsSubMenu(game, tableContainer);
            }
        });

        buttonNoAds = new Button(new TextureRegionDrawable(Assets.buttonNoAds));
        buttonNoAds.setSize(55, 55);
        buttonNoAds.setPosition(17, 390);
        addPressEffect(buttonNoAds);
        buttonNoAds.addListener(new ClickListener() {
            public void clicked(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y) {
                new NoAdsSubMenu(game, tableContainer);
            }
        });

        buttonBack = new Button(new TextureRegionDrawable(Assets.buttonBack));
        buttonBack.setSize(55, 55);
        buttonBack.setPosition(17, 10);
        addPressEffect(buttonBack);
        buttonBack.addListener(new ClickListener() {
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
        labelCoin.setText(Settings.coinsTotal + "");
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
            changeScreenWithFadeOut(MainMenuScreen.class, game);
            return true;
        }
        return false;
    }
}
