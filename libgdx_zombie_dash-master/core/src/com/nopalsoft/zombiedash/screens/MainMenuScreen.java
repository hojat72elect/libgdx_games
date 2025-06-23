package com.nopalsoft.zombiedash.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.zombiedash.Assets;
import com.nopalsoft.zombiedash.MainZombieDash;
import com.nopalsoft.zombiedash.Settings;
import com.nopalsoft.zombiedash.game.GameScreen;
import com.nopalsoft.zombiedash.shop.VentanaShop;

public class MainMenuScreen extends Screens {

    Button btPlay, btLeaderboard, btAchievement, btFacebook, btTwitter, btHelp, btSettings, btShop;

    Button btMusica;
    Button btSonido;

    VentanaShop ventanaShop;

    Image titulo;

    public MainMenuScreen(final MainZombieDash game) {
        super(game);


        music = Gdx.audio.newMusic(Gdx.files.internal("data/Sounds/musicMenu.mp3"));
        music.setLooping(true);
        if (Settings.isMusicOn)
            music.play();

        ventanaShop = new VentanaShop(this);

        titulo = new Image(Assets.zombieDashTitulo);
        // titulo.setSize(504, 249);
        titulo.setPosition(SCREEN_WIDTH / 2f - titulo.getWidth() / 2f + 10, SCREEN_HEIGHT);
        titulo.setOrigin(titulo.getWidth() / 2f, titulo.getHeight() / 2f);
        titulo.setScale(.85f);
        titulo.addAction(Actions.parallel(Actions.moveTo(titulo.getX(), 135, .5f, Interpolation.swing), Actions.scaleTo(1, 1, .5f)));

        Table containerBt = new Table();
        // containerBt.debug();
        containerBt.setSize(700, 100);
        Image imgBack = new Image(Assets.containerButtons);
        imgBack.setSize(containerBt.getWidth(), containerBt.getHeight());
        containerBt.addActor(imgBack);
        containerBt.setPosition(SCREEN_WIDTH / 2f - containerBt.getWidth() / 2f, 0);

        btPlay = new Button(Assets.btPlay);
        addEfectoPress(btPlay);
        btPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(GameScreen.class, game);

            }
        });

        btShop = new Button(Assets.btShop);
        addEfectoPress(btShop);
        btShop.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ventanaShop.show(stage);

            }
        });

        btLeaderboard = new Button(Assets.btLeaderboard);
        addEfectoPress(btLeaderboard);
        btLeaderboard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.gameServiceHandler.isSignedIn()) {
                    game.gameServiceHandler.getLeaderboard();
                } else
                    game.gameServiceHandler.signIn();

            }
        });

        btAchievement = new Button(Assets.btAchievement);
        addEfectoPress(btAchievement);
        btAchievement.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.gameServiceHandler.isSignedIn()) {
                    game.gameServiceHandler.getAchievements();
                } else
                    game.gameServiceHandler.signIn();

            }
        });

        btSettings = new Button(Assets.btSettings);
        addEfectoPress(btSettings);
        btSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(SettingsScreen.class, game);

            }
        });

        containerBt.defaults().size(100).padBottom(40).padLeft(20);
        containerBt.add(btPlay);
        containerBt.add(btShop);
        containerBt.add(btLeaderboard);
        containerBt.add(btAchievement);
        containerBt.add(btSettings);

        btFacebook = new Button(Assets.btFacebook);
        btFacebook.setSize(50, 50);
        addEfectoPress(btFacebook);
        btFacebook.setPosition(SCREEN_WIDTH - 55, SCREEN_HEIGHT - 55);
        btFacebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.facebookHandler.showFacebook();

            }
        });

        btTwitter = new Button(Assets.btTwitter);
        btTwitter.setSize(50, 50);
        addEfectoPress(btTwitter);
        btTwitter.setPosition(SCREEN_WIDTH - 55, SCREEN_HEIGHT - 120);
        btTwitter.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.reqHandler.shareOnTwitter("");

            }
        });

        btMusica = new Button(Assets.styleButtonMusic);
        btMusica.setSize(50, 50);
        btMusica.setPosition(5, SCREEN_HEIGHT - 55);
        btMusica.setChecked(!Settings.isMusicOn);
        Gdx.app.log("Musica", Settings.isMusicOn + "");
        btMusica.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isMusicOn = !Settings.isMusicOn;
                btMusica.setChecked(!Settings.isMusicOn);
                if (Settings.isMusicOn)
                    music.play();
                else
                    music.pause();

            }
        });

        btSonido = new Button(Assets.styleButtonSound);
        btSonido.setSize(50, 50);
        btSonido.setPosition(5, SCREEN_HEIGHT - 120);
        btSonido.setChecked(!Settings.isSoundOn);
        btSonido.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isSoundOn = !Settings.isSoundOn;
                btSonido.setChecked(!Settings.isSoundOn);
            }
        });

        stage.addActor(containerBt);
        stage.addActor(btFacebook);
        stage.addActor(btTwitter);
        stage.addActor(btSonido);
        stage.addActor(btMusica);
        stage.addActor(titulo);


    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(float delta) {
        Assets.parallaxBackground.render(delta);

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            if (ventanaShop.isVisible()) {
                ventanaShop.hide();
            } else
                Gdx.app.exit();
            return true;
        }
        return false;
    }
}
