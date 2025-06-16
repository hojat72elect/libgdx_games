package com.nopalsoft.invaders.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.invaders.Assets;
import com.nopalsoft.invaders.GalaxyInvadersGame;
import com.nopalsoft.invaders.Settings;
import com.nopalsoft.invaders.screens.MainMenuScreen;
import com.nopalsoft.invaders.screens.Screens;

public class GameScreen extends Screens {

    static final int GAME_READY = 0;
    public static final int GAME_RUNNING = 1;
    static final int GAME_OVER = 2;
    public static final int GAME_PAUSE = 3;
    public final int GAME_TUTORIAL = 4;

    int tutorialScreenIndex; // if it is on screen 1 or 2 of the tutorial
    World world;
    WorldRenderer renderer;
    boolean isFiring = false;
    boolean isMissileFired = false;
    Vector3 touchPoint;

    Rectangle leftButton;
    Rectangle rightButton;

    Dialog dialogPause, dialogGameOver;

    Table scoresBarTable;
    Label labelLevel, labelScore, labelLivesCount;
    ImageButton buttonPause;

    ImageButton buttonLeft, buttonRight, buttonFire;
    TextButton buttonMissile;

    Group groupTutorial;
    Label labelTiltYourDevice;

    public static int state;

    float acceleration;

    int gameLevel;

    public GameScreen(final GalaxyInvadersGame game) {
        super(game);
        Settings.numberOfTimesPlayed++;
        state = GAME_READY;
        if (Settings.numberOfTimesPlayed < 3) {// Se mostrara 2 veces, la vez cero y la vez 1
            state = GAME_TUTORIAL;
            tutorialScreenIndex = 0;
            setUpTutorial();
        }
        touchPoint = new Vector3();

        world = new World();
        renderer = new WorldRenderer(batch, world);
        leftButton = new Rectangle(0, 0, 160, 480);
        rightButton = new Rectangle(161, 0, 160, 480);

        // OnScreen Controls
        acceleration = 0;
        gameLevel = world.currentLevel;
        buttonLeft = new ImageButton(Assets.buttonLeft);
        buttonLeft.setSize(65, 50);
        buttonLeft.setPosition(10, 5);
        buttonLeft.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                acceleration = 5;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                acceleration = 0;
                super.exit(event, x, y, pointer, toActor);
            }
        });
        buttonRight = new ImageButton(Assets.buttonRight);
        buttonRight.setSize(65, 50);
        buttonRight.setPosition(85, 5);
        buttonRight.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                acceleration = -5;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                acceleration = 0;
                super.exit(event, x, y, pointer, toActor);
            }
        });

        buttonMissile = new TextButton(world.missileCount + "", new TextButtonStyle(Assets.buttonMissile, Assets.buttonMissilePressed, null, Assets.font10));
        buttonMissile.getLabel().setColor(Color.GREEN);
        buttonMissile.setSize(60, 60);
        buttonMissile.setPosition(SCREEN_WIDTH - 5 - 60 - 20 - 60, 5);
        buttonMissile.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isMissileFired = true;
            }
        });
        buttonFire = new ImageButton(Assets.buttonFire, Assets.buttonFirePressed);
        buttonFire.setSize(60, 60);
        buttonFire.setPosition(SCREEN_WIDTH - 60 - 5, 5);
        buttonFire.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isFiring = true;
            }
        });

        // End OnScreen Controls

        // Start dialog Pause
        dialogPause = new Dialog(Assets.languagesBundle.get("game_paused"), Assets.styleDialogPause);

        TextButton btContinue = new TextButton(Assets.languagesBundle.get("continue"), Assets.styleTextButton);
        TextButton btMenu = new TextButton(Assets.languagesBundle.get("main_menu"), Assets.styleTextButton);

        btContinue.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                state = GAME_RUNNING;
                world.state = World.STATE_RUNNING;
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

        // Start GameOver dialog

        dialogGameOver = new Dialog("Game Over", Assets.styleDialogPause);

        TextButton buttonTryAgain = new TextButton(Assets.languagesBundle.get("try_again"), Assets.styleTextButton);
        TextButton buttonMenu2 = new TextButton(Assets.languagesBundle.get("main_menu"), Assets.styleTextButton);
        TextButton buttonShare = new TextButton(Assets.languagesBundle.get("share"), Assets.styleTextButtonFacebook);

        buttonTryAgain.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new GameScreen(game));
                dialogGameOver.hide();
            }
        });

        buttonMenu2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new MainMenuScreen(game));
                dialogGameOver.hide();
            }
        });
        buttonShare.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String text = Assets.languagesBundle.format("i_just_score_n_points_playing_droid_invaders_can_you_beat_me", world.score);
                Gdx.app.log("Share text", text);
                Assets.playSound(Assets.clickSound);
            }
        });

        dialogGameOver.getButtonTable().pad(15);
        dialogGameOver.getButtonTable().add(buttonTryAgain).minWidth(160).minHeight(40).expand().padBottom(20);
        dialogGameOver.getButtonTable().row();
        dialogGameOver.getButtonTable().add(buttonMenu2).minWidth(160).minHeight(40).expand();
        dialogGameOver.getButtonTable().row();


        Label labelShare = new Label(Assets.languagesBundle.get("share_your_score_on_facebook"), Assets.styleLabelDialog);
        labelShare.setAlignment(Align.center);
        labelShare.setWrap(true);
        dialogGameOver.getButtonTable().add(labelShare).width(200).expand();
        dialogGameOver.getButtonTable().row();
        dialogGameOver.getButtonTable().add(buttonShare).expand();


        if (Settings.numberOfTimesPlayed % 5 == 0) {
            game.dialog.showDialogRate();
        }

        buttonPause = new ImageButton(Assets.styleImageButtonPause);
        buttonPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setPaused();
            }
        });

        labelLevel = new Label(Assets.languagesBundle.get("level") + " " + world.currentLevel, Assets.styleLabel);
        labelScore = new Label(Assets.languagesBundle.get("score") + " " + world.score, Assets.styleLabel);
        labelLivesCount = new Label("x" + world.oSpaceShip.lives, Assets.styleLabel);
        Image livesImage = new Image(Assets.spaceShip);

        scoresBarTable = new Table();
        scoresBarTable.setBackground(Assets.inGameStatusDrawable);
        scoresBarTable.setWidth(SCREEN_WIDTH);
        scoresBarTable.setHeight(30);
        scoresBarTable.setPosition(0, SCREEN_HEIGHT - 30);

        scoresBarTable.add(labelLevel).left();

        scoresBarTable.add(labelScore).center().expandX();

        scoresBarTable.add(livesImage).size(20).right();
        scoresBarTable.add(labelLivesCount).right();
        scoresBarTable.add(buttonPause).size(26).right().padLeft(8);

        stage.addActor(scoresBarTable);
    }

    private void setUpTutorial() {

        labelTiltYourDevice = new Label(Assets.languagesBundle.get("tilt_your_device_to_move_horizontally"), new LabelStyle(Assets.font45, Color.GREEN));
        labelTiltYourDevice.setWrap(true);
        labelTiltYourDevice.setAlignment(Align.center);
        labelTiltYourDevice.setPosition(0, 120);
        labelTiltYourDevice.setWidth(SCREEN_WIDTH);
        stage.addActor(labelTiltYourDevice);

        groupTutorial = new Group();

        Table boostTable = new Table();
        groupTutorial.addActor(boostTable);

        Image livesImage = new Image(Assets.upgLife);
        Image boostBombImage = new Image(Assets.boost2);
        Image boostShieldImage = new Image(Assets.boost3);
        Image boostUpgradeWeaponImage = new Image(Assets.boost1);

        Label labelLives = new Label(Assets.languagesBundle.get("get_one_extra_life"), Assets.styleLabel);
        Label labelBomb = new Label(Assets.languagesBundle.get("get_one_extra_missil"), Assets.styleLabel);
        Label labelShield = new Label(Assets.languagesBundle.get("get_a_shield"), Assets.styleLabel);
        Label labelUpgradeWeapon = new Label(Assets.languagesBundle.get("upgrade_your_weapon"), Assets.styleLabel);

        boostTable.setPosition(0, 340);
        boostTable.setWidth(SCREEN_WIDTH);

        int iconSize = 40;
        boostTable.add(livesImage).size(iconSize);
        boostTable.add(labelLives).padLeft(15).left();
        boostTable.row().padTop(10);
        boostTable.add(boostBombImage).size(iconSize);
        boostTable.add(labelBomb).padLeft(15).left();
        boostTable.row().padTop(10);
        boostTable.add(boostShieldImage).size(iconSize);
        boostTable.add(labelShield).padLeft(15).left();
        boostTable.row().padTop(10);
        boostTable.add(boostUpgradeWeaponImage).size(iconSize);
        boostTable.add(labelUpgradeWeapon).padLeft(15).left();

        Label touchLeft, touchRight;
        touchLeft = new Label(Assets.languagesBundle.get("touch_left_side_to_fire_missils"), Assets.styleLabel);
        touchLeft.setWrap(true);
        touchLeft.setWidth(160);
        touchLeft.setAlignment(Align.center);
        touchLeft.setPosition(0, 50);

        touchRight = new Label(Assets.languagesBundle.get("touch_right_side_to_fire"), Assets.styleLabel);
        touchRight.setWrap(true);
        touchRight.setWidth(160);
        touchRight.setAlignment(Align.center);
        touchRight.setPosition(165, 50);

        groupTutorial.addActor(touchRight);
        groupTutorial.addActor(touchLeft);
    }

    @Override
    public void update(float deltaTime) {
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
            if (tutorialScreenIndex == 0) {
                tutorialScreenIndex++;
                labelTiltYourDevice.remove();
                stage.addActor(groupTutorial);
            } else {
                state = GAME_READY;
                groupTutorial.remove();
            }
        }
    }

    private void updateReady() {
        if (Gdx.input.justTouched() && !game.dialog.isDialogShown()) {
            state = GAME_RUNNING;

            if (!Settings.isTiltControl) {
                stage.addActor(buttonLeft);
                stage.addActor(buttonRight);
                stage.addActor(buttonMissile);
                stage.addActor(buttonFire);
            }
        }
    }

    private void updateRunning(float deltaTime) {

        if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT) || Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.DPAD_RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
            acceleration = 0;
            if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT) || Gdx.input.isKeyPressed(Keys.A))
                acceleration = 5f;
            if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT) || Gdx.input.isKeyPressed(Keys.D))
                acceleration = -5f;

            world.update(deltaTime, acceleration, isFiring, isMissileFired);
        } else if (Settings.isTiltControl) {
            if (Gdx.input.justTouched()) {
                camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

                if (leftButton.contains(touchPoint.x, touchPoint.y)) {
                    isMissileFired = true;
                }
                if (rightButton.contains(touchPoint.x, touchPoint.y)) {
                    isFiring = true;
                }
            }
            world.update(deltaTime, Gdx.input.getAccelerometerX(), isFiring, isMissileFired);
        } else {
            world.update(deltaTime, acceleration, isFiring, isMissileFired);
        }

        if (gameLevel != world.currentLevel) {
            gameLevel = world.currentLevel;
            labelLevel.setText(Assets.languagesBundle.get("level") + " " + gameLevel);
        }

        labelScore.setText(Assets.languagesBundle.get("score") + " " + world.score);
        labelLivesCount.setText("x" + world.oSpaceShip.lives);

        if (world.state == World.STATE_GAME_OVER) {
            state = GAME_OVER;
            dialogGameOver.show(stage);
        }

        buttonMissile.setText(world.missileCount + "");

        isFiring = false;
        isMissileFired = false;
    }

    private void setPaused() {
        Assets.playSound(Assets.clickSound);
        state = GAME_PAUSE;
        world.state = World.STATE_PAUSED;
        dialogPause.show(stage);
    }

    @Override
    public void draw(float delta) {

        if (state != GAME_TUTORIAL)
            renderer.render(delta);
        else
            Assets.backgroundLayer.render(delta);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.enableBlending();
        batch.begin();

        switch (state) {
            case GAME_TUTORIAL:
                displayTutorial();
                break;
            case GAME_READY:
                drawTouchToStart();
                break;
            case GAME_RUNNING:
                showMissileCount();
                break;
        }
        batch.end();
    }

    float rotationAngle = 0;
    float rotationStep = .3f;

    private void displayTutorial() {
        if (tutorialScreenIndex == 0 && Settings.isTiltControl) {
            if (rotationAngle < -20 || rotationAngle > 20)
                rotationStep *= -1;
            rotationAngle += rotationStep;
            batch.draw(Assets.help1, SCREEN_WIDTH / 2f - 51, 190, 51, 0, 102, 200, 1, 1, rotationAngle);
        } else {
            batch.draw(Assets.helpClick, 155, 0, 10, 125);
        }
    }

    private void drawTouchToStart() {
        String touchToStart = Assets.languagesBundle.get("touch_to_start");
        float textWidth = Assets.getTextWidth(Assets.font45, touchToStart);
        Assets.font45.draw(batch, touchToStart, (SCREEN_WIDTH / 2f) - (textWidth / 2f), 220);
    }

    private void showMissileCount() {
        if (world.missileCount > 0 && Settings.isTiltControl) {
            batch.draw(Assets.missileAnimation.getKeyFrame(0), 1, 1, 8, 28);
            Assets.font15.draw(batch, "X" + world.missileCount, 10, 25);
        }
    }

    @Override
    public void hide() {
        Settings.addScore(world.score);
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
            isFiring = true;

            return true;
        } else if (keycode == Keys.ALT_LEFT) {
            isMissileFired = true;
            return true;
        }
        return false;
    }
}
