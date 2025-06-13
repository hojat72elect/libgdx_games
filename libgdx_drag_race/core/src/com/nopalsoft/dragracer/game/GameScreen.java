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

    Label labelScore, labelCoin;
    Table tableScores;

    Label labelTryAgain;
    Label labelShopScreen;
    Label labelLeaderboard;

    SpeedBar speedBar;
    private final Stage stageGame;
    private final TrafficGame trafficGame;
    int score, coins;

    boolean canSuperSpeed;

    Group groupPaused;
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
        speedBar = new SpeedBar(TrafficGame.NUM_COINS_FOR_SUPER_SPEED, 5, 720, 160, 20);

        labelScore = new Label("Distance 0m", Assets.labelStyleGrande);
        labelScore.setFontScale(.8f);

        labelCoin = new Label("0", Assets.labelStyleGrande);
        labelCoin.setFontScale(.8f);

        Image imgCoin = new Image(Assets.coinFrente);

        tableScores = new Table();
        tableScores.setWidth(SCREEN_WIDTH);
        tableScores.setPosition(0, SCREEN_HEIGHT - labelScore.getHeight() / 2);
        tableScores.padLeft(5).padRight(5);

        tableScores.add(labelScore).left();
        tableScores.add(labelCoin).right().expand().padRight(5);
        tableScores.add(imgCoin).right();

        // Gameover
        labelTryAgain = new Label("Try again", Assets.labelStyleGrande);
        labelTryAgain.setPosition(500, 310);
        labelTryAgain.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(GameScreen.class, game);
            }
        });

        labelShopScreen = new Label("Shop screen", Assets.labelStyleGrande);
        labelShopScreen.setPosition(500, 210);
        labelShopScreen.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(ShopScreen.class, game);
            }
        });

        labelLeaderboard = new Label("Leaderboard", Assets.labelStyleGrande);
        labelLeaderboard.setPosition(500, 110);


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

        entranceAction(labelTryAgain, 310, .25f);
        entranceAction(labelShopScreen, 210, .5f);
        entranceAction(labelLeaderboard, 110, .85f);

        setAnimationChangeColor(labelShopScreen);
        setAnimationChangeColor(labelLeaderboard);
        setAnimationChangeColor(labelTryAgain);

        // Paused
        groupPaused = new Group();
        groupPaused.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

        Image background = new Image(Assets.pixelNegro);
        background.setSize(groupPaused.getWidth(), groupPaused.getHeight());

        Label labelPause = new Label("Game Paused\nTouch to resume", Assets.labelStyleGrande);
        labelPause.setPosition(groupPaused.getWidth() / 2f - labelPause.getWidth() / 2f, groupPaused.getHeight() / 2f - labelPause.getHeight() / 2f);
        labelPause.setAlignment(Align.center);
        labelPause.addAction(Actions.forever(Actions.sequence(Actions.alpha(.55f, .85f), Actions.alpha(1, .85f))));
        groupPaused.addActor(labelPause);
        groupPaused.addListener(new ClickListener() {
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

        labelScore.setText("Distance " + score + "m");
        labelCoin.setText(coins + "");
    }

    private void setRunning() {
        stage.clear();
        state = STATE_RUNNING;
        stage.addActor(tableScores);
        stage.addActor(speedBar);
    }

    private void setGameover() {
        state = STATE_GAME_OVER;
        Settings.setNewScore(score);
        Settings.coinsTotal += coins;
        stage.clear();
        marcoGameOver = new MarcoGameOver(this, score, coins);
        stage.addActor(marcoGameOver);
        stage.addActor(labelTryAgain);
        stage.addActor(labelLeaderboard);
        stage.addActor(labelShopScreen);
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
            stage.addActor(groupPaused);
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
