package com.nopalsoft.ninjarunner.scene2d;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.game.GameScreen;
import com.nopalsoft.ninjarunner.game.GameWorld;
import com.nopalsoft.ninjarunner.leaderboard.LeaderboardScreen;
import com.nopalsoft.ninjarunner.screens.Screens;
import com.nopalsoft.ninjarunner.screens.SettingsScreen;
import com.nopalsoft.ninjarunner.shop.ShopScreen;


public class MenuUI extends Group {
    public static final float ANIMATION_TIME = .35f;

    GameScreen gameScreen;
    GameWorld oWorld;
    Image titleImage;

    Table tableMenu;

    Button buttonPlay;
    Button buttonShop, buttonLeaderboard, buttonAchievements, buttonSettings, buttonRate, buttonShare;

    boolean showMainMenu;

    public MenuUI(final GameScreen gameScreen, GameWorld gameWorld) {
        setBounds(0, 0, Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT);
        this.gameScreen = gameScreen;
        this.oWorld = gameWorld;

        init();
    }

    private void init() {
        titleImage = new Image(Assets.titleDrawable);
        titleImage.setScale(1f);
        titleImage.setPosition(getWidth() / 2f - titleImage.getWidth() * titleImage.getScaleX() / 2f, Screens.SCREEN_HEIGHT + titleImage.getHeight());

        tableMenu = new Table();
        tableMenu.setSize(122, getHeight());
        tableMenu.setBackground(Assets.backgroundMenu);

        initButtons();

        tableMenu.pad(25, 20, 10, 0);
        tableMenu.defaults().size(80).padBottom(15);

        tableMenu.row().colspan(2);
        tableMenu.add(buttonShop);

        tableMenu.row().colspan(2);
        tableMenu.add(buttonLeaderboard);

        tableMenu.row().colspan(2);
        tableMenu.add(buttonAchievements);

        tableMenu.row().colspan(2);
        tableMenu.add(buttonSettings);

        tableMenu.row().size(40).padRight(5).padLeft(5);
        tableMenu.add(buttonRate);
        tableMenu.add(buttonShare);

        tableMenu.setPosition(Screens.SCREEN_WIDTH + tableMenu.getWidth(), 0);

        addActor(tableMenu);
        addActor(buttonPlay);
    }

    void initButtons() {

        buttonShop = new Button(Assets.buttonShop, Assets.buttonShopPress);
        buttonLeaderboard = new Button(Assets.buttonLeaderboard, Assets.buttonLeaderBoardPress);
        buttonAchievements = new Button(Assets.buttonAchievement, Assets.buttonAchievementPress);
        buttonSettings = new Button(Assets.buttonSettings, Assets.buttonSettingsPress);
        buttonRate = new Button(Assets.buttonRate, Assets.buttonRatePress);
        buttonShare = new Button(Assets.buttonShare, Assets.buttonSharePress);

        buttonPlay = new Button(new ButtonStyle(null, null, null));
        buttonPlay.setSize(getWidth() - tableMenu.getWidth(), getHeight());
        buttonPlay.setPosition(0, 0);
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (showMainMenu)
                    gameScreen.setRunning(true);
                else {
                    gameScreen.game.setScreen(new GameScreen(gameScreen.game, false));
                }
            }
        });

        buttonShop.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.changeScreenWithFadeOut(ShopScreen.class, gameScreen.game);
            }
        });

        buttonLeaderboard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {


                gameScreen.game.setScreen(new LeaderboardScreen(gameScreen.game));
            }
        });

        buttonSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.changeScreenWithFadeOut(SettingsScreen.class, gameScreen.game);
            }
        });
    }

    private void addInActions() {
        titleImage.addAction(Actions.moveTo(getWidth() / 2f - titleImage.getWidth() * titleImage.getScaleX() / 2f, 300, ANIMATION_TIME));
        tableMenu.addAction(Actions.moveTo(Screens.SCREEN_WIDTH - tableMenu.getWidth(), 0, ANIMATION_TIME));
    }

    private void addOutActions() {
        titleImage.addAction(Actions.moveTo(getWidth() / 2f - titleImage.getWidth() * titleImage.getScaleX() / 2f, Screens.SCREEN_HEIGHT + titleImage.getHeight(),
                ANIMATION_TIME));

        tableMenu.addAction(Actions.moveTo(Screens.SCREEN_WIDTH + tableMenu.getWidth(), 0, ANIMATION_TIME));
    }

    public void show(Stage stage, final boolean showMainMenu) {
        addInActions();
        stage.addActor(this);

        titleImage.remove();
        addActor(titleImage);
        this.showMainMenu = showMainMenu;
    }

    public void removeWithAnimations() {
        addOutActions();
        addAction(Actions.sequence(Actions.delay(ANIMATION_TIME), Actions.removeActor()));
    }
}
