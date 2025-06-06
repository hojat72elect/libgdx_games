package com.nopalsoft.ninjarunner.shop;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.NinjaRunnerGame;
import com.nopalsoft.ninjarunner.Settings;
import com.nopalsoft.ninjarunner.game.GameScreen;
import com.nopalsoft.ninjarunner.screens.Screens;

public class ShopScreen extends Screens {

    Table tbMenu;
    Button buttonPlayer, buttonMascot, buttonUpgrade, buttonNoAds, buttonMore;

    ScrollPane scroll;
    Table tableContainer;

    Label labelCoins;

    public ShopScreen(NinjaRunnerGame game) {
        super(game);

        Label lbShop = new Label("Shop", Assets.labelStyleLarge);

        Table tbTitle = new Table();
        tbTitle.setSize(400, 100);
        tbTitle.setPosition(SCREEN_WIDTH / 2f - tbTitle.getWidth() / 2f, SCREEN_HEIGHT - tbTitle.getHeight());
        tbTitle.setBackground(Assets.backgroundTitleShop);
        tbTitle.padTop(20).padBottom(5);
        // tbTitle.debugAll();

        tbTitle.row().colspan(2);
        tbTitle.add(lbShop).expand();
        tbTitle.row();

        Image imgGem = new Image(Assets.coinAnimation.getKeyFrame(0));
        imgGem.setSize(20, 20);

        labelCoins = new Label("x0", Assets.labelStyleSmall);

        tbTitle.add(imgGem).size(20).right();
        tbTitle.add(labelCoins).padLeft(5).left();

        initButtons();

        tbMenu = new Table();
        tbMenu.defaults().size(58).padBottom(8);

        tbMenu.row();
        tbMenu.add(buttonPlayer);

        tbMenu.row();
        tbMenu.add(buttonMascot);

        tbMenu.row();
        tbMenu.add(buttonUpgrade);

        tbMenu.row();
        tbMenu.add(buttonNoAds);

        tbMenu.row();
        tbMenu.add(buttonMore);

        Table tbShop = new Table();
        tbShop.setSize(SCREEN_WIDTH, SCREEN_HEIGHT - tbTitle.getHeight());
        tbShop.setBackground(Assets.backgroundShop);
        tbShop.pad(25, 5, 15, 5);

        // Container for the shop content
        tableContainer = new Table();

        scroll = new ScrollPane(tableContainer, new ScrollPaneStyle(null, null, null, null, null));
        scroll.setFadeScrollBars(false);
        scroll.setSize(SCREEN_WIDTH - tbMenu.getWidth(), 420);
        scroll.setPosition(tbMenu.getWidth() + 1, 0);
        scroll.setVariableSizeKnobs(false);

        tbShop.add(tbMenu).expandY().width(122);
        tbShop.add(scroll).expand().fill();

        stage.addActor(tbTitle);
        stage.addActor(tbShop);

        new PlayersSubMenu(tableContainer, game);
        buttonPlayer.setChecked(true);
    }

    void initButtons() {
        buttonPlayer = new Button(Assets.buttonShop, Assets.buttonShopPress, Assets.buttonShopPress);
        buttonMascot = new Button(Assets.buttonLeaderboard, Assets.buttonLeaderBoardPress, Assets.buttonLeaderBoardPress);
        buttonUpgrade = new Button(Assets.buttonAchievement, Assets.buttonAchievementPress, Assets.buttonLeaderBoardPress);
        buttonNoAds = new Button(Assets.buttonSettings, Assets.buttonSettingsPress, Assets.buttonLeaderBoardPress);
        buttonMore = new Button(Assets.buttonRate, Assets.buttonSettingsPress);

        buttonPlayer.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new PlayersSubMenu(tableContainer, game);
            }
        });

        buttonMascot.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new MascotsSubMenu(tableContainer, game);
            }
        });

        buttonUpgrade.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new UpgradesSubMenu(tableContainer, game);
            }
        });

        buttonMore.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.requestHandler.showMoreGames();
            }
        });

        ButtonGroup<Button> radioGroup = new ButtonGroup<>();
        radioGroup.add(buttonPlayer, buttonMascot, buttonUpgrade, buttonNoAds);
    }

    @Override
    public void draw(float delta) {
        Assets.cloudsParallaxBackground.render(0);
    }

    @Override
    public void update(float delta) {
        labelCoins.setText("x" + Settings.totalCoins);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            changeScreenWithFadeOut(GameScreen.class, game);
            return true;
        }
        return super.keyUp(keycode);
    }

    @Override
    public void hide() {

    }
}
