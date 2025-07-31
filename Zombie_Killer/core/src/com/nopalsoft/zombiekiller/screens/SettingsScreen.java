package com.nopalsoft.zombiekiller.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.zombiekiller.Assets;
import com.nopalsoft.zombiekiller.MainZombie;
import com.nopalsoft.zombiekiller.Settings;
import com.nopalsoft.zombiekiller.scene2d.TouchPadControls;

public class SettingsScreen extends Screens {

    public static final int DEFAULT_SIZE_PAD = 170;
    public static final Vector2 DEFAULT_POSITION_PAD = new Vector2(10, 10);

    public static final int DEFAULT_SIZE_BUTTONS = 80;
    public static final Vector2 DEFAULT_POSITION_BUTTON_JUMP = new Vector2(560, 20);
    public static final Vector2 DEFAULT_POSITION_BUTTON_FIRE = new Vector2(680, 20);

    TouchPadControls touchPadControls;
    Touchpad pad;
    Image buttonJump, buttonFire;
    Vector3 dragPoint;

    Button buttonEnablePad;
    Slider sliderPadSize;
    Slider sliderButtonSize;

    TextButton buttonDefaults;

    Button buttonMenu;

    public SettingsScreen(final MainZombie game) {
        super(game);
        dragPoint = new Vector3();

        Table tableSizes = new Table();
        tableSizes.setPosition(25, 275);

        Table tbEnablePad = new Table();

        Label lbEnablePad = new Label("Enable Pad", Assets.labelStyleChico);
        buttonEnablePad = new Button(Assets.upgradeOff, new TextureRegionDrawable(Assets.itemSkull), new TextureRegionDrawable(Assets.itemSkull));
        buttonEnablePad.setChecked(Settings.isPadEnabled);

        ClickListener clickEnablePad = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isPadEnabled = !Settings.isPadEnabled;
                buttonEnablePad.setChecked(Settings.isPadEnabled);

                pad.remove();
                touchPadControls.remove();

                if (Settings.isPadEnabled) {
                    pad.setPosition(Settings.padPositionX, Settings.padPositionY);
                    stage.addActor(pad);
                } else {
                    touchPadControls.setPosition(Settings.padPositionX, Settings.padPositionY);
                    stage.addActor(touchPadControls);
                }
            }
        };

        buttonEnablePad.addListener(clickEnablePad);
        lbEnablePad.addListener(clickEnablePad);

        tbEnablePad.add(lbEnablePad);
        tbEnablePad.add(buttonEnablePad).size(30).padLeft(10);

        // Size pad
        Label lbPadSize = new Label("Pad size:", Assets.labelStyleChico);
        sliderPadSize = new Slider(.5f, 1.5f, .1f, false, Assets.sliderStyle);
        sliderPadSize.setValue(1);// LA mitad es 1
        sliderPadSize.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float size = DEFAULT_SIZE_PAD * sliderPadSize.getValue();
                pad.setSize(size, size);
                Settings.padSize = size;
                touchPadControls.setNewSize(size);
            }
        });

        // Size buttons
        Label lbButtonsSize = new Label("Buttons size:", Assets.labelStyleChico);
        sliderButtonSize = new Slider(.5f, 1.5f, .1f, false, Assets.sliderStyle);
        sliderButtonSize.setValue(1);// LA mitad es 1
        sliderButtonSize.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                float size = DEFAULT_SIZE_BUTTONS * sliderButtonSize.getValue();
                buttonJump.setSize(size, size);
                buttonFire.setSize(size, size);
                Settings.buttonSize = size;
            }
        });

        buttonDefaults = new TextButton("Defaults", Assets.styleTextButtonBuy);
        addPressEffect(buttonDefaults);
        buttonDefaults.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonFire.setSize(DEFAULT_SIZE_BUTTONS, DEFAULT_SIZE_BUTTONS);
                buttonJump.setSize(DEFAULT_SIZE_BUTTONS, DEFAULT_SIZE_BUTTONS);
                pad.setSize(DEFAULT_SIZE_PAD, DEFAULT_SIZE_PAD);
                touchPadControls.setNewSize(DEFAULT_SIZE_PAD);
                sliderButtonSize.setValue(1);
                sliderPadSize.setValue(1);

                pad.setPosition(DEFAULT_POSITION_PAD.x, DEFAULT_POSITION_PAD.y);
                touchPadControls.setPosition(DEFAULT_POSITION_PAD.x, DEFAULT_POSITION_PAD.y);
                buttonFire.setPosition(DEFAULT_POSITION_BUTTON_FIRE.x, DEFAULT_POSITION_BUTTON_FIRE.y);
                buttonJump.setPosition(DEFAULT_POSITION_BUTTON_JUMP.x, DEFAULT_POSITION_BUTTON_JUMP.y);

                Settings.saveNewPadSettings(pad.getX(), pad.getY(), pad.getWidth());
                Settings.saveNewButtonFireSettings(buttonFire.getX(), buttonFire.getY(), buttonFire.getWidth());
                Settings.saveNewButtonJumpSettings(buttonJump.getX(), buttonJump.getY(), buttonJump.getWidth());
            }
        });

        tableSizes.defaults().left();

        tableSizes.add(tbEnablePad).colspan(2);
        tableSizes.row().padTop(20);

        tableSizes.add(lbPadSize);
        tableSizes.add(lbButtonsSize).padLeft(100);

        tableSizes.row().padTop(20);
        tableSizes.add(sliderPadSize).width(200);
        tableSizes.add(sliderButtonSize).width(200).padLeft(100);

        tableSizes.row().colspan(2).padTop(20);
        tableSizes.add(buttonDefaults).height(50);

        tableSizes.pack();

        touchPadControls = new TouchPadControls();
        touchPadControls.setPosition(Settings.padPositionX, Settings.padPositionY);
        touchPadControls.getColor().a = .5f;
        touchPadControls.addListener(new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                stage.getCamera().unproject(dragPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
                touchPadControls.setPosition(dragPoint.x - touchPadControls.getWidth() / 2f, dragPoint.y - touchPadControls.getHeight() / 2f);
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                Settings.saveNewPadSettings(touchPadControls.getX(), touchPadControls.getY(), touchPadControls.widthButtons);
            }
        });

        pad = new Touchpad(1000, Assets.touchPadStyle);
        pad.setPosition(Settings.padPositionX, Settings.padPositionY);
        pad.setSize(Settings.padSize, Settings.padSize);
        pad.getColor().a = .5f;
        pad.addListener(new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                stage.getCamera().unproject(dragPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
                pad.setPosition(dragPoint.x - pad.getWidth() / 2f, dragPoint.y - pad.getHeight() / 2f);
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                Settings.saveNewPadSettings(pad.getX(), pad.getY(), pad.getWidth());
            }
        });

        buttonJump = new Image(Assets.btUp);
        buttonJump.setSize(Settings.buttonSize, Settings.buttonSize);
        buttonJump.setPosition(Settings.buttonJumpPositionX, Settings.buttonJumpPositionY);
        buttonJump.getColor().a = .5f;
        addPressEffect(buttonJump);
        buttonJump.addListener(new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                stage.getCamera().unproject(dragPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
                buttonJump.setPosition(dragPoint.x - buttonJump.getWidth() / 2f, dragPoint.y - buttonJump.getHeight() / 2f);
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                Settings.saveNewButtonJumpSettings(buttonJump.getX(), buttonJump.getY(), buttonJump.getWidth());
            }
        });

        buttonFire = new Image(Assets.btFire);
        buttonFire.setSize(Settings.buttonSize, Settings.buttonSize);
        buttonFire.setPosition(Settings.buttonFirePositionX, Settings.buttonFirePositionY);
        buttonFire.getColor().a = .5f;
        addPressEffect(buttonFire);
        buttonFire.addListener(new DragListener() {
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                stage.getCamera().unproject(dragPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
                buttonFire.setPosition(dragPoint.x - buttonFire.getWidth() / 2f, dragPoint.y - buttonFire.getHeight() / 2f);
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                Settings.saveNewButtonFireSettings(buttonFire.getX(), buttonFire.getY(), buttonFire.getWidth());
            }
        });

        buttonMenu = new Button(Assets.btMenu);
        buttonMenu.setSize(45, 45);
        buttonMenu.setPosition(SCREEN_WIDTH - 50, SCREEN_HEIGHT - 50);
        addPressEffect(buttonMenu);
        buttonMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                changeScreenWithFadeOut(MainMenuScreen.class, game);
            }
        });

        if (Settings.isPadEnabled)
            stage.addActor(pad);
        else
            stage.addActor(touchPadControls);
        stage.addActor(buttonJump);
        stage.addActor(buttonFire);
        stage.addActor(tableSizes);
        stage.addActor(buttonMenu);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void draw(float delta) {
        batcher.begin();
        batcher.draw(Assets.background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        batcher.draw(Assets.moon, 450, 220, 350, 255);

        batcher.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            changeScreenWithFadeOut(MainMenuScreen.class, game);
            return true;
        }
        return false;
    }
}
