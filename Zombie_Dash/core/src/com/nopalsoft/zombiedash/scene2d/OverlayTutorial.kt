package com.nopalsoft.zombiedash.scene2d

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.I18NBundle
import com.nopalsoft.zombiedash.Assets
import com.nopalsoft.zombiedash.game.GameScreen
import com.nopalsoft.zombiedash.screens.Screens

class OverlayTutorial(gameScreen: GameScreen) : Group() {
    @JvmField
    var isVisible: Boolean = false
    var currentDialog: LabelDialog? = null
    var helpJump: LabelDialog
    var helpFire: LabelDialog
    var helpLifeBar: LabelDialog
    var helpShieldBar: LabelDialog
    var helpAmmo: LabelDialog
    var helpCollectGems: LabelDialog
    var numDialogShown: Int = 0

    var idiomas: I18NBundle

    init {
        setSize(Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat())
        setBackground()
        idiomas = gameScreen.game!!.idiomas!!

        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                showHelpDialog()
                return true
            }
        })

        helpJump = LabelDialog(idiomas.get("help_jump"), false)
        helpJump.sizeBy(0f, 10f)
        helpJump.setPosition(
            gameScreen.btJump.getX() + gameScreen.btJump.getWidth() / 2f, gameScreen.btJump.getY() + gameScreen.btJump.getHeight()
                    / 2f
        )

        helpFire = LabelDialog(idiomas.get("help_fire"), false)
        helpFire.sizeBy(0f, 10f)
        helpFire.setPosition(
            gameScreen.btFire.getX() + gameScreen.btFire.getWidth() / 2f, gameScreen.btFire.getY() + gameScreen.btFire.getHeight()
                    / 2f
        )

        helpLifeBar = LabelDialog(idiomas.get("help_life"), true)
        helpLifeBar.sizeBy(-100f, 15f)
        helpLifeBar.setPosition(100f, 340f)

        helpShieldBar = LabelDialog(idiomas.get("help_shield"), true)
        helpShieldBar.sizeBy(-50f, 15f)
        helpShieldBar.setPosition(100f, 295f)

        helpCollectGems = LabelDialog(idiomas.get("help_collect_gems"), true)
        helpCollectGems.sizeBy(-50f, 15f)
        helpCollectGems.setPosition(60f, 200f)

        helpAmmo = LabelDialog(idiomas.get("help_ammo"), true)
        helpAmmo.sizeBy(-50f, 15f)
        helpAmmo.setPosition(70f, 200f)

        showHelpDialog()
    }

    private fun showHelpDialog() {
        if (currentDialog != null) currentDialog!!.remove()

        when (numDialogShown) {
            0 -> currentDialog = helpJump
            1 -> currentDialog = helpFire
            2 -> currentDialog = helpLifeBar
            3 -> currentDialog = helpShieldBar
            4 -> currentDialog = helpCollectGems
            5 -> currentDialog = helpAmmo
            else -> hide()
        }
        numDialogShown++
        addActor(currentDialog)
    }

    fun show(stage: Stage) {
        stage.addActor(this)
        isVisible = true
    }

    fun hide() {
        isVisible = false
        remove()
    }

    private fun setBackground() {
        val img = Image(Assets.pixelNegro)
        img.setSize(getWidth(), getHeight())
        img.getColor().a = .4f
        addActor(img)
    }
}
