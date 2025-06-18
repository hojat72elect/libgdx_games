package com.nopalsoft.slamthebird.shop;

import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
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

public class UpgradesSubMenu {
    public final int MAX_LEVEL = 6;

    int priceLevel1 = 500;
    int priceLevel2 = 1000;
    int priceLevel3 = 2500;
    int priceLevel4 = 4000;
    int priceLevel5 = 5000;
    int priceLevel6 = 6000;

    TextButton buttonBoostTime, buttonFreeze, buttonCoins,
            buttonSuperJump, buttonInvincible;
    Label labelPriceTime, labelPriceFreeze, labelPriceCoins,
            labelPriceSuperJump, labelPriceInvincible;

    Image[] arrayBoostTime;
    Image[] arrayBoostFreeze;
    Image[] arrayBoostCoins;
    Image[] arrayBoostSuperJump;
    Image[] arrayBoostInvincible;

    Table tableContainer;
    SlamTheBirdGame game;

    public UpgradesSubMenu(SlamTheBirdGame game, Table tableContainer) {
        this.game = game;
        this.tableContainer = tableContainer;
        tableContainer.clear();

        arrayBoostTime = new Image[MAX_LEVEL];
        arrayBoostFreeze = new Image[MAX_LEVEL];
        arrayBoostCoins = new Image[MAX_LEVEL];
        arrayBoostSuperJump = new Image[MAX_LEVEL];
        arrayBoostInvincible = new Image[MAX_LEVEL];

        if (Settings.BOOST_DURATION < MAX_LEVEL)
            labelPriceTime = new Label(
                    getPriceForLevel(Settings.BOOST_DURATION) + "",
                    Assets.smallLabelStyle);

        if (Settings.BOOST_FREEZE < MAX_LEVEL)
            labelPriceFreeze = new Label(getPriceForLevel(Settings.BOOST_FREEZE)
                    + "", Assets.smallLabelStyle);

        if (Settings.BOOST_COINS < MAX_LEVEL)
            labelPriceCoins = new Label(
                    getPriceForLevel(Settings.BOOST_COINS) + "",
                    Assets.smallLabelStyle);

        if (Settings.BOOST_SUPER_JUMP < MAX_LEVEL)
            labelPriceSuperJump = new Label(
                    getPriceForLevel(Settings.BOOST_SUPER_JUMP) + "",
                    Assets.smallLabelStyle);

        if (Settings.BOOST_INVINCIBLE < MAX_LEVEL)
            labelPriceInvincible = new Label(
                    getPriceForLevel(Settings.BOOST_INVINCIBLE) + "",
                    Assets.smallLabelStyle);

        initializeButtons();

        tableContainer.add(new Image(Assets.horizontalSeparator)).expandX().fill()
                .height(5);
        tableContainer.row();

        // Upgrade BoostTime
        tableContainer
                .add(addPlayerTable("More power-ups",
                        labelPriceTime, Assets.boosts,
                        "Power-ups will appear more often in the game",
                        arrayBoostTime, buttonBoostTime)).expandX().fill();
        tableContainer.row();

        // Upgrade Super Jump
        tableContainer
                .add(addPlayerTable("Super jump", labelPriceSuperJump,
                        Assets.superJumpBoost,
                        "Super jump power up will last more time",
                        arrayBoostSuperJump, buttonSuperJump)).expandX()
                .fill();
        tableContainer.row();

        // Upgrade Ice
        tableContainer
                .add(addPlayerTable("Freeze enemies", labelPriceFreeze,
                        Assets.freezeBoost, "Enemies will last more time frozen",
                        arrayBoostFreeze, buttonFreeze)).expandX().fill();
        tableContainer.row();

        // Upgrade Invincibility
        tableContainer
                .add(addPlayerTable("Invencible", labelPriceInvincible,
                        Assets.invincibilityBoost,
                        "The invencible power-up will last more time",
                        arrayBoostInvincible, buttonInvincible)).expandX()
                .fill();
        tableContainer.row();

        // Upgrade Coins
        tableContainer
                .add(addPlayerTable(
                        "Coin rain",
                        labelPriceCoins,
                        Assets.coinRainBoost,
                        "More coins will fall down when the coin rain power-up is taken",
                        arrayBoostCoins, buttonCoins)).expandX().fill();
        tableContainer.row();

        setArrays();
    }

    private Table addPlayerTable(String title, Label labelPrice,
                                 AtlasRegion imageAtlasRegion, String description, Image[] levels,
                                 TextButton button) {

        Image imageCoin = new Image(Assets.coinsRegion);
        Image imagePlayer = new Image(imageAtlasRegion);

        if (labelPrice == null)
            imageCoin.setVisible(false);

        Table tableTitleBar = new Table();
        tableTitleBar.add(new Label(title, Assets.smallLabelStyle)).expandX()
                .left().padLeft(5);
        tableTitleBar.add(imageCoin).right();
        tableTitleBar.add(labelPrice).right().padRight(10);

        Table tableDescription = new Table();
        tableDescription.add(imagePlayer).left().pad(10).size(55, 45);
        Label labelDescription = new Label(description, Assets.smallLabelStyle);
        labelDescription.setWrap(true);
        tableDescription.add(labelDescription).expand().fill().padLeft(5);

        Table tableContent = new Table();
        tableContent.add(tableTitleBar).expandX().fill().colspan(2).padTop(8);
        tableContent.row().colspan(2);
        tableContent.add(tableDescription).expandX().fill();
        tableContent.row();

        Table auxiliaryTable = new Table();
        auxiliaryTable.defaults().padLeft(5);
        for (int i = 0; i < MAX_LEVEL; i++) {
            levels[i] = new Image(Assets.upgradeOff);
            auxiliaryTable.add(levels[i]).width(18).height(25);
        }

        tableContent.add(auxiliaryTable).center().expand();
        tableContent.add(button).right().padRight(10).size(120, 45);

        tableContent.row().colspan(2);
        tableContent.add(new Image(Assets.horizontalSeparator)).expandX().fill()
                .height(5).padTop(15);

        return tableContent;
    }

    private void initializeButtons() {
        buttonBoostTime = new TextButton("Upgrade",
                Assets.styleTextButtonSelected);
        if (Settings.BOOST_DURATION == MAX_LEVEL)
            buttonBoostTime.setVisible(false);
        addPressEffect(buttonBoostTime);
        buttonBoostTime.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.currentCoins >= getPriceForLevel(Settings.BOOST_DURATION)) {
                    Settings.currentCoins -= getPriceForLevel(Settings.BOOST_DURATION);
                    Settings.BOOST_DURATION++;
                    updateLabelPriceAndButton(Settings.BOOST_DURATION,
                            labelPriceTime, buttonBoostTime);
                    setArrays();
                }
            }
        });

        buttonSuperJump = new TextButton("Upgrade",
                Assets.styleTextButtonSelected);
        if (Settings.BOOST_SUPER_JUMP == MAX_LEVEL)
            buttonSuperJump.setVisible(false);
        addPressEffect(buttonSuperJump);
        buttonSuperJump.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.currentCoins >= getPriceForLevel(Settings.BOOST_SUPER_JUMP)) {
                    Settings.currentCoins -= getPriceForLevel(Settings.BOOST_SUPER_JUMP);
                    Settings.BOOST_SUPER_JUMP++;
                    updateLabelPriceAndButton(Settings.BOOST_SUPER_JUMP,
                            labelPriceSuperJump, buttonSuperJump);
                    setArrays();
                }
            }
        });

        buttonFreeze = new TextButton("Upgrade", Assets.styleTextButtonSelected);
        if (Settings.BOOST_FREEZE == MAX_LEVEL)
            buttonFreeze.setVisible(false);

        addPressEffect(buttonFreeze);
        buttonFreeze.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.currentCoins >= getPriceForLevel(Settings.BOOST_FREEZE)) {
                    Settings.currentCoins -= getPriceForLevel(Settings.BOOST_FREEZE);
                    Settings.BOOST_FREEZE++;
                    updateLabelPriceAndButton(Settings.BOOST_FREEZE,
                            labelPriceFreeze, buttonFreeze);
                    setArrays();
                }
            }
        });

        buttonInvincible = new TextButton("Upgrade",
                Assets.styleTextButtonSelected);
        if (Settings.BOOST_INVINCIBLE == MAX_LEVEL)
            buttonInvincible.setVisible(false);

        addPressEffect(buttonInvincible);
        buttonInvincible.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.currentCoins >= getPriceForLevel(Settings.BOOST_INVINCIBLE)) {
                    Settings.currentCoins -= getPriceForLevel(Settings.BOOST_INVINCIBLE);
                    Settings.BOOST_INVINCIBLE++;
                    updateLabelPriceAndButton(Settings.BOOST_INVINCIBLE,
                            labelPriceInvincible, buttonInvincible);
                    setArrays();
                }
            }
        });

        buttonCoins = new TextButton("Upgrade",
                Assets.styleTextButtonSelected);
        if (Settings.BOOST_COINS == MAX_LEVEL)
            buttonCoins.setVisible(false);

        addPressEffect(buttonCoins);
        buttonCoins.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Settings.currentCoins >= getPriceForLevel(Settings.BOOST_COINS)) {
                    Settings.currentCoins -= getPriceForLevel(Settings.BOOST_COINS);
                    Settings.BOOST_COINS++;
                    updateLabelPriceAndButton(Settings.BOOST_COINS,
                            labelPriceCoins, buttonCoins);
                    setArrays();
                }
            }
        });
    }

    private void setArrays() {
        for (int i = 0; i < Settings.BOOST_DURATION; i++) {
            arrayBoostTime[i].setDrawable(new TextureRegionDrawable(
                    Assets.upgradeOn));
        }

        for (int i = 0; i < Settings.BOOST_FREEZE; i++) {
            arrayBoostFreeze[i].setDrawable(new TextureRegionDrawable(
                    Assets.upgradeOn));
        }

        for (int i = 0; i < Settings.BOOST_INVINCIBLE; i++) {
            arrayBoostInvincible[i].setDrawable(new TextureRegionDrawable(
                    Assets.upgradeOn));
        }

        for (int i = 0; i < Settings.BOOST_SUPER_JUMP; i++) {
            arrayBoostSuperJump[i].setDrawable(new TextureRegionDrawable(
                    Assets.upgradeOn));
        }

        for (int i = 0; i < Settings.BOOST_COINS; i++) {
            arrayBoostCoins[i].setDrawable(new TextureRegionDrawable(
                    Assets.upgradeOn));
        }
    }

    private int getPriceForLevel(int level) {
        return switch (level) {
            case 0 -> priceLevel1;
            case 1 -> priceLevel2;
            case 2 -> priceLevel3;
            case 3 -> priceLevel4;
            case 4 -> priceLevel5;
            default -> priceLevel6;
        };
    }

    private void updateLabelPriceAndButton(int nivel, Label label,
                                           TextButton boton) {
        if (nivel < MAX_LEVEL) {
            label.setText(getPriceForLevel(nivel) + "");
        } else {
            label.setVisible(false);
            boton.setVisible(false);
        }
    }

    protected void addPressEffect(final Actor actor) {
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
