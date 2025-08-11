package com.nopalsoft.lander.dialogs

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.SizeToAction
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Window
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.nopalsoft.lander.Assets
import com.nopalsoft.lander.MainLander
import com.nopalsoft.lander.Settings
import com.nopalsoft.lander.screens.Screens

/**
 * I used the Window class because I had to put the little mark on it.
 */
class VentanaShop(var game: MainLander?) : Window("", Assets.styleDialogGameOver) {


    var arrVida: Array<Image?>
    var arrVelocidadY: Array<Image?>
    var arrRotacion: Array<Image?>
    var arrGas: Array<Image?>
    var arrPower: Array<Image?> // It doesn't upgrade anything, it only serves to take money.
    var arrOtro1: Array<Image?> // It doesn't upgrade anything, it only serves to take money.

    var btUpVida: ImageButton? = null
    var btUpVelocidadY: ImageButton? = null
    var btUpGas: ImageButton? = null
    var btUpRotacion: ImageButton? = null
    var btUpPower: ImageButton? = null
    var btUpOtro1: ImageButton? = null

    var btMenu: ImageButton? = null
    var ignoreTouchDown: InputListener = object : InputListener() {
        override fun touchDown(event: InputEvent, x: Float, y: Float, pointer: Int, button: Int): Boolean {
            event.cancel()
            return false
        }
    }

    init {
        this.setMovable(false)

        arrVida = arrayOfNulls<Image>(NIVEL_MAX_UPGRADES)
        arrVelocidadY = arrayOfNulls<Image>(NIVEL_MAX_UPGRADES)
        arrRotacion = arrayOfNulls<Image>(NIVEL_MAX_UPGRADES)
        arrGas = arrayOfNulls<Image>(NIVEL_MAX_UPGRADES)
        arrPower = arrayOfNulls<Image>(NIVEL_MAX_UPGRADES)
        arrOtro1 = arrayOfNulls<Image>(NIVEL_MAX_UPGRADES)

        initBotonesMenu()
        initBotonesUpgrades()

        val upgrades = Label("Upgrades", Assets.styleLabelMediana)

        this.row().padTop(25f)
        this.add<Label?>(upgrades).colspan(4)

        // Speed
        var auxTab = Table()
        auxTab.defaults().pad(1.15f)
        for (i in 0..<NIVEL_MAX_UPGRADES) {
            arrVelocidadY[i] = Image(Assets.upgradeOff)
            auxTab.add<Image?>(arrVelocidadY[i]).width(14f).height(18f)
        }
        this.row().padTop(20f)
        this.add<Label?>(Label("Thrusters", Assets.styleLabelMediana))
        this.add<Table?>(auxTab)
        this.add<ImageButton?>(btUpVelocidadY)
        this.add<Label?>(Label("$100", Assets.styleLabelMediana))

        // Life
        auxTab = Table()
        auxTab.defaults().pad(1.15f)
        for (i in 0..<NIVEL_MAX_UPGRADES) {
            arrVida[i] = Image(Assets.upgradeOff)
            auxTab.add<Image?>(arrVida[i]).width(14f).height(18f)
        }
        this.row().padTop(20f)
        this.add<Label?>(Label("Life", Assets.styleLabelMediana))
        this.add<Table?>(auxTab)
        this.add<ImageButton?>(btUpVida)
        this.add<Label?>(Label("$100", Assets.styleLabelMediana))

        // Gas
        auxTab = Table()
        auxTab.defaults().pad(1.15f)
        for (i in 0..<NIVEL_MAX_UPGRADES) {
            arrGas[i] = Image(Assets.upgradeOff)
            auxTab.add<Image?>(arrGas[i]).width(14f).height(18f)
        }
        this.row().padTop(20f)
        this.add<Label?>(Label("Gas", Assets.styleLabelMediana))
        this.add<Table?>(auxTab)
        this.add<ImageButton?>(btUpGas)
        this.add<Label?>(Label("$100", Assets.styleLabelMediana))

        // Rotation
        auxTab = Table()
        auxTab.defaults().pad(1.15f)
        for (i in 0..<NIVEL_MAX_UPGRADES) {
            arrRotacion[i] = Image(Assets.upgradeOff)
            auxTab.add<Image?>(arrRotacion[i]).width(14f).height(18f)
        }
        this.row().padTop(20f)
        this.add<Label?>(Label("Rotacion", Assets.styleLabelMediana))
        this.add<Table?>(auxTab)
        this.add<ImageButton?>(btUpRotacion)
        this.add<Label?>(Label("$100", Assets.styleLabelMediana))

        // Power
        auxTab = Table()
        auxTab.defaults().pad(1.15f)
        for (i in 0..<NIVEL_MAX_UPGRADES) {
            arrPower[i] = Image(Assets.upgradeOff)
            auxTab.add<Image?>(arrPower[i]).width(14f).height(18f)
        }
        this.row().padTop(20f)
        this.add<Label?>(Label("Power", Assets.styleLabelMediana))
        this.add<Table?>(auxTab)
        this.add<ImageButton?>(btUpPower)
        this.add<Label?>(Label("$100", Assets.styleLabelMediana))

        // Other1
        auxTab = Table()
        auxTab.defaults().pad(1.15f)
        for (i in 0..<NIVEL_MAX_UPGRADES) {
            arrOtro1[i] = Image(Assets.upgradeOff)
            auxTab.add<Image?>(arrOtro1[i]).width(14f).height(18f)
        }
        this.row().padTop(20f)
        this.add<Label?>(Label("Otroo", Assets.styleLabelMediana))
        this.add<Table?>(auxTab)
        this.add<ImageButton?>(btUpOtro1)
        this.add<Label?>(Label("$100", Assets.styleLabelMediana))

        // Menu
        this.row().padTop(30f).expandX()
        this.add<ImageButton?>(btMenu).colspan(4)

        this.row().expandX()
        this.add<Label?>(Label("Menu", Assets.styleLabelMediana)).colspan(4)

        setArrays()
    }

    private fun initBotonesUpgrades() {
        btUpVelocidadY = ImageButton(Assets.styleImageButtonUpgradePlus)
        btUpVelocidadY!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.nivelVelocidadY++
                setArrays()
            }
        })

        btUpVida = ImageButton(Assets.styleImageButtonUpgradePlus)
        btUpVida!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.nivelVida++
                setArrays()
            }
        })

        btUpGas = ImageButton(Assets.styleImageButtonUpgradePlus)
        btUpGas!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.nivelGas++
                setArrays()
            }
        })

        btUpRotacion = ImageButton(Assets.styleImageButtonUpgradePlus)
        btUpRotacion!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.nivelRotacion++
                setArrays()
            }
        })

        btUpPower = ImageButton(Assets.styleImageButtonUpgradePlus)
        btUpPower!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.nivelPower++
                setArrays()
            }
        })

        btUpOtro1 = ImageButton(Assets.styleImageButtonUpgradePlus)
        btUpOtro1!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                Settings.nivelOtro1++
                setArrays()
            }
        })
    }

    private fun initBotonesMenu() {
        btMenu = ImageButton(Assets.styleImageButtonPause)
        btMenu!!.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                hide()
            }
        })
    }

    private fun setArrays() {
        // Rotation
        for (i in 0..<Settings.nivelRotacion) {
            arrRotacion[i]!!.setDrawable(TextureRegionDrawable(Assets.upgradeOn))
        }
        if (Settings.nivelRotacion >= NIVEL_MAX_UPGRADES) {
            btUpRotacion!!.isVisible = false
        }

        // SpeedY
        for (i in 0..<Settings.nivelVelocidadY) {
            arrVelocidadY[i]!!.setDrawable(TextureRegionDrawable(Assets.upgradeOn))
        }
        if (Settings.nivelVelocidadY >= NIVEL_MAX_UPGRADES) {
            btUpVelocidadY!!.isVisible = false
        }

        // Life
        for (i in 0..<Settings.nivelVida) {
            arrVida[i]!!.setDrawable(TextureRegionDrawable(Assets.upgradeOn))
        }
        if (Settings.nivelVida >= NIVEL_MAX_UPGRADES) {
            btUpVida!!.isVisible = false
        }

        // Gas
        for (i in 0..<Settings.nivelGas) {
            arrGas[i]!!.setDrawable(TextureRegionDrawable(Assets.upgradeOn))
        }
        if (Settings.nivelGas >= NIVEL_MAX_UPGRADES) {
            btUpGas!!.isVisible = false
        }

        // Power
        for (i in 0..<Settings.nivelPower) {
            arrPower[i]!!.setDrawable(TextureRegionDrawable(Assets.upgradeOn))
        }
        if (Settings.nivelPower >= NIVEL_MAX_UPGRADES) {
            btUpPower!!.isVisible = false
        }

        // Other1
        for (i in 0..<Settings.nivelOtro1) {
            arrOtro1[i]!!.setDrawable(TextureRegionDrawable(Assets.upgradeOn))
        }
        if (Settings.nivelOtro1 >= NIVEL_MAX_UPGRADES) {
            btUpOtro1!!.isVisible = false
        }
    }

    fun show(stage: Stage) {
        this.pack()

        setSize(Screens.SCREEN_WIDTH.toFloat(), 0f)

        val sizeAction = Actions.action(SizeToAction::class.java)
        sizeAction.setSize(Screens.SCREEN_WIDTH.toFloat(), 600f) // ALTURA FINAL
        sizeAction.duration = .25f

        setPosition(Screens.SCREEN_WIDTH / 2f - getWidth() / 2f, Screens.SCREEN_HEIGHT / 2f - 600 / 2f) // 500 ALTURA FINAL
        addAction(sizeAction)

        stage.addActor(this)
        getColor().a = 0f
        addAction(Actions.fadeIn(FADE_DURATION, Interpolation.fade))
    }

    fun hide() {
        addCaptureListener(ignoreTouchDown)

        val sizeAction = Actions.action(SizeToAction::class.java)
        sizeAction.setSize(Screens.SCREEN_WIDTH.toFloat(), 0f) // ALTURA FINAL
        sizeAction.duration = .25f
        addAction(Actions.sequence(Actions.parallel(Actions.fadeOut(FADE_DURATION, Interpolation.fade), sizeAction), Actions.removeListener(ignoreTouchDown, true), Actions.removeActor()))
    }

    companion object {
        private const val FADE_DURATION = 0.4f
        private const val NIVEL_MAX_UPGRADES = 15
    }
}
