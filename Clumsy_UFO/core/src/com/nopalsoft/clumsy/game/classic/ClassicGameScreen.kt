package com.nopalsoft.clumsy.game.classic;

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
import com.nopalsoft.clumsy.game.arcade.GameScreenArcade;
import com.nopalsoft.clumsy.objects.Ufo;
import com.nopalsoft.clumsy.screens.MainMenuScreen;
import com.nopalsoft.clumsy.screens.Screens;

public class ClassicGameScreen extends Screens {
    static final int STATE_READY = 1;
    static final int STATE_RUNNING = 2;
    static final int STATE_PAUSED = 3;
    static final int STATE_GAME_OVER = 4;
    static final int STATE_TRY_AGAIN = 5;

    static int state;
    final float TIME_INC_GAMEOVER = .035f;
    public int numIncGameOver;
    public boolean comenzarIncrementarPuntuacionGameOver;
    WorldGameClassic oWorld;
    WorldGameRenderer renderer;
    boolean salto;
    Image hurtFlashImage;

    Group GameOverBackground;
    Image gameOverImage;

    Image getReadyImage;
    Image tapCatImage;
    Button buttonPlayClassic, buttonPlayArcade, buttonScore;
    Button buttonRate, buttonRestorePurchases, buttonNoAds;
    Table bottomMenu;
    Button buttonShareFacebook, buttonShareTwitter;
    float timeIncGameOver;

    public ClassicGameScreen(final ClumsyUfoGame game) {
        super(game);
        Settings.numberOfTimesPlayed++;
        oWorld = new WorldGameClassic();
        renderer = new WorldGameRenderer(batch, oWorld);
        state = STATE_READY;
        comenzarIncrementarPuntuacionGameOver = false;
        timeIncGameOver = 0;

        hurtFlashImage = new Image(Assets.whiteDrawable);
        hurtFlashImage.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        hurtFlashImage.addAction(Actions.sequence(Actions.fadeOut(Ufo.HURT_DURATION),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        hurtFlashImage.remove();
                    }
                })));

        initializeGameOverScreen();
        setupReadyScreen();
    }

    private void setupReadyScreen() {
        getReadyImage = new Image(Assets.getReady);
        getReadyImage.setSize(320, 100);
        getReadyImage.setPosition(SCREEN_WIDTH / 2f - 160, SCREEN_HEIGHT / 2f + 50);
        getReadyImage.getColor().a = 0;
        getReadyImage.addAction(Actions.fadeIn(.4f));

        tapCatImage = new Image(Assets.tapCat);
        tapCatImage.setSize(150, 140);
        tapCatImage.setPosition(SCREEN_WIDTH / 2f - 75, SCREEN_HEIGHT / 2f - 100);
        tapCatImage.getColor().a = 0;
        tapCatImage.addAction(Actions.fadeIn(.4f));

        stage.addActor(getReadyImage);
        stage.addActor(tapCatImage);
    }

    private void initializeGameOverScreen() {
        GameOverBackground = new Group();
        GameOverBackground.setSize(400, 200);
        Image background = new Image(Assets.medalsBackground);
        background.setSize(GameOverBackground.getWidth(), GameOverBackground.getHeight());
        GameOverBackground.setPosition(SCREEN_WIDTH / 2f - 200, -201);
        GameOverBackground.addActor(background);

        MoveToAction action = Actions.action(MoveToAction.class);
        action.setInterpolation(Interpolation.sine);
        action.setPosition(SCREEN_WIDTH / 2f - 200, 385);
        action.setDuration(.25f);
        GameOverBackground.addAction(Actions.sequence(action, Actions.delay(.1f),
                Actions.run(new Runnable() {

                    @Override
                    public void run() {
                        comenzarIncrementarPuntuacionGameOver = true;
                        if (numIncGameOver == oWorld.score) {
                            stage.addActor(buttonPlayClassic);
                            stage.addActor(buttonPlayArcade);
                            stage.addActor(buttonScore);
                            stage.addActor(buttonShareFacebook);
                            stage.addActor(buttonShareTwitter);
                            stage.addActor(bottomMenu);
                        }
                    }
                })));

        buttonPlayClassic = new Button(new TextureRegionDrawable(
                Assets.buttonPlayClassic));
        buttonPlayClassic.setSize(160, 95);
        buttonPlayClassic.setPosition(75, 280);
        buttonPlayClassic.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                buttonPlayClassic.setPosition(75, 277);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                buttonPlayClassic.setPosition(75, 280);
                fadeOutButtons();
                state = STATE_TRY_AGAIN;
                Assets.playSound(Assets.swooshing);
                changeScreenWithFadeOut(ClassicGameScreen.class, game);
            }
        });

        buttonPlayArcade = new Button(
                new TextureRegionDrawable(Assets.buttonPlayArcade));
        buttonPlayArcade.setSize(160, 95);
        buttonPlayArcade.setPosition(250, 280);
        buttonPlayArcade.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                buttonPlayArcade.setPosition(250, 277);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                buttonPlayArcade.setPosition(250, 280);
                fadeOutButtons();
                state = STATE_TRY_AGAIN;
                Assets.playSound(Assets.swooshing);
                changeScreenWithFadeOut(GameScreenArcade.class, game);
            }
        });

        buttonScore = new Button(new TextureRegionDrawable(Assets.buttonLeaderboard));
        buttonScore.setSize(160, 95);
        buttonScore.setPosition(130, 180);
        buttonScore.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                buttonScore.setPosition(buttonScore.getX(), buttonScore.getY() - 3);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                buttonScore.setPosition(buttonScore.getX(), buttonScore.getY() + 3);
            }
        });

        buttonShareFacebook = new Button(new TextureRegionDrawable(
                Assets.buttonFacebook));
        buttonShareFacebook.setSize(45, 45);
        buttonShareFacebook.setPosition(295, 230);
        buttonShareFacebook.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                buttonShareFacebook.setPosition(295, 227);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                buttonShareFacebook.setPosition(295, 230);
            }
        });

        buttonShareTwitter = new Button(new TextureRegionDrawable(Assets.buttonTwitter));
        buttonShareTwitter.setSize(45, 45);
        buttonShareTwitter.setPosition(295, 181f);
        buttonShareTwitter.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                buttonShareTwitter.setPosition(buttonShareTwitter.getX(),
                        buttonShareTwitter.getY() - 3);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                buttonShareTwitter.setPosition(buttonShareTwitter.getX(),
                        buttonShareTwitter.getY() + 3);
            }
        });

        buttonRate = new Button(new TextureRegionDrawable(Assets.buttonRate));
        buttonRate.setSize(60, 60);
        buttonRate.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                buttonRate.setPosition(buttonRate.getX(), buttonRate.getY() - 3);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                buttonRate.setPosition(buttonRate.getX(), buttonRate.getY() + 3);
            }
        });

        buttonNoAds = new Button(new TextureRegionDrawable(Assets.buttonNoAds));
        if (Settings.didBuyNoAds)
            buttonNoAds.setVisible(false);
        buttonNoAds.setSize(60, 60);
        buttonNoAds.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                buttonNoAds.setPosition(buttonNoAds.getX(), buttonNoAds.getY() - 3);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                buttonNoAds.setPosition(buttonNoAds.getX(), buttonNoAds.getY() + 3);
            }
        });

        buttonRestorePurchases = new Button(new TextureRegionDrawable(Assets.buttonRestorePurchases));
        buttonRestorePurchases.setSize(60, 60);
        buttonRestorePurchases.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                                     int pointer, int button) {
                buttonRestorePurchases.setPosition(buttonRestorePurchases.getX(),
                        buttonRestorePurchases.getY() - 3);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y,
                                int pointer, int button) {
                buttonRestorePurchases.setPosition(buttonRestorePurchases.getX(),
                        buttonRestorePurchases.getY() + 3);
            }
        });

        bottomMenu = new Table();
        bottomMenu.setPosition(1, 1);
        bottomMenu.defaults().padRight(2.5f);

        bottomMenu.add(buttonRate);
        bottomMenu.add(buttonRestorePurchases);
        bottomMenu.add(buttonNoAds);
        bottomMenu.pack();

        gameOverImage = new Image(Assets.gameover);
        gameOverImage.setSize(320, 100);
        gameOverImage.setPosition(SCREEN_WIDTH / 2f - 160, 600);
    }

    private void fadeOutButtons() {
        GameOverBackground.addAction(Actions.fadeOut(.2f));
        buttonPlayClassic.addAction(Actions.fadeOut(.2f));
        buttonPlayArcade.addAction(Actions.fadeOut(.2f));
        buttonScore.addAction(Actions.fadeOut(.2f));
        gameOverImage.addAction(Actions.fadeOut(.2f));
        buttonShareFacebook.addAction(Actions.fadeOut(.2f));
        buttonShareTwitter.addAction(Actions.fadeOut(.2f));
        bottomMenu.addAction(Actions.fadeOut(.2f));
    }

    @Override
    public void update(float delta) {

        if (Settings.didBuyNoAds)
            buttonNoAds.setVisible(false);

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
            getReadyImage.remove();
            tapCatImage.remove();
            state = STATE_RUNNING;
        }
    }

    private void updateGameOver(float delta) {

        timeIncGameOver += delta;
        if (comenzarIncrementarPuntuacionGameOver
                && numIncGameOver < oWorld.score
                && timeIncGameOver >= TIME_INC_GAMEOVER) {
            timeIncGameOver -= TIME_INC_GAMEOVER;
            numIncGameOver++;

            if (numIncGameOver == oWorld.score) {
                stage.addActor(buttonPlayClassic);
                stage.addActor(buttonScore);
                stage.addActor(buttonPlayArcade);
                stage.addActor(buttonShareFacebook);
                stage.addActor(buttonShareTwitter);
                stage.addActor(bottomMenu);

                if (oWorld.score >= 10) {
                    AtlasRegion med;
                    if (oWorld.score >= 40)
                        med = Assets.med1;
                    else if (oWorld.score >= 30)
                        med = Assets.med2;
                    else if (oWorld.score >= 20)
                        med = Assets.med3;
                    else
                        med = Assets.med4;

                    Image medalla = new Image(med);
                    medalla.setSize(90, 90);
                    medalla.setPosition(45, 47);
                    GameOverBackground.addActor(medalla);
                }
            }
        }
    }

    private void updateRunning(float delta) {

        if (Gdx.input.justTouched())
            salto = true;

        oWorld.update(delta, salto);

        if (oWorld.oUfo.state == Ufo.STATE_HURT) {
            stage.addActor(hurtFlashImage);
            oWorld.oUfo.die();
        }

        if (oWorld.state == WorldGameClassic.STATE_GAMEOVER) {
            setGameover();
        }

        salto = false;
    }

    private void setGameover() {
        state = STATE_GAME_OVER;
        if (Settings.bestScoreClassic < oWorld.score)
            Settings.bestScoreClassic = oWorld.score;
        stage.addActor(GameOverBackground);
        stage.addActor(gameOverImage);
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
            drawScore(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f + 250,
                    oWorld.score);
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
                GameOverBackground.getX() + GameOverBackground.getWidth() - 30,
                GameOverBackground.getY() + 40, Settings.bestScoreClassic);
        drawSmallScoreRightAligned(
                GameOverBackground.getX() + GameOverBackground.getWidth() - 30,
                GameOverBackground.getY() + 110, numIncGameOver);
    }

    private void drawReady(float delta) {
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
