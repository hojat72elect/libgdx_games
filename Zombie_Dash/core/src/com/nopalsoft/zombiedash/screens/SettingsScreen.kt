package com.nopalsoft.zombiedash.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Slider
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.DragListener
import com.nopalsoft.zombiedash.Assets
import com.nopalsoft.zombiedash.MainZombieDash
import com.nopalsoft.zombiedash.Settings

class SettingsScreen(game: MainZombieDash) : Screens(game) {
    var btJump: Image
    var btFire: Image
    var dragPoint: Vector3

    var sliderButtonSize: Slider

    var btDefaults: TextButton
    var btFacebookLogin: TextButton

    var btMenu: Button

    init {
        dragPoint = Vector3()

        val tbSizes = Table()
        tbSizes.setPosition(25f, 210f)

        // tbSizes.debug();

        // Size buttons
        val lbButtonsSize = Label(game.idiomas.get("button_size"), Assets.labelStyleChico)
        sliderButtonSize = Slider(.5f, 1.5f, .1f, false, Assets.sliderStyle)
        sliderButtonSize.setValue(1f) // LA mitad es 1
        sliderButtonSize.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                val size: Float = DEFAULT_SIZE_BUTTONS * sliderButtonSize.value
                btJump.setSize(size, size)
                btFire.setSize(size, size)
                Settings.buttonSize = size
            }
        })

        btDefaults = TextButton(game.idiomas.get("defaults"), Assets.styleTextButtonBuy)
        addEfectoPress(btDefaults)
        btDefaults.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                btFire.setSize(DEFAULT_SIZE_BUTTONS.toFloat(), DEFAULT_SIZE_BUTTONS.toFloat())
                btJump.setSize(DEFAULT_SIZE_BUTTONS.toFloat(), DEFAULT_SIZE_BUTTONS.toFloat())

                sliderButtonSize.setValue(1f)

                btFire.setPosition(DEFAULT_POSITION_BUTTON_FIRE.x, DEFAULT_POSITION_BUTTON_FIRE.y)
                btJump.setPosition(DEFAULT_POSITION_BUTTON_JUMP.x, DEFAULT_POSITION_BUTTON_JUMP.y)

                Settings.saveNewButtonFireSettings(btFire.getX(), btFire.getY(), btFire.getWidth())
                Settings.saveNewButtonJumpSettings(btJump.getX(), btJump.getY(), btJump.getWidth())
            }
        })

        tbSizes.defaults().left()

        tbSizes.row().padTop(20f)
        tbSizes.add<Label?>(lbButtonsSize)
        tbSizes.row()
        tbSizes.add<Slider?>(sliderButtonSize).width(200f)

        tbSizes.row().padTop(15f)
        tbSizes.add<TextButton?>(btDefaults).height(50f)

        tbSizes.pack()

        btJump = Image(Assets.btJump)
        btJump.setSize(Settings.buttonSize, Settings.buttonSize)
        btJump.setPosition(Settings.buttonJumpPositionX, Settings.buttonJumpPositionY)
        btJump.getColor().a = .5f
        addEfectoPress(btJump)
        btJump.addListener(object : DragListener() {
            override fun drag(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                stage.camera.unproject(dragPoint.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
                btJump.setPosition(dragPoint.x - btJump.getWidth() / 2f, dragPoint.y - btJump.getHeight() / 2f)
            }

            override fun dragStop(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                Settings.saveNewButtonJumpSettings(btJump.getX(), btJump.getY(), btJump.getWidth())
            }
        })

        btFire = Image(Assets.btFire)
        btFire.setSize(Settings.buttonSize, Settings.buttonSize)
        btFire.setPosition(Settings.buttonFirePositionX, Settings.buttonFirePositionY)
        btFire.getColor().a = .5f
        addEfectoPress(btFire)
        btFire.addListener(object : DragListener() {
            override fun drag(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                stage.camera.unproject(dragPoint.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
                btFire.setPosition(dragPoint.x - btFire.getWidth() / 2f, dragPoint.y - btFire.getHeight() / 2f)
            }

            override fun dragStop(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                Settings.saveNewButtonFireSettings(btFire.getX(), btFire.getY(), btFire.getWidth())
            }
        })

        btMenu = Button(Assets.btMenu)
        btMenu.setSize(45f, 45f)
        btMenu.setPosition((SCREEN_WIDTH - 50).toFloat(), (SCREEN_HEIGHT - 50).toFloat())
        addEfectoPress(btMenu)
        btMenu.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                changeScreenWithFadeOut(MainMenuScreen::class.java, game)
            }
        })

        // Facebook
        btFacebookLogin = TextButton("", Assets.styleTextButtonFacebook)
        btFacebookLogin.label.setWrap(true)
        btFacebookLogin.setSize(160f, 64f)

        addEfectoPress(btFacebookLogin)

        val tbFacebookStuff = Table()
        tbFacebookStuff.setPosition(25f, 350f)

        val lbLoginFacebook = Label(game.idiomas.get("facebook_login_description"), Assets.labelStyleChico)
        lbLoginFacebook.setWrap(true)

        tbFacebookStuff.add<Label?>(lbLoginFacebook).width(600f).expandX().left()

        tbFacebookStuff.row()
        tbFacebookStuff.add<TextButton?>(btFacebookLogin).left()

        tbFacebookStuff.pack()

        stage.addActor(btJump)
        stage.addActor(btFire)
        stage.addActor(tbSizes)
        stage.addActor(btMenu)
        stage.addActor(tbFacebookStuff)
    }

    override fun update(delta: Float) {}

    override fun draw(delta: Float) {
        Assets.parallaxBackground.render(delta)
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            changeScreenWithFadeOut(MainMenuScreen::class.java, game!!)
            return true
        }
        return false
    }

    companion object {
        const val DEFAULT_SIZE_BUTTONS: Int = 100

        @JvmField
        val DEFAULT_POSITION_BUTTON_JUMP: Vector2 = Vector2(30f, 20f)

        @JvmField
        val DEFAULT_POSITION_BUTTON_FIRE: Vector2 = Vector2(670f, 20f)
    }
}
