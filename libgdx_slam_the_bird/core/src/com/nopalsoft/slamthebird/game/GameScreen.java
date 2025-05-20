package com.nopalsoft.slamthebird.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.slamthebird.Achievements;
import com.nopalsoft.slamthebird.Assets;
import com.nopalsoft.slamthebird.MainSlamBird;
import com.nopalsoft.slamthebird.Settings;
import com.nopalsoft.slamthebird.scene2d.LabelCombo;
import com.nopalsoft.slamthebird.scene2d.LabelMonedas;
import com.nopalsoft.slamthebird.scene2d.LabelScore;
import com.nopalsoft.slamthebird.scene2d.VentanaPause;
import com.nopalsoft.slamthebird.scene2d.VentanaRate;
import com.nopalsoft.slamthebird.screens.Screens;
import com.nopalsoft.slamthebird.shop.ShopScreen;

public class GameScreen extends Screens {
    static final int STATE_READY = 1;
    static final int STATE_RUNNING = 2;
    static final int STATE_PAUSED = 3;
    static final int STATE_GAME_OVER = 4;
    static final int STATE_TRY_AGAIN = 5;

    static int state;

    WorldGame oWorld;
    WorldGameRender renderer;

    Image fondoGameover;

    Group groupTryAgain;
    Group botones;
    Image tituloApp;
    boolean sideComboText;// Esta variable hace que apareza el texto del combo a la izq luego derecha luego izq,der,izq

    VentanaRate ventanaRate;
    VentanaPause ventanaPause;

    public GameScreen(MainSlamBird game) {
        super(game);
        oWorld = new WorldGame();
        renderer = new WorldGameRender(batcher, oWorld);

        groupTryAgain = new Group();
        ventanaRate = new VentanaRate(this);
        ventanaPause = new VentanaPause(this);

        setUpBotones();
        setUpGameover();

        setReady();

    }

    private void setUpBotones() {
        botones = new Group();
        botones.setSize(stage.getWidth(), stage.getHeight());
        botones.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                if (ventanaRate.isVisible())
                    return false;

                setRunning();
                Settings.numeroVecesJugadas++;
                return true;
            }
        });

        Button btAchievements, btLeaderboard, btMore, btRate, btShop;
        Button btShareFacebook, btShareTwitter;

        Image tapToPlay, bestScore;

        bestScore = new Image(Assets.bestScore);
        bestScore.setSize(170, 25);
        bestScore
                .setPosition(SCREEN_WIDTH / 2 - bestScore.getWidth() / 2f, 770);
        bestScore.addAction(Actions.repeat(
                Integer.MAX_VALUE,
                Actions.sequence(Actions.alpha(.6f, .75f),
                        Actions.alpha(1, .75f))));

        btShop = new Button(Assets.btShop);
        btShop.setSize(90, 70);
        btShop.setPosition(0, 730);
        addEfectoPress(btShop);
        btShop.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(ShopScreen.class, game);
            }
        });

        btMore = new Button(Assets.btMore);
        btMore.setSize(90, 70);
        btMore.setPosition(390, 730);
        addEfectoPress(btMore);
        btMore.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.reqHandler.showMoreGames();
            }
        });

        btLeaderboard = new Button(Assets.btLeaderboard);
        btLeaderboard.setSize(110, 75);
        btLeaderboard.setPosition(230 - 110, 310);
        addEfectoPress(btLeaderboard);
        btLeaderboard.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.gameServiceHandler.isSignedIn())
                    game.gameServiceHandler.getLeaderboard();
                else
                    game.gameServiceHandler.signIn();
            }
        });

        btAchievements = new Button(Assets.btAchievements);
        btAchievements.setSize(110, 75);
        btAchievements.setPosition(250, 310);
        addEfectoPress(btAchievements);
        btAchievements.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (game.gameServiceHandler.isSignedIn())
                    game.gameServiceHandler.getAchievements();
                else
                    game.gameServiceHandler.signIn();
            }
        });

        btRate = new Button(Assets.btRate);
        btRate.setSize(110, 75);

        btRate.setPosition(SCREEN_WIDTH / 2f - btRate.getWidth() / 2f - 25, 220);// Con el boton face y twitter cambia la pos
        addEfectoPress(btRate);
        btRate.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.reqHandler.showRater();
            }
        });

        btShareFacebook = new Button(new TextureRegionDrawable(
                Assets.btFacebook));
        btShareFacebook.setSize(40, 40);
        btShareFacebook.setPosition(280, 257);
        addEfectoPress(btShareFacebook);
        btShareFacebook.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.reqHandler
                        .shareOnFacebook("My best score playing Slam the bird is "
                                + Settings.bestScore + ", can you beat me?");
            }
        });

        btShareTwitter = new Button(new TextureRegionDrawable(Assets.btTwitter));
        btShareTwitter.setSize(40, 40);
        btShareTwitter.setPosition(280, 212);
        addEfectoPress(btShareTwitter);
        btShareTwitter.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.reqHandler
                        .shareOnTwitter("My best score playing Slam the bird is "
                                + Settings.bestScore + ", can you beat me?");
            }
        });

        final Button btMusica, btSonido;

        btMusica = new Button(Assets.styleButtonMusica);
        btMusica.setPosition(5, 100);
        btMusica.setChecked(!Settings.isMusicOn);
        btMusica.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                event.stop();
                return true;
            }
        });
        btMusica.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                event.stop();
                Settings.isMusicOn = !Settings.isMusicOn;
                btMusica.setChecked(!Settings.isMusicOn);
                if (Settings.isMusicOn)
                    Assets.playMusic();
                else
                    Assets.pauseMusic();
                Gdx.app.log("Muscia", Settings.isMusicOn + "");
            }
        });

        btSonido = new Button(Assets.styleButtonSonido);
        btSonido.setPosition(5, 180);
        btSonido.setChecked(!Settings.isSoundOn);
        btSonido.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                event.stop();
                return true;
            }
        });
        btSonido.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                Settings.isSoundOn = !Settings.isSoundOn;
                btSonido.setChecked(!Settings.isSoundOn);

            }
        });

        tapToPlay = new Image(Assets.tapToPlay);
        tapToPlay.setSize(333, 40);
        tapToPlay
                .setPosition(SCREEN_WIDTH / 2 - tapToPlay.getWidth() / 2f, 140);
        tapToPlay.setOrigin(tapToPlay.getWidth() / 2f,
                tapToPlay.getHeight() / 2f);
        float scaleTime = .75f;
        tapToPlay.addAction(Actions.repeat(Integer.MAX_VALUE, Actions.sequence(
                Actions.scaleTo(.95f, .95f, scaleTime),
                Actions.scaleTo(1f, 1f, scaleTime))));


        botones.addActor(tapToPlay);
        botones.addActor(bestScore);
        botones.addActor(btShop);
        botones.addActor(btMore);
        botones.addActor(btLeaderboard);
        botones.addActor(btAchievements);
        botones.addActor(btRate);
        botones.addActor(btMusica);
        botones.addActor(btSonido);
        botones.addActor(btShareFacebook);
        botones.addActor(btShareTwitter);
    }

    private void setUpGameover() {
        fondoGameover = new Image(Assets.fondoGameover);
        fondoGameover.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        fondoGameover.setOrigin(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f);
        fondoGameover.setScale(2);
        fondoGameover.addAction(Actions.sequence(
                Actions.scaleTo(1.1f, 1.1f, .25f), Actions.delay(1f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        fondoGameover.remove();
                        fondoGameover.setScale(2);
                        setTryAgain();
                    }
                })));

    }

    @Override
    public void update(float delta) {

        switch (state) {
            case STATE_RUNNING:
                updateRunning(delta);
                break;
            case STATE_READY:
            case STATE_TRY_AGAIN:
                updateReady(delta);
                break;
            default:
                break;
        }

    }

    private void updateReady(float delta) {
        float acelX = Gdx.input.getAccelerometerX() * -1 / 5f;

        if (Gdx.input.isKeyPressed(Keys.A))
            acelX = -1;
        else if (Gdx.input.isKeyPressed(Keys.D))
            acelX = 1;

        oWorld.updateReady(delta, acelX);

    }

    int combo;

    private void updateRunning(float delta) {
        boolean slam = false;
        float acelX = Gdx.input.getAccelerometerX() * -1 / 5f;

        if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT))
            acelX = -1;
        else if (Gdx.input.isKeyPressed(Keys.D)
                || Gdx.input.isKeyPressed(Keys.RIGHT))
            acelX = 1;
        Gdx.app.log("Slam is", " " + slam);

        if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE)
                || Gdx.input.isKeyPressed(Keys.DOWN)) {
            slam = true;
            Gdx.app.log("Slam is", " " + slam);
        }

        oWorld.update(delta, acelX, slam);

        if (oWorld.state == WorldGame.STATE_GAME_OVER) {
            setGameover();
        }

        if (oWorld.combo == 0)
            combo = 0;

        if (oWorld.combo > combo) {
            stage.getBatch().setColor(1, 1, 1, 1);// Un BUG que no pone el alpha en 1 otra vez

            combo = oWorld.combo;
            LabelCombo lblCombo = new LabelCombo(oWorld.oRobo.position.x * 100,
                    oWorld.oRobo.position.y * 100 - 50, combo);

            float sideToMove;
            if (sideComboText) {
                sideToMove = 0;
                sideComboText = false;
            } else {
                sideComboText = true;
                sideToMove = 380;
            }

            lblCombo.addAction(Actions.sequence(Actions.moveTo(sideToMove, 400,
                    2.5f, Interpolation.exp10Out), Actions.removeActor()));
            stage.addActor(lblCombo);

        }

    }

    @Override
    public void draw(float delta) {
        renderer.render(delta);

        oCam.update();
        batcher.setProjectionMatrix(oCam.combined);

        batcher.begin();

        switch (state) {
            case STATE_RUNNING:
                drawRunning(delta);
                break;
            case STATE_READY:
            case STATE_TRY_AGAIN:
                drawReady(delta);
                break;
            default:
                break;
        }
        batcher.end();

    }

    private void drawRunning(float delta) {
        drawNumGrandeCentradoX(SCREEN_WIDTH / 2f, 700, oWorld.scoreSlamed);

        batcher.draw(Assets.moneda, 449, 764, 30, 34);
        drawPuntuacionChicoOrigenDerecha(445, 764, oWorld.monedasTomadas);

    }

    private void drawReady(float delta) {

        drawNumChicoCentradoX(SCREEN_WIDTH / 2f, 730, Settings.bestScore);

    }

    private void setPaused() {
        if (state == STATE_RUNNING) {
            state = STATE_PAUSED;
            ventanaPause.show(stage);
        }

    }

    public void setRunningFromPaused() {
        if (state == STATE_PAUSED) {
            state = STATE_RUNNING;
        }

    }

    private void setReady() {
        tituloApp = new Image(Assets.titulo);
        tituloApp.setSize(400, 290);
        tituloApp.setPosition(SCREEN_WIDTH / 2f - tituloApp.getWidth() / 2f,
                415);
        state = STATE_READY;
        stage.addActor(botones);
        stage.addActor(tituloApp);

    }

    private void setRunning() {

        groupTryAgain.addAction(Actions.sequence(Actions.fadeOut(.5f),
                Actions.removeActor()));
        tituloApp.addAction(Actions.sequence(Actions.fadeOut(.5f),
                Actions.removeActor()));
        botones.addAction(Actions.sequence(Actions.fadeOut(.5f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        botones.remove();
                        groupTryAgain.remove();// POr el bug
                        state = STATE_RUNNING;
                        game.reqHandler.hideAdBanner();
                    }
                })));

    }

    private void setGameover() {

        Settings.setBestScores(oWorld.scoreSlamed);
        game.gameServiceHandler.submitScore(oWorld.scoreSlamed);

        state = STATE_GAME_OVER;
        stage.addActor(fondoGameover);
        game.reqHandler.showAdBanner();

    }

    private void setTryAgain() {
        state = STATE_TRY_AGAIN;
        setUpGameover();

        groupTryAgain = new Group();
        groupTryAgain.setSize(420, 300);
        groupTryAgain.setPosition(SCREEN_WIDTH / 2 - groupTryAgain.getWidth()
                / 2, 800);
        groupTryAgain.addAction(Actions.sequence(Actions.moveTo(
                groupTryAgain.getX(), 410, 1, Interpolation.bounceOut), Actions
                .run(new Runnable() {

                    @Override
                    public void run() {
                        botones.addAction(Actions.fadeIn(.5f));
                        stage.addActor(botones);

                        if (Settings.numeroVecesJugadas % 7 == 0
                                && !Settings.seCalifico) {
                            ventanaRate.show(stage);
                        }
                    }
                })));

        Image background = new Image(Assets.fondoPuntuaciones);
        background.setSize(groupTryAgain.getWidth(), groupTryAgain.getHeight());
        groupTryAgain.addActor(background);

        /*
         * Aqui voy a agregar lo de mas del try agai
         */

        Image score = new Image(Assets.score);
        score.setSize(225, 70);
        score.setPosition(420 / 2f - score.getWidth() / 2f, 200);

        Image coinsEarned = new Image(Assets.coinsEarned);
        coinsEarned.setSize(243, 25);
        coinsEarned.setPosition(25, 47);

        LabelScore lblScore = new LabelScore(420 / 2f, 120, oWorld.scoreSlamed);
        LabelMonedas lblMonedas = new LabelMonedas(385, 45,
                oWorld.monedasTomadas);

        Achievements.unlockCoins(oWorld.monedasTomadas);

        groupTryAgain.addActor(score);
        groupTryAgain.addActor(lblScore);
        groupTryAgain.addActor(lblMonedas);
        groupTryAgain.addActor(coinsEarned);

        oWorld = new WorldGame();
        renderer = new WorldGameRender(batcher, oWorld);

        stage.addActor(groupTryAgain);

        if (Settings.numeroVecesJugadas % 10 == 0)
            game.reqHandler.showInterstitial();

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
            if (state == STATE_READY)
                Gdx.app.exit();
            else if (state == STATE_TRY_AGAIN)
                changeScreenWithFadeOut(GameScreen.class, game);
            setPaused();
            return true;
        }
        return false;

    }
}
