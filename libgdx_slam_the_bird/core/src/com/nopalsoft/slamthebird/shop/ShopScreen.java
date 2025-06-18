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
import com.nopalsoft.slamthebird.Settings;
import com.nopalsoft.slamthebird.SlamTheBirdGame;
import com.nopalsoft.slamthebird.game.GameScreen;
import com.nopalsoft.slamthebird.screens.BaseScreen;

public class ShopScreen extends BaseScreen {

    Button buttonPlayers, buttonPowerUps, buttonCoins, buttonNoAds, buttonBack;

    ScrollPane scrollPane;
    Table tableContainer;

    public ShopScreen(final SlamTheBirdGame game) {
        super(game);
        Image shop = new Image(Assets.shop);
        shop.setSize(135, 50);
        shop.setPosition(3, 747);

        Image horizontalSeparatorImage = new Image(Assets.horizontalSeparator);
        horizontalSeparatorImage.setSize(SCREEN_WIDTH, 5);
        horizontalSeparatorImage.setColor(Color.LIGHT_GRAY);
        horizontalSeparatorImage.setPosition(0, 740);

        Image verticalSeparatorImage = new Image(Assets.verticalSeparator);
        verticalSeparatorImage.setSize(5, 745);
        verticalSeparatorImage.setColor(Color.LIGHT_GRAY);
        verticalSeparatorImage.setPosition(90, 0);

        initButtons();

        tableContainer = new Table();

        scrollPane = new ScrollPane(tableContainer, Assets.styleScrollPane);
        scrollPane.setSize(SCREEN_WIDTH - 95, (SCREEN_HEIGHT - 62));
        scrollPane.setPosition(95, 0);

        stage.addActor(shop);
        stage.addActor(verticalSeparatorImage);
        stage.addActor(horizontalSeparatorImage);
        stage.addActor(buttonPlayers);
        stage.addActor(buttonPowerUps);
        stage.addActor(buttonCoins);
        stage.addActor(buttonNoAds);
        stage.addActor(buttonBack);
        stage.addActor(scrollPane);

        new PlayerSkinsSubMenu(game, tableContainer);

        buttonCoins.remove();
    }

    private void initButtons() {
        buttonPlayers = new Button(new TextureRegionDrawable(Assets.defaultPlayerSkin));
        buttonPlayers.setSize(55, 55);
        buttonPlayers.setPosition(17, 660);
        addPressEffect(buttonPlayers);
        buttonPlayers.addListener(new ClickListener() {
            public void clicked(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y) {
                new PlayerSkinsSubMenu(game, tableContainer);
            }
        });

        buttonPowerUps = new Button(new TextureRegionDrawable(Assets.boosts));
        buttonPowerUps.setSize(55, 55);
        buttonPowerUps.setPosition(17, 570);
        addPressEffect(buttonPowerUps);
        buttonPowerUps.addListener(new ClickListener() {
            public void clicked(
                    com.badlogic.gdx.scenes.scene2d.InputEvent event, float x,
                    float y) {
                new UpgradesSubMenu(game, tableContainer);
            }
        });

        buttonCoins = new Button(new TextureRegionDrawable(Assets.coinsRegion));
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
                changeScreenWithFadeOut(GameScreen.class, game);
            }
        });
    }

    @Override
    public void draw(float delta) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(Assets.coinsRegion, 449, 764, 30, 34);
        drawSmallScoreRightAligned(445, 764, Settings.currentCoins);
        batch.end();
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
