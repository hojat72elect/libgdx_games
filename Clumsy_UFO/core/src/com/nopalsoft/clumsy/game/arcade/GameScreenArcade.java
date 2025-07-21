package com.nopalsoft.clumsy.game.arcade;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.clumsy.Assets;
import com.nopalsoft.clumsy.ClumsyUfoGame;
import com.nopalsoft.clumsy.Settings;
import com.nopalsoft.clumsy.game.classic.GameScreenClassic;
import com.nopalsoft.clumsy.objects.Ufo;
import com.nopalsoft.clumsy.screens.MainMenuScreen;
import com.nopalsoft.clumsy.screens.Screens;

public class GameScreenArcade extends Screens {
    static final int STATE_READY = 1;
    static final int STATE_RUNNING = 2;
    static final int STATE_PAUSED = 3;
    static final int STATE_GAME_OVER = 4;
    static final int STATE_TRY_AGAIN = 5;

    static int state;
    final float TIME_INC_GAMEOVER = .0025f;
    public int numIncGameOver;
    public boolean comenzarIncrementarPuntuacionGameOver;
    WorldGameArcade oWorld;
    WorldGameRendererArcade renderer;
    boolean salto;
    Image flashazo;
    /* Game Over */
    Group medallsFondo;
    Image gameOver;
    /* Ready */
    Image getReady;
    Image tapCat;
    Button btPlayClassic, btPlayArcade, btScore;
    Button btRate, btRestorePurchases, btNoAds;
    Table bottomMenu;// rate,ads,restore purchses
    Button btShareFacebook, btShareTwitter;
    float timeIncGameOver;

    public GameScreenArcade(final ClumsyUfoGame game) {
        super(game);
        Settings.numberOfTimesPlayed++;
        oWorld = new WorldGameArcade();
        renderer = new WorldGameRendererArcade(batch, oWorld);
        state = STATE_READY;
        comenzarIncrementarPuntuacionGameOver = false;
        timeIncGameOver = 0;

        flashazo = new Image(Assets.whiteDrawable);
        flashazo.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        flashazo.addAction(Actions.sequence(Actions.fadeOut(Ufo.HURT_DURATION),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        flashazo.remove();
                    }
                })));

        inicializarGameOver();
        inicializarReady();
    }

    private void inicializarReady() {
        getReady = new Image(Assets.getReady);
        getReady.setSize(320, 100);
        getReady.setPosition(SCREEN_WIDTH / 2f - 160, SCREEN_HEIGHT / 2f + 50);
        getReady.getColor().a = 0;
        getReady.addAction(Actions.fadeIn(.4f));

        tapCat = new Image(Assets.tapCat);
        tapCat.setSize(150, 140);
        tapCat.setPosition(SCREEN_WIDTH / 2f - 75, SCREEN_HEIGHT / 2f - 100);
        tapCat.getColor().a = 0;
        tapCat.addAction(Actions.fadeIn(.4f));

        stage.addActor(getReady);
        stage.addActor(tapCat);
    }

    private void inicializarGameOver() {
        medallsFondo = new Group();
        medallsFondo.setSize(400, 200);
        Image background = new Image(Assets.medalsBackground);
        background.setSize(medallsFondo.getWidth(), medallsFondo.getHeight());
        medallsFondo.setPosition(SCREEN_WIDTH / 2f - 200, -201);
        medallsFondo.addActor(background);

        MoveToAction action = Actions.action(MoveToAction.class);
        action.setInterpolation(Interpolation.sine);
        action.setPosition(SCREEN_WIDTH / 2f - 200, 385);
        action.setDuration(.25f);
        medallsFondo.addAction(Actions.sequence(action, Actions.delay(.1f),
                Actions.run(new Runnable() {

                    @Override
                    public void run() {
                        comenzarIncrementarPuntuacionGameOver = true;
                        if (numIncGameOver == oWorld.score) {
                            stage.addActor(btPlayClassic);
                            stage.addActor(btPlayArcade);
                            stage.addActor(btScore);
                            stage.addActor(btShareFacebook);
                            stage.addActor(btShareTwitter);
                            stage.addActor(bottomMenu);
                        }
                    }
                })));

        btPlayClassic = new Button(new TextureRegionDrawable(
                Assets.buttonPlayClassic));
        btPlayClassic.setSize(160, 95);
        btPlayClassic.setPosition(75, 280);
        btPlayClassic.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                btPlayClassic.setPosition(75, 277);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                btPlayClassic.setPosition(75, 280);
                fadeOutButtons();
                state = STATE_TRY_AGAIN;
                Assets.playSound(Assets.swooshing);
                changeScreenWithFadeOut(GameScreenClassic.class, game);
            }
        });

        btPlayArcade = new Button(
                new TextureRegionDrawable(Assets.buttonPlayArcade));
        btPlayArcade.setSize(160, 95);
        btPlayArcade.setPosition(250, 280);
        btPlayArcade.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                btPlayArcade.setPosition(250, 277);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                btPlayArcade.setPosition(250, 280);
                fadeOutButtons();
                state = STATE_TRY_AGAIN;
                Assets.playSound(Assets.swooshing);
                changeScreenWithFadeOut(GameScreenArcade.class, game);
            }
        });

        btScore = new Button(new TextureRegionDrawable(Assets.buttonLeaderboard));
        btScore.setSize(160, 95);
        btScore.setPosition(130, 180);
        btScore.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                btScore.setPosition(btScore.getX(), btScore.getY() - 3);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                btScore.setPosition(btScore.getX(), btScore.getY() + 3);
            }
        });

        btShareFacebook = new Button(new TextureRegionDrawable(
                Assets.buttonFacebook));
        btShareFacebook.setSize(45, 45);
        btShareFacebook.setPosition(295, 230);
        btShareFacebook.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                btShareFacebook.setPosition(295, 227);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                btShareFacebook.setPosition(295, 230);
            }
        });

        btShareTwitter = new Button(new TextureRegionDrawable(Assets.buttonTwitter));
        btShareTwitter.setSize(45, 45);
        btShareTwitter.setPosition(295, 181);
        btShareTwitter.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                btShareTwitter.setPosition(btShareTwitter.getX(),
                        btShareTwitter.getY() - 3);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                btShareTwitter.setPosition(btShareTwitter.getX(),
                        btShareTwitter.getY() + 3);
            }
        });

        btRate = new Button(new TextureRegionDrawable(Assets.buttonRate));
        btRate.setSize(60, 60);
        btRate.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                btRate.setPosition(btRate.getX(), btRate.getY() - 3);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                btRate.setPosition(btRate.getX(), btRate.getY() + 3);
            }
        });

        btNoAds = new Button(new TextureRegionDrawable(Assets.buttonNoAds));
        if (Settings.didBuyNoAds)
            btNoAds.setVisible(false);
        btNoAds.setSize(60, 60);
        btNoAds.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                btNoAds.setPosition(btNoAds.getX(), btNoAds.getY() - 3);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                btNoAds.setPosition(btNoAds.getX(), btNoAds.getY() + 3);
            }
        });

        btRestorePurchases = new Button(new TextureRegionDrawable(Assets.buttonRestorePurchases));
        btRestorePurchases.setSize(60, 60);
        btRestorePurchases.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                btRestorePurchases.setPosition(btRestorePurchases.getX(),
                        btRestorePurchases.getY() - 3);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                btRestorePurchases.setPosition(btRestorePurchases.getX(),
                        btRestorePurchases.getY() + 3);
            }
        });

        bottomMenu = new Table();
        bottomMenu.setPosition(1, 1);
        bottomMenu.defaults().padRight(2.5f);

        bottomMenu.add(btRate);
        bottomMenu.add(btRestorePurchases);
        bottomMenu.add(btNoAds);
        bottomMenu.pack();

        gameOver = new Image(Assets.gameover);
        gameOver.setSize(320, 100);
        gameOver.setPosition(SCREEN_WIDTH / 2f - 160, 600);
    }

    private void fadeOutButtons() {
        medallsFondo.addAction(Actions.fadeOut(.2f));
        btPlayClassic.addAction(Actions.fadeOut(.2f));
        btPlayArcade.addAction(Actions.fadeOut(.2f));
        btScore.addAction(Actions.fadeOut(.2f));
        gameOver.addAction(Actions.fadeOut(.2f));
        btShareFacebook.addAction(Actions.fadeOut(.2f));
        btShareTwitter.addAction(Actions.fadeOut(.2f));
        bottomMenu.addAction(Actions.fadeOut(.2f));
    }

    @Override
    public void update(float delta) {

        if (Settings.didBuyNoAds)
            btNoAds.setVisible(false);

        switch (state) {
            case STATE_READY:
                updateReady();
                break;
            case STATE_RUNNING:
                updateRunning(delta);
                break;
            case STATE_GAME_OVER:
                updateGameOver(delta);
                break;
            default:
                break;
        }
    }

    private void updateReady() {
        if (Gdx.input.justTouched()) {
            getReady.remove();
            tapCat.remove();
            state = STATE_RUNNING;
        }
    }

    private void updateGameOver(float delta) {

        timeIncGameOver += delta;
        if (comenzarIncrementarPuntuacionGameOver
                && numIncGameOver < (int) oWorld.score
                && timeIncGameOver >= TIME_INC_GAMEOVER) {
            timeIncGameOver -= TIME_INC_GAMEOVER;
            numIncGameOver++;

            if (numIncGameOver == (int) oWorld.score) {
                stage.addActor(btPlayClassic);
                stage.addActor(btScore);
                stage.addActor(btPlayArcade);
                stage.addActor(btShareFacebook);
                stage.addActor(btShareTwitter);
                stage.addActor(bottomMenu);

                if (oWorld.score >= 50) {
                    AtlasRegion med;

                    if (oWorld.score >= 250)
                        med = Assets.med1;
                    else if (oWorld.score >= 200)
                        med = Assets.med2;
                    else if (oWorld.score >= 100)
                        med = Assets.med3;
                    else
                        med = Assets.med4;

                    Image medalla = new Image(med);
                    medalla.setSize(90, 90);
                    medalla.setPosition(45, 47);
                    medallsFondo.addActor(medalla);
                }
            }
        }
    }

    private void updateRunning(float delta) {

        if (Gdx.input.justTouched())
            salto = true;

        oWorld.update(delta, salto);

        if (oWorld.oUfo.state == Ufo.STATE_HURT) {
            stage.addActor(flashazo);
            oWorld.oUfo.die();
        }

        if (oWorld.state == WorldGameArcade.STATE_GAMEOVER) {
            setGameover();
        }

        salto = false;
    }

    private void setGameover() {
        state = STATE_GAME_OVER;
        if (Settings.bestScoreArcade < oWorld.score)
            Settings.bestScoreArcade = (int) oWorld.score;
        stage.addActor(medallsFondo);
        stage.addActor(gameOver);
    }

    @Override
    public void draw(float delta) {

        if (state == STATE_PAUSED || state == STATE_GAME_OVER)
            delta = 0;

        batch.begin();
        batch.draw(Assets.background0, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batch.end();

        renderer.render();
        Assets.parallaxBackground.render(delta);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        if (state == STATE_READY)
            drawReady(delta);

        if (state != STATE_GAME_OVER)
            drawScoreCentered(0, SCREEN_HEIGHT - 65,
                    (int) oWorld.score);
        batch.end();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (state == STATE_GAME_OVER) {
            batch.begin();
            drawGameover();
            batch.end();
        }
    }

    private void drawGameover() {
        drawSmallScoreRightAligned(
                medallsFondo.getX() + medallsFondo.getWidth() - 30,
                medallsFondo.getY() + 40, Settings.bestScoreArcade);
        drawSmallScoreRightAligned(
                medallsFondo.getX() + medallsFondo.getWidth() - 30,
                medallsFondo.getY() + 110, numIncGameOver);
    }

    private void drawReady(float delta) {
        // if (oWorld.oUfo != null)
        oWorld.oUfo.update(delta, null);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.SPACE) {
            salto = true;
            return true;
        } else if (keycode == Keys.BACK || keycode == Keys.ESCAPE)
            game.setScreen(new MainMenuScreen(game));
        return false;
    }
}
