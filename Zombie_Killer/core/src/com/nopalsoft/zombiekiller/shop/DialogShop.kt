package com.nopalsoft.zombiekiller.shop;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.zombiekiller.Assets;
import com.nopalsoft.zombiekiller.Settings;
import com.nopalsoft.zombiekiller.scene2d.Dialog;
import com.nopalsoft.zombiekiller.screens.Screens;

public class DialogShop extends Dialog {

    Button buttonPlay, buttonUpgrade, buttonGems, buttonNoAds;

    int buttonSize = 55;

    ScrollPane scroll;
    Table containerTable;

    Label labelCoins;

    public DialogShop(Screens currentScreen) {
        super(currentScreen, 650, 450, 20, Assets.backgroundBigWindow);
        setCloseButton(570, 320, 65);

        Label labelShop = new Label(idiomas.get("shop"), Assets.labelStyleGrande);
        labelShop.setPosition(getWidth() / 2f - labelShop.getWidth() / 2f, 380);
        labelShop.setFontScale(1.2f);
        addActor(labelShop);

        initButtons();

        Table coinsTable = new Table();
        coinsTable.setPosition(getWidth() / 2f - coinsTable.getWidth() / 2f, 365);

        Image imgGem = new Image(Assets.itemGem);
        imgGem.setSize(20, 20);

        labelCoins = new Label("x0", Assets.labelStyleChico);

        coinsTable.add(imgGem).size(20);
        coinsTable.add(labelCoins).padLeft(5);

        containerTable = new Table();
        scroll = new ScrollPane(containerTable, Assets.styleScrollPane);
        scroll.setFadeScrollBars(false);
        scroll.setSize(380, 280);
        scroll.setPosition(175, 55);
        scroll.setVariableSizeKnobs(false);

        addActor(buttonPlay);
        addActor(buttonUpgrade);
        if (Gdx.app.getType() != ApplicationType.WebGL) {// En web no se muestran todos los botones
            addActor(buttonGems);
            addActor(buttonNoAds);
        }
        addActor(scroll);
        addActor(coinsTable);

        new UpgradesSubMenu(containerTable, game);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        labelCoins.setText("x" + Settings.gemsTotal);
    }

    private void initButtons() {

        buttonUpgrade = new Button(Assets.btFire);
        buttonUpgrade.setSize(buttonSize, buttonSize);
        buttonUpgrade.setPosition(100, 270);
        screen.addPressEffect(buttonUpgrade);
        buttonUpgrade.addListener(new ClickListener() {
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                new UpgradesSubMenu(containerTable, game);
            }
        });

        buttonPlay = new Button(Assets.btPlayer);
        buttonPlay.setSize(buttonSize, buttonSize);
        buttonPlay.setPosition(100, 205);
        screen.addPressEffect(buttonPlay);
        buttonPlay.addListener(new ClickListener() {
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                new PlayersSubMenu(containerTable, game);
            }
        });

        buttonGems = new Button(Assets.btGems);
        buttonGems.setSize(buttonSize, buttonSize);
        buttonGems.setPosition(100, 140);
        screen.addPressEffect(buttonGems);
        buttonGems.addListener(new ClickListener() {
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                new GetGemsSubMenu(screen.game, containerTable);
            }
        });

        buttonNoAds = new Button(Assets.btMore);
        buttonNoAds.setSize(buttonSize, buttonSize);
        buttonNoAds.setPosition(100, 75);
        screen.addPressEffect(buttonNoAds);
    }
}
