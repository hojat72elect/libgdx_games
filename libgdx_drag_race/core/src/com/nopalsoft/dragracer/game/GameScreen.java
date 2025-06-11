package com.nopalsoft.dragracer.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.nopalsoft.dragracer.Assets;
import com.nopalsoft.dragracer.MainStreet;
import com.nopalsoft.dragracer.Settings;
import com.nopalsoft.dragracer.objects.SpeedBar;
import com.nopalsoft.dragracer.scene2D.MarcoGameOver;
import com.nopalsoft.dragracer.scene2D.SwipeHorizontalTutorial;
import com.nopalsoft.dragracer.scene2D.SwipeVerticalTutorial;
import com.nopalsoft.dragracer.screens.MainMenuScreen;
import com.nopalsoft.dragracer.screens.Screens;
import com.nopalsoft.dragracer.shop.ShopScreen;

public class GameScreen extends Screens {
    static final int STATE_READY = 1;
    static final int STATE_RUNNING = 2;
    static final int STATE_PAUSED = 3;
    static final int STATE_GAME_OVER = 4;
    static int state;

    int TIME_TO_START = 3;// Time that appears at the beginning

    Label lbScore, lbCoin;
    Table tbScores;

    Label lbTryAgain;
    Label lbShopScreen;
    Label lbLeaderboard;

    SpeedBar speedBar;
    private final Stage stageGame;
    private final TrafficGame trafficGame;
    int score, coins;

    boolean canSuperSpeed;

    Group gpPaused;
    MarcoGameOver marcoGameOver;
    Button buttonMusic;

    public GameScreen(MainStreet game) {
        super(game);
        stageGame = new Stage(new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT));
        trafficGame = new TrafficGame();
        stageGame.addActor(trafficGame);

        initUI();

        setReady();
        Settings.numeroVecesJugadas++;
    }

    private void initUI() {
        speedBar = new SpeedBar(TrafficGame.NUM_COINS_FOR_SUPERSPEED, 5, 720, 160, 20);

        lbScore = new Label("Distance 0m", Assets.labelStyleGrande);
        lbScore.setFontScale(.8f);

        lbCoin = new Label("0", Assets.labelStyleGrande);
        lbCoin.setFontScale(.8f);

        Image imgCoin = new Image(Assets.coinFrente);

        tbScores = new Table();
        tbScores.setWidth(SCREEN_WIDTH);
        tbScores.setPosition(0, SCREEN_HEIGHT - lbScore.getHeight() / 2);
        tbScores.padLeft(5).padRight(5);

        tbScores.add(lbScore).left();
        tbScores.add(lbCoin).right().expand().padRight(5);
        tbScores.add(imgCoin).right();

        // Gameover
        lbTryAgain = new Label("Try again", Assets.labelStyleGrande);
        lbTryAgain.setPosition(500, 310);
        lbTryAgain.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(GameScreen.class, game);
            }
        });

        lbShopScreen = new Label("Shop screen", Assets.labelStyleGrande);
        lbShopScreen.setPosition(500, 210);
        lbShopScreen.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(ShopScreen.class, game);
            }
        });

        lbLeaderboard = new Label("Leaderboard", Assets.labelStyleGrande);
        lbLeaderboard.setPosition(500, 110);


        buttonMusic = new Button(Assets.styleButtonMusica);
        buttonMusic.setPosition(5, 5);
        buttonMusic.setChecked(!Settings.isMusicOn);
        Gdx.app.log("Musica", Settings.isMusicOn + "");
        buttonMusic.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isMusicOn = !Settings.isMusicOn;
                buttonMusic.setChecked(!Settings.isMusicOn);
                if (Settings.isMusicOn)
                    Assets.music.play();
                else
                    Assets.music.stop();
                super.clicked(event, x, y);
            }
        });

        entranceAction(lbTryAgain, 310, .25f);
        entranceAction(lbShopScreen, 210, .5f);
        entranceAction(lbLeaderboard, 110, .85f);

        setAnimationChangeColor(lbShopScreen);
        setAnimationChangeColor(lbLeaderboard);
        setAnimationChangeColor(lbTryAgain);

        // Paused
        gpPaused = new Group();
        gpPaused.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

        Image background = new Image(Assets.pixelNegro);
        background.setSize(gpPaused.getWidth(), gpPaused.getHeight());

        Label lblPaused = new Label("Game Paused\nTouch to resume", Assets.labelStyleGrande);
        lblPaused.setPosition(gpPaused.getWidth() / 2f - lblPaused.getWidth() / 2f, gpPaused.getHeight() / 2f - lblPaused.getHeight() / 2f);
        lblPaused.setAlignment(Align.center);
        lblPaused.addAction(Actions.forever(Actions.sequence(Actions.alpha(.55f, .85f), Actions.alpha(1, .85f))));
        gpPaused.addActor(lblPaused);
        gpPaused.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setRunning();
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stageGame.getViewport().update(width, height, true);
    }

    @Override
    public void update(float delta) {

        if (state == STATE_RUNNING) {
            updateRunning(delta);
        }
    }

    private void updateRunning(float delta) {

        stageGame.act(delta);

        score = (int) trafficGame.score;
        coins = trafficGame.coins;
        if (trafficGame.state == TrafficGame.STATE_GAMEOVER) {
            setGameover();
        }

        if (!canSuperSpeed && trafficGame.canSuperSpeed) {
            canSuperSpeed = true;
            new SwipeVerticalTutorial(stage);
        }
        speedBar.updateActualLife(trafficGame.numCoinsForSuperSpeed);

        lbScore.setText("Distance " + score + "m");
        lbCoin.setText(coins + "");
    }

    private void setRunning() {
        stage.clear();
        state = STATE_RUNNING;
        stage.addActor(tbScores);
        stage.addActor(speedBar);
    }

    private void setGameover() {
        state = STATE_GAME_OVER;
        Settings.setNewScore(score);
        Settings.coinsTotal += coins;
        stage.clear();
        marcoGameOver = new MarcoGameOver(this, score, coins);
        stage.addActor(marcoGameOver);
        stage.addActor(lbTryAgain);
        stage.addActor(lbLeaderboard);
        stage.addActor(lbShopScreen);
        stage.addActor(buttonMusic);
    }

    private void setReady() {
        state = STATE_READY;

        final Label labelCounter = new Label(TIME_TO_START + "", Assets.labelStyleGrande);
        labelCounter.setFontScale(2.5f);
        labelCounter.setPosition(SCREEN_WIDTH / 2f - labelCounter.getWidth() / 2f, 600);
        labelCounter.setAlignment(Align.center);
        labelCounter.getColor().a = 0;
        labelCounter.addAction(Actions.repeat(3, Actions.sequence(Actions.fadeIn(1), Actions.run(new Runnable() {
            @Override
            public void run() {
                if (TIME_TO_START == 1)// Because the next time it is called it becomes 0
                    setRunning();
                TIME_TO_START--;
                labelCounter.setText(TIME_TO_START + "");
                labelCounter.getColor().a = 0;
            }
        }))));

        if (Settings.numeroVecesJugadas < 5) {
            stage.addActor(new SwipeHorizontalTutorial());
        }

        stage.addActor(labelCounter);
    }

    private void setPaused() {
        if (state == STATE_RUNNING || state == STATE_READY) {
            stage.clear();
            state = STATE_PAUSED;
            stage.addActor(gpPaused);
        }
    }

    @Override
    public void pause() {

        setPaused();
        super.pause();
    }

    @Override
    public void hide() {
        super.hide();
        stageGame.dispose();
    }

    @Override
    public void draw(float delta) {
        stageGame.draw();
    }

    @Override
    public void left() {
        trafficGame.oCar.tryMoveLeft();
        super.left();
    }

    @Override
    public void right() {
        trafficGame.oCar.tryMoveRight();
        super.right();
    }

    @Override
    public void up() {
        if (!canSuperSpeed)
            return;
        trafficGame.setSuperSpeed();
        canSuperSpeed = false;
        super.up();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.LEFT || keycode == Keys.A)
            trafficGame.oCar.tryMoveLeft();
        else if (keycode == Keys.RIGHT || keycode == Keys.D)
            trafficGame.oCar.tryMoveRight();
        else if (keycode == Keys.ESCAPE || keycode == Keys.BACK)
            changeScreenWithFadeOut(MainMenuScreen.class, game);
        else if (keycode == Keys.SPACE)
            trafficGame.setSuperSpeed();

        return true;
    }
}
