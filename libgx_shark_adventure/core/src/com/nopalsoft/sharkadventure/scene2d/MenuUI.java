package com.nopalsoft.sharkadventure.scene2d;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.sharkadventure.Assets;
import com.nopalsoft.sharkadventure.Settings;
import com.nopalsoft.sharkadventure.game.GameScreen;
import com.nopalsoft.sharkadventure.game.GameWorld;
import com.nopalsoft.sharkadventure.screens.Screens;

public class MenuUI extends Group {
    public static final float ANIMATION_TIME = .35f;

    GameScreen gameScreen;
    GameWorld gameWorld;
    Image titleImage;
    Image gameOverImage;

    Table tableMenu;
    Table tableGameOver;

    Label labelBestScore;
    Label labelScore;

    Button buttonPlay, buttonLeaderboard, buttonAchievements, buttonFacebook, buttonTwitter;
    Button buttonMusic, buttonSoundEffect;

    boolean showMainMenu;

    public MenuUI(final GameScreen gameScreen, GameWorld gameWorld) {
        setBounds(0, 0, Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT);
        this.gameScreen = gameScreen;
        this.gameWorld = gameWorld;

        init();

        tableGameOver = new Table();
        tableGameOver.setSize(350, 200);
        tableGameOver.setBackground(Assets.windowBackgroundDrawable);
        tableGameOver.setPosition(getWidth() / 2f - tableGameOver.getWidth() / 2f, 110);

        labelBestScore = new Label("0", Assets.lblStyle);
        labelScore = new Label("0", Assets.lblStyle);

        labelScore.setFontScale(.8f);
        labelBestScore.setFontScale(.8f);

        tableGameOver.pad(15).padTop(30).padBottom(50);
        tableGameOver.defaults().expand();

        tableGameOver.add(new Label("Score", Assets.lblStyle)).left();
        tableGameOver.add(labelScore).expandX().right();

        tableGameOver.row();
        tableGameOver.add(new Label("Best score", Assets.lblStyle)).left();
        tableGameOver.add(labelBestScore).expandX().right();
    }

    private void init() {
        titleImage = new Image(Assets.titleDrawable);
        titleImage.setScale(1f);
        titleImage.setPosition(getWidth() / 2f - titleImage.getWidth() * titleImage.getScaleX() / 2f, Screens.SCREEN_HEIGHT + titleImage.getHeight());

        gameOverImage = new Image(Assets.gameOverDrawable);
        gameOverImage.setScale(1.25f);
        gameOverImage.setPosition(getWidth() / 2f - gameOverImage.getWidth() * gameOverImage.getScaleX() / 2f, Screens.SCREEN_HEIGHT + gameOverImage.getHeight());

        buttonFacebook = new Button(Assets.buttonFacebook, Assets.buttonFacebookPressed);
        buttonFacebook.setSize(60, 60);
        buttonFacebook.setPosition(Screens.SCREEN_WIDTH + buttonFacebook.getWidth(), 410);

        buttonTwitter = new Button(Assets.buttonTwitter, Assets.buttonTwitterPressed);
        buttonTwitter.setSize(60, 60);
        buttonTwitter.setPosition(Screens.SCREEN_WIDTH + buttonTwitter.getWidth(), 410);

        buttonMusic = new Button(Assets.buttonMusicOff, Assets.buttonMusicOn, Assets.buttonMusicOn);
        buttonMusic.setSize(60, 60);
        buttonMusic.setPosition(-buttonMusic.getWidth(), 410);

        buttonSoundEffect = new Button(Assets.buttonSoundOff, Assets.buttonSoundOn, Assets.buttonSoundOn);
        buttonSoundEffect.setSize(60, 60);
        buttonSoundEffect.setPosition(-buttonSoundEffect.getWidth(), 325);

        tableMenu = new Table();
        tableMenu.setBackground(Assets.menuBackgroundDrawable);

        buttonPlay = new Button(Assets.buttonRight, Assets.buttonRightPressed);
        buttonLeaderboard = new Button(Assets.buttonLeaderboard, Assets.buttonLeaderboardPressed);
        buttonAchievements = new Button(Assets.buttonAchievements, Assets.buttonAchievementsPressed);

        tableMenu.defaults().size(90).padBottom(20).padLeft(10).padRight(10);
        if (Gdx.app.getType() != ApplicationType.WebGL) {
            tableMenu.setSize(385, 85);
            tableMenu.add(buttonPlay);
            tableMenu.add(buttonLeaderboard);
            tableMenu.add(buttonAchievements);
        } else {
            tableMenu.setSize(120, 85);
            tableMenu.add(buttonPlay);
        }
        tableMenu.setPosition(Screens.SCREEN_WIDTH / 2f - tableMenu.getWidth() / 2f, -tableMenu.getHeight());


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

        buttonMusic.setChecked(Settings.isMusicOn);
        buttonMusic.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isMusicOn = !Settings.isMusicOn;
                buttonMusic.setChecked(Settings.isMusicOn);
                if (Settings.isMusicOn)
                    Assets.music.play();
                else
                    Assets.music.pause();
            }
        });

        buttonSoundEffect.setChecked(Settings.isSoundOn);
        buttonSoundEffect.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isSoundOn = !Settings.isSoundOn;
                buttonSoundEffect.setChecked(Settings.isSoundOn);
            }
        });

        addActor(tableMenu);
        addActor(buttonFacebook);
        addActor(buttonTwitter);
        addActor(buttonMusic);
        addActor(buttonSoundEffect);
    }

    private void addInActions() {
        titleImage.addAction(Actions.moveTo(getWidth() / 2f - titleImage.getWidth() * titleImage.getScaleX() / 2f, 300, ANIMATION_TIME));
        gameOverImage.addAction(Actions.moveTo(getWidth() / 2f - gameOverImage.getWidth() * gameOverImage.getScaleX() / 2f, 320, ANIMATION_TIME));

        tableMenu.addAction(Actions.moveTo(Screens.SCREEN_WIDTH / 2f - tableMenu.getWidth() / 2f, 0, ANIMATION_TIME));

        buttonFacebook.addAction(Actions.moveTo(735, 410, ANIMATION_TIME));
        buttonTwitter.addAction(Actions.moveTo(735, 325, ANIMATION_TIME));
        buttonMusic.addAction(Actions.moveTo(5, 410, ANIMATION_TIME));
        buttonSoundEffect.addAction(Actions.moveTo(5, 325, ANIMATION_TIME));
    }

    private void addOutActions() {
        titleImage.addAction(Actions.moveTo(getWidth() / 2f - titleImage.getWidth() * titleImage.getScaleX() / 2f, Screens.SCREEN_HEIGHT + titleImage.getHeight(),
                ANIMATION_TIME));
        gameOverImage.addAction(Actions.moveTo(getWidth() / 2f - gameOverImage.getWidth() * gameOverImage.getScaleX() / 2f,
                Screens.SCREEN_HEIGHT + gameOverImage.getHeight(), ANIMATION_TIME));

        tableMenu.addAction(Actions.moveTo(Screens.SCREEN_WIDTH / 2f - tableMenu.getWidth() / 2f, -tableMenu.getHeight(), ANIMATION_TIME));

        buttonFacebook.addAction(Actions.moveTo(Screens.SCREEN_WIDTH + buttonFacebook.getWidth(), 410, ANIMATION_TIME));
        buttonTwitter.addAction(Actions.moveTo(Screens.SCREEN_WIDTH + buttonTwitter.getWidth(), 325, ANIMATION_TIME));
        buttonMusic.addAction(Actions.moveTo(-buttonMusic.getWidth(), 410, ANIMATION_TIME));
        buttonSoundEffect.addAction(Actions.moveTo(-buttonSoundEffect.getWidth(), 325, ANIMATION_TIME));
    }

    public void show(Stage stage, final boolean showMainMenu) {
        addInActions();
        stage.addActor(this);

        titleImage.remove();
        gameOverImage.remove();
        tableGameOver.remove();

        if (showMainMenu) {
            addActor(titleImage);
        } else {
            labelBestScore.setText(Settings.bestScore + " m");
            labelScore.setText(gameScreen.score + " m");

            addActor(gameOverImage);
            addActor(tableGameOver);
        }

        this.showMainMenu = showMainMenu;
    }

    public void removeWithAnimations() {
        addOutActions();
        addAction(Actions.sequence(Actions.delay(ANIMATION_TIME), Actions.removeActor()));
    }
}
