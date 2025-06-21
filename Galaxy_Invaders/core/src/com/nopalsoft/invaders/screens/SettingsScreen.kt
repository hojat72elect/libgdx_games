package com.nopalsoft.invaders.screens

import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.nopalsoft.invaders.Assets
import com.nopalsoft.invaders.Assets.getTextWidth
import com.nopalsoft.invaders.Assets.playSound
import com.nopalsoft.invaders.GalaxyInvadersGame
import com.nopalsoft.invaders.Settings
import com.nopalsoft.invaders.game_objects.SpaceShip

class SettingsScreen(game: GalaxyInvadersGame) : Screens(game) {
    var tiltControl: ImageButton = ImageButton(Assets.styleImageButtonStyleCheckBox)
    var onScreenControl: ImageButton

    //Accelerometer Slider
    var accelerometerSlider: Slider = Slider(1f, 20f, 1f, false, Assets.styleSlider)
    var buttonBack: TextButton
    var menuControls: Table

    var buttonLeft: ImageButton
    var buttonRight: ImageButton
    var buttonMissile: ImageButton
    var buttonFire: ImageButton

    var touchLeft: Label
    var touchRight: Label

    val spaceShip: SpaceShip

    var acceleration: Float = 0f

    init {
        accelerometerSlider.setPosition(70f, 295f)
        accelerometerSlider.setValue((21 - Settings.accelerometerSensitivity).toFloat())
        accelerometerSlider.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent, actor: Actor) {
                Settings.accelerometerSensitivity = 21 - (actor as Slider).value.toInt()
            }
        })

        menuControls = Table()
        menuControls.setPosition(SCREEN_WIDTH / 2f - 30, 380f) // a la mitad menos 30

        onScreenControl = ImageButton(Assets.styleImageButtonStyleCheckBox)
        if (!Settings.isTiltControl) onScreenControl.isChecked = true
        onScreenControl.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Settings.isTiltControl = false
                onScreenControl.isChecked = true
                tiltControl.isChecked = false
                setOptions()
            }
        })


        if (Settings.isTiltControl) tiltControl.isChecked = true
        tiltControl.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Settings.isTiltControl = true
                onScreenControl.isChecked = false
                tiltControl.isChecked = true
                setOptions()
            }
        })

        // OnScreen Controls
        buttonLeft = ImageButton(Assets.buttonLeft)
        buttonLeft.setSize(65f, 50f)
        buttonLeft.setPosition(10f, 5f)
        buttonLeft.addListener(object : ClickListener() {
            override fun enter(event: InputEvent, x: Float, y: Float, pointer: Int, fromActor: Actor) {
                acceleration = 5f
            }

            override fun exit(event: InputEvent, x: Float, y: Float, pointer: Int, toActor: Actor) {
                acceleration = 0f
                super.exit(event, x, y, pointer, toActor)
            }
        })
        buttonRight = ImageButton(Assets.buttonRight)
        buttonRight.setSize(65f, 50f)
        buttonRight.setPosition(85f, 5f)
        buttonRight.addListener(object : ClickListener() {
            override fun enter(event: InputEvent, x: Float, y: Float, pointer: Int, fromActor: Actor) {
                acceleration = -5f
            }

            override fun exit(event: InputEvent, x: Float, y: Float, pointer: Int, toActor: Actor) {
                acceleration = 0f
                super.exit(event, x, y, pointer, toActor)
            }
        })

        buttonMissile = ImageButton(Assets.buttonMissile, Assets.buttonMissilePressed)
        buttonMissile.setSize(60f, 60f)
        buttonMissile.setPosition((SCREEN_WIDTH - 5 - 60 - 20 - 60).toFloat(), 5f)
        buttonFire = ImageButton(Assets.buttonFire, Assets.buttonFirePressed)
        buttonFire.setSize(60f, 60f)
        buttonFire.setPosition((SCREEN_WIDTH - 60 - 5).toFloat(), 5f)

        menuControls.add(Label(Assets.languagesBundle!!["on_screen_control"], Assets.styleLabel)).left()
        menuControls.add(onScreenControl).size(25f)
        menuControls.row().padTop(10f)
        menuControls.add(Label(Assets.languagesBundle!!["tilt_control"], Assets.styleLabel)).left()
        menuControls.add(tiltControl).size(25f)

        buttonBack = TextButton(Assets.languagesBundle!!["back"], Assets.styleTextButtonBack)
        buttonBack.pad(0f, 15f, 35f, 0f)
        buttonBack.setSize(63f, 63f)
        buttonBack.setPosition((SCREEN_WIDTH - 63).toFloat(), (SCREEN_HEIGHT - 63).toFloat())
        buttonBack.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                playSound(Assets.clickSound!!)
                game.screen = MainMenuScreen(game)
            }
        })

        touchLeft = Label(Assets.languagesBundle!!["touch_left_side_to_fire_missils"], Assets.styleLabel)
        touchLeft.wrap = true
        touchLeft.width = 160f
        touchLeft.setAlignment(Align.center)
        touchLeft.setPosition(0f, 50f)

        touchRight = Label(Assets.languagesBundle!!["touch_right_side_to_fire"], Assets.styleLabel)
        touchRight.wrap = true
        touchRight.width = 160f
        touchRight.setAlignment(Align.center)
        touchRight.setPosition(165f, 50f)

        setOptions()

        // I'm going to put the ship here to move too.
        spaceShip = SpaceShip(WORLD_SCREEN_WIDTH / 2.0f, WORLD_SCREEN_HEIGHT / 3.0f) // Coloco la nave en posicion
        this.camera = OrthographicCamera(WORLD_SCREEN_WIDTH.toFloat(), WORLD_SCREEN_HEIGHT.toFloat())
        camera.position[WORLD_SCREEN_WIDTH / 2.0f, WORLD_SCREEN_HEIGHT / 2.0f] = 0f
    }

    protected fun setOptions() {
        stage!!.clear()
        acceleration = 0f // because sometimes the ship would stay moving when switching from tilt to control
        stage!!.addActor(buttonBack)
        stage!!.addActor(menuControls)
        stage!!.addActor(accelerometerSlider)
        if (Settings.isTiltControl) setTiltControls()
        else setOnScreenControl()
    }

    private fun setTiltControls() {
        stage!!.addActor(touchLeft)
        stage!!.addActor(touchRight)
    }

    protected fun setOnScreenControl() {
        stage!!.addActor(buttonLeft)
        stage!!.addActor(buttonRight)
        stage!!.addActor(buttonMissile)
        stage!!.addActor(buttonFire)
    }

    override fun update(delta: Float) {
        if (Settings.isTiltControl) {
            acceleration = Gdx.input.accelerometerX
        } else {
            if (Gdx.app.type == ApplicationType.Applet || Gdx.app.type == ApplicationType.Desktop || Gdx.app.type == ApplicationType.WebGL) {
                acceleration = 0f
                if (Gdx.input.isKeyPressed(Input.Keys.DPAD_LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) acceleration = 5f
                if (Gdx.input.isKeyPressed(Input.Keys.DPAD_RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) acceleration = -5f
            }
        }
        spaceShip.velocity.x = -acceleration / Settings.accelerometerSensitivity * SpaceShip.SPACESHIP_SPEED

        spaceShip.update(delta)
    }

    override fun draw(delta: Float) {
        camera.update()
        batch!!.projectionMatrix = camera.combined

        batch!!.disableBlending()
        Assets.backgroundLayer!!.render(delta)

        stage!!.act(delta)
        stage!!.draw()

        batch!!.enableBlending()
        batch!!.begin()
        Assets.font45!!.draw(batch, Assets.languagesBundle!!["control_options"], 10f, 460f)

        if (Settings.isTiltControl) {
            val tiltSensitive = Assets.languagesBundle!!["tilt_sensitive"]
            val textWidth = getTextWidth(Assets.font15, tiltSensitive)
            Assets.font15!!.draw(batch, tiltSensitive, SCREEN_WIDTH / 2f - textWidth / 2f, 335f)
            batch!!.draw(Assets.helpClick, 155f, 0f, 10f, 125f)
        } else {
            val speed = Assets.languagesBundle!!["speed"]
            val textWidth = getTextWidth(Assets.font15, speed)
            Assets.font15!!.draw(batch, speed, SCREEN_WIDTH / 2f - textWidth / 2f, 335f)
        }
        Assets.font15!!.draw(batch, accelerometerSlider.value.toInt().toString() + "", 215f, 313f)
        batch!!.end()

        camera.update()
        batch!!.projectionMatrix = camera.combined
        batch!!.begin()
        renderNave()
        batch!!.end()
    }

    private fun renderNave() {
        val keyFrame: TextureRegion? = if (spaceShip.velocity.x < -3) Assets.spaceShipLeft
        else if (spaceShip.velocity.x > 3) Assets.spaceShipRight
        else Assets.spaceShip

        batch!!.draw(keyFrame, spaceShip.position.x - SpaceShip.DRAW_WIDTH / 2f, spaceShip.position.y - SpaceShip.DRAW_HEIGHT / 2f, SpaceShip.DRAW_WIDTH, SpaceShip.DRAW_HEIGHT)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
            playSound(Assets.clickSound!!)
            game.screen = MainMenuScreen(game)
            return true
        }
        return false
    }
}
