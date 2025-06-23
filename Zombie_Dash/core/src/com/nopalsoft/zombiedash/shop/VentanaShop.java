package com.nopalsoft.zombiedash.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.zombiedash.Assets;
import com.nopalsoft.zombiedash.Settings;
import com.nopalsoft.zombiedash.scene2d.NumItemsBar;
import com.nopalsoft.zombiedash.scene2d.Ventana;
import com.nopalsoft.zombiedash.screens.Screens;

public class VentanaShop extends Ventana {

    Button btPersonajes, btMejoras, btGems, btNoAds;

    int buttonSize = 55;

    ScrollPane scroll;
    Table contenedor;

    NumItemsBar numGemsBar;
    NumItemsBar numBulletsBar;

    public VentanaShop(Screens currentScreen) {
        super(currentScreen, 650, 450, 20, Assets.backgroundBigWindow);
        setCloseButton(570, 320, 65);

        Label lbShop = new Label(idiomas.get("shop"), Assets.labelStyleGrande);
        lbShop.setPosition(getWidth() / 2f - lbShop.getWidth() / 2f, 380);
        lbShop.setFontScale(1.2f);
        addActor(lbShop);

        initButtons();

        numGemsBar = new NumItemsBar(Assets.itemGem, 210, 350);
        numBulletsBar = new NumItemsBar(Assets.weaponSmall, 350, 350);

        contenedor = new Table();
        // contenedor.debug();
        scroll = new ScrollPane(contenedor, Assets.styleScrollPane);
        scroll.setFadeScrollBars(false);
        scroll.setSize(380, 280);
        scroll.setPosition(175, 55);
        scroll.setVariableSizeKnobs(false);

        addActor(btPersonajes);
        addActor(btMejoras);
        addActor(btGems);
        addActor(btNoAds);
        addActor(scroll);
        addActor(numGemsBar);
        addActor(numBulletsBar);

        new UpgradesSubMenu(contenedor, game);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        numGemsBar.updateNumGems(Settings.gemsTotal);
        numBulletsBar.updateNumGems(Settings.numBullets);
    }

    private void initButtons() {

        btMejoras = new Button(Assets.btFire);
        btMejoras.setSize(buttonSize, buttonSize);
        btMejoras.setPosition(100, 270);
        screen.addEfectoPress(btMejoras);
        btMejoras.addListener(new ClickListener() {
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                new UpgradesSubMenu(contenedor, game);
            }
        });

        btPersonajes = new Button(Assets.btPlayer);
        btPersonajes.setSize(buttonSize, buttonSize);
        btPersonajes.setPosition(100, 205);
        screen.addEfectoPress(btPersonajes);
        btPersonajes.addListener(new ClickListener() {
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                new PersonajesSubMenu(contenedor, game);
            }

            ;
        });

        btGems = new Button(Assets.btGems);
        btGems.setSize(buttonSize, buttonSize);
        btGems.setPosition(100, 140);
        screen.addEfectoPress(btGems);
        btGems.addListener(new ClickListener() {
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                new GetGemsSubMenu(screen.game, contenedor);
            }

            ;
        });

        btNoAds = new Button(Assets.btMore);
        btNoAds.setSize(buttonSize, buttonSize);
        btNoAds.setPosition(100, 75);
        screen.addEfectoPress(btNoAds);
        btNoAds.addListener(new ClickListener() {
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.reqHandler.showMoreGames();
            }

            ;
        });

    }

}
