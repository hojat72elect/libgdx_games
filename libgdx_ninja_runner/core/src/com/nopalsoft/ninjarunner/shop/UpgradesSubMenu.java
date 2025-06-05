package com.nopalsoft.ninjarunner.shop;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.NinjaRunnerGame;
import com.nopalsoft.ninjarunner.Settings;

public class UpgradesSubMenu {

    public final int MAX_LEVEL = 6;

    final int PRICE_LEVEL_1 = 500;
    final int PRICE_LEVEL_2 = 1000;
    final int PRICE_LEVEL_3 = 1750;
    final int PRICE_LEVEL_4 = 2500;
    final int PRICE_LEVEL_5 = 3000;

    Button buttonUpgradeMagnet;
    Button buttonUpgradeLife;
    Button buttonUpgradeEnergy;
    Button buttonUpgradeCoins;
    Button buttonUpgradeTreasureChest;

    Label labelPriceMagnet;
    Label labelPriceLife;
    Label labelPriceEnergy;
    Label labelPriceCoins;
    Label labelPriceTreasureChest;

    Image[] arrayMagnet;
    Image[] arrayLife;
    Image[] arrayEnergy;
    Image[] arrayCoin;
    Image[] arrayTreasureChest;

    Table tableContainer;
    I18NBundle languages;

    public UpgradesSubMenu(Table tableContainer, NinjaRunnerGame game) {
        this.tableContainer = tableContainer;
        languages = game.languages;
        tableContainer.clear();

        arrayMagnet = new Image[MAX_LEVEL];
        arrayLife = new Image[MAX_LEVEL];
        arrayEnergy = new Image[MAX_LEVEL];
        arrayCoin = new Image[MAX_LEVEL];
        arrayTreasureChest = new Image[MAX_LEVEL];

        if (Settings.LEVEL_MAGNET < MAX_LEVEL)
            labelPriceMagnet = new Label(calculatePrice(Settings.LEVEL_MAGNET) + "", Assets.labelStyleSmall);

        if (Settings.LEVEL_LIFE < MAX_LEVEL)
            labelPriceLife = new Label(calculatePrice(Settings.LEVEL_LIFE) + "", Assets.labelStyleSmall);

        if (Settings.LEVEL_ENERGY < MAX_LEVEL)
            labelPriceEnergy = new Label(calculatePrice(Settings.LEVEL_ENERGY) + "", Assets.labelStyleSmall);

        if (Settings.LEVEL_COINS < MAX_LEVEL)
            labelPriceCoins = new Label(calculatePrice(Settings.LEVEL_COINS) + "", Assets.labelStyleSmall);

        if (Settings.LEVEL_TREASURE_CHEST < MAX_LEVEL)
            labelPriceTreasureChest = new Label(calculatePrice(Settings.LEVEL_TREASURE_CHEST) + "", Assets.labelStyleSmall);

        initializeButtons();

        tableContainer.defaults().expand().fill().padLeft(10).padRight(20).padBottom(10);

        // Upgrade MAGNET
        tableContainer.add(
                addPlayerTable(languages.get("upgradeMagnet"), labelPriceMagnet, Assets.magnet, 35, 35, languages.get("magnetDescription"),
                        arrayMagnet, buttonUpgradeMagnet)).row();
        tableContainer.add(
                        addPlayerTable("Upgrade Life", labelPriceLife, Assets.hearth, 38, 29, languages.get("bombDescription"), arrayLife, buttonUpgradeLife))
                .row();
        tableContainer.add(
                addPlayerTable("Upgrade Eneergy", labelPriceEnergy, Assets.energy, 25, 35, languages.get("bombDescription"), arrayEnergy,
                        buttonUpgradeEnergy)).row();
        tableContainer.add(
                addPlayerTable("Upgrade coins", labelPriceCoins, Assets.coinAnimation.getKeyFrame(0), 35, 35, languages.get("bombDescription"), arrayCoin,
                        buttonUpgradeCoins)).row();
        tableContainer.add(
                addPlayerTable(languages.get("upgradeTreasureChest"), labelPriceTreasureChest, Assets.magnet, 35, 35,
                        languages.get("treasureChestDescription"), arrayTreasureChest, buttonUpgradeTreasureChest)).row();

        setArrays();
    }

    private Table addPlayerTable(String title, Label labelPrice, Sprite image, float imageWidth, float imageHeight, String description,
                                 Image[] arrayLevel, Button buttonUpgrade) {

        Image imageCoin = new Image(Assets.coinAnimation.getKeyFrame(0));
        Image imagePlayer = new Image(image);

        if (labelPrice == null)
            imageCoin.setVisible(false);

        Table tableTitleBar = new Table();
        tableTitleBar.add(new Label(title, Assets.labelStyleSmall)).expandX().left();
        tableTitleBar.add(imageCoin).right().size(20);
        tableTitleBar.add(labelPrice).right().padRight(10);

        Table tableContent = new Table();
        tableContent.setBackground(Assets.backgroundItemShop);
        tableContent.pad(5);

        tableContent.add(tableTitleBar).expandX().fill().colspan(2);
        tableContent.row();

        tableContent.add(imagePlayer).size(imageWidth, imageHeight);
        Label labelDescription = new Label(description, Assets.labelStyleSmall);
        labelDescription.setWrap(true);
        tableContent.add(labelDescription).expand().fill();

        Table auxiliaryTable = new Table();
        auxiliaryTable.setBackground(Assets.backgroundUpgradeBar);
        auxiliaryTable.pad(5);
        auxiliaryTable.defaults().padLeft(5);
        for (int i = 0; i < MAX_LEVEL; i++) {
            arrayLevel[i] = new Image();
            auxiliaryTable.add(arrayLevel[i]).size(15);
        }

        tableContent.row();
        tableContent.add(auxiliaryTable);
        tableContent.add(buttonUpgrade).left().size(40);

        return tableContent;
    }

    private void initializeButtons() {
        buttonUpgradeMagnet = new Button(Assets.styleButtonUpgrade);
        buttonUpgradeMagnet.setUserObject(Settings.LEVEL_MAGNET);
        initializeButton(buttonUpgradeMagnet, labelPriceMagnet);

        buttonUpgradeLife = new Button(Assets.styleButtonUpgrade);
        buttonUpgradeLife.setUserObject(Settings.LEVEL_LIFE);
        initializeButton(buttonUpgradeLife, labelPriceLife);

        buttonUpgradeEnergy = new Button(Assets.styleButtonUpgrade);
        buttonUpgradeEnergy.setUserObject(Settings.LEVEL_ENERGY);
        initializeButton(buttonUpgradeEnergy, labelPriceEnergy);

        buttonUpgradeCoins = new Button(Assets.styleButtonUpgrade);
        buttonUpgradeCoins.setUserObject(Settings.LEVEL_COINS);
        initializeButton(buttonUpgradeCoins, labelPriceCoins);

        buttonUpgradeTreasureChest = new Button(Assets.styleButtonUpgrade);
        buttonUpgradeTreasureChest.setUserObject(Settings.LEVEL_TREASURE_CHEST);
        initializeButton(buttonUpgradeTreasureChest, labelPriceTreasureChest);
    }

    private void initializeButton(final Button button, final Label labelPrice) {
        if ((Integer) button.getUserObject() == MAX_LEVEL)
            button.setVisible(false);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int levelActual = (Integer) button.getUserObject();

                if (Settings.totalCoins >= calculatePrice(levelActual)) {
                    Settings.totalCoins -= calculatePrice(levelActual);

                    if (button == buttonUpgradeMagnet) {
                        Settings.LEVEL_MAGNET++;
                    } else if (button == buttonUpgradeLife) {
                        Settings.LEVEL_LIFE++;
                    } else if (button == buttonUpgradeEnergy) {
                        Settings.LEVEL_ENERGY++;
                    } else if (button == buttonUpgradeCoins) {
                        Settings.LEVEL_COINS++;
                    } else if (button == buttonUpgradeTreasureChest) {
                        Settings.LEVEL_TREASURE_CHEST++;
                    }

                    levelActual++;
                    button.setUserObject(levelActual);

                    updateLabelPriceAndButton(levelActual, labelPrice, button);
                    setArrays();
                }
            }
        });
    }

    private int calculatePrice(int level) {
        return switch (level) {
            case 0 -> PRICE_LEVEL_1;
            case 1 -> PRICE_LEVEL_2;
            case 2 -> PRICE_LEVEL_3;
            case 3 -> PRICE_LEVEL_4;
            default -> PRICE_LEVEL_5;
        };
    }

    private void updateLabelPriceAndButton(int level, Label label, Button button) {
        if (level < MAX_LEVEL) {
            label.setText(calculatePrice(level) + "");
        } else {
            label.setVisible(false);
            button.setVisible(false);
        }
    }

    private void setArrays() {
        for (int i = 0; i < Settings.LEVEL_MAGNET; i++) {
            arrayMagnet[i].setDrawable(new TextureRegionDrawable(Assets.buttonShare));
        }

        for (int i = 0; i < Settings.LEVEL_LIFE; i++) {
            arrayLife[i].setDrawable(new TextureRegionDrawable(Assets.buttonShare));
        }

        for (int i = 0; i < Settings.LEVEL_ENERGY; i++) {
            arrayEnergy[i].setDrawable(new TextureRegionDrawable(Assets.buttonShare));
        }

        for (int i = 0; i < Settings.LEVEL_COINS; i++) {
            arrayCoin[i].setDrawable(new TextureRegionDrawable(Assets.buttonShare));
        }

        for (int i = 0; i < Settings.LEVEL_TREASURE_CHEST; i++) {
            arrayTreasureChest[i].setDrawable(new TextureRegionDrawable(Assets.buttonShare));
        }
    }
}
