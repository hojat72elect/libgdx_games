package com.nopalsoft.zombiekiller.scene2d;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.zombiekiller.Assets;
import com.nopalsoft.zombiekiller.game.GameScreen;
import com.nopalsoft.zombiekiller.game.WorldGame;
import com.nopalsoft.zombiekiller.screens.Screens;
import com.nopalsoft.zombiekiller.shop.DialogShop;

public class DialogNextLevel extends Dialog {

    WorldGame oWorld;

    Button btLevels, btShop, btTryAgain;
    int buttonSize = 55;
    DialogShop ventanaShop;
    DialogSelectLevel ventanaSelectLevel;

    int skulls;

    public DialogNextLevel(Screens currentScreen) {
        super(currentScreen, 380, 390, 50, Assets.backgroundSmallWindow);
        oWorld = ((GameScreen) currentScreen).worldGame;
        ventanaShop = new DialogShop(screen);
        ventanaSelectLevel = new DialogSelectLevel(screen);

        skulls = oWorld.skulls;

        Label lbShop = new Label(idiomas.get("congratulations"), Assets.labelStyleGrande);
        lbShop.setFontScale(1.3f);
        lbShop.setAlignment(Align.center);
        lbShop.setPosition(getWidth() / 2f - lbShop.getWidth() / 2f, 300);
        addActor(lbShop);

        initButtons();

        Table tableSkulls = new Table();
        tableSkulls.setSize(210, 60);
        tableSkulls.setPosition(getWidth() / 2f - tableSkulls.getWidth() / 2f, 225);
        tableSkulls.defaults().expandX().uniform();

        for (int i = 1; i <= 3; i++) {
            Image imageSkull = new Image(Assets.upgradeOff);
            if (skulls >= i)
                imageSkull.setDrawable(new TextureRegionDrawable(Assets.itemSkull));
            tableSkulls.add(imageSkull).size(55);
        }

        Table tableStatistics = new Table();
        tableStatistics.setSize(getWidth(), 95);
        tableStatistics.setPosition(0, 130);

        Label labelZombiesKilled = new Label(idiomas.get("zombies_killed"), Assets.labelStyleChico);
        Label labelZombiesKilledNum = new Label(oWorld.totalZombiesKilled + "/" + oWorld.TOTAL_ZOMBIES_LEVEL, Assets.labelStyleChico);

        Label labelGemsCollected = new Label(idiomas.get("gems"), Assets.labelStyleChico);
        Label labelGemsCollectedNum = new Label(oWorld.gems + "", Assets.labelStyleChico);

        Label labelBonus = new Label(idiomas.get("bonus_gems"), Assets.labelStyleChico);
        Label labelBonusNum = new Label(oWorld.bonus + "", Assets.labelStyleChico);

        tableStatistics.defaults().pad(0, 45, 0, 45).expandX();

        tableStatistics.add(labelZombiesKilled).left();
        tableStatistics.add(labelZombiesKilledNum).right();

        tableStatistics.row();
        tableStatistics.add(labelGemsCollected).left();
        tableStatistics.add(labelGemsCollectedNum).right();

        tableStatistics.row();
        tableStatistics.add(labelBonus).left();
        tableStatistics.add(labelBonusNum).right();

        // Buttons
        Table tableButtons = new Table();
        tableButtons.setSize(250, 90);
        tableButtons.setPosition(getWidth() / 2f - tableButtons.getWidth() / 2f, 40);

        tableButtons.defaults().expandX().uniform();

        tableButtons.add(btShop);
        tableButtons.add(btTryAgain);
        tableButtons.add(btLevels);

        addActor(tableButtons);
        addActor(tableSkulls);
        addActor(tableStatistics);
    }

    private void initButtons() {
        btLevels = new Button(Assets.btRight);
        btLevels.setSize(buttonSize, buttonSize);

        screen.addPressEffect(btLevels);
        btLevels.addListener(new ClickListener() {
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                ventanaSelectLevel.show(screen.stage);
            }
        });

        btShop = new Button(Assets.btShop);
        btShop.setSize(buttonSize, buttonSize);

        screen.addPressEffect(btShop);
        btShop.addListener(new ClickListener() {
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                ventanaShop.show(screen.stage);
            }
        });

        btTryAgain = new Button(Assets.btTryAgain);
        btTryAgain.setSize(buttonSize, buttonSize);

        screen.addPressEffect(btTryAgain);
        btTryAgain.addListener(new ClickListener() {
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                screen.changeScreenWithFadeOut(GameScreen.class, ((GameScreen) screen).level, game);
            }
        });
    }

    @Override
    public void show(Stage stage) {
        super.show(stage);
    }
}
