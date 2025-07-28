package com.nopalsoft.ponyrace.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.ponyrace.AssetsHandler;
import com.nopalsoft.ponyrace.PonyRacingGame;
import com.nopalsoft.ponyrace.Settings;
import com.nopalsoft.ponyrace.game_objects.Pony;
import com.nopalsoft.ponyrace.menuobjetos.BotonNube;
import com.nopalsoft.ponyrace.scene2d.Ventana;
import com.nopalsoft.ponyrace.scene2d.VentanaNextLevel;
import com.nopalsoft.ponyrace.scene2d.VentanaPause;
import com.nopalsoft.ponyrace.scene2d.VentanaRate;
import com.nopalsoft.ponyrace.scene2d.VentanaTimesUp;
import com.nopalsoft.ponyrace.scene2d.VentanaTryAgain;
import com.nopalsoft.ponyrace.screens.BaseScreen;
import com.nopalsoft.ponyrace.screens.LoadingScreen;
import com.nopalsoft.ponyrace.screens.WorldMapTiledScreen;

public class GameScreen extends BaseScreen {

    // Variables to deduct coins 1 by 1 when you finish the race and get coins
    public final float GET_COIN_FOR_TIME_LEFT = .065f;
    public final int COIN_MULTIPLIER_TIME_LEFT;
    public float time_left_coin = 0;
    public TileMapHandler world;
    // finish
    public int nivelTiled;

    public StringBuilder stringMonedasRecolectadas;
    public StringBuilder stringTiempoLeft;
    public StringBuilder lapTime;
    WorldTiledRenderer renderer;
    Vector3 touchPoint;
    State state;
    boolean jump, fireBomb, fireWood;
    Button btIzq, btDer, btJump;
    TextButton btFireBomb, btFireWood;
    Button btPausa;
    String stringMundoActual;
    float accelX;
    boolean drawStatsEndRace;
    VentanaRate vtRate;
    VentanaPause vtPause;
    int tamanoBoton = 105;
    BotonNube btMainMenu;
    BotonNube btContinue;
    BotonNube btTryAgain;
    BotonNube btNextLevel;
    ImageButton btSonido;
    ImageButton btMusica;

    public GameScreen(PonyRacingGame game, int nivelTiled) {
        super(game);
        Settings.statTimesPlayed++;

        world = new TileMapHandler(game, nivelTiled);
        state = State.ready;
        this.nivelTiled = nivelTiled;
        renderer = new WorldTiledRenderer(batch, world);
        touchPoint = new Vector3();

        fireBomb = jump = fireWood = false;

        btDer = new Button(assetsHandler.padDer);
        btIzq = new Button(assetsHandler.padIzq);
        btJump = new Button(assetsHandler.btJumpUp, assetsHandler.btJumpDown);

        btPausa = new Button(assetsHandler.btPauseUp);

        TextButtonStyle txButtonStyleFireBombs = new TextButtonStyle(assetsHandler.btBombaUp, assetsHandler.btBombaDown, null, assetsHandler.fontChco);
        TextButtonStyle txButtonStyleFireWoods = new TextButtonStyle(assetsHandler.btTroncoUp, assetsHandler.btTroncoDown, null, assetsHandler.fontChco);

        btFireBomb = new TextButton(Settings.numeroBombas + "", txButtonStyleFireBombs);
        btFireWood = new TextButton(Settings.numeroWoods + "", txButtonStyleFireWoods);

        setBotonesInterfaz();
        inicializarBotonesMenusInGame();

        lapTime = new StringBuilder();

        stringMonedasRecolectadas = new StringBuilder();
        stringMonedasRecolectadas.append("0");

        stringTiempoLeft = new StringBuilder();
        stringTiempoLeft.append((int) world.tiempoLeft);

        if (nivelTiled == 1000) {
            stringMundoActual = "Secret world";
        } else {
            stringMundoActual = "World " + nivelTiled;
        }
        drawStatsEndRace = false;

        switch (Settings.timeLevel) {
            default:
            case 0:
                COIN_MULTIPLIER_TIME_LEFT = 0;
                break;
            case 1:
                COIN_MULTIPLIER_TIME_LEFT = 1;
                break;
            case 2:
                COIN_MULTIPLIER_TIME_LEFT = 2;
                break;
            case 3:
                COIN_MULTIPLIER_TIME_LEFT = 3;
                break;
            case 4:
                COIN_MULTIPLIER_TIME_LEFT = 4;
                break;
            case 5:
                COIN_MULTIPLIER_TIME_LEFT = 5;
                break;
        }

        vtPause = new VentanaPause(this);
        vtRate = new VentanaRate(this);

        if (!Settings.wasAppRated && Settings.statTimesPlayed % 5 == 0) {

            vtRate.show(stage);
        }
    }

    public void setBotonesInterfaz() {
        stage.clear();

        btJump.setPosition(692, 10);
        btJump.setSize(tamanoBoton, tamanoBoton);

        btFireBomb.setPosition(584, 10);
        btFireBomb.setSize(tamanoBoton, tamanoBoton);

        btFireWood.setPosition(692, 123);
        btFireWood.setSize(tamanoBoton, tamanoBoton);

        btIzq.setPosition(5, 5);
        btIzq.setSize(120, 120);

        btDer.setPosition(130, 5);
        btDer.setSize(120, 120);

        btPausa.setSize(45, 45);
        btPausa.setPosition(750, 430);

        btJump.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                jump = true;
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        btFireBomb.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                fireBomb = true;
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        btFireWood.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                fireWood = true;
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        btDer.addListener(new ClickListener() {
            public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                accelX = 1;
            }

            public void exit(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor toActor) {
                accelX = 0;
            }
        });
        btIzq.addListener(new ClickListener() {
            public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                accelX = -1;
            }

            public void exit(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor toActor) {
                accelX = 0;
            }
        });

        btPausa.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPause();
                super.clicked(event, x, y);
            }
        });

//        if (Gdx.app.getType() != ApplicationType.Desktop) {
        stage.addActor(btDer);
        stage.addActor(btIzq);
        stage.addActor(btJump);
        if (Settings.numeroBombas > 0)
            stage.addActor(btFireBomb);
        if (Settings.numeroWoods > 0)
            stage.addActor(btFireWood);
//        }

        stage.addActor(btPausa);
    }

    @Override
    public void update(float delta) {

        if (world == null)
            return;

        switch (state) {
            default:
            case ready:
                updateReady();
                break;
            case running:
                updateRunning(delta);
                break;
            case timeUp:
                updateTimeUp(delta);
                break;
            case paused:
                break;
            case nextLevel:
                updateNextLevel(delta);
                break;
            case tryAgain:
                updateTryAgain(delta);
                break;
        }
    }

    private void updateReady() {
        if (Gdx.input.isTouched() && !vtRate.isVisible())
            state = State.running;
    }

    private void updateRunning(float delta) {
        if (Gdx.input.isKeyPressed(Keys.A))
            accelX = -1;
        else if (Gdx.input.isKeyPressed(Keys.D))
            accelX = 1;

        world.update(delta, accelX, jump, fireBomb, fireWood, renderer);

        if (world.state == TileMapHandler.State.timeUp) {
            setTimeUp();
        } else if (world.state == TileMapHandler.State.nextLevel) {// Solo se pone cuando ganas en primer lugar
            setNextLevel();

            if (nivelTiled == Settings.numberOfGameLevelsUnlocked)
                Settings.numberOfGameLevelsUnlocked++;

            if (Settings.numberOfGameLevelsUnlocked > AssetsHandler.mundoMaximo)
                Settings.numberOfGameLevelsUnlocked = AssetsHandler.mundoMaximo;
        } else if (world.state == TileMapHandler.State.tryAgain) {
            setTryAgain();
        }

        stringMonedasRecolectadas.delete(0, stringMonedasRecolectadas.length());
        stringMonedasRecolectadas.append(world.oPony.monedasRecolectadas);

        stringTiempoLeft.delete(0, stringTiempoLeft.length());
        stringTiempoLeft.append((int) world.tiempoLeft);

        lapTime.delete(0, lapTime.length());
        lapTime.append("Lap ").append(game.formatter.format("%.2f", world.tiempoLap));

        if (fireBomb)
            btFireBomb.setText(Settings.numeroBombas + "");

        if (fireWood)
            btFireWood.setText(Settings.numeroWoods + "");
        fireBomb = jump = fireWood = false;

        if (world.state == TileMapHandler.State.nextLevel || world.state == TileMapHandler.State.tryAgain) {
            if (((int) world.tiempoLeft) % 2 == 0 && world.oPony.monedasRecolectadas % 2 == 0 && ((int) world.tiempoLap % 2) == 0)
                Settings.isEnabledSecretWorld = true;
        }
    }

    private void updateTimeUp(float delta) {
        world.update(delta, renderer);
    }

    private void updateNextLevel(float delta) {
        world.update(delta, renderer);

        if (ScreenStateTime >= Ventana.DURACION_ANIMATION + .2f) {
            drawStatsEndRace = true;
            giveCoinsAfterfinish(delta);
        }
    }

    private void updateTryAgain(float delta) {
        world.update(delta, renderer);

        if (ScreenStateTime >= Ventana.DURACION_ANIMATION + .2f) {
            drawStatsEndRace = true;
            giveCoinsAfterfinish(delta);
        }
    }

    private void giveCoinsAfterfinish(float delta) {
        time_left_coin += delta;
        if (Settings.timeLevel > 0 && Settings.difficultyLevel >= Settings.DIFFICULTY_NORMAL && world.tiempoLeft > 0
                && time_left_coin >= GET_COIN_FOR_TIME_LEFT) {
            time_left_coin -= GET_COIN_FOR_TIME_LEFT;
            world.tiempoLeft--;
            world.oPony.monedasRecolectadas += COIN_MULTIPLIER_TIME_LEFT;

            stringMonedasRecolectadas.delete(0, stringMonedasRecolectadas.length());
            stringMonedasRecolectadas.delete(0, stringMonedasRecolectadas.length());
            stringMonedasRecolectadas.append(world.oPony.monedasRecolectadas);

            stringTiempoLeft.delete(0, stringTiempoLeft.length());
            stringTiempoLeft.append((int) world.tiempoLeft);

            Settings.sumarMonedas(COIN_MULTIPLIER_TIME_LEFT);
            game.assetsHandler.playSound(game.assetsHandler.pickCoin);
        }
    }

    @Override
    public void draw(float delta) {

        Gdx.gl.glClearColor(.38f, .77f, .87f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (state == State.paused)
            delta = 0;

        if (renderer == null)
            return;

        renderer.render(delta);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.enableBlending();
        batch.begin();
        switch (state) {
            case ready:
                presentReady();
                break;
            case running:
                presentRunning();
                break;

            case tryAgain:
            case paused:
            case timeUp:
            case nextLevel:
                break;
        }

        batch.end();

        stage.act();
        stage.draw();
    }

    private void presentReady() {
        glyphLayout.setText(assetsHandler.fontGde, "Touch the screen to start");
        assetsHandler.fontGde.draw(batch, "Touch the screen to start", SCREEN_WIDTH / 2f - glyphLayout.width / 2f, SCREEN_HEIGHT / 2f - glyphLayout.height / 2f);
    }

    private void presentRunning() {

        int alturaIndicador = 440;
        batch.draw(assetsHandler.indicador, 150f, alturaIndicador, 500, 15);
        batch.draw(assetsHandler.lugaresMarco, 0, 250, 75, 164);// Barra del lado izq que muestra los primeros 3 lugares

        // Para dibujar los lugares donde estan los ponyes en la barra superior
        for (int i = 0; i < world.arrPosiciones.size; i++) {
            Pony oPony = world.arrPosiciones.get(i);

            AtlasRegion textura;
            AtlasRegion perfil;

            switch (oPony.nombreSkin) {
                case "Cloud":
                    textura = assetsHandler.indicadorCloud;
                    perfil = assetsHandler.perfilRegionCloud;
                    break;
                case "Natylol":
                    textura = assetsHandler.indicadorNatylol;
                    perfil = assetsHandler.perfilRegionNatylol;
                    break;
                case "Ignis":
                    textura = assetsHandler.indicadorIgnis;
                    perfil = assetsHandler.perfilRegionIgnis;
                    break;
                case "cientifico":
                    textura = assetsHandler.indicadorCientifico;
                    perfil = assetsHandler.perfilRegionCientifico;
                    break;
                case "LAlba":
                    textura = assetsHandler.indicadorLighthingAlba;
                    perfil = assetsHandler.perfilRegionLAlba;
                    break;
                default:
                    textura = assetsHandler.indicadorMinion;
                    perfil = assetsHandler.perfilRegionEnemigo;
                    break;
            }

            float posocion = 500 / world.tamanoMapaX * oPony.position.x + 140;
            batch.draw(textura, posocion, alturaIndicador, 25, 25);

            if (i == 0) {
                batch.draw(perfil, 26, 368, 45, 45);
            }
            if (i == 1) {
                batch.draw(perfil, 26, 310, 45, 45);
            }
            if (i == 2) {
                batch.draw(perfil, 26, 252, 45, 45);
            }
        }
        // Fin

        // Dibujar Monedas
        batch.draw(assetsHandler.moneda, 5, 445, 30, 30);
        assetsHandler.fontChco.draw(batch, stringMonedasRecolectadas, 38, 472);

        // El mundo actual
        glyphLayout.setText(assetsHandler.fontChco, stringMundoActual);
        assetsHandler.fontChco.draw(batch, stringMundoActual, SCREEN_WIDTH / 2F - glyphLayout.width / 2, alturaIndicador - 5);

        // El tiempo que queda
        glyphLayout.setText(assetsHandler.fontChco, stringTiempoLeft);
        assetsHandler.fontChco.draw(batch, stringTiempoLeft, SCREEN_WIDTH / 2F - glyphLayout.width / 2, alturaIndicador - 32);

        // fin
        assetsHandler.fontChco.draw(batch, lapTime, 0, 225);

        if (AssetsHandler.drawDebugLines)
            assetsHandler.fontChco.draw(batch, "FPS=" + Gdx.graphics.getFramesPerSecond(), 0, 190);
    }

    private void setTimeUp() {
        state = State.timeUp;
        ScreenStateTime = 0;
        stage.clear();
        new VentanaTimesUp(this).show(stage);
    }

    public void setNextLevel() {
        state = State.nextLevel;
        ScreenStateTime = 0;
        stage.clear();

        btTryAgain.setPosition(5, 5);
        btNextLevel.setPosition(645, 5);

        new VentanaNextLevel(this).show(stage);

        stage.addActor(btNextLevel);

        if (nivelTiled != 1000)// Si es el mundo secreto no agrego el try again
            stage.addActor(btTryAgain);
    }

    public void setTryAgain() {
        state = State.tryAgain;
        ScreenStateTime = 0;
        stage.clear();

        btTryAgain.setPosition(5, 5);
        btMainMenu.setPosition(645, 5);

        new VentanaTryAgain(this).show(stage);

        stage.addActor(btMainMenu);
        if (nivelTiled != 1000)// Si es el mundo secreto no agrego el try again
            stage.addActor(btTryAgain);
    }

    public void setRunning() {
        setBotonesInterfaz();
        state = State.running;
    }

    public void setPause() {
        state = State.paused;
        stage.clear();
        vtPause.show(stage);
    }

    private void inicializarBotonesMenusInGame() {
        btMainMenu = new BotonNube(assetsHandler.nube, "Menu", assetsHandler.fontChco);
        btMainMenu.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                btMainMenu.wasSelected = true;
                btMainMenu.addAction(Actions.sequence(Actions.delay(.2f), btMainMenu.accionInicial, Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.game.setScreen(new LoadingScreen(game, WorldMapTiledScreen.class));
                        dispose();
                    }
                })));
            }
        });

        btContinue = new BotonNube(assetsHandler.nube, "Continue", assetsHandler.fontChco);
        btContinue.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                btContinue.wasSelected = true;
                btContinue.addAction(Actions.sequence(Actions.delay(.2f), btContinue.accionInicial, Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        setRunning();
                    }
                })));
            }
        });

        btTryAgain = new BotonNube(assetsHandler.nube, "Try again", assetsHandler.fontChco);
        btTryAgain.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                btTryAgain.wasSelected = true;
                btTryAgain.addAction(Actions.sequence(Actions.delay(.2f), btTryAgain.accionInicial, Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.game.setScreen(new LoadingScreen(game, GameScreen.class, nivelTiled));
                        if (Settings.difficultyLevel == Settings.DIFFICULTY_VERY_HARD & state == State.nextLevel)
                            Settings.sumarMonedas((int) (COIN_MULTIPLIER_TIME_LEFT * world.tiempoLeft));
                    }
                })));
            }
        });

        btNextLevel = new BotonNube(assetsHandler.nube, "Next", assetsHandler.fontChco);
        btNextLevel.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                btNextLevel.wasSelected = true;
                btNextLevel.addAction(Actions.sequence(Actions.delay(.2f), btNextLevel.accionInicial, Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        GameScreen.this.game.setScreen(new LoadingScreen(game, WorldMapTiledScreen.class));
                        if (Settings.difficultyLevel == Settings.DIFFICULTY_VERY_HARD)
                            Settings.sumarMonedas((int) (COIN_MULTIPLIER_TIME_LEFT * world.tiempoLeft));

                        dispose();
                    }
                })));
            }
        });

        btSonido = new ImageButton(assetsHandler.btSonidoOff, null, assetsHandler.btSonidoON);
        btSonido.setChecked(Settings.isSoundOn);
        btSonido.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isSoundOn = !Settings.isSoundOn;
                super.clicked(event, x, y);
            }
        });

        btMusica = new ImageButton(assetsHandler.btMusicaOff, null, assetsHandler.btMusicaON);
        btMusica.setChecked(Settings.isMusicOn);
        btMusica.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isMusicOn = !Settings.isMusicOn;
                assetsHandler.platMusicInGame();
                super.clicked(event, x, y);
            }
        });

        btTryAgain.setSize(150, 100);
        btMainMenu.setSize(150, 100);
        btContinue.setSize(150, 100);
        btNextLevel.setSize(150, 100);
    }

    @Override
    public void show() {

    }

    @Override
    public void dispose() {
        super.dispose();

        // Pongo esta condicion porque algunas veces el usuario presiona 2 veces el boton y se llama 2 veces este metodo;
        if (world == null || renderer == null)
            return;

        world.oWorldBox.dispose();
        renderer.renderBox.dispose();
        assetsHandler.tiledMap.dispose();
        renderer.tiledRender.dispose();
        renderer = null;
        world = null;
        game.assetsHandler.unloadGameScreenTiled();
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (Keys.DPAD_DOWN == keycode)
            renderer.OrthoCam.position.y -= 0.1F;
        else if (Keys.DPAD_UP == keycode) {
            renderer.OrthoCam.position.y += 0.1F;
            jump = true;
        } else if (Keys.DPAD_LEFT == keycode)
            renderer.OrthoCam.position.x -= 3;

        else if (Keys.DPAD_RIGHT == keycode)
            renderer.OrthoCam.position.x += 3;
        else if (Keys.K == keycode)
            renderer.OrthoCam.position.x -= .1f;
        else if (Keys.L == keycode)
            renderer.OrthoCam.position.x += .1f;

        else if (Keys.SPACE == keycode)
            jump = true;
        else if (Keys.B == keycode)
            fireBomb = true;
        else if (Keys.N == keycode)
            fireWood = true;
        else if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
            if (state == State.running)
                setPause();
            else {
                game.setScreen(new LoadingScreen(game, WorldMapTiledScreen.class));
                dispose();
            }
            return true;
        }

        return super.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.A || keycode == Keys.D)
            accelX = 0;
        return super.keyUp(keycode);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        this.camera.unproject(touchPoint.set(screenX, screenY, 0));
        Gdx.app.log("X", touchPoint.x + "");
        Gdx.app.log("Y", touchPoint.y + "");

        return false;
    }

    @Override
    public void pause() {
        setPause();
        super.pause();
    }

    public enum State {
        ready, running, paused, timeUp, nextLevel, tryAgain
    }
}
