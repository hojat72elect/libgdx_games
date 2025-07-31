package com.nopalsoft.zombiekiller.scene2d

import com.badlogic.gdx.scenes.scene2d.Group
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.I18NBundle
import com.nopalsoft.zombiekiller.Assets
import com.nopalsoft.zombiekiller.game.GameScreen
import com.nopalsoft.zombiekiller.screens.Screens

class OverlayTutorial(gameScreen: GameScreen) : Group() {
    @JvmField
    var isVisible: Boolean = false
    var currentDialog: LabelDialog? = null
    var helpPad: LabelDialog
    var helpJump: LabelDialog
    var helpFire: LabelDialog
    var helpLifeBar: LabelDialog
    var helpShieldBar: LabelDialog
    var helpCollectSkulls: LabelDialog
    var helpCollectGems: LabelDialog
    var gameScreen: GameScreen?
    var numDialogShown: Int = 0

    var languagesBundle: I18NBundle

    init {
        this.gameScreen = gameScreen
        setSize(Screens.SCREEN_WIDTH.toFloat(), Screens.SCREEN_HEIGHT.toFloat())
        setBackground()
        languagesBundle = gameScreen.game!!.idiomas!!

        addListener(object : InputListener() {
            override fun touchDown(event: InputEvent?, x: Float, y: Float, pointer: Int, button: Int): Boolean {
                showHelpDialog()
                return true
            }
        })

        helpPad = LabelDialog(languagesBundle.get("help_pad"), false)
        helpPad.sizeBy(-50f, 10f)
        helpPad.setPosition(gameScreen.touchpad.getX() + gameScreen.touchpad.getWidth() / 2f, gameScreen.touchpad.getY() + gameScreen.touchpad.getHeight() / 2f)

        helpJump = LabelDialog(languagesBundle.get("help_jump"), false)
        helpJump.sizeBy(0f, 10f)
        helpJump.setPosition(gameScreen.buttonJump.getX() + gameScreen.buttonJump.getWidth() / 2f, gameScreen.buttonJump.getY() + gameScreen.buttonJump.getHeight() / 2f)

        helpFire = LabelDialog(languagesBundle.get("help_fire"), false)
        helpFire.sizeBy(0f, 10f)
        helpFire.setPosition(gameScreen.buttonFire.getX() + gameScreen.buttonFire.getWidth() / 2f, gameScreen.buttonFire.getY() + gameScreen.buttonFire.getHeight() / 2f)

        helpLifeBar = LabelDialog(languagesBundle.get("help_life"), true)
        helpLifeBar.sizeBy(-100f, 15f)
        helpLifeBar.setPosition(100f, 340f)

        helpShieldBar = LabelDialog(languagesBundle.get("help_shield"), true)
        helpShieldBar.sizeBy(-50f, 15f)
        helpShieldBar.setPosition(100f, 295f)

        helpCollectSkulls = LabelDialog(languagesBundle.get("help_collect_skulls"), false)
        helpCollectSkulls.sizeBy(-300f, 40f)
        helpCollectSkulls.setPosition(465f, 290f)

        helpCollectGems = LabelDialog(languagesBundle.get("help_collect_gems"), false)
        helpCollectGems.sizeBy(-570f, 60f)
        helpCollectGems.setPosition(250f, 220f)

        showHelpDialog()
    }

    private fun showHelpDialog() {
        if (currentDialog != null) currentDialog!!.remove()

        when (numDialogShown) {
            0 -> currentDialog = helpPad
            1 -> currentDialog = helpJump
            2 -> currentDialog = helpFire
            3 -> currentDialog = helpLifeBar
            4 -> currentDialog = helpShieldBar
            5 -> currentDialog = helpCollectSkulls
            6 -> currentDialog = helpCollectGems
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
