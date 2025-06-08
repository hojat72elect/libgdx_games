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
    GameWorld oWorld;
    Image titleImage;
    Image gameOver;

    Table tbMenu;
    Table tbGameOver;

    Label lbBestScore;
    Label lbScore;

    Button buttonPlay, buttonLeaderboard, buttonAchievements, buttonFacebook, buttonTwitter;
    Button buttonMusic, buttonSoundEffect;

    boolean showMainMenu;

    public MenuUI(final GameScreen gameScreen, GameWorld oWorld) {
        setBounds(0, 0, Screens.SCREEN_WIDTH, Screens.SCREEN_HEIGHT);
        this.gameScreen = gameScreen;
        this.oWorld = oWorld;

        init();

        tbGameOver = new Table();
        tbGameOver.setSize(350, 200);
        tbGameOver.setBackground(Assets.windowBackgroundDrawable);
        tbGameOver.setPosition(getWidth() / 2f - tbGameOver.getWidth() / 2f, 110);

        lbBestScore = new Label("0", Assets.lblStyle);
        lbScore = new Label("0", Assets.lblStyle);

        lbScore.setFontScale(.8f);
        lbBestScore.setFontScale(.8f);

        tbGameOver.pad(15).padTop(30).padBottom(50);
        tbGameOver.defaults().expand();

        tbGameOver.add(new Label("Score", Assets.lblStyle)).left();
        tbGameOver.add(lbScore).expandX().right();

        tbGameOver.row();
        tbGameOver.add(new Label("Best score", Assets.lblStyle)).left();
        tbGameOver.add(lbBestScore).expandX().right();
    }

    private void init() {
        titleImage = new Image(Assets.titleDrawable);
        titleImage.setScale(1f);
        titleImage.setPosition(getWidth() / 2f - titleImage.getWidth() * titleImage.getScaleX() / 2f, Screens.SCREEN_HEIGHT + titleImage.getHeight());

        gameOver = new Image(Assets.gameOverDrawable);
        gameOver.setScale(1.25f);
        gameOver.setPosition(getWidth() / 2f - gameOver.getWidth() * gameOver.getScaleX() / 2f, Screens.SCREEN_HEIGHT + gameOver.getHeight());

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

        tbMenu = new Table();
        tbMenu.setBackground(Assets.menuBackgroundDrawable);

        buttonPlay = new Button(Assets.buttonRight, Assets.buttonRightPressed);
        buttonLeaderboard = new Button(Assets.buttonLeaderboard, Assets.buttonLeaderboardPressed);
        buttonAchievements = new Button(Assets.buttonAchievements, Assets.buttonAchievementsPressed);

        tbMenu.defaults().size(90).padBottom(20).padLeft(10).padRight(10);
        if (Gdx.app.getType() != ApplicationType.WebGL) {
            tbMenu.setSize(385, 85);
            tbMenu.add(buttonPlay);
            tbMenu.add(buttonLeaderboard);
            tbMenu.add(buttonAchievements);
        } else {
            tbMenu.setSize(120, 85);
            tbMenu.add(buttonPlay);
        }
        tbMenu.setPosition(Screens.SCREEN_WIDTH / 2f - tbMenu.getWidth() / 2f, -tbMenu.getHeight());


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

        addActor(tbMenu);
        addActor(buttonFacebook);
        addActor(buttonTwitter);
        addActor(buttonMusic);
        addActor(buttonSoundEffect);
    }

    private void addInActions() {
        titleImage.addAction(Actions.moveTo(getWidth() / 2f - titleImage.getWidth() * titleImage.getScaleX() / 2f, 300, ANIMATION_TIME));
        gameOver.addAction(Actions.moveTo(getWidth() / 2f - gameOver.getWidth() * gameOver.getScaleX() / 2f, 320, ANIMATION_TIME));

        tbMenu.addAction(Actions.moveTo(Screens.SCREEN_WIDTH / 2f - tbMenu.getWidth() / 2f, 0, ANIMATION_TIME));

        buttonFacebook.addAction(Actions.moveTo(735, 410, ANIMATION_TIME));
        buttonTwitter.addAction(Actions.moveTo(735, 325, ANIMATION_TIME));
        buttonMusic.addAction(Actions.moveTo(5, 410, ANIMATION_TIME));
        buttonSoundEffect.addAction(Actions.moveTo(5, 325, ANIMATION_TIME));
    }

    private void addOutActions() {
        titleImage.addAction(Actions.moveTo(getWidth() / 2f - titleImage.getWidth() * titleImage.getScaleX() / 2f, Screens.SCREEN_HEIGHT + titleImage.getHeight(),
                ANIMATION_TIME));
        gameOver.addAction(Actions.moveTo(getWidth() / 2f - gameOver.getWidth() * gameOver.getScaleX() / 2f,
                Screens.SCREEN_HEIGHT + gameOver.getHeight(), ANIMATION_TIME));

        tbMenu.addAction(Actions.moveTo(Screens.SCREEN_WIDTH / 2f - tbMenu.getWidth() / 2f, -tbMenu.getHeight(), ANIMATION_TIME));

        buttonFacebook.addAction(Actions.moveTo(Screens.SCREEN_WIDTH + buttonFacebook.getWidth(), 410, ANIMATION_TIME));
        buttonTwitter.addAction(Actions.moveTo(Screens.SCREEN_WIDTH + buttonTwitter.getWidth(), 325, ANIMATION_TIME));
        buttonMusic.addAction(Actions.moveTo(-buttonMusic.getWidth(), 410, ANIMATION_TIME));
        buttonSoundEffect.addAction(Actions.moveTo(-buttonSoundEffect.getWidth(), 325, ANIMATION_TIME));
    }

    public void show(Stage stage, final boolean showMainMenu) {
        addInActions();
        stage.addActor(this);

        titleImage.remove();
        gameOver.remove();
        tbGameOver.remove();

        if (showMainMenu) {
            addActor(titleImage);
        } else {
            lbBestScore.setText(Settings.bestScore + " m");
            lbScore.setText(gameScreen.score + " m");

            addActor(gameOver);
            addActor(tbGameOver);
        }

        this.showMainMenu = showMainMenu;
    }

    public void removeWithAnimations() {
        addOutActions();
        addAction(Actions.sequence(Actions.delay(ANIMATION_TIME), Actions.removeActor()));
    }
}
