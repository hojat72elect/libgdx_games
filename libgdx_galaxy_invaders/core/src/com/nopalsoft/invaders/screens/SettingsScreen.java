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
import com.nopalsoft.invaders.MainInvaders;
import com.nopalsoft.invaders.Settings;
import com.nopalsoft.invaders.frame.Nave;

public class SettingsScreen extends Screens {

    ImageButton tiltControl;
    ImageButton onScreenControl;
    Slider aceletometerSlider;
    TextButton btBack;
    Table menuControls;

    ImageButton btLeft, btRight, btMissil, btFire;

    Label touchLeft, touchRight;

    public final Nave oNave;
    OrthographicCamera camRender;
    float accel;

    public SettingsScreen(final MainInvaders game) {
        super(game);

        /* Acelerometer Slider */
        aceletometerSlider = new Slider(1, 20, 1f, false, Assets.styleSlider);
        aceletometerSlider.setPosition(70, 295);
        aceletometerSlider.setValue(21 - Settings.aceletometerSensitive);
        aceletometerSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Settings.aceletometerSensitive = 21 - (int) ((Slider) actor).getValue();

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

        /* OnScreenControls */

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

        btMissil = new ImageButton(Assets.btMissil, Assets.btMissilDown);
        btMissil.setSize(60, 60);
        btMissil.setPosition(SCREEN_WIDTH - 5 - 60 - 20 - 60, 5);
        btFire = new ImageButton(Assets.btFire, Assets.btFireDown);
        btFire.setSize(60, 60);
        btFire.setPosition(SCREEN_WIDTH - 60 - 5, 5);

        menuControls.add(new Label(Assets.idiomas.get("on_screen_control"), Assets.styleLabel)).left();
        menuControls.add(onScreenControl).size(25);
        menuControls.row().padTop(10);
        menuControls.add(new Label(Assets.idiomas.get("tilt_control"), Assets.styleLabel)).left();
        menuControls.add(tiltControl).size(25);

        btBack = new TextButton(Assets.idiomas.get("back"), Assets.styleTextButtonBack);
        btBack.pad(0, 15, 35, 0);
        btBack.setSize(63, 63);
        btBack.setPosition(SCREEN_WIDTH - 63, SCREEN_HEIGHT - 63);
        btBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Assets.playSound(Assets.clickSound);
                game.setScreen(new MainMenuScreen(game));
            }
        });

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

        setOptions();

        // Voy a poner a la nave aqui que se mueva tambien;
        oNave = new Nave(WORLD_SCREEN_WIDTH / 2.0f, WORLD_SCREEN_HEIGHT / 3.0f); // Coloco la nave en posicion
        this.camRender = new OrthographicCamera(WORLD_SCREEN_WIDTH, WORLD_SCREEN_HEIGHT);
        camRender.position.set(WORLD_SCREEN_WIDTH / 2.0f, WORLD_SCREEN_HEIGHT / 2.0f, 0);
        // menuControls.debug();

    }

    protected void setOptions() {
        stage.clear();
        accel = 0;// porque a veces se quedaba moviendo la nave cuando se pasaba de tilt a control
        stage.addActor(btBack);
        stage.addActor(menuControls);
        stage.addActor(aceletometerSlider);
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
        stage.addActor(btLeft);
        stage.addActor(btRight);
        stage.addActor(btMissil);
        stage.addActor(btFire);
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
        oNave.velocity.x = -accel / Settings.aceletometerSensitive * Nave.NAVE_MOVE_SPEED;

        oNave.update(delta);
    }

    @Override
    public void draw(float delta) {

        oCam.update();
        batcher.setProjectionMatrix(oCam.combined);

        batcher.disableBlending();
        Assets.parallaxFondo.render(delta);

        stage.act(delta);
        stage.draw();

        batcher.enableBlending();
        batcher.begin();
        Assets.font45.draw(batcher, Assets.idiomas.get("control_options"), 10, 460);

        if (Settings.isTiltControl) {
            String tiltSensitive = Assets.idiomas.get("tilt_sensitive");
            float textWidth = Assets.getTextWidth(Assets.font15, tiltSensitive);
            Assets.font15.draw(batcher, tiltSensitive, SCREEN_WIDTH / 2f - textWidth / 2f, 335);
            batcher.draw(Assets.clickAyuda, 155, 0, 10, 125);
        } else {
            String speed = Assets.idiomas.get("speed");
            float textWidth = Assets.getTextWidth(Assets.font15, speed);
            Assets.font15.draw(batcher, speed, SCREEN_WIDTH / 2f - textWidth / 2f, 335);

        }
        Assets.font15.draw(batcher, (int) aceletometerSlider.getValue() + "", 215, 313);
        batcher.end();

        camRender.update();
        batcher.setProjectionMatrix(camRender.combined);
        batcher.begin();
        renderNave(delta);
        batcher.end();

    }

    private void renderNave(float delta) {
        TextureRegion keyFrame;
        if (oNave.velocity.x < -3)
            keyFrame = Assets.naveLeft;
        else if (oNave.velocity.x > 3)
            keyFrame = Assets.naveRight;
        else
            keyFrame = Assets.nave;

        batcher.draw(keyFrame, oNave.position.x - Nave.DRAW_WIDTH / 2f, oNave.position.y - Nave.DRAW_HEIGHT / 2f, Nave.DRAW_WIDTH, Nave.DRAW_HEIGHT);
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
