package com.nopalsoft.zombiedash.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.zombiedash.Achievements;
import com.nopalsoft.zombiedash.Assets;
import com.nopalsoft.zombiedash.MainZombieDash;
import com.nopalsoft.zombiedash.Settings;
import com.nopalsoft.zombiedash.scene2d.NumItemsBar;
import com.nopalsoft.zombiedash.scene2d.OverlayTutorial;
import com.nopalsoft.zombiedash.scene2d.ProgressBarUI;
import com.nopalsoft.zombiedash.scene2d.VentanaGameover;
import com.nopalsoft.zombiedash.scene2d.VentanaPause;
import com.nopalsoft.zombiedash.scene2d.VentanaRate;
import com.nopalsoft.zombiedash.scene2d.VentanaRevive;
import com.nopalsoft.zombiedash.screens.Screens;

public class GameScreen extends Screens {
    static final int STATE_RUNNING = 0;
    static final int STATE_GAME_OVER = 1;
    static final int STATE_CHECK_REVIVE = 2;
    static final int STATE_PAUSED = 3;
    int state;

    public WorldGame oWorld;
    WorldGameRenderer2 renderer;

    public Button btJump, btFire;
    boolean didJump;
    boolean didFire;
    boolean isJumpPressed;

    Button btPause;

    VentanaPause ventanaPause;

    ProgressBarUI lifeBar;
    ProgressBarUI shieldBar;
    NumItemsBar numGemsBar;
    NumItemsBar numBulletsBar;

    Label lbDistance;

    int numRevives;

    OverlayTutorial overlayTutorial;

    public GameScreen(MainZombieDash game) {
        super(game);
        Achievements.tryAgainAchievements();

        switch (MathUtils.random(3)) {
            case 0:
                music = Gdx.audio.newMusic(Gdx.files.internal("data/Sounds/music1.mp3"));
                break;
            case 1:
                music = Gdx.audio.newMusic(Gdx.files.internal("data/Sounds/music2.mp3"));
                break;
            case 2:
                music = Gdx.audio.newMusic(Gdx.files.internal("data/Sounds/DST-Legends.mp3"));
                break;
            case 3:
                music = Gdx.audio.newMusic(Gdx.files.internal("data/Sounds/DST-Robotical.mp3"));
                break;
        }

        music.setLooping(true);

        if (Settings.isMusicOn)
            music.play();

        Settings.numeroVecesJugadas++;

        state = STATE_RUNNING;
        numRevives = 0;

        ventanaPause = new VentanaPause(this);

        oWorld = new WorldGame();
        renderer = new WorldGameRenderer2(batcher, oWorld);

        lifeBar = new ProgressBarUI(Assets.redBar, Assets.itemHeart, oWorld.oHero.vidas, 20, 440);
        shieldBar = new ProgressBarUI(Assets.whiteBar, Assets.itemShield, oWorld.oHero.MAX_SHIELD, oWorld.oHero.shield, 20, 395);
        numGemsBar = new NumItemsBar(Assets.itemGem, 20, 350);
        numBulletsBar = new NumItemsBar(Assets.weaponSmall, 20, 305);

        lbDistance = new Label("0 m", Assets.labelStyleGrande);
        lbDistance.setAlignment(Align.center);
        lbDistance.setPosition(SCREEN_WIDTH / 2f - lbDistance.getWidth() / 2f, 445);

        btJump = new Button(Assets.btJump);
        btJump.setSize(Settings.buttonSize, Settings.buttonSize);
        btJump.setPosition(Settings.buttonJumpPositionX, Settings.buttonJumpPositionY);
        btJump.getColor().a = .5f;
        addEfectoPress(btJump);
        btJump.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                didJump = true;
                isJumpPressed = true;
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isJumpPressed = false;
                super.touchUp(event, x, y, pointer, button);
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
                didFire = true;
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

        stage.addActor(lifeBar);
        stage.addActor(shieldBar);
        stage.addActor(numGemsBar);
        stage.addActor(numBulletsBar);
        stage.addActor(lbDistance);
        stage.addActor(btFire);
        stage.addActor(btJump);
        stage.addActor(btPause);

        if (Settings.numeroVecesJugadas % 7 == 0 && !Settings.didRate) {
            setPaused();
            new VentanaRate(this).show(stage);
        }

        overlayTutorial = new OverlayTutorial(this);
        if (Settings.showHelp) {
            overlayTutorial.show(stage);
            Settings.showHelp = false;
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

        oWorld.update(delta, didJump, isJumpPressed, didFire);

        lifeBar.updateActualNum(oWorld.oHero.vidas);
        shieldBar.updateActualNum(oWorld.oHero.shield);
        numGemsBar.updateNumGems(oWorld.gems);
        numBulletsBar.updateNumGems(Settings.numBullets);
        lbDistance.setText((int) oWorld.distance + " m");
        Achievements.distance((int) oWorld.distance, oWorld.oHero.didGetHurtAtLeastOnce);

        if (oWorld.state == WorldGame.STATE_GAMEOVER) {
            checkRevive();
        }

        didFire = didJump = false;
    }

    @Override
    public void draw(float delta) {

        if (state == STATE_RUNNING)
            Assets.parallaxBackground.render(delta);
        else
            Assets.parallaxBackground.render(0);
        renderer.render(delta);

        oCam.update();
        batcher.setProjectionMatrix(oCam.combined);

        batcher.begin();
        // Assets.fontGrande.draw(batcher, Gdx.graphics.getFramesPerSecond() + "", 10, 200);
        batcher.end();

    }

    protected void setPaused() {
        if (state == STATE_RUNNING) {
            state = STATE_PAUSED;
            ventanaPause.show(stage);
        }

    }

    public void setGameover() {
        state = STATE_GAME_OVER;
        game.gameServiceHandler.submitScore((int) oWorld.distance);
        Settings.setBestScore((int) oWorld.distance);
        new VentanaGameover(this).show(stage);
    }

    public void setRunning() {
        state = STATE_RUNNING;

    }

    public void checkRevive() {
        state = STATE_CHECK_REVIVE;
        int price = (numRevives + 1) * 500;

        if (price < Settings.gemsTotal) {
            new VentanaRevive(this, price).show(stage);
        } else {
            setGameover();
        }

    }

    public void setRevive() {
        numRevives++;
        state = STATE_RUNNING;
        oWorld.state = WorldGame.STATE_RUNNING;
        oWorld.oHero.revive();
    }

    @Override
    public void hide() {
        if (Settings.numeroVecesJugadas % 10 == 0)
            game.reqHandler.showInterstitial();
        super.hide();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.SPACE) {
            didJump = true;
            isJumpPressed = true;
            return true;
        } else if (keycode == Keys.F) {
            didFire = true;
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

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.SPACE) {
            isJumpPressed = false;
            return true;
        }
        return false;
    }

}
