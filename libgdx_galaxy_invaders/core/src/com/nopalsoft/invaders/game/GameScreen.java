package com.nopalsoft.invaders.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.invaders.Assets;
import com.nopalsoft.invaders.MainInvaders;
import com.nopalsoft.invaders.Settings;
import com.nopalsoft.invaders.screens.MainMenuScreen;
import com.nopalsoft.invaders.screens.Screens;

public class GameScreen extends Screens {
    FPSLogger fps = new FPSLogger();

    static final int GAME_READY = 0;
    public static final int GAME_RUNNING = 1;
    static final int GAME_OVER = 2;
    public static final int GAME_PAUSE = 3;
    public final int GAME_TUTORIAL = 4;

    int pantallaTutorial; // si esta en la pantalla 1 o en la 2 del tutorial
    World oWorld;
    WorldRenderer renderer;
    boolean seDisparo = false;
    boolean seDisparoMissil = false;
    Vector3 touchPoint;

    Rectangle leftButton;
    Rectangle rightButton;

    Dialog dialogPause, dialogGameOver;

    Table scoresBar;
    Label lbLevel, lbScore, lbNumVidas;
    ImageButton btPause;

    ImageButton btLeft, btRight, btFire;
    TextButton btMissil;

    Group gpTutorial;
    Label lbTiltYourDevice;

    public static int state;

    float accel;

    int nivel;

    public GameScreen(final MainInvaders game) {
        super(game);
        Settings.numeroDeVecesQueSeHaJugado++;
        state = GAME_READY;
        if (Settings.numeroDeVecesQueSeHaJugado < 3) {// Se mostrara 2 veces, la vez cero y la vez 1
            state = GAME_TUTORIAL;
            pantallaTutorial = 0;
            setUpTutorial();
        }
        touchPoint = new Vector3();

        oWorld = new World();
        renderer = new WorldRenderer(batcher, oWorld);
        leftButton = new Rectangle(0, 0, 160, 480);
        rightButton = new Rectangle(161, 0, 160, 480);

        // Controles OnScreen
        accel = 0;
        nivel = oWorld.currentLevel;
        btLeft = new ImageButton(Assets.btLeft);
        btLeft.setSize(65, 50);
        btLeft.setPosition(10, 5);
        btLeft.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                accel = 5;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                accel = 0;
                super.exit(event, x, y, pointer, toActor);
            }

        });
        btRight = new ImageButton(Assets.btRight);
        btRight.setSize(65, 50);
        btRight.setPosition(85, 5);
        btRight.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                accel = -5;

            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                accel = 0;
                super.exit(event, x, y, pointer, toActor);
            }

        });

        btMissil = new TextButton(oWorld.missileCount + "", new TextButtonStyle(Assets.btMissil, Assets.btMissilDown, null, Assets.font10));
        btMissil.getLabel().setColor(Color.GREEN);
        btMissil.setSize(60, 60);
        btMissil.setPosition(SCREEN_WIDTH - 5 - 60 - 20 - 60, 5);
        btMissil.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                seDisparoMissil = true;
            }
        });
        btFire = new ImageButton(Assets.btFire, Assets.btFireDown);
        btFire.setSize(60, 60);
        btFire.setPosition(SCREEN_WIDTH - 60 - 5, 5);
        btFire.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                seDisparo = true;
            }
        });

        // Fin Controles OnScreen

        // /Inicio dialog Pause
        dialogPause = new Dialog(Assets.idiomas.get("game_paused"), Assets.styleDialogPause);

        TextButton btContinue = new TextButton(Assets.idiomas.get("continue"), Assets.styleTextButton);
        TextButton btMenu = new TextButton(Assets.idiomas.get("main_menu"), Assets.styleTextButton);

        btContinue.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                state = GAME_RUNNING;
                oWorld.state = World.STATE_RUNNING;
                dialogPause.hide();

            }
        });

        btMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new MainMenuScreen(game));
                dialogPause.hide();

            }
        });

        dialogPause.getButtonTable().pad(15);
        dialogPause.getButtonTable().add(btContinue).minWidth(160).minHeight(40).expand().padBottom(20);
        dialogPause.getButtonTable().row();
        dialogPause.getButtonTable().add(btMenu).minWidth(160).minHeight(40).expand();

        // Inicio dialogGameOver

        dialogGameOver = new Dialog("Game Over", Assets.styleDialogPause);

        TextButton btTryAgain = new TextButton(Assets.idiomas.get("try_again"), Assets.styleTextButton);
        TextButton btMenu2 = new TextButton(Assets.idiomas.get("main_menu"), Assets.styleTextButton);
        TextButton btShare = new TextButton(Assets.idiomas.get("share"), Assets.styleTextButtonFacebook);

        btTryAgain.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new GameScreen(game));
                dialogGameOver.hide();

            }
        });

        btMenu2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new MainMenuScreen(game));
                dialogGameOver.hide();

            }
        });
        btShare.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String text = Assets.idiomas.format("i_just_score_n_points_playing_droid_invaders_can_you_beat_me", oWorld.score);
                Gdx.app.log("Share text", text);
                Assets.playSound(Assets.clickSound);
            }
        });

        dialogGameOver.getButtonTable().pad(15);
        dialogGameOver.getButtonTable().add(btTryAgain).minWidth(160).minHeight(40).expand().padBottom(20);
        dialogGameOver.getButtonTable().row();
        dialogGameOver.getButtonTable().add(btMenu2).minWidth(160).minHeight(40).expand();
        dialogGameOver.getButtonTable().row();


        Label lbShare = new Label(Assets.idiomas.get("share_your_score_on_facebook"), Assets.styleLabelDialog);
        lbShare.setAlignment(Align.center);
        lbShare.setWrap(true);
        dialogGameOver.getButtonTable().add(lbShare).width(200).expand();
        dialogGameOver.getButtonTable().row();
        dialogGameOver.getButtonTable().add(btShare).expand();


        if (Settings.numeroDeVecesQueSeHaJugado % 5 == 0) {
            game.dialogs.showDialogRate();
        }

        btPause = new ImageButton(Assets.styleImageButtonPause);
        btPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPaused();
            }
        });

        lbLevel = new Label(Assets.idiomas.get("level") + " " + oWorld.currentLevel, Assets.styleLabel);
        lbScore = new Label(Assets.idiomas.get("score") + " " + oWorld.score, Assets.styleLabel);
        lbNumVidas = new Label("x" + oWorld.oNave.vidas, Assets.styleLabel);
        Image imVida = new Image(Assets.nave);

        scoresBar = new Table();
        scoresBar.setBackground(Assets.recuadroInGameStatus);
        scoresBar.setWidth(SCREEN_WIDTH);
        scoresBar.setHeight(30);
        scoresBar.setPosition(0, SCREEN_HEIGHT - 30);

        scoresBar.add(lbLevel).left();

        scoresBar.add(lbScore).center().expandX();

        scoresBar.add(imVida).size(20).right();
        scoresBar.add(lbNumVidas).right();
        scoresBar.add(btPause).size(26).right().padLeft(8);
        // scoresBar.debug();

        stage.addActor(scoresBar);

    }

    private void setUpTutorial() {

        lbTiltYourDevice = new Label(Assets.idiomas.get("tilt_your_device_to_move_horizontally"), new LabelStyle(Assets.font45, Color.GREEN));
        lbTiltYourDevice.setWrap(true);
        lbTiltYourDevice.setAlignment(Align.center);
        lbTiltYourDevice.setPosition(0, 120);
        lbTiltYourDevice.setWidth(SCREEN_WIDTH);
        stage.addActor(lbTiltYourDevice);

        gpTutorial = new Group();
        // gpTutorial.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

        Table boostTable = new Table();
        gpTutorial.addActor(boostTable);

        Image vida = new Image(Assets.upgLife);
        Image boostBomba = new Image(Assets.boost2);
        Image boostEscudo = new Image(Assets.boost3);
        Image boostUpgradeWeapon = new Image(Assets.boost1);

        Label lblVida = new Label(Assets.idiomas.get("get_one_extra_life"), Assets.styleLabel);
        Label lblBomba = new Label(Assets.idiomas.get("get_one_extra_missil"), Assets.styleLabel);
        Label lblShield = new Label(Assets.idiomas.get("get_a_shield"), Assets.styleLabel);
        Label lblUpgradeWeapn = new Label(Assets.idiomas.get("upgrade_your_weapon"), Assets.styleLabel);

        boostTable.setPosition(0, 340);
        boostTable.setWidth(SCREEN_WIDTH);

        int iconSize = 40;
        boostTable.add(vida).size(iconSize);
        boostTable.add(lblVida).padLeft(15).left();
        boostTable.row().padTop(10);
        boostTable.add(boostBomba).size(iconSize);
        boostTable.add(lblBomba).padLeft(15).left();
        boostTable.row().padTop(10);
        boostTable.add(boostEscudo).size(iconSize);
        boostTable.add(lblShield).padLeft(15).left();
        boostTable.row().padTop(10);
        boostTable.add(boostUpgradeWeapon).size(iconSize);
        boostTable.add(lblUpgradeWeapn).padLeft(15).left();

        Label touchLeft, touchRight;
        touchLeft = new Label(Assets.idiomas.get("touch_left_side_to_fire_missils"), Assets.styleLabel);
        touchLeft.setWrap(true);
        touchLeft.setWidth(160);
        touchLeft.setAlignment(Align.center);
        touchLeft.setPosition(0, 50);

        touchRight = new Label(Assets.idiomas.get("touch_right_side_to_fire"), Assets.styleLabel);
        touchRight.setWrap(true);
        touchRight.setWidth(160);
        touchRight.setAlignment(Align.center);
        touchRight.setPosition(165, 50);

        gpTutorial.addActor(touchRight);
        gpTutorial.addActor(touchLeft);

    }

    @Override
    public void update(float deltaTime) {
        // if (deltaTime > 0.1f) deltaTime = 0.1f;
        switch (state) {
            case GAME_TUTORIAL:
                updateTutorial();
                break;
            case GAME_READY:
                updateReady();
                break;
            case GAME_RUNNING:
                updateRunning(deltaTime);
                break;

        }

    }

    private void updateTutorial() {
        if (Gdx.input.justTouched()) {
            if (pantallaTutorial == 0) {
                pantallaTutorial++;
                lbTiltYourDevice.remove();
                stage.addActor(gpTutorial);
            } else {
                state = GAME_READY;
                gpTutorial.remove();
            }

        }
    }

    private void updateReady() {
        if (Gdx.input.justTouched() && !game.dialogs.isDialogShown()) {
            state = GAME_RUNNING;

            if (!Settings.isTiltControl) {
                stage.addActor(btLeft);
                stage.addActor(btRight);
                stage.addActor(btMissil);
                stage.addActor(btFire);
            }

        }
    }

    private void updateRunning(float deltaTime) {

        if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT) || Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.DPAD_RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
            accel = 0;
            if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT) || Gdx.input.isKeyPressed(Keys.A))
                accel = 5f;
            if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT) || Gdx.input.isKeyPressed(Keys.D))
                accel = -5f;

            oWorld.update(deltaTime, accel, seDisparo, seDisparoMissil);
        } else if (Settings.isTiltControl) {
            if (Gdx.input.justTouched()) {
                oCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

                if (leftButton.contains(touchPoint.x, touchPoint.y)) {
                    seDisparoMissil = true;
                }
                if (rightButton.contains(touchPoint.x, touchPoint.y)) {
                    seDisparo = true;
                }
            }
            oWorld.update(deltaTime, Gdx.input.getAccelerometerX(), seDisparo, seDisparoMissil);
        } else {
            oWorld.update(deltaTime, accel, seDisparo, seDisparoMissil);
        }

        if (nivel != oWorld.currentLevel) {
            nivel = oWorld.currentLevel;
            lbLevel.setText(Assets.idiomas.get("level") + " " + nivel);
        }

        lbScore.setText(Assets.idiomas.get("score") + " " + oWorld.score);
        lbNumVidas.setText("x" + oWorld.oNave.vidas);

        if (oWorld.state == World.STATE_GAME_OVER) {
            state = GAME_OVER;
            dialogGameOver.show(stage);
        }

        btMissil.setText(oWorld.missileCount + "");

        seDisparo = false;
        seDisparoMissil = false;
    }

    private void setPaused() {
        Assets.playSound(Assets.clickSound);
        state = GAME_PAUSE;
        oWorld.state = World.STATE_PAUSED;
        dialogPause.show(stage);
    }

    @Override
    public void draw(float delta) {

        if (state != GAME_TUTORIAL)
            renderer.render(delta);
        else
            Assets.parallaxFondo.render(delta);
        oCam.update();
        batcher.setProjectionMatrix(oCam.combined);
        batcher.enableBlending();
        batcher.begin();

        switch (state) {
            case GAME_TUTORIAL:
                presentTurorial(delta);
                break;
            case GAME_READY:
                presentReady();
                break;
            case GAME_RUNNING:
                presentRunning();
                break;
        }
        batcher.end();


    }

    float rotacion = 0;
    float addRotacion = .3f;

    private void presentTurorial(float delta) {
        if (pantallaTutorial == 0 && Settings.isTiltControl) {
            if (rotacion < -20 || rotacion > 20)
                addRotacion *= -1;
            rotacion += addRotacion;
            batcher.draw(Assets.help1, SCREEN_WIDTH / 2f - 51, 190, 51, 0, 102, 200, 1, 1, rotacion);
        } else {
            batcher.draw(Assets.clickAyuda, 155, 0, 10, 125);

        }

    }

    private void presentReady() {
        String touchToStart = Assets.idiomas.get("touch_to_start");
        float textWidth = Assets.getTextWidth(Assets.font45, touchToStart);
        Assets.font45.draw(batcher, touchToStart, (SCREEN_WIDTH / 2f) - (textWidth / 2f), 220);
    }

    private void presentRunning() {
        if (oWorld.missileCount > 0 && Settings.isTiltControl) {
            batcher.draw(Assets.misil.getKeyFrame(0), 1, 1, 8, 28);
            Assets.font15.draw(batcher, "X" + oWorld.missileCount, 10, 25);
        }
    }

    @Override
    public void hide() {
        Settings.agregarPuntuacion(oWorld.score);
        super.hide();
    }

    @Override
    public void pause() {
        setPaused();
        super.pause();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.BACK || keycode == Keys.ESCAPE) {
            Assets.playSound(Assets.clickSound);
            if (state == GAME_RUNNING) {
                setPaused();
                return true;
            } else if (state == GAME_PAUSE) {
                game.setScreen(new MainMenuScreen(game));
                return true;
            }
        } else if (keycode == Keys.MENU) {
            setPaused();
            return true;
        } else if (keycode == Keys.SPACE) {
            seDisparo = true;

            return true;
        } else if (keycode == Keys.ALT_LEFT) {
            seDisparoMissil = true;
            return true;
        }
        return false;
    }

}
