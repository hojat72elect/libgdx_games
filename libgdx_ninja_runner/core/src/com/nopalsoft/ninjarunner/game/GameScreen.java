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

/**
 * Created by Yayo on 1/21/15.
 */
public class GameScreen extends Screens {
    static final int STATE_MENU = 0;
    static final int STATE_RUNNING = 1;
    static final int STATE_GAME_OVER = 2;
    static final int STATE_CHECK_REVIVE = 3;
    static final int STATE_PAUSED = 4;
    public WorldGame oWorld;
    int state;
    GameUI gameUI;
    MenuUI menuUI;
    WorldGameRenderer renderer;

    NextGoalFrame nextGoalFrame;

    public GameScreen(Game _game, boolean showMainMenu) {
        super(_game);
        oWorld = new WorldGame();
        renderer = new WorldGameRenderer(batcher, oWorld);
        gameUI = new GameUI(this, oWorld);
        menuUI = new MenuUI(this, oWorld);
        // vtPause = new VentanaPause(this);

        if (showMainMenu) {
            state = STATE_MENU;
            menuUI.show(stage, showMainMenu);
        } else {
            setRunning(false);
        }

        // Siempre intento cargar in interstitial al inicio del juego
        game.reqHandler.loadInterstitial();


        if (game.facebookHandler.facebookIsSignedIn())
            game.facebookHandler.facebookGetScores();

        if (game.gameServiceHandler.isSignedIn())
            game.gameServiceHandler.getScores();


    }

    public void setRunning(boolean removeMenu) {
        Runnable runAfterHideMenu = new Runnable() {
            @Override
            public void run() {
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        state = STATE_RUNNING;
                        if (Settings.isMusicEnabled) {
                            Assets.musica1.play();
                        }

                        nextGoalFrame = new NextGoalFrame(SCREEN_WIDTH, 400);
                        setNextGoalFrame(0);


                    }
                };
                gameUI.addAction(Actions.sequence(Actions.delay(GameUI.ANIMATION_TIME), Actions.run(run)));
                gameUI.show(stage);
            }
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
            oWorld.oPersonaje.updateStateTime(delta);
            oWorld.oMascota.updateStateTime(delta);

        } else if (state == STATE_RUNNING) {
            boolean isJumpPressed = false;

            oWorld.update(delta, gameUI.didJump, isJumpPressed, gameUI.didDash, gameUI.didSlide);

            gameUI.didJump = false;
            gameUI.didDash = false;

            if (oWorld.state == WorldGame.STATE_GAMEOVER) {
                setGameover();
            }

            setNextGoalFrame(oWorld.puntuacion);
            nextGoalFrame.updatePuntuacion(oWorld.puntuacion);

        } else if (state == STATE_GAME_OVER) {
            if (Gdx.input.justTouched()) {
                game.setScreen(new GameScreen(game, true));
            }
        }


    }


    public void setNextGoalFrame(long puntos) {
        //Para que solo se muestren las personas que no haya superado aun
        if (puntos < Settings.bestScore)
            puntos = Settings.bestScore;

        game.arrPerson.sort(); // Acomoda de mayor puntuacion a menor puntuacion


        Person oPersonAux = null;
        //Calculo la posicion del jugador que tenga mas puntos que yo. Por ejemplo si yo voy en quinto lugar
        //esta debe ser la pocion del cuarto lugar.
        int posicionArribaDeMi = game.arrPerson.size - 1;
        //El arreglo esta ordenado de mayor a menor
        for (; posicionArribaDeMi >= 0; posicionArribaDeMi--) {
            Person obj = game.arrPerson.get(posicionArribaDeMi);
            if (obj.isMe)
                continue;

            if (obj.score > puntos) {
                oPersonAux = obj;
                break;
            }
        }

        final Person oPersona = oPersonAux;

        if (oPersona == null)
            return;

        if (oPersona.equals(nextGoalFrame.oPersona))
            return;


        Runnable run = new Runnable() {
            @Override
            public void run() {
                nextGoalFrame.updatePersona(oPersona);
                nextGoalFrame.addAction(Actions.sequence(Actions.moveTo(SCREEN_WIDTH - NextGoalFrame.WIDTH, nextGoalFrame.getY(), 1)));

            }
        };

        if (!nextGoalFrame.hasParent()) {
            stage.addActor(nextGoalFrame);
            Gdx.app.postRunnable(run);
            return;
        } else if (!nextGoalFrame.hasActions()) {
            nextGoalFrame.addAction(Actions.sequence(Actions.moveTo(SCREEN_WIDTH, nextGoalFrame.getY(), 1), Actions.run(run)));
            return;
        }
    }

    private void setGameover() {
        game.gameServiceHandler.submitScore(oWorld.puntuacion);
        game.facebookHandler.facebookSubmitScore(oWorld.puntuacion);
        Settings.setNewScore(oWorld.puntuacion);
        state = STATE_GAME_OVER;
        Assets.musica1.stop();

    }

    @Override
    public void right() {
        super.right();
        gameUI.didDash = true;
    }

    @Override
    public void draw(float delta) {

        if (state == STATE_MENU) {
            Assets.backgroundNubes.render(0);
        } else {
            Assets.backgroundNubes.render(delta);
        }

        renderer.render(delta);

        oCam.update();
        batcher.setProjectionMatrix(oCam.combined);

        batcher.begin();
        Assets.fontChico.draw(batcher, "FPS GERA" + Gdx.graphics.getFramesPerSecond() + "", 5, 20);
        Assets.fontChico.draw(batcher, "Bodies " + oWorld.oWorldBox.getBodyCount(), 5, 40);
        Assets.fontChico.draw(batcher, "Vidas " + oWorld.oPersonaje.vidas, 5, 60);
        Assets.fontChico.draw(batcher, "Monedas " + oWorld.monedasTomadas, 5, 80);
        Assets.fontChico.draw(batcher, "Puntos " + oWorld.puntuacion, 5, 100);
        Assets.fontChico.draw(batcher, "Distancia " + oWorld.oPersonaje.position.x, 5, 120);
        Assets.fontChico.draw(batcher, "Plataformas " + oWorld.arrPlataformas.size, 5, 140);

        batcher.end();

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
            if (game.arrPerson != null) {
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
}
