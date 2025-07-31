package com.nopalsoft.zombiekiller.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.zombiekiller.Assets;
import com.nopalsoft.zombiekiller.MainZombie;
import com.nopalsoft.zombiekiller.Settings;
import com.nopalsoft.zombiekiller.scene2d.DialogSelectLevel;
import com.nopalsoft.zombiekiller.shop.DialogShop;

public class MainMenuScreen extends Screens {

    Button buttonPlay;
    Button buttonLeaderboard;
    Button buttonAchievement;
    Button buttonFacebook;
    Button buttonTwitter;
    Button buttonSettings;
    Button buttonShop;

    Button buttonMusic;
    Button buttonSound;

    DialogShop dialogShop;
    DialogSelectLevel dialogSelectLevel;

    Image titleImage;

    public MainMenuScreen(final MainZombie game) {
        super(game);

        music = Gdx.audio.newMusic(Gdx.files.internal("data/sounds/musicMenu.mp3"));
        music.setLooping(true);
        if (Settings.isMusicOn)
            music.play();

        dialogShop = new DialogShop(this);
        dialogSelectLevel = new DialogSelectLevel(this);

        titleImage = new Image(Assets.zombieKillerTitulo);
        titleImage.setPosition(SCREEN_WIDTH / 2f - titleImage.getWidth() / 2f - 30, SCREEN_HEIGHT);
        titleImage.setOrigin(titleImage.getWidth() / 2f, titleImage.getHeight() / 2f);
        titleImage.setScale(.85f);
        titleImage.addAction(Actions.parallel(Actions.moveTo(titleImage.getX(), SCREEN_HEIGHT - titleImage.getHeight(), .5f, Interpolation.swing),
                Actions.scaleTo(1, 1, .5f)));

        buttonPlay = new Button(Assets.btPlay);
        addPressEffect(buttonPlay);
        buttonPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialogSelectLevel.show(stage);
            }
        });

        buttonShop = new Button(Assets.btShop);
        addPressEffect(buttonShop);
        buttonShop.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialogShop.show(stage);
            }
        });

        buttonLeaderboard = new Button(Assets.btLeaderboard);
        addPressEffect(buttonLeaderboard);
        buttonLeaderboard.addListener(new ClickListener() {

        });

        buttonAchievement = new Button(Assets.btAchievement);
        addPressEffect(buttonAchievement);


        buttonSettings = new Button(Assets.btSettings);
        addPressEffect(buttonSettings);
        buttonSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(SettingsScreen.class, game);
            }
        });

        Table containerBt = new Table();
        containerBt.setBackground(Assets.containerButtons);

        containerBt.defaults().size(100).padBottom(40).padLeft(10).padRight(10);
        containerBt.setSize(700, 100);
        containerBt.add(buttonPlay);
        containerBt.add(buttonShop);
        containerBt.add(buttonLeaderboard);
        containerBt.add(buttonAchievement);
        containerBt.add(buttonSettings);


        containerBt.setPosition(SCREEN_WIDTH / 2f - containerBt.getWidth() / 2f, 0);

        buttonFacebook = new Button(Assets.btFacebook);
        buttonFacebook.setSize(50, 50);
        addPressEffect(buttonFacebook);
        buttonFacebook.setPosition(SCREEN_WIDTH - 55, SCREEN_HEIGHT - 55);

        buttonTwitter = new Button(Assets.btTwitter);
        buttonTwitter.setSize(50, 50);
        addPressEffect(buttonTwitter);
        buttonTwitter.setPosition(SCREEN_WIDTH - 55, SCREEN_HEIGHT - 120);


        buttonMusic = new Button(Assets.styleButtonMusic);
        buttonMusic.setSize(50, 50);
        buttonMusic.setPosition(5, SCREEN_HEIGHT - 55);
        buttonMusic.setChecked(!Settings.isMusicOn);
        Gdx.app.log("Musica", Settings.isMusicOn + "");
        buttonMusic.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isMusicOn = !Settings.isMusicOn;
                buttonMusic.setChecked(!Settings.isMusicOn);
                if (Settings.isMusicOn)
                    music.play();
                else
                    music.pause();
            }
        });

        buttonSound = new Button(Assets.styleButtonSound);
        buttonSound.setSize(50, 50);
        buttonSound.setPosition(5, SCREEN_HEIGHT - 120);
        buttonSound.setChecked(!Settings.isSoundOn);
        buttonSound.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isSoundOn = !Settings.isSoundOn;
                buttonSound.setChecked(!Settings.isSoundOn);
            }
        });

        stage.addActor(containerBt);
        if (Gdx.app.getType() != ApplicationType.WebGL) {// En web no se muestran todos los botones
            stage.addActor(buttonFacebook);
            stage.addActor(buttonTwitter);
        }
        stage.addActor(buttonSound);
        stage.addActor(buttonMusic);
        stage.addActor(titleImage);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(float delta) {
        batcher.begin();
        batcher.draw(Assets.background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batcher.draw(Assets.moon, 450, 220, 350, 255);

        batcher.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            if (dialogSelectLevel.isVisible())
                dialogSelectLevel.hide();
            else if (dialogShop.isVisible()) {
                dialogShop.hide();
            } else
                Gdx.app.exit();
            return true;
        }
        return false;
    }
}
