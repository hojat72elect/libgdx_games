package com.nopalsoft.zombiekiller.screens

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
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.DragListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.zombiekiller.Assets
import com.nopalsoft.zombiekiller.MainZombie
import com.nopalsoft.zombiekiller.Settings
import com.nopalsoft.zombiekiller.scene2d.TouchPadControls

class SettingsScreen(game: MainZombie) : Screens(game) {
    var touchPadControls: TouchPadControls
    var pad: Touchpad
    var buttonJump: Image
    var buttonFire: Image
    var dragPoint: Vector3

    var buttonEnablePad: Button
    var sliderPadSize: Slider
    var sliderButtonSize: Slider

    var buttonDefaults: TextButton

    var buttonMenu: Button

    init {
        dragPoint = Vector3()

        val tableSizes = Table()
        tableSizes.setPosition(25f, 275f)

        val tbEnablePad = Table()

        val lbEnablePad = Label("Enable Pad", Assets.labelStyleChico)
        buttonEnablePad = Button(Assets.upgradeOff, TextureRegionDrawable(Assets.itemSkull), TextureRegionDrawable(Assets.itemSkull))
        buttonEnablePad.setChecked(Settings.isPadEnabled)

        val clickEnablePad: ClickListener = object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.isPadEnabled = !Settings.isPadEnabled
                buttonEnablePad.setChecked(Settings.isPadEnabled)

                pad.remove()
                touchPadControls.remove()

                if (Settings.isPadEnabled) {
                    pad.setPosition(Settings.padPositionX, Settings.padPositionY)
                    stage!!.addActor(pad)
                } else {
                    touchPadControls.setPosition(Settings.padPositionX, Settings.padPositionY)
                    stage!!.addActor(touchPadControls)
                }
            }
        }

        buttonEnablePad.addListener(clickEnablePad)
        lbEnablePad.addListener(clickEnablePad)

        tbEnablePad.add<Label?>(lbEnablePad)
        tbEnablePad.add<Button?>(buttonEnablePad).size(30f).padLeft(10f)

        // Size pad
        val lbPadSize = Label("Pad size:", Assets.labelStyleChico)
        sliderPadSize = Slider(.5f, 1.5f, .1f, false, Assets.sliderStyle)
        sliderPadSize.setValue(1f) // LA mitad es 1
        sliderPadSize.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                val size: Float = DEFAULT_SIZE_PAD * sliderPadSize.value
                pad.setSize(size, size)
                Settings.padSize = size
                touchPadControls.setNewSize(size)
            }
        })

        // Size buttons
        val lbButtonsSize = Label("Buttons size:", Assets.labelStyleChico)
        sliderButtonSize = Slider(.5f, 1.5f, .1f, false, Assets.sliderStyle)
        sliderButtonSize.setValue(1f) // LA mitad es 1
        sliderButtonSize.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                val size: Float = DEFAULT_SIZE_BUTTONS * sliderButtonSize.value
                buttonJump.setSize(size, size)
                buttonFire.setSize(size, size)
                Settings.buttonSize = size
            }
        })

        buttonDefaults = TextButton("Defaults", Assets.styleTextButtonBuy)
        addPressEffect(buttonDefaults)
        buttonDefaults.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                buttonFire.setSize(DEFAULT_SIZE_BUTTONS.toFloat(), DEFAULT_SIZE_BUTTONS.toFloat())
                buttonJump.setSize(DEFAULT_SIZE_BUTTONS.toFloat(), DEFAULT_SIZE_BUTTONS.toFloat())
                pad.setSize(DEFAULT_SIZE_PAD.toFloat(), DEFAULT_SIZE_PAD.toFloat())
                touchPadControls.setNewSize(DEFAULT_SIZE_PAD.toFloat())
                sliderButtonSize.setValue(1f)
                sliderPadSize.setValue(1f)

                pad.setPosition(DEFAULT_POSITION_PAD.x, DEFAULT_POSITION_PAD.y)
                touchPadControls.setPosition(DEFAULT_POSITION_PAD.x, DEFAULT_POSITION_PAD.y)
                buttonFire.setPosition(DEFAULT_POSITION_BUTTON_FIRE.x, DEFAULT_POSITION_BUTTON_FIRE.y)
                buttonJump.setPosition(DEFAULT_POSITION_BUTTON_JUMP.x, DEFAULT_POSITION_BUTTON_JUMP.y)

                Settings.saveNewPadSettings(pad.getX(), pad.getY(), pad.getWidth())
                Settings.saveNewButtonFireSettings(buttonFire.getX(), buttonFire.getY(), buttonFire.getWidth())
                Settings.saveNewButtonJumpSettings(buttonJump.getX(), buttonJump.getY(), buttonJump.getWidth())
            }
        })

        tableSizes.defaults().left()

        tableSizes.add<Table?>(tbEnablePad).colspan(2)
        tableSizes.row().padTop(20f)

        tableSizes.add<Label?>(lbPadSize)
        tableSizes.add<Label?>(lbButtonsSize).padLeft(100f)

        tableSizes.row().padTop(20f)
        tableSizes.add<Slider?>(sliderPadSize).width(200f)
        tableSizes.add<Slider?>(sliderButtonSize).width(200f).padLeft(100f)

        tableSizes.row().colspan(2).padTop(20f)
        tableSizes.add<TextButton?>(buttonDefaults).height(50f)

        tableSizes.pack()

        touchPadControls = TouchPadControls()
        touchPadControls.setPosition(Settings.padPositionX, Settings.padPositionY)
        touchPadControls.getColor().a = .5f
        touchPadControls.addListener(object : DragListener() {
            override fun drag(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                stage!!.camera.unproject(dragPoint.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
                touchPadControls.setPosition(dragPoint.x - touchPadControls.getWidth() / 2f, dragPoint.y - touchPadControls.getHeight() / 2f)
            }

            override fun dragStop(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                Settings.saveNewPadSettings(touchPadControls.getX(), touchPadControls.getY(), touchPadControls.widthButtons)
            }
        })

        pad = Touchpad(1000f, Assets.touchPadStyle)
        pad.setPosition(Settings.padPositionX, Settings.padPositionY)
        pad.setSize(Settings.padSize, Settings.padSize)
        pad.getColor().a = .5f
        pad.addListener(object : DragListener() {
            override fun drag(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                stage!!.camera.unproject(dragPoint.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
                pad.setPosition(dragPoint.x - pad.getWidth() / 2f, dragPoint.y - pad.getHeight() / 2f)
            }

            override fun dragStop(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                Settings.saveNewPadSettings(pad.getX(), pad.getY(), pad.getWidth())
            }
        })

        buttonJump = Image(Assets.btUp)
        buttonJump.setSize(Settings.buttonSize, Settings.buttonSize)
        buttonJump.setPosition(Settings.buttonJumpPositionX, Settings.buttonJumpPositionY)
        buttonJump.getColor().a = .5f
        addPressEffect(buttonJump)
        buttonJump.addListener(object : DragListener() {
            override fun drag(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                stage!!.camera.unproject(dragPoint.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
                buttonJump.setPosition(dragPoint.x - buttonJump.getWidth() / 2f, dragPoint.y - buttonJump.getHeight() / 2f)
            }

            override fun dragStop(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                Settings.saveNewButtonJumpSettings(buttonJump.getX(), buttonJump.getY(), buttonJump.getWidth())
            }
        })

        buttonFire = Image(Assets.btFire)
        buttonFire.setSize(Settings.buttonSize, Settings.buttonSize)
        buttonFire.setPosition(Settings.buttonFirePositionX, Settings.buttonFirePositionY)
        buttonFire.getColor().a = .5f
        addPressEffect(buttonFire)
        buttonFire.addListener(object : DragListener() {
            override fun drag(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                stage!!.camera.unproject(dragPoint.set(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
                buttonFire.setPosition(dragPoint.x - buttonFire.getWidth() / 2f, dragPoint.y - buttonFire.getHeight() / 2f)
            }

            override fun dragStop(event: InputEvent?, x: Float, y: Float, pointer: Int) {
                Settings.saveNewButtonFireSettings(buttonFire.getX(), buttonFire.getY(), buttonFire.getWidth())
            }
        })

        buttonMenu = Button(Assets.btMenu)
        buttonMenu.setSize(45f, 45f)
        buttonMenu.setPosition((SCREEN_WIDTH - 50).toFloat(), (SCREEN_HEIGHT - 50).toFloat())
        addPressEffect(buttonMenu)
        buttonMenu.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                changeScreenWithFadeOut(MainMenuScreen::class.java, game)
            }
        })

        if (Settings.isPadEnabled) stage!!.addActor(pad)
        else stage!!.addActor(touchPadControls)
        stage!!.addActor(buttonJump)
        stage!!.addActor(buttonFire)
        stage!!.addActor(tableSizes)
        stage!!.addActor(buttonMenu)
    }

    override fun update(delta: Float) {
    }

    override fun draw(delta: Float) {
        batcher!!.begin()
        batcher!!.draw(Assets.background, 0f, 0f, SCREEN_WIDTH.toFloat(), SCREEN_HEIGHT.toFloat())
        batcher!!.draw(Assets.moon, 450f, 220f, 350f, 255f)

        batcher!!.end()
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK) {
            changeScreenWithFadeOut(MainMenuScreen::class.java, game!!)
            return true
        }
        return false
    }

    companion object {
        const val DEFAULT_SIZE_PAD: Int = 170

        @JvmField
        val DEFAULT_POSITION_PAD: Vector2 = Vector2(10f, 10f)

        const val DEFAULT_SIZE_BUTTONS: Int = 80

        @JvmField
        val DEFAULT_POSITION_BUTTON_JUMP: Vector2 = Vector2(560f, 20f)

        @JvmField
        val DEFAULT_POSITION_BUTTON_FIRE: Vector2 = Vector2(680f, 20f)
    }
}
