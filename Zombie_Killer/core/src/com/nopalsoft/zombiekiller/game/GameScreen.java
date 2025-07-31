package com.nopalsoft.zombiekiller.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.zombiekiller.Achievements;
import com.nopalsoft.zombiekiller.Assets;
import com.nopalsoft.zombiekiller.MainZombie;
import com.nopalsoft.zombiekiller.Settings;
import com.nopalsoft.zombiekiller.scene2d.TouchPadControls;
import com.nopalsoft.zombiekiller.scene2d.NumGemsBar;
import com.nopalsoft.zombiekiller.scene2d.OverlayTutorial;
import com.nopalsoft.zombiekiller.scene2d.ProgressBarUI;
import com.nopalsoft.zombiekiller.scene2d.SkullBar;
import com.nopalsoft.zombiekiller.scene2d.DialogGameover;
import com.nopalsoft.zombiekiller.scene2d.DialogNextLevel;
import com.nopalsoft.zombiekiller.scene2d.DialogPause;
import com.nopalsoft.zombiekiller.scene2d.DialogRate;
import com.nopalsoft.zombiekiller.screens.Screens;

public class GameScreen extends Screens {
    static final int STATE_RUNNING = 0;
    static final int STATE_GAME_OVER = 1;
    static final int STATE_NEXT_LEVEL = 2;
    static final int STATE_PAUSED = 3;
    public WorldGame worldGame;
    public Button buttonJump, buttonFire;
    public Touchpad touchpad;
    public TouchPadControls touchPadControls;
    public int level;
    int state;
    WorldGameRenderer2 renderer;
    float accelerationX, accelerationY;
    boolean didJump;
    boolean isFiring;
    Button buttonPause;
    DialogGameover gameOverDialog;
    DialogPause pauseDialog;
    OverlayTutorial overlayTutorial;
    Label labelLevel;

    ProgressBarUI lifeBar;
    ProgressBarUI shieldBar;
    NumGemsBar numGemsBar;
    SkullBar skullBar;

    public GameScreen(MainZombie game, int level) {
        super(game);

        if (music != null) {
            music.stop();
            music.dispose();
            music = null;
        }

        switch (MathUtils.random(3)) {
            case 0:
                music = Gdx.audio.newMusic(Gdx.files.internal("data/sounds/music1.mp3"));
                break;
            case 1:
                music = Gdx.audio.newMusic(Gdx.files.internal("data/sounds/music2.mp3"));
                break;
            case 2:
                music = Gdx.audio.newMusic(Gdx.files.internal("data/sounds/DST-Legends.mp3"));
                break;
            case 3:
                music = Gdx.audio.newMusic(Gdx.files.internal("data/sounds/DST-Robotical.mp3"));
                break;
        }

        music.setLooping(true);

        if (Settings.isMusicOn)
            music.play();

        this.level = level;
        Gdx.app.log("Map", level + "");
        labelLevel = new Label(game.idiomas.get("world") + " " + (level + 1), Assets.labelStyleChico);
        labelLevel.setPosition(SCREEN_WIDTH / 2f - labelLevel.getWidth() / 2f, 391);

        state = STATE_RUNNING;
        Settings.numeroVecesJugadas++;

        gameOverDialog = new DialogGameover(this);
        pauseDialog = new DialogPause(this);

        worldGame = new WorldGame();
        renderer = new WorldGameRenderer2(batcher, worldGame);

        lifeBar = new ProgressBarUI(Assets.redBar, Assets.itemHeart, worldGame.hero.lives, 20, 440);
        shieldBar = new ProgressBarUI(Assets.whiteBar, Assets.itemShield, worldGame.hero.MAX_SHIELDS, worldGame.hero.shield, 20, 395);
        numGemsBar = new NumGemsBar(Assets.itemGem, 20, 350);
        skullBar = new SkullBar(SCREEN_WIDTH / 2f, 415);

        buttonJump = new Button(Assets.btUp);
        buttonJump.setSize(Settings.buttonSize, Settings.buttonSize);
        buttonJump.setPosition(Settings.buttonJumpPositionX, Settings.buttonJumpPositionY);
        buttonJump.getColor().a = .5f;
        addEfectoPress(buttonJump);
        buttonJump.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                didJump = true;
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        buttonFire = new Button(Assets.btFire);
        buttonFire.setSize(Settings.buttonSize, Settings.buttonSize);
        buttonFire.setPosition(Settings.buttonFirePositionX, Settings.buttonFirePositionY);
        buttonFire.getColor().a = .5f;
        addEfectoPress(buttonFire);
        buttonFire.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isFiring = true;
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        buttonPause = new Button(Assets.btPause);
        buttonPause.setSize(45, 45);
        buttonPause.setPosition(SCREEN_WIDTH - 50, SCREEN_HEIGHT - 50);
        addEfectoPress(buttonPause);
        buttonPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPaused();
            }
        });

        touchpad = new Touchpad(5, Assets.touchPadStyle);
        touchpad.setPosition(Settings.padPositionX, Settings.padPositionY);
        touchpad.setSize(Settings.padSize, Settings.padSize);
        touchpad.getColor().a = .5f;

        touchPadControls = new TouchPadControls();
        touchPadControls.setPosition(Settings.padPositionX, Settings.padPositionY);
        touchPadControls.getColor().a = .5f;

        if (Gdx.app.getType() == ApplicationType.Android || Gdx.app.getType() == ApplicationType.iOS) {
            stage.addActor(buttonFire);
            stage.addActor(buttonJump);

            if (Settings.isPadEnabled)
                stage.addActor(touchpad);
            else
                stage.addActor(touchPadControls);
        }
        stage.addActor(buttonPause);
        stage.addActor(lifeBar);
        stage.addActor(shieldBar);
        stage.addActor(numGemsBar);
        stage.addActor(skullBar);
        stage.addActor(labelLevel);

        overlayTutorial = new OverlayTutorial(this);
        if (level == 0) {
            overlayTutorial.show(stage);
        }

        if (Settings.numeroVecesJugadas % 7 == 0 && !Settings.didRate) {
            setPaused();
            new DialogRate(this).show(stage);
        }
    }

    @Override
    public void update(float delta) {

        if (state == STATE_RUNNING) {
            updateRunning(delta);
        }
    }

    private void updateRunning(float delta) {
        if (overlayTutorial.isVisible)
            return;

        float sensibility = .6f;

        if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT) || touchpad.getKnobPercentX() < -sensibility
                || touchPadControls.isMovingLeft)
            accelerationX = -1;
        else if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT) || touchpad.getKnobPercentX() > sensibility
                || touchPadControls.isMovingRight)
            accelerationX = 1;
        else
            accelerationX = 0;

        if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP) || touchpad.getKnobPercentY() > sensibility || touchPadControls.isMovingUp)
            accelerationY = 1;
        else if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN) || touchpad.getKnobPercentY() < -sensibility
                || touchPadControls.isMovingDown)
            accelerationY = -1;
        else
            accelerationY = 0;

        if (Gdx.input.isKeyPressed(Keys.F))
            isFiring = true;

        worldGame.update(delta, didJump, isFiring, accelerationX, accelerationY);

        lifeBar.updateActualNum(worldGame.hero.lives);
        shieldBar.updateActualNum(worldGame.hero.shield);
        numGemsBar.updateNumGems(worldGame.gems);
        skullBar.tryToUpdateSkulls(worldGame.skulls);

        if (worldGame.state == WorldGame.STATE_GAMEOVER) {
            setGameover();
        } else if (worldGame.state == WorldGame.STATE_NEXT_LEVEL) {
            setNextLevel();
        }

        isFiring = didJump = false;
    }

    private void setGameover() {
        state = STATE_GAME_OVER;
        gameOverDialog.show(stage);
    }

    private void setNextLevel() {
        state = STATE_NEXT_LEVEL;
        Settings.saveLevel(level, worldGame.skulls);

        Achievements.unlockCollectedSkulls();

        new DialogNextLevel(this).show(stage);
    }

    private void setPaused() {
        if (state == STATE_RUNNING) {
            state = STATE_PAUSED;
            pauseDialog.show(stage);
        }
    }

    @Override
    public void hide() {

        Settings.zombiesKilled += worldGame.totalZombiesKilled;
        Achievements.unlockKilledZombies();
        super.hide();
    }

    public void setRunning() {
        state = STATE_RUNNING;
    }

    @Override
    public boolean keyUp(int keycode) {
        return super.keyUp(keycode);
    }

    @Override
    public void draw(float delta) {
        renderer.render();

        oCam.update();
        batcher.setProjectionMatrix(oCam.combined);
        batcher.begin();
        batcher.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.SPACE || keycode == Keys.X || keycode == Keys.C || keycode == Keys.V || keycode == Keys.B || keycode == Keys.N
                || keycode == Keys.M) {
            didJump = true;
            return true;
        } else if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            if (pauseDialog.isVisible())
                pauseDialog.hide();
            else
                setPaused();
            return true;
        }
        return false;
    }
}
