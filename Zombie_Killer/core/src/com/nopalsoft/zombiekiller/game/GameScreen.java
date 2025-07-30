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
    public WorldGame oWorld;
    public Button btJump, btFire;
    public Touchpad pad;
    public TouchPadControls touchPadControls;
    public int level;
    int state;
    WorldGameRenderer2 renderer;
    float accelX, accelY;
    boolean didJump;
    boolean isFiring;
    Button btPause;
    DialogGameover ventanaGameover;
    DialogPause ventanaPause;
    OverlayTutorial overlayTutorial;
    Label lbLevel;

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
        lbLevel = new Label(game.idiomas.get("world") + " " + (level + 1), Assets.labelStyleChico);
        lbLevel.setPosition(SCREEN_WIDTH / 2f - lbLevel.getWidth() / 2f, 391);

        state = STATE_RUNNING;
        Settings.numeroVecesJugadas++;

        ventanaGameover = new DialogGameover(this);
        ventanaPause = new DialogPause(this);

        oWorld = new WorldGame();
        renderer = new WorldGameRenderer2(batcher, oWorld);

        lifeBar = new ProgressBarUI(Assets.redBar, Assets.itemHeart, oWorld.oHero.vidas, 20, 440);
        shieldBar = new ProgressBarUI(Assets.whiteBar, Assets.itemShield, oWorld.oHero.MAX_SHIELD, oWorld.oHero.shield, 20, 395);
        numGemsBar = new NumGemsBar(Assets.itemGem, 20, 350);
        skullBar = new SkullBar(SCREEN_WIDTH / 2f, 415);

        btJump = new Button(Assets.btUp);
        btJump.setSize(Settings.buttonSize, Settings.buttonSize);
        btJump.setPosition(Settings.buttonJumpPositionX, Settings.buttonJumpPositionY);
        btJump.getColor().a = .5f;
        addEfectoPress(btJump);
        btJump.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                didJump = true;
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        btFire = new Button(Assets.btFire);
        btFire.setSize(Settings.buttonSize, Settings.buttonSize);
        btFire.setPosition(Settings.buttonFirePositionX, Settings.buttonFirePositionY);
        btFire.getColor().a = .5f;
        addEfectoPress(btFire);
        btFire.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                isFiring = true;
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        btPause = new Button(Assets.btPause);
        btPause.setSize(45, 45);
        btPause.setPosition(SCREEN_WIDTH - 50, SCREEN_HEIGHT - 50);
        addEfectoPress(btPause);
        btPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPaused();
            }
        });

        pad = new Touchpad(5, Assets.touchPadStyle);
        pad.setPosition(Settings.padPositionX, Settings.padPositionY);
        pad.setSize(Settings.padSize, Settings.padSize);
        pad.getColor().a = .5f;

        touchPadControls = new TouchPadControls();
        touchPadControls.setPosition(Settings.padPositionX, Settings.padPositionY);
        touchPadControls.getColor().a = .5f;

        if (Gdx.app.getType() == ApplicationType.Android || Gdx.app.getType() == ApplicationType.iOS) {
            stage.addActor(btFire);
            stage.addActor(btJump);

            if (Settings.isPadEnabled)
                stage.addActor(pad);
            else
                stage.addActor(touchPadControls);
        }
        stage.addActor(btPause);
        stage.addActor(lifeBar);
        stage.addActor(shieldBar);
        stage.addActor(numGemsBar);
        stage.addActor(skullBar);
        stage.addActor(lbLevel);

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

        if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT) || pad.getKnobPercentX() < -sensibility
                || touchPadControls.isMovingLeft)
            accelX = -1;
        else if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT) || pad.getKnobPercentX() > sensibility
                || touchPadControls.isMovingRight)
            accelX = 1;
        else
            accelX = 0;

        if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP) || pad.getKnobPercentY() > sensibility || touchPadControls.isMovingUp)
            accelY = 1;
        else if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN) || pad.getKnobPercentY() < -sensibility
                || touchPadControls.isMovingDown)
            accelY = -1;
        else
            accelY = 0;

        if (Gdx.input.isKeyPressed(Keys.F))
            isFiring = true;

        oWorld.update(delta, didJump, isFiring, accelX, accelY);

        lifeBar.updateActualNum(oWorld.oHero.vidas);
        shieldBar.updateActualNum(oWorld.oHero.shield);
        numGemsBar.updateNumGems(oWorld.gems);
        skullBar.tryToUpdateSkulls(oWorld.skulls);

        if (oWorld.state == WorldGame.STATE_GAMEOVER) {
            setGameover();
        } else if (oWorld.state == WorldGame.STATE_NEXT_LEVEL) {
            setNextLevel();
        }

        isFiring = didJump = false;
    }

    private void setGameover() {
        state = STATE_GAME_OVER;
        ventanaGameover.show(stage);
    }

    private void setNextLevel() {
        state = STATE_NEXT_LEVEL;
        Settings.saveLevel(level, oWorld.skulls);

        Achievements.unlockCollectedSkulls();

        new DialogNextLevel(this).show(stage);
    }

    private void setPaused() {
        if (state == STATE_RUNNING) {
            state = STATE_PAUSED;
            ventanaPause.show(stage);
        }
    }

    @Override
    public void hide() {

        Settings.zombiesKilled += oWorld.totalZombiesKilled;
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
            if (ventanaPause.isVisible())
                ventanaPause.hide();
            else
                setPaused();
            return true;
        }
        return false;
    }
}
