package com.nopalsoft.sokoban.scene2d

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.nopalsoft.sokoban.Assets
import com.nopalsoft.sokoban.Settings
import com.nopalsoft.sokoban.game.GameScreen
import com.nopalsoft.sokoban.screens.MainMenuScreen
import com.nopalsoft.sokoban.screens.Screens

/**
 * The pause dialog that can be shown during the game.
 * It shows 3 buttons : 1- Home, 2- Refresh, 3- Toggle Animation.
 * After toggling the animation, this setting will be saved.
 */
class DialogPause(currentScreen: Screens):Dialog(currentScreen, 350F, 300F, 100F) {

   private val buttonHome = Button(Assets.btHome, Assets.btHomePress)
   private val buttonRefresh =  Button(Assets.btRefresh, Assets.btRefreshPress)
   private val tableAnimations= Table()

    init {
        setCloseButton()
        setTitle("Paused", 1F)

        val tableMenu = Table()
        tableMenu.setFillParent(true)

        buttonHome.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                screen.changeScreenWithFadeOut(MainMenuScreen::class.java, screen.game)
            }
        })
        buttonRefresh.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                screen.changeScreenWithFadeOut(GameScreen::class.java, (screen as GameScreen).level, screen.game)
            }
        })

        val btAnimations = Button(Assets.btOff, Assets.btOn, Assets.btOn)
        btAnimations.isChecked = Settings.animationWalkIsON

        tableAnimations.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent, x: Float, y: Float) {
                Settings.animationWalkIsON = !Settings.animationWalkIsON
                btAnimations.isChecked = Settings.animationWalkIsON
                Settings.save()
            }
        })

        tableMenu.defaults().expandX()
        tableMenu.pad(30f).padTop(55f)
        tableMenu.add(buttonHome)
        tableMenu.add(buttonRefresh)
        tableMenu.row()

        val labelAnimations = Label("Animations", LabelStyle(Assets.fontRed, Color.WHITE))
        tableAnimations.add(labelAnimations)
        tableAnimations.add(btAnimations).padLeft(15f)

        tableMenu.add(tableAnimations).colspan(2).padTop(10f)
        addActor(tableMenu)
    }

    /**
     * When this dialog is shown, the game is paused. So, when this dialog is closed, the game will resume running.
     */
    override fun hideCompleted() {
        (screen as GameScreen).setRunning()
    }
}