package com.nopalsoft.ninjarunner.shop;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.MainGame;
import com.nopalsoft.ninjarunner.Settings;
import com.nopalsoft.ninjarunner.game.GameScreen;
import com.nopalsoft.ninjarunner.screens.Screens;

public class ShopScreen extends Screens {

    Table tbMenu;
    Button btPersonajes, btMascota, btUpgrades, btNoAds, btMore;

    ScrollPane scroll;
    Table contenedor;

    Label lbCoins;

    public ShopScreen(MainGame game) {
        super(game);

        Label lbShop = new Label("Shop", Assets.labelStyleGrande);

        Table tbTitle = new Table();
        tbTitle.setSize(400, 100);
        tbTitle.setPosition(SCREEN_WIDTH / 2f - tbTitle.getWidth() / 2f, SCREEN_HEIGHT - tbTitle.getHeight());
        tbTitle.setBackground(Assets.backgroundTitleShop);
        tbTitle.padTop(20).padBottom(5);
        // tbTitle.debugAll();

        tbTitle.row().colspan(2);
        tbTitle.add(lbShop).expand();
        tbTitle.row();

        Image imgGem = new Image(Assets.moneda.getKeyFrame(0));
        imgGem.setSize(20, 20);

        lbCoins = new Label("x0", Assets.labelStyleChico);

        tbTitle.add(imgGem).size(20).right();
        tbTitle.add(lbCoins).padLeft(5).left();

        initButtons();

        tbMenu = new Table();
        tbMenu.defaults().size(58).padBottom(8);

        tbMenu.row();
        tbMenu.add(btPersonajes);

        tbMenu.row();
        tbMenu.add(btMascota);

        tbMenu.row();
        tbMenu.add(btUpgrades);

        tbMenu.row();
        tbMenu.add(btNoAds);

        tbMenu.row();
        tbMenu.add(btMore);

        Table tbShop = new Table();
        tbShop.setSize(SCREEN_WIDTH, SCREEN_HEIGHT - tbTitle.getHeight());
        tbShop.setBackground(Assets.backgroundShop);
        tbShop.pad(25, 5, 15, 5);

        // Contenedor
        contenedor = new Table();

        scroll = new ScrollPane(contenedor, new ScrollPaneStyle(null, null, null, null, null));
        scroll.setFadeScrollBars(false);
        scroll.setSize(SCREEN_WIDTH - tbMenu.getWidth(), 420);
        scroll.setPosition(tbMenu.getWidth() + 1, 0);
        scroll.setVariableSizeKnobs(false);

        tbShop.add(tbMenu).expandY().width(122);
        tbShop.add(scroll).expand().fill();

        stage.addActor(tbTitle);
        stage.addActor(tbShop);

        new PersonajesSubMenu(contenedor, game);
        btPersonajes.setChecked(true);

    }

    void initButtons() {
        btPersonajes = new Button(Assets.btShop, Assets.btShopPress, Assets.btShopPress);
        btMascota = new Button(Assets.btLeaderboard, Assets.btLeaderboardPress, Assets.btLeaderboardPress);
        btUpgrades = new Button(Assets.btAchievement, Assets.btAchievementPress, Assets.btLeaderboardPress);
        btNoAds = new Button(Assets.btSettings, Assets.btSettingsPress, Assets.btLeaderboardPress);
        btMore = new Button(Assets.btRate, Assets.btSettingsPress);

        btPersonajes.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new PersonajesSubMenu(contenedor, game);
            }

        });

        btMascota.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new MascotasSubMenu(contenedor, game);
            }
        });

        btUpgrades.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new UpgradesSubMenu(contenedor, game);
            }
        });

        btNoAds.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        btMore.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.reqHandler.showMoreGames();
            }
        });

        ButtonGroup<Button> radioGroup = new ButtonGroup<Button>();
        radioGroup.add(btPersonajes, btMascota, btUpgrades, btNoAds);

    }

    @Override
    public void draw(float delta) {
        Assets.backgroundNubes.render(0);

    }

    @Override
    public void update(float delta) {
        lbCoins.setText("x" + Settings.monedasTotal);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            changeScreenWithFadeOut(GameScreen.class, game);
            return true;
        }
        return super.keyUp(keycode);
    }

}
