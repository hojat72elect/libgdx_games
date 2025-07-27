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
import com.nopalsoft.ponyrace.Assets;
import com.nopalsoft.ponyrace.MainPonyRace;
import com.nopalsoft.ponyrace.Settings;
import com.nopalsoft.ponyrace.menuobjetos.BotonNube;
import com.nopalsoft.ponyrace.objetos.Pony;
import com.nopalsoft.ponyrace.scene2d.Ventana;
import com.nopalsoft.ponyrace.scene2d.VentanaNextLevel;
import com.nopalsoft.ponyrace.scene2d.VentanaPause;
import com.nopalsoft.ponyrace.scene2d.VentanaRate;
import com.nopalsoft.ponyrace.scene2d.VentanaTimesUp;
import com.nopalsoft.ponyrace.scene2d.VentanaTryAgain;
import com.nopalsoft.ponyrace.screens.LoadingScreen;
import com.nopalsoft.ponyrace.screens.Screens;
import com.nopalsoft.ponyrace.screens.WorldMapTiledScreen;

public class GameScreenTileds extends Screens {

    // Variables to deduct coins 1 by 1 when you finish the race and get coins
    public final float GET_COIN_FOR_TIME_LEFT = .065f;
    public final int MULTIPLICADOR_MONEDAS_TIME_LEFT;
    public float time_left_coin = 0;
    public WorldTiled oWorld;
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

    public GameScreenTileds(MainPonyRace game, int nivelTiled) {
        super(game);
        Settings.statTimesPlayed++;

        oWorld = new WorldTiled(game, nivelTiled);
        state = State.ready;
        this.nivelTiled = nivelTiled;
        renderer = new WorldTiledRenderer(batcher, oWorld);
        touchPoint = new Vector3();

        fireBomb = jump = fireWood = false;

        btDer = new Button(oAssets.padDer);
        btIzq = new Button(oAssets.padIzq);
        btJump = new Button(oAssets.btJumpUp, oAssets.btJumpDown);

        btPausa = new Button(oAssets.btPauseUp);

        TextButtonStyle txButtonStyleFireBombs = new TextButtonStyle(oAssets.btBombaUp, oAssets.btBombaDown, null, oAssets.fontChco);
        TextButtonStyle txButtonStyleFireWoods = new TextButtonStyle(oAssets.btTroncoUp, oAssets.btTroncoDown, null, oAssets.fontChco);

        btFireBomb = new TextButton(Settings.numeroBombas + "", txButtonStyleFireBombs);
        btFireWood = new TextButton(Settings.numeroWoods + "", txButtonStyleFireWoods);

        setBotonesInterfaz();
        inicializarBotonesMenusInGame();

        lapTime = new StringBuilder();

        stringMonedasRecolectadas = new StringBuilder();
        stringMonedasRecolectadas.append("0");

        stringTiempoLeft = new StringBuilder();
        stringTiempoLeft.append((int) oWorld.tiempoLeft);

        if (nivelTiled == 1000) {
            stringMundoActual = "Secret world";
        } else {
            stringMundoActual = "World " + nivelTiled;
        }
        drawStatsEndRace = false;

        switch (Settings.nivelTime) {
            default:
            case 0:
                MULTIPLICADOR_MONEDAS_TIME_LEFT = 0;
                break;
            case 1:
                MULTIPLICADOR_MONEDAS_TIME_LEFT = 1;
                break;
            case 2:
                MULTIPLICADOR_MONEDAS_TIME_LEFT = 2;
                break;
            case 3:
                MULTIPLICADOR_MONEDAS_TIME_LEFT = 3;
                break;
            case 4:
                MULTIPLICADOR_MONEDAS_TIME_LEFT = 4;
                break;
            case 5:
                MULTIPLICADOR_MONEDAS_TIME_LEFT = 5;
                break;
        }

        vtPause = new VentanaPause(this);
        vtRate = new VentanaRate(this);

        if (!Settings.seCalificoApp && Settings.statTimesPlayed % 5 == 0) {

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

        if (oWorld == null)
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

        oWorld.update(delta, accelX, jump, fireBomb, fireWood, renderer);

        if (oWorld.state == WorldTiled.State.timeUp) {
            setTimeUp();
        } else if (oWorld.state == WorldTiled.State.nextLevel) {// Solo se pone cuando ganas en primer lugar
            setNextLevel();

            if (nivelTiled == Settings.mundosDesbloqueados)
                Settings.mundosDesbloqueados++;

            if (Settings.mundosDesbloqueados > Assets.mundoMaximo)
                Settings.mundosDesbloqueados = Assets.mundoMaximo;
        } else if (oWorld.state == WorldTiled.State.tryAgain) {
            setTryAgain();
        }

        stringMonedasRecolectadas.delete(0, stringMonedasRecolectadas.length());
        stringMonedasRecolectadas.append(oWorld.oPony.monedasRecolectadas);

        stringTiempoLeft.delete(0, stringTiempoLeft.length());
        stringTiempoLeft.append((int) oWorld.tiempoLeft);

        lapTime.delete(0, lapTime.length());
        lapTime.append("Lap ").append(game.formatter.format("%.2f", oWorld.tiempoLap));

        if (fireBomb)
            btFireBomb.setText(Settings.numeroBombas + "");

        if (fireWood)
            btFireWood.setText(Settings.numeroWoods + "");
        fireBomb = jump = fireWood = false;

        if (oWorld.state == WorldTiled.State.nextLevel || oWorld.state == WorldTiled.State.tryAgain) {
            if (((int) oWorld.tiempoLeft) % 2 == 0 && oWorld.oPony.monedasRecolectadas % 2 == 0 && ((int) oWorld.tiempoLap % 2) == 0)
                Settings.isEnabledSecretWorld = true;
        }
    }

    private void updateTimeUp(float delta) {
        oWorld.update(delta, renderer);
    }

    private void updateNextLevel(float delta) {
        oWorld.update(delta, renderer);

        if (ScreenStateTime >= Ventana.DURACION_ANIMATION + .2f) {
            drawStatsEndRace = true;
            giveCoinsAfterfinish(delta);
        }
    }

    private void updateTryAgain(float delta) {
        oWorld.update(delta, renderer);

        if (ScreenStateTime >= Ventana.DURACION_ANIMATION + .2f) {
            drawStatsEndRace = true;
            giveCoinsAfterfinish(delta);
        }
    }

    private void giveCoinsAfterfinish(float delta) {
        time_left_coin += delta;
        if (Settings.nivelTime > 0 && Settings.dificultadActual >= Settings.DIFICULTAD_NORMAL && oWorld.tiempoLeft > 0
                && time_left_coin >= GET_COIN_FOR_TIME_LEFT) {
            time_left_coin -= GET_COIN_FOR_TIME_LEFT;
            oWorld.tiempoLeft--;
            oWorld.oPony.monedasRecolectadas += MULTIPLICADOR_MONEDAS_TIME_LEFT;

            stringMonedasRecolectadas.delete(0, stringMonedasRecolectadas.length());
            stringMonedasRecolectadas.delete(0, stringMonedasRecolectadas.length());
            stringMonedasRecolectadas.append(oWorld.oPony.monedasRecolectadas);

            stringTiempoLeft.delete(0, stringTiempoLeft.length());
            stringTiempoLeft.append((int) oWorld.tiempoLeft);

            Settings.sumarMonedas(MULTIPLICADOR_MONEDAS_TIME_LEFT);
            game.oAssets.playSound(game.oAssets.pickCoin);
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
        guiCam.update();
        batcher.setProjectionMatrix(guiCam.combined);

        batcher.enableBlending();
        batcher.begin();
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

        batcher.end();

        stage.act();
        stage.draw();
    }

    private void presentReady() {
        glyphLayout.setText(oAssets.fontGde, "Touch the screen to start");
        oAssets.fontGde.draw(batcher, "Touch the screen to start", SCREEN_WIDTH / 2f - glyphLayout.width / 2f, SCREEN_HEIGHT / 2f - glyphLayout.height / 2f);
    }

    private void presentRunning() {

        int alturaIndicador = 440;
        batcher.draw(oAssets.indicador, 150f, alturaIndicador, 500, 15);
        batcher.draw(oAssets.lugaresMarco, 0, 250, 75, 164);// Barra del lado izq que muestra los primeros 3 lugares

        // Para dibujar los lugares donde estan los ponyes en la barra superior
        for (int i = 0; i < oWorld.arrPosiciones.size; i++) {
            Pony oPony = oWorld.arrPosiciones.get(i);

            AtlasRegion textura;
            AtlasRegion perfil;

            switch (oPony.nombreSkin) {
                case "Cloud":
                    textura = oAssets.indicadorCloud;
                    perfil = oAssets.perfilRegionCloud;
                    break;
                case "Natylol":
                    textura = oAssets.indicadorNatylol;
                    perfil = oAssets.perfilRegionNatylol;
                    break;
                case "Ignis":
                    textura = oAssets.indicadorIgnis;
                    perfil = oAssets.perfilRegionIgnis;
                    break;
                case "cientifico":
                    textura = oAssets.indicadorCientifico;
                    perfil = oAssets.perfilRegionCientifico;
                    break;
                case "LAlba":
                    textura = oAssets.indicadorLighthingAlba;
                    perfil = oAssets.perfilRegionLAlba;
                    break;
                default:
                    textura = oAssets.indicadorMinion;
                    perfil = oAssets.perfilRegionEnemigo;
                    break;
            }

            float posocion = 500 / oWorld.tamanoMapaX * oPony.position.x + 140;
            batcher.draw(textura, posocion, alturaIndicador, 25, 25);

            if (i == 0) {
                batcher.draw(perfil, 26, 368, 45, 45);
            }
            if (i == 1) {
                batcher.draw(perfil, 26, 310, 45, 45);
            }
            if (i == 2) {
                batcher.draw(perfil, 26, 252, 45, 45);
            }
        }
        // Fin

        // Dibujar Monedas
        batcher.draw(oAssets.moneda, 5, 445, 30, 30);
        oAssets.fontChco.draw(batcher, stringMonedasRecolectadas, 38, 472);

        // El mundo actual
        glyphLayout.setText(oAssets.fontChco, stringMundoActual);
        oAssets.fontChco.draw(batcher, stringMundoActual, SCREEN_WIDTH / 2F - glyphLayout.width / 2, alturaIndicador - 5);

        // El tiempo que queda
        glyphLayout.setText(oAssets.fontChco, stringTiempoLeft);
        oAssets.fontChco.draw(batcher, stringTiempoLeft, SCREEN_WIDTH / 2F - glyphLayout.width / 2, alturaIndicador - 32);

        // fin
        oAssets.fontChco.draw(batcher, lapTime, 0, 225);

        if (Assets.drawDebugLines)
            oAssets.fontChco.draw(batcher, "FPS=" + Gdx.graphics.getFramesPerSecond(), 0, 190);
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
        btMainMenu = new BotonNube(oAssets.nube, "Menu", oAssets.fontChco);
        btMainMenu.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                btMainMenu.wasSelected = true;
                btMainMenu.addAction(Actions.sequence(Actions.delay(.2f), btMainMenu.accionInicial, Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        GameScreenTileds.this.game.setScreen(new LoadingScreen(game, WorldMapTiledScreen.class));
                        dispose();
                    }
                })));
            }
        });

        btContinue = new BotonNube(oAssets.nube, "Continue", oAssets.fontChco);
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

        btTryAgain = new BotonNube(oAssets.nube, "Try again", oAssets.fontChco);
        btTryAgain.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                btTryAgain.wasSelected = true;
                btTryAgain.addAction(Actions.sequence(Actions.delay(.2f), btTryAgain.accionInicial, Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        GameScreenTileds.this.game.setScreen(new LoadingScreen(game, GameScreenTileds.class, nivelTiled));
                        if (Settings.dificultadActual == Settings.DIFICULTAD_SUPERHARD & state == State.nextLevel)
                            Settings.sumarMonedas((int) (MULTIPLICADOR_MONEDAS_TIME_LEFT * oWorld.tiempoLeft));
                    }
                })));
            }
        });

        btNextLevel = new BotonNube(oAssets.nube, "Next", oAssets.fontChco);
        btNextLevel.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                btNextLevel.wasSelected = true;
                btNextLevel.addAction(Actions.sequence(Actions.delay(.2f), btNextLevel.accionInicial, Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        GameScreenTileds.this.game.setScreen(new LoadingScreen(game, WorldMapTiledScreen.class));
                        if (Settings.dificultadActual == Settings.DIFICULTAD_SUPERHARD)
                            Settings.sumarMonedas((int) (MULTIPLICADOR_MONEDAS_TIME_LEFT * oWorld.tiempoLeft));

                        dispose();
                    }
                })));
            }
        });

        btSonido = new ImageButton(oAssets.btSonidoOff, null, oAssets.btSonidoON);
        btSonido.setChecked(Settings.isSonidoON);
        btSonido.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isSonidoON = !Settings.isSonidoON;
                super.clicked(event, x, y);
            }
        });

        btMusica = new ImageButton(oAssets.btMusicaOff, null, oAssets.btMusicaON);
        btMusica.setChecked(Settings.isMusicaON);
        btMusica.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isMusicaON = !Settings.isMusicaON;
                oAssets.platMusicInGame();
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
        if (oWorld == null || renderer == null)
            return;

        oWorld.oWorldBox.dispose();
        renderer.renderBox.dispose();
        oAssets.tiledMap.dispose();
        renderer.tiledRender.dispose();
        renderer = null;
        oWorld = null;
        game.oAssets.unloadGameScreenTiled();
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

        this.guiCam.unproject(touchPoint.set(screenX, screenY, 0));
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
