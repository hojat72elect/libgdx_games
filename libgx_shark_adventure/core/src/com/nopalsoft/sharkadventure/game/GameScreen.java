package com.nopalsoft.sharkadventure.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.nopalsoft.sharkadventure.Achievements;
import com.nopalsoft.sharkadventure.Assets;
import com.nopalsoft.sharkadventure.Settings;
import com.nopalsoft.sharkadventure.SharkAdventureGame;
import com.nopalsoft.sharkadventure.scene2d.GameUI;
import com.nopalsoft.sharkadventure.scene2d.MenuUI;
import com.nopalsoft.sharkadventure.scene2d.VentanaPause;
import com.nopalsoft.sharkadventure.screens.Screens;

public class GameScreen extends Screens {
    final int STATE_MENU = 0;// Menu principal
    final int STATE_RUNNING = 1;// The game begins
    final int STATE_PAUSED = 2;// Pause
    final int STATE_GAME_OVER = 3;// Same as the main menu but without the title.
    int state;

    GameWorld oWorld;
    WorldRenderer renderer;

    GameUI gameUI;
    MenuUI menuUI;
    VentanaPause vtPause;

    public long puntuacion;

    /**
     * @param showMainMenu Shows the main menu otherwise starts the game immediately
     */
    public GameScreen(SharkAdventureGame game, boolean showMainMenu) {
        super(game);
        oWorld = new GameWorld();
        renderer = new WorldRenderer(batch, oWorld);
        gameUI = new GameUI(this, oWorld);
        menuUI = new MenuUI(this, oWorld);
        vtPause = new VentanaPause(this);

        Assets.reloadFondo();

        if (showMainMenu) {
            state = STATE_MENU;
            menuUI.show(stage, true);
        } else {
            setRunning(false);
        }

        Achievements.tryAgainAchievements();
    }

    @Override
    public void update(float delta) {
        switch (state) {
            case STATE_RUNNING:
                updateRunning(delta);
                break;
            case STATE_MENU:
                updateStateMenu(delta);
                break;
        }
    }

    private void updateRunning(float delta) {
        if (Gdx.input.isKeyPressed(Keys.A))
            gameUI.accelerationX = -1;

        else if (Gdx.input.isKeyPressed(Keys.D))
            gameUI.accelerationX = 1;

        if (Gdx.input.isKeyJustPressed(Keys.W) || Gdx.input.isKeyJustPressed(Keys.SPACE))
            gameUI.didSwimUp = true;

        if (Gdx.input.isKeyJustPressed(Keys.CONTROL_RIGHT) || Gdx.input.isKeyJustPressed(Keys.CONTROL_RIGHT) || Gdx.input.isKeyJustPressed(Keys.F))
            gameUI.didFire = true;

        oWorld.update(delta, gameUI.accelerationX, gameUI.didSwimUp, gameUI.didFire);

        puntuacion = (long) oWorld.puntuacion;

        gameUI.lifeBar.updateActualNum(oWorld.oTiburon.life);
        gameUI.energyBar.updateActualNum(oWorld.oTiburon.energy);

        gameUI.didSwimUp = false;
        gameUI.didFire = false;

        if (oWorld.state == GameWorld.STATE_GAMEOVER) {
            setGameOver();
        }
    }

    private void updateStateMenu(float delta) {
        oWorld.oTiburon.updateStateTime(delta);
    }

    public void setRunning(boolean removeMenu) {
        Runnable runAfterHideMenu = () -> {
            Runnable run = () -> state = STATE_RUNNING;
            gameUI.addAction(Actions.sequence(Actions.delay(GameUI.ANIMATION_TIME), Actions.run(run)));
            gameUI.show(stage);
        };

        if (removeMenu) {
            menuUI.addAction(Actions.sequence(Actions.delay(MenuUI.ANIMATION_TIME), Actions.run(runAfterHideMenu)));
            menuUI.removeWithAnimations();
        } else {
            stage.addAction(Actions.run(runAfterHideMenu));
        }
    }

    private void setGameOver() {
        if (state != STATE_GAME_OVER) {
            state = STATE_GAME_OVER;
            Runnable runAfterHideGameUI = () -> menuUI.show(stage, false);

            Settings.setBestScore(puntuacion);

            gameUI.addAction(Actions.sequence(Actions.delay(MenuUI.ANIMATION_TIME), Actions.run(runAfterHideGameUI)));
            gameUI.removeWithAnimations();

            Settings.numberOfTimesPlayed++;
        }
    }

    public void setPaused() {
        if (state == STATE_RUNNING) {
            state = STATE_PAUSED;
            gameUI.removeWithAnimations();
            vtPause.show(stage);
        }
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.A || keycode == Keys.D)
            gameUI.accelerationX = 0;
        return super.keyUp(keycode);
    }

    @Override
    public void draw(float delta) {

        if (state == STATE_PAUSED || state == STATE_GAME_OVER)
            delta = 0;

        renderer.render(delta);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.enableBlending();
        batch.begin();

        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.BACK | keycode == Keys.ESCAPE) {
            if (state == STATE_RUNNING) {
                setPaused();
            } else if (state == STATE_PAUSED) {
                vtPause.hide();
                setRunning(false);
            } else if (state == STATE_MENU) {
                Gdx.app.exit();
            }
            return true;
        }


        if (keycode == Keys.P) {
            game.setScreen(new GameScreen(game, false));
            return true;
        }
        return super.keyDown(keycode);
    }
}
