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
import com.nopalsoft.ninjarunner.game.WorldGame;
import com.nopalsoft.ninjarunner.leaderboard.LeaderboardScreen;
import com.nopalsoft.ninjarunner.screens.Screens;
import com.nopalsoft.ninjarunner.screens.SettingsScreen;
import com.nopalsoft.ninjarunner.shop.ShopScreen;


public class MenuUI extends Group {
    public static final float ANIMATION_TIME = .35f;

    GameScreen gameScreen;
    WorldGame oWorld;
    Image titulo;

    Table tbMenu;

    Button btPlay;
    Button btShop, btLeaderboard, btAchievements, btSettings, btRate, btShare;

    boolean showMainMenu;

    public MenuUI(final GameScreen gameScreen, WorldGame oWorld) {
        setBounds(0, 0, Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT);
        this.gameScreen = gameScreen;
        this.oWorld = oWorld;

        init();

    }

    private void init() {
        titulo = new Image(Assets.titulo);
        titulo.setScale(1f);
        titulo.setPosition(getWidth() / 2f - titulo.getWidth() * titulo.getScaleX() / 2f, Screens.SCREEN_HEIGHT + titulo.getHeight());

        tbMenu = new Table();
        tbMenu.setSize(122, getHeight());
        tbMenu.setBackground(Assets.backgroundMenu);

        initButtons();

        tbMenu.pad(25, 20, 10, 0);
        tbMenu.defaults().size(80).padBottom(15);
        // tbMenu.debugAll();

        tbMenu.row().colspan(2);
        tbMenu.add(btShop);

        tbMenu.row().colspan(2);
        tbMenu.add(btLeaderboard);

        tbMenu.row().colspan(2);
        tbMenu.add(btAchievements);

        tbMenu.row().colspan(2);
        tbMenu.add(btSettings);

        tbMenu.row().size(40).padRight(5).padLeft(5);
        tbMenu.add(btRate);
        tbMenu.add(btShare);

        tbMenu.setPosition(Screens.SCREEN_WIDTH + tbMenu.getWidth(), 0);

        addActor(tbMenu);
        addActor(btPlay);

    }

    void initButtons() {

        btShop = new Button(Assets.btShop, Assets.btShopPress);
        btLeaderboard = new Button(Assets.btLeaderboard, Assets.btLeaderboardPress);
        btAchievements = new Button(Assets.btAchievement, Assets.btAchievementPress);
        btSettings = new Button(Assets.btSettings, Assets.btSettingsPress);
        btRate = new Button(Assets.btRate, Assets.btRatePress);
        btShare = new Button(Assets.btShare, Assets.btSharePress);

        btPlay = new Button(new ButtonStyle(null, null, null));
        btPlay.setSize(getWidth() - tbMenu.getWidth(), getHeight());
        btPlay.setPosition(0, 0);
        btPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // gameScreen.game.reqHandler.hideAdBanner();FIXME
                if (showMainMenu)
                    gameScreen.setRunning(true);
                else {
                    gameScreen.game.setScreen(new GameScreen(gameScreen.game, false));
                }
            }
        });

        btShop.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.changeScreenWithFadeOut(ShopScreen.class, gameScreen.game);
            }
        });

        btRate.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.game.reqHandler.showRater();
            }
        });

        btShare.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.game.reqHandler.shareApp();

            }
        });

        btLeaderboard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {


                gameScreen.game.setScreen(new LeaderboardScreen(gameScreen.game));

            }
        });

        btAchievements.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (gameScreen.game.gameServiceHandler.isSignedIn()) {
                    gameScreen.game.gameServiceHandler.getAchievements();
                } else {
                    gameScreen.game.gameServiceHandler.signIn();
                }
            }
        });

        btSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.changeScreenWithFadeOut(SettingsScreen.class, gameScreen.game);
            }
        });

    }

    private void addInActions() {
        titulo.addAction(Actions.moveTo(getWidth() / 2f - titulo.getWidth() * titulo.getScaleX() / 2f, 300, ANIMATION_TIME));
        tbMenu.addAction(Actions.moveTo(Screens.SCREEN_WIDTH - tbMenu.getWidth(), 0, ANIMATION_TIME));

    }

    private void addOutActions() {
        titulo.addAction(Actions.moveTo(getWidth() / 2f - titulo.getWidth() * titulo.getScaleX() / 2f, Screens.SCREEN_HEIGHT + titulo.getHeight(),
                ANIMATION_TIME));

        tbMenu.addAction(Actions.moveTo(Screens.SCREEN_WIDTH + tbMenu.getWidth(), 0, ANIMATION_TIME));

    }

    public void show(Stage stage, final boolean showMainMenu) {
        addInActions();
        stage.addActor(this);

        titulo.remove();
        addActor(titulo);
        this.showMainMenu = showMainMenu;

    }

    public void removeWithAnimations() {
        addOutActions();
        addAction(Actions.sequence(Actions.delay(ANIMATION_TIME), Actions.removeActor()));
    }

}
