package com.nopalsoft.invaders.screens;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.invaders.Assets;
import com.nopalsoft.invaders.GalaxyInvadersGame;
import com.nopalsoft.invaders.Settings;
import com.nopalsoft.invaders.game_objects.SpaceShip;

public class SettingsScreen extends Screens {

    ImageButton tiltControl;
    ImageButton onScreenControl;
    Slider accelerometerSlider;
    TextButton buttonBack;
    Table menuControls;

    ImageButton buttonLeft, buttonRight, buttonMissile, buttonFire;

    Label touchLeft, touchRight;

    public final SpaceShip oSpaceShip;
    OrthographicCamera camRender;
    float accel;

    public SettingsScreen(final GalaxyInvadersGame game) {
        super(game);

        //Accelerometer Slider
        accelerometerSlider = new Slider(1, 20, 1f, false, Assets.styleSlider);
        accelerometerSlider.setPosition(70, 295);
        accelerometerSlider.setValue(21 - Settings.accelerometerSensitivity);
        accelerometerSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.accelerometerSensitivity = 21 - (int) ((Slider) actor).getValue();
            }
        });

        menuControls = new Table();
        menuControls.setPosition(SCREEN_WIDTH / 2f - 30, 380);// a la mitad menos 30

        onScreenControl = new ImageButton(Assets.styleImageButtonStyleCheckBox);
        if (!Settings.isTiltControl)
            onScreenControl.setChecked(true);
        onScreenControl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isTiltControl = false;
                onScreenControl.setChecked(true);
                tiltControl.setChecked(false);
                setOptions();
            }
        });

        tiltControl = new ImageButton(Assets.styleImageButtonStyleCheckBox);
        if (Settings.isTiltControl)
            tiltControl.setChecked(true);
        tiltControl.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Settings.isTiltControl = true;
                onScreenControl.setChecked(false);
                tiltControl.setChecked(true);
                setOptions();
            }
        });

        // OnScreen Controls

        buttonLeft = new ImageButton(Assets.buttonLeft);
        buttonLeft.setSize(65, 50);
        buttonLeft.setPosition(10, 5);
        buttonLeft.addListener(new ClickListener() {
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
        buttonRight = new ImageButton(Assets.buttonRight);
        buttonRight.setSize(65, 50);
        buttonRight.setPosition(85, 5);
        buttonRight.addListener(new ClickListener() {
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

        buttonMissile = new ImageButton(Assets.buttonMissile, Assets.buttonMissilePressed);
        buttonMissile.setSize(60, 60);
        buttonMissile.setPosition(SCREEN_WIDTH - 5 - 60 - 20 - 60, 5);
        buttonFire = new ImageButton(Assets.buttonFire, Assets.buttonFirePressed);
        buttonFire.setSize(60, 60);
        buttonFire.setPosition(SCREEN_WIDTH - 60 - 5, 5);

        menuControls.add(new Label(Assets.languagesBundle.get("on_screen_control"), Assets.styleLabel)).left();
        menuControls.add(onScreenControl).size(25);
        menuControls.row().padTop(10);
        menuControls.add(new Label(Assets.languagesBundle.get("tilt_control"), Assets.styleLabel)).left();
        menuControls.add(tiltControl).size(25);

        buttonBack = new TextButton(Assets.languagesBundle.get("back"), Assets.styleTextButtonBack);
        buttonBack.pad(0, 15, 35, 0);
        buttonBack.setSize(63, 63);
        buttonBack.setPosition(SCREEN_WIDTH - 63, SCREEN_HEIGHT - 63);
        buttonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new MainMenuScreen(game));
            }
        });

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

        setOptions();

        // I'm going to put the ship here to move too.
        oSpaceShip = new SpaceShip(WORLD_SCREEN_WIDTH / 2.0f, WORLD_SCREEN_HEIGHT / 3.0f); // Coloco la nave en posicion
        this.camRender = new OrthographicCamera(WORLD_SCREEN_WIDTH, WORLD_SCREEN_HEIGHT);
        camRender.position.set(WORLD_SCREEN_WIDTH / 2.0f, WORLD_SCREEN_HEIGHT / 2.0f, 0);

    }

    protected void setOptions() {
        stage.clear();
        accel = 0;// because sometimes the ship would stay moving when switching from tilt to control
        stage.addActor(buttonBack);
        stage.addActor(menuControls);
        stage.addActor(accelerometerSlider);
        if (Settings.isTiltControl)
            setTiltControls();
        else
            setOnScreenControl();
    }

    private void setTiltControls() {
        stage.addActor(touchLeft);
        stage.addActor(touchRight);
    }

    protected void setOnScreenControl() {
        stage.addActor(buttonLeft);
        stage.addActor(buttonRight);
        stage.addActor(buttonMissile);
        stage.addActor(buttonFire);
    }

    @Override
    public void update(float delta) {

        if (Settings.isTiltControl) {
            accel = Gdx.input.getAccelerometerX();
        } else {
            if (Gdx.app.getType() == ApplicationType.Applet || Gdx.app.getType() == ApplicationType.Desktop || Gdx.app.getType() == ApplicationType.WebGL) {
                accel = 0;
                if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT) || Gdx.input.isKeyPressed(Keys.A))
                    accel = 5f;
                if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT) || Gdx.input.isKeyPressed(Keys.D))
                    accel = -5f;
            }
        }
        oSpaceShip.velocity.x = -accel / Settings.accelerometerSensitivity * SpaceShip.NAVE_MOVE_SPEED;

        oSpaceShip.update(delta);
    }

    @Override
    public void draw(float delta) {

        oCam.update();
        batcher.setProjectionMatrix(oCam.combined);

        batcher.disableBlending();
        Assets.backgroundLayer.render(delta);

        stage.act(delta);
        stage.draw();

        batcher.enableBlending();
        batcher.begin();
        Assets.font45.draw(batcher, Assets.languagesBundle.get("control_options"), 10, 460);

        if (Settings.isTiltControl) {
            String tiltSensitive = Assets.languagesBundle.get("tilt_sensitive");
            float textWidth = Assets.getTextWidth(Assets.font15, tiltSensitive);
            Assets.font15.draw(batcher, tiltSensitive, SCREEN_WIDTH / 2f - textWidth / 2f, 335);
            batcher.draw(Assets.helpClick, 155, 0, 10, 125);
        } else {
            String speed = Assets.languagesBundle.get("speed");
            float textWidth = Assets.getTextWidth(Assets.font15, speed);
            Assets.font15.draw(batcher, speed, SCREEN_WIDTH / 2f - textWidth / 2f, 335);
        }
        Assets.font15.draw(batcher, (int) accelerometerSlider.getValue() + "", 215, 313);
        batcher.end();

        camRender.update();
        batcher.setProjectionMatrix(camRender.combined);
        batcher.begin();
        renderNave();
        batcher.end();
    }

    private void renderNave() {
        TextureRegion keyFrame;
        if (oSpaceShip.velocity.x < -3)
            keyFrame = Assets.spaceShipLeft;
        else if (oSpaceShip.velocity.x > 3)
            keyFrame = Assets.spaceShipRight;
        else
            keyFrame = Assets.spaceShip;

        batcher.draw(keyFrame, oSpaceShip.position.x - SpaceShip.DRAW_WIDTH / 2f, oSpaceShip.position.y - SpaceShip.DRAW_HEIGHT / 2f, SpaceShip.DRAW_WIDTH, SpaceShip.DRAW_HEIGHT);
    }

    @Override
    public boolean keyDown(int tecleada) {
        if (tecleada == Keys.BACK || tecleada == Keys.ESCAPE) {
            Assets.playSound(Assets.clickSound);
            game.setScreen(new MainMenuScreen(game));
            return true;
        }
        return false;
    }
}
