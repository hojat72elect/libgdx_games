package com.nopalsoft.ninjarunner.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.nopalsoft.ninjarunner.Assets;
import com.nopalsoft.ninjarunner.Settings;
import com.nopalsoft.ninjarunner.leaderboard.NextGoalFrame;
import com.nopalsoft.ninjarunner.leaderboard.Person;
import com.nopalsoft.ninjarunner.scene2d.GameUI;
import com.nopalsoft.ninjarunner.scene2d.MenuUI;
import com.nopalsoft.ninjarunner.screens.Screens;

public class GameScreen extends Screens {
    static final int STATE_MENU = 0;
    static final int STATE_RUNNING = 1;
    static final int STATE_GAME_OVER = 2;
    public GameWorld gameWorld;
    int state;
    GameUI gameUI;
    MenuUI menuUI;
    WorldGameRenderer renderer;

    NextGoalFrame nextGoalFrame;

    public GameScreen(Game game, boolean showMainMenu) {
        super(game);
        gameWorld = new GameWorld();
        renderer = new WorldGameRenderer(batch, gameWorld);
        gameUI = new GameUI(this, gameWorld);
        menuUI = new MenuUI(this, gameWorld);

        if (showMainMenu) {
            state = STATE_MENU;
            menuUI.show(stage, true);
        } else {
            setRunning(false);
        }

        // I always try to load in interstitial at the start of the game
        this.game.requestHandler.loadInterstitial();
    }

    public void setRunning(boolean removeMenu) {
        Runnable runAfterHideMenu = () -> {
            Runnable run = () -> {
                state = STATE_RUNNING;
                if (Settings.isMusicEnabled) {
                    Assets.music1.play();
                }

                nextGoalFrame = new NextGoalFrame(SCREEN_WIDTH, 400);
                setNextGoalFrame(0);
            };
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

    @Override
    public void update(float delta) {

        if (state == STATE_MENU) {
            gameWorld.player.updateStateTime(delta);
            gameWorld.oMascot.updateStateTime(delta);
        } else if (state == STATE_RUNNING) {
            boolean isJumpPressed = false;

            gameWorld.update(delta, gameUI.didJump, isJumpPressed, gameUI.didDash, gameUI.didSlide);

            gameUI.didJump = false;
            gameUI.didDash = false;

            if (gameWorld.state == GameWorld.STATE_GAMEOVER) {
                setGameover();
            }

            setNextGoalFrame(gameWorld.puntuacion);
        } else if (state == STATE_GAME_OVER) {
            if (Gdx.input.justTouched()) {
                game.setScreen(new GameScreen(game, true));
            }
        }
    }


    public void setNextGoalFrame(long puntos) {
        // So that only people you haven't passed yet are shown
        if (puntos < Settings.bestScore)
            puntos = Settings.bestScore;

        game.arrayPerson.sort(); // Arrange from highest score to lowest score


        Person tempPerson = null;
        // I calculate the position of the player with the most points. For example, if I'm in fifth place, this should be the position for fourth place.
        int posicionArribaDeMi = game.arrayPerson.size - 1;
        // The arrangement is ordered from largest to smallest.
        for (; posicionArribaDeMi >= 0; posicionArribaDeMi--) {
            Person obj = game.arrayPerson.get(posicionArribaDeMi);
            if (obj.isMe)
                continue;

            if (obj.score > puntos) {
                tempPerson = obj;
                break;
            }
        }

        final Person person = tempPerson;

        if (person == null)
            return;

        if (person.equals(nextGoalFrame.person))
            return;


        Runnable run = () -> {
            nextGoalFrame.updatePersona(person);
            nextGoalFrame.addAction(Actions.sequence(Actions.moveTo(SCREEN_WIDTH - NextGoalFrame.WIDTH, nextGoalFrame.getY(), 1)));
        };

        if (!nextGoalFrame.hasParent()) {
            stage.addActor(nextGoalFrame);
            Gdx.app.postRunnable(run);
        } else if (!nextGoalFrame.hasActions()) {
            nextGoalFrame.addAction(Actions.sequence(Actions.moveTo(SCREEN_WIDTH, nextGoalFrame.getY(), 1), Actions.run(run)));
        }
    }

    private void setGameover() {
        Settings.setNewScore(gameWorld.puntuacion);
        state = STATE_GAME_OVER;
        Assets.music1.stop();
    }

    @Override
    public void right() {
        super.right();
        gameUI.didDash = true;
    }

    @Override
    public void draw(float delta) {

        if (state == STATE_MENU) {
            Assets.cloudsParallaxBackground.render(0);
        } else {
            Assets.cloudsParallaxBackground.render(delta);
        }

        renderer.render(delta);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        Assets.smallFont.draw(batch, "FPS GERA" + Gdx.graphics.getFramesPerSecond(), 5, 20);
        Assets.smallFont.draw(batch, "Bodies " + gameWorld.world.getBodyCount(), 5, 40);
        Assets.smallFont.draw(batch, "Lives " + gameWorld.player.vidas, 5, 60);
        Assets.smallFont.draw(batch, "Coins " + gameWorld.monedasTomadas, 5, 80);
        Assets.smallFont.draw(batch, "Scores " + gameWorld.puntuacion, 5, 100);
        Assets.smallFont.draw(batch, "Distance " + gameWorld.player.position.x, 5, 120);
        Assets.smallFont.draw(batch, "Platforms " + gameWorld.arrPlataformas.size, 5, 140);

        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.R) {
            game.setScreen(new GameScreen(game, true));
            return true;
        } else if (keycode == Keys.SPACE || keycode == Keys.W || keycode == Keys.UP) {
            gameUI.didJump = true;
            return true;
        } else if (keycode == Keys.S || keycode == Keys.DOWN) {
            gameUI.didSlide = true;
            return true;
        } else if (keycode == Keys.BACK) {
            Gdx.app.exit();
            return true;
        } else if (keycode == Keys.P) {
            if (game.arrayPerson != null) {
                setNextGoalFrame(0);
            }
            return true;
        }
        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.S || keycode == Keys.DOWN) {
            gameUI.didSlide = false;
            return true;
        }
        return super.keyUp(keycode);
    }

    @Override
    public void hide() {
        // noting to do here
    }
}
